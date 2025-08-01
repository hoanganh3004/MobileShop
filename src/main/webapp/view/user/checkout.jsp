<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="activeCid" value="" />
<c:set var="listC" value="${sessionScope.listC}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>MobileShop-Giỏ hàng</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"/>
</head>
<body>

<jsp:include page="header.jsp"/>

<div id="breadcrumb" class="section">
    <div class="container">
        <h3 class="breadcrumb-header">Checkout</h3>
        <ul class="breadcrumb-tree">
            <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="active">Checkout</li>
        </ul>
    </div>
</div>

<div class="section">
    <div class="container">
        <c:if test="${not empty outOfStockMessages}">
            <div class="alert alert-danger">
                <ul class="mb-0">
                    <c:forEach var="msg" items="${outOfStockMessages}">
                        <li>${msg}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form action="checkout" method="post">
            <div class="row">
                <!-- Thông tin người đặt -->
                <div class="col-md-7">
                    <div class="billing-details">
                        <h3 class="title">Thông tin người đặt</h3>
                        <input class="input form-group" type="text" name="fullName" value="${sessionScope.acc.fullName}" readonly>
                        <input class="input form-group" type="email" name="email" value="${sessionScope.acc.email}" readonly>
                        <input class="input form-group" type="tel" name="phone" value="${sessionScope.acc.phone}" readonly>
                        <input class="input form-group" type="text" name="address" value="${sessionScope.acc.address}" readonly>
                    </div>

                    <div class="shiping-details">
                        <h3 class="title">Đặt hàng hộ người khác</h3>
                        <div class="input-checkbox">
                            <input type="checkbox" id="shiping-address" name="isGuestOrder">
                            <label for="shiping-address">
                                <span></span> Tick nếu bạn đặt giúp người khác
                            </label>
                            <div class="caption">
                                <input class="input form-group" type="text" name="guestFullName" placeholder="Họ tên người nhận">
                                <input class="input form-group" type="email" name="guestEmail" placeholder="Email người nhận">
                                <input class="input form-group" type="tel" name="guestPhone" placeholder="Số điện thoại người nhận">
                                <input class="input form-group" type="text" name="guestAddress" placeholder="Địa chỉ người nhận">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Thông tin đơn hàng -->
                <div class="col-md-5 order-details">
                    <h3 class="title text-center">Đơn của bạn</h3>
                    <div class="order-summary">
                        <div class="order-col">
                            <div><strong>Sản phẩm</strong></div>
                            <div><strong>Tổng tiền</strong></div>
                        </div>

                        <div class="order-products">
                            <c:forEach var="item" items="${sessionScope.cartList}">
                                <div class="order-col">
                                    <div style="display: flex; align-items: center;">
                                        <img src="${pageContext.request.contextPath}/image?file=${fn:substringAfter(item.product.image, 'image\\')}"
                                             alt="${item.product.name}" width="40" height="40"
                                             style="margin-right: 8px; border-radius: 4px;">
                                        <div style="display: inline-flex; align-items: center;">
                                            <button type="button" class="btn-decrease" data-id="${item.product.id}" style="border:none;background:#eee;padding:4px 8px;">-</button>
                                            <span style="margin: 0 8px;">${item.quantity}</span>
                                            <button type="button" class="btn-increase" data-id="${item.product.id}" style="border:none;background:#eee;padding:4px 8px;">+</button>
                                            <span style="margin-left: 8px;">${item.product.name}</span>
                                        </div>
                                    </div>
                                    <div style="font-size: 14px;">
                                            ${item.quantity} x $<fmt:formatNumber value="${item.product.price}" type="number" minFractionDigits="2"/>
                                        = <strong>$<fmt:formatNumber value="${item.quantity * item.product.price}" type="number" minFractionDigits="2"/></strong>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="order-col">
                            <div>Shipping</div>
                            <div><strong>FREE</strong></div>
                        </div>

                        <div class="order-col">
                            <div><strong>Số tiền phải thanh toán</strong></div>
                            <div><strong class="order-total">$<fmt:formatNumber value="${sessionScope.cartTotal}" type="number" minFractionDigits="2"/></strong></div>
                        </div>
                    </div>

                    <c:if test="${not empty sessionScope.cartList && empty outOfStockMessages}">
                        <div class="payment-method text-center mt-3">
                            <button type="submit" class="primary-btn order-submit">Đặt hàng</button>
                        </div>
                    </c:if>
                </div>
            </div>
        </form>
    </div>
</div>

<jsp:include page="footer.jsp" ></jsp:include>

<!-- Script -->
<script src="<%= request.getContextPath() %>/js/jquery.min.js"></script>
<script src="<%= request.getContextPath() %>/js/bootstrap.min.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const checkbox = document.getElementById("shiping-address");
        const guestInputs = document.querySelectorAll('.caption input');
        function toggleRequired() {
            guestInputs.forEach(input => input.required = checkbox.checked);
        }
        checkbox.addEventListener("change", toggleRequired);
        toggleRequired();

        document.querySelectorAll(".btn-increase, .btn-decrease").forEach(btn => {
            btn.addEventListener("click", function () {
                const productId = this.dataset.id;
                const operation = this.classList.contains("btn-increase") ? "increase" : "decrease";

                fetch("checkout", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: new URLSearchParams({
                        action: "updateQuantity",
                        productId: productId,
                        operation: operation
                    })
                }).then(() => window.location.reload());
            });
        });
    });
</script>

</body>
</html>
