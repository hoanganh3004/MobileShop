<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="change-password" method="post">
<div class="modal-header bg-gradient-primary text-white">
    <h5 class="modal-title">Đổi mật khẩu cho ${acc.fullName}</h5>
    <button type="button" class="close text-white" data-dismiss="modal">
        <span>&times;</span>
    </button>
</div>

<div class="modal-body px-4 pt-3 pb-1">
        <input type="hidden" name="id" value="${acc.id}" />

        <div class="form-group">
            <label for="newPassword" class="font-weight-bold">🔒 Mật khẩu mới</label>
            <input type="password" name="newPassword" id="newPassword"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập mật khẩu mới..." required>
        </div>

        <div class="form-group">
            <label for="confirmPassword" class="font-weight-bold">🔒 Xác nhận mật khẩu</label>
            <input type="password" name="confirmPassword" id="confirmPassword"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập lại mật khẩu..." required>
        </div>

        <div class="modal-footer border-0 px-0 pt-3">
            <button type="submit" class="btn btn-success rounded-pill px-4">
                <i class="fas fa-check-circle"></i> Cập nhật
            </button>
            <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
                <i class="fas fa-times-circle"></i> Hủy
            </button>
        </div>
</div>
</form>