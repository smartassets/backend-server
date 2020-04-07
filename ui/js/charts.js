var groupBy = function (xs, key) {
    return xs.reduce(function (rv, x) {
        (rv[x[key]] = rv[x[key]] || []).push(x);
        return rv;
    }, {});
};
function materialsChartByDate() {
    var result = [];
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations", null, function (xhr) {
        var responseObj = JSON.parse(xhr.responseText);
        // console.log(responseObj);
        $.each(responseObj, function (i, item) {
            var itemDate = new Date(item.createdAt);
            var simpleItemDate = itemDate.getUTCDate() + "-" + (itemDate.getMonth() + 1) + "-" + itemDate.getFullYear();
            var data = {
                "successRate": item.sucessRateMaterial,
                "defectRate": item.defectRateMaterial,
                "date": simpleItemDate,
                "total": item.totalInputMaterialQuantity
            };
            result.push(data);

        });
        console.log(result);

        var groupedBy = groupBy(result, "date");
        console.log(groupedBy);

        var datesLabels = [];
        $.each(groupedBy, function (i, item) {
            datesLabels.push(i);
        });
        console.log(datesLabels);
        var materialsCount = [];
        $.each(groupedBy, function (i, item) {
            var totalMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                totalMaterialSumPerDate += item[index].total;
            }
            materialsCount.push(totalMaterialSumPerDate);
        });
        var defectRateCount = []
        $.each(groupedBy, function (i, item) {
            var defectRateMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                defectRateMaterialSumPerDate += item[index].defectRate;
            }
            defectRateCount.push(defectRateMaterialSumPerDate);
        });
        var successRateCount = []
        $.each(groupedBy, function (i, item) {
            var successRateMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                successRateMaterialSumPerDate += item[index].successRate;
            }
            successRateCount.push(successRateMaterialSumPerDate);
        });
        var ctx = $('#materialsByDate');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: datesLabels,
                datasets: [
                    {
                        label: '# material',
                        data: materialsCount,
                        borderWidth: 3,
                        fill: false,
                        borderColor: 'rgba(75, 192, 192, 0.2)'
                    },
                    {
                        label: '# success rate',
                        data: successRateCount,
                        borderWidth: 3,
                        fill: '-1',
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        type: "line"
                    },
                    {
                        label: '# defect rate',
                        data: defectRateCount,
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        borderWidth: 3,
                        // fill: false,
                        borderColor: 'rgba(255, 99, 132, 1)',
                        type: "line"
                    }
                ]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });
    });
}
function materialsByPerson() {
    var selectedEmployeeId = $("#employee-selection-chart").children("option:selected").val();
    var result = [];
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations?searchType=employee&criteria=" + selectedEmployeeId, null, function (xhr) {
        var responseObj = JSON.parse(xhr.responseText);
        // console.log(responseObj);
        $.each(responseObj, function (i, item) {
            var itemDate = new Date(item.createdAt);
            var simpleItemDate = `${itemDate.getUTCDate()}-${itemDate.getMonth() + 1}-${itemDate.getFullYear()}`
            var data = {
                "successRate": item.sucessRateMaterial,
                "defectRate": item.defectRateMaterial,
                "date": simpleItemDate,
                "total": item.totalInputMaterialQuantity
            };
            result.push(data);

        });
        console.log(result);

        var groupedBy = groupBy(result, "date");
        console.log(groupedBy);

        var datesLabels = [];
        $.each(groupedBy, function (i, item) {
            datesLabels.push(i);
        });
        console.log(datesLabels);
        var materialsCount = [];
        $.each(groupedBy, function (i, item) {
            var totalMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                totalMaterialSumPerDate += item[index].total;
            }
            materialsCount.push(totalMaterialSumPerDate);
        });
        var defectRateCount = []
        $.each(groupedBy, function (i, item) {
            var defectRateMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                defectRateMaterialSumPerDate += item[index].defectRate;
            }
            defectRateCount.push(defectRateMaterialSumPerDate);
        });
        var successRateCount = []
        $.each(groupedBy, function (i, item) {
            var successRateMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                successRateMaterialSumPerDate += item[index].successRate;
            }
            successRateCount.push(successRateMaterialSumPerDate);
        });
        var ctx = $('#materialsByPersonCanvas');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: datesLabels,
                datasets: [{
                    label: '# material',
                    data: materialsCount,
                    fill: false,
                    borderWidth: 3,
                    borderColor: 'rgba(75, 192, 192, 0.2)'
                },
                {
                    label: '# defect rate',
                    data: defectRateCount,
                    fill: false,
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    borderWidth: 3,
                    borderColor: 'rgba(255, 99, 132, 1)',
                    type: "bar"
                },
                {
                    label: '# success rate',
                    data: successRateCount,
                    borderWidth: 3,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)'
                }]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });
    });
}

function materialsByMachine() {
    var selectedMachineId = $("#machine-selection-chart").children("option:selected").val();
    var result = [];
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations?searchType=machine&criteria=" + selectedMachineId, null, function (xhr) {
        var responseObj = JSON.parse(xhr.responseText);
        // console.log(responseObj);
        $.each(responseObj, function (i, item) {
            var itemDate = new Date(item.createdAt);
            var simpleItemDate = `${itemDate.getUTCDate()}-${itemDate.getMonth() + 1}-${itemDate.getFullYear()}`
            var data = {
                "successRate": item.sucessRateMaterial,
                "defectRate": item.defectRateMaterial,
                "date": simpleItemDate,
                "total": item.totalInputMaterialQuantity
            };
            result.push(data);

        });
        console.log(result);

        var groupedBy = groupBy(result, "date");
        console.log(groupedBy);

        var datesLabels = [];
        $.each(groupedBy, function (i, item) {
            datesLabels.push(i);
        });
        console.log(datesLabels);
        var materialsCount = [];
        $.each(groupedBy, function (i, item) {
            var totalMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                totalMaterialSumPerDate += item[index].total;
            }
            materialsCount.push(totalMaterialSumPerDate);
        });
        var defectRateCount = []
        $.each(groupedBy, function (i, item) {
            var defectRateMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                defectRateMaterialSumPerDate += item[index].defectRate;
            }
            defectRateCount.push(defectRateMaterialSumPerDate);
        });
        var successRateCount = []
        $.each(groupedBy, function (i, item) {
            var successRateMaterialSumPerDate = 0;
            for (index = 0; index < item.length; index++) {
                successRateMaterialSumPerDate += item[index].successRate;
            }
            successRateCount.push(successRateMaterialSumPerDate);
        });
        var ctx = $('#materialsByMachineCanvas');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: datesLabels,
                datasets: [{
                    label: '# material',
                    data: materialsCount,
                    fill: false,
                    borderWidth: 3,
                    borderColor: 'rgba(75, 192, 192, 0.2)'
                },
                {
                    label: '# defect rate',
                    data: defectRateCount,
                    fill: false,
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    borderWidth: 3,
                    borderColor: 'rgba(255, 99, 132, 1)',
                    type: "bar"
                },
                {
                    label: '# success rate',
                    data: successRateCount,
                    borderWidth: 3,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)'
                }]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });
    });
}

function totalOperationsChart() {
    var result = [];
    httpClient.get("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations", null, function (xhr) {
        var responseObj = JSON.parse(xhr.responseText);
        // console.log(responseObj);
        $.each(responseObj, function (i, item) {
            var itemDate = new Date(item.createdAt);
            var simpleItemDate = itemDate.getUTCDate() + "-" + (itemDate.getMonth() + 1) + "-" + itemDate.getFullYear();
            var data = {
                "successRate": item.sucessRateMaterial,
                "date": simpleItemDate
            };
            result.push(data);

        });
        console.log(result);

        var groupedBy = groupBy(result, "date");
        console.log(groupedBy);

        var datesLabels = [];
        $.each(groupedBy, function (i, item) {
            datesLabels.push(i);
        });
        console.log(datesLabels);
        var operationsCount = [];
        $.each(groupedBy, function (i, item) {
            var totalOperationsCount = 0;
            for (index = 0; index < item.length; index++) {
                totalOperationsCount ++;
            }
            operationsCount.push(totalOperationsCount);
        });
        var ctx = $('#totalOperationsChart');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: datesLabels,
                datasets: [
                    {
                        label: '# operations',
                        data: operationsCount,
                        borderWidth: 3,
                        fill: false,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)'
                    },
                ]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });
    });
}