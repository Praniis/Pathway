<!DOCTYPE html>
<html lang="en">
    <head>
        <jsp:include page="common/head.jsp" >
            <jsp:param name="title" value="Error" />
        </jsp:include>
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Assistant:wght@242&amp;display=swap" rel="stylesheet">
    </head>
    <body style="font-family: 'Assistant', sans-serif;">
        <div id="layoutError">
            <div id="layoutError_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-6">
                                <div class="text-center mt-4">
                                    <h1 class="display-1">500</h1>
                                    <p class="lead">Internal Server Error</p>
                                    <a href="/">
                                        <i class="fas fa-arrow-left mr-1" aria-hidden="true"></i>
                                        Return to Homepage
                                    </a>
                                </div>
                            </div>
                        </div>
                        <p></p>
                    </div>
                </main>
            </div>
            <div id="layoutError_footer"></div>
        </div>
        <jsp:include page="common/foot.jsp" />
    </body>
</html>
