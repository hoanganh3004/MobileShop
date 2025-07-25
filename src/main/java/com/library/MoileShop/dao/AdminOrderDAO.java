package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminOrderDAO {

    //  tìm kiếm đơn hàng theo tên khách hàng và ngày đặt hàng
    public List<Order> searchOrders(String customerName, String orderDate) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, a.full_name " +
                "FROM orders o JOIN accounts a ON o.user_code = a.code " +
                "WHERE a.full_name LIKE ? ";
        if (orderDate != null && !orderDate.isEmpty()) {
            sql += "AND DATE(o.order_date) = ? ";
        }
        sql += "ORDER BY o.id DESC";

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + customerName + "%");
            if (orderDate != null && !orderDate.isEmpty()) {
                ps.setString(2, orderDate);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserCode(rs.getString("user_code"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setTotal(rs.getDouble("total_amount"));
                o.setStatus(rs.getString("status"));
                o.setCancelReason(rs.getString("cancel_reason"));
                o.setRecipientName(rs.getString("recipient_name"));
                o.setRecipientEmail(rs.getString("recipient_email"));
                o.setRecipientPhone(rs.getString("recipient_phone"));
                o.setRecipientAddress(rs.getString("recipient_address"));

                Account a = new Account();
                a.setFullName(rs.getString("full_name"));
                o.setCustomer(a);

                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //  Thêm phương thức tìm kiếm theo userCode
    public List<Order> searchOrdersByUserCode(String userCode, String orderDate) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, a.full_name " +
                "FROM orders o JOIN accounts a ON o.user_code = a.code " +
                "WHERE o.user_code = ? ";
        if (orderDate != null && !orderDate.isEmpty()) {
            sql += "AND DATE(o.order_date) = ? ";
        }
        sql += "ORDER BY o.id ASC";

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            if (orderDate != null && !orderDate.isEmpty()) {
                ps.setString(2, orderDate);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserCode(rs.getString("user_code"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setTotal(rs.getDouble("total_amount"));
                o.setStatus(rs.getString("status"));
                o.setCancelReason(rs.getString("cancel_reason"));
                o.setRecipientName(rs.getString("recipient_name"));
                o.setRecipientEmail(rs.getString("recipient_email"));
                o.setRecipientPhone(rs.getString("recipient_phone"));
                o.setRecipientAddress(rs.getString("recipient_address"));

                Account a = new Account();
                a.setFullName(rs.getString("full_name"));
                o.setCustomer(a);

                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //  Chi tiết đơn hàng
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name, p.price, p.image " +
                "FROM order_items oi JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(rs.getInt("product_id"), rs.getString("name"),
                        rs.getDouble("price"), rs.getString("image"));

                Order o = new Order();
                o.setId(orderId);

                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrder(o);
                item.setProduct(p);
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getDouble("unit_price"));

                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    //  Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(int orderId, String status, String cancelReason) {
        String updateOrderSQL = "UPDATE orders SET status = ?, cancel_reason = ? WHERE id = ?";
        String getItemsSQL = "SELECT product_id, quantity FROM order_items WHERE order_id = ?";
        String restoreQtySQL = "UPDATE products SET quantity = quantity + ? WHERE id = ?";

        try (Connection conn = new DBcontext().getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement psUpdateOrder = conn.prepareStatement(updateOrderSQL);
                    PreparedStatement psGetItems = conn.prepareStatement(getItemsSQL);
                    PreparedStatement psRestoreQty = conn.prepareStatement(restoreQtySQL)
            ) {
                // Cập nhật trạng thái đơn hàng
                psUpdateOrder.setString(1, status);
                psUpdateOrder.setString(2, "Đã hủy".equals(status) ? cancelReason : "Không có");
                psUpdateOrder.setInt(3, orderId);
                int updated = psUpdateOrder.executeUpdate();

                if (updated == 0) {
                    conn.rollback();
                    return false;
                }

                // Nếu hủy đơn / hoàn lại hàng
                if ("Đã hủy".equals(status)) {
                    psGetItems.setInt(1, orderId);
                    ResultSet rs = psGetItems.executeQuery();

                    while (rs.next()) {
                        int productId = rs.getInt("product_id");
                        int quantity = rs.getInt("quantity");

                        psRestoreQty.setInt(1, quantity);
                        psRestoreQty.setInt(2, productId);
                        psRestoreQty.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    //  Thêm thông báo cho người dùng
    public void notifyUser(String userCode, String message) {
        String sql = "INSERT INTO notifications (user_code, message, created_at, is_read) VALUES (?, ?, NOW(), 0)";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  Lấy đơn hàng theo ID
    public Order getOrderById(int id) {
        String sql = "SELECT o.*, a.full_name FROM orders o JOIN accounts a ON o.user_code = a.code WHERE o.id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order o = new Order();
                o.setId(id);
                o.setUserCode(rs.getString("user_code"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setTotal(rs.getDouble("total_amount"));
                o.setStatus(rs.getString("status"));
                o.setCancelReason(rs.getString("cancel_reason"));
                o.setRecipientName(rs.getString("recipient_name"));
                o.setRecipientEmail(rs.getString("recipient_email"));
                o.setRecipientPhone(rs.getString("recipient_phone"));
                o.setRecipientAddress(rs.getString("recipient_address"));

                Account a = new Account();
                a.setFullName(rs.getString("full_name"));
                a.setCode(rs.getString("user_code"));
                o.setCustomer(a);

                return o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //  Tạo đơn hàng và trừ số lượng tồn kho
    public boolean createOrder(String userCode, List<Integer> productIds, List<Integer> quantities) {
        String insertOrderSQL = "INSERT INTO orders(user_code, order_date, total_amount, status) VALUES (?, NOW(), ?, 'pending')";
        String insertItemSQL = "INSERT INTO order_items(order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String updateProductQtySQL = "UPDATE products SET quantity = quantity - ? WHERE id = ?";

        try (Connection conn = new DBcontext().getConnection()) {
            conn.setAutoCommit(false);

            if (productIds == null || quantities == null || productIds.size() != quantities.size()) {
                return false;
            }

            AdminProductDAO productDAO = new AdminProductDAO();
            double total = 0;
            List<Double> unitPrices = new ArrayList<>();

            for (int i = 0; i < productIds.size(); i++) {
                int productId = productIds.get(i);
                int quantity = quantities.get(i);
                Product p = productDAO.getProductById(productId);
                if (p == null || p.getQuantity() < quantity) {
                    conn.rollback();
                    return false;
                }
                total += p.getPrice() * quantity;
                unitPrices.add(p.getPrice());
            }

            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, userCode);
                psOrder.setDouble(2, total);
                psOrder.executeUpdate();

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        int orderId = rs.getInt(1);

                        try (PreparedStatement psItem = conn.prepareStatement(insertItemSQL);
                             PreparedStatement psUpdateQty = conn.prepareStatement(updateProductQtySQL)) {

                            for (int i = 0; i < productIds.size(); i++) {
                                int productId = productIds.get(i);
                                int quantity = quantities.get(i);
                                double price = unitPrices.get(i);

                                psItem.setInt(1, orderId);
                                psItem.setInt(2, productId);
                                psItem.setInt(3, quantity);
                                psItem.setDouble(4, price);
                                psItem.addBatch();

                                psUpdateQty.setInt(1, quantity);
                                psUpdateQty.setInt(2, productId);
                                psUpdateQty.addBatch();
                            }

                            psItem.executeBatch();
                            int[] qtyResults = psUpdateQty.executeBatch();

                            for (int result : qtyResults) {
                                if (result == 0) {
                                    conn.rollback();
                                    return false;
                                }
                            }

                            try (PreparedStatement psUser = conn.prepareStatement("SELECT id FROM accounts WHERE code = ?")) {
                                psUser.setString(1, userCode);
                                try (ResultSet userRs = psUser.executeQuery()) {
                                    if (userRs.next()) {
                                        notifyUser(userCode, "Bạn vừa tạo đơn hàng mới #" + orderId);
                                    }
                                }
                            }

                            conn.commit();
                            return true;
                        }
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }


    //  Đếm số đơn hàng của người dùng
    public int countOrdersByUser(String userCode) {
        if (userCode == null) return 0;

        String sql = "SELECT COUNT(*) FROM orders WHERE user_code = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //  Tạo đơn hàng có lưu thông tin người nhận (dành cho đặt giúp người khác)
    public boolean createOrder(String userCode, String name, String email, String phone, String address,
                               List<CartItem> cartItems, Double totalPrice) {
        String insertOrderSQL = "INSERT INTO orders(user_code, recipient_name, recipient_email, recipient_phone, recipient_address, order_date, total_amount, status) " +
                "VALUES (?, ?, ?, ?, ?, NOW(), ?, 'Chờ xác nhận')";
        String insertItemSQL = "INSERT INTO order_items(order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String updateQtySQL = "UPDATE products SET quantity = quantity - ? WHERE id = ?";

        try (Connection conn = new DBcontext().getConnection()) {
            conn.setAutoCommit(false);

            // Thêm đơn hàng
            PreparedStatement psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            psOrder.setString(1, userCode);
            psOrder.setString(2, name);
            psOrder.setString(3, email);
            psOrder.setString(4, phone);
            psOrder.setString(5, address);
            psOrder.setDouble(6, totalPrice);
            psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);

                // Thêm từng item
                PreparedStatement psItem = conn.prepareStatement(insertItemSQL);
                for (CartItem item : cartItems) {
                    psItem.setInt(1, orderId);
                    psItem.setInt(2, item.getProduct().getId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getProduct().getPrice());
                    psItem.addBatch();
                }
                psItem.executeBatch();

                // Trừ số lượng tồn kho
                PreparedStatement psUpdateQty = conn.prepareStatement(updateQtySQL);
                for (CartItem item : cartItems) {
                    psUpdateQty.setInt(1, item.getQuantity());
                    psUpdateQty.setInt(2, item.getProduct().getId());
                    psUpdateQty.addBatch();
                }
                psUpdateQty.executeBatch();

                // Gửi thông báo
                String userIdSql = "SELECT id FROM accounts WHERE code = ?";
                int userId = -1;
                try (PreparedStatement psUser = conn.prepareStatement(userIdSql)) {
                    psUser.setString(1, userCode);
                    ResultSet userRs = psUser.executeQuery();
                    if (userRs.next()) {
                        userId = userRs.getInt("id");
                    }
                }

                if (userId != -1) {
                    notifyUser(userCode, "Bạn đã đặt hàng thành công đơn hàng với mã đơn hàng là #" + orderId);
                }

                conn.commit();
                return true;
            }

            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}