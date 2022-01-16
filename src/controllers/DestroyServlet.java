package controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Tasks;
import utils.DBUtil;

/**
 * Servlet implementation class DestroyServlet
 */
@WebServlet("/destroy")
public class DestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //_tokenのパラメータを受け取る
        String _token = request.getParameter("_token");
        //_tokenが空、もしくはセッションIDと異なる場合はDBの編集できない
        if(_token != null && _token.equals(request.getSession().getId())) {
            //EntityMManagerのインスタンス生成
            EntityManager em = DBUtil.createEntityManager();

            //セッションスコープからタスクのIDを取得して
            //該当のIDのタスク一件のみをDBから取得
            Tasks t = em.find(Tasks.class, (Integer)(request.getSession().getAttribute("task_id")));

            //DB上にある該当のデータの削除
            em.getTransaction().begin();
            em.remove(t);
            em.getTransaction().commit();
            em.close();

            //セッションスコープ内の不要データを削除
            request.getSession().removeAttribute("task_id");

            //index.jspに自動遷移
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}
