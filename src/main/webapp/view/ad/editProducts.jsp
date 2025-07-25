<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form method="post" action="edit-product" enctype="multipart/form-data">
    <div class="modal-header bg-gradient-warning text-white">
        <h5 class="modal-title">✏️ Chỉnh sửa sản phẩm</h5>
        <button type="button" class="close text-white" data-dismiss="modal">×</button>
    </div>

    <div class="modal-body px-4 pt-3 pb-1">
        <input type="hidden" name="id" value="${product.id}" />
        <input type="hidden" name="oldImage" value="${product.image}" />
        <!-- Thêm trường ẩn cho categoryId -->
        <input type="hidden" name="category_id" value="${product.categoryId}" />

        <!-- Mã sản phẩm -->
        <div class="form-group">
            <label for="masp" class="font-weight-bold">🔢 Mã sản phẩm</label>
            <input type="text" id="masp" name="masp"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${product.masp}" required />
        </div>

        <!-- Tên sản phẩm -->
        <div class="form-group">
            <label for="name" class="font-weight-bold">📱 Tên sản phẩm</label>
            <input type="text" id="name" name="name"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${product.name}" required />
        </div>

        <!-- Giá -->
        <div class="form-group">
            <label for="price" class="font-weight-bold">💲 Giá</label>
            <input type="number" id="price" name="price" min="0" step="0.01"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${product.price}" required />
        </div>

        <!-- Số lượng -->
        <div class="form-group">
            <label for="quantity" class="font-weight-bold">📦 Số lượng</label>
            <input type="number" id="quantity" name="quantity" min="0"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${product.quantity}" required />
        </div>

        <!-- Mô tả -->
        <div class="form-group">
            <label for="description" class="font-weight-bold">📝 Mô tả</label>
            <textarea id="description" name="description"
                      class="form-control shadow-sm rounded px-3"
                      rows="3" placeholder="Mô tả sản phẩm...">${product.description}</textarea>
        </div>

        <!-- Ảnh hiện tại -->
        <div class="form-group">
            <label class="font-weight-bold">🖼️ Ảnh hiện tại</label><br>
            <c:choose>
                <c:when test="${not empty product.image}">
                    <img src="${pageContext.request.contextPath}/image?file=${fn:substringAfter(product.image, 'image\\')}"
                         alt="${product.name}" class="img-thumbnail mb-2" style="width: 200px; height: auto;">
                </c:when>
                <c:otherwise>
                    <p><i>Không có ảnh</i></p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Ảnh mới -->
        <div class="form-group">
            <label for="image" class="font-weight-bold">📤 Chọn ảnh mới (nếu muốn thay)</label>
            <input type="file" id="image" name="image"
                   class="form-control-file" />
            <small class="form-text text-muted">Bỏ trống nếu không thay đổi ảnh.</small>
        </div>

        <!-- Nút -->
        <div class="modal-footer border-0 px-0 pt-3">
            <button type="submit" class="btn btn-warning rounded-pill px-4 text-white">
                <i class="fas fa-save"></i> Lưu thay đổi
            </button>
            <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
                <i class="fas fa-times-circle"></i> Hủy
            </button>
        </div>
    </div>
</form>