<!DOCTYPE html>
<html lang="en">
    <head>
        <jsp:include page="common/head.jsp" >
            <jsp:param name="title" value="Not Authorized" />
        </jsp:include>
    </head>
    <body style="font-family: 'Assistant', sans-serif;">
        <div id="layoutError">
            <div id="layoutError_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-6">
                                <div class="text-center mt-4">
                                    <h1 class="display-1">401</h1>
                                    <p class="lead">Unauthorized</p>
                                    <p>Access to this resource is denied.</p>
                                    <a href="/">
                                        <i class="fas fa-arrow-left mr-1" aria-hidden="true"></i>
                                        Return to Homepage
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
            <div id="layoutError_footer"></div>
        </div>
        <jsp:include page="common/foot.jsp" />
    </body>
</html>
