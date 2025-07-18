package com.library.MoileShop.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBcontext {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/phone_shop";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "12345678";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public static void main(String[] args) {
        DBcontext db = new DBcontext(); // tạo object DBcontext (đúng class ngoài cùng)
        try (Connection conn = db.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Kết nối thành công!");
            } else {
                System.out.println("❌ Kết nối thất bại!");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
