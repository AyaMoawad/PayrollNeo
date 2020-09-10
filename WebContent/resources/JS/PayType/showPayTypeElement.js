var controller = (function () {
    queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const code = urlParams.get('code');

    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: "http://localhost:8080/Payroll/payType/getPayType",
        data: {
            code: code
        },
        success: function (response) {
            $('#payType_code').val(response.code);
            $('#payType_name').val(response.name);
            $('#start_date').val(response.startDate);
            $('#end_date').val(response.endDate);
            $('#interval').val(response.interval);
            $('#type').val(response.type);
            $('#inputValue').val(response.inputValue);
            $('#Taxes').val(response.taxes);

            if(response.unit !=null){
                var newUnit=response.unit*100;
                $('#unit').val(newUnit);
                $('#payTypeUnit').removeAttr('hidden');
            }
            $('#showPayType').removeAttr('hidden');

        },
        error: function (xhr) {
            $('#payTypeModal').modal('show');
            $('#showPayType').attr('hidden','');
            console.log(xhr);
        }
    });


    jQuery(document).ready(function ($) {
        queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const code = urlParams.get('code');

        $("#modalOkButton").click(function (e) {
            location = 'editPayType.html';
        });

        $('#edit').mouseenter(function () {
            $('#edit-text').removeAttr('hidden');
        });

        $('#edit').mouseout(function () {
            $('#edit-text').attr('hidden','');
        });

        $('#edit').click(function (e) {
            window.location = "editpayTypeData.html?code="+code;
        });

        $('#copy').mouseenter(function () {
            $('#copy-text').removeAttr('hidden');
        });

        $('#copy').mouseout(function () {
            $('#copy-text').attr('hidden','');
        });

        $('#copy').click(function (e) {
            window.location = "copyPayType.html?code="+code;
        });

        $('#delimit').mouseenter(function () {
            $('#delimit-text').removeAttr('hidden');
        });

        $('#delimit').mouseout(function () {
            $('#delimit-text').attr('hidden','');
        });

        $('#delimit').click(function (e) {
            window.location = "delimitPayType.html?code="+code;
        });

        $('#delete').mouseenter(function () {
            $('#delete-text').removeAttr('hidden');
        });

        $('#delete').mouseout(function () {
            $('#delete-text').attr('hidden','');
        });
        $('#delete').click(function (e) {
            window.location = "deletePayType.html?code="+code;
        });
        $('#modalOkButton').click(function (e) {
            location = 'editPayType.html';
        });
    
    });

})();
