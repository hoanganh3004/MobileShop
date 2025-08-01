<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Google font -->
<link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css"/>
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/slick.css"/>
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/slick-theme.css"/>
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/nouislider.min.css"/>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/font-awesome.min.css">
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"/>

<title>MobileShop - Lịch sử mua hàng</title>

<jsp:include page="header.jsp" />

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
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${orders}">
                <tr>
                    <td>#${o.id}</td>
                    <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                    <td><fmt:formatNumber value="${o.total}" type="number" maxFractionDigits="2"/> $</td>
                    <td>${o.status}</td>
                    <td>
                        <button class="btn btn-info btn-sm mb-1" onclick="openOrderDetail(${o.id})">Xem chi tiết</button>
                        <c:if test="${o.status == 'Chờ xác nhận'}">
                            <button class="btn btn-danger btn-sm" onclick="openCancelModal(${o.id})">Hủy đơn</button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

<!-- Modal hiển thị chi tiết -->
<div class="modal fade" id="orderDetailModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content" id="orderDetailContent">
            <!-- Nội dung AJAX load -->
        </div>
    </div>
</div>

<!-- Modal nhập lý do hủy -->
<div class="modal fade" id="cancelOrderModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content p-3">
            <div class="modal-header">
                <h5 class="modal-title">Hủy đơn hàng</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <p>Vui lòng nhập lý do hủy đơn hàng:</p>
                <textarea id="cancelReasonInput" class="form-control" rows="3"></textarea>
                <input type="hidden" id="cancelOrderId" />
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" onclick="submitCancel()">Xác nhận hủy</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" ></jsp:include>

<!-- Script -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/slick.min.js"></script>
<script src="js/nouislider.min.js"></script>
<script src="js/jquery.zoom.min.js"></script>
<script src="js/main.js"></script>

<script>
    // Mở modal chi tiết
    function openOrderDetail(orderId) {
        $.ajax({
            url: 'order-detail',
            type: 'GET',
            data: { id: orderId },
            success: function (data) {
                $('#orderDetailContent').html(data);
                $('#orderDetailModal').modal('show');
            },
            error: function () {
                alert("Không thể tải chi tiết đơn hàng.");
            }
        });
    }

    // Mở modal nhập lý do hủy
    function openCancelModal(orderId) {
        $('#cancelOrderId').val(orderId);
        $('#cancelReasonInput').val('');
        $('#cancelOrderModal').modal('show');
    }

    // Gửi AJAX hủy đơn
    function submitCancel() {
        var orderId = $('#cancelOrderId').val();
        var reason = $('#cancelReasonInput').val().trim();

        if (reason === '') {
            alert("Vui lòng nhập lý do hủy đơn hàng.");
            return;
        }

        $.ajax({
            url: 'cancel-order',
            type: 'POST',
            data: {
                id: orderId,
                reason: reason
            },
            success: function () {
                alert("Đơn hàng đã được hủy.");
                $('#cancelOrderModal').modal('hide');
                location.reload();
            },
            error: function () {
                alert("Không thể hủy đơn hàng.");
            }
        });
    }

    // ✅ Tự động mở chi tiết nếu có ?orderId=
    $(document).ready(function () {
        const params = new URLSearchParams(window.location.search);
        const orderId = params.get("orderId");
        if (orderId) {
            openOrderDetail(orderId);
        }
    });
</script>
