<!DOCTYPE html>
<html lang="en">
    <head>
        <jsp:include page="common/head.jsp" >
            <jsp:param name="title" value="Not Found" />
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
                                    <img class="mb-4 img-error" src="/images/error-404-monochrome.svg">
                                    <p class="lead">This requested URL was not found on this server.</p>
                                    <p class="lead"></p>
                                    <a href="/">
                                        <i class="fas fa-arrow-left mr-1" aria-hidden="true"></i>
                                        Return to Homepage
                                    </a>
                                </div>
                            </div>
                        </div>
                        <p class="lead"></p>
                    </div>
                </main>
            </div>
            <div id="layoutError_footer"></div>
        </div>
        <jsp:include page="common/foot.jsp" />
    </body>
</html>
