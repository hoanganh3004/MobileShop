<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="edit-account" method="post">
    <div class="modal-header bg-gradient-primary text-white">
        <h5 class="modal-title">✏️ Chỉnh sửa tài khoản</h5>
        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>

    <div class="modal-body px-4 pt-3 pb-1">
        <input type="hidden" name="id" value="${acc.id}" />

        <!-- Họ tên -->
        <div class="form-group">
            <label for="fullName" class="font-weight-bold">👤 Họ tên</label>
            <input type="text" id="fullName" name="fullName"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${acc.fullName}" placeholder="Nhập họ tên..." required />
        </div>

        <!-- Email -->
        <div class="form-group">
            <label for="email" class="font-weight-bold">📧 Email</label>
            <input type="email" id="email" name="email"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${acc.email}" placeholder="Nhập email..." required />
        </div>

        <!-- Số điện thoại -->
        <div class="form-group">
            <label for="phone" class="font-weight-bold">📱 Số điện thoại</label>
            <input type="text" id="phone" name="phone"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${acc.phone}" placeholder="Nhập số điện thoại..." />
        </div>

        <!-- Địa chỉ -->
        <div class="form-group">
            <label for="address" class="font-weight-bold">🏠 Địa chỉ</label>
            <textarea id="address" name="address"
                      class="form-control shadow-sm rounded px-3"
                      placeholder="Nhập địa chỉ..." rows="2">${acc.address}</textarea>
        </div>
    </div>

    <!-- Nút -->
    <div class="modal-footer border-0 px-4 pb-3">
        <button type="submit" class="btn btn-primary rounded-pill px-4">
            <i class="fas fa-save"></i> Cập nhật
        </button>
        <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
            <i class="fas fa-times-circle"></i> Hủy
        </button>
    </div>
</form>
