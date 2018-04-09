<%--
  Created by IntelliJ IDEA.
  User: 成王败寇
  Date: 2018/3/27
  Time: 18:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <link rel="stylesheet" href="styles.css">
    <title>首页</title>
  </head>
  <body>
  <div id="container">
    <div id="header">
      <h1>社区宠物诊所</h1>
    </div>
    <div id="content">
      <form action="LoginServlet" method="post">
        <table>
          <tr>
            <td>用户名</td>
            <td><input type="text" name="name"/></td>
          </tr>
          <tr>
            <td>密码</td>
            <td><input type="password" name="pwd"/></td>
          </tr>
          <tr>
            <td>验证码</td>
            <td><input type="text" name="checkcode"/></td>
          </tr>
          <tr>
            <td>点击刷新</td>
            <td><input type="image" src="CheckCode" onclick="javascript:src='CheckCode?+rand=Math.random()'"></td>
          </tr>
          <tr class="cols2">
            <td colspan="2"><input type="submit" value="登录" /><input  type="reset" value="重置" /></td>
          </tr>
          <tr class="cols2">
            <td colspan="2" class="info"><%=request.getAttribute("msg")==null?"":request.getAttribute("msg") %></td>
          </tr>
        </table>
      </form>
    </div>
    <div id="footer"></div>
  </div>
  </body>
</html>
