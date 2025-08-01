<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>MobileShop - Thông tin cá nhân</title>

    <!-- Fonts & CSS -->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/slick.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/slick-theme.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/nouislider.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

<!-- SECTION -->
<div class="section">
    <div class="container">
        <div class="row">
            <div class="col-md-7">
                <div class="billing-details">
                    <div class="section-title">
                        <h3 class="title">THÔNG TIN CÁ NHÂN</h3>
                    </div>

                    <!-- Thông báo -->
                    <c:if test="${not empty msg}">
                        <div class="alert alert-success">${msg}</div>
                    </c:if>
                    <c:if test="${not empty err}">
                        <div class="alert alert-danger">${err}</div>
                    </c:if>

                    <!-- Form cập nhật -->
                    <form action="${pageContext.request.contextPath}/account" method="post">
                        <input type="hidden" name="action" value="updateInfo"/>
                        <div class="form-group">
                            <label>HỌ TÊN</label>
                            <input class="input" type="text" name="full_name" value="${acc.fullName}" placeholder="Họ và tên" required>
                        </div>
                        <div class="form-group">
                            <label>GMAIL</label>
                            <input class="input" type="email" name="email" value="${acc.email}" placeholder="Email" required>
                        </div>
                        <div class="form-group">
                            <label>ĐỊA CHỈ</label>
                            <input class="input" type="text" name="address" value="${acc.address}" placeholder="Địa chỉ">
                        </div>
                        <div class="form-group">
                            <label>SỐ ĐIỆN THOẠI</label>
                            <input class="input" type="tel" name="phone" value="${acc.phone}" placeholder="Số điện thoại">
                        </div>

                        <!-- Checkbox đổi mật khẩu -->
                        <div class="form-group">
                            <input type="checkbox" id="changePassCheckbox">
                            <label for="changePassCheckbox">Bạn muốn đổi mật khẩu</label>
                        </div>

                        <!-- Form đổi mật khẩu ẩn/hiện -->
                        <div id="changePassForm" style="display: none;">
                            <div class="form-group">
                                <label>MẬT KHẨU MỚI</label>
                                <input class="input" type="password" name="password" placeholder="Mật khẩu mới">
                            </div>
                            <div class="form-group">
                                <label>NHẬP LẠI MẬT KHẨU</label>
                                <input class="input" type="password" name="confirmPassword" placeholder="Nhập lại mật khẩu">
                            </div>
                        </div>

                        <button type="submit" class="primary-btn order-submit">LƯU THAY ĐỔI</button>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>
<!-- /SECTION -->

<jsp:include page="footer.jsp" ></jsp:include>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/slick.min.js"></script>
<script src="${pageContext.request.contextPath}/js/nouislider.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.zoom.min.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>

<!-- Script xử lý ẩn/hiện form đổi mật khẩu -->
<script>
    $(document).ready(function () {
        $('#changePassCheckbox').change(function () {
            if ($(this).is(':checked')) {
                $('#changePassForm').slideDown();
            } else {
                $('#changePassForm').slideUp();
                $('#changePassForm input').val('');
            }
        });
    });
</script>

</body>
</html>
