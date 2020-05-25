package com.my.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class URLParamsUtil {



    public static String appendParams(String url, Map<String, String> params){
        if(null == url){
            return "";
        }else if(params.isEmpty()){
            return url.trim();
        }else{
            StringBuffer sb = new StringBuffer("");
            Set<String> keys = params.keySet();
            for (String key : keys) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);

            url = url.trim();
            int length = url.length();
            int index = url.indexOf("?");
            if(index > -1){
                if((length - 1) == index){//url最后一个符号为？，如：http://wwww.yy.com?
                    url += sb.toString();
                }else{//情况为：http://wwww.baidu.com?aa=11
                    url += "&" + sb.toString();
                }
            }else{//url后面没有问号，如：http://wwww.baidu.com
                url += "?" + sb.toString();
            }
            return url;
        }
    }


    public static String mapToStr(Map<String, String> params){
        StringBuffer sb = new StringBuffer("");
        Set<String> keys = params.keySet();
        for (String key : keys) {
            sb.append(key).append("=").append(params.get(key)).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("code","s3ers89u");
        params.put("grant_type","authorization_code");



        String url="http://localhost/OauthServlet";

//        System.out.println(URLParamsUtil.appendParams(url,params));
        System.out.println(URLParamsUtil.mapToStr(params));


    }
}


