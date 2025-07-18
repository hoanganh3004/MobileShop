<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="admin-header.jsp" %>

<h1 class="h3 mb-4 text-gray-800"> 👤 Quản lý tài khoản</h1>

<!-- ✅ Thông báo -->
<c:if test="${not empty sessionScope.msg}">
  <div class="alert alert-${sessionScope.msgType} alert-dismissible fade show" role="alert">
      ${sessionScope.msg}
    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <c:remove var="msg" scope="session"/>
  <c:remove var="msgType" scope="session"/>
</c:if>

<!-- ✅ Bảng tài khoản -->
<div class="table-responsive">
  <table class="table table-bordered">
    <thead class="thead-light">
    <tr>
      <th>Họ tên</th>
      <th>Tên đăng nhập</th>
      <th>Email</th>
      <th>Địa chỉ</th>
      <th>Vai trò</th>
      <th>Trạng thái</th>
      <th>Hành động</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="acc" items="${accountList}">
      <tr>
        <td>${acc.fullName}</td>
        <td>${acc.username}</td>
        <td>${acc.email}</td>
        <td>${acc.address}</td>
        <td>
          <form action="change-role" method="post" class="form-inline">
            <input type="hidden" name="id" value="${acc.id}" />
            <select name="role" class="form-control form-control-sm mr-2">
              <option value="user" ${acc.role == 'user' ? 'selected' : ''}>User</option>
              <option value="admin" ${acc.role == 'admin' ? 'selected' : ''}>Admin</option>
            </select>
            <button type="submit" class="btn btn-sm btn-primary">Lưu</button>
          </form>
        </td>
        <td>
          <span class="badge badge-${acc.status == 1 ? 'success' : 'secondary'}">
              ${acc.status == 1 ? 'Hoạt động' : 'Đã khóa'}
          </span>
        </td>
        <td>
          <button class="btn btn-sm btn-warning mb-1" onclick="loadEditForm(${acc.id})">
            <i class="fas fa-edit"></i> Sửa
          </button>
          <button type="button" class="btn btn-sm btn-info mb-1" onclick="loadPasswordForm(${acc.id})">
            <i class="fas fa-key"></i> Đổi mật khẩu
          </button>
          <form action="toggle-status" method="get" style="display:inline;">
            <input type="hidden" name="id" value="${acc.id}" />
            <button type="submit" class="btn btn-sm btn-${acc.status == 1 ? 'danger' : 'success'} mb-1">
              <i class="fas fa-lock"></i> ${acc.status == 1 ? 'Khóa' : 'Mở'}
            </button>
          </form>
          <form action="delete-account" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc muốn xóa?')">
            <input type="hidden" name="id" value="${acc.id}" />
            <button type="submit" class="btn btn-sm btn-danger mb-1">
              <i class="fas fa-trash"></i> Xóa
            </button>
          </form>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<!-- ✅ Modal sửa tài khoản -->
<div class="modal fade" id="editAccountModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content" id="editAccountContent">
      <!-- Nội dung sẽ được load bằng JS -->
    </div>
  </div>
</div>

<!-- ✅ Modal đổi mật khẩu -->
<div class="modal fade" id="changePasswordModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-md" role="document">
    <div class="modal-content" id="changePasswordContent">
      <!-- Nội dung sẽ được load bằng JS -->
    </div>
  </div>
</div>

<!-- ✅ Scripts -->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script>
  function loadEditForm(id) {
    $.get("edit-account?id=" + id, function(data) {
      const formHtml = $('<div>').html(data).find('form').parent().html();
      $('#editAccountContent').html(formHtml);
      $('#editAccountModal').modal('show');
    });
  }

  function loadPasswordForm(id) {
    $.get("change-password?id=" + id, function(data) {
      const formHtml = $('<div>').html(data).find('form').parent().html();
      $('#changePasswordContent').html(formHtml);
      $('#changePasswordModal').modal('show');
    });
  }
</script>

<!-- ✅ CSS -->
<style>
  td {
    vertical-align: middle;
  }
  td:nth-child(4) {
    max-width: 200px;
    word-wrap: break-word;
    white-space: normal;
  }
</style>

</div> <!-- end container-fluid -->
</div> <!-- end content -->
</div> <!-- end content-wrapper -->
</div> <!-- end wrapper -->
</body>
</html>
