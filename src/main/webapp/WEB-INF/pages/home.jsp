<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <jsp:include page="common/head.jsp" >
            <jsp:param name="title" value="Home" />
        </jsp:include>
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Assistant&amp;display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.39.0/css/tempusdominus-bootstrap-4.min.css" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs4/dt-1.10.24/af-2.3.5/b-1.7.0/b-colvis-1.7.0/b-html5-1.7.0/cr-1.5.3/date-1.0.3/fc-3.3.2/fh-3.1.8/kt-2.6.1/r-2.2.7/rg-1.1.2/rr-1.2.7/sc-2.0.3/sb-1.0.1/sp-1.2.2/sl-1.3.3/datatables.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/css/bootstrap-datetimepicker.min.css">
    </head>
    <body class="sb-nav-fixed" style="font-size: .875rem;">
        <jsp:include page="common/topnav.jsp" />
        <div id="layoutSidenav">
            <jsp:include page="common/sidenav.jsp" />
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid mt-3">
                        <div class="card xs-12">
                            <div class="card-header">
                                <i class="fas fa-table mr-1" aria-hidden="true"></i>
                                Home Page
                            </div>
                            <div class="card-body">
                                <div class="d-flex align-items-center">
                                    <div class="ml-3 w-100">
                                        <h4 class="mb-0 mt-0"></h4>
                                        <span></span>
                                        <div class="p-2 mt-2 d-flex justify-content-between rounded stats">
                                            <div class="d-flex flex-column">
                                                <span class="font-weight-bold">Name</span>
                                                <span><c:out value="${user.getName()}" /></span>
                                            </div>
                                            <div class="d-flex flex-column">
                                                <span class="font-weight-bold">Email</span>
                                                <span><c:out value="${user.getEmail()}" /></span>
                                            </div>
                                            <div class="d-flex flex-column">
                                                <span class="font-weight-bold">Mobile</span>
                                                <span><c:out value="${user.getMobile()}" /></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
        <jsp:include page="common/foot.jsp" />
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.0/moment.min.js"></script>
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.39.0/js/tempusdominus-bootstrap-4.min.js" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/pdfmake.min.js"></script>
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/vfs_fonts.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/v/bs4/dt-1.10.24/af-2.3.5/b-1.7.0/b-colvis-1.7.0/b-html5-1.7.0/cr-1.5.3/date-1.0.3/fc-3.3.2/fh-3.1.8/kt-2.6.1/r-2.2.7/rg-1.1.2/rr-1.2.7/sc-2.0.3/sb-1.0.1/sp-1.2.2/sl-1.3.3/datatables.min.js"></script>
        <script src="/js/jquery.nicescroll.min.js"></script>
    </body>
</html>
