package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    //lấy thông tin tài khoản theo id
    public Account getAccountById(int id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account acc = new Account(
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
                System.out.println("getAccountById: Loaded account with username: " + acc.getUsername());
                return acc;
            } else {
                System.out.println("getAccountById: No account found for id: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error in getAccountById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //cập nhật thông tin tài khoản
    public boolean updateAccountInfo(Account acc) {
        String sql = "UPDATE accounts SET full_name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acc.getFullName());
            ps.setString(2, acc.getEmail());
            ps.setString(3, acc.getPhone());
            ps.setString(4, acc.getAddress());
            ps.setInt(5, acc.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //cập nhật mật khẩu của tài khoản dựa trên id
    public boolean updatePassword(int id, String hashedPassword) {
        String sql = "UPDATE accounts SET password = ? WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //lấy tất cả tài khoản
    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY id ASC";

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Account acc = new Account(
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
                list.add(acc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //lấy tài khoản theo username
    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM accounts WHERE username = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
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
            e.printStackTrace();
        }
        return null;
    }

    // Thêm phương thức hỗ trợ
    public String getUserCodeById(int id) {
        String sql = "SELECT code FROM accounts WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("code");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}