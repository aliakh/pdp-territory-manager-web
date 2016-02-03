var googleMap = null;

function showMap(region, zips, franchise) {
    if (region.map == null || region.map.length == 0) {
        alert('No map for the region')
    } else {
        showRegionCenter(region, franchise);
    }
    if (googleMap != null) {
        if (zips != null) {
            for (var i = 0; i < zips.length; i++) {
                var zip = zips[i];
                showZipPolygonFromXml(zip, 'http://maps.google.com/mapfiles/ms/icons/blue.png');
            }
        }
        if (region.map != null && region.map.length > 0) {
            showRegionPolygonFromXml(region, franchise);
        }
    }
}

function showRegionCenter(region, franchise) {
    $xml = $($.parseXML(region.map));

    $xml.find("region").each(function () {

        var mapOptions = {
            center:new google.maps.LatLng($(this).attr('lat'), $(this).attr('lng')),
            zoom:8
        };
        googleMap = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

        var marker = new google.maps.Marker({
            position: new google.maps.LatLng($(this).attr('lat'), $(this).attr('lng')),
            icon:'http://maps.google.com/mapfiles/ms/micons/dollar.png',
            map:googleMap,
            title: 'Region ' + $(this).attr('code')
        });

        attachFranchiseMarkerInfo(marker, getRegionTable(region, franchise));

        var circleOptions = {
            map: googleMap,
            strokeColor: '#86B5D9',
            strokeOpacity: 1,
            strokeWeight: 2,
            fillColor: '#86B5D9',
            fillOpacity: 0,
            center: new google.maps.LatLng($(this).attr('lat'), $(this).attr('lng')),
            radius: region.radius * 1610
        };

        new google.maps.Circle(circleOptions);
    })
}

function attachFranchiseMarkerInfo(marker, html) {
    marker.infoWindow = new google.maps.InfoWindow({
        content: html
    });
    marker.infoWindow.open(googleMap, marker);

    google.maps.event.addListener(marker, 'mouseover', function() {
        marker.infoWindow.open(googleMap, marker);
    });
}

function attachZipMarkerInfo(marker, html) {
    marker.infoWindow = new google.maps.InfoWindow({
        content: html
    });

    google.maps.event.addListener(marker, 'mouseover', function() {
        marker.infoWindow.open(googleMap, marker);
    });
    google.maps.event.addListener(marker, 'mouseout', function() {
        marker.infoWindow.close();
    });
}

function attachZipPolygonInfo(polygon, marker, html) {
    marker.infoWindow = new google.maps.InfoWindow({
        content: html
    });
    google.maps.event.addListener(polygon, 'mouseover', function(e) {
        marker.infoWindow.open(googleMap, marker);
    });
    google.maps.event.addListener(polygon, 'mouseout', function() {
        marker.infoWindow.close();
    });
}

function getRegionTable(region, franchise) {
    var html = '<table class="noscrollbar">';

    if (region) {
        html += getRegionTableRow('Region Id', region.regionId);
        html += getRegionTableRow('Region Code', region.regionCode);
    }

    if (franchise) {
        html += getRegionTableRow('State', franchise.state);
        html += getRegionTableRow('Franchise Id', franchise.franchiseId);
        html += getRegionTableRow('Franchise Name', franchise.franchiseName);
        html += getRegionTableRow('Make', franchise.make);
    }

    html += '</table>';
    return html;
}

function getRegionTableRow(name, value) {
    html = '<tr>';
    html += '<td>' + name + '</td>';
    html += '<td>' + (value ? value : '') + '</td>';
    html += '</tr>';
    return html;
}

function getZipTable(zip) {
    var html = '<table class="noscrollbar">';

    html += getZipTableRow('ZIP ' + zip.zipCode);
    for (var i = 0; i < zip.messages.length; i++) {
        html += getZipTableRow(zip.messages[i]);
    }

    html += '</table>';
    return html;
}

function getZipTableRow(value) {
    html = '<tr>';
    html += '<td>' + value + '</td>';
    html += '</tr>';
    return html;
}

function hideMapDiv() {
    googleMap = null;
    $('#map-canvas').remove();
}

function showMapDiv() {
    $('#map-cell').html('<div id="map-canvas""></div>');
    $("#map-canvas").width($(window).width()- $("#grid").width()-15);
    $("#map-canvas").height($(window).height()-10);
}