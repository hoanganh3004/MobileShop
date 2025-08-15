package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.CartItem;
import com.library.MoileShop.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // Đếm tổng số lượng sản phẩm trong giỏ hàng của một người dùng
    public int countItemsByUser(String userCode) {
        String sql = "SELECT SUM(quantity) FROM cart_items WHERE user_code = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return rs.wasNull() ? 0 : count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Lấy thông tin 1 sản phẩm trong giỏ hàng của người dùng (nếu có)
    public CartItem getCartItem(String userCode, int productId) {
        String sql = "SELECT quantity FROM cart_items WHERE user_code = ? AND product_id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                Product product = new AdminProductDAO().getProductById(productId);
                return new CartItem(product, quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm sản phẩm mới vào giỏ hàng (nếu chưa có)
    public void addToCart(String userCode, int productId, int quantity) {
        String sql = "INSERT INTO cart_items (user_code, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cập nhật số lượng sản phẩm nếu đã tồn tại trong giỏ hàng
    public void updateQuantity(String userCode, int productId, int newQuantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE user_code = ? AND product_id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setString(2, userCode);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // thêm+ cập nhật sản phẩm (nếu đã tồn tại thì tăng số lượng)
    public void addOrUpdateItem(String userCode, int productId, int quantityToAdd) {
        CartItem existingItem = getCartItem(userCode, productId);
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantityToAdd;
            updateQuantity(userCode, productId, newQuantity);
        } else {
            addToCart(userCode, productId, quantityToAdd);
        }
    }

    // Lấy toàn bộ danh sách sản phẩm trong giỏ hàng của người dùng
    public List<CartItem> getCartItemsByUser(String userCode) {
        List<CartItem> list = new ArrayList<>();
        String sql = """
            SELECT ci.quantity, p.* 
            FROM cart_items ci 
            JOIN products p ON ci.product_id = p.id 
            WHERE ci.user_code = ?
        """;
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("image")
                        // thêm các field khác nếu cần
                );
                int quantity = rs.getInt("quantity");
                list.add(new CartItem(p, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Xoá toàn bộ giỏ hàng của người dùng
    public void clearCartByUser(String userCode) {
        String sql = "DELETE FROM cart_items WHERE user_code = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xoá 1 sản phẩm khỏi giỏ hàng
    public void removeItem(String userCode, int productId) {
        String sql = "DELETE FROM cart_items WHERE user_code = ? AND product_id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
