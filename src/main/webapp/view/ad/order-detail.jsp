<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="modal-header bg-info text-white">
    <h5 class="modal-title">🧾 Chi tiết đơn hàng </h5>
    <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
</div>

<div class="modal-body">
    <p><strong>👤 Khách hàng:</strong> ${order.customer.fullName}</p>

    <c:if test="${not empty order.recipientName}">
        <p><strong>👤 Người nhận:</strong> ${order.recipientName}</p>
        <p><strong>📞 Số điện thoại:</strong> ${order.recipientPhone}</p>
        <p><strong>📬 Địa chỉ:</strong> ${order.recipientAddress}</p>
    </c:if>

    <p><strong>📅 Ngày đặt:</strong> ${order.orderDate}</p>
    <p><strong>💵 Tổng tiền:</strong>
        <fmt:formatNumber value="${order.total}" type="number" maxFractionDigits="0"/> $
    </p>
    <p><strong>📦 Trạng thái:</strong> ${order.status}</p>
    <c:if test="${order.status == 'Đã hủy'}">
        <p><strong>❌ Lý do hủy:</strong> ${order.cancelReason}</p>
    </c:if>

    <hr>
    <h6>🛍️ Danh sách sản phẩm:</h6>
    <table class="table table-bordered">
        <thead class="thead-light">
        <tr>
            <th>Ảnh</th>
            <th>Tên</th>
            <th>Đơn giá</th>
            <th>Số lượng</th>
            <th>Thành tiền</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${items}">
            <tr>
                <td>
                    <img src="${pageContext.request.contextPath}/image?file=${fn:substringAfter(item.product.image, 'image\\')}"
                         alt="${item.product.name}" width="60" height="60" />
                </td>
                <td>${item.product.name}</td>
                <td><fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="0"/> $</td>
                <td>${item.quantity}</td>
                <td>
                    <fmt:formatNumber value="${item.unitPrice * item.quantity}" type="number" maxFractionDigits="0"/> $
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div class="modal-footer">
    <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
</div>
