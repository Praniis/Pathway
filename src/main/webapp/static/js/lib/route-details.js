function initMap() {
    //#region Init
    var map, geocoder,
        directionsService, directionsRenderer,
        markers = [], prevTimeStamp = 0;

    var infoWindow = new google.maps.InfoWindow();
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 21.1289955, lng: 82.7792201 },
        zoom: 5,
        mapTypeControl: true,
        streetViewControl: false,
    });

    directionsRenderer = new google.maps.DirectionsRenderer({ suppressMarkers: true });
    directionsRenderer.setMap(map);

    directionsService = new google.maps.DirectionsService();
    geocoder = new google.maps.Geocoder();

    var pacInput = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(pacInput);
    map.controls[google.maps.ControlPosition.LEFT_TOP].push(pacInput);
    var searchBox = new google.maps.places.SearchBox(pacInput);
    map.controls[google.maps.ControlPosition.RIGHT_TOP].push(document.getElementById('totalDistanceDiv'));

    searchBox.addListener('places_changed', function () {
        const places = searchBox.getPlaces();

        if (places.length == 0) { return; }

        // For each place, get the icon, name and location.
        const bounds = new google.maps.LatLngBounds();
        places.forEach(function (place) {
            if (!place.geometry) { return; }

            if (place.geometry.viewport) {
                bounds.union(place.geometry.viewport);
            } else {
                bounds.extend(place.geometry.location);
            }
        });
        map.fitBounds(bounds);
        $(pacInput).val('');
    });
    //#endregion Init

    function computeTotalDistance(result) {
        let route = result.routes[0];
        let total = 0;
        if (!route) {
            $("#totalDistance").html(`0 Km.`);
            return;
        }
        for (let i = 0; i < route.legs.length; i++) {
            total += route.legs[i].distance.value;
        }
        $("#totalDistance").html(`${(total / 1000).toFixed(2)} Km.`);
    }

    function calculateRoute() {
        $("#totalDistance").html(`0 Km.`);
        directionsRenderer.setMap();
        if (markers.length < 2) { return; }
        let waypoints = [];
        for (let i = 1; i < (markers.length - 1); i++) {
            waypoints.push({
                location: markers[i].getPosition(),
            });
        }

        let request = {
            origin: markers[0].getPosition(),
            destination: markers[markers.length - 1].getPosition(),
            waypoints: waypoints,
            travelMode: google.maps.TravelMode.DRIVING
        };

        directionsService.route(request, function (response, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                directionsRenderer.setDirections(response);
                directionsRenderer.setMap(map);
                computeTotalDistance(response);
            } else {
                swal.fire("Fail!", "Could not display directions due to: " + status, "error");
            }
        });
    }

    function updateMarkerStyles() {
        if (markers.length > 1) {
            markers[0].setIcon('https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=1|49AA42|000000');
            for (let i = 1; i < (markers.length - 1); i++) {
                markers[i].setIcon(`https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=${i + 1}|F4DB95|000000`);
            }
            markers[markers.length - 1].setIcon(`https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=${markers.length}|FE6256|000000`);
        } else if (markers.length == 1) {
            markers[0].setIcon('https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=1|49AA42|000000');
        }
    }

    var routeDetailsTable = $('#routeDetailsTable').DataTable({
        ajax: {
            dataType: "json",
            headers: { "Authorization": localStorage.Authorization },
            type: "GET",
            url: `/api/routes/list`,
            dataSrc: function (response) {
                if (!response.success) {
                    swal.fire("Fail!", response.error, "error");
                }
                return response.results || [];
            }
        },
        lengthMenu: [[100, 200, -1], [100, 200, 'All']],
        scrollY: '70vh',
        scrollX: true,
        scrollCollapse: true,
        columnDefs: [
            {
                defaultContent: '',
                targets: '_all'
            }
        ],
        columns: [
            { data: 'id' },
            { data: 'name' },
            { data: 'type' },
            {
                data: 'status',
                render: function (data, type, full, meta) {
                    return data ? 'Active' : 'In Active';
                }
            },
            {
                data: 'expireOn',
                class: 'text-nowrap',
                render: function (data, type, full, meta) {
                    return (data && moment(data).format('DD/MM/YYYY')) || '';
                }
            },
            {
                data: 'start',
                render: function (data, type, full, meta) {
                    if(data && data.address) {
                        return `<a href='#' title='click to view on map' class='tableLocation' data-details='${JSON.stringify(data)}'>${data.address}</a>`
                    }
                    return '';
                }
            },
            {
                data: 'end',
                render: function (data, type, full, meta) {
                    if(data && data.address) {
                        return `<a href='#' title='click to view on map' class='tableLocation' data-details='${JSON.stringify(data)}'>${data.address}</a>`
                    }
                    return '';
                }
            },
            {
                data: null,
                targets: -1,
                orderable: false,
                class: 'text-nowrap',
                render: function (data, type, full, meta) {
                    return `<a data-details='${JSON.stringify(full.waypoints)}' class="seeMap btn btn-warning btn-sm py-0">Map</a>`
                }
            }
        ]
    });

    $('#refresh').on('click', function (e) {
        infoWindow.close();
		e.preventDefault();
		routeDetailsTable.ajax.reload();
        $('#waypointsList').html('');
        clearAllMarkers();
        markers = [];
	});

    var tempMarker = new google.maps.Marker({
        map: map
    });
    google.maps.event.addListener(tempMarker, 'click', function () {
        showInfoWindowMarker(tempMarker);
    });

    $('#routeDetailsTable').on('click', '.tableLocation', function (e) {
		e.preventDefault();
        let ele = $(this);
        let details = ele.data('details');
        clearAllMarkers();
        map.setCenter({ lat: details.lat, lng: details.lng });
        map.setZoom(15);
        tempMarker.setMap(map);
        tempMarker.setPosition({ lat: details.lat, lng: details.lng });
        tempMarker.content = details.address || '';
        tempMarker.time = details.time || '';
        showInfoWindowMarker(tempMarker);
    });

    function clearAllMarkers() {
        for (let i = 0; i < markers.length; i++) {
          markers[i].setMap(null);
        }
        tempMarker.setMap(null);
        markers = [];
        infoWindow.close();
        calculateRoute();
    }

    $('#routeDetailsTable').on('click', '.seeMap', function (e) {
		e.preventDefault();
        clearAllMarkers();
        $('#waypointsList').html('');
        try {
            let ele = $(this);
            let waypoints = ele.data('details');
            for (const waypoint of waypoints) {
                let marker = new google.maps.Marker({
                    position: { lat: waypoint.lat, lng: waypoint.lng },
                    map: map
                });
                marker.content = waypoint.address;
                marker.time = waypoint.time;
                markers.push(marker);
                google.maps.event.addListener(marker, 'click', function () {
                    showInfoWindow(markers.length - 1);
                });
                addMarkerDiv(markers.length - 1);
            }
            calculateRoute();
            updateMarkerStyles();
        } catch (error) {
           console.error(error) ;
        }
	});

    function addMarkerDiv(markerIndex) {
        let marker = markers[markerIndex];
        if(!marker) { return; };
        let html = `<div class="waypoint list-group-item nested-1" data-id="${markerIndex}">`;
        html += `<b>Address: </b> ${marker.content}<br>`;
        html += `<b>Time: </b> ${marker.time}`;
        html += `</div>`;
        $('#waypointsList').append(html);
        $(`.waypoint[data-id="${markerIndex}"]`).on('click', function () {
            showInfoWindow($(this).data('id'));
        });
        $(`.waypoint[data-id="${markerIndex}"]`).click(function () {
            showInfoWindow($(this).data('id'));
        });
        $("#waypointsList").getNiceScroll().resize();
    }

    function showInfoWindow(markerIndex) {
        let marker = markers[markerIndex];
        if(!marker) { return; }
        let html = '';
        html += `<b>Address:</b>${marker.content}<br>`;
        html += `<b>Time:</b>${marker.time}`;
        infoWindow.setContent(html);
        infoWindow.open(map, marker);
    }

    function showInfoWindowMarker(marker) {
        infoWindow.close();
        let html = '';
        html += marker.content && `<b>Address:</b>${marker.content}<br>` || '';
        html += marker.time && `<b>Time:</b>${marker.time}` || '';
        infoWindow.setContent(html);
        html && infoWindow.open(map, marker);
    }

    function updateMarkerStyles() {
        if (markers.length > 1) {
            markers[0].setIcon('https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=1|49AA42|000000');
            for (let i = 1; i < (markers.length - 1); i++) {
                markers[i].setIcon(`https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=${i + 1}|F4DB95|000000`);
            }
            markers[markers.length - 1].setIcon(`https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=${markers.length}|FE6256|000000`);
        } else if (markers.length == 1) {
            markers[0].setIcon('https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=1|49AA42|000000');
        }
    }
}