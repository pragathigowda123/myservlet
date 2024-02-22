package form_registration;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/save", loadOnStartup = 1)
public class Registration extends HttpServlet {
	Connection connection;

	@Override
	public void init() throws ServletException {
		loadingClass();
		establishConnection();
		createTable();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String mobile = req.getParameter("mobile");
		String date = req.getParameter("dob");
		///String email = req.getParameter("email");//
		String gender = req.getParameter("gender");
		String[] subjects = req.getParameterValues("subject");
		String country = req.getParameter("country");
		String color = req.getParameter("color");

		if (insertValues(name, mobile, date, gender, subjects, country, color))
			resp.getWriter().print("<h1 style='color:" + color + "'>Data Inserted Success</h1>");
		else
			resp.getWriter().print("<h1 style='color:red'>Data Not Inserted</h1>");

	}
	private void closeConnection() {
		try {
			connection.close();
			System.out.println("Connection Closed");
		} catch (SQLException e) {
			System.out.println("Connection failed to Close");
		}
	}

	private boolean insertValues(String name, String mobile, String dob,  String Gender, String[] subjects, String country, String color) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into student values(?,?,?,?,?,?,?)");
			preparedStatement.setString(1, name);
			preparedStatement.setLong(2, Long.parseLong(mobile));
			preparedStatement.setDate(3, Date.valueOf(dob));
			//preparedStatement.setString(4, email);//
			preparedStatement.setString(4, Gender);
			preparedStatement.setString(5, Arrays.toString(subjects));
			preparedStatement.setString(6, color);
			preparedStatement.setString(7, country);
			preparedStatement.execute();
			System.out.println("Data Inserted");
			return true;

		} catch (SQLException e) {
			System.out.println("Data Not Inserted");
			return false;
		}
	}

	private void loadingClass() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver Class Loaded");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Class Not Loaded");
		}
	}
	private void establishConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?createDatabaseIfNotExist=true",
					"root", "root");
			System.out.println("Connection Established");
		} catch (SQLException e) {
			System.out.println("Connection Not Established");
		}
	}

	private void createTable() {
		try {
			Statement statement = connection.createStatement();
			statement.execute(
					"create table student(name varchar(30),mobile bigint primary key,dob date,gender varchar(10),subjects varchar(50),color varchar(50),country varchar(50))");
			System.out.println("Table got Created");
		} catch (SQLException e) {
			System.out.println("Table Already Exists or Qurery is Wrong");
		}
	}
}

