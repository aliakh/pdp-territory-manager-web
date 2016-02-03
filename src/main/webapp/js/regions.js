var regionFranchiseId = null;
var regionRadius = null;

$(document).ready(function() {

    $("#create-region-apply")
        .button()
        .click(function() {
            if (regionFranchiseId == null || regionRadius == null) {
                alert('Region parameters are not set');
            } else {
                createOrUpdateRegion(regionFranchiseId, regionRadius, false);
            }

            $("#create-region-apply").hide();
            $("#create-region-cancel").hide();
        });
    $("#create-region-apply").hide();

    $("#create-region-cancel")
        .button()
        .click(function() {
            regionFranchiseId = null;
            regionRadius = null;

            $("#create-region-apply").hide();
            $("#create-region-cancel").hide();
        });
    $("#create-region-cancel").hide();

    $('#plus').toggle(function() {
        $("#message-dialog-body").slideDown(
            function() {
                $("#message-dialog").dialog("option", "height", 600);
                $("#message-dialog").dialog('option', 'position', 'center');
                $("#plus").text("[-]");
            }
        );
    }, function() {
        $("#message-dialog-body").slideUp(
            function() {
                $("#message-dialog").dialog("option", "height", 120);
                $("#message-dialog").dialog('option', 'position', 'center');
                $("#plus").text("[+]");
            }
        );
    });

    $("#message-dialog").dialog({
        autoOpen: false,
        height: 120,
        width: 580,
        modal: false,
        buttons: {
            OK: function() {
                $(this).dialog("close");
            }
        }
    });

    $("#create-region-dialog").dialog({
        autoOpen: false,
        height: 250,
        width: 400,
        modal: true,
        buttons: {
            Preview: function() {
                var valid = true;
//                allFields.removeClass("ui-state-error");
//                valid = valid && checkLength(name, "username", 3, 16);
//                valid = valid && checkLength(email, "email", 6, 80);
                if (valid) {
                    regionFranchiseId = $('#create-region-franchise-id').val();
                    regionRadius = $('#create-region-radius').val();
                    createOrUpdateRegion(regionFranchiseId, regionRadius, true);

                    $("#create-region-apply").show();
                    $("#create-region-cancel").show();

                    $(this).dialog("close");
                }
            },
            Cancel: function() {
                $(this).dialog("close");
            }
        },
        close: function() {
            $(".ui-autocomplete").hide();
//            allFields.val("").removeClass("ui-state-error");
        }
    });

    $("#show-create-region-dialog")
        .button()
        .click(function() {
            $("#create-region-dialog").dialog("open");

            var grid = $('#grid');
            var rowId = grid.jqGrid('getGridParam', 'selrow');
            if (rowId) {
                var franchiseId = grid.jqGrid('getCell', rowId, 'franchise.franchiseId');
                var franchiseName = grid.jqGrid('getCell', rowId, 'franchise.franchiseName');
                var make = grid.jqGrid('getCell', rowId, 'regionMake');
                var radius = grid.jqGrid('getCell', rowId, 'radius');

                $('#create-region-franchise-name').val(franchiseName + ' / ' + make);
                $('#create-region-franchise-id').val(franchiseId);
                $('#create-region-radius').val(radius);
            } else {
                $('#create-region-franchise-name').val('');
                $('#create-region-franchise-id').val(null);
                $('#create-region-radius').val(100);
            }
        });

    $("#create-region-franchise-name").autocomplete({
        source: function(request, response) {
            $.ajax({
                url: "rest/franchises",
                dataType: "JSON",
                data: {term: request.term},
                success: function(data) {
                    response($.map(data, function(item) {
                        return {
                            label: item.franchiseName + ' / ' + item.make,
                            value: item.franchiseId
                        }
                    }));
                }
            });
        },
        minLength: 3,
        select: function(event, ui) {
            event.preventDefault();
            $('#create-region-franchise-name').val(ui.item.label);
            $('#create-region-franchise-id').val(ui.item.value);
        },
        focus: function(event, ui) {
            event.preventDefault();
            $('#create-region-franchise-name').val(ui.item.label);
        },
        search: function() {
            $('#create-region-franchise-id').val(null);
        },
        open: function() {
            $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
        },
        close: function() {
            $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
        }
    });

    showMapDiv();
});

function createOrUpdateRegion(franchiseId, radius, test) {
    $.ajax({
        url: 'rest/regions',
        cache: false,
        type: "POST",
        async: false,
        data: ({
            franchiseId: franchiseId,
            radius: radius,
            test: test
        }),
        error: function(xhr, textStatus, thrownError) {
            alert((xhr.status != 0 ? xhr.status + " " : "") + xhr.statusText);
        },
        success: function(response, status, xhr) {
            try {
                showConflicts(response);
            } catch(e) {
                alert(e);
            }
        }
    });
}

function showConflicts(response) {
    showMapDiv();
    showConflictsFranchise(response);
    showConflictsZips(response);
    $('#grid').trigger('reloadGrid');

    $('#message-dialog-header').html(response.messages[0]);
    var html = '<br/>';
    for (var i = 1; i < response.messages.length; i++) {
        var message = response.messages[i];
        html += message + '<br/>';
    }
    $('#message-dialog-body').html(html);

    $("#message-dialog-body").hide();
    $("#plus").text("[+]")
    $("#plus").show();
    
    $("#message-dialog").dialog("option", "height", 120);
    $("#message-dialog").dialog('option', 'position', 'center');

    $("#message-dialog").dialog("open");
}

function showConflictsFranchise(response) {
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

function showConflictsZips(response) {
    var zips = response.zips;

    for (var i = 0; i < zips.length; i++) {
        var zip = zips[i];
        showZipPolygonFromXml(zip, getConflictsZipIcon(zip.zipType));
    }
}

function getConflictsZipIcon(zipType) {
    if (zipType == 'OWN') {
        return 'http://maps.google.com/mapfiles/ms/icons/blue.png';
    } else if (zipType == 'NEW') {
        return 'http://maps.google.com/mapfiles/ms/icons/lightblue.png';
    } else if (zipType == 'WON') {
        return 'http://maps.google.com/mapfiles/ms/icons/green.png';
    } else if (zipType == 'HOME_PINNED') {
        return 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png';
    } else if (zipType == 'LOST') {
        return 'http://maps.google.com/mapfiles/ms/icons/red.png';
    } else if (zipType == 'LOST_PINNED') {
        return 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
    } else {
        return 'http://maps.google.com/mapfiles/ms/micons/question.png';
    }
}
