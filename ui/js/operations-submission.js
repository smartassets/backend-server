jQuery(function ($) {
    var SERVER_API = "localhost";
    var SERVER_PORT = "8082";
    var counter = 0;
    var operationId;
    $("#submit-machine-rate").click(function () {
        if (counter == 0) {
            // First time creation
            var postData = {
                "employee": {
                    "id": "" + $("#employee-selection-operation option:selected").val() + ""
                },
                "machine": {
                    "machineNumber": "" + $("#machine option:selected").val() + ""
                },
                "inputMaterialType": "" + $("#material option:selected").val() + "",
                "totalInputMaterialQuantity": $("#overall-material").val(),
                "sucessRateMaterial": $("#success-rate").val(),
                "defectRateMaterial": $("#defect-rate").val()
            };

            httpClient.jsonPost("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations", postData, function (xhr) {
                var responseObj = JSON.parse(xhr.responseText);
                console.log(responseObj);
                operationId = responseObj.id;
            });
        } else {
            var putData = {
                "sucessRateMaterial": $("#success-rate").val(),
                "defectRateMaterial": $("#defect-rate").val()
            };
            httpClient.jsonPut("http://" + SERVER_API + ":" + SERVER_PORT + "/api/operations/"+operationId, putData, function (xhr) {
                var responseObj = JSON.parse(xhr.responseText);
                console.log(responseObj);
            });
        }
        $("#defect-rate").val("");
        $("#success-rate").val("");
        counter++;
    });

});
