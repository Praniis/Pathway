jQuery(function () {

	var complaintTable = $('#complaintTable').DataTable({
		ajax: {
			dataType: "json",
			headers: { "Authorization": localStorage.Authorization },
			type: "GET",
			url: `/api/complaints/list`,
			dataSrc: 'results'
		},
		lengthMenu: [[100, 200, -1], [100, 200, 'All']],
		order: [[3, 'desc']],
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
				data: 'subject'
			},
			{
				data: null,
				class: 'text-nowrap',
				render: function (data, type, full, meta) {
					return `${full.username || full.email} - ${full.role}`;
				}
			},
			{
				data: 'createdAt',
				class: 'text-nowrap text-center',
				render: function (data, type, full, meta) {
					if (type == 'display' || type == 'filter') {
						return moment(data).format(`DD/MM/YYYY hh:mm A`);
					} else {
						return moment(data).format()
					}
				}
			},
			{
				data: 'updatedAt',
				class: 'text-nowrap text-center',
				render: function (data, type, full, meta) {
					if (type == 'display' || type == 'filter') {
						return moment(data).format(`DD/MM/YYYY hh:mm A`);
					} else {
						return moment(data).format()
					}
				}
			},
			{
				data: null,
				targets: -1,
				orderable: false,
				class: 'text-nowrap',
				render: function (data, type, full, meta) {
					if(full.id == 9) {
						console.log(full.response);
					}
					var html = '';
					html += `<a data-id='${full.id}' class="addNewResponse btn btn-success btn-sm py-0">Add</a>`
					html += `<a data-id='${full.id}' data-responses='${JSON.stringify(full.responses)}' class="viewResponse btn btn-warning btn-sm py-0 ml-2">View</a>`
					return html;
				}
			}
		]
	});

	$('#refresh').on('click', function (e) {
		e.preventDefault();
		complaintTable.ajax.reload();
	});

	var viewResponsesTable = $('#viewResponsesTable').DataTable({
		data: [],
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
				class: 'text-nowrap text-center',
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

	$('#viewResponsesModal').on('shown.bs.modal', function () {
		viewResponsesTable.columns.adjust().draw();
	});
	$('#addNewComplaintModal').on('show.bs.modal', function () {
		if ($('#addNewComplaintModal #id').val() == 'new') {
			$('#addNewComplaintModal .subject').show();
			$('#addNewComplaintModal .modal-title').text('New Complaint');
		} else {
			$('#addNewComplaintModal .subject').hide();
			$('#addNewComplaintModal .modal-title').text('Add New Response');
		}
	});
	$('#addNewComplaintModal').on('hidden.bs.modal', function () {
		$('#addNewComplaintModal input:not(.btn), #addNewComplaintModal textarea').val('');
		$('#addNewComplaintModal #id').val('new');
	});

	$('#viewResponsesModal').on('hidden.bs.modal', function () {
		viewResponsesTable.clear().draw();
	});
	$('#complaintTable').on('click', '.viewResponse', function (e) {
		e.preventDefault();
		let responses = $(this).data('responses');
		$('#viewResponsesModal').modal('show');
		viewResponsesTable.rows.add(responses);
		viewResponsesTable.columns.adjust().draw();
	});

	$('#complaintTable').on('click', '.addNewResponse', function (e) {
		e.preventDefault();
		let id = $(this).data('id');
		$('#addNewComplaintModal #id').val(id);
		$('#addNewComplaintModal').modal('show');
	});

	$('#addNewComplaintForm').submit(function (e) {
		e.preventDefault();
	}).validate({
		submitHandler: function (form) {
			let method = 'POST', id = '';
			if ($('#addNewComplaintModal #id').val() != 'new') {
				method = 'PUT';
				id = $('#addNewComplaintModal #id').val();
			}
			$.ajax({
				type: method,
				url: $(form).attr('action') + id,
				data: $(form).serialize(),
				headers: { Authorization: localStorage.Authorization },
				success: function (response) {
					if (response.success) {
						complaintTable.ajax.reload();
						swal.fire("Done!", response.message, "success").then(() => {
							$('#addNewComplaintModal').modal('hide');
						});
					} else {
						complaintTable.ajax.reload(null, false);
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