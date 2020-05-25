package com.my.oauth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@WebServlet("/OauthServlet")
public class OauthServlet extends HttpServlet {


    String authUrl="http://localhost/OauthServlet/response_type=code&redirect_uri=redirect_uri&app_id=app_id&app_secret=app_secret";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("start accept post req, generate access_toen");

        String grantType = request.getParameter("grant_type");
        if("authorization_code".equals(grantType)){

            System.out.println("start generate access_toen");


            response.getWriter().write("ACCESSTOKEN");

        }
    }




    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {



//        String responseType = "";//响应类型 告诉授权服务要求返回什么，比如code，token
        String appId = "";//第三方软件ID
        String appSecret = "";//第三方软件秘钥
        String redirecturi = "";//重定向地址
        String code = "";//授权码
        String accessToken = "";//访问令牌
        String refreshToken = "";//刷新令牌
        String scop = "" ;//授权访问权限范围
        String grantType = "";//授权类型 告诉授权服务我要采用什么样的授权类型去请求
        String tokenType = "";// 令牌类型
        String state = "";// 用在安全防护上 比如跨站保护



        String responseType = request.getParameter("response_type");
        request.getParameter("app_id");
        request.getParameter("app_secret");
        request.getParameter("redirect_uri");
        request.getParameter("code");
        request.getParameter("access_token");
        request.getParameter("refresh_token");
        request.getParameter("scop");
        request.getParameter("grant_type");
        request.getParameter("token_type");
        request.getParameter("state");

        if("code".equals(responseType)){

            String url = new StringBuilder(redirecturi).append("").toString();
            response.sendRedirect(url);

        }else if("token".equals(responseType)){

        }



        if("authorization_code".equals(grantType)){

            System.out.println("start generate access_toen");

            // TODO: 2020/2/19  生成access_token

            response.getWriter().write("ACCESSTOKEN");

        }else if("client_credentials".equals(grantType)){


        }else if("password".equals(grantType)){


        }else if("refresh_token".equals(grantType)){


        }else{


        }


    }


    private String generateCode(){
        Random r = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            rs.append(r.nextInt(10));
        }
        return rs.toString();

    }


    private String generateAccessToken(String appId,String userPin){

        return "";
    }


    public static void main(String[] args) {

        System.out.println(new OauthServlet().generateCode());

    }

}
