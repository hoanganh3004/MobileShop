<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-header bg-gradient-primary text-white">
  <h5 class="modal-title">📢 Tạo thông báo mới</h5>
  <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
</div>

<div class="modal-body px-4 pt-3 pb-1">
  <c:if test="${not empty error}">
    <div class="alert alert-danger rounded-sm px-3 py-2 mb-3" role="alert">
      <i class="fas fa-exclamation-circle mr-1"></i> ${error}
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/create-notification" method="post">
    <div class="form-group">
      <label class="font-weight-bold">📝 Nội dung thông báo</label>
      <textarea name="message" class="form-control rounded px-3" rows="4" required>${param.message}</textarea>
    </div>

    <div class="form-group">
      <label class="font-weight-bold">👥 Người nhận</label>
      <select name="receiver" class="form-control rounded-pill px-4">
        <option value="all">📬 Tất cả người dùng</option>
        <c:if test="${not empty userList}">
          <c:forEach var="user" items="${userList}">
            <option value="${user.username}">${user.fullName} (${user.username})</option>
          </c:forEach>
        </c:if>
      </select>
    </div>

    <div class="modal-footer border-0 px-0 pt-3">
      <button type="submit" class="btn btn-primary rounded-pill px-4">
        <i class="fas fa-paper-plane"></i> Gửi
      </button>
      <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
        <i class="fas fa-times-circle"></i> Hủy
      </button>
    </div>
  </form>
</div>
