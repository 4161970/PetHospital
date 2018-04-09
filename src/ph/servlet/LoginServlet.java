package ph.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ph.dao.UserDAO;
import ph.entity.User;
import ph.utils.MD5Util;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String url = null;
        String msg = null;
        String realcode = request.getSession().getAttribute("realcode").toString();
        //toString()转换为字符串格式
        String inputcode = request.getParameter("checkcode");
        if (realcode.equalsIgnoreCase(inputcode)) {
            UserDAO userDAO = new UserDAO();
            try {
                User user = userDAO.getByName(request.getParameter("name"));
                if (user == null) {
                    url = "/index.jsp";
                    msg = "用户名不存在";
                } else if (!user.getPwd().equals(/*MD5Util.MD5(*/request.getParameter("pwd")))/*) */{
                /*现在添加新用户到数据库中保存的初始密码就不是123456而是它的MD5编码，所以在登陆
                验证的时候也要将用户输入的登录密码经过MD5编码后的结果与数据库中的记录进行比较才行*/
                    url = "/index.jsp";
                    msg = "密码错误";
                } else {
                    request.getSession(true).setAttribute("user", user);
                    if(user.getRole().equals("admin")){
                        url = "/vetsearch.jsp";
                    }else if(user.getRole().equals("customer")){
                        url="/custindex.jsp";
                    }
                    msg = "登录成功";
                }
            } catch (Exception e) {
                url = "/index.jsp";
                msg = e.toString();
            }
        } else {
            url = "/index.jsp";
            msg = "验证码输入有误";
        }

        request.setAttribute("msg", msg);
        request.getRequestDispatcher(url).forward(request, response);
    }

}
