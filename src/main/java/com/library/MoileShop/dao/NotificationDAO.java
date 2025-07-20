package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    private final DBcontext db = new DBcontext();

    // lấy tất cả thông báo
    public List<Notification> getAllByUser(String userCode) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_code = ? ORDER BY created_at DESC";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setUserCode(rs.getString("user_code"));
                n.setMessage(rs.getString("message"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                n.setRead(rs.getBoolean("is_read"));
                list.add(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //đếm số thông báo chưa đọc
    public int countUnread(String userCode) { // Sửa từ int userId thành String userCode
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_code = ? AND is_read = false";
        try (Connection conn = db.getConnection();
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

    // đánh dấu thông báo là đã đọc
    public void markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = true WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}