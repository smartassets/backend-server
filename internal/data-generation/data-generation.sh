function generate_random() {
    local floor=$1
    local ceiling=$2
    RANGE=$(($ceiling-$floor+1));
    RESULT=$RANDOM;
    let "RESULT %= $RANGE";
    RESULT=$(($RESULT+$floor));

    echo $RESULT
}

function generate_machine_operation() {
    for i in $(seq 1 15); do 
        # rpmUpperSpindle - 30000 - 35000
        rpmUpperSpindle=$(generate_random 30000 35000)
        # rpmLowerSpindle - 15000 - 20000
        rpmLowerSpindle=$(generate_random 15000 20000)
        # pressureUpperSpindle - 3 - 10 kp
        pressureUpperSpindle=$(generate_random 3 10)
        # polishingLiquidTemp - 25 - 45 C
        polishingLiquidTemp=$(generate_random 25 45)
        # oscilation_edge - 15 - 25 gradusa
        oscilation_edge=$(generate_random 15 25)
        # detail timer - 25 - 35m
        oepration_timer=$(generate_random 25 40)

        # employee - 1 - 10
        employee=$(generate_random 1 10)
        # mahcine - 1 - 15
        machine=$(generate_random 1 15)
        # total material - 150 - 250
        total_material=$(generate_random 150 250)
        # defect rate - 1 - 8 %
        defect_rate=$(generate_random 1 8)
        # total work hours - 1 - 8h
        total_working_hours=$(generate_random 1 8)

        ((defect_rate_material=${total_material} * ${defect_rate}/100))
        ((success_rate_material=${total_material}-${defect_rate_material}))



        param_id=$(curl -d "{  \"rpmUpperSpindle\": ${rpmUpperSpindle},  \"rpmLowerSpindle\": ${rpmLowerSpindle},  \"pressureUpperSpindle\": ${pressureUpperSpindle},  \"polishingLiquidTemperature\": ${polishingLiquidTemp},  \"oscillationEdge\": ${oscilation_edge},  \"operationDetailTimer\": ${oepration_timer}}" -H 'Content-Type: application/json' -X POST http://localhost:8082/api/operations/parameters);

        curl -d "{    \"employee\": {        \"id\": \"${employee}\"    },    \"machine\": {        \"machineNumber\": \"100${i}\"    },    \"operationParameter\": {    \"id\": ${param_id}    },    \"inputMaterialType\": \"Lense Type A\",    \"totalInputMaterialQuantity\": ${total_material},    \"sucessRateMaterial\": ${success_rate_material},    \"defectRateMaterial\": ${defect_rate_material},    \"totalWorkingHours\": ${total_working_hours}}" -H 'Content-Type: application/json' -X POST http://localhost:8082/api/operations
    done
}


function generate_environment_data() {
    IFS=', ' read -r -a temperature_devices <<< "1e7a2a07-af24-428b-8cb5-3618c91e434d, 31e1f8e5-71a9-49ca-9ecb-dc4fa188185d, 43054cbb-e051-48df-9aee-ad9ef5506357, a2d1e42c-8f20-46f2-be4f-483a254adb93, a8090a03-fdb1-4059-a2e7-b6f7cb50035b"
    IFS=', ' read -r -a humidity_devices <<< "05587558-4ff3-46dc-990e-161c09b6ba9d, 40399e55-734a-4994-be95-4084146b1f6b, 40399e55-734a-4994-be95-4089676b1f6b"
    for i in ${temperature_devices[@]}; do
        temperature=$(generate_random 19 33)
        mosquitto_pub -h 127.0.0.1 -p 1883 -t temperature -q 2 -m "{\"device_id\":\"${i}\",\"data\":\"${temperature}\"}"
    done

    for i in ${humidity_devices[@]}; do
        humidity=$(generate_random 28 48)
        echo ""
        mosquitto_pub -h 127.0.0.1 -p 1883 -t humidity -q 2 -m "{\"device_id\":\"${i}\",\"data\":\"${humidity}\"}"
    done
}