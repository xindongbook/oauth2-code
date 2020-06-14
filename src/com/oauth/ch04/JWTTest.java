package com.oauth.ch04;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import sun.misc.BASE64Decoder;

import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {



    /*static String publicKey = "QgkAQIDAQAB";
    static String privateKey = "hellooauth";


    public static PublicKey getPublicKey() {
        try {
//            byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(publicKey); // 正确方式


            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));

//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return pubKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKey() {

        try {
//            byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privateKey); // 正确方式

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }*/


    public static void main(String[] args) {
        String sharedTokenSecret="hellooauthhellooauthhellooauthhellooauth";
        Key key = new SecretKeySpec(sharedTokenSecret.getBytes(),
                SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("iss", "http://localhost:8081/");
        payloadMap.put("sub", "XIAOMINGTEST");
        payloadMap.put("aud", "APPID_RABBIT");
        payloadMap.put("exp", 1584105790703L);
        payloadMap.put("iat", 1584105948372L);

        String jws2 = Jwts.builder().setHeaderParams(headerMap).setClaims(payloadMap).signWith(key,SignatureAlgorithm.HS256).compact();

        System.out.println("jws2:" + jws2);

        /*String sharedTokenSecret2="hellooauthhellooauthhellooauthhellooaut0";
        Key key2 = new SecretKeySpec(sharedTokenSecret2.getBytes(),
                SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key2).build().parseClaimsJws(jws2);*/

        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws2);

        JwsHeader header = claimsJws.getHeader();
        Claims body = claimsJws.getBody();

        System.out.println("jwt header:" + header);
        System.out.println("jwt body:" + body);
        System.out.println("jwt sub:" + body.getSubject());
        System.out.println("jwt aud:" + body.getAudience());
        System.out.println("jwt iss:" + body.getIssuer());
        System.out.println("jwt exp:" + body.getExpiration());
        System.out.println("jwt iat:" + body.getIssuedAt());
    }

}
