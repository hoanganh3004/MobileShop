<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="add-product" method="post" enctype="multipart/form-data">
    <div class="modal-header bg-gradient-success text-white">
        <h5 class="modal-title">➕ Thêm sản phẩm mới</h5>
        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">×</span>
        </button>
    </div>

    <div class="modal-body px-4 pt-3 pb-1">
        <!-- Mã sản phẩm -->
        <div class="form-group">
            <label for="masp" class="font-weight-bold">🔢 Mã sản phẩm</label>
            <input type="text" id="masp" name="masp"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập mã sản phẩm..." required>
        </div>

        <!-- Tên sản phẩm -->
        <div class="form-group">
            <label for="name" class="font-weight-bold">📱 Tên sản phẩm</label>
            <input type="text" id="name" name="name"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập tên sản phẩm..." required>
        </div>

        <!-- Giá -->
        <div class="form-group">
            <label for="price" class="font-weight-bold">💲 Giá</label>
            <input type="number" id="price" name="price" min="0"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập giá..." required>
        </div>

        <!-- Số lượng -->
        <div class="form-group">
            <label for="quantity" class="font-weight-bold">📦 Số lượng</label>
            <input type="number" id="quantity" name="quantity" min="1"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập số lượng..." required>
        </div>

        <!-- Mô tả -->
        <div class="form-group">
            <label for="description" class="font-weight-bold">📝 Mô tả</label>
            <textarea id="description" name="description"
                      class="form-control shadow-sm rounded px-3"
                      placeholder="Nhập mô tả..." rows="3"></textarea>
        </div>

        <!-- Danh mục (loại hàng) -->
        <div class="form-group">
            <label for="category_id" class="font-weight-bold">📂 Danh mục</label>
            <select id="category_id" name="category_id" class="form-control shadow-sm rounded-pill px-4" required>
                <option value="">-- Chọn danh mục --</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.cid}">${category.cname}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Hình ảnh -->
        <div class="form-group">
            <label for="image" class="font-weight-bold">🖼️ Hình ảnh</label>
            <input type="file" id="image" name="image"
                   class="form-control-file" required>
        </div>

        <!-- Nút -->
        <div class="modal-footer border-0 px-0 pt-3">
            <button type="submit" class="btn btn-success rounded-pill px-4">
                <i class="fas fa-plus-circle"></i> Thêm mới
            </button>
            <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
                <i class="fas fa-times-circle"></i> Hủy
            </button>
        </div>
    </div>
</form>