jQuery(function () {
    $('.auth-form').validate({
        rules: {
            password: {
                required: true, minlength: 5
            },
            passwordConfirm: {
                required: true,
                minlength: 5,
                equalTo: "#password"
            }
        },
        submitHandler: function (form) {
            var isLogin = !/sign-up/gi.test($(form).attr('class'));
            $.ajax({
                type: 'POST',
                url: $(form).attr('action'),
                data: $(form).serialize(),
                success: function (response) {
                    if (response.success) {
                        if (isLogin) {
                            for (const i in response.data || []) {
                                localStorage.setItem(i, response.data[i]);
                            }
                            location.reload();
                        } else {
                            swal.fire("Done!", response.message, "success")
                                .then(() => {
                                    $(location).attr('href', '/login');
                                });
                        }
                    } else {
                        swal.fire("Fail!", response.error, "error");
                    }
                },
                error: function (response) {
                    swal.fire("Fail!", response.error || '', "error").then(() => {
                        location.reload();
                    });
                }
            });
            return false;
        }
    });

    $('#forgotPassword').on('click', function (e) {
        e.preventDefault();
        swal.fire({
            title: 'Recover Password',
            text: 'Enter your username and we will send you a link to reset your password.',
            input: 'text',
            inputPlaceholder: 'Enter username',
            showCancelButton: true,
            inputValidator: (value) => {
                if (!value) {
                    return 'Invalid Username';
                }
            },
        }).then(username => {
            if (username.isConfirmed) {
                Swal.showLoading();
                $.ajax({
                    type: 'POST',
                    url: '/api/auth/recover-password',
                    data: { username: username.value },
                    success: function (response) {
                        Swal.hideLoading();
                        if (response.success) {
                            swal.fire("Done!", response.message, "success");
                        } else {
                            swal.fire("Fail!", response.error, "error");
                        }
                    },
                    error: function (response) {
                        Swal.hideLoading();
                        swal.fire("Fail!", response.error || '', "error").then(() => {
                            location.reload();
                        });
                    }
                });
            }
        });
    });
    $('#username').trigger('keyup');
});