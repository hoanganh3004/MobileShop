<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="admin-header.jsp" %>

<h1 class="h3 mb-4 text-gray-800">📦 Quản lý danh mục sản phẩm</h1>

<!-- Tìm kiếm -->
<form action="admin-category" method="get" class="form-inline mb-3">
  <input type="text" name="keyword" class="form-control rounded-pill mr-2 shadow-sm" placeholder="Tìm danh mục..." value="${param.keyword}">
  <button type="submit" class="btn btn-primary rounded-pill px-4"><i class="fas fa-search"></i> Tìm</button>
  <button type="button" class="btn btn-success ml-2 rounded-pill px-4" onclick="loadAddCategoryForm()">
    <i class="fas fa-plus"></i> Thêm danh mục
  </button>
</form>

<!-- Danh sách danh mục -->
<div class="table-responsive">
  <table class="table table-bordered">
    <thead class="thead-light">
    <tr>
      <th>Tên danh mục</th>
      <th>Mô tả</th>
      <th>Hành động</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${categoryList}">
      <tr>
        <td>${c.cname}</td>
        <td>${c.description}</td>
        <td>
          <button type="button" class="btn btn-sm btn-warning mb-1" onclick="loadEditCategoryForm(${c.cid})">
            <i class="fas fa-edit"></i> Sửa
          </button>
          <a href="delete-category?id=${c.cid}" class="btn btn-sm btn-danger mb-1"
             onclick="return confirm('Bạn có chắc muốn xóa?')">
            <i class="fas fa-trash"></i> Xóa
          </a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<!-- Modal Thêm -->
<div class="modal fade" id="addCategoryModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-dialog-centered modal-md" role="document">
    <div class="modal-content" id="addCategoryContent">

    </div>
  </div>
</div>

<!-- Modal Sửa -->
<div class="modal fade" id="editCategoryModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-dialog-centered modal-md" role="document">
    <div class="modal-content" id="editCategoryContent">
      <!-- AJAX sẽ chèn nội dung ở đây -->
    </div>
  </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<script>

  function loadAddCategoryForm() {
    $.get("add-category", function (data) {
      $('#addCategoryContent').html(data);
      $('#addCategoryModal').modal('show');
    });
  }

  function loadEditCategoryForm(id) {
    $.get("edit-category?id=" + id, function (data) {
      $('#editCategoryContent').html(data);
      $('#editCategoryModal').modal('show');
    });
  }
</script>
