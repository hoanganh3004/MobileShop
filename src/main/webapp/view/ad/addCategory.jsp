<%@ page contentType="text/html;charset=UTF-8" %>

<div class="modal-header bg-gradient-success text-white">
  <h5 class="modal-title">➕ Thêm danh mục</h5>
  <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
</div>

<div class="modal-body px-4 pt-3 pb-1">
  <form action="add-category" method="post">
    <div class="form-group">
      <label class="font-weight-bold">📂 Tên danh mục</label>
      <input type="text" name="name" class="form-control rounded-pill px-4" required />
    </div>
    <div class="form-group">
      <label class="font-weight-bold">📝 Mô tả</label>
      <textarea name="description" class="form-control rounded px-3" rows="3"></textarea>
    </div>
    <div class="modal-footer border-0 px-0 pt-3">
      <button type="submit" class="btn btn-success rounded-pill px-4">
        <i class="fas fa-plus-circle"></i> Thêm mới
      </button>
      <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
        <i class="fas fa-times-circle"></i> Hủy
      </button>
    </div>
  </form>
</div>
