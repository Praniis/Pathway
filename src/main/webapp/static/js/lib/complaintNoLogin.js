jQuery(function () {
	var url = window.location.href;
	var token = url.slice(url.lastIndexOf('/'), url.length);
	var viewResponsesTable = $('#viewResponsesTable').DataTable({
		ajax: {
			dataType: "json",
			headers: { "Authorization": localStorage.Authorization },
			type: "GET",
			url: `/api/complaints/responsesNoLogin` + token,
			dataSrc: function (response) {
				if (!response.success) {
					swal.fire("Fail!", response.error, "error");
				}
				return response.results || [];
			}
		},
		order: [[2, 'desc']],
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
				data: '',
				class: 'text-nowrap',
				render: function (data, type, full, meta) {
					return `${full.username || full.email} - ${full.idNo || ''} ${full.role}`;
				}
			},
			{
				data: 'message'
			},
			{
				data: 'time',
				class: 'text-nowrap text-center',
				render: function (data, type, full, meta) {
					if (type == 'display' || type == 'filter') {
						return moment(data).format(`DD/MM/YYYY hh:mm A`);
					} else {
						return moment(data).format()
					}
				}
			}
		]
	});

	$('#refresh').on('click', function (e) {
		e.preventDefault();
		viewResponsesTable.ajax.reload();
	});

	$('#addNewComplaintModal').on('hidden.bs.modal', function () {
		$('#addNewComplaintModal input:not(.btn), #addNewComplaintModal textarea').val('');
		$('#addNewComplaintModal #id').val('new');
	});

	$('#addNewComplaintForm').submit(function (e) {
		e.preventDefault();
	}).validate({
		submitHandler: function (form) {
			$.ajax({
				type: $(form).attr('method'),
				url: $(form).attr('action'),
				data: $(form).serialize(),
				headers: { Authorization: localStorage.Authorization },
				success: function (response) {
					if (response.success) {
						viewResponsesTable.ajax.reload();
						swal.fire("Done!", response.message, "success").then(() => {
							$('#addNewComplaintModal').modal('hide');
						});
					} else {
						viewResponsesTable.ajax.reload(null, false);
						swal.fire("Fail!", response.error, "error").then(() => {
							$('#addNewComplaintModal').modal('hide');
						});
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
});