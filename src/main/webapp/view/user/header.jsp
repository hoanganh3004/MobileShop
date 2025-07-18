<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="currentUrl" value="${pageContext.request.requestURI}${pageContext.request.queryString != null ? '?' += pageContext.request.queryString : ''}" />

<style>
    .user-dropdown-wrapper,
    .notify-dropdown-wrapper {
        position: relative;
    }

    .user-dropdown, .notify-dropdown {
        top: 100%;
        right: 0;
        position: absolute;
        display: none;
        background-color: white;
        min-width: 200px;
        z-index: 1000;
        border-radius: 4px;
        box-shadow: 0 6px 12px rgba(0,0,0,.175);
    }

    .user-dropdown-wrapper:hover .user-dropdown,
    .notify-dropdown-wrapper:hover .notify-dropdown {
        display: block;
    }

    .dropdown-item {
        padding: 10px 16px;
        text-decoration: none;
        display: block;
        border-left: 4px solid transparent;
        transition: background-color 0.3s, color 0.3s;
    }

    /* ✅ Mặc định */
    .notify-dropdown .dropdown-item,
    .user-dropdown .dropdown-item {
        color: #333 !important;
        background-color: #fff !important;
    }

    /* ✅ Hover hiệu ứng */
    .notify-dropdown .dropdown-item:hover,
    .user-dropdown .dropdown-item:hover {
        color: #D10024 !important;
        background-color: #f5f5f5 !important;
        font-weight: bold;
    }

    /* ✅ Thông báo chưa đọc: làm đậm, nền khác và viền trái màu xanh */
    .notify-dropdown .dropdown-item.unread {
        font-weight: bold;
        background-color: #e6f7ff !important;
        border-left: 4px solid #1890ff;
    }

    .header-ctn {
        display: flex;
        justify-content: flex-end;
        gap: 20px;
        align-items: center;
    }

    .header-icon {
        text-align: center;
        position: relative;
    }

    .header-icon .qty {
        position: absolute;
        top: -8px;
        right: -8px;
        background: #D10024;
        color: white;
        border-radius: 50%;
        font-size: 12px;
        width: 18px;
        height: 18px;
        line-height: 18px;
        text-align: center;
    }
</style>


<!-- HEADER -->
<header>
    <!-- TOP HEADER -->
    <div id="top-header">
        <div class="container">
            <ul class="header-links pull-left">
                <li><a href="#"><i class="fa fa-phone"></i> +123456789</a></li>
                <li><a href="#"><i class="fa fa-envelope-o"></i> Hoanganhhy3004@gmail.com</a></li>
                <li><a href="#"><i class="fa fa-map-marker"></i> Hồng nhan, Bạc phận, Sóng gió</a></li>
            </ul>
            <ul class="header-links pull-right">
                <c:choose>
                    <c:when test="${not empty sessionScope.acc}">
                        <!-- THÔNG BÁO -->
                        <li class="nav-item dropdown notify-dropdown-wrapper">
                            <a href="#" class="nav-link">
                                <i class="fa fa-bell-o"></i> Thông báo
                                <c:if test="${sessionScope.notifyCount > 0}">
                                    <span class="badge qty">${sessionScope.notifyCount}</span>
                                </c:if>
                            </a>
                            <div class="dropdown-menu notify-dropdown">
                                <c:if test="${not empty sessionScope.notifyList}">
                                    <c:forEach var="n" items="${sessionScope.notifyList}">
                                        <a class="dropdown-item ${!n.read ? 'unread' : ''}"
                                           href="${pageContext.request.contextPath}/read-notification?id=${n.id}">
                                            <strong>Thông báo</strong><br/>
                                            <small>${n.message}</small><br/>
                                            <small><i>${n.createdAt}</i></small>
                                        </a>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty sessionScope.notifyList}">
                                    <span class="dropdown-item">Không có thông báo</span>
                                </c:if>
                            </div>
                        </li>
                        <!-- TÀI KHOẢN -->
                        <li class="nav-item dropdown user-dropdown-wrapper">
                            <a class="nav-link dropdown-toggle" href="#">
                                <i class="fa fa-user-o"></i> ${sessionScope.acc.fullName}
                            </a>
                            <div class="dropdown-menu user-dropdown">
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/account">Thông tin chi tiết</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                            </div>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/login"><i class="fa fa-user-o"></i> Đăng nhập</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>

    <!-- MAIN HEADER -->
    <div id="header">
        <div class="container">
            <div class="row">
                <!-- LOGO -->
                <div class="col-md-3">
                    <div class="header-logo">
                        <a href="${pageContext.request.contextPath}/home" class="logo">
                            <img src="${pageContext.request.contextPath}/img/logo.png" alt="">
                        </a>
                    </div>
                </div>

                <!-- SEARCH -->
                <div class="col-md-6">
                    <div class="header-search">
                        <form action="search" method="get">
                            <select class="input-select" name="category">
                                <option value="0">Tất cả</option>
                                <c:forEach items="${listC}" var="p">
                                    <option value="${p.cid}">${p.cname}</option>
                                </c:forEach>
                            </select>
                            <input class="input" name="txt" placeholder="Tìm kiếm tại đây">
                            <button type="submit" class="search-btn">Tìm kiếm</button>
                        </form>
                    </div>
                </div>

                <!-- TÀI KHOẢN -->
                <div class="col-md-3 clearfix">
                    <div class="header-ctn">

                        <!-- LỊCH SỬ -->
                        <div class="header-icon">
                            <c:choose>
                                <c:when test="${not empty sessionScope.acc}">
                                    <a href="${pageContext.request.contextPath}/order-history">
                                        <i class="fa fa-history"></i>
                                        <span>Lịch sử đặt hàng</span>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/view/acc/login.jsp">
                                        <i class="fa fa-history"></i>
                                        <span>Lịch sử đặt hàng</span>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>


                        <!-- GIỎ HÀNG -->
                        <div class="header-icon dropdown">
                            <c:choose>
                                <c:when test="${not empty sessionScope.acc}">
                                    <a class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">
                                        <i class="fa fa-shopping-cart"></i>
                                        <span>Giỏ hàng của bạn</span>
                                        <c:if test="${not empty sessionScope.cartCount && sessionScope.cartCount > 0}">
                                            <div class="qty">${sessionScope.cartCount}</div>
                                        </c:if>
                                    </a>
                                    <div class="cart-dropdown">
                                        <div class="cart-list">
                                            <c:if test="${not empty sessionScope.cartItems}">
                                                <c:forEach var="item" items="${sessionScope.cartItems}">
                                                    <div class="product-widget" style="position: relative;">
                                                        <div class="product-img">
                                                            <img src="${pageContext.request.contextPath}/image?file=${fn:substringAfter(item.product.image, 'image\\')}"
                                                                 alt="${item.product.name}" width="50" height="50" />
                                                        </div>
                                                        <div class="product-body">
                                                            <h3 class="product-name">${item.product.name}</h3>
                                                            <h4 class="product-price">${item.quantity} x $${item.product.price}</h4>
                                                        </div>

                                                        <!-- Nút xóa -->
                                                        <a href="${pageContext.request.contextPath}/remove-cart-item?pid=${item.product.id}"
                                                           style="position: absolute; top: 0; right: 0; color: red; font-weight: bold; text-decoration: none;"
                                                           onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')">×</a>
                                                    </div>
                                                </c:forEach>

                                            </c:if>
                                            <c:if test="${empty sessionScope.cartItems}">
                                                <p>Giỏ hàng của bạn đang trống</p>
                                            </c:if>
                                        </div>
                                        <div class="cart-summary">
                                            <small>${sessionScope.cartCount} sản phẩm đã chọn</small>
                                            <h5>
                                                TỔNG CỘNG:
                                                $<fmt:formatNumber value="${sessionScope.cartTotal}" type="number" maxFractionDigits="2"/>
                                            </h5>
                                        </div>
                                        <div class="cart-btns">
                                            <a href="${pageContext.request.contextPath}/checkout">Xem giỏ hàng</a>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/view/acc/login.jsp">
                                        <i class="fa fa-shopping-cart"></i>
                                        <span>Giỏ hàng của bạn</span>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- MENU -->
                        <div class="menu-toggle header-icon">
                            <a href="#">
                                <i class="fa fa-bars"></i>
                                <span>Menu</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>

<!-- NAVIGATION -->
<nav id="navigation">
    <div class="container">
        <div id="responsive-nav">
            <ul class="main-nav nav navbar-nav">
                <li class="${empty activeCid ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                </li>
                <c:forEach items="${listC}" var="p">
                    <li class="${activeCid == p.cid ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/category?cid=${p.cid}">${p.cname}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</nav>
