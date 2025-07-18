  <%@ page contentType="text/html; charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

  <%@ include file="admin-header.jsp" %>

  <style>
    td.description-cell {
      max-width: 300px;
      max-height: 80px;
      overflow-y: auto;
      white-space: pre-line;
      word-break: break-word;
    }
  </style>

  <h1 class="h3 mb-4 text-gray-800">🛍️ Quản lý sản phẩm</h1>

  <!-- ✅ Thông báo -->
  <c:if test="${not empty sessionScope.msg}">
    <div class="alert alert-${sessionScope.msgType} alert-dismissible fade show" role="alert">
        ${sessionScope.msg}
      <button type="button" class="close" data-dismiss="alert">&times;</button>
    </div>
    <c:remove var="msg" scope="session"/>
    <c:remove var="msgType" scope="session"/>
  </c:if>

  <!-- Tìm kiếm sản phẩm -->
  <form action="admin-product" method="get" class="form-inline mb-3">
    <input type="text" name="keyword" class="form-control rounded-pill mr-2 shadow-sm" placeholder="Tìm theo mã, tên, mô tả" value="${keyword}">
    <button type="submit" class="btn btn-primary rounded-pill px-4"><i class="fas fa-search"></i> Tìm kiếm</button>

    <!-- ✅ Nút mở modal thêm sản phẩm -->
    <button type="button" class="btn btn-success ml-2 rounded-pill px-4" onclick="loadAddProductForm()">
      <i class="fas fa-plus"></i> Thêm sản phẩm
    </button>
  </form>

  <!-- Danh sách sản phẩm -->
  <div class="table-responsive">
    <table class="table table-bordered">
      <thead class="thead-light">
      <tr>
        <th>Mã SP</th>
        <th>Tên sản phẩm</th>
        <th>Mô tả</th>
        <th>Giá</th>
        <th>Hình ảnh</th>
        <th>Hành động</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="p" items="${productList}">
        <tr>
          <td>${p.masp}</td>
          <td>${p.name}</td>
          <td class="description-cell">${p.description}</td>
          <td><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>$</td>
          <td>
            <img src="${pageContext.request.contextPath}/image?file=${fn:substringAfter(p.image, 'image\\')}"
                 alt="${p.name}" width="60" height="60" />
          </td>
          <td>
            <!-- ✅ Nút chi tiết (sửa) -->
            <a href="javascript:void(0);" class="btn btn-sm btn-info mb-1"
               onclick="openEditProductModal(${p.id})"><i class="fas fa-edit"></i> Chi tiết</a>

            <!-- ✅ Nút xóa -->
            <a href="deleteProduct?id=${p.id}" class="btn btn-sm btn-danger mb-1"
               onclick="return confirm('Xóa sản phẩm này?');"><i class="fas fa-trash-alt"></i> Xóa</a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

  <!-- Phân trang -->
  <c:set var="start" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}" />
  <c:set var="end" value="${currentPage + 2 > totalPage ? totalPage : currentPage + 2}" />

  <nav>
    <ul class="pagination justify-content-center">
      <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
        <a class="page-link" href="admin-product?page=${currentPage - 1}&keyword=${keyword}">«</a>
      </li>

      <c:if test="${start > 1}">
        <li class="page-item"><a class="page-link" href="admin-product?page=1&keyword=${keyword}">1</a></li>
        <li class="page-item disabled"><span class="page-link">...</span></li>
      </c:if>

      <c:forEach begin="${start}" end="${end}" var="i">
        <li class="page-item ${i == currentPage ? 'active' : ''}">
          <a class="page-link" href="admin-product?page=${i}&keyword=${keyword}">${i}</a>
        </li>
      </c:forEach>

      <c:if test="${end < totalPage}">
        <li class="page-item disabled"><span class="page-link">...</span></li>
        <li class="page-item"><a class="page-link" href="admin-product?page=${totalPage}&keyword=${keyword}">${totalPage}</a></li>
      </c:if>

      <li class="page-item ${currentPage == totalPage ? 'disabled' : ''}">
        <a class="page-link" href="admin-product?page=${currentPage + 1}&keyword=${keyword}">»</a>
      </li>
    </ul>
  </nav>

  <!-- Đi tới trang -->
  <form action="admin-product" method="get" class="form-inline justify-content-center mt-2">
    <input type="hidden" name="keyword" value="${keyword}">
    <label class="mr-2">Đến trang:</label>
    <input type="number" name="page" class="form-control mr-2" placeholder="1-${totalPage}" min="1" style="width:100px;">
    <button type="submit" class="btn btn-secondary">Đi</button>
  </form>

  <!-- ✅ Modal thêm sản phẩm -->
  <div class="modal fade" id="addProductModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
      <div class="modal-content" id="addProductContent">
        <!-- Nội dung form sẽ được load bằng JS -->
      </div>
    </div>
  </div>

  <!-- ✅ Modal sửa sản phẩm -->
  <div class="modal fade" id="editProductModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
      <div class="modal-content" id="editProductContent">
        <!-- Nội dung form sẽ được load bằng JS -->
      </div>
    </div>
  </div>

  <!-- Scripts -->
  <script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

  <!-- ✅ Script AJAX load form thêm/sửa sản phẩm -->
  <script>
    function loadAddProductForm() {
      $.get("add-product", function(data) {
        const formHtml = $('<div>').html(data).find('form').parent().html();
        $('#addProductContent').html(formHtml);
        $('#addProductModal').modal('show');
      });
    }

    function openEditProductModal(productId) {
      $.get("edit-product?id=" + productId, function(data) {
        const formHtml = $('<div>').html(data).find('form').parent().html();
        $('#editProductContent').html(formHtml);
        $('#editProductModal').modal('show');
      });
    }
  </script>

  </body>
  </html>
