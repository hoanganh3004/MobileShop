package com.library.MoileShop.control.ad;
import com.library.MoileShop.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/create-notification")
public class CreateNotificationControl extends HttpServlet {
    private final AdminService adminService;

    public CreateNotificationControl() {
        this.adminService = new AdminService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        adminService.handleCreateNotification(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        adminService.handleCreateNotificationForm(request, response);
    }
}