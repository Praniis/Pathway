function initMap() {
    //#region Init
    var map, drawingManager, geocoder,
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

    drawingManager = new google.maps.drawing.DrawingManager({
        drawingControlOptions: {
            position: google.maps.ControlPosition.TOP_CENTER,
            drawingModes: [
                google.maps.drawing.OverlayType.MARKER,
            ]
        }
    });
    drawingManager.setMap(map);

    google.maps.event.addListener(drawingManager, 'overlaycomplete', function (event) {
        drawingManager.setDrawingMode(null);
        if (event.type == google.maps.drawing.OverlayType.MARKER) {
            let marker = event.overlay;
            marker.content = '';
            marker.infoWindow = infoWindow;
            markers.push(marker);
            initNewMarker(markers.length - 1);
        }
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

    function initNewMarker(markerIndex) {
        let marker = markers[markerIndex];
        if(!marker) { return; }
        google.maps.event.addListener(marker, 'click', function () {
            showInfoWindow(markerIndex);
        });
        addMarkerDiv(markerIndex);
        updateMarkerStyles();
        calculateRoute();
    }

    function showInfoWindow(markerIndex) {
        let marker = markers[markerIndex];
        if(!marker) { return; }
        marker.infoWindow.setContent(marker.content);
        marker.infoWindow.open(map, marker);
    }



    function addMarkerDiv(markerIndex) {
        let html = `<div class="waypoint list-group-item nested-1" data-id="${markerIndex}">`;
        html += `<a href="#" title="Remove Stop" class="waypointRemoveBtn float-right"><i class="fa fa-times error" aria-hidden="true"></i></a>`;
        html += `<div class="address"><b>Address:</b><p>Loading...</p></div>`;
        html += `<div class="time"><input name="stop[${markerIndex}]" class='form-control' type='text' placeholder='Enter stop Time' required></div>`;
        html += `</div>`;
        $('#waypointsList').append(html);
        $(`.waypoint[data-id="${markerIndex}"]`).on('click', function () {
            showInfoWindow($(this).data('id'));
        });
        $(`.waypoint .time input[name="stop[${markerIndex}]"]`).datetimepicker({
            format: 'LT',
        });
        $('.waypointRemoveBtn').on('click', function (e) {
            e.preventDefault();
            if (e.timeStamp !== prevTimeStamp) {
                prevTimeStamp = e.timeStamp;
                removeMarker($(this).parents('.waypoint').data('id'));

            }
        });
        $(`.waypoint[data-id="${markerIndex}"]`).click(function () {
            showInfoWindow($(this).data('id'));
        });
        $("#waypointsList").getNiceScroll().resize();
        updateMarkerContent(markerIndex);
    }

    function updateMarkerContent(markerIndex) {
        let marker = markers[markerIndex];
        if(!marker) { return; }
        geocoder.geocode({ location: marker.getPosition() }, (results, status) => {
            if (status === "OK") {
                if (results[0]) {
                    marker.content = results[0].formatted_address;
                    $(`.waypoint[data-id="${markerIndex}"] .address p`).html(marker.content);
                    $(`.waypoint[data-id="${markerIndex}"] .time input`).val('');
                    $(`.waypoint .time input[name="stop[${markerIndex}]"]`).datetimepicker({
                        format: 'LT'
                    });
                    $(`.waypoint[data-id="${markerIndex}"]`).off('click');
                    $(`.waypoint[data-id="${markerIndex}"]`).click(function () {
                        showInfoWindow(markerIndex);
                    });
                }
            } else {
                alert("Geocoder failed due to: " + status);
            }
            $("#waypointsList").getNiceScroll().resize();
        });
    }

    function removeMarker(markerIndex) {
        if (markerIndex < markers.length) {
            $(`.waypoint[data-id="${markerIndex}"]`).remove();
            $(`.waypoint`).each(function () {
                if ($(this).data('id') > markerIndex) {
                    $(this).attr('data-id', $(this).data('id') - 1);
                    $(this).childrens('input').attr('name', `stop[${$(this).data('id') - 1}]`);
                }
                $(`.waypoint[data-id="${$(this).data('id')}"]`).click(function () {
                    showInfoWindow($(this).data('id'));
                });
            });
            markers[markerIndex].setMap(null);
            markers.splice(markerIndex, 1);
        }
        updateMarkerStyles();
        calculateRoute();
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

    $('#waypointsList').sortable({
        swap: true,
        swapClass: 'highlight',
        animation: 150,
        onSort: function (evt) {
            changeOrder(evt.oldIndex, evt.newIndex);
        },
    });
    function changeOrder(oldIndex, newIndex) {
        let tempPositon = markers[oldIndex].getPosition();
        markers[oldIndex].setPosition(markers[newIndex].getPosition());
        markers[newIndex].setPosition(tempPositon);
        $(`.waypoint[data-id="${newIndex}"]`).attr('data-id', '-1');
        $(`.waypoint[data-id="${oldIndex}"]`).attr('data-id', newIndex);
        $(`.waypoint[data-id="${newIndex}"] .time input`).val('');
        $(`.waypoint[data-id="${oldIndex}"] .time input`).val('');
        $(`.waypoint[data-id="-1"]`).attr('data-id', oldIndex);
        $('#waypointsList').sortable({
            swap: true,
            swapClass: 'highlight',
            animation: 150,
            onSort: function (evt) {
                changeOrder(evt.oldIndex, evt.newIndex);
            },
        });
        updateMarkerContent(newIndex);
        updateMarkerContent(oldIndex);
        updateMarkerStyles();
        calculateRoute();
    }


    $('#newRouteForm #type').on('click', function (e) {
        e.preventDefault();
        if ($(this).val() == 'Permanent') {
            $('#newRouteForm #expireOn').val('');
            $('#newRouteForm .expireOn').hide();
        } else {
            $('#newRouteForm .expireOn').show()
        };
    });

    function getWaypoints() {
        let waypointsData = [];
        if (!(directionsRenderer.directions && directionsRenderer.directions.routes)) {
            return waypointsData;
        }
        let length = directionsRenderer.directions.routes[0].legs.length;
        let waypoints = directionsRenderer.directions.routes[0].legs;
        let timings = [];
        $(`.waypoint .time input`).each(function () {
            timings.push($(this).val());
        });
        waypointsData.push({ 'lat': waypoints[0].start_location.lat(), 'lng': waypoints[0].start_location.lng(), address: waypoints[0].start_address, time: timings[0] });
        for (let i = 1; i < length; i++) {
            waypointsData.push({ 'lat': waypoints[i].start_location.lat(), 'lng': waypoints[i].start_location.lng(), address: waypoints[i].start_address, time: timings[i] });
        }
        waypointsData.push({ 'lat': waypoints[length - 1].end_location.lat(), 'lng': waypoints[length - 1].end_location.lng(), address: waypoints[length - 1].end_address, time: timings[length -1] });
        return waypointsData;
    }

    $('#newRouteForm').submit(function (e) {
        e.preventDefault();
    }).validate({
        submitHandler: function (form) {
            let waypoints = getWaypoints();
            if (waypoints.length < 1) {
                swal.fire("Fail!", 'Route not processed currectly. Please select proper stops.', "error");
                return;
            }
            $.ajax({
                type: $(form).attr('method'),
                url: $(form).attr('action'),
                data: {
                    name: $('#newRouteForm #name').val(),
                    type: $('#newRouteForm #type').val(),
                    expireOn: $('#newRouteForm #expireOn').val(),
                    waypoints: JSON.stringify(waypoints),
                },
                headers: { Authorization: localStorage.Authorization },
                success: function (response) {
                    if (response.success) {
                        swal.fire("Done!", response.message, "success").then(() => {
                            location.href = response.location;
                        });
                    } else {
                        swal.fire("Fail!", response.message || response.error, "error");
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
    $("#waypointsList").niceScroll({
        cursorcolor:"grey",
        cursorwidth:"4px"
    });
}