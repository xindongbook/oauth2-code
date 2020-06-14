package com.oauth.ch06;

import com.my.util.HttpURLClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/AppServlet-ch02")
public class AppServlet extends HttpServlet {



    String oauthURl="http://localhost:8081/OauthServlet-ch02";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        //授权码许可流程，DEMO CODE
        /*System.out.println("start parse code...");

        String code = request.getParameter("code");

        Map<String, String> params = new HashMap<String, String>();
        params.put("code",code);
        params.put("grant_type","authorization_code");
        params.put("app_id","APPIDTEST");
        params.put("app_secret","APPSECRETTEST");

        String accessToken = HttpURLClient.doPost(oauthURl,HttpURLClient.mapToStr(params));

        System.out.println("accessToken:"+accessToken);*/


        //隐式许可流程（模拟），DEMO CODE
        /*String accessToken = request.getParameter("access_token");
        System.out.println("accessToken:"+accessToken);*/

        //第三方软件凭据许可流程，DEMO CODE
        /*Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type","client_credentials");
        params.put("app_id","APPIDTEST");
        params.put("app_secret","APPSECRETTEST");

        String accessToken = HttpURLClient.doPost(oauthURl,HttpURLClient.mapToStr(params));
        System.out.println("accessToken:"+accessToken);*/


        //资源拥有者凭据许可流程，DEMO CODE
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type","password");
        params.put("app_id","APPIDTEST");
        params.put("app_secret","APPSECRETTEST");
        params.put("name","NAMETEST");
        params.put("password","PASSWORDTEST");

        String accessToken = HttpURLClient.doPost(oauthURl,HttpURLClient.mapToStr(params));
        System.out.println("accessToken:"+accessToken);

    }
}
