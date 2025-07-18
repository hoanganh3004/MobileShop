package com.library.MoileShop.service;

import com.library.MoileShop.dao.ShopDao;
import com.library.MoileShop.dao.AdminOrderDAO;
import com.library.MoileShop.dao.CartDAO;
import com.library.MoileShop.dao.AccountDAO;
import com.library.MoileShop.entity.Account;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Properties;

public class AccService {

    private final ShopDao dao;
    private final AdminOrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final AccountDAO accountDAO;

    public AccService() {
        this.dao = new ShopDao();
        this.orderDAO = new AdminOrderDAO();
        this.cartDAO = new CartDAO();
        this.accountDAO = new AccountDAO();
    }

    // ✅ Sinh mật khẩu ngẫu nhiên
    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


    public String generateRandomCode() {
        int maxId = dao.getLastAccountId(); // ⚠️ đảm bảo ShopDao có hàm getLastAccountId()
        String prefix = "ad";
        String number = String.format("%05d", maxId + 1);
        return prefix + number;
    }

    public void sendEmail(String to, String subject, String content) {
        final String fromEmail = "hoanganhhy3004@gmail.com";
        final String password = "drcraswnjdzdgbnr"; // App Password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.localhost", "localhost");

        // Cũng đặt hệ thống nếu cần (tuỳ máy)
        System.setProperty("mail.smtp.localhost", "localhost");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);

            // 👇 Hiển thị tên người gửi: MobileShop
            message.setFrom(new InternetAddress(fromEmail, "MobileShop", "UTF-8"));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));


            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", null));

            // 👇 Nội dung HTML UTF-8 để tránh lỗi font tiếng Việt
            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("Sent!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ✅ Xử lý quên mật khẩu
    public void handleForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập địa chỉ email.");
            request.getRequestDispatcher("view/acc/forgot-password.jsp").forward(request, response);
            return;
        }

        Account account = dao.getAccountByEmail(email);
        if (account == null) {
            request.setAttribute("error", "Không tìm thấy tài khoản với email đã nhập.");
            request.getRequestDispatcher("view/acc/forgot-password.jsp").forward(request, response);
            return;
        }

        String newPassword = generateRandomPassword(8);
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        dao.updatePassword(account.getId(), hashedPassword);

        String subject = "Khôi phục mật khẩu từ MobileShop";
        String content = "<p>Xin chào " + account.getFullName() + ",</p>"
                + "<p>Mật khẩu mới của bạn là: <b>" + newPassword + "</b></p>"
                + "<p>Vui lòng đăng nhập và đổi lại mật khẩu để đảm bảo an toàn.</p>"
                + "<p>Trân trọng,<br>MobileShop</p>";

        sendEmail(email, subject, content);

        request.setAttribute("success", "Mật khẩu mới đã được gửi đến email của bạn.");
        request.getRequestDispatcher("view/acc/login.jsp").forward(request, response);
    }

    // ✅ Xử lý đăng xuất
    public void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/home");
    }

    // ✅ Xử lý đăng ký
    public void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        String repassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        fullName = fullName != null ? fullName : "";
        email = email != null ? email : "";
        phone = phone != null ? phone : "";
        address = address != null ? address : "";

        if (!password.equals(repassword)) {
            request.setAttribute("message", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("/view/acc/register.jsp").forward(request, response);
            return;
        }

        if (dao.isUsernameExists(username)) {
            request.setAttribute("message", "Tên đăng nhập đã tồn tại.");
            request.getRequestDispatcher("/view/acc/register.jsp").forward(request, response);
            return;
        }

        if (dao.isEmailExists(email)) {
            request.setAttribute("message", "Email đã được sử dụng.");
            request.getRequestDispatcher("/view/acc/register.jsp").forward(request, response);
            return;
        }

        String code = generateRandomCode(); // ✅ sửa chỗ này, không còn trùng nữa
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Account acc = new Account(
                0, code, username, hashedPassword,
                fullName, email, phone, address,
                "user", 1, now
        );

        if (dao.registerAccount(acc)) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("message", "Đăng ký thất bại.");
            request.getRequestDispatcher("/view/acc/register.jsp").forward(request, response);
        }
    }

    // ✅ Xử lý đăng nhập
    public void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Account acc = dao.login(username, password);
            if (acc != null) {
                if (acc.getStatus() == 0) {
                    request.setAttribute("error", "Tài khoản của bạn đã bị khóa!");
                    request.getRequestDispatcher("view/acc/login.jsp").forward(request, response);
                    return;
                }

                HttpSession session = request.getSession();
                session.setAttribute("acc", acc);

                String userCode = accountDAO.getUserCodeById(acc.getId());
                int orderCount = orderDAO.countOrdersByUser(userCode);
                int cartCount = cartDAO.countItemsByUser(userCode);
                session.setAttribute("orderCount", orderCount);
                session.setAttribute("cartCount", cartCount);

                if ("admin".equalsIgnoreCase(acc.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/view/ad/admin.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                request.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
                request.getRequestDispatcher("view/acc/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi đăng nhập!");
            request.getRequestDispatcher("view/acc/login.jsp").forward(request, response);
        }
    }
}
