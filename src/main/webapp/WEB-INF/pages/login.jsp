<!DOCTYPE html>
<html lang="en">
    <head>
        <jsp:include page="common/head.jsp" >
            <jsp:param name="title" value="Login" />
            <jsp:param name="hideDefaultTheme" value="true" />
        </jsp:include>
    </head>
    <body class="authMainBody">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-5 col-md-8 col-sm-12">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header">
                                        <h4 class="text-center font-weight-light my-4 text-uppercase">LOGIN | PATHWAY</h4>
                                    </div>
                                    <div class="card-body">
                                        <form class="auth-form" action="/api/auth/login" method="POST">
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="username">Username</label>
                                                <input class="form-control" id="username" name="username" type="text" autocomplete="username" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="password">Password</label>
                                                <input class="form-control" id="password" name="password" type="password" autocomplete="current-password" required>
                                            </div>
                                            <div class="form-group mb-0 justify-content-center">
                                                <a class="text-decoration-none font-weight-bolder" id="forgotPassword" href="#">Forgot Password?</a>
                                            </div>
                                            <div class="form-group float-right mb-0">
                                                <button class="btn btn-outline-dark" type="submit">Login</button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="card-footer d-flex justify-content-between">
                                        <a class="text-decoration-none" href="#">Register Complaint</a>
                                        <a class="text-decoration-none" href="/register">New user? Sign up !</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
        <jsp:include page="common/foot.jsp" >
            <jsp:param name="hideDefaultTheme" value="true" />
        </jsp:include>
        <script src="/js/lib/login.js"></script>
    </body>
</html>
