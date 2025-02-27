package com.dlz.comm.consts;
import com.dlz.comm.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public interface Charsets {
    String CONTENT_TYPE_NAME = "Content-type";

    Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    String ISO_8859_1_NAME = ISO_8859_1.name();

    String GBK_NAME = "GBK";
    Charset GBK = Charset.forName(GBK_NAME);

    Charset UTF_8 = StandardCharsets.UTF_8;
    String UTF_8_NAME = UTF_8.name();

    /**
     * 转换为Charset对象
     *
     * @param charsetName 字符集，为空则返回默认字符集
     * @return Charsets
     * @throws UnsupportedCharsetException 编码不支持
     */
    static Charset charset(String charsetName) throws UnsupportedCharsetException {
        return StringUtils.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }
}