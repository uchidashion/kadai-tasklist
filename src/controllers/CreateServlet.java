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
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponseresponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //_tokenを受け取る
        String _token = request.getParameter("_token");
        //_tokenに値がない、もしくはセッションIDと値が異なったりしたらデータ登録できない
        if(_token != null && _token.equals(request.getSession().getId())) {
            //EntityManagerのインスタンスを生成
            EntityManager em = DBUtil.createEntityManager();

            //DBに問い合わせを始める
            em.getTransaction().begin();

            //Tasksのインスタンス生成
            Tasks t = new Tasks();

            //タスクの内容を格納
            String content = request.getParameter("content");
            t.setContent(content);

            //現在時刻と更新日時を格納
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setCreated_at(currentTime);
            t.setUpdate_at(currentTime);

            //DBにセーブ
            em.persist(t);
            //データの確定
            em.getTransaction().commit();
            //DBの接続を閉じる
            em.close();

            //IndexServletに自動遷移
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}
