package com.lqc.util;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云 OSS工具类
 * 
 */
public class OSSClientUtil {
	private final static Logger log = LoggerFactory.getLogger(util.OSSClientUtil.class);

	
	
	private OSSClient ossClient;
 
	public OSSClientUtil() {
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(5000);
		conf.setMaxErrorRetry(3);
		DefaultCredentialProvider credentialsProvider = new  DefaultCredentialProvider(ConstUtil.oss_access_key_id.trim(), ConstUtil.oss_access_key_secret.trim());
		ossClient = new OSSClient(ConstUtil.oss_endpoint,credentialsProvider,conf);
	}
	
	
 
	/**
	 * 销毁
	 */
	public void destory() {
		ossClient.shutdown();
	}
 
	/**
	 * 获得图片路径
	 *
	 * @param fileUrl
	 * @return
	 */
	public String getImgUrl(String fileUrl) {
		if (!StringUtils.isEmpty(fileUrl)) {
			String[] split = fileUrl.split("/");
			return this.getUrl(ConstUtil.oss_file_dir + split[split.length - 1]);
		}
		return null;
	}
 
	/**
	 * 上传到OSS服务器 如果同名文件会覆盖服务器上的
	 *
	 * @param instream
	 *            文件流
	 * @param fileName
	 *            文件名称 包括后缀名
	 * @return 出错返回"" ,唯一MD5数字签名
	 */
//	public String uploadFile2OSS(File file, String fileName) {
////		String ret = "";
//		InputStream instream=null;
//		String path="";
//		try {
//			instream=new FileInputStream(file);
//			// 创建上传Object的Metadata
//			ObjectMetadata objectMetadata = new ObjectMetadata();
//			objectMetadata.setContentLength(file.length());
//			objectMetadata.setCacheControl("no-cache");
//			objectMetadata.setHeader("Pragma", "no-cache");
//			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
//			objectMetadata.setContentDisposition("inline;filename=" + fileName);
//			String dir=CommonUtil.mkFileDir(ConstUtil.oss_file_dir,fileName);
//			// 上传文件
//			PutObjectResult putResult = ossClient.putObject(ConstUtil.oss_bucket_name, dir, instream, objectMetadata);
////			ret = putResult.getETag();
//			path=getUrl(dir);
//		} catch (IOException e) {
//			log.error(e.getMessage(), e);
//		} finally {
//			try {
//				if (instream != null) {
//					instream.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			destory();
//		}
//		return ConstUtil.pic_server_show+path;
//	}
	
	/**
	 * 上传到OSS服务器 如果同名文件会覆盖服务器上的
	 *
	 * @param file
	 *            文件流
	 * @param fileName
	 *            文件名称 包括后缀名
	 * @return 出错返回"" ,唯一MD5数字签名
	 */
	public String uploadFile2OSSNew(File file, String filePath,String fileName) {
//		String ret = "";
		InputStream instream=null;
		String path="";
		try {
			instream=new FileInputStream(file);
			// 创建上传Object的Metadata
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.length());
			objectMetadata.setCacheControl("no-cache");
			objectMetadata.setHeader("Pragma", "no-cache");
			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
			objectMetadata.setContentDisposition("inline;filename=" + fileName);
			String dir=CommonUtil.mkFileDirByTime(filePath)+fileName;
			// 上传文件
			PutObjectResult putResult = ossClient.putObject(ConstUtil.oss_bucket_name, dir, instream, objectMetadata);
//			ret = putResult.getETag();
			path=getUrl(dir);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			destory();
		}
		return path;
	}
 
	/**
	 * Description: 判断OSS服务文件上传时文件的contentType
	 *
	 * @param filenameExtension
	 *            文件后缀
	 * @return String
	 */
	public static String getcontentType(String filenameExtension) {
		if (filenameExtension.equalsIgnoreCase("bmp")) {
			return "image/bmp";
		}
		if (filenameExtension.equalsIgnoreCase("gif")) {
			return "image/gif";
		}
		if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
				|| filenameExtension.equalsIgnoreCase("png")) {
			return "image/jpeg";
		}
		if (filenameExtension.equalsIgnoreCase("html")) {
			return "text/html";
		}
		if (filenameExtension.equalsIgnoreCase("txt")) {
			return "text/plain";
		}
		if (filenameExtension.equalsIgnoreCase("vsd")) {
			return "application/vnd.visio";
		}
		if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
			return "application/msword";
		}
		if (filenameExtension.equalsIgnoreCase("xml")) {
			return "text/xml";
		}
		return "image/jpeg";
	}
 
	/**
	 * 获得url链接
	 *
	 * @param key
	 * @return
	 */
	public String getUrl(String key) {
		// 设置URL过期时间为10年 3600l* 1000*24*365*10
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		// 生成URL
		URL url = ossClient.generatePresignedUrl(ConstUtil.oss_bucket_name, key, expiration);
		if (url != null) {
			String path=url.toString().split("\\?")[0];
			return path.replace("http://", "https://");
		}
		return null;
	}
	/**
	 * 临时授权1小时
	 * @param fileName
	 * @return
	 */
	public  URL authUplaodOSS(String fileName) {
		// 生成签名URL。
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 1);//1小时
		URL signedUrl=null;
		try {
		GeneratePresignedUrlRequest request = 
				new GeneratePresignedUrlRequest(ConstUtil.oss_bucket_name, CommonUtil.mkFileDir(ConstUtil.oss_file_dir)+fileName, HttpMethod.PUT);
		// 设置过期时间。
		request.setExpiration(expiration);
		// 设置Content-Type。
		request.setContentType("application/octet-stream");
		// 添加用户自定义元信息。
//		request.addUserMetadata("author", "aliy");
		// 生成签名URL（HTTP PUT请求）。
		signedUrl = ossClient.generatePresignedUrl(request);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			destory();
		}
		if (signedUrl != null) {
			return signedUrl;
		}
		return null;
	}
	
	/**
	 * 授权上传
	 * @param fileName
	 * @return
	 */
	public  void authUplaod(URL url) {
		File f = new File("C:/Users/pc/Desktop/BO{HU$D3(EOF_A0I3E{TIOX.png");
		FileInputStream fin;
		try {
			fin = new FileInputStream(f);
			// 添加PutObject请求头。
			Map<String, String> customHeaders = new HashMap<String, String>();
			customHeaders.put("Content-Type", "application/octet-stream");
//			customHeaders.put("x-oss-meta-author", "aliy");

			PutObjectResult result = ossClient.putObject(url, fin, f.length(), customHeaders);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			destory();
		}
	}
}
