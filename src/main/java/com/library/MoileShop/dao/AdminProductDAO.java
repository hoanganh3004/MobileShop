package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminProductDAO {

    public boolean isCategoryExists(int categoryId) {
        String sql = "SELECT id FROM categories WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countProducts(String keyword) {
        String sql = "SELECT COUNT(*) FROM products WHERE masp LIKE ? OR name LIKE ? OR description LIKE ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countProducts() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Product> search(String keyword, int offset, int limit) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE masp LIKE ? OR name LIKE ? OR description LIKE ? ORDER BY id ASC LIMIT ? OFFSET ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setInt(4, limit);
            ps.setInt(5, offset);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getProductsByPage(int offset, int limit) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id ASC LIMIT ? OFFSET ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addProduct(Product p) {
        if (!isCategoryExists(p.getCategoryId())) {
            System.err.println("Category ID " + p.getCategoryId() + " does not exist.");
            return false;
        }
        String sql = "INSERT INTO products (masp, name, price, description, image, quantity, category_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getMasp());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getDescription());
            ps.setString(5, p.getImage());
            ps.setInt(6, p.getQuantity());
            ps.setInt(7, p.getCategoryId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Add product error: " + e.getMessage());
        }
        return false;
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProduct(Product p) {
        if (!isCategoryExists(p.getCategoryId())) {
            System.err.println("Category ID " + p.getCategoryId() + " does not exist.");
            return false;
        }
        String sql = "UPDATE products SET masp=?, name=?, price=?, description=?, image=?, quantity=?, category_id=? WHERE id=?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getMasp());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getDescription());
            ps.setString(5, p.getImage());
            ps.setInt(6, p.getQuantity());
            ps.setInt(7, p.getCategoryId());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Update product error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteProductById(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
            e.printStackTrace();
        }
        return list;
    }
}