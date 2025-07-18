<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quên mật khẩu</title>

    <!-- Bootstrap -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link href="<%= request.getContextPath() %>/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,700" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="<%= request.getContextPath() %>/css/sb-admin-2.min.css" rel="stylesheet">

    <style>
        .bg-gradient-primary {
            background: linear-gradient(180deg, #4e73df 10%, #224abe 100%);
            min-height: 100vh;
        }

        .bg-password-image {
            background-image: url('<%= request.getContextPath() %>/img/login.png');
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center;
            background-color: #f8f9fc;
        }

        .card {
            position: relative;
            overflow: visible;
        }

        .close-btn {
            position: absolute;
            top: 15px;
            right: 15px;
            font-size: 1.5rem;
            color: #dc3545;
            cursor: pointer;
            z-index: 1000;
            text-decoration: none;
            opacity: 0;
            transition: opacity 0.3s, color 0.3s;
        }

        .card:hover .close-btn {
            opacity: 1;
        }

        .close-btn:hover {
            color: #a71d2a;
        }
    </style>
</head>

<body class="bg-gradient-primary">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-xl-10 col-lg-12 col-md-9">
            <div class="card o-hidden border-0 shadow-lg my-5">
                <div class="card-body p-0 position-relative">
                    <a class="close-btn" onclick="window.location.href='<%= request.getContextPath() %>/home'">×</a>
                    <div class="row">
                        <!-- ẢNH MINH HỌA -->
                        <div class="col-lg-6 d-none d-lg-block bg-password-image"></div>

                        <!-- FORM QUÊN MẬT KHẨU -->
                        <div class="col-lg-6">
                            <div class="p-5">
                                <div class="text-center">
                                    <h1 class="h4 text-gray-900 mb-2">Quên mật khẩu?</h1>
                                    <p class="mb-4">Nhập email và chúng tôi sẽ gửi liên kết để đặt lại mật khẩu.</p>
                                </div>

                                <!-- HIỂN THỊ LỖI -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger">${error}</div>
                                </c:if>

                                <form class="user" action="${pageContext.request.contextPath}/forgot-password" method="post">
                                    <div class="form-group">
                                        <input type="email" class="form-control form-control-user"
                                               name="email"
                                               placeholder="Nhập địa chỉ email..." required>
                                    </div>
                                    <button type="submit" class="btn btn-primary btn-user btn-block">
                                        Gửi liên kết đặt lại mật khẩu
                                    </button>
                                </form>
                                <hr>
                                <div class="text-center">
                                    <a class="small" href="${pageContext.request.contextPath}/register">Tạo tài khoản!</a>
                                </div>
                                <div class="text-center">
                                    <a class="small" href="${pageContext.request.contextPath}/login">Đã có tài khoản? Đăng nhập!</a>
                                </div>
                            </div>
                        </div>
                        <!-- /FORM -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JS -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/sb-admin-2.min.js"></script>

</body>
</html>
