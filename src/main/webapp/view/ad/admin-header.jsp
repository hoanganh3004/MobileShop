<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.library.MoileShop.entity.Account" %>
<%
    Account acc = (Account) session.getAttribute("acc");
    String currentPath = request.getRequestURI();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang quản trị</title>
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
</head>
<body id="page-top">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- Brand -->
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${pageContext.request.contextPath}/view/ad/admin.jsp">
            <div class="sidebar-brand-icon rotate-n-15">
                <i class="fas fa-tools"></i>
            </div>
            <div class="sidebar-brand-text mx-3">Quản Trị</div>
        </a>

        <hr class="sidebar-divider my-0">

        <li class="nav-item <%= currentPath.endsWith("/admin-accounts") ? "active" : "" %>">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin-accounts">
                <i class="fas fa-user-cog"></i>
                <span>Quản lý tài khoản</span>
            </a>
        </li>

        <li class="nav-item <%= currentPath.endsWith("/admin-product") ? "active" : "" %>">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin-product">
                <i class="fas fa-box-open"></i>
                <span>Quản lý sản phẩm</span>
            </a>
        </li>

        <li class="nav-item <%= currentPath.endsWith("/admin-category") ? "active" : "" %>">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin-category">
                <i class="fas fa-th-list"></i>
                <span>Quản lý danh mục</span>
            </a>
        </li>

        <li class="nav-item <%= currentPath.endsWith("/admin-order") ? "active" : "" %>">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin-order">
                <i class="fas fa-shopping-cart"></i>
                <span>Quản lý đơn hàng</span>
            </a>
        </li>

        <li class="nav-item <%= currentPath.endsWith("/admin-notification") ? "active" : "" %>">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin-notification">
                <i class="fas fa-bell"></i>
                <span>Thông báo</span>
            </a>
        </li>

        <hr class="sidebar-divider d-none d-md-block">

        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                <i class="fas fa-sign-out-alt"></i>
                <span>Đăng xuất</span>
            </a>
        </li>
    </ul>

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
                <div class="d-flex justify-content-between w-100">
                    <h5 class="text-primary m-0 font-weight-bold">Trang quản trị hệ thống</h5>
                    <div class="text-dark font-weight-bold">
                        <% if (acc != null) { %>
                        Xin chào, <%= acc.getFullName() %>
                        <% } %>
                    </div>
                </div>
            </nav>
