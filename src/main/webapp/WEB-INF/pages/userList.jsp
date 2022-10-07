<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <jsp:include page="common/head.jsp">
        <jsp:param name="title" value="Users" />
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
                            <i class="fas fa-comment-alt mr-1" aria-hidden="true"></i>
                            Users
                            <div class="float-right" style="margin:-8px 0 0;">
                                <button class="btn btn-info" id="addUser" href="#addNewUserModal" data-toggle="modal">
                                    New User <i class="fa fa-user-plus" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                        <div class="modal fade" id="addNewUserModal" aria-hidden="true" aria-labelledby="myModalLabel" data-backdrop="static" role="dialog" data-keyboard="false">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form class="form-horizontal cmxform" id="addNewUserForm" role="form" method="POST"
                                        action="/api/user/createOrUpdate">
                                        <div class="modal-header">
                                            <h5 class="modal-title">New User</h5>
                                            <button class="close" aria-hidden="true" data-dismiss="modal" type="button">&times;</button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="container-fluid">
                                                <div class="form-group">
                                                    <label class="bmd-label-floating" for="Name">Name</label>
                                                    <input class="form-control" type="text" id="name" name="name" placeholder="Name" autofocus required>
                                                </div>
                                                <div class="form-group">
                                                    <label class="bmd-label-floating" for="Username">Username</label>
                                                    <input class="form-control" type="text" id="username" name="username" placeholder="Username" autofocus required>
                                                </div>
                                                <div class="form-group password">
                                                    <label class="bmd-label-floating" for="Password">Password</label>
                                                    <input class="form-control" type="password" id="password" name="password" placeholder="Password" autofocus required>
                                                </div>
                                                <div class="form-group">
                                                    <label class="bmd-label-floating" for="Email">Email</label>
                                                    <input class="form-control" type="email" id="email" name="email" placeholder="Email" autofocus required>
                                                </div>
                                                <div class="form-group">
                                                    <label class="bmd-label-floating" for="Mobile">Mobile</label>
                                                    <input class="form-control" type="tel" id="mobile" name="mobile" placeholder="Mobile" pattern="[7-9]{1}[0-9]{9}" autofocus required>
                                                </div>
                                                <div class="form-group">
                                                    <label class="bmd-label-floating" for="role">User Role</label>
                                                    <select class="custom-select" id="userrole" name="userRoleId" required></select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <input class="btn btn-primary" id="saveaddNewUserBtn" type="submit"
                                                value="Save">
                                            <input id="id" type="hidden" name="id" value="new">
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table id="userTable" class="display table table-bordered table-sm" id="usersTable" cellpadding="0"
                                    cellspacing="0" border="0" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Username</th>
                                            <th>Email</th>
                                            <th>Mobile</th>
                                            <th>UserRole</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                </table>
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
    <script src="/js/lib/userList.js"></script>
</body>

</html>