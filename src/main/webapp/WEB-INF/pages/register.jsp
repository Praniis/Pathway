<!DOCTYPE html>
<html lang="en">
    <head>
        <jsp:include page="common/head.jsp" >
            <jsp:param name="title" value="Register" />
            <jsp:param name="hideDefaultTheme" value="true" />
        </jsp:include>
    </head>
    <body class="authMainBody">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-6 col-md-10 col-sm-12">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header">
                                        <h4 class="text-center font-weight-light my-4 text-uppercase">Sign Up | PATHWAY</h4>
                                    </div>
                                    <div class="card-body">
                                        <form class="auth-form sign-up" method="POST" action="/api/auth/register">
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="name">Name</label>
                                                <input class="form-control" id="name" name="name" type="text" autofocus required>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="email">Email</label>
                                                <input class="form-control" id="email" name="email" type="email" required>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="mobile">Mobile</label>
                                                <input class="form-control" id="mobile" name="mobile" type="tel"  pattern="[7-9]{1}[0-9]{9}" required>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="username">Username</label>
                                                <input class="form-control" id="username" name="username" type="text" required>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="bmd-label-floating" for="password">Password</label>
                                                        <input class="form-control" id="password" name="password" type="password" autocomplete="new-password" required>
                                                        <div class="invalid-tooltip"></div>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="bmd-label-floating" for="passwordConfirm">Confirm Password</label>
                                                        <input class="form-control" id="passwordConfirm" name="passwordConfirm" type="password" autocomplete="new-password" required>
                                                        <div class="invalid-tooltip"></div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="orgName">Organisation Name</label>
                                                <input class="form-control" id="orgName" name="orgName" type="text" required>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="orgAddress">Organisation Address</label>
                                                <textarea class="form-control" id="orgAddress" name="orgAddress" cols="30" rows="10" required></textarea>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="orgEmail">Organisation Email </label>
                                                <input class="form-control" id="orgEmail" name="orgEmail" type="email" required>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="orgWebsite">Organisation Website</label>
                                                <input class="form-control" id="orgWebsite" name="orgWebsite" type="text" required>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="bmd-label-floating" for="orgType">Organisation Type</label>
                                                <select class="custom-select" id="orgType" name="orgType" required>
                                                    <option value="" selected disabled>Select Type</option>
                                                    <option value="1">Education</option>
                                                    <option value="2">Industry</option>
                                                    <option value="3">Public Sector</option>
                                                </select>
                                                <div class="invalid-tooltip"></div>
                                            </div>
                                            <div class="form-group mt-4">
                                                <button class="btn btn-block btn-outline-dark" type="submit">Register</button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="card-footer text-center">
                                        <a href="/login">Have an account? Go to login</a>
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
        <script src="js/lib/login.js"></script>
    </body>
</html>
