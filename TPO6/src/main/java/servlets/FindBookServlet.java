package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/FindBook")
public class FindBookServlet extends HttpServlet{
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		try {
			Context init = new InitialContext();
			Context context = (Context)init.lookup("java:comp/env");
			dataSource = (DataSource) context.lookup("jdbc/sqlserver");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		serviceRequest(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		serviceRequest(request, response);
	}

	private void serviceRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Connection conn = null;
		Statement st = null;
		ResultSet resultSet;
		try {
			synchronized(dataSource) {
				conn= dataSource.getConnection();	
			}
			st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = st.executeQuery("SELECT * FROM Books WHERE BookName = '" + req.getParameter("Title")
			+ "' OR Author = '" + req.getParameter("Author") + "'"); 
			req.setAttribute("resultSet",resultSet);
			req.getRequestDispatcher("DisplayBooks.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				st.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
