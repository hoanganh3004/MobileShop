package com.library.MoileShop.service;

import com.library.MoileShop.dao.AccountDAO;
import com.library.MoileShop.dao.AdminCategoryDAO;
import com.library.MoileShop.dao.AdminNotificationDAO;
import com.library.MoileShop.dao.AdminOrderDAO;
import com.library.MoileShop.dao.AdminProductDAO;
import com.library.MoileShop.dao.NotificationDAO;
import com.library.MoileShop.dao.ShopDao;
import com.library.MoileShop.entity.Account;
import com.library.MoileShop.entity.CartItem;
import com.library.MoileShop.entity.Category;
import com.library.MoileShop.entity.Notification;
import com.library.MoileShop.entity.Order;
import com.library.MoileShop.entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

public class UserService {

    private final AccountDAO accountDAO = new AccountDAO();
    private final ShopDao shopDao = new ShopDao();
    private final AdminOrderDAO adminOrderDAO = new AdminOrderDAO();
    private final AdminCategoryDAO adminCategoryDAO = new AdminCategoryDAO();
    private final AdminProductDAO adminProductDAO = new AdminProductDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final AdminNotificationDAO adminNotificationDAO = new AdminNotificationDAO();

    // Phương thức xử lý AccountControl
    public void handleAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/view/acc/login.jsp");
            return;
        }

        Account acc = (Account) session.getAttribute("acc");
        if (acc == null) {
            response.sendRedirect(request.getContextPath() + "/view/acc/login.jsp");
            return;
        }

        Account freshAcc = accountDAO.getAccountById(acc.getId());
        if (freshAcc == null) {
            response.sendRedirect(request.getContextPath() + "/view/acc/login.jsp");
            return;
        }

        List<Category> listC = shopDao.getAllCategory();

        request.setAttribute("listC", listC);
        request.setAttribute("acc", freshAcc);
        session.setAttribute("acc", freshAcc);

        request.getRequestDispatcher("view/user/profile.jsp").forward(request, response);
    }

    public void handleAccountUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/view/acc/login.jsp");
            return;
        }

        Account acc = (Account) session.getAttribute("acc");
        if (acc == null) {
            response.sendRedirect(request.getContextPath() + "/view/acc/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("updateInfo".equals(action)) {
            String full_name = request.getParameter("full_name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String newPass = request.getParameter("password");
            String confirmPass = request.getParameter("confirmPassword");

            if (full_name == null || full_name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                request.setAttribute("err", "Họ tên và email không được để trống!");
                handleAccount(request, response);
                return;
            }

            acc.setFullName(full_name);
            acc.setEmail(email);
            acc.setPhone(phone);
            acc.setAddress(address);

            boolean infoUpdated = accountDAO.updateAccountInfo(acc);

            if (newPass != null && !newPass.isEmpty()) {
                if (!newPass.equals(confirmPass)) {
                    request.setAttribute("err", "Mật khẩu xác nhận không khớp!");
                    handleAccount(request, response);
                    return;
                }

                String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(newPass, org.mindrot.jbcrypt.BCrypt.gensalt());
                boolean passUpdated = accountDAO.updatePassword(acc.getId(), hashed);
                if (!passUpdated) {
                    request.setAttribute("err", "Cập nhật mật khẩu thất bại.");
                    handleAccount(request, response);
                    return;
                }
                acc.setPassword(hashed);
            }

            session.setAttribute("acc", acc);
            request.setAttribute("msg", "Cập nhật thông tin thành công!");
        }

        handleAccount(request, response);
    }

    // Phương thức xử lý DetailControl
    public void handleDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("pid");
        Product p = shopDao.getProductsbyID(id);
        List<Category> listC = shopDao.getAllCategory();

        request.setAttribute("detail", p);
        request.setAttribute("listC", listC);

        request.getRequestDispatcher("view/user/product.jsp").forward(request, response);
    }

    // Phương thức xử lý SearchControl
    public void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String txtSearch = request.getParameter("txt");
        String cateIdRaw = request.getParameter("category");
        int cateId = 0;

        try {
            cateId = Integer.parseInt(cateIdRaw);
        } catch (NumberFormatException e) {
            cateId = 0;
        }

        List<Product> list;
        if (cateId == 0) {
            list = shopDao.searchByName(txtSearch);
        } else {
            list = shopDao.searchByNameAndCategory(txtSearch, cateId);
        }

        List<Category> listC = shopDao.getAllCategory();

        request.setAttribute("listP", list);
        request.setAttribute("listC", listC);

        request.getRequestDispatcher("view/user/store.jsp").forward(request, response);
    }

    // Phương thức xử lý ReadNotificationControl
    public void handleReadNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String nid = request.getParameter("id");
        HttpSession session = request.getSession();
        Object accObj = session.getAttribute("acc");

        if (nid != null && accObj != null) {
            try {
                int notificationId = Integer.parseInt(nid);

                // Đánh dấu đã đọc
                notificationDAO.markAsRead(notificationId);

                // Lấy nội dung thông báo
                Notification notify = notificationDAO.getNotificationById(notificationId);

                // Cập nhật session
                Account acc = (Account) accObj;
                String userCode = accountDAO.getUserCodeById(acc.getId());
                List<Notification> updatedList = notificationDAO.getAllByUser(userCode);
                int unreadCount = notificationDAO.countUnread(userCode);
                session.setAttribute("notifyList", updatedList);
                session.setAttribute("notifyCount", unreadCount);

                // 👉 Nếu message chứa mã đơn dạng #1234 → redirect đến order-history
                if (notify != null && notify.getMessage() != null) {
                    String message = notify.getMessage();
                    java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("#(\\d+)").matcher(message);
                    if (matcher.find()) {
                        String orderId = matcher.group(1); // lấy số sau #
                        response.sendRedirect("order-history?orderId=" + orderId);
                        return;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Nếu không có orderId → quay lại trang trước hoặc về home
        String referer = request.getHeader("referer");
        if (referer != null) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("home");
        }
    }


    // Phương thức xử lý RemoveCartItemControl
    public void handleRemoveCartItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pid = request.getParameter("pid");
        HttpSession session = request.getSession();

        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
        if (cart != null && pid != null && cart.containsKey(pid)) {
            cart.remove(pid);
            session.setAttribute("cart", cart);
        }

        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems != null) {
            cartItems.removeIf(item -> String.valueOf(item.getProduct().getId()).equals(pid));
            session.setAttribute("cartItems", cartItems);
        }

        session.setAttribute("cartCount", cart != null ? cart.size() : 0);

        response.sendRedirect(request.getHeader("Referer"));
    }

    // Phương thức xử lý CategoryControl
    public void handleCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String cateID = request.getParameter("cid");
        List<Product> list = shopDao.getProductsbyCid(cateID);
        List<Category> listC = shopDao.getAllCategory();

        if (listC == null) {
            listC = new ArrayList<>();
        }

        request.setAttribute("listP", list);
        request.setAttribute("listC", listC);

        try {
            int cid = Integer.parseInt(cateID);
            request.setAttribute("activeCid", cid);
        } catch (NumberFormatException e) {
            request.setAttribute("activeCid", null);
        }

        request.getRequestDispatcher("view/user/store.jsp").forward(request, response);
    }

    // Phương thức xử lý CheckoutControl
    public void handleCheckoutGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");
        if (acc == null) {
            response.sendRedirect("view/acc/login.jsp");
            return;
        }

        List<Category> listC = adminCategoryDAO.getAllCategories();
        session.setAttribute("listC", listC);

        Map<String, ?> cartRaw = (Map<String, ?>) session.getAttribute("cart");
        List<CartItem> cartItems = new ArrayList<>();
        AdminProductDAO productDAO = new AdminProductDAO();
        double total = 0;
        List<String> outOfStockMessages = new ArrayList<>();
        if (cartRaw != null) {
            for (Map.Entry<String, ?> entry : cartRaw.entrySet()) {
                try {
                    int productId = Integer.parseInt(entry.getKey());
                    int quantity = (entry.getValue() instanceof String)
                            ? Integer.parseInt((String) entry.getValue())
                            : (Integer) entry.getValue();

                    Product p = productDAO.getProductById(productId);
                    if (p != null) {
                        if (p.getQuantity() < quantity) {
                            outOfStockMessages.add("Sản phẩm '" + p.getName() + "' chỉ còn " + p.getQuantity() + " sản phẩm trong kho.");
                            continue;
                        }

                        CartItem item = new CartItem(p, quantity);
                        cartItems.add(item);
                        total += p.getPrice() * quantity;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        session.setAttribute("cartList", cartItems.isEmpty() ? null : cartItems);
        session.setAttribute("cartTotal", total);

        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        session.setAttribute("cartCount", count);

        // Truyền thông báo lỗi (nếu có) về JSP
        if (!outOfStockMessages.isEmpty()) {
            request.setAttribute("outOfStockMessages", outOfStockMessages);
        }

        request.getRequestDispatcher("view/user/checkout.jsp").forward(request, response);
    }


    public void handleCheckoutPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("view/acc/login.jsp");
            return;
        }

        // 👉 Xử lý tăng/giảm số lượng sản phẩm
        String action = request.getParameter("action");
        String productIdStr = request.getParameter("productId");
        String operation = request.getParameter("operation");

        System.out.println("DEBUG: action = " + action + ", productId = " + productIdStr + ", operation = " + operation);

        if ("updateQuantity".equals(action)) {
            List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartList");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
                session.setAttribute("cartList", cartItems);
            }

            if (productIdStr != null && !productIdStr.isEmpty() && operation != null) {
                try {
                    int productId = Integer.parseInt(productIdStr);

                    Iterator<CartItem> iterator = cartItems.iterator();
                    while (iterator.hasNext()) {
                        CartItem item = iterator.next();
                        if (item.getProduct().getId() == productId) {
                            System.out.println("DEBUG: Trước cập nhật: " + item.getQuantity());

                            if ("increase".equals(operation)) {
                                item.setQuantity(item.getQuantity() + 1);
                            } else if ("decrease".equals(operation)) {
                                int newQty = item.getQuantity() - 1;
                                if (newQty <= 0) {
                                    iterator.remove();
                                } else {
                                    item.setQuantity(newQty);
                                }
                            }

                            System.out.println("DEBUG: Sau cập nhật: " + item.getQuantity());
                            break;
                        }
                    }

                    // Tính lại tổng tiền và số lượng
                    double total = 0;
                    int totalQuantity = 0;
                    for (CartItem item : cartItems) {
                        total += item.getQuantity() * item.getProduct().getPrice();
                        totalQuantity += item.getQuantity();
                    }

                    session.setAttribute("cartList", cartItems); // cập nhật lại giỏ
                    session.setAttribute("cartTotal", total);
                    session.setAttribute("cartCount", totalQuantity);

                    // 👉 Cập nhật lại cart Map để handleCheckoutGet hiển thị đúng
                    Map<String, Integer> cartMap = new HashMap<>();
                    for (CartItem item : cartItems) {
                        cartMap.put(String.valueOf(item.getProduct().getId()), item.getQuantity());
                    }
                    session.setAttribute("cart", cartMap);

                    response.setStatus(HttpServletResponse.SC_OK);
                    return;

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            } else {
                System.out.println("DEBUG: Thiếu productId hoặc operation.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        // 👉 Xử lý đặt hàng bình thường
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute("cartList", cartItems);
        }

        Double total = (Double) session.getAttribute("cartTotal");
        if (cartItems.isEmpty() || total == null) {
            request.setAttribute("message", "Giỏ hàng trống!");
            handleCheckoutGet(request, response);
            return;
        }

        String name, email, phone, address;
        boolean isGuest = request.getParameter("isGuestOrder") != null;

        if (isGuest) {
            name = request.getParameter("guestFullName");
            email = request.getParameter("guestEmail");
            phone = request.getParameter("guestPhone");
            address = request.getParameter("guestAddress");
        } else {
            name = acc.getFullName();
            email = acc.getEmail();
            phone = acc.getPhone();
            address = acc.getAddress();
        }

        // Lấy userCode từ DB
        String userCode = null;
        try {
            userCode = accountDAO.getUserCodeById(acc.getId());
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("flash", "Lỗi lấy thông tin người dùng.");
            response.sendRedirect("home");
            return;
        }

        if (userCode == null) {
            session.setAttribute("flash", "Không tìm thấy mã người dùng.");
            response.sendRedirect("home");
            return;
        }

        boolean success = adminOrderDAO.createOrder(userCode, name, email, phone, address, cartItems, total);

        if (success) {
            session.removeAttribute("cart");
            session.removeAttribute("cartCount");
            session.removeAttribute("cartTotal");
            session.removeAttribute("cartList");
            session.removeAttribute("cartItems");

            session.setAttribute("cartList", new ArrayList<CartItem>());
            session.setAttribute("cartItems", new ArrayList<CartItem>());
            session.setAttribute("cartCount", 0);
            session.setAttribute("cartTotal", 0.0);

            List<Notification> notifyList = adminNotificationDAO.getUnreadNotificationsByUser(acc.getCode());
            session.setAttribute("notifyList", notifyList);
            session.setAttribute("notifyCount", notifyList.size());

            session.setAttribute("flash", "Đặt hàng thành công!");
        } else {
            session.setAttribute("flash", "Có lỗi xảy ra khi đặt hàng.");
        }

        response.sendRedirect("home");
    }

    public void handleCancelOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        String reason = request.getParameter("reason");
        boolean success = adminOrderDAO.cancelOrder(orderId, reason);

        response.setContentType("text/plain");
        if (success) {
            response.getWriter().write("OK");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không thể hủy đơn hàng.");
        }
    }



    // Phương thức xử lý OrderHistoryControl
    public void handleOrderHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("view/acc/login.jsp");
            return;
        }

        // Sử dụng userCode thay vì userId
        String userCode = accountDAO.getUserCodeById(acc.getId());
        List<Order> orders = adminOrderDAO.searchOrdersByUserCode(userCode, null);
        List<Category> listC = adminCategoryDAO.getAllCategories();

        request.setAttribute("orders", orders);
        request.setAttribute("listC", listC);

        request.getRequestDispatcher("view/user/orderHistory.jsp").forward(request, response);
    }

    // Phương thức xử lý HomeControl
    public void handleHome(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        List<Product> list = shopDao.getAllProducts();
        List<Category> listC = shopDao.getAllCategory();

        request.setAttribute("listP", list);
        request.setAttribute("listC", listC);

        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (acc != null) {
            // Sử dụng userCode thay vì userId
            String userCode = accountDAO.getUserCodeById(acc.getId());
            List<Notification> notifyList = notificationDAO.getAllByUser(userCode);
            int unreadCount = notificationDAO.countUnread(userCode);

            session.setAttribute("notifyList", notifyList);
            session.setAttribute("notifyCount", unreadCount);
        }

        request.getRequestDispatcher("view/user/home.jsp").forward(request, response);
    }

    // Phương thức xử lý AddToCartControl
    public void handleAddToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("acc") == null) {
            response.sendRedirect("view/acc/login.jsp");
            return;
        }

        String pid = request.getParameter("pid");
        String quantityStr = request.getParameter("quantity");
        int quantity = 1;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) quantity = 1;
        } catch (NumberFormatException e) {
            quantity = 1;
        }

        if (pid == null || pid.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        cart.put(pid, cart.getOrDefault(pid, 0) + quantity);
        session.setAttribute("cart", cart);

        int totalItems = cart.values().stream().mapToInt(i -> i).sum();
        session.setAttribute("cartCount", totalItems);

        AdminProductDAO dao = new AdminProductDAO();
        List<CartItem> cartItems = new ArrayList<>();
        double cartTotal = 0;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            try {
                int productId = Integer.parseInt(entry.getKey());
                Product p = dao.getProductById(productId);
                if (p != null) {
                    CartItem item = new CartItem(p, entry.getValue());
                    cartItems.add(item);
                    cartTotal += item.getTotalPrice();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        session.setAttribute("cartItems", cartItems);
        session.setAttribute("cartTotal", cartTotal);

        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "home");
    }
}