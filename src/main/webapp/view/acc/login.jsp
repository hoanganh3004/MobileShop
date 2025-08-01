<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Đăng nhập</title>

    <!-- Bootstrap -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,700" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">

    <style>
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

        .card {
            position: relative;
            overflow: visible;
        }

        .bg-gradient-primary {
            background: linear-gradient(180deg, #4e73df 10%, #224abe 100%);
            min-height: 100vh;
        }

        .bg-login-image {
            background-image: url('${pageContext.request.contextPath}/img/login.png');
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center;
            background-color: #f8f9fc;
        }
    </style>
</head>

<body class="bg-gradient-primary">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-xl-10 col-lg-12 col-md-9">
            <div class="card o-hidden border-0 shadow-lg my-5">
                <div class="card-body p-0 position-relative">
                    <a class="close-btn" onclick="window.location.href='${pageContext.request.contextPath}/home'">×</a>
                    <div class="row">
                        <!-- ẢNH MINH HỌA -->
                        <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>

                        <!-- FORM ĐĂNG NHẬP -->
                        <div class="col-lg-6">
                            <div class="p-5">
                                <div class="text-center">
                                    <h1 class="h4 text-gray-900 mb-4">Chào mừng bạn đến với bình nguyên vô tận!</h1>
                                </div>

                                <!-- ✨ HIỂN THỊ LỖI -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger text-center">
                                            ${error}
                                    </div>
                                </c:if>

                                <form class="user" action="${pageContext.request.contextPath}/login" method="post">
                                    <div class="form-group">
                                        <input type="text" class="form-control form-control-user"
                                               name="username"
                                               placeholder="Nhập tài khoản ..." required>
                                    </div>
                                    <div class="form-group">
                                        <input type="password" class="form-control form-control-user"
                                               name="password"
                                               placeholder="Mật khẩu nè" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary btn-user btn-block">
                                        Đăng nhập
                                    </button>
                                    <hr>

                                </form>
                                <div class="text-center">
                                    <a class="small" href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
                                </div>
                                <div class="text-center">
                                    <a class="small" href="${pageContext.request.contextPath}/register">Tạo tài khoản mới!</a>
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

<!-- JS Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
</body>
</html>
