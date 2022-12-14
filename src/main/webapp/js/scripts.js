(function ($) {
    "use strict";
    var path = window.location.href;
    $("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function () {
        if (this.href === path) {
            $(this).addClass("active");
        }
    });

    $("#sidebarToggle").on("click", function (e) {
        e.preventDefault();
        $("body").toggleClass("sb-sidenav-toggled");
    });
    try {
        $('body').bootstrapMaterialDesign();
    } catch (err) {}
    
    $(document)
        .ajaxStart(function() { Pace.start(); })
        .ajaxStop(function() { Pace.restart(); })
        .ajaxError(function() { Pace.restart(); });

    $.fn.modal.Constructor.prototype.enforceFocus = function() {};
})(jQuery);