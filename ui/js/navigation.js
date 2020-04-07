var SERVER_API = "localhost"
var SERVER_PORT = "8082"
function hideAllContents() {
    $(".main-wrapper").hide();
    $(".container-table100").hide();
    $(".container").hide();
    $(".container-charts").hide();
    $(".operations-dashboard").hide();
}

function loadContainerWithData() {
    loadMachines("#machine");
    loadEmployees("#employee-selection-operation");
}

function loadMachines(containerId) {
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/machines", null, function (xhr) {
        var responseObj = JSON.parse(xhr.responseText);
        // console.log(responseObj);
        $.each(responseObj, function (i, item) {
            $(containerId).append(`<option value="${item.machineNumber}"> 
                                    ID: ${item.machineNumber} | ${item.location}
                                 </option>`)
        });
    });
}

function loadEmployees(containerId) {
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/employees", null, function (xhr) {
        var responseObj = JSON.parse(xhr.responseText);
        $.each(responseObj, function (i, item) {
            $(containerId).append(`<option value="${item.id}"> 
                                    ID: ${item.id} | ${item.name}
                                 </option>`)
        });
    });
}




function loadEmployeesInDashboardTable() {
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/employees", null, function (xhr) {
        var responseObj = JSON.parse(xhr.responseText);
        $.each(responseObj, function (i, item) {
            console.log(item);
            httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations?searchType=employee&criteria=" + item.id + "&statistics=true", null, function (xhr) {
                var operationsForEmployee = JSON.parse(xhr.responseText);
                console.log(operationsForEmployee[0]);
                $("#employees").append(` <tr>
                                        <td>${item.name}</td>
                                        <td>${operationsForEmployee[0].sucessRateMaterial}</td>
                                        <td>${operationsForEmployee[0].defectRateMaterial}</td>
                                        <td>${operationsForEmployee[0].totalWorkingHours}</td>
                                    </tr>`)
            });

        });
    });
}

function loadMaterialStatistics() {
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations?statistics=true", null, function (xhr) {
        var operationSummary = JSON.parse(xhr.responseText);
        console.log(operationSummary[0]);
        $("#succ-rate-perc").text(`${Math.floor((operationSummary[0].sucessRateMaterial / operationSummary[0].totalInputMaterialQuantity) * 100.0)}%`);
        $("#def-rate-perc").text(`${Math.floor((operationSummary[0].defectRateMaterial / operationSummary[0].totalInputMaterialQuantity) * 100.0)}%`);
        $("#total-count").text(`${operationSummary[0].totalInputMaterialQuantity}`);
        httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations", null, function (xhr) {
            var totalOps = JSON.parse(xhr.responseText);
            $("#machine-ops-count").text(`${totalOps.length}`);
        });

    });
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations", null, function (xhr) {
        var operationSummary = JSON.parse(xhr.responseText);
        console.log(operationSummary.length);

    });
}

hideAllContents();
loadContainerWithData();
loadEmployeesInDashboardTable();
loadMaterialStatistics();
$(".operations-dashboard").show();
$("#device-monitoring-dashboards").click(function () {
    hideAllContents();
    $(".container-table100").show();
});
$(".about").click(function () {
    hideAllContents();
    $(".main-wrapper").show();
});

$("#operations").click(function () {
    hideAllContents();
    $(".container").show();
});

$("#charts-nav").click(function () {
    hideAllContents();
    loadEmployees("#employee-selection-chart");
    loadMachines("#machine-selection-chart");
    $("#machine-selection-chart").find("option:gt(0)").remove();
    $("#employee-selection-chart").find("option:gt(0)").remove();
    materialsChartByDate();
    $(".container-charts").show();
});

$("#machine-operations-dashboards").click(function () {
    hideAllContents();
    totalOperationsChart();
    $(".operations-dashboard").show();
});