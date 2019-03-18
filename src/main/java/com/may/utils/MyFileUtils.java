package com.may.utils;

import com.may.enums.ResultEnum;
import com.may.exception.MyException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ClassName : MyFileUtils
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 文件工具类
 */
public class MyFileUtils extends FileUtils {
    private static Logger logger = LogManager.getLogger("utils.FileUtils");


    public static void closeQuietly(OutputStream out) {
        closeQuietly((Closeable) out);
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
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean delDir(String sPath) {
        boolean flag = true;
        sPath = addSeparator(sPath);
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            logger.error("删除目录失败：目录不存在--" + sPath);
            return false;
        }
        File[] fileList = listDirFiles(sPath);
        if (null != fileList) {
            for (File file : fileList) {
                if (file.isFile()) {
                    flag = delFile(file.getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                } else {
                    flag = delDir(file.getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }
            logger.debug("删除目录成功>>>" + sPath);
        }
        return flag ? dirFile.delete() : flag;
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
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                flag = true;
                logger.debug("删除文件成功>>>" + sPath);
            } else {
                logger.error("删除文件失败：目标非文件，不能直接删除--" + sPath);
            }
        } else {
            logger.error("删除文件失败：文件不存在--" + sPath);
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
     * 下载远程文件到本地-利用common-io工具类
     *
     * @param url 远程文件下载地址URL
     * @param localPath 保存到本的文件路径
     */
    public static void downRemoteFile(String url, String localPath) throws Exception {
        String opt = "下载远程文件到本地，";
        if (StringUtils.isBlank(url) || StringUtils.isBlank(localPath)) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        File localFile=new File(localPath);
        logger.info(opt+"开始，url:"+url);
        copyURLToFile(new URL(url), localFile ,60000,60000);
        logger.info(opt+"url:"+url+"，文件大小："+formatFileSize(localPath));
    }

    /**
     * 获取请求URL返回的conten-length
     *
     * @param url
     * @param retryTime     当前重试次数
     * @param bigTime       最大重试次数
     * @param totalSize     content-length值
     * @return
     */
    public static long getContentLen(String url, int retryTime, int bigTime, long totalSize) {
        try {
            if (totalSize > 0) {
                return totalSize;
            }
            if (retryTime > bigTime) {
                logger.error("请求(" + url + ")已重试" + retryTime + "次，放弃重试");
                return totalSize;
            }
            if (retryTime > 1) {
                //根据重试次数延迟请求
                Thread.sleep(retryTime * 1000 * 60);
            }
            URL httpUrl = new URL(url);
            HttpURLConnection urlc = (HttpURLConnection) httpUrl.openConnection();
            logger.info("URL(" + url + ")请求返回：Content-Length="
                    + urlc.getHeaderField("Content-Length"));
            if (StringUtils.isNotBlank(urlc.getHeaderField("Content-Length"))) {
                totalSize = Long.parseLong(urlc.getHeaderField("Content-Length"));
            }
            urlc.disconnect(); // 先断开，下面再连接，否则下面会报已经连接的错误
            if (totalSize == 0) {
                retryTime++;
                totalSize = getContentLen(url, retryTime, bigTime, totalSize);
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
    public static String addSeparator(String oldPath) {
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

    public static void main(String[] args) {
        delFile("D:\\data\\3");
        delFile("D:\\data\\1.sql");
    }

    /**
     * 获取文件大小
     *
     * @param pathAndFile
     * @return
     */
    public static long getFileLength(String pathAndFile) {
        return sizeOf(new File(pathAndFile));
    }

    /**
     * 获取文件大小，带单位
     *
     * @param filePath
     * @return 文件大小字符串，如：112B、32KB、56MB、102GB
     * @throws Exception
     */
    public static String formatFileSize(String filePath) {
        return formatFileSize(getFileLength(filePath));
    }
    /**
     * 格式化文件大小
     *
     * @param fz 文件大小（字节数）36389319
     * @return 文件大小字符串，如：112B、32KB、56MB、102GB
     * @throws Exception
     */
    public static String formatFileSize(long fz) {
        String fileSize = fz + "B";
        double fzd = fz;
        DecimalFormat df = new DecimalFormat("#.00");
        int base=1024;
        if (fzd/base>0) {
            fzd = fzd/base;
            fileSize = df.format(fzd) + "KB";
        }
        if (fzd/base>0) {
            fzd = fzd/base;
            fileSize = df.format(fzd) + "MB";
        }
        if (fzd/base>0) {
            fzd = fzd/base;
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
    public static boolean renameFile(String oldFileNamePath, String newFileNamePath, boolean delOrg) {
        boolean flag=false;
        if (oldFileNamePath.equals(newFileNamePath)) {
            logger.error("重命名文件(" + oldFileNamePath + ")，原文件与新文件路径名称相同，不能重命名");
            return flag;
        }
        //如果原文件不存在直接返回
        if (!checkFileExist(oldFileNamePath)) {
            logger.error("重命名文件(" + oldFileNamePath + ")，原文件不存在");
            return flag;
        }
        delFile(newFileNamePath);
        File file = new File(oldFileNamePath);

        flag= file.renameTo(new File(newFileNamePath));
        if (delOrg) {
            file.delete();
        }
        return flag;
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
     * @param fileList 要合并的文件列表
     * @param outPath  输出文件路径
     * @param ext      文件扩展名
     */
    public static void mergeFilesByNIO(List<File> fileList, String outPath, String ext) throws MyException {
        SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTime = fd.format(new Date());
        FileInputStream in = null;
        FileOutputStream out = null;
        File fout = new File(outPath + File.separator + nowTime + ext);
        try {
            out = new FileOutputStream(fout, true);
            FileChannel resultFileChannel = out.getChannel();
            for (File file : fileList) {
                in = new FileInputStream(file);
                FileChannel blk = in.getChannel();
                resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
                blk.close();
                in.close();
            }
            resultFileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("利用nio FileChannel合并多个文件，异常：", e);
        } finally {
            closeStream(in, out);
        }
    }

    /**
     * 上传文件
     *
     * @param uploadFile
     * @return
     */
    public static String uploadFile(File uploadFile) throws MyException {
        FileInputStream in = null;
        BufferedOutputStream out = null;
        String result;
        try {
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
            SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
            String dir1 = df1.format(new Date());
            String dir2 = df2.format(new Date());
            String cPath = dir1 + "/" + dir2 + "/";
            String localFilePath = ConstUtil.FILE_PATH_UPLOAD + cPath;
            File fs = new File(localFilePath);
            if (!fs.exists()) {
                fs.mkdirs();
            }
            String fName = CommonUtil.getUUID() + getFileExt(uploadFile.getName(), true);
            result = localFilePath + fName;
            logger.info("上传文件：" + result);
            in = new FileInputStream(uploadFile);
            out = new BufferedOutputStream(new FileOutputStream(result, true));
            int len = 2048;
            byte[] b = new byte[len];
            boolean success = false;
            while ((len = in.read(b)) != -1) {
                success = true;
                out.write(b, 0, len);
            }
            out.flush();
            if (!success) {
                result = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("上传文件异常：", e);
            throw new MyException("上传文件异常：" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("上传文件异常：", e);
            throw new MyException("上传文件异常：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传文件异常：", e);
            throw new MyException("上传文件异常：" + e.getMessage());
        } finally {
            closeStream(in, out);
        }
        return result;
    }

    /**
     * 关闭文件流
     *
     * @param in  FileInputStream
     * @param out FileOutputStream
     * @throws MyException
     */
    private static void closeStream(FileInputStream in, FileOutputStream out) throws MyException {
        closeStream(in, out, null, null, null, null);
    }

    /**
     * 关闭文件流
     *
     * @param in  FileInputStream
     * @param bos BufferedOutputStream
     * @throws MyException
     */
    private static void closeStream(FileInputStream in, BufferedOutputStream bos) throws MyException {
        closeStream(in, null, null, null, bos, null);
    }

    /**
     * 关闭文件流
     *
     * @param dos    DataOutputStream
     * @param bis    BufferedInputStream
     * @param out    FileOutputStream
     * @param raFile RandomAccessFile
     * @throws MyException
     */
    private static void closeStream(DataOutputStream dos, BufferedInputStream bis, FileOutputStream out
            , RandomAccessFile raFile) throws MyException {
        closeStream(null, out, dos, bis, null, raFile);

    }

    /**
     * 关闭文件流
     *
     * @param in     FileInputStream
     * @param out    FileOutputStream
     * @param dos    DataOutputStream
     * @param bis    BufferedInputStream
     * @param bos    BufferedOutputStream
     * @param raFile RandomAccessFile
     * @throws MyException
     */
    private static void closeStream(FileInputStream in, FileOutputStream out
            , DataOutputStream dos, BufferedInputStream bis, BufferedOutputStream bos
            , RandomAccessFile raFile) throws MyException {
        try {
            if (null != in) {
                in.close();
            }
            if (null != out) {
                out.close();
            }
            if (null != dos) {
                dos.close();
            }
            if (null != bis) {
                bis.close();
            }
            if (null != bos) {
                bos.close();
            }
            if (null != raFile) {
                raFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("关闭文件流异常：", e);
            throw new MyException("关闭文件流异常：" + e.getMessage());
        }
    }
}
