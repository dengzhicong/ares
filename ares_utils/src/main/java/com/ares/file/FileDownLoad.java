package com.ares.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.ares.log.LogUtil;

public class FileDownLoad {
	public static boolean downLoad(String fileUrl, String saveLocalPath, String fileName) {
		try {
			String urlFileName = fileUrl + "_" + fileName;
			@SuppressWarnings("unused")
			int byteSum = 0;
			int byteRead = 0;
			URL url = new URL(urlFileName);
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			FileOutputStream out = new FileOutputStream(new File(saveLocalPath + fileName));
			byte[] bytes = new byte[1024];
			while ((byteRead = in.read(bytes)) != -1) {
				byteSum += byteRead;
				out.write(bytes, 0, byteRead);
			}
			out.flush();
			out.close();
			in.close();
		} catch (MalformedURLException e) {
			LogUtil.error("下载文件异常 FileName " + fileName, e);
			return false;
		} catch (IOException e) {
			LogUtil.error("下载文件异常 FileName " + fileName, e);
			return false;
		}
		return true;
	}
}
