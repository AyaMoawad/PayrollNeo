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
        url: "http://localhost:8080/Payroll/PayrollStruct/getPayrollStruct",
        data: {
            code: code
        },
        success: function (response) {
            $('#payrollStruct_code').val(response.code);
            $('#payrollStruct_name').val(response.name);
            $('#start_date').val(response.startDate);
            $('#end_date').val(response.endDate);
            $('#country').val(response.country);
            $('#interval').val(response.interval);
            $('#currency').val(response.currency);
            $('#payrollVal').val(response.payrollValuation);
            $('#company').val('');

            if(response.country==='Egypt'){
                $('#taxSettlement').val(response.taxSettlement);
            }else{
                $('#taxSettlementID').attr('hidden','');
             }

             if(response.payrollValuation==='fixed'){
                $('#noOfDays').val(response.noOfDays);
                $('#fixedDays').removeAttr('hidden','');
            }

            
            $('#showPayrollStruct').removeAttr('hidden');

        },
        error: function (xhr) {
            $('#payrollStructModal').modal('show');
            $('#showPayrollStruct').attr('hidden','');
            console.log(xhr);
        }
    });


    jQuery(document).ready(function ($) {
        queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const code = urlParams.get('code');
        
        $("#modalOkButton").click(function (e) {
            location = 'editPayrollStructure.html';
        });

        $('#edit').mouseenter(function () {
            $('#edit-text').removeAttr('hidden');
        });

        $('#edit').mouseout(function () {
            $('#edit-text').attr('hidden','');
        });

        $('#edit').click(function (e) {
            window.location = "editPayrollStructData.html?code="+code;
        });

        $('#copy').mouseenter(function () {
            $('#copy-text').removeAttr('hidden');
        });

        $('#copy').mouseout(function () {
            $('#copy-text').attr('hidden','');
        });

        $('#copy').click(function (e) {
            window.location = "copyPayrollStruct.html?code="+code;
        });

        $('#delimit').mouseenter(function () {
            $('#delimit-text').removeAttr('hidden');
        });

        $('#delimit').mouseout(function () {
            $('#delimit-text').attr('hidden','');
        });

        $('#delimit').click(function (e) {
            window.location = "delimitPayrollStruct.html?code="+code;
        });

        $('#delete').mouseenter(function () {
            $('#delete-text').removeAttr('hidden');
        });

        $('#delete').mouseout(function () {
            $('#delete-text').attr('hidden','');
        });
        $('#delete').click(function (e) {
            window.location = "deletePayrollStruct.html?code="+code;
        });
        $('#modalOkButton').click(function (e) {
            location = 'editPayrollStructure.html';
        });
    
    });

})();
