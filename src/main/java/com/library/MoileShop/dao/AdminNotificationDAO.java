package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminNotificationDAO {

    //  Tìm kiếm thông báo theo keyword
    public List<Notification> searchNotifications(String keyword, int offset, int size) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT n.*, a.full_name FROM notifications n " +
                "JOIN accounts a ON n.user_code = a.code ";

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += "WHERE n.message LIKE ? ";
        }

        sql += "ORDER BY n.created_at DESC LIMIT ? OFFSET ?";

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
            }

            ps.setInt(paramIndex++, size);
            ps.setInt(paramIndex, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setUserCode(rs.getString("user_code"));
                n.setFullName(rs.getString("full_name"));
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


    // Đếm số thông báo
    public int countNotifications(String keyword) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE message LIKE ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //  Thêm thông báo mới (dùng user_code kiểu String)
    public void insertNotification(String userCode, String message) {
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


    //  Lấy danh sách thông báo mới nhất của user
    public List<Notification> getUnreadNotificationsByUser(String userCode) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_code = ? ORDER BY created_at DESC";

        try (Connection conn = new DBcontext().getConnection();
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
}
