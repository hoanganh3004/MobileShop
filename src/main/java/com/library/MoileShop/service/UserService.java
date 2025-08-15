package com.library.MoileShop.service;

import com.library.MoileShop.dao.*;
import com.library.MoileShop.entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

public class UserService {

    // DAO
    private final AccountDAO accountDAO = new AccountDAO();
    private final ShopDao shopDao = new ShopDao();
    private final AdminOrderDAO adminOrderDAO = new AdminOrderDAO();
    private final AdminCategoryDAO adminCategoryDAO = new AdminCategoryDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final AdminNotificationDAO adminNotificationDAO = new AdminNotificationDAO();
    private final AdminProductDAO productDAO = new AdminProductDAO();
    private final CartDAO cartDAO = new CartDAO();

    // Helper
    private Account requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Account acc = (session != null) ? (Account) session.getAttribute("acc") : null;

        if (acc == null) {
            resp.sendRedirect("view/acc/login.jsp");
            return null;
        }
        return acc;
    }

    private void loadCategories(HttpServletRequest req) {
        req.setAttribute("listC", shopDao.getAllCategory());
    }

    private void updateCartSession(HttpSession session, String userCode) {
        List<CartItem> items = cartDAO.getCartItemsByUser(userCode);

        double total = 0;
        int qty = 0;
        Map<String, Integer> map = new HashMap<>();

        for (CartItem i : items) {
            total += i.getQuantity() * i.getProduct().getPrice();
            qty += i.getQuantity();
            map.put(String.valueOf(i.getProduct().getId()), i.getQuantity());
        }

        session.setAttribute("cartItems", items);
        session.setAttribute("cartList", items);
        session.setAttribute("cartTotal", total);
        session.setAttribute("cartCount", qty);
        session.setAttribute("cart", map);
    }

    // Account
    public void handleAccount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = requireLogin(req, resp);
        if (acc == null) return;

        Account fresh = accountDAO.getAccountById(acc.getId());
        if (fresh == null) {
            resp.sendRedirect("view/acc/login.jsp");
            return;
        }

        loadCategories(req);
        req.setAttribute("acc", fresh);
        req.getSession().setAttribute("acc", fresh);
        req.getRequestDispatcher("view/user/profile.jsp").forward(req, resp);
    }

    public void handleAccountUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = requireLogin(req, resp);
        if (acc == null) return;

        if ("updateInfo".equals(req.getParameter("action"))) {
            String full = req.getParameter("full_name");
            String email = req.getParameter("email");

            if (full == null || full.isBlank() || email == null || email.isBlank()) {
                req.setAttribute("err", "Họ tên và email không được để trống!");
                handleAccount(req, resp);
                return;
            }

            acc.setFullName(full);
            acc.setEmail(email);
            acc.setPhone(req.getParameter("phone"));
            acc.setAddress(req.getParameter("address"));
            accountDAO.updateAccountInfo(acc);

            String pass = req.getParameter("password");
            String confirm = req.getParameter("confirmPassword");

            if (pass != null && !pass.isEmpty()) {
                if (!pass.equals(confirm)) {
                    req.setAttribute("err", "Mật khẩu xác nhận không khớp!");
                    handleAccount(req, resp);
                    return;
                }
                String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(pass, org.mindrot.jbcrypt.BCrypt.gensalt());
                accountDAO.updatePassword(acc.getId(), hashed);
                acc.setPassword(hashed);
            }

            req.getSession().setAttribute("acc", acc);
            req.setAttribute("msg", "Cập nhật thông tin thành công!");
        }

        handleAccount(req, resp);
    }

    //  Product Detail
    public void handleDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("detail", shopDao.getProductsbyID(req.getParameter("pid")));
        loadCategories(req);
        req.getRequestDispatcher("view/user/product.jsp").forward(req, resp);
    }

    public void handleSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String txt = req.getParameter("txt");
        String cateRaw = req.getParameter("category");

        int cid;
        try {
            cid = Integer.parseInt(cateRaw);
        } catch (Exception e) {
            cid = 0;
        }

        List<Product> list = (cid == 0)
                ? shopDao.searchByName(txt)
                : shopDao.searchByNameAndCategory(txt, cid);

        req.setAttribute("listP", list);
        loadCategories(req);
        req.getRequestDispatcher("view/user/store.jsp").forward(req, resp);
    }

    //  Notification
    public void handleReadNotification(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nid = req.getParameter("id");
        Account acc = (Account) req.getSession().getAttribute("acc");

        if (nid != null && acc != null) {
            try {
                int id = Integer.parseInt(nid);
                //ĐÁNH DẤU THÔNG BÁO LÀ ĐÃ ĐỌC
                notificationDAO.markAsRead(id);
                Notification n = notificationDAO.getNotificationById(id);

                String code = accountDAO.getUserCodeById(acc.getId());
                req.getSession().setAttribute("notifyList", notificationDAO.getAllByUser(code));
                req.getSession().setAttribute("notifyCount", notificationDAO.countUnread(code));
                //CHUYỂN TỚI TRANG LỊCH SỬ ĐƠN HÀNG
                if (n != null && n.getMessage() != null && n.getMessage().contains("#")) {
                    String orderId = n.getMessage().split("#")[1].split(" ")[0];
                    resp.sendRedirect("order-history?orderId=" + orderId);
                    return;
                }
            } catch (Exception ignored) {}
        }

        resp.sendRedirect(Optional.ofNullable(req.getHeader("referer")).orElse("home"));
    }

    //removeCart
    public void handleRemoveCartItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account acc = requireLogin(req, resp);
        if (acc == null) return;
        try {
            cartDAO.removeItem(acc.getCode(), Integer.parseInt(req.getParameter("pid")));
        } catch (Exception ignored) {}

        updateCartSession(req.getSession(), acc.getCode());
        resp.sendRedirect(req.getHeader("Referer"));
    }

    //Category
    public void handleCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cid = req.getParameter("cid");

        req.setAttribute("listP", shopDao.getProductsbyCid(cid));
        loadCategories(req);

        try {
            req.setAttribute("activeCid", Integer.parseInt(cid));
        } catch (Exception e) {
            req.setAttribute("activeCid", null);
        }

        req.getRequestDispatcher("view/user/store.jsp").forward(req, resp);
    }

    //Checkout
    public void handleCheckoutGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = requireLogin(req, resp);
        if (acc == null) return;

        updateCartSession(req.getSession(), acc.getCode());

        if (req.getAttribute("outOfStockMessages") == null) {
            List<String> msgs = new ArrayList<>();
            boolean block = false;

            for (CartItem i : cartDAO.getCartItemsByUser(acc.getCode())) {
                Product p = productDAO.getProductById(i.getProduct().getId());
                if (p.getQuantity() < i.getQuantity()) {
                    msgs.add("Sản phẩm '" + p.getName() + "' chỉ còn " + p.getQuantity() + " sản phẩm trong kho.");
                    block = true;
                }
            }

            if (!msgs.isEmpty()) {
                req.setAttribute("outOfStockMessages", msgs);
                req.setAttribute("hasBlockingStock", block);
            }
        }

        req.setAttribute("listC", adminCategoryDAO.getAllCategories());
        req.getRequestDispatcher("view/user/checkout.jsp").forward(req, resp);
    }

    //Checkout
    public void handleCheckoutPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = requireLogin(req, resp);
        if (acc == null) return;

        String userCode = acc.getCode();

        // Cập nhật số lượng
        if ("updateQuantity".equals(req.getParameter("action"))) {
            try {
                int pid = Integer.parseInt(req.getParameter("productId"));
                CartItem item = cartDAO.getCartItem(userCode, pid);

                if (item != null) {
                    int qty = item.getQuantity();

                    if ("increase".equals(req.getParameter("operation"))) {
                        // Kiểm tra tồn kho trước khi tăng
                        Product p = productDAO.getProductById(pid);
                        if (qty >= p.getQuantity()) {
                            req.setAttribute("outOfStockMessages",
                                    List.of("Sản phẩm '" + p.getName() + "' chỉ còn " + p.getQuantity() + " sản phẩm trong kho."));
                            handleCheckoutGet(req, resp);
                            return;
                        }
                        qty++;
                    } else if ("decrease".equals(req.getParameter("operation"))) {
                        // Chỉ giảm khi > 1
                        if (qty > 1) {
                            qty--;
                        } else {
                            req.setAttribute("outOfStockMessages",
                                    List.of("Số lượng sản phẩm không thể nhỏ hơn 1."));
                            handleCheckoutGet(req, resp);
                            return;
                        }
                    }

                    // Cập nhật DB
                    cartDAO.updateQuantity(userCode, pid, qty);

                    // Cập nhật lại giỏ hàng trong session
                    updateCartSession(req.getSession(), userCode);
                }

                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return;
        }
        // Đặt hàng
        List<CartItem> cartItems = (List<CartItem>) req.getSession().getAttribute("cartList");
        Double total = (Double) req.getSession().getAttribute("cartTotal");

        if (cartItems == null || cartItems.isEmpty() || total == null) {
            req.setAttribute("message", "Giỏ hàng trống!");
            handleCheckoutGet(req, resp); // load lại trang checkout
            return;
        }

        boolean guest = req.getParameter("isGuestOrder") != null;
        String name = guest ? req.getParameter("guestFullName") : acc.getFullName();
        String email = guest ? req.getParameter("guestEmail") : acc.getEmail();
        String phone = guest ? req.getParameter("guestPhone") : acc.getPhone();
        String addr = guest ? req.getParameter("guestAddress") : acc.getAddress();

        if (adminOrderDAO.createOrder(userCode, name, email, phone, addr, cartItems, total)) {
            // Xóa giỏ hàng trong DB
            cartDAO.clearCartByUser(userCode);

            // Reset giỏ hàng trong session
            req.getSession().setAttribute("cartItems", new ArrayList<CartItem>());
            req.getSession().setAttribute("cartCount", 0);
            req.getSession().setAttribute("cartTotal", 0.0);

            // Cập nhật thông báo
            List<Notification> nl = adminNotificationDAO.getAllNotificationsByUser(acc.getCode());
            req.getSession().setAttribute("notifyList", nl);
            req.getSession().setAttribute("notifyCount", nl.size());
            req.getSession().setAttribute("flash", "Đặt hàng thành công!");

            // Chuyển hướng về home chỉ khi đặt hàng thành công
            resp.sendRedirect("home");
        } else {
            handleCheckoutGet(req, resp);
        }
    }

        //Cancel Order
    public void handleCancelOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean ok = adminOrderDAO.cancelOrder(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("reason")
        );
        if (ok) {
            resp.getWriter().write("OK");
        } else {
            resp.sendError(400, "Không thể hủy đơn hàng.");
        }
    }

    //Order History
    public void handleOrderHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = requireLogin(req, resp);
        if (acc == null) return;

        req.setAttribute("orders", adminOrderDAO.searchOrdersByUserCode(
                acc.getCode(), null
        ));
        req.setAttribute("listC", adminCategoryDAO.getAllCategories());
        req.getRequestDispatcher("view/user/orderHistory.jsp").forward(req, resp);
    }

    // Home
    public void handleHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listP", shopDao.getAllProducts());
        loadCategories(req);

        Account acc = (Account) req.getSession().getAttribute("acc");
        if (acc != null) {
            String code = accountDAO.getUserCodeById(acc.getId());
            req.getSession().setAttribute("notifyList", notificationDAO.getAllByUser(code));
            req.getSession().setAttribute("notifyCount", notificationDAO.countUnread(code));
        }

        req.getRequestDispatcher("view/user/home.jsp").forward(req, resp);
    }

    //  Add To Cart
    public void handleAddToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account acc = requireLogin(req, resp);
        if (acc == null) return;
        int pid;
        int qty = 1;
        try {
            pid = Integer.parseInt(req.getParameter("pid"));
            qty = Integer.parseInt(req.getParameter("quantity"));
        } catch (NumberFormatException e) {
            resp.sendRedirect(Optional.ofNullable(req.getHeader("Referer")).orElse("home"));
            return;
        }

        try {
            cartDAO.addOrUpdateItem(acc.getCode(), pid, qty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateCartSession(req.getSession(), acc.getCode());
        resp.sendRedirect(Optional.ofNullable(req.getHeader("Referer")).orElse("home"));
    }
}
