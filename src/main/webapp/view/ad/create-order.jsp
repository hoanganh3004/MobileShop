<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-header bg-success text-white">
    <h5 class="modal-title">➕ Tạo đơn hàng mới</h5>
    <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
</div>

<form action="create-order" method="post">
    <div class="modal-body">
        <!-- 👤 Chọn khách hàng -->
        <div class="form-group">
            <label>👤 Chọn khách hàng:</label>
            <select class="form-control" name="userId" required>
                <c:forEach var="u" items="${users}">
                    <option value="${u.id}">${u.fullName}</option>
                </c:forEach>
            </select>
        </div>

        <!-- 🔎 Tìm kiếm sản phẩm -->
        <div class="form-group">
            <label for="searchProduct">🔎 Tìm sản phẩm:</label>
            <input type="text" id="searchProduct" class="form-control" placeholder="Nhập tên sản phẩm...">
        </div>

        <!-- Danh sách sản phẩm -->
        <div class="form-group">
            <label>🛍️ Danh sách sản phẩm:</label>
            <div class="table-responsive">
                <table class="table table-bordered table-hover table-sm" id="productTable">
                    <thead class="thead-light">
                    <tr>
                        <th class="text-center">Chọn</th>
                        <th>Tên sản phẩm</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${products}">
                        <tr>
                            <td class="text-center">
                                <input type="checkbox" name="productIds" value="${p.id}" id="p${p.id}">
                            </td>
                            <td><label for="p${p.id}" class="product-name">${p.name}</label></td>
                            <td>${p.price} $</td>
                            <td>
                                <input type="number" name="quantities" class="form-control form-control-sm" min="1" value="1">
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- ✅ Phân trang -->
            <nav>
                <ul class="pagination justify-content-center mt-3">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link load-modal-page" href="create-order?page=${currentPage - 1}">«</a>
                        </li>
                    </c:if>

                    <li class="page-item ${currentPage == 1 ? 'active' : ''}">
                        <a class="page-link load-modal-page" href="create-order?page=1">1</a>
                    </li>

                    <c:if test="${currentPage > 4}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>

                    <c:set var="startPage" value="${currentPage - 2 < 2 ? 2 : currentPage - 2}" />
                    <c:set var="endPage" value="${currentPage + 2 >= totalPage ? totalPage - 1 : currentPage + 2}" />

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link load-modal-page" href="create-order?page=${i}">${i}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPage - 3}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>

                    <c:if test="${totalPage > 1}">
                        <li class="page-item ${currentPage == totalPage ? 'active' : ''}">
                            <a class="page-link load-modal-page" href="create-order?page=${totalPage}">${totalPage}</a>
                        </li>
                    </c:if>

                    <c:if test="${currentPage < totalPage}">
                        <li class="page-item">
                            <a class="page-link load-modal-page" href="create-order?page=${currentPage + 1}">»</a>
                        </li>
                    </c:if>
                </ul>

                <div class="d-flex justify-content-center align-items-center mt-2">
                    <label class="mr-2">Đến trang:</label>
                    <input type="number" id="gotoPage" min="1" max="${totalPage}" class="form-control form-control-sm mx-1" style="width: 80px;" placeholder="1-${totalPage}">
                    <button type="button" class="btn btn-secondary btn-sm goto-button">Đi</button>
                </div>
            </nav>
        </div>
    </div>

    <div class="modal-footer">
        <button type="submit" class="btn btn-success">Tạo đơn</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
    </div>
</form>
