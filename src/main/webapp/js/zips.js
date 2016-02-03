$(document).ready(function() {

    $("#zips-dialog").dialog({
        autoOpen: false,
        height: 250,
        width: 350,
        modal: true,
        buttons: {
            OK: function() {
                var valid = true;
//                allFields.removeClass("ui-state-error");
//                valid = valid && checkLength(name, "username", 3, 16);
//                valid = valid && checkLength(email, "email", 6, 80);
                if (valid) {
                    var franchiseId = $('#zips-franchise-id').val();
                    var radius = $('#zips-radius').val();
                    getAllZips(franchiseId, radius);

                    $(this).dialog("close");
                }
            },
            Cancel: function() {
                $(this).dialog("close");
            }
        },
        close: function() {
//            allFields.val("").removeClass("ui-state-error");
        }
    });

    $("#show-zips-dialog")
        .button()
        .click(function() {
            $("#zips-dialog").dialog("open");

            var grid = $('#grid');
            var rowId = grid.jqGrid('getGridParam', 'selrow');
            if (rowId) {
                var franchiseId = grid.jqGrid('getCell', rowId, 'franchise.franchiseId');
                $('#zips-franchise-id').val(franchiseId);
            } else {
                $('#zips-franchise-id').val('');
            }
        });

});

function getAllZips(franchiseId, radius) {
    $.ajax({
        url: 'rest/zips',
        cache: false,
        type: "GET",
        async: false,
        data: ({
            franchiseId: franchiseId,
            radius: radius
        }),
        error: function(xhr, textStatus, thrownError) {
            alert((xhr.status != 0 ? xhr.status + " " : "") + xhr.statusText);
        },
        success: function(response, status, xhr) {
            try {
                showAllZips(response);
            } catch(e) {
                alert(e);
            }
        }
    });
}

function showAllZips(response) {
    showAllZipsFranchise(response);
    showAllZipsZips(response);
}

function showAllZipsFranchise(response) {
    var franchise = response.franchise;

    var mapOptions = {
        center:new google.maps.LatLng(franchise.latitude, franchise.longitude),
        zoom:8
    };
    googleMap = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(franchise.latitude, franchise.longitude),
        icon:'http://maps.google.com/mapfiles/ms/micons/dollar.png',
        map:googleMap,
        title: 'Franchise Id ' + response.franchiseId
    });

    attachFranchiseMarkerInfo(marker, getRegionTable(null, franchise));

    var circleOptions = {
        map: googleMap,
        strokeColor: '#86B5D9',
        strokeOpacity: 1,
        strokeWeight: 2,
        fillColor: '#86B5D9',
        fillOpacity: 0,
        center: new google.maps.LatLng(franchise.latitude, franchise.longitude),
        radius: response.radius * 1610
    };

    new google.maps.Circle(circleOptions);
}

function showAllZipsZips(response) {
    var zips = response.zips;

    for (var i = 0; i < zips.length; i++) {
        var zip = zips[i];
        showZipPolygonFromXml(zip, 'http://maps.google.com/mapfiles/ms/icons/lightblue.png');
    }
}
