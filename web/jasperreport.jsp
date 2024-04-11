<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<head>
    <title>TODO supply a title</title>
    <!-- Latest compiled and minified CSS -->

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./plugins/fontawesome-free/css/all.min.css">
    <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet" href="./plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
    <link rel="stylesheet" href="./plugins/icheck-bootstrap/icheck-bootstrap.min.css">
    <link rel="stylesheet" href="./plugins/jqvmap/jqvmap.min.css">
    <link rel="stylesheet" href="./dist/css/adminlte.min.css">
    <link rel="stylesheet" href="./plugins/overlayScrollbars/css/OverlayScrollbars.min.css">
    <link rel="stylesheet" href="./plugins/daterangepicker/daterangepicker.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.carousel.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.theme.default.css">
    <link rel="stylesheet" href="./dist/css/myStyle.css">
    <link rel="stylesheet" type="text/css" href="./dist/css/mobileResponsive.css">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="./plugins/jquery/jquery.min.js"></script>
    <script src="./plugins/jquery-ui/jquery-ui.min.js"></script>
    <script src="./plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="./dist/js/adminlte.min.js"></script>
    <script src="./plugins/flot/jquery.flot.js"></script> 
    <script src="./plugins/flot/plugins/jquery.flot.resize.js"></script> 
    <script src="./plugins/flot/plugins/jquery.flot.pie.js"></script> 
    <script src="./plugins/chart.js/Chart.min.js"></script> 
    <!-- <script src="https://code.jquery.com/jquery-3.5.1.js"></script> -->
    <script src="./dist/js/myJS.js" type="text/javascript"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
    <link href = "https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css"
          rel = "stylesheet">
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.21/css/jquery.dataTables.css">

    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.js">
    </script>
</head>


<script>

    $(function () {
        $("#mytable").dataTable();


        setTimeout(function () {
            $('#message').fadeOut('fast');
        }, 10000);
    });
    $(function () {
        $("#searchReportStatusName").autocomplete({
            source: function (request, response) {
                var random = document.getElementById("searchReportStatusName").value;
                $.ajax({
                    url: "UploadServlet",
                    dataType: "json",
                    data: {action1: "getReportStatus", str: random},
                    success: function (data) {
                        console.log(data);
                        response(data.list);
                    }, error: function (error) {
                        console.log(error.responseText);
                        response(error.responseText);
                    }
                });
            },
            select: function (events, ui) {
                console.log(ui);
                $('#searchReportStatusName').val(ui.item.label); // display the selected text
                return false;
            }
        });
    });
    $(function () {
        $("#searchReportName").autocomplete({
            source: function (request, response) {
                var random = document.getElementById("searchReportName").value;
                $.ajax({
                    url: "UploadServlet",
                    dataType: "json",
                    data: {action1: "getSearchReportName", str: random},
                    success: function (data) {
                        console.log(data);
                        response(data.list);
                    }, error: function (error) {
                        console.log(error.responseText);
                        response(error.responseText);
                    }
                });
            },
            select: function (events, ui) {
                console.log(ui);
                $('#searchReportName').val(ui.item.label); // display the selected text
                return false;
            }
        });
        $("#searchType").autocomplete({
            source: function (request, response) {
                var random = document.getElementById("searchType").value;
                $.ajax({
                    url: "UploadServlet",
                    dataType: "json",
                    data: {action1: "getSearchType", str: random},
                    success: function (data) {
                        console.log(data);
                        response(data.list);
                    }, error: function (error) {
                        console.log(error.responseText);
                        response(error.responseText);
                    }
                });
            },
            select: function (events, ui) {
                console.log(ui);
                $('#searchType').val(ui.item.label); // display the selected text
                return false;
            }
        });
        $(document).on('keydown', '.myautocomplete', function () {
            var id = this.id;
            var random = this.value;
            var count = id.substring(13, 20);
            var previous_status = $('#previous_status' + count).val();

            $('#' + id).autocomplete({
                source: function (request, response) {
                    $.ajax({
                        url: "UploadServlet",
                        dataType: "json",
                        data: {
                            action1: "getStatus",
                            str: random,
                            previous_status: previous_status
                        },
                        success: function (data) {
                            console.log(data);
                            response(data.list);

                        },
                        error: function (error) {
                            console.log(error.responseText);
                            response(error.responseText);
                        }
                    });
                },
                select: function (events, ui) {
                    console.log(ui);
                    $(this).val(ui.item.label);
                    return false;
                }
            });
        });



    });

    function makeEditable(id) {
        document.getElementById("report_status_name").disabled = false;
        document.getElementById("report_name").disabled = false;

        document.getElementById("save").disabled = false;
        if (id == 'new') {
            $("#message").html("");
            document.getElementById("report_status_id").value = "";
            document.getElementById("report_table_id").value = "";
            document.getElementById("edit").disabled = true;
            document.getElementById("delete").disabled = true;
            document.getElementById("save_As").disabled = true;
            document.getElementById("report_status_name").focus();
            document.getElementById("type").focus();
            document.getElementById("report_name").focus();
            setDefaultColor(document.getElementById("noOfRowsTraversed").value, 4);
        }
        if (id == 'edit') {
            document.getElementById("save_As").disabled = false;
            document.getElementById("delete").disabled = false;
            document.getElementById("report_status_name").focus();
            document.getElementById("type").focus();
            document.getElementById("report_name").focus();
        }
    }

    function updateStatus(count, report_table_id) {
        var report_status = $('#report_status' + count).val();
        var previous_status = $('#previous_status' + count).val();
        $.ajax({
            url: "UploadServlet",
            dataType: "json",
            data: {task: "Save", report_table_id: report_table_id, report_status: report_status},
            success: function (data) {
                console.log(data.message);
                alert(data.message);
                window.location.reload();
            }
        });
    }

    function setStatus(id) {
        if (id == 'save') {
            document.getElementById("clickedButton").value = "Save";
        } else if (id == 'save_As') {
            document.getElementById("clickedButton").value = "Save AS New";
        } else
            document.getElementById("clickedButton").value = "Delete";
    }

    function myLeftTrim(str) {
        var beginIndex = 0;
        for (var i = 0; i < str.length; i++) {
            if (str.charAt(i) == ' ')
                beginIndex++;
            else
                break;
        }
        return str.substring(beginIndex, str.length);
    }

    function verify() {
        var result;
        if (document.getElementById("clickedButton").value == 'Save' || document.getElementById("clickedButton").value == 'Save AS New') {
            var report_status_name = document.getElementById("report_status_name").value;
            var type = document.getElementById("type").value;
            var report_name = document.getElementById("report_name").value;

            if (myLeftTrim(report_status_name).length == 0) {
                $("#message").html('<div class="col-md-12 text-center"><label style="color:red"><b>Report Status Name is required...</b></label></div>');
                document.getElementById("report_status_name").focus();
                return  false;
            }
            if (myLeftTrim(type).length == 0) {
                $("#message").html('<div class="col-md-12 text-center"><label style="color:red"><b>Report Type is required...</b></label></div>');
                document.getElementById("type").focus();
                return  false;
            }
            if (myLeftTrim(report_name).length == 0) {
                $("#message").html('<div class="col-md-12 text-center"><label style="color:red"><b>Report Name is required...</b></label></div>');
                document.getElementById("report_name").focus();
                return  false;
            }
            if (result == false)
            {

            } else {
                result = true;

            }
            if (document.getElementById("clickedButton").value == 'Save AS New') {
                result = confirm("Are you sure you want to save it as New record?")
                return result;
            }
        } else
            result = confirm("Are you sure you want to delete this record?")
        return result;
    }

    function verifySearch() {
        var result;
        if (document.getElementById("clickedButton").value === 'SEARCH') {
            var searchReportName = document.getElementById("searchReportName").value;
            if (myLeftTrim(searchReportName).length === 0) {
                document.getElementById("org_msg").innerHTML = "<b>Report Name is required...</b>";
                document.getElementById("searchReportName").focus();
                return false; // code to stop from submitting the form2.
            }
        }
    }

    function popupWindow(url, windowName)
    {
        var windowfeatures = "left=50, top=50, width=1000, height=500, resizable=no, scrollbars=yes, location=0, menubar=no, status=no, dependent=yes";
        return window.open(url, windowName, windowfeatures)
    }

    function fillColumn(id, count) {
        $('#report_table_id').val(id);
        $('#report_name').val($("#" + count + '2').html());
        $('#edit').attr('disabled', false);
        $('#delete').attr('disabled', false);
    }
    
    function downloadFile(id) {
        window.location.href = 'fileServletController?id=' + id;
    }
   
</script>

<section>
    <div class="container-fluid page_heading sectionPadding35">
        <h1>Report</h1>
    </div>
</section>

<section class="marginTop30">
    <div class="container organizationBox">
        <div class="headBox">
            <h5 class="">Search Engine</h5>
        </div>
        <form name="form1" method="POST" action="UploadServlet" onsubmit="return verifySearch();" >
            <div class="row mt-3">
                <div class="col-md-3">
                    <div class="form-group mb-md-0">
                        <label>Report Status Name</label>
                        <input class="form-control myInput searchInput1 w-100" type="text" id="searchReportStatusName"
                               name="searchReportStatusName" value="${searchReportStatusName}" size="150" >
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="form-group mb-md-0">

                        <label>Report Name</label>
                        <input class="form-control myInput searchInput1 w-100" type="text" id="searchReportName"
                               name="searchReportName" value="${searchReportName}" size="150" >
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="form-group mb-md-0">

                        <label>Report Type</label>
                        <input class="form-control myInput searchInput1 w-100" type="text" id="searchType"
                               name="searchType" value="${searchType}" size="150" >
                    </div>
                </div>
            </div>
            <hr>
            <div class="row">
                <div class="col-md-12 text-center">
                    <input type="submit" class="btn normalBtn" id="submit" name="submit" value="SEARCH RECORDS"">
                </div>
            </div>
        </form>
    </div>
</section>

<section class="marginTop30 ">
    <div class="container organizationBox">
        <div class="headBox">
            <h5 class="">Search List</h5>
        </div>
        <div class="row mt-3 myTable">
            <div class="col-md-12">
                <div class="table-responsive verticleScroll">
                    <table class="table table-striped table-bordered" id="mytable" style="width:100%" data-page-length='6'>
                        <thead>
                            <tr>                                
                                <th>S.No.</th>
                                <th>Report Name</th>
                                <th>Report Status</th>
                                <th>Report Type</th>
                                <th>Update Status</th>
                                <th></th>
                                <th>Sample File</th>
                            </tr>
                        </thead>
                        <tbody>                                
                            <c:forEach var="beanType" items="${requestScope['reportList']}" varStatus="loopCounter">
                                <tr>
                                    <td>${loopCounter.count }</td>
                                    <td id="${loopCounter.count }2">${beanType.report_name}</td>
                                    <td id="${loopCounter.count }4">${beanType.report_status_name}
                                        <input type="hidden"  name="previous_status" id="previous_status${loopCounter.count }" value="${beanType.report_status_name}">
                                    </td>
                                    <td id="${loopCounter.count }3">${beanType.type}</td>

                                    <c:if test="${beanType.type=='spreadsheet'}">
                                          
                                    <c:if test="${beanType.report_status_name=='Report Generated'}">
                                        <td></td>
                                        <td></td>
                                    </c:if>
                                    <c:if test="${beanType.report_status_name!='Report Generated'}">
                                        <td>
                                            <input type="text"  disabled  class="myautocomplete" id="report_status${loopCounter.count }" placeholder="Select status">
<!--                                            <a class="btn btn-success"  disabled  onclick="updateStatus('${loopCounter.count }', '${beanType.report_table_id}')">Update</a>-->
                                        </td>
                                        <td>
                                            <form method="Post" action="UploadServlet" enctype="multipart/form-data">
                                                <input type ="file" disabled  id="myfile" name = "file" >
                                                <input type ="hidden" id="report_name" name = "report_name" value="${beanType.report_name}">
                                                <input type ="submit"  disabled  class="btn btn-primary" id="file" name="task1" value="Send Text File" >
                                            </form>
                                        </td>
                                    </c:if>        
                                <td id="${loopCounter.count }8"> <button   disabled  class="btn btn-primary" id="addBtn" onclick="downloadFile(${beanType.report_table_id});">View</button></td>
                                   
                                    
                                    
                                    
                                    </c:if>
                                    <c:if test="${beanType.type!='spreadsheet'}">
                                           
                                    <c:if test="${beanType.report_status_name=='Report Generated'}">
                                        <td></td>
                                        <td></td>
                                    </c:if>
                                    <c:if test="${beanType.report_status_name!='Report Generated'}">
                                        <td>
                                            <input type="text" class="myautocomplete" id="report_status${loopCounter.count }" placeholder="Select status">
                                            <a class="btn btn-success" onclick="updateStatus('${loopCounter.count }', '${beanType.report_table_id}')">Update</a>
                                        </td>
                                        <td>
                                            <form method="Post" action="UploadServlet" enctype="multipart/form-data">
                                                <input type ="file" id="myfile" name = "file" >
                                                <input type ="hidden" id="report_name" name = "report_name" value="${beanType.report_name}">
                                                <input type ="submit" class="btn btn-primary" id="file" name="task1" value="Send Text File" >
                                            </form>
                                        </td>
                                    </c:if>        
                                    <td id="${loopCounter.count }8"> <button class="btn btn-primary" id="addBtn" onclick="downloadFile(${beanType.report_table_id});">View</button></td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>    
            </div>
        </div>
    </div>
</section>

                    
</html>

