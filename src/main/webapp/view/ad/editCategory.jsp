<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.library.MoileShop.entity.Category" %>
<%
    Category c = (Category) request.getAttribute("category");
%>
<form action="edit-category" method="post">
<div class="modal-header bg-gradient-warning text-white">
    <h5 class="modal-title">✏️ Sửa danh mục</h5>
    <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
</div>

<div class="modal-body px-4 pt-3 pb-1">
        <input type="hidden" name="id" value="<%= c.getCid() %>" />
        <div class="form-group">
            <label class="font-weight-bold">📂 Tên danh mục</label>
            <input type="text" name="name" class="form-control rounded-pill px-4" value="<%= c.getCname() %>" required />
        </div>
        <div class="form-group">
            <label class="font-weight-bold">📝 Mô tả</label>
            <textarea name="description" class="form-control rounded px-3" rows="3"><%= c.getDescription() %></textarea>
        </div>
        <div class="modal-footer border-0 px-0 pt-3">
            <button type="submit" class="btn btn-warning rounded-pill px-4">
                <i class="fas fa-save"></i> Cập nhật
            </button>
            <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
                <i class="fas fa-times-circle"></i> Hủy
            </button>
        </div>
</div>
</form>