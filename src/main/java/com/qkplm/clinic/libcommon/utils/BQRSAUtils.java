/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author: Wcke
 * @description: RSA加解密工具类
 * @datetime: 2024-07-04 21:36
 */
public class BQRSAUtils {
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 4096;
    public static final String PEM_PATH = "pem";
    private static final String PUBLIC_KEY_NAME = "public_key.pem";
    private static final String PRIVATE_KEY_NAME = "private_key.pem";

    private static String getOutputDir() throws Exception {
        String defaultPath = Paths.get(System.getProperty("user.dir"), PEM_PATH).toString();
        return BQIOUtils.getAppPathIfExists(PEM_PATH, defaultPath);
    }

    private static PublicKey loadPublicKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(spec);
    }

    private static PrivateKey loadPrivateKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        PKCS8EncodedKeySpec  spec = new PKCS8EncodedKeySpec (keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(spec);
    }

    public static String encryptWithPublicKey(String plainText, String publicKeyFile) throws Exception {
        return Base64.getEncoder().encodeToString(encryptWithPublicKeyToBytes(plainText, publicKeyFile));
    }

    public static byte[] encryptWithPublicKeyToBytes(String plainText, String publicKeyFile) throws Exception {
        PublicKey publicKey = loadPublicKey(publicKeyFile);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return handleByBlock(plainText.getBytes(), cipher, 11);
    }

    public static String decryptWithPrivateKey(String cipherText, String privateKeyFile) throws Exception {
        return decryptWithPrivateKey(Base64.getDecoder().decode(cipherText), privateKeyFile);
    }

    public static String decryptWithPrivateKey(byte[] bytes, String privateKeyFile) throws Exception {
        PrivateKey privateKey = loadPrivateKey(privateKeyFile);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(handleByBlock(bytes, cipher, 0));
    }

    private static byte[] handleByBlock(byte[] bytes, Cipher cipher, int blockSizeOffset) throws IllegalBlockSizeException, BadPaddingException {
        int blockSize = cipher.getOutputSize(bytes.length) - blockSizeOffset;
        byte[] decryptedBytes = new byte[0];
        for (int i = 0; i < bytes.length; i += blockSize) {
            int remainingLength = Math.min(blockSize, bytes.length - i);
            byte[] block = new byte[remainingLength];
            System.arraycopy(bytes, i, block, 0, remainingLength);
            byte[] decryptedBlock = cipher.doFinal(block);
            decryptedBytes = concatenateByteArrays(decryptedBytes, decryptedBlock);
        }
        return decryptedBytes;
    }

    public static String encryptWithPrivateKey(String plainText, String privateKeyFile) throws Exception {
        return Base64.getEncoder().encodeToString(encryptWithPrivateKeyToBytes(plainText, privateKeyFile));
    }

    public static byte[] encryptWithPrivateKeyToBytes(String plainText, String privateKeyFile) throws Exception {
        PrivateKey privateKey = loadPrivateKey(privateKeyFile);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return handleByBlock(plainText.getBytes(), cipher, 11);
    }

    public static String decryptWithPublicKey(String cipherText, String publicKeyFile) throws Exception {
        PublicKey publicKey = loadPublicKey(publicKeyFile);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return decryptWithPublicKey(Base64.getDecoder().decode(cipherText), publicKeyFile);
    }

    public static String decryptWithPublicKey(byte[] bytes, String publicKeyFile) throws Exception {
        PublicKey publicKey = loadPublicKey(publicKeyFile);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(handleByBlock(bytes, cipher, 0));
    }

    public static void generateKeys() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Files.write(getPublicKeyFile(), publicKey.getEncoded());
        Files.write(getPrivateKeyFile(), privateKey.getEncoded());
    }

    public static Path getPublicKeyFile() throws Exception {
        return Paths.get(getOutputDir(), PUBLIC_KEY_NAME);
    }

    public static Path getPrivateKeyFile() throws Exception {
        return Paths.get(getOutputDir(), PRIVATE_KEY_NAME);
    }

    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static void main(String[] args) throws Exception {
//        String plainText = "Hello, World!电视剧发货时间洞口防护较大刷卡合计dds";
//        System.out.println("Plain Text: " + plainText);
//        String encryptedText = encryptWithPublicKey(plainText, getPublicKeyFile().toString());
//        System.out.println("Encrypted Text: " + encryptedText);
//        String decryptedText = decryptWithPrivateKey(encryptedText, getPrivateKeyFile().toString());
//        System.out.println("Decrypted Text: " + decryptedText);
//
//        encryptedText = encryptWithPrivateKey(plainText, getPrivateKeyFile().toString());
//        System.out.println("Encrypted Text: " + encryptedText);
//        decryptedText = decryptWithPublicKey(encryptedText, getPublicKeyFile().toString());
//        System.out.println("Decrypted Text: " + decryptedText);

        byte[] bytes = Files.readAllBytes(Path.of("C:\\Users\\Wcke\\Downloads\\index.bqa"));
        String text = decryptWithPublicKey(bytes, getPublicKeyFile().toString());
        System.out.println(text);
    }
}
