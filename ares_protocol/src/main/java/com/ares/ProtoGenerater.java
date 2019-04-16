package com.ares;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ares.file.FileUtil;

/**
 * proto生成器
 *
 * <dl>
 * String command = protocPath + " -I " + protoPath + " --java_out="
 * +javaOutPath + " " + protoFile;
 * </dl>
 */
public class ProtoGenerater {
	public static void main(String[] args) throws IOException {
		String basePath = System.getProperty("user.dir");
		File file = new File(basePath);

		List<File> sourceFileList = new ArrayList<>();
		FileUtil.getFiles(file.getParentFile(), sourceFileList, ".exe", null);
		Optional<File> optional = sourceFileList.stream()
				.filter(_file -> _file.getName().toLowerCase().contains("protoc")).findAny();
		if (!optional.isPresent()) {
			System.err.println("没有获取到protoc的路径");
		}
		// 获取编译器路径
		String protocPath = optional.get().getPath();
		System.out.println(protocPath);

		// 找到proto所在的目录
		String protoPath = FileUtil.getFileDirectory(file, "proto").getPath();
		// 找到编译成java的输出目录
		String javaOutPath = FileUtil.getFileDirectory(file, "java").getPath();

		List<File> protos = new ArrayList<>();

		File protoFiles = new File(protoPath);

		FileUtil.getFiles(protoFiles, protos, ".proto", null);

		for (File protoFile : protos) {
			String command = protocPath + " -I " + protoPath + " --java_out=" + javaOutPath + " "
					+ protoFile.getAbsolutePath();
			System.out.println(command);
			exeCmd(command);
		}

	}

	public static void exeCmd(String commandStr) {
		BufferedReader br = null;
		try {
			Process p = Runtime.getRuntime().exec(commandStr);
			br = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
