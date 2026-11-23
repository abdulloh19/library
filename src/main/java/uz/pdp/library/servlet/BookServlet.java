package uz.pdp.library.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.library.DAO.AuthDao;
import uz.pdp.library.DAO.BookDao;
import uz.pdp.library.entity.Author;
import uz.pdp.library.entity.Book;

import java.io.IOException;

@WebServlet("/book")
public class BookServlet extends HttpServlet {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private BookDao bookDao = new BookDao();
    private AuthDao authDao = new AuthDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        double price = Double.parseDouble(req.getParameter("price"));
        String authorId = req.getParameter("author");
        int page = Integer.parseInt(req.getParameter("page"));

        Author author1 = authDao.findById(authorId);
        if (author1 == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("Author not found");
            return;
        }
        Book build1 = Book.builder()
                .title(title)
                .price(price)
                .authors(author1)
                .page(page)
                .build();
        bookDao.create(build1);
        resp.getWriter().println(gson.toJson(build1));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(gson.toJson(bookDao.findAll()));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book = bookDao.findById(req.getParameter("id"));
        String id = req.getParameter("id");
        String title = req.getParameter("title");
        double price = Double.parseDouble(req.getParameter("price"));
        String authorId = req.getParameter("author");
        int page = Integer.parseInt(req.getParameter("page"));
        Author author1 = authDao.findById(authorId);
        if (author1 == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("Author not found");
            return;
        }
        if (id == null || id.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("id yuborilishi shart");
            return;
        }
        if (title != null && !title.isEmpty()) {
            book.setTitle(title);
        }
        if (price > 0) {
            book.setPrice(price);
        }
        if (authorId != null && !authorId.isEmpty()) {
            book.setAuthors(author1);
        }
        if (page > 0) {
            book.setPage(page);
        }
        Book update = bookDao.update(book);
        resp.getWriter().println(gson.toJson(update));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        bookDao.deleteById(id);
        resp.getWriter().println("Book deleted");
    }
}
