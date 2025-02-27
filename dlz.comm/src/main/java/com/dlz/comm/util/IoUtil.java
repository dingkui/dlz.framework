package com.dlz.comm.util;

import com.dlz.comm.consts.Charsets;
import com.dlz.comm.exception.SystemException;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 流工具类
 *
 * @author dk
 */
public class IoUtil{
	public static final int BUFFER_SIZE = 4096;

	private static final byte[] EMPTY_CONTENT = new byte[0];
	public static int copy(InputStream in, OutputStream out) throws IOException {
		int byteCount = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		return byteCount;
	}
	public static String copyToString(InputStream in, Charset charset) throws IOException {
		if (in == null) {
			return "";
		}

		StringBuilder out = new StringBuilder(BUFFER_SIZE);
		InputStreamReader reader = new InputStreamReader(in, charset);
		char[] buffer = new char[BUFFER_SIZE];
		int charsRead;
		while ((charsRead = reader.read(buffer)) != -1) {
			out.append(buffer, 0, charsRead);
		}
		return out.toString();
	}
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		if (in == null) {
			return new byte[0];
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}
	/**
	 * closeQuietly
	 *
	 * @param closeable 自动关闭
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		if (closeable instanceof Flushable) {
			try {
				((Flushable) closeable).flush();
			} catch (IOException ignored) {
				// ignore
			}
		}
		try {
			closeable.close();
		} catch (IOException ignored) {
			// ignore
		}
	}

	/**
	 * InputStream to String utf-8
	 *
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested String
	 */
	public static String readToString(InputStream input) {
		return readToString(input, Charsets.UTF_8);
	}

	/**
	 * InputStream to String
	 *
	 * @param input   the <code>InputStream</code> to read from
	 * @param charset the <code>Charset</code>
	 * @return the requested String
	 */
	public static String readToString(InputStream input, Charset charset) {
		try {
			return copyToString(input, charset);
		} catch (IOException e) {
			throw SystemException.build(e);
		} finally {
			closeQuietly(input);
		}
	}

	public static byte[] readToByteArray(InputStream input) {
		try {
			return copyToByteArray(input);
		} catch (IOException e) {
			throw SystemException.build(e);
		} finally {
			closeQuietly(input);
		}
	}

	/**
	 * Writes chars from a <code>String</code> to bytes on an
	 * <code>OutputStream</code> using the specified character encoding.
	 * <p>
	 * This method uses {@link String#getBytes(String)}.
	 * </p>
	 * @param data     the <code>String</code> to write, null ignored
	 * @param output   the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if output is null
	 * @throws IOException          if an I/O error occurs
	 */
	public static void write(final String data, final OutputStream output, final Charset encoding) throws IOException {
		if (data != null) {
			output.write(data.getBytes(encoding));
		}
	}
}
