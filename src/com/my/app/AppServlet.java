package com.my.app;

import com.my.util.HttpURLClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/AppServlet")
public class AppServlet extends HttpServlet {



    String oauthURl="http://localhost:8080/OauthServlet";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        System.out.println("start parse code...");

        String code = request.getParameter("code");

        Map<String, String> params = new HashMap<String, String>();
        params.put("code","s3ers89u");
        params.put("grant_type","authorization_code");

        String result = HttpURLClient.doPost(oauthURl,HttpURLClient.mapToStr(params));


        System.out.println("result:"+result);


    }
}
