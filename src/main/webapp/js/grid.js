$(function() {

	$.extend($.jgrid.defaults, {
				datatype: 'json',
				jsonReader : {
					repeatitems:false,
					total: function(result) {
						//Total number of pages
						return Math.ceil(result.total / result.max);
					},
					records: function(result) {
						//Total number of records
						return result.total;
					}
				},
//				prmNames: {
//					page: "page.page",
//					rows: "page.size",
//					sort: "page.sort",
//					order: "page.sort.dir"
//				},
				sortname: 'regionId',
				sortorder: 'asc',
				height: 'auto',
				viewrecords: true,
				rowList: [10, 20, 50, 100],
				altRows: true,
				loadError: function(xhr, status, error) {
					alert(error);
				}
			    });

	$.extend($.jgrid.edit, {
				closeAfterEdit: true,
				closeAfterAdd: true,
				ajaxEditOptions: { contentType: "application/json" },
				mtype: 'PUT',
				serializeEditData: function(data) {
					delete data.oper;
					return JSON.stringify(data);
				}
			});
	$.extend($.jgrid.del, {
				mtype: 'DELETE',
				serializeDelData: function() {
					return "";
				}
			});

	var editOptions = {
		onclickSubmit: function(params, postdata) {
			params.url = URL + '/' + postdata.id;
		}
	};
	var addOptions = {mtype: "POST"};
	var delOptions = {
		onclickSubmit: function(params, postdata) {
			params.url = URL + '/' + postdata;
		}
	};

	var URL = 'rest/regions';
	var options = {
		url: URL,
		editurl: URL,
		colModel:[
			{
				name:'regionId',
				label: 'Id',
				index: 'regionId',
				formatter:'integer',
				width: 35,
				editable: true,
				editoptions: {disabled: true, size:5}
			},
			{
				name:'regionCode',
				label: 'Code',
				index: 'regionCode',
				width: 55,
				editable: true,
				editrules: {required: true}
			},
            {
                name:'state',
                label: 'State',
                index: 'state',
                width: 30,
                editable: true,
                editrules: {required: true}
            },
            {
                name:'franchise.franchiseId',
                label: 'Franchise Id',
                index: 'franchise.franchiseId',
                width: 75,
                editable: true,
                editrules: {required: true},
                search : false
            },
            {
                name:'franchise.franchiseName',
                label: 'Franchise Name',
                index: 'franchise.franchiseName',
                width: 120,
                editable: true,
                editrules: {required: true},
                search : false
            },
            {
                name:'regionMake',
                label: 'Make',
                index: 'regionMake',
                width: 60,
                editable: true,
                editrules: {required: true}
            },
            {
                name:'radius',
                label: 'Radius',
                index: 'radius',
                width: 40,
                editable: true,
                editrules: {required: true}
            },
        ],
		caption: "Regions",
		pager : '#pager',
		height: 'auto',
//        ondblClickRow: function(id) {
//            jQuery(this).jqGrid('editGridRow', id, editOptions);
//        },
        onSelectRow: function(id) {
            var region = $(this).getRowData(id);
            hideMapDiv();
            getRegion(region.regionId);
//            try {
//                var region = $(this).getRowData(id);
//                var custom = $(this).jqGrid("getGridParam", "userData");
//                showMap(region, custom[id].zips, custom[id].franchise);
//            }
//            catch(e) {
//                alert(e);
//            }
        }
    };

	$("#grid")
			.jqGrid(options)
			.navGrid('#pager',
			{edit:false, add:false, del:false, search:true}, //options
			editOptions,
			addOptions,
			delOptions,
			{} // search options
	);

});

function getRegion(regionId) {
    $.ajax({
        url: 'rest/region',
        cache: false,
        type: "GET",
        async: false,
        data: ({
            regionId: regionId
        }),
        error: function(xhr, textStatus, thrownError) {
            alert((xhr.status != 0 ? xhr.status + " " : "") + xhr.statusText);
        },
        success: function(response, status, xhr) {
            try {
                showMapDiv();
                showMap(response, response.zips, response.franchise);
            } catch(e) {
                alert(e);
            }
        }
    });
}
