function showRegionPolygonFromXml(region, franchise) {
    $xml = $($.parseXML(region.map));

    $xml.find("region").each(function () {

        $(this).children().each(function () {
            var points = [];

            $(this).children().each(function () {
                points.push(new google.maps.LatLng($(this).attr('lat'), $(this).attr('lng')));
            })

            var line = new google.maps.Polyline({
                path:points,
                strokeColor:'#000000',
                strokeOpacity:0.6,
                strokeWeight:2
            });

            line.setMap(googleMap);
        })
    })
}

function showZipPolygonFromXml(zip, icon) {
    $xml = $($.parseXML(zip.map));

    $xml.find("zip").each(function () {

        var marker = new google.maps.Marker({
            position:new google.maps.LatLng($(this).attr('lat'), $(this).attr('lng')),
            icon:icon,
            map:googleMap,
            title: 'ZIP ' + $(this).attr('id')
        });

        if (zip.messages) {
            attachZipMarkerInfo(marker, getZipTable(zip));
        } else {
            attachZipMarkerInfo(marker, 'ZIP ' + $(this).attr('id'));
        }

        var points = [];
        $(this).children().each(function () {
            points.push(new google.maps.LatLng($(this).attr('lat'), $(this).attr('lng')));
        });

        var polygon = new google.maps.Polygon({
            paths:points,
            strokeColor:'#86B5D9',
            strokeOpacity:0.8,
            strokeWeight:2,
            fillColor: '#86B5D9',
            fillOpacity: 0.2
        });

        polygon.setMap(googleMap);
        google.maps.event.addListener(polygon, 'click', function (event) {
            this.setMap(null);
            (this.fillOpacity == 0.2) ? this.fillOpacity = 0.6 : this.fillOpacity = 0.2;
            this.setMap(googleMap);
        });

        if (!zip.messages) {
            attachZipPolygonInfo(polygon, marker, 'ZIP ' + $(this).attr('id'));
        }
    })
}
