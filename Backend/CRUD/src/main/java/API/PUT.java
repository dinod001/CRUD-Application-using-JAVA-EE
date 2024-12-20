package API;

import DTO.StudentDTO;
import com.google.gson.Gson;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

@WebServlet(urlPatterns = "/put")
public class PUT extends HttpServlet {
    @Resource(name = "MyPool")
    DataSource pool;
    Gson gson = new Gson();
    Connection connection = null;
    PreparedStatement ps = null;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String header = req.getHeader("Content-Type");
        if (header.equals("application/json")) {
            try {
                StudentDTO studentDTO = gson.fromJson(req.getReader(), StudentDTO.class);
                connection = pool.getConnection();
                ps = connection.prepareStatement("UPDATE students SET first_name = ?, last_name = ?, age = ? WHERE id = ?");
                ps.setString(1, studentDTO.getFrstName());
                ps.setString(2, studentDTO.getLastName());
                ps.setInt(3, studentDTO.getAge());
                ps.setInt(4, studentDTO.getId());
                int affextedRows = ps.executeUpdate();
                if (affextedRows > 0) {
                    resp.getWriter().println("Student Updated successfully");
                } else {
                    resp.getWriter().println("Student Updated Unsuccessfully");
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

    public Boolean checkAvailability(int id) {
        try {
            connection = pool.getConnection();
            ps = connection.prepareStatement("select * from students where id=?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(1) == id) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
