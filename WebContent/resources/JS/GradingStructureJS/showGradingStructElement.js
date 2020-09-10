var controller = (function () {
    queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const grade = urlParams.get('grade');

    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: "http://localhost:8080/Payroll/GradingStruct/getGradingAndSalary",
        data: {
            grade: grade
        },
        success: function (response) {
            $('#gradingStruct_grade').val(response.grade);
            $('#gradingStruct_level').val(response.level);
            $('#start_date').val(response.startDate);
            $('#end_date').val(response.endDate);
            $('#grading_min').val(response.min);
            $('#grading_mid').val(response.mid);
            $('#grading_max').val(response.max);
            $('#basicSalary').val(response.basicSalary);
            $('#showGradingStruct').removeAttr('hidden');
        },
        error: function (xhr) {
            $('#gradingStructModal').modal('show');
            $('#showGradingStruct').attr('hidden','');
            console.log(xhr);
        }
    });
    

    jQuery(document).ready(function ($) {
        queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const grade = urlParams.get('grade');

        $("#modalOkButton").click(function (e) {
            location = 'editGradingStructure.html';
        });

        $('#edit').mouseenter(function () {
            $('#edit-text').removeAttr('hidden');
        });

        $('#edit').mouseout(function () {
            $('#edit-text').attr('hidden','');
        });

        $('#edit').click(function (e) {
            window.location = "editGradingData.html?grade="+grade;
        });

        $('#copy').mouseenter(function () {
            $('#copy-text').removeAttr('hidden');
        });

        $('#copy').mouseout(function () {
            $('#copy-text').attr('hidden','');
        });

        $('#copy').click(function (e) {
            window.location = "copyGradingStruct.html?grade="+grade;
        });

        $('#delimit').mouseenter(function () {
            $('#delimit-text').removeAttr('hidden');
        });

        $('#delimit').mouseout(function () {
            $('#delimit-text').attr('hidden','');
        });

        $('#delimit').click(function (e) {
            window.location = "delimitGradingStruct.html?grade="+grade;
        });

        $('#delete').mouseenter(function () {
            $('#delete-text').removeAttr('hidden');
        });

        $('#delete').mouseout(function () {
            $('#delete-text').attr('hidden','');
        });
        $('#delete').click(function (e) {
            window.location = "deleteGradingStruct.html?grade="+grade;
        });
    });

})();
