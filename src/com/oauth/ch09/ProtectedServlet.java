package com.oauth.ch09;

import com.oauth.ch09.OauthServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * **
 * 使用此类来模拟【受保护资源服务】
 */
@WebServlet("/ProtectedServlet-ch09")
public class ProtectedServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //省略验证代码

        String accessToken =  request.getParameter("token");

        //根据当时授权的token对应的权限范围，做相应的处理动作
        //不同权限对应不同的操作
        String[] scope = OauthServlet.tokenScopeMap.get(accessToken);

        StringBuffer sbuf = new StringBuffer();
        for(int i=0;i<scope.length;i++){
            sbuf.append(scope[i]).append("|");
        }

        if(sbuf.toString().indexOf("query")>0){
            queryGoods("");
        }

        if(sbuf.toString().indexOf("add")>0){
            addGoods("");
        }

        if(sbuf.toString().indexOf("del")>0){
            delGoods("");
        }

        //不同的用户对应不同的数据
        String user = OauthServlet.tokenMap.get(accessToken);
        queryOrders(user);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {




    }


    private String queryGoods(String id){
        return "";
    }

    private boolean addGoods(String goods){
        return true;
    }

    private boolean delGoods(String id){
        return true;
    }

    private String queryOrders(String user){
        return "";
    }

}
