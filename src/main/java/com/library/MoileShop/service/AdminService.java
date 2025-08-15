package com.library.MoileShop.service;

import com.library.MoileShop.dao.AdminOrderDAO;
import com.library.MoileShop.dao.AdminAccountDAO;
import com.library.MoileShop.dao.AdminProductDAO;
import com.library.MoileShop.dao.AdminCategoryDAO;
import com.library.MoileShop.dao.AdminNotificationDAO;
import com.library.MoileShop.dao.AccountDAO;
import com.library.MoileShop.entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    private final AdminOrderDAO orderDAO;
    private final AdminAccountDAO accDAO;
    private final AdminProductDAO productDAO;
    private final AdminCategoryDAO categoryDAO;
    private final AdminNotificationDAO notificationDAO;
    private final AccountDAO accountDAO;
    private static final String IMAGE_SAVE_PATH = "D:\\image";
    private static final int PAGE_SIZE = 5;

    public AdminService() {
        this.orderDAO = new AdminOrderDAO();
        this.accDAO = new AdminAccountDAO();
        this.productDAO = new AdminProductDAO();
        this.categoryDAO = new AdminCategoryDAO();
        this.notificationDAO = new AdminNotificationDAO();
        this.accountDAO = new AccountDAO();
    }

    // Xử lý tìm kiếm đơn hàng (có thêm lọc trạng thái)
    public void handleSearchOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String customerName = request.getParameter("customer");
        String orderDate = request.getParameter("orderDate");
        String status = request.getParameter("status"); // <- Lấy trạng thái

        if (customerName == null) customerName = "";
        if (orderDate != null && orderDate.trim().isEmpty()) orderDate = null;
        if (status != null && status.trim().isEmpty()) status = null;

        // Phân trang
        int page = 1;
        int size = 5;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ignored) {}

        // Tổng số đơn phù hợp
        int total = orderDAO.countOrders(customerName, orderDate, status);
        int totalPage = (int) Math.ceil(total * 1.0 / size);
        int offset = (page - 1) * size;

        // Lấy danh sách đơn phù hợp
        List<Order> orderList = orderDAO.searchOrders(customerName, orderDate, status, offset, size);

        // Đưa dữ liệu về JSP
        request.setAttribute("orderList", orderList);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("customer", customerName);
        request.setAttribute("orderDate", orderDate);
        request.setAttribute("status", status);
        request.setAttribute("pageSize", size);
        request.getRequestDispatcher("view/ad/adminOrder.jsp").forward(request, response);
    }


    // Xử lý danh sách tài khoản
    public void handleGetAllAccounts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Account> list = accDAO.getAllAccounts();
        request.setAttribute("accountList", list);
        request.getRequestDispatcher("/view/ad/adminAccount.jsp").forward(request, response);
    }

    // Xử lý danh sách danh mục
    public void handleGetAllCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        int page = 1;
        int size = 10;

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ignored) {}

        int total = categoryDAO.count(keyword); //
        int totalPage = (int) Math.ceil(total * 1.0 / size);
        int offset = (page - 1) * size;

        List<Category> list = categoryDAO.search(keyword, offset, size);

        request.setAttribute("categoryList", list);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/view/ad/adminCategory.jsp").forward(request, response);
    }


    // Xử lý danh sách thông báo
    public void handleSearchNotifications(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        // Thiết lập phân trang
        int page = 1;
        int size = 5; // Số thông báo mỗi trang
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ignored) {}

        // Xử lý logic phân trang
        int total = notificationDAO.countNotifications(keyword);
        int totalPage = (int) Math.ceil(total * 1.0 / size);
        int offset = (page - 1) * size;

        List<Notification> list = notificationDAO.searchNotifications(keyword, offset, size);
        request.setAttribute("notificationList", list);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("keyword", keyword);
        request.setAttribute("pageSize", size);
        request.getRequestDispatcher("view/ad/adminNotification.jsp").forward(request, response);
    }
    // xử lý thống kê
    public void handleDashboardStatistics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int totalOrders = orderDAO.countOrders(null, null);
        int totalProducts = productDAO.countProducts("");
        int totalUsers = accDAO.getAllUsers().size();
        int totalNotifications = notificationDAO.countNotifications("");

        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalNotifications", totalNotifications);

        request.getRequestDispatcher("view/ad/admin.jsp").forward(request, response);
    }

    // Xử lý danh sách sản phẩm
    public void handleSearchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        int page = 1;
        int size = 5;

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }

            String sizeParam = request.getParameter("size");
            if (sizeParam != null) {
                size = Integer.parseInt(sizeParam);
            }
        } catch (NumberFormatException ignored) {}

        int total = productDAO.countProducts(keyword);
        int totalPage = (int) Math.ceil(total * 1.0 / size);
        int offset = (page - 1) * size;

        List<Product> productList = productDAO.search(keyword, offset, size);
        request.setAttribute("productList", productList);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("keyword", keyword);
        request.setAttribute("pageSize", size);
        request.getRequestDispatcher("/view/ad/adminProduct.jsp").forward(request, response);
    }


    // Xử lý thêm sản phẩm
    public void handleAddProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        try {
            String masp = request.getParameter("masp");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String qtyStr = request.getParameter("quantity");
            int categoryId = Integer.parseInt(request.getParameter("category_id"));

            if (!productDAO.isCategoryExists(categoryId)) {
                session.setAttribute("msg", " Danh mục không hợp lệ hoặc không tồn tại!");
                session.setAttribute("msgType", "danger");
                response.sendRedirect("admin-product");
                return;
            }

            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(qtyStr.trim());

            Part filePart = request.getPart("image");
            String submittedFileName = filePart.getSubmittedFileName();
            if (submittedFileName == null || submittedFileName.trim().isEmpty()) {
                session.setAttribute("msg", " Vui lòng chọn ảnh!");
                session.setAttribute("msgType", "danger");
                response.sendRedirect("admin-product");
                return;
            }

            File uploadDir = new File(IMAGE_SAVE_PATH);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = new File(submittedFileName).getName();
            File savedFile = new File(uploadDir, fileName);

            try (InputStream input = filePart.getInputStream();
                 FileOutputStream output = new FileOutputStream(savedFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
                session.setAttribute("msg", " Không thể lưu ảnh: " + e.getMessage());
                session.setAttribute("msgType", "danger");
                response.sendRedirect("admin-product");
                return;
            }

            Product p = new Product(0, name, masp, price, description, IMAGE_SAVE_PATH + "\\" + fileName, quantity, categoryId);
            boolean success = productDAO.addProduct(p);
            session.setAttribute("msg", success ? " Thêm sản phẩm thành công!" : "Thêm sản phẩm thất bại!");
            session.setAttribute("msgType", success ? "success" : "danger");
            System.out.println("Add product result: " + success + ", categoryId: " + categoryId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("msg", " Dữ liệu đầu vào không hợp lệ!");
            session.setAttribute("msgType", "danger");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", " Lỗi xử lý dữ liệu: " + e.getMessage());
            session.setAttribute("msgType", "danger");
        }
        response.sendRedirect("admin-product");
    }
    public List<Category> getAllCategories() {
        return categoryDAO.getAll(""); // Truyền keyword rỗng để lấy tất cả
    }


    // Xử lý thêm danh mục
    public void handleAddCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        Category c = new Category();
        c.setCname(name);
        c.setDescription(description);

        boolean result = categoryDAO.addCategory(c);

        // Chuyển hướng về lại admin-category.jsp với thông báo
        if (result) {
            response.sendRedirect("admin-category?success=" + URLEncoder.encode("Thêm thành công", "UTF-8"));
        } else {
            response.sendRedirect("admin-category?error=" + URLEncoder.encode("Thêm thất bại", "UTF-8"));
        }
    }


    // Xử lý tạo thông báo
    public void handleCreateNotification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String message = request.getParameter("message");
        String receiver = request.getParameter("receiver");

        if (message == null || message.trim().isEmpty()) {
            request.setAttribute("error", "Nội dung thông báo không được để trống!");
            handleCreateNotificationForm(request, response);
            return;
        }

        try {
            if ("all".equals(receiver)) {
                List<Account> allUsers = accountDAO.getAllAccounts();
                for (Account user : allUsers) {
                    notificationDAO.insertNotification(user.getCode(), message);
                }
            } else {
                Account acc = accountDAO.getAccountByUsername(receiver);
                if (acc != null) {
                    notificationDAO.insertNotification(acc.getCode(), message);
                } else {
                    request.setAttribute("error", "Người nhận không tồn tại!");
                    handleCreateNotificationForm(request, response);
                    return;
                }
            }

            request.getSession().setAttribute("msg", "Gửi thông báo thành công!");
            response.sendRedirect("admin-notification");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tạo thông báo: " + e.getMessage());
            handleCreateNotificationForm(request, response);
        }
    }

    // Hiển thị form tạo thông báo
    public void handleCreateNotificationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        List<Account> userList = new ArrayList<>();
        try {
            userList = accountDAO.getAllAccounts();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách người dùng: " + e.getMessage());
        }
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/view/ad/createNotification.jsp").forward(request, response);
    }

    // Xử lý đổi mật khẩu
    public void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        HttpSession session = request.getSession();
        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("msg", "Mật khẩu xác nhận không khớp!");
            session.setAttribute("msgType", "danger");
        } else {
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
            accDAO.updatePassword(id, hashedPassword);
            session.setAttribute("msg", "Đổi mật khẩu thành công.");
            session.setAttribute("msgType", "success");
        }
        response.sendRedirect("admin-accounts");
    }

    // Hiển thị form đổi mật khẩu
    public void handleChangePasswordForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Account acc = accDAO.getAccountById(id);
        request.setAttribute("acc", acc);
        request.getRequestDispatcher("view/ad/changePassword.jsp").forward(request, response);
    }

    // Xử lý tạo đơn hàng
    public void handleCreateOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            String userIdParam = request.getParameter("userId");
            String userCode = null;

            if (userIdParam != null && !userIdParam.trim().isEmpty()) {
                int userId = Integer.parseInt(userIdParam);
                userCode = accountDAO.getUserCodeById(userId);
            }

            String[] productIds = request.getParameterValues("productIds");
            String[] quantities = request.getParameterValues("quantities");

            if (productIds == null || productIds.length == 0) {
                showAlert(response, "Vui lòng chọn ít nhất một sản phẩm.");
                return;
            }

            List<CartItem> cartItems = new ArrayList<>();
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int qty = Integer.parseInt(quantities[i]);
                Product p = productDAO.getProductById(productId);
                cartItems.add(new CartItem(p, qty));
            }

            // Lấy thông tin người nhận
            String name = request.getParameter("recipientName");
            String email = request.getParameter("recipientEmail");
            String phone = request.getParameter("recipientPhone");
            String address = request.getParameter("recipientAddress");

            if (name == null || phone == null || address == null ||
                    name.trim().isEmpty() || phone.trim().isEmpty() || address.trim().isEmpty()) {
                showAlert(response, "Vui lòng nhập đầy đủ thông tin người nhận.");
                return;
            }

            // Tính tổng giá
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }

            boolean success = orderDAO.createOrder(userCode, name, email, phone, address, cartItems, total);

            if (success) {
                response.sendRedirect("admin-order");
            } else {
                showAlert(response, "Không thể tạo đơn hàng.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(response, "Lỗi xử lý đơn hàng.");
        }
    }


    // Hiển thị form tạo đơn hàng
    public void handleCreateOrderForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Account> users = accDAO.getAllUsers();
            request.setAttribute("users", users);

            String pageStr = request.getParameter("page");
            int page = (pageStr != null) ? Integer.parseInt(pageStr) : 1;

            int totalProducts = productDAO.countProducts("");
            int totalPage = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

            List<Product> products = productDAO.search("", (page - 1) * PAGE_SIZE, PAGE_SIZE);

            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPage", totalPage);

            String requestedWith = request.getHeader("X-Requested-With");
            boolean isAjax = "XMLHttpRequest".equals(requestedWith);

            if (isAjax) {
                request.getRequestDispatcher("/view/ad/createOrder.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/view/ad/adminOrder.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Lỗi khi tải form tạo đơn hàng: " + e.getMessage());
        }
    }

    // Xử lý đổi vai trò
    public void handleChangeRole(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String role = request.getParameter("role");

        boolean success = accDAO.updateRole(id, role);
        HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("msg", "Cập nhật vai trò thành công.");
            session.setAttribute("msgType", "success");
        } else {
            session.setAttribute("msg", "Cập nhật vai trò thất bại.");
            session.setAttribute("msgType", "danger");
        }
        response.sendRedirect("admin-accounts");
    }

    // Xử lý xóa sản phẩm
    public void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        HttpSession session = request.getSession();

        try {
            int id = Integer.parseInt(idRaw);
            boolean deleted = productDAO.deleteProductById(id);

            if (deleted) {
                session.setAttribute("msg", " Xóa sản phẩm thành công!");
                session.setAttribute("msgType", "success");
            } else {
                session.setAttribute("msg", " Không tìm thấy sản phẩm cần xóa!");
                session.setAttribute("msgType", "warning");
            }
        } catch (Exception e) {
            session.setAttribute("msg", " Lỗi khi xóa sản phẩm: " + e.getMessage());
            session.setAttribute("msgType", "danger");
            e.printStackTrace();
        }
        response.sendRedirect("admin-product");
    }

    // Xử lý chi tiết đơn hàng
    public void handleOrderDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            System.out.println(" Xem chi tiết đơn hàng ID = " + orderId);

            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }

            List<OrderItem> items = orderDAO.getOrderItemsByOrderId(orderId);
            System.out.println("Tìm thấy " + items.size() + " sản phẩm trong đơn hàng.");

            request.setAttribute("order", order);
            request.setAttribute("items", items);
            request.getRequestDispatcher("view/ad/orderDetail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }

    // Xử lý xóa danh mục
    public void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String message;
        String redirectURL = "admin-category";

        try {
            int cid = Integer.parseInt(request.getParameter("id"));
            boolean success = categoryDAO.deleteCategory(cid);

            if (success) {
                message = "Xóa thành công!";
                redirectURL += "?success=" + URLEncoder.encode(message, "UTF-8");
            } else {
                message = "Không thể xóa danh mục. Có thể vì danh mục đang chứa sản phẩm.";
                redirectURL += "?error=" + URLEncoder.encode(message, "UTF-8");
            }

        } catch (NumberFormatException e) {
            message = "ID không hợp lệ.";
            redirectURL += "?error=" + URLEncoder.encode(message, "UTF-8");
        } catch (Exception e) {
            message = "Có lỗi xảy ra trong quá trình xóa.";
            redirectURL += "?error=" + URLEncoder.encode(message, "UTF-8");
        }

        response.sendRedirect(redirectURL);
    }

    // Xử lý đổi trạng thái tài khoản
    public void handleToggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = accDAO.toggleStatus(id);
            if (success) {
                System.out.println("Đã đổi trạng thái tài khoản có ID: " + id);
            } else {
                System.out.println("Không thể đổi trạng thái tài khoản ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("admin-accounts");
    }

    // Xử lý sửa sản phẩm
    public void handleEditProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        try {
            // Kiểm tra và parse id
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                session.setAttribute("msg", " ID sản phẩm không hợp lệ!");
                session.setAttribute("msgType", "danger");
                response.sendRedirect("admin-product");
                return;
            }
            int id = Integer.parseInt(idStr);

            String masp = request.getParameter("masp");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String qtyStr = request.getParameter("quantity");
            String oldImage = request.getParameter("oldImage");
            String categoryIdStr = request.getParameter("category_id");

            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                session.setAttribute("msg", " Danh mục không hợp lệ!");
                session.setAttribute("msgType", "danger");
                response.sendRedirect("admin-product");
                return;
            }
            int categoryId = Integer.parseInt(categoryIdStr);

            if (priceStr == null || priceStr.trim().isEmpty()) {
                session.setAttribute("msg", " Giá không hợp lệ!");
                session.setAttribute("msgType", "danger");
                response.sendRedirect("admin-product");
                return;
            }
            double price = Double.parseDouble(priceStr);

            if (qtyStr == null || qtyStr.trim().isEmpty()) {
                session.setAttribute("msg", " Số lượng không hợp lệ!");
                session.setAttribute("msgType", "danger");
                response.sendRedirect("admin-product");
                return;
            }
            int quantity = Integer.parseInt(qtyStr.trim());

            if (!productDAO.isCategoryExists(categoryId)) {
                session.setAttribute("msg", " Danh mục không hợp lệ hoặc không tồn tại!");
                session.setAttribute("msgType", "danger");
                response.sendRedirect("admin-product");
                return;
            }

            String imagePath = oldImage;

            Part filePart = request.getPart("image");
            String submittedFileName = filePart.getSubmittedFileName();
            if (submittedFileName != null && !submittedFileName.trim().isEmpty()) {
                File uploadDir = new File(IMAGE_SAVE_PATH);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileName = new File(submittedFileName).getName();
                File savedFile = new File(uploadDir, fileName);

                try (InputStream input = filePart.getInputStream();
                     FileOutputStream output = new FileOutputStream(savedFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }
                imagePath = IMAGE_SAVE_PATH + "\\" + submittedFileName;
            }

            Product p = new Product(id, name, masp, price, description, imagePath, quantity, categoryId);
            boolean updated = productDAO.updateProduct(p);
            session.setAttribute("msg", updated ? " Cập nhật sản phẩm thành công!" : " Cập nhật sản phẩm thất bại!");
            session.setAttribute("msgType", updated ? "success" : "danger");
            System.out.println("Update product result: " + updated + ", categoryId: " + categoryId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("msg", " Dữ liệu đầu vào không hợp lệ!");
            session.setAttribute("msgType", "danger");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Lỗi xử lý dữ liệu: " + e.getMessage());
            session.setAttribute("msgType", "danger");
        }
        response.sendRedirect("admin-product");
    }

    // Hiển thị form sửa sản phẩm
    public void handleEditProductForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("admin-product");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Product product = productDAO.getProductById(id);
            if (product == null) {
                response.sendRedirect("admin-product");
                return;
            }
            List<Category> categories = categoryDAO.getAll("");
            request.setAttribute("product", product);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("view/ad/editProducts.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-product");
        }
    }

    // Xử lý xóa tài khoản
    public void handleDeleteAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = accDAO.deleteAccount(id);

        HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("msg", "Xóa tài khoản thành công!");
            session.setAttribute("msgType", "success");
        } else {
            session.setAttribute("msg", "Xóa tài khoản thất bại!");
            session.setAttribute("msgType", "danger");
        }
        response.sendRedirect("admin-accounts");
    }

    // Xử lý sửa tài khoản
    public void handleEditAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account acc = new Account();
        acc.setId(Integer.parseInt(request.getParameter("id")));
        acc.setFullName(request.getParameter("fullName"));
        acc.setEmail(request.getParameter("email"));
        acc.setPhone(request.getParameter("phone"));
        acc.setAddress(request.getParameter("address"));

        boolean success = accDAO.updateAccount(acc);

        HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("msg", "Cập nhật tài khoản thành công!");
            session.setAttribute("msgType", "success");
        } else {
            session.setAttribute("msg", "Cập nhật tài khoản thất bại!");
            session.setAttribute("msgType", "danger");
        }
        response.sendRedirect("admin-accounts");
    }

    // Hiển thị form sửa tài khoản
    public void handleEditAccountForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Account acc = accDAO.getAccountById(id);
        request.setAttribute("acc", acc);
        request.getRequestDispatcher("view/ad/editAccount.jsp").forward(request, response);
    }

    // Xử lý sửa danh mục
    public void handleEditCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int cid = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            Category c = new Category(cid, name, description);
            categoryDAO.updateCategory(c);
        } catch (Exception ignored) {
        }
        response.sendRedirect("admin-category");
    }

    // Hiển thị form sửa danh mục
    public void handleEditCategoryForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int cid = Integer.parseInt(request.getParameter("id"));
            Category c = categoryDAO.getById(cid);

            if (c != null) {
                request.setAttribute("category", c);
                request.getRequestDispatcher("/view/ad/editCategory.jsp").forward(request, response);
            } else {
                response.sendRedirect("admin-category");
            }
        } catch (Exception e) {
            response.sendRedirect("admin-category");
        }
    }

    // Xử lý cập nhật trạng thái đơn hàng
    public void handleUpdateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdRaw = request.getParameter("orderId");
        String status = request.getParameter("status");
        String cancelReason = request.getParameter("cancelReason");

        try {
            int orderId = Integer.parseInt(orderIdRaw);
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                response.sendRedirect("admin-order?error=notfound");
                return;
            }

            boolean updated = orderDAO.updateOrderStatus(orderId, status, cancelReason);

            if (updated) {
                notifyUserAndSendEmail(order, orderId, status, cancelReason);
            }

            response.sendRedirect("admin-order");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-order?error=invalid");
        }
    }

    private void notifyUserAndSendEmail(Order order, int orderId, String status, String cancelReason) {
        String userCode = (order.getCustomer() != null) ? order.getCustomer().getCode() : null;

        // Tạo nội dung thông báo
        String message;
        switch (status) {
            case "Đang giao":
                message = "Đơn hàng #" + orderId + " đang được giao đến bạn.";
                break;
            case "Hoàn thành":
                message = "Đơn hàng #" + orderId + " đã hoàn thành.";
                break;
            case "Đã hủy":
                message = "Đơn hàng #" + orderId + " đã bị hủy. ";
                break;
            default:
                message = "Trạng thái đơn hàng #" + orderId + " đã được cập nhật thành: " + status;
        }

        // Gửi thông báo trong hệ thống
        if (userCode != null && !userCode.isEmpty()) {
            orderDAO.notifyUser(userCode, message);

            // Gửi email nếu có địa chỉ email
            String recipientEmail = order.getRecipientEmail();
            if (recipientEmail != null && !recipientEmail.trim().isEmpty()) {
                try {
                    String subject = "Cập nhật trạng thái đơn hàng #" + orderId;
                    String content = "<h2>Thông báo cập nhật đơn hàng</h2>"
                            + "<p>Đơn hàng #" + orderId + " của bạn đã được cập nhật trạng thái:</p>"
                            + "<p><strong>Trạng thái mới:</strong> " + status + "</p>"
                            + "<p>" + message + "</p>"
                            + "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>";

                    AccService accService = new AccService();
                    accService.sendEmail(recipientEmail, subject, content);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Không thể gửi email thông báo: " + e.getMessage());
                }
            }
        }
    }




    private void showAlert(HttpServletResponse response, String message) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + message + "'); window.history.back();</script>");
    }
}