var controller = (function () {
    try {
        $('.js-datepicker').daterangepicker({
            "singleDatePicker": true,
            "showDropdowns": true,
            "autoUpdateInput": false,
            locale: {
                format: 'DD/MM/YYYY'
            },
        });
        var myCalendar = $('.js-datepicker');
        var isClick = 0;

        $(window).on('click', function () {
            isClick = 0;
        });

        $(myCalendar).on('apply.daterangepicker', function (ev, picker) {
            isClick = 0;
            $(this).val(picker.startDate.format('DD/MM/YYYY'));

        });

        $('.js-btn-calendar').on('click', function (e) {
            e.stopPropagation();

            if (isClick === 1) isClick = 0;
            else if (isClick === 0) isClick = 1;

            if (isClick === 1) {
                myCalendar.focus();
            }
        });

        $(myCalendar).on('click', function (e) {
            e.stopPropagation();
            isClick = 1;
        });

        $('.daterangepicker').on('click', function (e) {
            e.stopPropagation();
        });


    } catch (er) { console.log(er); }
    /*[ Select 2 Config ]
        ===========================================================*/

    try {
        var selectSimple = $('.js-select-simple');

        selectSimple.each(function () {
            var that = $(this);
            var selectBox = that.find('select');
            var selectDropdown = that.find('.select-dropdown');
            selectBox.select2({
                dropdownParent: selectDropdown
            });
        });

    } catch (err) {
        console.log(err);
    }
    //dropdowns
    var payTypes;
    
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: "http://localhost:8080/Payroll/payType/getAllPayTypes",

        success: function (response) {
            payTypes = response;
            if(payTypes==null || payTypes =='' || payTypes ==[]){
                $('#basicSalary').append($('<option id="Null" selected>').val('No Paytypes available').text('No Paytypes available'));
            }else{
                for (var i = 0; i < payTypes.length; i++) {
                    if (i == 0) {
                        $('#basicSalary').append($('<option selected>').val(payTypes[i].name).text(payTypes[i].name));
                    } else {
                        
                        $('#basicSalary').append($('<option>').val(payTypes[i].name).text(payTypes[i].name));
                    }
                }
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
    
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
            location = '../../index.html';
        });

        $("#buttonSubmit").click(function (e) {
            var grade = document.getElementById("grading_grade");
            var level = document.getElementById("grading_level");
            var startDate = document.getElementById("start_date");
            var endDate = document.getElementById("end_date");
            var min = document.getElementById("grading_min");
            var mid = document.getElementById("grading_mid");
            var max = document.getElementById("grading_max");
            var basicSalary = document.getElementById("basicSalary");
            var gradeisEmpty = document.getElementById("gradeisEmpty");
            var levelisEmpty = document.getElementById("levelisEmpty");
            var startDateisEmpty = document.getElementById("startDateisEmpty");
            var endDateisEmpty = document.getElementById("endDateisEmpty");
            var minisEmpty = document.getElementById("minisEmpty");
            var midisEmpty = document.getElementById("midisEmpty");
            var maxisEmpty = document.getElementById("maxisEmpty");
            var basicSalaryisEmpty = document.getElementById("basicSalaryisEmpty");
            var basicSalaryfield =  document.getElementById("basicSalary");

            grade.setAttribute("class", "input--style-4");
            level.setAttribute("class", "input--style-4");
            startDate.setAttribute("class", "input--style-4");
            endDate.setAttribute("class", "input--style-4");
            min.setAttribute("class", "input--style-4");
            mid.setAttribute("class", "input--style-4");
            max.setAttribute("class", "input--style-4");
            basicSalaryfield.setAttribute("class", "input--style-4");


            document.getElementById('minisEmpty').innerHTML ="* Required Field ";
            document.getElementById('midisEmpty').innerHTML ="* Required Field ";
            document.getElementById('maxisEmpty').innerHTML ="* Required Field ";
            document.getElementById('basicSalaryisEmpty').innerHTML ="* Required Field ";
            
            gradeisEmpty.setAttribute('hidden','');
            levelisEmpty.setAttribute('hidden','');
            startDateisEmpty.setAttribute('hidden','');
            endDateisEmpty.setAttribute('hidden','');
            minisEmpty.setAttribute('hidden','');
            midisEmpty.setAttribute('hidden','');
            maxisEmpty.setAttribute('hidden','');
            basicSalaryisEmpty.setAttribute('hidden','');

            //validations 
            if (grade.value == '' || level.value == '' || startDate.value == '' || endDate.value == '' || min.value == '' || mid.value == '' || max.value == ''|| basicSalary.value == '') {
                if (grade.value == '') {
                    gradeisEmpty.removeAttribute('hidden');
                    grade.setAttribute("class", "input--style-4-redBorder");
                }
                if (level.value == '') {
                    levelisEmpty.removeAttribute('hidden');
                    level.setAttribute("class", "input--style-4-redBorder");
                }
                if (startDate.value == '') {
                    startDateisEmpty.removeAttribute('hidden');
                    startDate.setAttribute("class", "input--style-4-redBorder");
                }
                if (endDate.value == '') {
                    endDateisEmpty.removeAttribute('hidden');
                    endDate.setAttribute("class", "input--style-4-redBorder");
                }
                if (min.value == '') {
                    minisEmpty.removeAttribute('hidden');
                    min.setAttribute("class", "input--style-4-redBorder");
                }
                if (mid.value == '') {
                    midisEmpty.removeAttribute('hidden');
                    mid.setAttribute("class", "input--style-4-redBorder");
                }
                if (max.value == '') {
                    maxisEmpty.removeAttribute('hidden');
                    max.setAttribute("class", "input--style-4-redBorder");
                }

            }else if(isNaN(min.value)||isNaN(mid.value)||isNaN(max.value)){

                if(isNaN(min.value)){
                    minisEmpty.removeAttribute('hidden');
                    min.setAttribute("class", "input--style-4-redBorder");
                    document.getElementById('minisEmpty').innerHTML = "should be a number";
    
                }if(isNaN(mid.value)){
                    midisEmpty.removeAttribute('hidden');
                    mid.setAttribute("class", "input--style-4-redBorder");
                    document.getElementById('midisEmpty').innerHTML = "should be a number";
    
                }if(isNaN(max.value)){
                    maxisEmpty.removeAttribute('hidden');
                    max.setAttribute("class", "input--style-4-redBorder");
                    document.getElementById('maxisEmpty').innerHTML = "should be a number";
                    
                }

            }else if(basicSalary.value== "No Paytypes available"){
                basicSalaryisEmpty.removeAttribute('hidden');
                basicSalaryfield.setAttribute("class", "input--style-4-redBorder");
                
            }else {
                var gradingModel = {
                    "grade": grade.value,
                    "level": level.value,
                    "startDate": startDate.value,
                    "endDate": endDate.value,
                    "min": min.value,
                    "mid": mid.value,
                    "max": max.value,
                    "basicSalary": basicSalary.value
                };
                var formData = JSON.stringify(gradingModel);

                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: "post",
                    url: "http://localhost:8080/Payroll/GradingStruct/addGradingAndSalary",
                    data: formData,
                    success: function (response) {
                        $('#ResultOfGradingStructureCreation').modal('show');
                        $('#success_msg').removeAttr('hidden');
                    },
                    error: function (xhr) {
                        var errorMessage = xhr.message;
                        $('#success_msg').attr('hidden', '');
                        $('#ResultOfGradingStructureCreation').modal('show');
                        $('#fail_msg').removeAttr('hidden');
                        document.getElementById('fail_msg').innerHTML = "Error!!" + errorMessage;
                    }
                });
            }
        });




    });




})();