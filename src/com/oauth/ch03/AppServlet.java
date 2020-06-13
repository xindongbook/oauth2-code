package com.oauth.ch03;

import com.my.util.HttpURLClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/AppServlet-ch03")
public class AppServlet extends HttpServlet {



    String oauthURl="http://localhost:8081/OauthServlet-ch03";
    String protectedURl="http://localhost:8081/ProtectedServlet-ch03";

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
        String accessToken = HttpURLClient.doPost(oauthURl,HttpURLClient.mapToStr(params));

        System.out.println("accessToken:"+accessToken);

        //使用 accessToken 请求受保护资源服务

        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put("app_id","APPID_RABBIT");
        paramsMap.put("app_secret","APPSECRET_RABBIT");
        paramsMap.put("token",accessToken);

        String result = HttpURLClient.doPost(protectedURl,HttpURLClient.mapToStr(paramsMap));


    }
}
