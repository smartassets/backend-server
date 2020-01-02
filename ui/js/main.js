(function ($) {
    "use strict";
    $('.column100').on('mouseover',function(){
            var table1 = $(this).parent().parent().parent();
            var table2 = $(this).parent().parent();
            var verTable = $(table1).data('vertable')+"";
            var column = $(this).data('column') + "";

            $(table2).find("."+column).addClass('hov-column-'+ verTable);
            $(table1).find(".row100.head ."+column).addClass('hov-column-head-'+ verTable);
    });

    $('.column100').on('mouseout',function(){
            var table1 = $(this).parent().parent().parent();
            var table2 = $(this).parent().parent();
            var verTable = $(table1).data('vertable')+"";
            var column = $(this).data('column') + "";

            $(table2).find("."+column).removeClass('hov-column-'+ verTable);
            $(table1).find(".row100.head ."+column).removeClass('hov-column-head-'+ verTable);
    });

    var knownDevices = [];

    var SERVER_API=${API}

    var statusFunction = function(deviceId) {
            console.log("Calling: http://"+SERVER_API+":8888/api/devices?deviceId="+deviceId);
            httpClient.get("http://192.168.1.109:8888/api/devices?deviceId="+deviceId, null, function(xhr) {
                    var responseObj = JSON.parse( xhr.responseText );
                    console.log(JSON.parse( xhr.responseText ) );
                    $("#thetable").append(
                            "<tr class='row100 body'>" +
                            "<td class='cell100 column1'>"+deviceId+"</td>"+
                            "<td class='cell100 column2 description'>"+responseObj.description+"</td>" +
                            "<td class='cell100 column3 status'>"+responseObj.status+"</td>" +
                            "<td class='cell100 column4'><img src='images/reload.png' class='reload' id='"+deviceId+"'width='30px' height='30px'/><img src='images/x.png' id='"+deviceId+"' class='delete' width='30px' height='30px'/></td>" +
                            "</tr>");
            });
    };


    $.ajax({
            type: "GET",
            dataType: "json",
            url: "http://192.168.1.109:8888/api/devices",
            success: function(msg) {
                    $.each(msg.devices, function(i, item){
                            console.log(item);
                            statusFunction(item);
                            knownDevices.push(item);
                    });
            }
    });

    var addNewDeviceFunction = function(){
            httpClient.get("http://192.168.1.109:8888/api/devices", null, function(xhr) {
                    var responseObj = JSON.parse( xhr.responseText );
                    console.log(JSON.parse( xhr.responseText ) );
                    $.each(responseObj.devices, function(i, item){
                            console.log(item);
                            if (!knownDevices.includes(item)) {
                                    statusFunction(item);
                                    knownDevices.push(item);
                            }
                    });
            });
    }

    var updateAllRowsFunction = function(){
            $("tr.row100.body").each(function() {
                    var trLocator = $(this);
                    var deviceId = trLocator.find("td.cell100.column1").html();
                    httpClient.get("http://192.168.1.109:8888/api/devices?deviceId="+deviceId, null, function(xhr) {
                            var responseObj = JSON.parse( xhr.responseText );
                            console.log(JSON.parse( xhr.responseText ) );
                            trLocator.find("td.cell100.column3.status").text(responseObj.status);
                    });
            });
    }

    $("body").on("click", "img.reload", function(){
            console.log("Clicked reload: " + $(this).attr('id'));
            var imageLocator = $(this);
            httpClient.get("http://192.168.1.109:8888/api/devices?deviceId="+$(this).attr('id'), null, function(xhr) {
                    var responseObj = JSON.parse( xhr.responseText );
                    console.log(responseObj);
                    imageLocator.closest("tr").find("td.cell100.column3.status").text(responseObj.status);
                    console.log(imageLocator.closest("tr").find("td.cell100.column3.status"));
            });
    });

    $("body").on("click", "img.delete", function(){
            console.log("Clicked delete: " + $(this).attr('id'));
            var imageLocator = $(this);
            httpClient.delete("http://192.168.1.109:8888/api/devices?deviceId="+$(this).attr('id'), null, function(xhr) {
                    imageLocator.closest("tr").remove();
                    knownDevices.pop(imageLocator.attr('id'));
            });
    });

    setInterval(updateAllRowsFunction, 1500);
    setInterval(addNewDeviceFunction, 1500);
})(jQuery);
