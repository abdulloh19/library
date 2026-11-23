package uz.pdp.library.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.library.DAO.AuthDao;
import uz.pdp.library.entity.Author;

import java.io.IOException;

@WebServlet("/author")
public class AuthorServlet extends HttpServlet {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private AuthDao authDao = new AuthDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = gson.toJson(authDao.findAll());
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullname = req.getParameter("fullname");
        String country = req.getParameter("country");
        int birthday = Integer.parseInt(req.getParameter("birthday"));
        Author author = Author.builder()
                .fullName(fullname)
                .country(country)
                .birthYear(birthday)
                .build();
        authDao.create(author);
        resp.getWriter().println(gson.toJson(author));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String id1 = req.getParameter("id");
        Author author = authDao.findById(id1);
        String id = req.getParameter("id");
        String fullname = req.getParameter("fullname");
        String country = req.getParameter("country");
        String birthdayParam = req.getParameter("birthday");
        if(id == null || id.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"id yuborilishi shart\"}");
            return;
        }
        if (fullname != null && !fullname.isBlank()) {
            author.setFullName(fullname);
        }
        if (country != null && !country.isBlank()) {
            author.setCountry(country);
        }
        if (birthdayParam != null && !birthdayParam.isBlank()) {
            author.setBirthYear(Integer.parseInt(birthdayParam));
        }
        resp.getWriter().println(gson.toJson(author));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        authDao.deleteById(id);
    }

}
