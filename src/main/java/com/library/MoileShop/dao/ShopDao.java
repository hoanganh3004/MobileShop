package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.Account;
import com.library.MoileShop.entity.Product;
import com.library.MoileShop.entity.Category;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShopDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    //lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("masp"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //lấy tất cả danh mục
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //lấy sản phẩm theo category_id
    public List<Product> getProductsbyCid(String cid) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category_id = ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, cid);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("masp"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //Phương thức lấy sản phẩm theo id, trả về đối tượng Product
    public Product getProductsbyID(String id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("masp"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //tìm kiếm sản phẩm theo tên
    public List<Product> searchByName(String txtSearch) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name LIKE ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + txtSearch + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("masp"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //tìm kiếm sản phẩm theo tên và danh mục
    public List<Product> searchByNameAndCategory(String txtSearch, int categoryId) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name LIKE ? AND category_id = ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + txtSearch + "%");
            ps.setInt(2, categoryId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("masp"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //đăng ký tài khoản mới
    public boolean registerAccount(Account acc) {
        String query = "INSERT INTO accounts (code, username, password, full_name, email, phone, address, role, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            String hashedPassword = BCrypt.hashpw(acc.getPassword(), BCrypt.gensalt());

            ps.setString(1, acc.getCode());
            ps.setString(2, acc.getUsername());
            ps.setString(3, hashedPassword);
            ps.setString(4, acc.getFullName());
            ps.setString(5, acc.getEmail());
            ps.setString(6, acc.getPhone());
            ps.setString(7, acc.getAddress());
            ps.setString(8, acc.getRole());
            ps.setInt(9, acc.getStatus());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //  đăng nhập
    public Account login(String username, String password) throws Exception {
        String query = "SELECT * FROM accounts WHERE username = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                if (BCrypt.checkpw(password, hashed)) {
                    return new Account(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("username"),
                            hashed,
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("role"),
                            rs.getInt("status"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    //lấy tài khoản theo email
    public Account getAccountByEmail(String email) {
        String query = "SELECT * FROM accounts WHERE email = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role"),
                        rs.getInt("status"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Phương thức cập nhật mật khẩu
    public void updatePassword(int accountId, String hashedPassword) {
        String query = "UPDATE accounts SET password = ? WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //  kiểm tra xem username đã tồn tại chưa
    public boolean isUsernameExists(String username) {
        String query = "SELECT 1 FROM accounts WHERE username = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //kiểm tra xem email đã tồn tại chưa
    public boolean isEmailExists(String email) {
        String query = "SELECT 1 FROM accounts WHERE email = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //lấy id lớn nhất của tài khoản
    public int getLastAccountId() {
        String sql = "SELECT MAX(id) AS max_id FROM accounts";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("max_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
