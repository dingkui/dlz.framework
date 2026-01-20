package com.dlz.comm.util;


import com.dlz.comm.consts.Char;
import com.dlz.comm.consts.Charsets;
import com.dlz.comm.consts.Str;
import com.dlz.comm.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * 提供便捷的文件操作功能，包括文件遍历、读取、写入、移动、删除等操作
 * 
 * @author dk
 * @since 2023
 */
@Slf4j
public class FileUtil {
	/**
	 * 扫描目录下的文件
	 *
	 * @param file 文件或目录
	 * @return 文件集合
	 */
	public static List<File> list(File file) {
		return list(file, new ArrayList<>(), null);
	}
	
	/**
	 * 扫描目录下的文件
	 *
	 * @param path 路径
	 * @return 文件集合
	 */
	public static List<File> list(String path) {
		return list(new File(path));
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param file   文件或目录
	 * @param filter 文件过滤器
	 * @return 文件集合
	 */
	public static List<File> list(File file, FileFilter filter) {
		return list(file, new ArrayList<>(), filter);
	}
	
	/**
	 * 扫描目录下的文件
	 *
	 * @param path   路径
	 * @param filter 文件过滤器
	 * @return 文件集合
	 */
	public static List<File> list(String path, FileFilter filter) {
		return list(new File(path), filter);
	}
	
	/**
	 * 扫描目录下的文件
	 *
	 * @param file   文件或目录
	 * @param fileNamePattern Spring AntPathMatcher 规则
	 * @return 文件集合
	 */
	public static List<File> list(File file, final String fileNamePattern) {
		return list(file, pathname -> fileNamePattern == null || MatchUtils.simpleMatch(fileNamePattern, pathname.getName()));
	}
	
	/**
	 * 扫描目录下的文件
	 *
	 * @param path   路径
	 * @param fileNamePattern 文件名匹配模式（支持通配符）
	 * @return 文件集合
	 */
	public static List<File> list(String path, final String fileNamePattern) {
		return list(new File(path), fileNamePattern);
	}
	
	/**
	 * 扫描目录下的文件
	 *
	 * @param file   文件或目录
	 * @param fileList 文件列表（用于收集结果）
	 * @param filter 文件过滤器，可为null
	 * @return 文件集合
	 */
	private static List<File> list(File file, List<File> fileList, FileFilter filter) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					list(f, fileList, filter);
				}
			}
		} else {
			// 过滤文件
			if (file.exists() && (filter == null || filter.accept(file))) {
				fileList.add(file);
			}
		}
		return fileList;
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fullName 文件全名
	 * @return 文件扩展名，如果没有扩展名则返回空字符串
	 */
	public static String getFileExtension(String fullName) {
		if (StringUtils.isBlank(fullName)) return  Str.EMPTY;
		String fileName = new File(fullName).getName();
		int dotIndex = fileName.lastIndexOf(Char.DOT);
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

	/**
	 * 获取文件名，去除后缀名
	 * 
	 * @param fullName 文件全名
	 * @return 不包含扩展名的文件名
	 */
	public static String getNameWithoutExtension(String fullName) {
		if (StringUtils.isBlank(fullName)) return  Str.EMPTY;
		String fileName = new File(fullName).getName();
		int dotIndex = fileName.lastIndexOf(Char.DOT);
		return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
	}

	/**
	 * 获取系统临时目录路径
	 *
	 * @return 系统临时目录路径
	 */
	public static String getTempDirPath() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 获取系统临时目录文件对象
	 *
	 * @return 系统临时目录文件对象
	 */
	public static File getTempDir() {
		return new File(getTempDirPath());
	}

	/**
	 * 将文件内容读取为字符串
	 * 
	 * 文件总是会被关闭
	 *
	 * @param file 要读取的文件，不能为 {@code null}
	 * @return 文件内容，永远不会为 {@code null}
	 */
	public static String readToString(final File file) {
		return readToString(file, Charsets.UTF_8);
	}

	/**
	 * 将文件内容读取为字符串
	 * 
	 * 文件总是会被关闭
	 *
	 * @param file     要读取的文件，不能为 {@code null}
	 * @param encoding 编码格式，{@code null} 表示使用平台默认编码
	 * @return 文件内容，永远不会为 {@code null}
	 */
	public static String readToString(final File file, final Charset encoding) {
		try (InputStream in = Files.newInputStream(file.toPath())) {
			return IoUtil.readToString(in, encoding);
		} catch (IOException e) {
			throw SystemException.build(e);
		}
	}

	/**
	 * 将文件内容读取为字节数组
	 * 
	 * 文件总是会被关闭
	 *
	 * @param file     要读取的文件，不能为 {@code null}
	 * @return 文件内容的字节数组，永远不会为 {@code null}
	 */
	public static byte[] readToByteArray(final File file) {
		try (InputStream in = Files.newInputStream(file.toPath())) {
			return IoUtil.readToByteArray(in);
		} catch (IOException e) {
			throw SystemException.build(e);
		}
	}

	/**
	 * 将字符串写入文件，如果文件不存在则创建
	 *
	 * @param file 文件对象
	 * @param data 要写入文件的内容
	 */
	public static void writeToFile(final File file, final String data) {
		writeToFile(file, data, Charsets.UTF_8, false);
	}

	/**
	 * 将字符串写入文件，如果文件不存在则创建
	 *
	 * @param file   文件对象
	 * @param data   要写入文件的内容
	 * @param append 如果为 {@code true}，则字符串将追加到文件末尾而不是覆盖
	 */
	public static void writeToFile(final File file, final String data, final boolean append){
		writeToFile(file, data, Charsets.UTF_8, append);
	}

	/**
	 * 将字符串写入文件，如果文件不存在则创建
	 *
	 * @param file     文件对象
	 * @param data     要写入文件的内容
	 * @param encoding 编码格式，{@code null} 表示使用平台默认编码
	 */
	public static void writeToFile(final File file, final String data, final Charset encoding) {
		writeToFile(file, data, encoding, false);
	}

	/**
	 * 将字符串写入文件，如果文件不存在则创建
	 *
	 * @param file     文件对象
	 * @param data     要写入文件的内容
	 * @param encoding 编码格式，{@code null} 表示使用平台默认编码
	 * @param append   如果为 {@code true}，则字符串将追加到文件末尾而不是覆盖
	 */
	public static void writeToFile(final File file, final String data, final Charset encoding, final boolean append) {
		try (OutputStream out = new FileOutputStream(file, append)) {
			IoUtil.write(data, out, encoding);
		} catch (IOException e) {
			throw SystemException.build(e);
		}
	}

	//	/**
	//	 * 转成file
	//	 * @param multipartFile MultipartFile
	//	 * @param file File
	//	 */
	//	public static void toFile(MultipartFile multipartFile, final File file) {
	//		try {
	//			FileUtil.toFile(multipartFile.getInputStream(), file);
	//		} catch (IOException e) {
	//			throw SystemException.build(e);
	//		}
	//	}

	/**
	 * 将输入流保存为文件
	 * 
	 * @param in 输入流
	 * @param file 目标文件
	 */
	public static void toFile(InputStream in, final File file) {
		try (OutputStream out = new FileOutputStream(file)) {
			IoUtil.copy(in, out);
		} catch (IOException e) {
			throw SystemException.build(e);
		}
	}

	/**
	 * 移动文件
	 * 
	 * 当目标文件在另一个文件系统上时，执行"复制并删除"操作
	 *
	 * @param srcFile  源文件
	 * @param destFile 目标文件
	 * @throws NullPointerException 如果源文件或目标文件为 {@code null}
	 * @throws IOException          如果源文件或目标文件无效
	 * @throws IOException          如果移动文件时发生IO错误
	 */
	public static void moveFile(final File srcFile, final File destFile) throws IOException {
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' is a directory");
		}
		if (destFile.exists()) {
			throw new IOException("Destination '" + destFile + "' already exists");
		}
		if (destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' is a directory");
		}
		final boolean rename = srcFile.renameTo(destFile);
		if (!rename) {
			copy(srcFile, destFile);
			if (!srcFile.delete()) {
				deleteQuietly(destFile);
				throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
			}
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param in 源文件
	 * @param out 目标文件
	 * @return 复制的字节数
	 * @throws IOException IO异常
	 */
	public static int copy(File in, File out) throws IOException {
		try (InputStream inputStream = Files.newInputStream(in.toPath()); OutputStream outputStream = Files.newOutputStream(out.toPath())){
			return IoUtil.copy(inputStream, outputStream);
		}catch (IOException e){
			throw e;
		}
	}

	/**
	 * 删除文件，不抛出异常
	 * 
	 * 如果文件是目录，则删除它及其所有子目录
	 * 
	 * 与 File.delete() 方法的区别：
	 * <ul>
	 * <li>要删除的目录不必为空。</li>
	 * <li>当无法删除文件或目录时不抛出异常。</li>
	 * </ul>
	 *
	 * @param file 要删除的文件或目录，可以为 {@code null}
	 * @return 如果文件或目录被删除则返回 {@code true}，否则返回 {@code false}
	 */
	public static boolean deleteQuietly(final File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}
					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						if (exc == null) {
							Files.delete(dir);
							return FileVisitResult.CONTINUE;
						} else {
							throw exc;
						}
					}
				});
			}
		} catch (final Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		try {
			return file.delete();
		} catch (final Exception ignored) {
			log.warn("delete file fail:" + file.getAbsolutePath());
			return false;
		}
	}

}