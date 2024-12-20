package API;

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
import java.util.ArrayList;
import java.util.List;

import DTO.StudentDTO;
import com.google.gson.Gson;

@WebServlet(urlPatterns = "/get")
public class GET extends HttpServlet {

    @Resource(name = "MyPool")
    DataSource pool;
    Gson gson = new Gson();
    Connection connection = null;
    PreparedStatement ps = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            StudentDTO studentDTO = new StudentDTO();
            connection = pool.getConnection();
            if (req.getParameter("option") != null) {
                getAll(resp);
            } else {
                ps = connection.prepareStatement("select * from students where id=?");
                ps.setInt(1, Integer.parseInt(req.getParameter("id")));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String firstname = rs.getString(2);
                    String lastname = rs.getString(3);
                    int age = rs.getInt(4);

                    studentDTO.setId(id);
                    studentDTO.setFrstName(firstname);
                    studentDTO.setLastName(lastname);
                    studentDTO.setAge(age);
                }
                resp.setContentType("application/json");
                String toJson = gson.toJson(studentDTO);
                resp.getWriter().write(toJson);
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

    public void getAll(HttpServletResponse resp) throws IOException {
        try {
            List<StudentDTO> studentDTOList = new ArrayList<>();
            connection = pool.getConnection();
            ps = connection.prepareStatement("select * from students");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstname = rs.getString(2);
                String lastname = rs.getString(3);
                int age = rs.getInt(4);
                StudentDTO studentDTO = new StudentDTO(id, firstname, lastname, age);
                studentDTOList.add(studentDTO);
            }
            String toJson = gson.toJson(studentDTOList);
            resp.setContentType("application/json");
            resp.getWriter().print(toJson);
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
