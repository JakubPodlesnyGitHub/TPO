package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Login")
public class LogServlet extends HttpServlet {
	private RequestDispatcher reqDispatcher;
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
		PrintWriter printWriter = resp.getWriter();
		Connection conn = null;
		Statement st = null;
		ResultSet resultSet = null;
		Map<String, String[]> map = req.getParameterMap();
		try {
			synchronized(dataSource) {
				conn= dataSource.getConnection();
			}
			st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			resultSet = st.executeQuery("SELECT 1 FROM LibraryClients WHERE ClientLogin = '"
					+ map.get("Login")[0] + "' AND ClientPassword = '" + map.get("Password")[0] + "'");
			
			if (resultSet.next() == false) {
				reqDispatcher = req.getRequestDispatcher("IncorrectLoginOrPassword.jsp");
				reqDispatcher.forward(req, resp);
			} else {
				reqDispatcher = req.getRequestDispatcher("Menu.jsp");
				reqDispatcher.forward(req, resp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}finally {
			try {
				st.close();
				resultSet.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
