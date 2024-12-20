package API;

import DTO.StudentDTO;
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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/post")
public class POST extends HttpServlet {

    @Resource(name = "MyPool")
    DataSource pool;
    Gson gson = new Gson();
    Connection connection = null;
    PreparedStatement ps = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String header = req.getHeader("Content-Type");
        if (header.equals("application/json")) {
            try {
                StudentDTO studentDTO = gson.fromJson(req.getReader(), StudentDTO.class);
                connection = pool.getConnection();
                ps = connection.prepareStatement("insert into students (first_name,last_name,age) values(?,?,?)");
                ps.setString(1, studentDTO.getFrstName());
                ps.setString(2, studentDTO.getLastName());
                ps.setInt(3, studentDTO.getAge());
                int affextedRows = ps.executeUpdate();
                if (affextedRows > 0) {
                    resp.getWriter().println("Student added successfully");
                } else {
                    resp.getWriter().println("Student added Unsuccessfully");
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

}
