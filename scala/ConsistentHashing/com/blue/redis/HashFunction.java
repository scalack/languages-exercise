package com.blue.redis;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-7-27
 * Time: 18:37:45
 * To change this template use File | Settings | File Templates.
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFunction {
    private MessageDigest md5 = null;

    public long hash(String key) {
        if (md5 == null) {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("no md5 algorythm found");
            }
        }

        md5.reset();
        md5.update(key.getBytes());
        byte[] bKey = md5.digest();
        long res = ((long) (bKey[3] & 0xFF) << 24)
                | ((long) (bKey[2] & 0xFF) << 16)
                | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF);
//      System.out.println("hashcode:"+res);
        return res;
    }
}