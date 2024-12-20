package API;

import com.google.gson.Gson;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/delete")
public class DELETE extends HttpServlet {

    @Resource(name = "MyPool")
    DataSource pool;
    Gson gson = new Gson();
    Connection connection = null;
    PreparedStatement ps = null;

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            connection = pool.getConnection();
            ps = connection.prepareStatement("DELETE FROM students WHERE id=?");
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                resp.getWriter().println("Delete Successful");
            } else {
                resp.getWriter().println("Delete Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                ps.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
