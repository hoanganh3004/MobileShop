<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="admin-header.jsp" %>

<h1 class="h3 mb-4 text-gray-800">🧾 Quản lý đơn hàng</h1>

<!-- Form tìm kiếm -->
<form action="admin-order" method="get" class="form-inline mb-3">
  <input type="text" name="customer" class="form-control rounded-pill mr-2 shadow-sm" placeholder="Tên khách hàng..." value="${param.customer}">
  <input type="date" name="orderDate" class="form-control rounded-pill mr-2 shadow-sm" value="${param.orderDate}">
  <button type="submit" class="btn btn-primary rounded-pill px-4"><i class="fas fa-search"></i> Tìm kiếm</button>
  <button type="button" class="btn btn-success ml-2 rounded-pill px-4" onclick="openCreateOrderModal()"><i class="fas fa-plus"></i> Tạo đơn hàng</button>
</form>

<!-- Danh sách đơn hàng -->
<div class="table-responsive">
  <table class="table table-bordered shadow-sm">
    <thead class="thead-light">
    <tr>
      <th>ID</th>
      <th>Khách hàng</th>
      <th>Ngày đặt</th>
      <th>Tổng tiền</th>
      <th>Trạng thái</th>
      <th>Thao tác</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="o" items="${orderList}">
      <tr>
        <td>${o.id}</td>
        <td>${o.customer.fullName}</td>
        <td>${o.orderDate}</td>
        <td><fmt:formatNumber value="${o.total}" type="number" maxFractionDigits="0"/> $</td>
        <td>
          <form action="${pageContext.request.contextPath}/updateOrderStatus" method="post" class="form-inline">
            <input type="hidden" name="orderId" value="${o.id}" />
            <select name="status" class="form-control form-control-sm mr-2 rounded-pill" onchange="toggleReason(this, ${o.id})">
              <option value="Chờ xác nhận" ${o.status == 'Chờ xác nhận' ? 'selected' : ''}>Chờ xác nhận</option>
              <option value="Đang giao" ${o.status == 'Đang giao' ? 'selected' : ''}>Đang giao</option>
              <option value="Hoàn thành" ${o.status == 'Hoàn thành' ? 'selected' : ''}>Hoàn thành</option>
              <option value="Đã hủy" ${o.status == 'Đã hủy' ? 'selected' : ''}>Đã hủy</option>
            </select>
            <input type="text" name="cancelReason" id="cancelReason-${o.id}" class="form-control form-control-sm mr-2 rounded-pill"
                   style="display: ${o.status == 'Đã hủy' ? 'inline-block' : 'none'};" placeholder="Nhập lý do hủy..." />
            <button type="submit" class="btn btn-sm btn-primary rounded-pill">Cập nhật</button>
          </form>
        </td>
        <td>
          <button type="button" class="btn btn-sm btn-info mb-1 rounded-pill" onclick="openOrderDetail(${o.id})">
            <i class="fas fa-eye"></i> Chi tiết
          </button>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<!-- Modal xem chi tiết đơn hàng -->
<div class="modal fade" id="orderDetailModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content" id="orderDetailContent">
      <!-- Nội dung AJAX sẽ load vào đây -->
    </div>
  </div>
</div>

<!-- Modal tạo đơn hàng -->
<div class="modal fade" id="createOrderModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content" id="createOrderContent">
      <!-- Nội dung AJAX sẽ load vào đây -->
    </div>
  </div>
</div>

<!-- jQuery + Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>

<!-- JS xử lý -->
<script>
  function toggleReason(selectElem, orderId) {
    var input = document.getElementById('cancelReason-' + orderId);
    input.style.display = (selectElem.value === 'Đã hủy') ? 'inline-block' : 'none';
  }

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
        alert('Không thể tải chi tiết đơn hàng.');
      }
    });
  }

  function openCreateOrderModal(page = 1) {
    $.ajax({
      url: 'create-order?page=' + page,
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
      success: function (data) {
        $('#createOrderContent').html(data);
        $('#createOrderModal').modal('show');
        attachCreateOrderEvents();
      },
      error: function () {
        alert('Không thể tải form tạo đơn hàng.');
      }
    });
  }

  function loadModalPage(url) {
    $.ajax({
      url: url,
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
      success: function (data) {
        $('#createOrderContent').html(data);
        attachCreateOrderEvents();
      },
      error: function () {
        alert('Không thể tải trang sản phẩm.');
      }
    });
  }

  function attachCreateOrderEvents() {
    $('#createOrderContent').find('.load-modal-page').off('click').on('click', function (e) {
      e.preventDefault();
      const url = $(this).attr('href');
      loadModalPage(url);
    });

    $('#createOrderContent').find('#searchProduct').off('keyup').on('keyup', function () {
      const keyword = $(this).val().toLowerCase();
      $('#createOrderContent').find('#productTable tbody tr').each(function () {
        const name = $(this).find('.product-name').text().toLowerCase();
        $(this).toggle(name.includes(keyword));
      });
    });

    $('#createOrderContent').find('.goto-button').off('click').on('click', function () {
      const page = $('#createOrderContent').find('#gotoPage').val();
      const max = parseInt($('#createOrderContent').find('#gotoPage').attr('max'));
      if (page >= 1 && page <= max) {
        loadModalPage('create-order?page=' + page);
      } else {
        alert("Vui lòng nhập số từ 1 đến " + max);
      }
    });
  }
</script>
