package com.bbk.util;
import com.logg.Logg;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAEncryptorAndroid {
	 static String public_key=
             "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCel54lYHfnu8kFz8dOfVPA4uab" + 
             "odzeUese+Fom5zl6FCDLrR6LduukWx/N37QYEXWo60qWYUP7JuamRn2ozscMrrqZ" + 
             "VqDcNsyKk0XO19verZfk0iz3Tx25qEv6fVl5GBeydLMRBE/ta8r6YI/LfpNVOrLJ" + 
             "gJ/YBBcSyp46L2ScVwIDAQAB" ;    
             
	

    public static void main(String[] args) throws Exception{  
        try {

            String test = "18008099823wori我日";
//            String testRSAEnWith64 = encryptStr(test);
//            System.out.println("加密后：" + testRSAEnWith64);
            //PXOBE6DE28MnbYOHIIn6Sq4hbu8PMmTiQQRARAzWdpf0Pea5ikWsd9oWuhHzLcnnB+Mee5LU5b6ObRGAJNrU9+sG11qERdV/bnmBaRA1Ujjaq+Hhi3Lcp+/YvdI6Ii3uqOYg41JzqK0WwMcyDiQ5cb/ZvJcEMcpUB4hAmLzRgUw=

            System.out.println(getSendCode("15928046246"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getSendCode(String phone) {
    	Date dt = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");//可以方便地修改日期格式
		String systemDate = dateFormat.format(dt);
        System.out.println(systemDate);
//        Logg.e(systemDate);
		String value = phone+systemDate;
		String code = encryptStr(value);
		return code;
    }


    public static String encryptStr(String string) {

    	try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(public_key);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            RSAPublicKey publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
            byte[] binaryData = encrypt(publicKey, string.getBytes());
            String base64String = new BASE64Encoder().encodeBuffer(binaryData) /* org.apache.commons.codec.binary.Base64.encodeBase64(binaryData) */;
            base64String = base64String.replace("\r\n", "");
            return base64String;
        } catch (Exception e) {  
            
        }  
    	return "";


    }

    //最近项目采用RSA进行加密,遇到了坑,记录一下
//
//1、BASE64Decoder在Android中是不存在的，需用Base64替换，
//    BASE64Decoder base64Decoder= new BASE64Decoder();
//    byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
//    替换为：
//    byte[] buffer= Base64.decode(DEFAULT_PUBLIC_KEY, Base64.DEFAULT);
//1
//        2
//        3
//        4
//        5
//    导包
//
//import android.util.Base64;
//1
//        2、cipher= Cipher.getInstance("RSA");
//cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//    做加密，但是一直无法与服务器那边对接，而且每次生成的密文是一样的。
//    后来参考文章：http://my.oschina.net/oschenxiaomo/blog/543199
//    使用cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding")后就可以了。
    /** 
     * 加密过程 
     * @param publicKey 公钥 
     * @param plainTextData 明文数据 
     * @return 
     * @throws Exception 加密过程中的异常信息 
     */  
    private static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{  
        if(publicKey== null){  
            throw new Exception("加密公钥为空, 请设置");  
        }  
        Cipher cipher= null;  
        try {  
            cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");//, new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
            byte[] output= cipher.doFinal(plainTextData);  
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此加密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        }catch (InvalidKeyException e) {  
            throw new Exception("加密公钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("明文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("明文数据已损坏");  
        }  
    }



}