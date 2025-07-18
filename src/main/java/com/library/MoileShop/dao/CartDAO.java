package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CartDAO {

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
}