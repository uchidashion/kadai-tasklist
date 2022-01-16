package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Tasks;
import utils.DBUtil;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //_tokenのパラメータを受け取る
        String _token = request.getParameter("_token");
        //_tokenが空、もしくはセッションIDと異なっている場合はDBに登録できないようにする
        if(_token != null && _token.equals(request.getSession().getId())) {
            //EntityManagerのインスタンス生成
            EntityManager em = DBUtil.createEntityManager();

            //セッションスコープからタスクのIDを取得し
            //該当のIDのタスク一件のみをデータベースから取得
            //取り出したIDはObject型のため、Integerにキャスト変換
            Tasks t = em.find(Tasks.class, (Integer)(request.getSession().getAttribute("task_id")));

            //フォーム内容を各フィールドに上書き
            String content = request.getParameter("content");
            t.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setUpdated_at(currentTime);

            //データベースの更新
            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();

            //セッションスコープ内の不要になったデータを削除
            request.getSession().removeAttribute("task_id");

            //indexページに自動遷移
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}
