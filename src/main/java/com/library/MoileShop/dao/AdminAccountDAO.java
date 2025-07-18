package com.library.MoileShop.dao;

import com.library.MoileShop.context.DBcontext;
import com.library.MoileShop.entity.Account;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminAccountDAO {

    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("id"));
                acc.setCode(rs.getString("code"));
                acc.setUsername(rs.getString("username"));
                acc.setFullName(rs.getString("full_name"));
                acc.setEmail(rs.getString("email"));
                acc.setPhone(rs.getString("phone"));
                acc.setAddress(rs.getString("address"));
                acc.setPassword(rs.getString("password"));
                acc.setRole(rs.getString("role"));
                acc.setStatus(rs.getInt("status"));
                acc.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(acc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Account getAccountById(int id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account acc = new Account();
                    acc.setId(rs.getInt("id"));
                    acc.setCode(rs.getString("code"));
                    acc.setUsername(rs.getString("username"));
                    acc.setFullName(rs.getString("full_name"));
                    acc.setEmail(rs.getString("email"));
                    acc.setPhone(rs.getString("phone"));
                    acc.setAddress(rs.getString("address"));
                    acc.setPassword(rs.getString("password"));
                    acc.setRole(rs.getString("role"));
                    acc.setStatus(rs.getInt("status"));
                    acc.setCreatedAt(rs.getTimestamp("created_at"));
                    return acc;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateRole(int id, String role) {
        String sql = "UPDATE accounts SET role = ? WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean toggleStatus(int id) {
        String sql = "UPDATE accounts SET status = CASE WHEN status = 1 THEN 0 ELSE 1 END WHERE id = ?"; // Sửa logic toggle
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAccount(Account acc) {
        String sql = "UPDATE accounts SET full_name=?, email=?, phone=?, address=? WHERE id=?";
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

    public boolean deleteAccount(int id) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int id, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
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

    public List<Account> getAllUsers() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE role = 'user'";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("id"));
                acc.setCode(rs.getString("code"));
                acc.setUsername(rs.getString("username"));
                acc.setFullName(rs.getString("full_name"));
                acc.setEmail(rs.getString("email"));
                acc.setPhone(rs.getString("phone"));
                acc.setRole(rs.getString("role"));
                acc.setStatus(rs.getInt("status"));
                acc.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(acc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}