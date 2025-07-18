<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!-- Google font -->
<link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">

<!-- Bootstrap -->
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css"/>

<!-- Slick -->
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/slick.css"/>
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/slick-theme.css"/>

<!-- nouislider -->
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/nouislider.min.css"/>

<!-- Font Awesome Icon -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/font-awesome.min.css">

<!-- Custom stlylesheet -->
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"/>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

<jsp:include page="header.jsp"></jsp:include>

<div class="container mt-5">
    <h2>Lịch sử đơn hàng</h2>
    <c:if test="${empty orders}">
        <p>Bạn chưa có đơn hàng nào.</p>
    </c:if>
    <c:if test="${not empty orders}">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Mã đơn</th>
                <th>Ngày đặt</th>
                <th>Tổng tiền</th>
                <th>Trạng thái</th>
                <th>Lý do hủy (nếu có)</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${orders}">
                <tr>
                    <td>#${o.id}</td>
                    <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                    <td>$<fmt:formatNumber value="${o.total}" type="number" maxFractionDigits="2"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${o.status == 'pending'}">Chờ xử lý</c:when>
                            <c:when test="${o.status == 'shipped'}">Đã giao</c:when>
                            <c:when test="${o.status == 'cancelled' || o.status == 'Đã hủy'}">Đã hủy</c:when>
                            <c:otherwise>${o.status}</c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:out value="${o.cancelReason}" /></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

<jsp:include page="footer.jsp"></jsp:include>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/slick.min.js"></script>
<script src="js/nouislider.min.js"></script>
<script src="js/jquery.zoom.min.js"></script>
<script src="js/main.js"></script>
