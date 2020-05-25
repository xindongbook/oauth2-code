package com.oauth.ch02;

import com.my.util.URLParamsUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@WebServlet("/OauthServlet-ch02")
public class OauthServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("start accept post req, generate access_toen");

        String grantType = request.getParameter("grant_type");
        String appId = request.getParameter("app_id");

        if(!"APPIDTEST".equals(appId)){
            response.getWriter().write("app_id is not available");
            return;
        }


        if("authorization_code".equals(grantType)){

            String code = request.getParameter("code");
            if(null == request.getSession().getAttribute(code)){//验证code值
                return;
            }

            System.out.println("start generate access_toen");
            String accessToken = generateAccessToken(appId,"USERTEST");//生成访问令牌access_token的值

            response.getWriter().write(accessToken);

        }else if("client_credentials".equals(grantType)){
            String appSecret = request.getParameter("app_secret");

            if(!"APPSECRETTEST".equals(appSecret)){
                response.getWriter().write("app_secret is not available");
                return;
            }

            String accessToken = generateAccessToken(appId,"USERTEST");//生成访问令牌access_token的值

            response.getWriter().write(accessToken);

        }else if("password".equals(grantType)){
            String appSecret = request.getParameter("app_secret");
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if(!"APPSECRETTEST".equals(appSecret)){
                response.getWriter().write("app_secret is not available");
                return;
            }
            if(!"USERNAMETEST".equals(username)){
                response.getWriter().write("username is not available");
                return;
            }

            if(!"PASSWORDTEST".equals(password)){
                response.getWriter().write("password is not available");
                return;
            }

            String accessToken = generateAccessToken(appId,"USERTEST");//生成访问令牌access_token的值

            response.getWriter().write(accessToken);

        }else if("refresh_token".equals(grantType)){


        }else{

        }


    }




    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String responseType = request.getParameter("response_type");
        String redirectUri =request.getParameter("redirect_uri");
        String appId = request.getParameter("app_id");
//        String appSecret = request.getParameter("app_secret");
//        String code = request.getParameter("code");
//        String accessToken = request.getParameter("access_token");
//        String refreshToken = request.getParameter("refresh_token");
//        String scop = request.getParameter("scop");
//        String grantType = request.getParameter("grant_type");
//        String tokenType = request.getParameter("token_type");
//        String state = request.getParameter("state");

        if(!"APPIDTEST".equals(appId)){
            return;
        }

        if("code".equals(responseType)){
            //授权码许可流程，DEMO CODE
            System.out.println("oauth accept code req...");

            String code = generateCode(appId,"USERTEST",request);//生成code值
            Map<String, String> params = new HashMap<String, String>();
            params.put("code",code);

            String toAppUrl = URLParamsUtil.appendParams(redirectUri,params);//构造第三方软件的回调地址，并重定向到该地址

            System.out.println("toAppUrl: "+toAppUrl);

            response.sendRedirect(toAppUrl);


        }else if("token".equals(responseType)){
            //隐式许可流程（模拟），DEMO CODE，注意 该流程全是在前端通信中完成的
            String accessToken = generateAccessToken(appId,"USERTEST");//生成访问令牌access_token的值

            Map<String, String> params = new HashMap<String, String>();
            params.put("redirect_uri",redirectUri);
            params.put("access_token",accessToken);

            String toAppUrl = URLParamsUtil.appendParams(redirectUri,params);//构造第三方软件的回调地址，并重定向到该地址

            System.out.println("toAppUrl: "+toAppUrl);

            response.sendRedirect(toAppUrl);//使用sendRedirect方式模拟前端通信

        }




    }


    /**
     * 生成code值
     * @return
     */
    private String generateCode(String appId,String user,HttpServletRequest request) {
        Random r = new Random();
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            strb.append(r.nextInt(10));
        }

        String code = strb.toString();


        request.getSession().setAttribute(code,appId+"|"+user);//在这一篇章我们仅作为演示用，实际这应该是一个全局内存数据库，有效期官方建议是10分钟

        return code;
    }


    /**
     * 生成access_token值
     * @param appId
     * @param user
     * @return
     */
    private String generateAccessToken(String appId,String user){

        String accessToken = UUID.randomUUID().toString();

        Map<String,String> map = new HashMap<String, String>();//在这一篇章我们仅作为演示用，实际这应该是一个全局数据库,并且有有效期
        map.put(accessToken,appId+"|"+user);

        return accessToken;
    }


    public static void main(String[] args) {

//        System.out.println(new OauthServlet().generateCode());
        System.out.println(UUID.randomUUID());

    }

}
