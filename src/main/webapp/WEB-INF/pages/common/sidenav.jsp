<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
            <div class="sb-sidenav-menu">
            <div class="nav">
                <a class="nav-link" href='/home'>
                    <div class="sb-nav-link-icon">
                        <i class="fas fa-laptop-house"></i>
                        Home
                    </div>
                </a>
                <a class="nav-link" href='/users'>
                    <div class="sb-nav-link-icon">
                        <i class="fas fa-users"></i>
                        Users
                    </div>
                </a>
            </div>
        </div>
        <div class="sb-sidenav-footer">
            <div class="small">Logged in as: </div>
            <c:out value="#session.username" />
        </div>
    </nav>
</div>