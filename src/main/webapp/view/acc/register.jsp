<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Đăng ký tài khoản</title>

    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,700" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/sb-admin-2.min.css" rel="stylesheet">

    <style>
        .bg-gradient-primary {
            background: linear-gradient(180deg, #4e73df 10%, #224abe 100%);
            min-height: 100vh;
        }
        .bg-register-image {
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
        .invalid-feedback {
            font-size: 0.875rem;
            color: #e74a3b;
            margin-top: 0.25rem;
        }
    </style>
</head>

<body class="bg-gradient-primary">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-xl-10 col-lg-12 col-md-9">
            <div class="card o-hidden border-0 shadow-lg my-5">
                <div class="card-body p-0 position-relative">
                    <a class="close-btn" href="<%= request.getContextPath() %>/home">×</a>
                    <div class="row">
                        <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>

                        <div class="col-lg-7">
                            <div class="p-5">
                                <div class="text-center">
                                    <h1 class="h4 text-gray-900 mb-4">Tạo tài khoản mới!</h1>
                                </div>

                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger" role="alert">
                                            ${error}
                                    </div>
                                </c:if>

                                <form class="user" action="${pageContext.request.contextPath}/register" method="post">
                                    <div class="form-group row">
                                        <div class="col-sm-6 mb-3 mb-sm-0">
                                            <input type="text"
                                                   class="form-control form-control-user"
                                                   name="fullName"
                                                   placeholder="Họ và tên"
                                                   value="${param.fullName}" required>
                                        </div>
                                        <div class="col-sm-6">
                                            <input type="text"
                                                   class="form-control form-control-user"
                                                   name="userName"
                                                   placeholder="Tên tài khoản"
                                                   pattern="^[a-zA-Z0-9_]{4,}$"
                                                   title="Tên tài khoản không hợp lệ (chỉ gồm chữ, số, gạch dưới, từ 4 ký tự)"
                                                   value="${param.userName}"
                                                   required>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <input type="email"
                                               class="form-control form-control-user"
                                               name="email"
                                               placeholder="Email"
                                               value="${param.email}" required>
                                    </div>
                                    <div class="form-group">
                                    <input type="tel" class="form-control form-control-user"
                                           name="phone"
                                           placeholder="Số điện thoại"
                                           value="${param.phone}"
                                           pattern="0[0-9]{9,14}"
                                           title="Số điện thoại phải bắt đầu bằng 0 và có từ 10 đến 15 chữ số"
                                           required>
                                    </div>
                                    <div class="form-group">
                                        <input type="text" class="form-control form-control-user"
                                               name="address"
                                               placeholder="Địa chỉ"
                                               value="${param.address}" required>
                                    </div>

                                    <div class="form-group row">
                                        <div class="col-sm-6 mb-3 mb-sm-0">
                                            <input type="password" class="form-control form-control-user"
                                                   name="password" placeholder="Mật khẩu" required>
                                        </div>
                                        <div class="col-sm-6">
                                            <input type="password" class="form-control form-control-user"
                                                   name="confirmPassword" placeholder="Nhập lại mật khẩu" required>
                                        </div>
                                    </div>

                                    <c:if test="${not empty passwordError}">
                                        <div class="invalid-feedback d-block">
                                                ${passwordError}
                                        </div>
                                    </c:if>

                                    <button type="submit" class="btn btn-primary btn-user btn-block">
                                        Đăng ký tài khoản
                                    </button>

                                    <hr>
                                </form>
                                <div class="text-center">
                                    <a class="small" href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
                                </div>
                                <div class="text-center">
                                    <a class="small" href="${pageContext.request.contextPath}/login">Bạn đã có tài khoản? Đăng nhập!</a>
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
<script src="<%= request.getContextPath() %>/js/sb-admin-2.min.js"></script>

</body>
</html>
