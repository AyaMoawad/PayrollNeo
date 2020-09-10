
var controller = (function () {
    queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const code = urlParams.get('code');
    const theCode = urlParams.get('theCode');
    jQuery(document).ready(function ($) {
        $("#buttonSubmit").mouseenter(function () {
            $(this).removeClass('btn-primary');
            $(this).addClass('bg-success');
        });

        $("#buttonSubmit").mouseout(function () {
            $(this).removeClass('bg-success');
            $(this).addClass('btn-primary');
        });
        $("#modalOkButton").click(function (e) {
            location='showEditTableCompany.html?code='+theCode;
        });
        $("#buttonSubmit").click(function (e) {
            $('#success_msg').attr('hidden','');
            $('#fail_msg').attr('hidden','');

            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                type: "get", 
                url: "http://localhost:8080/Payroll/companyStructure/deleteCompanyStructure",
                data: {
                    code: code
                },
                success: function (response) {
                    $('#success_msg').removeAttr('hidden');
                    
                },
                error: function (xhr) {
                    $('#fail_msg').removeAttr('hidden');
                    console.log(xhr);
                }
            });
            
        
        
        });
        
    });
})();





