package com.oauth.ch09;

import com.my.util.URLParamsUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
import java.util.Random;
import java.util.UUID;

@WebServlet("/OauthServlet-ch09")
public class OauthServlet extends HttpServlet {

    //模拟授权码、令牌等数据存储
    static Map<String,String> codeMap =  new HashMap<String, String>();
    static Map<String,String[]> codeScopeMap =  new HashMap<String, String[]>();

    static Map<String,String> tokenMap =  new HashMap<String, String>();
    static Map<String,String[]> tokenScopeMap =  new HashMap<String, String[]>();

    static Map<String,String> refreshTokenMap =  new HashMap<String, String>();

    static Map<String,String> appMap =  new HashMap<String, String>();

    static Map<String,String> reqidMap =  new HashMap<String, String>();


    static {

        //模拟第三方软件注册之后的数据库存储
        appMap.put("app_id","APPID_RABBIT");
        appMap.put("app_secret","APPSECRET_RABBIT");
        appMap.put("redirect_uri","http://localhost:8080/AppServlet-ch09");
        appMap.put("scope","today history");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("start accept post req, generate access_toen");
        String reqType = request.getParameter("reqType");

        String grantType = request.getParameter("grant_type");
        String appId = request.getParameter("app_id");
        String appSecret = request.getParameter("app_secret");

        String responseType = request.getParameter("response_type");
        String redirectUri =request.getParameter("redirect_uri");
        String scope =request.getParameter("scope");

        //处理用户点击approve按钮动作
        if("approve".equals(reqType)){
            String reqid = request.getParameter("reqid");//假设一定能够获取到值

            if(!reqidMap.containsKey(reqid)){
                return;
            }

            if("code".equals(responseType)){

                String[] rscope =request.getParameterValues("rscope");

                if(!checkScope(rscope)){//验证权限范围，对又验证一次
                    //超出注册的权限范围
                    System.out.println("out of scope ...");
                    return;
                }

                String code = generateCode(appId,"USERTEST");//模拟登陆用户为USERTEST

                codeScopeMap.put(code,rscope);//授权范围与授权码做绑定

                Map<String, String> params = new HashMap<String, String>();
                params.put("code",code);

                String toAppUrl = URLParamsUtil.appendParams(redirectUri,params);//构造第三方软件的回调地址，并重定向到该地址

                response.sendRedirect(toAppUrl);//授权码流程的【第二次】重定向
            }
        }

        //处理授权码流程中的 颁发访问令牌 环节
        if("authorization_code".equals(grantType)){

            if(!appMap.get("app_id").equals(appId)){
                response.getWriter().write("app_id is not available");
                return;
            }

            if(!appMap.get("app_secret").equals(appSecret)){
                response.getWriter().write("app_secret is not available");
                return;
            }

            String code = request.getParameter("code");

            if(!isExistCode(code)){//验证code值
                return;
            }
            codeMap.remove(code);//授权码一旦被使用，须要立即作废

            System.out.println("start generate access_toen");
            String accessToken = generateAccessToken(appId,"USERTEST");//生成访问令牌access_token的值
            tokenScopeMap.put(accessToken,codeScopeMap.get(code));//授权范围与访问令牌绑定

            //GENATE ID TOKEN
            String id_token=genrateIdToken(appId,"XIAOMINGTEST");//模拟用户小明登录

            response.getWriter().write(accessToken+"&"+id_token);

        }

    }


    /**
     * genrate
     * @param appId
     * @param user
     * @return
     */
    private String genrateIdToken(String appId,String user){
        String sharedTokenSecret="hellooauthhellooauthhellooauthhellooauth";
        Key key = new SecretKeySpec(sharedTokenSecret.getBytes(),
                SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("iss", "http://localhost:8081/");
        payloadMap.put("sub", user);
        payloadMap.put("aud", appId);
        payloadMap.put("exp", 1584105790703L);
        payloadMap.put("iat", 1584105948372L);

        return Jwts.builder().setHeaderParams(headerMap).setClaims(payloadMap).signWith(key,SignatureAlgorithm.HS256).compact();
    }




    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        String responseType = request.getParameter("response_type");
        String redirectUri =request.getParameter("redirect_uri");
        String appId = request.getParameter("app_id");
        String scope = request.getParameter("scope");

        System.out.println("8081 GET responseType: "+responseType);
        System.out.println("8081 GET redirect_uri: "+redirectUri);
        System.out.println("8081 GET app_id: "+appId);
        System.out.println("8081 GET scope: "+scope);

        if(!appMap.get("app_id").equals(appId)){
            return;
        }

        if(!appMap.get("redirect_uri").equals(redirectUri)){
            return;
        }


        //验证第三方软件请求的权限范围是否与当时注册的权限范围一致
        if(!checkScope(scope)){
            //超出注册的权限范围
            return;
        }

        //生成页面reqid
        String reqid = String.valueOf(System.currentTimeMillis());
        reqidMap.put(reqid,reqid);//保存该reqid值

        request.setAttribute("reqid",reqid);
        request.setAttribute("response_type",responseType);
        request.setAttribute("redirect_uri",redirectUri);
        request.setAttribute("app_id",appId);

        //跳转到授权页面
        request.getRequestDispatcher("/approve-09.jsp").forward(request,response);

        //至此颁发授权码code的准备工作完毕

    }


    /**
     * 生成code值
     * @return
     */
    private String generateCode(String appId,String user) {
        Random r = new Random();
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            strb.append(r.nextInt(10));
        }

        String code = strb.toString();


        codeMap.put(code,appId+"|"+user+"|"+System.currentTimeMillis());//在这一篇章我们仅作为演示用，实际这应该是一个全局内存数据库，有效期官方建议是10分钟

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

        String expires_in = "1";//1天时间过期

        tokenMap.put(accessToken,appId+"|"+user+"|"+System.currentTimeMillis()+"|"+expires_in);//在这一篇章我们仅作为演示用，实际这应该是一个全局数据库,并且有有效期

        return accessToken;
    }


    /**
     * 生成refresh_token值
     * @param appId
     * @param user
     * @return
     */
    private String generateRefreshToken(String appId,String user){

        String refreshToken = UUID.randomUUID().toString();

        refreshTokenMap.put(refreshToken,appId+"|"+user+"|"+System.currentTimeMillis());//在这一篇章我们仅作为演示用，实际这应该是一个全局数据库,并且有有效期

        return refreshToken;
    }

    /**
     * 是否存在code值
     * @param code
     * @return
     */
    private boolean isExistCode(String code){
        return codeMap.containsKey(code);
    }

    /**
     * 验证权限
     * @param scope
     * @return
     */
    private boolean checkScope(String scope){

        System.out.println("appMap size: "+appMap.size());
        System.out.println("appMap scope: "+appMap.get("scope"));
        System.out.println("scope: "+scope);

        return appMap.get("scope").contains(scope);//简单模拟权限验证
    }


    /**
     *
     * @param rscope
     * @return
     */
    private boolean checkScope(String[] rscope){
        String scope="";

        for(int i=0; i<rscope.length ;i++){
            scope=scope+rscope[i];
        }

        return appMap.get("scope").replace(" ","").contains(scope);//简单模拟权限验证
    }


    public static void main(String[] args) {

//        System.out.println(new OauthServlet().generateCode());
        System.out.println(UUID.randomUUID());

    }

}
