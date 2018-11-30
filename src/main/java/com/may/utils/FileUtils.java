package com.may.utils;

import com.may.enums.ResultEnum;
import com.may.exception.MyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wgj on 2018/5/31.
 * 文件下载工具类
 */
public class FileUtils {
    private static Logger logger = LogManager.getLogger("utils.FileUtils");



    public static void touch(File file) throws IOException {
        if (!file.exists()) {
            OutputStream out = openOutputStream(file);
            closeQuietly(out);
        }
        boolean success = file.setLastModified(System.currentTimeMillis());
        if (!success) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    public static String readFileAsString(File file, String encoding) {
        InputStream is = null;
        StringWriter result = null;
        PrintWriter out = null;
        BufferedReader reader = null;
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is, encoding));
            String line = null;
            result = new StringWriter();
            out = new PrintWriter(result);
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
            String str = result.toString();
            return str;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(reader);
            closeQuietly(result);
            closeQuietly(out);
            closeQuietly(is);
        }
    }

    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable) output);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * 获取文件的扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean delDir(String sPath) {
        boolean flag = true;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = delFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = delDir(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag){
            return false;
        }
        // 删除当前目录
        return dirFile.delete();
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean delFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 下载文件到客户端
     *
     * @param filePath 服务器端文件路径
     * @param response
     * @param delFlag  是否删除服务器端文件
     */
    public static void downFileToClient(String filePath, String fileName, HttpServletResponse response
            , boolean delFlag) {
        String opt = "下载文件到客户端，";
        File downFile = null;
        try {
            logger.debug(opt + "文件路径：" + filePath);
            if (StringUtils.isBlank(filePath)) {
                throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
            }
            logger.debug(opt + "start.");
            downFile = new File(filePath);
            fileName = (StringUtils.isBlank(fileName)) ? filePath.substring(filePath.lastIndexOf("/")
                    + 1, filePath.length()) : fileName;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downFile));
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
//			response.setContentType("application/octet-stream");
            FileOutputStream out = new FileOutputStream(downFile);
            int data = -1;
            while ((data = bis.read()) != -1) {
                out.write(data);
            }
            bis.close();
            out.flush();
            out.close();
            logger.debug(opt + "end.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(opt + "异常：", e);
        } finally {
            if (downFile != null && delFlag) {
                downFile.delete();
            }
        }

    }

    /**
     * 下载远程文件到本地
     *
     * @param remoteFileUrl 远程文件下载地址URL
     * @param localFilePath 保存到本的文件路径
     */
    public static void downRemoteFile(String remoteFileUrl, String localFilePath) throws Exception {
        String opt = "下载远程文件到本地，";
        DataOutputStream dos = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        String localFile_tmp = localFilePath + ".tp "; // 未下载完文件加.tp扩展名，以便于区别
        long start = System.currentTimeMillis();
        byte[] buffer = new byte[1024];
        RandomAccessFile raFile = null;
        long totalSize = 0; // 要下载的文件总大小
        try {
            int len;
            if (StringUtils.isBlank(remoteFileUrl) || StringUtils.isBlank(localFilePath)) {
                throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
            }
            totalSize = getURLContentLength(remoteFileUrl, 0, 9, totalSize);
            logger.info(opt + "下载文件大小为: " + totalSize + "b=" + (totalSize / 1024 / 1024) + "mb");
            URL url = new URL(remoteFileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 确定文件是否存在
            if (checkFileExist(localFile_tmp)) {
                // 采用断点续传，这里的依据是看下载文件是否在本地有.tp有扩展名同名文件
                long fileSize = getFileLength(localFile_tmp); // 取得文件在小，以便确定随机写入的位置
                logger.info(opt + "文件(" + localFile_tmp + ")续传中…FileSize: " + (fileSize / 1024 / 1024) + "mb");
                // 设置User-Agent
                // urlc.setRequestProperty("User-Agent","NetFox");
                // 设置断点续传的开始位置
                connection.setRequestProperty("RANGE ", " bytes= " + fileSize + " - ");
                // urlc.setRequestProperty("RANGE", "bytes="+fileSize); //
                // 这样写不行，不能少了这个"-".
                // 设置接受信息
                connection.setRequestProperty("Accept ", " image/gif,image/x-xbitmap,application/msword,*/*");
                raFile = new RandomAccessFile(localFile_tmp, "rw"); // 随机方位读取
                raFile.seek(fileSize); // 定位指针到fileSize位置
                bis = new BufferedInputStream(connection.getInputStream());
                while ((len = bis.read(buffer)) > 0) {
                    // 循环获取文件
                    raFile.write(buffer, 0, len);
                    // buffer=buffer+bt;
                }
                logger.info(opt + "文件(" + localFile_tmp + ")续传接收完毕！ ");
            } else {
                // 采用原始下载
                // new File(localFile_bak);
                fos = new FileOutputStream(localFile_tmp); // 没有下载完毕就将文件的扩展名命名.bak
                dos = new DataOutputStream(fos);
                bis = new BufferedInputStream(connection.getInputStream());
                logger.info(opt + "正在接收文件(" + localFile_tmp + ")… ");
                while ((len = bis.read(buffer)) > 0) // 循环获取文件
                {
                    dos.write(buffer, 0, len);
                }
                logger.info(opt + "文件(" + localFile_tmp + ")正常接收完毕！");
            }
            logger.info(opt + "共用时： " + (System.currentTimeMillis() - start) / 1000 + "秒");
            // 下载完毕后，将文件重命名
            renameFile(localFile_tmp, localFilePath, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (raFile != null) {
                    raFile.close();
                }
            } catch (Exception f) {
                f.printStackTrace();
                logger.error("关闭流异常：", f);
            }
        }
    }


    /**
     * 获取请求URL返回的conten-length
     *
     * @param remoteFileUrl
     * @param retryTime     当前重试次数
     * @param bigTime       最大重试次数
     * @param totalSize     content-length值
     * @return
     */
    public static long getURLContentLength(String remoteFileUrl, int retryTime, int bigTime, long totalSize) {
        try {
            if (totalSize > 0) {
                return totalSize;
            }
            if (retryTime > bigTime) {
                logger.error("请求(" + remoteFileUrl + ")已重试" + retryTime + "次，放弃重试");
                return totalSize;
            }
            if (retryTime > 1) {
                //根据重试次数延迟请求
                Thread.sleep(retryTime * 1000 * 60);
            }
            URL url = new URL(remoteFileUrl);
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            logger.info("URL(" + remoteFileUrl + ")请求返回：Content-Length="
                    + urlc.getHeaderField("Content-Length"));
            if (StringUtils.isNotBlank(urlc.getHeaderField("Content-Length"))) {
                totalSize = Long.parseLong(urlc.getHeaderField("Content-Length"));
            }
            urlc.disconnect(); // 先断开，下面再连接，否则下面会报已经连接的错误
            if (totalSize == 0) {
                retryTime++;
                totalSize = getURLContentLength(remoteFileUrl, retryTime, bigTime, totalSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取请求URL返回的conten-length异常：", e);
        }
        return totalSize;
    }

    /**
     * 给路径末尾追加/ 或者\
     *
     * @param oldPath
     * @return
     */
    public static String addFileSeparator(String oldPath) {
        if (!File.separator.equals(oldPath.substring(oldPath.length() - 1, oldPath.length()))) {
            oldPath += File.separator;
        }
        return oldPath;
    }

    /**
     * 获取文件夹中的所有文件
     *
     * @param sPath
     * @return
     */
    public static File[] listDirFiles(String sPath) {
        File[] files = null;
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (dirFile.exists() && dirFile.isDirectory()) {
            files = dirFile.listFiles();
        }
        return files;
    }

    /**
     * 坚持文件是否存在
     *
     * @param pathAndFile
     * @return
     */
    public static boolean checkFileExist(String pathAndFile) {
        return new File(pathAndFile).exists();
    }

    /**
     * 获取文件大小
     *
     * @param pathAndFile
     * @return
     */
    public static long getFileLength(String pathAndFile) {
        return new File(pathAndFile).length();
    }

    /**
     * 获取文件大小，带单位
     *
     * @param filePath
     * @return 文件大小字符串，如：112B、32KB、56MB、102GB
     * @throws Exception
     */
    public static String getFileSize(String filePath) throws Exception {
        String fileSize = "0B";
        File orgFile = new File(filePath);
        if (orgFile.exists()) {
            long fz = orgFile.length();
            fileSize = calcFileSize(fz);
        }
        return fileSize;
    }

    /**
     * 计算文件大小
     *
     * @param fz 文件大小（字节数）36389319
     * @return 文件大小字符串，如：112B、32KB、56MB、102GB
     * @throws Exception
     */
    public static String calcFileSize(long fz) {
        String fileSize = fz + "B";
        double fzd = fz;
        DecimalFormat df = new DecimalFormat("#.00");
        if (fzd > 1024) {
            fzd = fzd / 1024;
            fileSize = df.format(fzd) + "KB";
        }
        if (fzd > 1024) {
            fzd = fzd / 1024;
            fileSize = df.format(fzd) + "MB";
        }
        if (fzd > 1024) {
            fzd = fzd / 1024;
            fileSize = df.format(fzd) + "GB";
        }
        return fileSize;
    }

    /**
     * 文件重命名
     *
     * @param oldFileNamePath 原文件
     * @param newFileNamePath 新文件
     * @param delOrg          是否删除原文件
     */
    public static void renameFile(String oldFileNamePath, String newFileNamePath, boolean delOrg) {
        if (oldFileNamePath.equals(newFileNamePath)) {
            logger.error("重命名文件(" + oldFileNamePath + ")，原文件与新文件路径名称相同，不能重命名");
            return;
        }
        //如果原文件不存在直接返回
        if (!checkFileExist(oldFileNamePath)) {
            logger.error("重命名文件(" + oldFileNamePath + ")，原文件不存在");
            return;
        }
        //如果目标文件存在则删除
        if (checkFileExist(newFileNamePath)) {
            delFile(newFileNamePath);
        }
        File file = new File(oldFileNamePath);
        file.renameTo(new File(newFileNamePath));
        if (delOrg) {
            file.delete();
        }
    }

    /**
     * byte[]保存写入文件
     *
     * @param filePath
     * @param fileByte
     * @throws Exception
     */
    public static void writeByteToFile(String filePath, byte[] fileByte) throws Exception {
        File savePath = new File(filePath);
        touch(savePath);
        OutputStream output = new FileOutputStream(savePath);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        bufferedOutput.write(fileByte);
        bufferedOutput.close();
    }

    /**
     * 获取文件原名，不包含扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        } else {
            return fileName.substring(0, fileName.lastIndexOf(".")).toLowerCase();
        }
    }
    /**
     * 获取文件扩展名，不包含符号点 .
     *
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName) {
        return getFileExt(fileName, false);
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @param witDot   是否包符号 .
     * @return
     */
    public static String getFileExt(String filename, boolean witDot) {
        if (StringUtils.isNotBlank(filename)) {
            int dot = filename.lastIndexOf('.');
            if (!witDot) {
                dot++;
            }
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot);
            }
        }
        return filename;
    }


    /**
     * .使用普通IO进行文件合并
     *
     * @param fileList    要合并的文件列表
     * @param outFilePath 输出文件路径
     * @param ext         文件扩展名
     */
    public static void mergeFilesByIO(List<File> fileList, String outFilePath, String ext) {
        SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTime = fd.format(new Date());

        File fout = new File(outFilePath + File.separator + nowTime + ext);
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            outBuff = new BufferedOutputStream(new FileOutputStream(fout));
            FileInputStream finput = null;
            for (File file : fileList) {
                finput = new FileInputStream(file);
                inBuff = new BufferedInputStream(finput);
                byte[] b = new byte[finput.available()];
                int len;
                while ((len = inBuff.read(b)) != -1) {
                    outBuff.write(b, 0, len);
                }
                inBuff.close();
                outBuff.flush();
            }
            outBuff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用nio FileChannel合并多个文件
     *
     * @param fileList    要合并的文件列表
     * @param outFilePath 输出文件路径
     * @param ext         文件扩展名
     */
    public static void mergeFilesByNIO(List<File> fileList, String outFilePath, String ext) {
        SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTime = fd.format(new Date());

        File fout = new File(outFilePath + File.separator + nowTime + ext);
        try {
            FileOutputStream out = new FileOutputStream(fout, true);
            FileInputStream in = null;
            FileChannel resultFileChannel = out.getChannel();
            for (File file : fileList) {
                in = new FileInputStream(file);
                FileChannel blk = in.getChannel();
                resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
                blk.close();
                in.close();
            }
            resultFileChannel.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
