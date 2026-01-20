/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dlz.comm.util;


/**
 * 简单模式匹配工具类
 * 
 * 提供简单模式匹配的工具方法，特别适用于Spring典型的"xxx*", "*xxx"和"*xxx*"模式样式
 * 
 * 支持以下模式：
 * - "xxx*"：以指定字符串开头
 * - "*xxx"：以指定字符串结尾
 * - "*xxx*"：包含指定字符串
 * - "xxx*yyy"：包含多个模式部分
 * - 直接相等匹配
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public class MatchUtils {

	/**
	 * 将字符串与给定模式进行匹配，支持以下简单模式样式：
	 * "xxx*", "*xxx", "*xxx*" 和 "xxx*yyy" 匹配（具有任意数量的模式部分），以及直接相等匹配
	 * 
	 * @param pattern 要匹配的模式
	 * @param str 要匹配的字符串
	 * @return 如果字符串匹配给定模式则返回true，否则返回false
	 */
	public static boolean simpleMatch(String pattern, String str) {
		if (pattern == null || str == null) {
			return false;
		}

		int firstIndex = pattern.indexOf('*');
		if (firstIndex == -1) {
			return pattern.equals(str);
		}

		if (firstIndex == 0) {
			if (pattern.length() == 1) {
				return true;
			}
			int nextIndex = pattern.indexOf('*', 1);
			if (nextIndex == -1) {
				return str.endsWith(pattern.substring(1));
			}
			String part = pattern.substring(1, nextIndex);
			if (part.isEmpty()) {
				return simpleMatch(pattern.substring(nextIndex), str);
			}
			int partIndex = str.indexOf(part);
			while (partIndex != -1) {
				if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
					return true;
				}
				partIndex = str.indexOf(part, partIndex + 1);
			}
			return false;
		}

		return (str.length() >= firstIndex &&
				pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) &&
				simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
	}

	/**
	 * 将字符串与给定的多个模式进行匹配，支持以下简单模式样式：
	 * "xxx*", "*xxx", "*xxx*" 和 "xxx*yyy" 匹配（具有任意数量的模式部分），以及直接相等匹配
	 * 
	 * @param patterns 要匹配的模式数组
	 * @param str 要匹配的字符串
	 * @return 如果字符串匹配任何给定模式则返回true，否则返回false
	 */
	public static boolean simpleMatch(String[] patterns, String str) {
		if (patterns != null) {
			for (String pattern : patterns) {
				if (simpleMatch(pattern, str)) {
					return true;
				}
			}
		}
		return false;
	}

}