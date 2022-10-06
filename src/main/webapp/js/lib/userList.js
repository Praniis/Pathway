jQuery(function () {
    $('select2').select2();
    $('#addNewUserModal #userrole').select2({
      ajax: {
        url: '/api/userrole/list',
        dataType: 'json',
        dropdownParent: $('#addNewUserModal'),
        processResults: function (data) {
              console.log(data);
              return {
                results: data.results.map(e => {
                    return { id: e.id, text: e.name }
                })
              };
        }
      }
    });

    let userTable = $('#userTable').DataTable({
		ajax: {
			dataType: "json",
			type: "GET",
			url: "/api/user/list",
			dataSrc: 'results'
		},
		lengthMenu: [
			[100, 200, -1],
			[100, 200, 'All']
		],
		order: [
			[3, 'desc']
		],
		scrollY: '60vh',
		scrollX: true,
		scrollCollapse: true,
		columnDefs: [{
			defaultContent: '',
			targets: '_all'
		}],
		columns: [{
				data: 'name'
			},
			{
				data: 'username'
			},
			{
				data: 'email',
				class: 'text-nowrap text-center'
			},
			{
				data: 'mobile',
				class: 'text-nowrap text-center'
			},
			{
				data: 'userRole.name',
				class: 'text-nowrap text-center'
			},
			{
				data: null,
				targets: -1,
				orderable: false,
				class: 'text-nowrap',
				render: function (data, type, full, meta) {
					var html = '';
					html += `<a data-id='${full.id}' data-response='${JSON.stringify(full)}' class="edit btn btn-warning btn-sm py-0 ml-2">Edit</a>`
					html += `<a data-id='${full.id}' data-response='${JSON.stringify(full)}' class="changePassword btn btn-primary btn-sm py-0 ml-2">Reset Password</a>`
					return html;
				}
			}
		]
	});

	$('#addNewUserModal').on('show.bs.modal', function () {
		if ($('#addNewUserModal #id').val() == 'new') {
			$('#addNewUserModal .modal-title').text('New User');
		} else {
			$('#addNewUserModal .modal-title').text('Edit User');
			$('#addNewUserModal .password').hide();
		}
	});
	$('#addNewUserModal').on('hidden.bs.modal', function () {
		$('#addNewUserModal input:not(.btn), #addNewUserModal textarea').val('');
		$('#addNewUserModal .password').show();
		$('#addNewUserModal #userrole').val('');
		$('#addNewUserModal #userrole').trigger('change.select2');
		$('#addNewUserModal #id').val('new');
	});

	$('#userTable').on('click', '.edit', function (e) {
		e.preventDefault();
		let response = $(this).data('response');
		$('#addNewUserModal #id').val(response.id);
		$('#addNewUserModal #name').val(response.name);
		$('#addNewUserModal #username').val(response.username);
		$('#addNewUserModal #email').val(response.email);
		$('#addNewUserModal #mobile').val(response.mobile);
		$('#addNewUserModal #userrole').val(response.userRole.id);
		$('#addNewUserModal #userrole').trigger('change.select2');
		$('#addNewUserModal').modal('show');
	});

	$('#addNewUserForm').submit(function (e) {
		e.preventDefault();
	}).validate({
		submitHandler: function (form) {
			let method = 'POST';
			if ($('#addNewUserModal #id').val() != 'new') {
				method = 'PUT';
			}
			$.ajax({
				type: method,
				url: $(form).attr('action'),
				data: $(form).serialize(),
				success: function (response) {
					if (response.success) {
						userTable.ajax.reload();
						swal.fire("Done!", response.message, "success").then(() => {
							$('#addNewUserModal').modal('hide');
						});
					} else {
						userTable.ajax.reload(null, false);
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

	$('#userTable').on('click', '.changePassword', function (e) {
            e.preventDefault();
            let data = $(this).data('response');
            swal.fire({
                title: 'Change Password',
                text: 'Enter the new password',
                input: 'text',
                inputPlaceholder: 'Enter password',
                showCancelButton: true,
                inputValidator: (value) => {
                    if (!value) {
                        return 'Invalid password';
                    }
                },
            }).then(password => {
                if (password.isConfirmed) {
                    Swal.showLoading();
                    $.ajax({
                        type: 'POST',
                        url: '/api/user/reset-password',
                        data: {
                            userId: data.id,
                            password: password.value
                        },
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
});