js
``` javascript
const crypto = require('crypto');

function md5(str) {
    const hash = crypto.createHash('md5');
    hash.update(str);
    return hash.digest('hex');
}
const base64Util ={
    /**
     * base64Encode
     * @param {Uint8Array} input - 输入字节数组
     * @return {String} base64编码
     */
    encode(input){
        // 将字节数组转换为ISO-8859-1编码的字符串
        let str = '';
        for (let i = 0; i < input.length; i++) {
            str += String.fromCharCode(input[i]);
        }
        return btoa(str);
    },
    decode(base64String) {
        // 使用atob解码Base64字符串得到二进制字符串
        const binaryString = atob(base64String);
        // 将二进制字符串转换为字节数组
        const byteArray = new Uint8Array(binaryString.length);
        for (let i = 0; i < binaryString.length; i++) {
            byteArray[i] = binaryString.charCodeAt(i);
        }
        return byteArray;
    }
}

const byteUtil = {
    getBytes(input) {
        if (input instanceof Uint8Array) {
            return input;
        }
        const utf8 = unescape(encodeURIComponent(input));
        const arr = new Uint8Array(utf8.length);
        for (let i = 0; i < utf8.length; i++) {
            arr[i] = utf8.charCodeAt(i);
        }
        return arr;
    },
    getStr(input,encoding = 'utf-8') {
        if (input instanceof Uint8Array) {
            return new TextDecoder(encoding).decode(input);
        }
        return input
    }
}
const Const = {
    START_HCAR: 97,
    ES: [7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
        43, 47, 53, 59, 61, 67, 71, 73, 79, 83,
        89, 97, 101, 103, 107, 109, 113, 127, 131, 137,
        139, 149, 151, 157, 163, 167, 173, 179, 181, 191],
    FIXSTR: "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
    CHECK_PASS_SPLIT: ","
}
class XorEncryption {
    constructor(key) {
        if (key) {
            this.pk = byteUtil.getBytes(md5(key));
        }
    }

    /**
     * 使用XOR加密字节数组
     * @param {Uint8Array | String} input - 输入字节数组
     * @param {Uint8Array | String} key - 密钥字节数组
     * @return {Uint8Array} 加密后的字节数组
     */
    xor(input, key) {
        input = byteUtil.getBytes(input);
        if (!key) {
            return input;
        }
        key = byteUtil.getBytes(key);
        if (this.pk) {
            for (let i = 0; i < key.length && i < this.pk.length; i++) {
                key[i] ^= this.pk[i];
            }
        }
        let encryptedBytes = new Uint8Array(input.length);

        for (let i = 0; i < input.length; i++) {
            let inputByte = input[i];
            if (i < key.length) {
                for (let j = i; j < key.length; j++) {
                    inputByte ^= key[j];
                }
            } else {
                inputByte ^= key[i % key.length];
            }
            encryptedBytes[i] = inputByte;
        }
        return encryptedBytes;
    }


    /**
     * 使用XOR加密,如果密码为空则不加密直接返回base64编码
     * @param {Uint8Array | String} input - 输入字节数组
     * @param {Uint8Array | String} key - 密钥字节数组
     * @return {String} 加密后的密文（base64编码）
     */
    encry(input, key) {
        let encodedArray = this.xor(input, key);
        let base64Str = base64Util.encode(encodedArray);
        return base64Str
            .replace("=", ".")
            .replace("+", "-")
            .replace("/", "_");
    }

    /**
     * 使用XOR加密
     * @param {String} input - 密文（base64编码）
     * @param {Uint8Array | String} key - 密钥字节数组
     * @return {String} 解密后的明文
     */
    decry(input, key) {
        let base64Str = input
            .replaceAll(".", "=")
            .replaceAll("-", "+")
            .replaceAll("_", "/");
        let encodedArray = base64Util.decode(base64Str);
        return byteUtil.getStr(this.xor(encodedArray, key))
    }
}



//位置混淆
class MixEncryption {
    constructor(esSzie = 10, minSize = 16) {
        this.esSzie = esSzie == 0 ? 10 : esSzie > 40 ? 40 : esSzie < 3 ? 3 : esSzie;
        this.minSize = minSize == 0 ? 16 : minSize > 32 ? 32 : minSize < 3 ? 3 : minSize;
    }
    getE(len){
        let eIndex = len % this.esSzie;
        while (len % Const.ES[eIndex] == 0 || (len>2 && len < Const.ES[eIndex] && Const.ES[eIndex] % len == 1)) {
            eIndex++;
        }
        return Const.ES[eIndex];
    }
    doMix(input, isEncode) {
        let len = input.length;
        let e = this.getE(len);
        let output = [];
        let i = 0;
        let ind = 1;
        while (i++ < len) {
            ind += e;
            let index = ind;
            while (index >= len) {
                index -= len;
            }
            if (isEncode) {
                output[i - 1] = input.charAt(index);
            } else {
                output[index] = input.charAt(i - 1);
            }
        }
        return output.join("");
    }

    encode(input) {
        //总长度=输入值长度+记录值长度1
        let len = input.length;
        //尾部补齐位数
        let addLen = len > this.minSize ? 0 : (this.minSize - len);

        //增加补齐位
        input = String.fromCharCode(Const.START_HCAR + addLen) + input + Const.FIXSTR.substring(0, addLen);
        return this.doMix(input, true);
    }

    decode(input) {
        let len = input.length;
        let mix = this.doMix(input, false);
        return mix.substring(1, len - mix.charCodeAt(0) + Const.START_HCAR);
    }
}

class AdvancedEncryption {
    constructor(xor, mix,makeExpireTime, isExpired) {
        //构造函数
        this.xor = xor || new XorEncryption();
        this.mix = mix || new MixEncryption();
        //判断是否有效
        this.isExpired = isExpired || function (expiryTime) {
            if (expiryTime == null) {
                return true;
            }
            return parseInt(expiryTime, 16) < Date.now() / 1000
        }
        //构建有效期
        this.makeExpireTime = makeExpireTime || function (expiry) {
            return (Math.floor((Date.now()) / 1000) + expiry).toString(16);
        }
    }

    encodePass(input) {
        return md5(input);
    }

    generateRandom(prefixLength) {
        let sb = "";
        while(prefixLength-->0){
            sb+=Const.FIXSTR.charAt(Math.floor(Math.random() * 64));
        }
        return sb;
    }

    /**
     * 加密字符串
     * @param {string} input - 需要加密的字符串
     * @param {string} key - 加密密钥
     * @param {number} expiry - 过期时间（秒）
     * @param {number} sTime - 客户端与服务器时间差（毫秒）
     * @return {string} 加密后的字符串（打乱的base64编码）
     */
    encry(inputStr, key = "", expiry = 0) {
        //取得随机数,每次加密时，随机数的长度为4位
        let random = this.generateRandom(2);
        let checkPass;
        let expiryTimeStr = "";

        let xorKey;
        if (key) {
            xorKey = this.encodePass(key + random);
        }
        if (expiry > 0) {
            if(!xorKey){
                xorKey = this.encodePass(random);
            }
            expiryTimeStr = this.xor.encry(this.makeExpireTime(expiry), xorKey);
        }

        let out = this.xor.encry(inputStr, xorKey);
        if(xorKey != null){
            checkPass = this.encodePass(xorKey).substring(0, 6);
            out = random + checkPass + expiryTimeStr + Const.CHECK_PASS_SPLIT + out;
        }
        return mix.encode(out);
    }

    /**
     * 解密字符串
     * @param {string} input - 需要解密的字符串
     * @param {string} key - 解密密钥
     * @return {string | null} 解密后的字符串或null（解密失败）
     */
    decry(input, key) {
        let result = this.mix.decode(input);
        let base64str = result;
        let xorKey = "";

        let hasPass = result.indexOf(Const.CHECK_PASS_SPLIT);
        if (hasPass > -1) {
            base64str = result.substring(hasPass + 1);
            const random = result.substring(0, 2);
            const checkPass = result.substring(2, 8);
            const expiryTimeStr = result.substring(8,hasPass);
            xorKey = this.encodePass(((key==null||typeof key=="undefined")?"":key) + random);

            if (!this.encodePass(xorKey).startsWith(checkPass)) {
                console.warn("解密失败，key错误");
                return null;
            }

            if (expiryTimeStr) {
                const expiry = this.xor.decry(expiryTimeStr, xorKey);
                if (this.isExpired(expiry)) {
                    console.warn('解密失败，过期失效');
                    return null;
                }
            }
        }
        return this.xor.decry(base64str, xorKey);
    }
}

const xor = new XorEncryption("fserver");
const mix = new MixEncryption();
const coder = new AdvancedEncryption(xor,mix);

// function test(text="中文_１２ａｂＡＢ＆＊（）_12abAB&*()_■△◎∧∨ "){
//     let message = coder.encry(text,"123",30);
//     console.log("加密后："+message)
//     console.log("解密后："+coder.decry("message","123"))
//     console.log("解密后错误："+coder.decry(message,"xx"))
//     let t2 =setInterval(()=>{
//         let input=message;
//         // let input="35vPLbo_3gE7f-qdzOSrzqzbgyAusaU184t2Hbp6ijLdeOa4W,2-g42e21NOjJHbubAj3Lgpo0WEbnao2pG72tqLZl6b9.6Z.cym-ui0E39Ree";
//         let decry = coder.decry(input,"123");
//         let res = decry==text
//         console.log("解密结果2：",res,decry)
//         if(!res){
//             clearInterval(t2)
//         }
//     },3000)
// }
// test()

module.exports = {
    md5,
    byteUtil,
    XorEncryption,
    AdvancedEncryption,
    MixEncryption,
    coder,
    xor,
    mix
}
```