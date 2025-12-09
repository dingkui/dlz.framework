package com.dlz.comm.util.encry;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public enum RSASignEnum {
    SHA1("SHA1WithRSA"),
    SHA256("SHA256WithRSA");
    private String algorithm;

    RSASignEnum(String algorithm) {
        this.algorithm = algorithm;
    }

    public String sign(byte[] data, byte[] privateKey) {
        try {
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey));
            Signature signature = Signature.getInstance(this.algorithm);
            signature.initSign(priKey);
            signature.update(data);
            byte[] signed = signature.sign();
            return Base64.encode2Str(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String sign(String content, String privateKey) {
        return sign(content.getBytes(), Base64.decode(privateKey));
    }

    public String sign(String content, byte[] privateKey) {
        return sign(content.getBytes(), privateKey);
    }

    public boolean check(byte[] data, byte[] publicKey, String sign) {
        try {
            PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
            Signature signature = Signature.getInstance(this.algorithm);
            signature.initVerify(pubKey);
            signature.update(data);
            return signature.verify(Base64.decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean check(String content, byte[] publicKey, String sign) {
        return check(content.getBytes(), publicKey, sign);
    }

    public boolean check(String content, String publicKey, String sign) {
        return check(content.getBytes(), Base64.decode(publicKey), sign);
    }
}