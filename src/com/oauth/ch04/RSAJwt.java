package com.oauth.ch04;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAJwt {
    public static Map<String, byte[]> map = new HashMap<String, byte[]>();

    public static void main(String[] args) throws Exception {
//        // 加密
        String token = generateToken(1000, "secert123");
        // 解密
        decode(token);
    }

    public static void decode(String token) throws Exception {
        getInfoFromToken(token, map.get("pub"));

    }

    // 打印解密后的数据
    public static void getInfoFromToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKey);
        Claims body = claimsJws.getBody();
        System.out.println("body:" + body.getSubject());
        System.out.println("id:" + body.get("id"));
        System.out.println("name:" + body.get("name"));
    }

    // 解析加密过的token，解析成Claims，就是自定义的playload部分
    public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getPublicKey(pubKey)).parseClaimsJws(token);
        return claimsJws;
    }

    /***
     *
     * @param expire 过期事件
     * @param secert 盐
     */
    public static String generateToken(int expire, String secert) throws Exception {
        Map<String, byte[]> key = generateKey(secert);

        /*String compactJws = Jwts.builder()
                .setSubject("subject2")
                // 添加clain，就是自定义的playload部分
                .claim("id", "1")
                .claim("name", "zhangsan")
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, getPrivateKey(key.get("pri")))
                .compact();*/

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("sub", "Tom");
        payloadMap.put("iss", "--");

        String compactJws = Jwts.builder().setHeaderParams(headerMap).setClaims(payloadMap).signWith(getPrivateKey(key.get("pri")), SignatureAlgorithm.HS256).compact();

        System.out.println(compactJws);
        return compactJws;
    }

    /**
     * 获取公钥
     *
     * @param publicKey
     * @return
     * @throws Exception
     */
    // 使用jdk 的security把byte数据解析成getPublicKey类
    public static PublicKey getPublicKey(byte[] publicKey) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    // 使用jdk 的security把byte数据解析成PrivateKey类
    public static PrivateKey getPrivateKey(byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // 使用jdk的security生成rsa的公钥和私钥
    public static Map<String, byte[]> generateKey(String password) throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(password.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        map.put("pub", publicKeyBytes);
        map.put("pri", privateKeyBytes);
        return map;
    }
}
