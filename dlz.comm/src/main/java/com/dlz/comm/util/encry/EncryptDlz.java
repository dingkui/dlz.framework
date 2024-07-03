package com.dlz.comm.util.encry;

import com.dlz.comm.util.ValUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * 加密工具
 * 加密含有效期校验，随机数密码，混淆等
 * 支持单独无秘钥混淆加密
 *
 * @author dk 2024-03-14
 */
@Slf4j
public class EncryptDlz {
    private interface Const {
        int START_HCAR = 97;
        int[] ES = new int[]{7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
                43, 47, 53, 59, 61, 67, 71, 73, 79, 83,
                89, 97, 101, 103, 107, 109, 113, 127, 131, 137,
                139, 149, 151, 157, 163, 167, 173, 179, 181, 191};
        String FIXSTR = "0123456789abcdef0123456789abcdef";
        String CHECK_PASS_EMPUTY = "nkabcdef";
        char CHECK_PASS_SPLIT = ',';
    }

    /**
     * 异或加密工具
     *
     * @author dk 2024-03-14
     */
    public static class XorEncryption {
        final byte[] key;

        public XorEncryption(String key) {
            if (key != null) {
                this.key = ByteUtil.getBytes(Md5Util.md5(key));
            } else {
                this.key = null;
            }
        }

        /**
         * 使用XOR加密
         *
         * @return 加密后的字节数组
         */
        public byte[] xor(byte[] input, byte[] KEY) {
            if (KEY == null) {
                return input;
            }
            if (this.key != null) {
                for (int i = 0; i < KEY.length && i < this.key.length; i++) {
                    KEY[i] ^= this.key[i];
                }
            }

            byte[] encryptedBytes = new byte[input.length];
            for (int i = 0; i < input.length; i++) {
                int inputByte = input[i];
                if (i < KEY.length) {
                    for (int j = i; j < KEY.length; j++) {
                        inputByte ^= KEY[j];
                    }
                } else {
                    inputByte ^= KEY[i % KEY.length];
                }
                encryptedBytes[i] = (byte) inputByte;
            }
            return encryptedBytes;
        }

        public byte[] xor(long input, byte[] KEY) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES).putLong(input);
            return xor(buffer.array(), KEY);
        }

        public byte[] xor(long input, String KEY) {
            return xor(input, KEY == null ? null : ByteUtil.getBytes(KEY));
        }

        public byte[] xor(byte[] input, String KEY) {
            return xor(input, KEY == null ? null : ByteUtil.getBytes(KEY));
        }

        public byte[] xor(String input, byte[] KEY) {
            return xor(ByteUtil.getBytes(input), KEY);
        }

        public byte[] xor(String input, String KEY) {
            return xor(ByteUtil.getBytes(input), KEY == null ? null : ByteUtil.getBytes(KEY));
        }

        /**
         * 使用XOR加密字符串
         *
         * @return 加密后的字节数组
         */
        public long xorAsLong(byte[] input, byte[] KEY) {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.put(xor(input, KEY));
            buffer.flip(); // 切换到读模式
            return buffer.getLong();
        }

        /**
         * 使用XOR加密,如果密码为空则不加密直接返回base64编码
         *
         * @param {Uint8Array | String} input - 输入字节数组
         * @param {Uint8Array | String} key - 密钥字节数组
         * @return {String} 加密后的密文（base64编码）
         */
        public String encry(String input, String key) {
            return encry(ByteUtil.getBytes(input), key);
        }

        /**
         * 使用XOR加密,如果密码为空则不加密直接返回base64编码
         *
         * @param {Uint8Array | String} input - 输入字节数组
         * @param {Uint8Array | String} key - 密钥字节数组
         * @return {String} 加密后的密文（base64编码）
         */
        public String encry(byte[] input, String key) {
            return Base64.encode2Str(this.xor(input, key))
                    .replace("=", ".")
                    .replace("+", "-")
                    .replace("/", "_");
        }

        /**
         * 使用XOR加密
         *
         * @param {String}    input - 密文（base64编码）
         * @param {Uint8Array | String} key - 密钥字节数组
         * @return {String} 解密后的明文
         */
        public byte[] decry(String input, String key) {
            String base64Str = input
                    .replace(".", "=")
                    .replace("-", "+")
                    .replace("_", "/");
            return xor(Base64.decode(base64Str), key);
        }

        /**
         * 使用XOR加密
         *
         * @param {String}    input - 密文（base64编码）
         * @param {Uint8Array | String} key - 密钥字节数组
         * @return {String} 解密后的明文
         */
        public String decryAsStr(String input, String key) {
            byte[] decry = decry(input, key);
            if (decry != null) {
                return ByteUtil.getStr(decry);
            }
            return null;
        }
    }

    /**
     * 加密工具(位置混淆)
     *
     * @author dk 2017-06-20
     */
    public static class MixEncryption {
        private final int esSzie;
        private final int minSize;

        public MixEncryption(int esSzie, int minSize) {
            this.esSzie = esSzie == 0 ? 10 : esSzie > 40 ? 40 : esSzie < 3 ? 3 : esSzie;
            this.minSize = minSize == 0 ? 16 : minSize > 32 ? 32 : minSize < 3 ? 3 : minSize;
        }

        public String doMix(String input, boolean isEncode) {
            int len = input.length();
            int e = getE(len);
            char[] output = new char[len];
            int i = 0;
            int ind = 1;
            while (i++ < len) {
                ind += e;
                int index = ind;
                while (index >= len) {
                    index -= len;
                }
                if (isEncode) {
                    output[i - 1] = input.charAt(index);
                } else {
                    output[index] = input.charAt(i - 1);
                }
            }
            return new String(output);
        }

        public int getE(int len) {
            int eIndex = len % esSzie;
            while (len % Const.ES[eIndex] == 0 || (len>2 && len < Const.ES[eIndex] && Const.ES[eIndex] % len == 1)) {
                eIndex++;
            }
            return Const.ES[eIndex];
        }

        public String encode(String input) {
            //总长度=输入值长度+记录值长度1
            int len = input.length();
            //尾部补齐位数
            int addLen = len > minSize ? 0 : (minSize - len);

            input = (char) (Const.START_HCAR + addLen) + input + Const.FIXSTR.substring(0, addLen);
            return doMix(input, true);
        }

        public String decode(String input) {
            int len = input.length();
            String mix = doMix(input, false);
            int end = len - (int) mix.charAt(0) + Const.START_HCAR;
            return mix.substring(1, end);
        }
    }

    public interface IExpire {
        default boolean isExpired(String expiryTime) {
            if (expiryTime == null) {
                return true;
            }
            return Long.parseLong(expiryTime, 16) < System.currentTimeMillis() / 1000;
        }

        default String makeExpireTime(long expiry) {
            long t = System.currentTimeMillis() / 1000 + expiry;
            return Long.toHexString(t);
        }
    }

    public static class AdvancedEncryption {
        private final XorEncryption xor;
        private final MixEncryption mix;
        private final IExpire expire;

        AdvancedEncryption(XorEncryption xor, MixEncryption mix, IExpire expire) {
            if (xor == null) {
                xor = new XorEncryption(null);
            }
            if (mix == null) {
                mix = new MixEncryption(0, 0);
            }
            if (expire == null) {
                expire = new IExpire() {
                };
            }
            this.xor = xor;
            this.mix = mix;
            this.expire = expire;
        }

        private String generateRandom(int prefixLength) {
            double base = Math.pow(10, prefixLength - 1);
            return ValUtil.getInt(Math.floor(base + Math.random() * base * 9)).toString();
        }

        /**
         * 加密内容
         * 1.xor加密
         * 2.转换成base64
         * 3.天机校验信息
         * 4.打乱顺序
         *
         * @param inputBytes
         * @param key
         * @param expiry
         * @return 加密后的字符串
         */
        public String encry(byte[] inputBytes, String key, int expiry) {
            //取得随机数,每次加密时，随机数的长度为4位
            String[] prifixs = new String[]{generateRandom(4), Const.CHECK_PASS_EMPUTY, ""};
            if (key != null) {
                key = Md5Util.md5(key + prifixs[0]);
                prifixs[1] = Md5Util.md5(key).substring(0, 8);
            }
            if (expiry > 0) {
                prifixs[2] = xor.encry(expire.makeExpireTime(expiry), key);
            }

            String xorEncryedStr = xor.encry(inputBytes, key);
            return mix.encode(prifixs[0] + prifixs[1] + prifixs[2] + Const.CHECK_PASS_SPLIT + xorEncryedStr);
        }

        /**
         * 解密成byte[]
         * 1.乱序恢复
         * 2.
         *
         * @param {String} input - 打乱的密文（base64编码）
         * @param {String} key - 密钥
         * @return {byte[]} 解密后的byte[]
         */
        public byte[] decry(String input, String key) {
            String result = mix.decode(input);
            String base64str = result;

            int hasPass = result.indexOf(Const.CHECK_PASS_SPLIT);
            if (hasPass > -1) {
                String prefix = result.substring(0, hasPass);
                base64str = result.substring(hasPass + 1);

                String random = result.substring(0, 4);
                String checkPass = result.substring(4, 12);
                String expiryTimeStr = prefix.substring(12);

                if (checkPass.equals(Const.CHECK_PASS_EMPUTY)) {
                    key = null;
                } else {
                    key = Md5Util.md5(key + random);
                    if (!checkPass.equals(Md5Util.md5(key).substring(0, 8))) {
                        log.warn("解密失败，key错误");
                        return null;
                    }
                }

                if (expiryTimeStr.length() > 0) {
                    if (expire.isExpired(xor.decryAsStr(expiryTimeStr, key))) {
                        log.warn("解密失败，过期失效");
                        return null;
                    }
                }
            } else {
                key = null;
            }
            return xor.decry(base64str, key);
        }

        /**
         * 解密成字符串
         *
         * @param {String} input - 打乱的密文（base64编码）
         * @param {String} key - 密钥
         * @return {String} 解密后的明文
         */
        public String decryAsStr(String input, String key) {
            byte[] decry = decry(input, key);
            if (decry != null) {
                return ByteUtil.getStr(decry);
            }
            return null;
        }
    }

    private static XorEncryption xor = new XorEncryption("fserver");
    private static AdvancedEncryption advanced = new AdvancedEncryption(xor, null, null);

    /**
     * 使用XOR解密字符串
     *
     * @param inputBytes 要加密的信息
     * @param key        解密秘钥 可为空
     * @param expiry     解密有效期 单位秒
     * @return 解密后的字符串
     */
    public static String encry(byte[] inputBytes, String key, int expiry) {
        return advanced.encry(inputBytes, key, expiry);
    }

    /**
     * 使用XOR加字符串
     *
     * @return 解密后的字符串
     */
    public static String encry(String input, String key, int expiry) {
        return encry(ByteUtil.getBytes(input), key, expiry);
    }

    /**
     * 使用XOR解密字符串
     *
     * @return 解密后的byte[]
     */
    public static byte[] decry(String input, String key) {
        return advanced.decry(input, key);
    }

    public static String decryAsStr(String input, String key) {
        return advanced.decryAsStr(input, key);
    }
}
