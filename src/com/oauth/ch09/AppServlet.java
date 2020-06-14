package com.oauth.ch09;

import com.my.util.HttpURLClient;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;


/**
 * **
 * 使用此类来模拟【第三方软件的Server端】
 *
 */
@WebServlet("/AppServlet-ch09")
public class AppServlet extends HttpServlet {

    String oauthURl="http://localhost:8081/OauthServlet-ch09";
    String protectedURl="http://localhost:8081/ProtectedServlet-ch09";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //授权码许可流程，DEMO CODE

        String code = request.getParameter("code");

        Map<String, String> params = new HashMap<String, String>();
        params.put("code",code);
        params.put("grant_type","authorization_code");
        params.put("app_id","APPID_RABBIT");
        params.put("app_secret","APPSECRET_RABBIT");

        System.out.println("start post code for token ...");
        String result = HttpURLClient.doPost(oauthURl,HttpURLClient.mapToStr(params));

        System.out.println("result:"+result);
        String[] arry = result.split("&");
        String accessToken = arry[0];
        String id_token = arry[1];

        System.out.println("accessToken:"+accessToken);
        System.out.println("id_token:"+id_token);

        //获取用户登录标识
        Map<String,String> map = parseJwt(id_token);

        request.setAttribute("sub",map.get("sub"));

        //跳转到授权页面
        request.getRequestDispatcher("/oidc.jsp").forward(request,response);


        /*//使用 accessToken 请求受保护资源服务
        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put("app_id","APPID_RABBIT");
        paramsMap.put("app_secret","APPSECRET_RABBIT");
        paramsMap.put("token",accessToken);

        HttpURLClient.doPost(protectedURl,HttpURLClient.mapToStr(paramsMap));
*/

    }

    private Map<String,String> parseJwt(String jwt){
        String sharedTokenSecret="hellooauthhellooauthhellooauthhellooauth";
        Key key = new SecretKeySpec(sharedTokenSecret.getBytes(),
                SignatureAlgorithm.HS256.getJcaName());

        Map<String,String> map = new HashMap<String, String>();

        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);

//        JwsHeader header = claimsJws.getHeader();
        Claims body = claimsJws.getBody();

//        System.out.println("jwt header:" + header);
        System.out.println("jwt body:" + body);

        map.put("sub",body.getSubject());
        map.put("aud",body.getAudience());
        map.put("iss",body.getIssuer());

        return map;
    }


    public static void main(String[] args) {

        String ss="95fd88bc-c69e-4add-bf3a-5d75766b85a7&eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJYSUFPTUlOR1RFU1QiLCJhdWQiOiJBUFBJRF9SQUJCSVQiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEvIiwiZXhwIjoxNTg0MTA1NzkwNzAzLCJpYXQiOjE1ODQxMDU5NDgzNzJ9.SoJT62wYOMihpaH3Ttxf3WYwnC6qEyKbJ-bF7jMqxL8";

        String[] arry = ss.split("&");
        System.out.println("access_token:"+arry[0]);
        System.out.println("id_token:"+arry[1]);
    }

}
