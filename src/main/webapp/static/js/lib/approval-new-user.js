jQuery(function () {
	var userApprovalTable = $('#userApprovalTable').DataTable({
		ajax: {
			dataType: "json",
			headers: { "Authorization": localStorage.Authorization },
			type: "GET",
			url: `/api/users/listApproval`,
			dataSrc: 'results'
		},
		lengthMenu: [[100, 200, -1], [100, 200, 'All']],
		order: [[6, 'desc']],
		scrollY: '60vh',
		scrollX: true,
		scrollCollapse: true,
		columnDefs: [
			{
				defaultContent: '',
				targets: '_all'
			}
		],
		columns: [
			{
				data: 'profileImage',
				class: 'text-nowrap text-center',
				render: function (data, type, full, meta) {
					if (data) {
						return `<img class="profileImage" src="${localStorage.uploads + data}" alt="photo" style="height:45px;width:45px;border-radius: 50%;object-fit:cover;">`;
					} return '';
				}
			},
			{
				data: 'idNo',
				class: 'text-nowrap text-center'
			},
			{
				data: 'name',
				class: 'text-nowrap text-center'
			},
			{
				data: 'username',
				class: 'text-nowrap text-center'
			},
			{
				data: 'email',
				class: 'text-nowrap text-center'
			},
			{
				data: 'role',
				class: 'text-nowrap text-center'
			},
			{
				data: 'createdAt',
				class: 'text-nowrap text-center',
				render: function (data, type, full, meta) {
					return moment(data).format('DD/MM/YYYY hh:mm A')
				}
			},
			{
				data: 'status',
				class: 'text-nowrap text-center'
			},
			{
				data: 'approvalMessage'
			},
			{
				data: 'UserIdApprovalBy',
				class: 'text-nowrap text-center',
				render: function (data, type, full, meta) {
					try {
						if (data) {
							return full.RequestApprovalBy.idNo + ' ' + full.RequestApprovalBy.name
						} else {
							return '';
						}
					} catch (error) {
						return '';
					}
				}
			},
			{
				data: null,
				targets: -1,
				orderable: false,
				class: 'text-nowrap',
				render: function (data, type, full, meta) {
					var html = '';
					if (full.status == 'Waiting') {
						html += `<a data-details='${JSON.stringify(full)}' class="approval btn btn-warning btn-sm py-0">Approve/Reject</a>`
					}
					return html;
				}
			}
		]
	});

	$('#userApprovalTable').on('click', '.approval', function (e) {
		e.preventDefault();
		let ele = $(this);
		let details = ele.data('details');
		swal.fire({
			title: 'New User Approval',
			html: `Do you want to approve user ${details.name} - ${details.idNo} (${details.role}) with username ${details.username}` +
				'<br><input id="approvalMesage" class="swal2-input" placeholder="Approval message">',
			showCancelButton: true,
			showDenyButton: true,
			showConfirmButton: true,
			cancelButtonText: 'Cancel',
			denyButtonText: 'Reject',
			confirmButtonText: 'Approve',
			inputValidator: (value) => {
			}
		}).then(data => {
			if (data.isConfirmed || data.isDenied) {
				$.ajax({
					type: 'POST',
					url: '/api/users/approve/' + details.id,
					headers: { Authorization: localStorage.Authorization },
					data: { approval: data.isConfirmed ? 'approve' : 'reject', approvalMessage: $('#approvalMesage').val() },
					success: function (response) {
						if (response.success) {
							swal.fire("Done!", response.message, "success");
							userApprovalTable.ajax.reload(null, false);
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
			}
		})
	});
	let viewerButton = { show: true, size: 'large' };
	let viewerOptions = {
		url: 'src',
		title: false,
		toolbar: {
			'prev': viewerButton,
			'zoom-in': viewerButton,
			'zoom-out': viewerButton,
			'rotateLeft': viewerButton,
			'rotateRight': viewerButton,
			'oneToOne': viewerButton,
			'reset': viewerButton,
			'next': viewerButton,
		}
	};
	$('#userApprovalTable').on('click', '.profileImage', function () {
		try {
			$(this).data('viewer').show();
		} catch (error) {
			$('.profileImage').viewer(viewerOptions);
			$(this).data('viewer').show();
		}
	});
});