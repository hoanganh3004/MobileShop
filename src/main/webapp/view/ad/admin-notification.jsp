<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="admin-header.jsp" %>

<h1 class="h3 mb-4 text-gray-800">📢 Quản lý thông báo</h1>

<!-- ✅ Thông báo khi gửi thành công -->
<c:if test="${not empty sessionScope.msg}">
  <div class="alert alert-success alert-dismissible fade show" role="alert">
    <i class="fas fa-check-circle mr-2"></i> ${sessionScope.msg}
    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <c:remove var="msg" scope="session" />
</c:if>

<!-- Form tìm kiếm -->
<form action="admin-notification" method="get" class="form-inline mb-3">
  <input type="text" name="keyword" class="form-control rounded-pill mr-2 shadow-sm"
         placeholder="Tìm theo nội dung..." value="${param.keyword}">
  <button type="submit" class="btn btn-primary rounded-pill px-4">
    <i class="fas fa-search"></i> Tìm kiếm
  </button>
  <button type="button" class="btn btn-success ml-2 rounded-pill px-4" onclick="openCreateNotificationModal()">
    <i class="fas fa-plus"></i> Tạo thông báo
  </button>
</form>

<!-- Danh sách thông báo -->
<div class="table-responsive">
  <table class="table table-bordered">
    <thead class="thead-light">
    <tr>
      <th>ID</th>
      <th>Người nhận</th> <!-- Đổi từ 'User ID' thành 'Người nhận' -->
      <th>Nội dung</th>
      <th>Ngày tạo</th>
      <th>Đã đọc</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="n" items="${notificationList}">
      <tr>
        <td>${n.id}</td>
        <td>${n.fullName}</td> <!-- ✅ Hiển thị tên đầy đủ -->
        <td>${n.message}</td>
        <td>${n.createdAt}</td>
        <td>
          <c:choose>
            <c:when test="${n.read}">✔️</c:when>
            <c:otherwise>❌</c:otherwise>
          </c:choose>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<!-- Modal tạo thông báo -->
<div class="modal fade" id="createNotificationModal" tabindex="-1" role="dialog"
     aria-labelledby="createModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <!-- AJAX sẽ load nội dung vào đây -->
      <div class="modal-body" id="createNotificationContent"></div>
    </div>
  </div>
</div>

<!-- jQuery & Bootstrap JS -->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<script>
  const contextPath = '${pageContext.request.contextPath}/';

  function openCreateNotificationModal() {
    $.get(contextPath + "create-notification", function (data) {
      $('#createNotificationContent').html(data);
      $('#createNotificationModal').modal('show');
    }).fail(function () {
      alert('Không thể tải form tạo thông báo. Vui lòng thử lại.');
    });
  }
</script>
