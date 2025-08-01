package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderDAO {

    // Tìm kiếm đơn hàng theo tên khách hàng, ngày đặt và trạng thái
    public List<Order> searchOrders(String customerName, String orderDate, String status, int offset, int size) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE recipient_name LIKE ? ";

        if (orderDate != null && !orderDate.isEmpty()) {
            sql += "AND DATE(order_date) = ? ";
        }

        if (status != null && !status.isEmpty()) {
            sql += "AND status = ? ";
        }

        sql += "ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            ps.setString(paramIndex++, "%" + customerName + "%");

            if (orderDate != null && !orderDate.isEmpty()) {
                ps.setString(paramIndex++, orderDate);
            }

            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            ps.setInt(paramIndex++, size);
            ps.setInt(paramIndex, offset);

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

                // Không cần Account nữa nếu chỉ dùng recipient_name
                list.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Đếm đơn hàng theo tên, ngày và trạng thái
    public int countOrders(String customer, String orderDate, String status) {
        String sql = "SELECT COUNT(*) FROM orders o JOIN accounts a ON o.user_code = a.code WHERE 1=1";

        if (customer != null && !customer.trim().isEmpty()) {
            sql += " AND a.full_name LIKE ?";
        }
        if (orderDate != null && !orderDate.isEmpty()) {
            sql += " AND DATE(o.order_date) = ?";
        }
        if (status != null && !status.isEmpty()) {
            sql += " AND o.status = ?";
        }

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;
            if (customer != null && !customer.trim().isEmpty()) {
                ps.setString(i++, "%" + customer + "%");
            }
            if (orderDate != null && !orderDate.isEmpty()) {
                ps.setString(i++, orderDate);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(i, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Đếm tổng số đơn hàng
    public boolean cancelOrder(int orderId, String cancelReason) {
        String sql = "UPDATE orders SET status = 'Đã hủy', cancel_reason = ? WHERE id = ? AND status = 'Chờ xác nhận'";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cancelReason);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
        String sql = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                // ✅ Lấy thông tin từ bảng order_items (không phải từ bảng products)
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setPrice(rs.getDouble("unit_price"));
                p.setImage(rs.getString("product_image"));
                p.setMasp(rs.getString("product_masp"));
                p.setDescription(rs.getString("product_description"));

                Order o = new Order();
                o.setId(orderId);

                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrder(o);
                item.setProduct(p);
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getDouble("unit_price")); // vẫn cần thiết

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
        String sql = "SELECT o.*, a.full_name FROM orders o LEFT JOIN accounts a ON o.user_code = a.code WHERE o.id = ?";
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

    //đếm tổng đơn hàng
    public int countOrders(String customer, String orderDate) {
        String sql = "SELECT COUNT(*) FROM orders o JOIN accounts a ON o.user_code = a.code WHERE 1=1";

        if (customer != null && !customer.trim().isEmpty()) {
            sql += " AND a.full_name LIKE ?";
        }

        if (orderDate != null && !orderDate.isEmpty()) {
            sql += " AND DATE(o.order_date) = ?";
        }

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;
            if (customer != null && !customer.trim().isEmpty()) {
                ps.setString(i++, "%" + customer + "%");
            }

            if (orderDate != null && !orderDate.isEmpty()) {
                ps.setString(i, orderDate);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
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

        String insertItemSQL = "INSERT INTO order_items(order_id, product_id, quantity, unit_price, product_name, product_image, product_masp, product_description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String updateQtySQL = "UPDATE products SET quantity = quantity - ? WHERE id = ?";

        try (Connection conn = new DBcontext().getConnection()) {
            conn.setAutoCommit(false);

            // Thêm đơn hàng
            PreparedStatement psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);

            // Nếu userCode là null, set NULL vào DB
            if (userCode == null || userCode.trim().isEmpty()) {
                psOrder.setNull(1, java.sql.Types.VARCHAR);
            } else {
                psOrder.setString(1, userCode);
            }

            psOrder.setString(2, name);
            psOrder.setString(3, email);
            psOrder.setString(4, phone);
            psOrder.setString(5, address);
            psOrder.setDouble(6, totalPrice);
            psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);

                // Thêm từng order item với snapshot sản phẩm
                PreparedStatement psItem = conn.prepareStatement(insertItemSQL);
                for (CartItem item : cartItems) {
                    Product p = item.getProduct();

                    psItem.setInt(1, orderId);
                    psItem.setInt(2, p.getId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, p.getPrice());

                    psItem.setString(5, p.getName());
                    psItem.setString(6, p.getImage());
                    psItem.setString(7, p.getMasp());
                    psItem.setString(8, p.getDescription());

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

                // Gửi thông báo nếu có userCode
                if (userCode != null && !userCode.trim().isEmpty()) {
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