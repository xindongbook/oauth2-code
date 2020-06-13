package com.oauth.ch03;

import com.my.util.URLParamsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * **
 * 使用此类来模拟第三方软件的首页
 * http://localhost:8080/AppIndexServlet-ch03
 */
@WebServlet("/AppIndexServlet-ch03")
public class AppIndexServlet extends HttpServlet {


    //8080:三方软件，8081：授权服务，8081：受保护资源服务 为了演示方便我们将授权服务和受保护资源服务放在同一个服务上面

    String oauthUrl = "http://localhost:8081/OauthServlet-ch03?reqType=oauth";
    String redirectUrl = "http://localhost:8080/AppServlet-ch03";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //授权码许可流程，DEMO CODE
        System.out.println("app index ...");

        Map<String, String> params = new HashMap<String, String>();
        params.put("response_type","code");
        params.put("redirect_uri",redirectUrl);
        params.put("app_id","APPID_RABBIT");
        params.put("scope","today history");


        String toOauthUrl = URLParamsUtil.appendParams(oauthUrl,params);//构造请求授权的URl

        System.out.println("toOauthUrl: "+toOauthUrl);

        response.sendRedirect(toOauthUrl);//授权码流程的【第一次】重定向

    }
}
