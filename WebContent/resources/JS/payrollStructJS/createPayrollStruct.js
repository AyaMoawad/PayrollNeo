
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
    var intervals;
    var payrollValuation;
    var currencies;
    var countries;



    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: "http://localhost:8080/Payroll/lookUps/getCountries",

        success: function (response) {
            countries = response;
            for (var i = 0; i < countries.length; i++) {
                if (i == 0) {
                    $('#country').append($('<option selected>').val(countries[i].name).text(countries[i].name));
                } else {
                    $('#country').append($('<option>').val(countries[i].name).text(countries[i].name));
                }
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: "http://localhost:8080/Payroll/lookUps/getIntervals",
        success: function (response) {
            intervals = response;
            for (var i = 0; i < intervals.length; i++) {
                if (i == 0) {
                    $('#interval').append($('<option selected>').val(intervals[i].name).text(intervals[i].name));
                } else {
                    $('#interval').append($('<option>').val(intervals[i].name).text(intervals[i].name));
                }
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: "http://localhost:8080/Payroll/lookUps/getCurrencies",
        success: function (response) {
            currencies = response;
            for (var i = 0; i < currencies.length; i++) {
                if (i == 0) {
                    $('#currency').append($('<option selected>').val(currencies[i].name).text(currencies[i].name));
                } else {
                    $('#currency').append($('<option>').val(currencies[i].name).text(currencies[i].name));
                }
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });

    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: "http://localhost:8080/Payroll/lookUps/getPayrollValuations",
        success: function (response) {
            payrollValuation = response;
            for (var i = 0; i < payrollValuation.length; i++) {
                if (i == 0) {
                    $('#payrollValuation').append($('<option selected>').val(payrollValuation[i].name).text(payrollValuation[i].name));
                } else {
                    $('#payrollValuation').append($('<option>').val(payrollValuation[i].name).text(payrollValuation[i].name));
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

        $('#country').on('change', function (e) {
            if($('#country').val()=== "Egypt") {
                $('#taxSettlementId').removeAttr('hidden','');
            } 
            if(!($('#country').val()=== "Egypt")) {
                $('#taxSettlementId').attr('hidden','');
            } 
          });

        $('#payrollValuation').on('change', function (e) {
            if($('#payrollValuation').val()=== "fixed") {
                $('#fixedDays').removeAttr('hidden','');
            } 
            if(!($('#payrollValuation').val()=== "fixed")) {
                $('#fixedDays').attr('hidden','');
            } 
          });

          
        $("#buttonSubmit").click(function (e) {
            var code = document.getElementById("payrollStruct_code");
            var name = document.getElementById("payrollStruct_name");
            var startDate = document.getElementById("start_date");
            var endDate = document.getElementById("end_date");
            var interval = document.getElementById("interval");
            var country = document.getElementById("country");
            var currency = document.getElementById("currency");
            var payrollValuation = document.getElementById("payrollValuation");
            var noOfDays = document.getElementById('noOfDays');
            var codeisEmpty = document.getElementById("codeisEmpty");
            var nameisEmpty = document.getElementById("nameisEmpty");
            var startDateisEmpty = document.getElementById("startDateisEmpty");
            var endDateisEmpty = document.getElementById("endDateisEmpty");
            var noOfDaysisEmpty = document.getElementById("noOfDaysisEmpty");
           
            code.setAttribute("class", "input--style-4");
            name.setAttribute("class", "input--style-4");
            startDate.setAttribute("class", "input--style-4");
            endDate.setAttribute("class", "input--style-4");
            payrollValuation.setAttribute("class", "input--style-4");
            noOfDays.setAttribute("class", "input--style-4");

            document.getElementById('noOfDaysisEmpty').innerHTML ="* Required Field ";
            codeisEmpty.setAttribute('hidden','');
            nameisEmpty.setAttribute('hidden','');
            startDateisEmpty.setAttribute('hidden','');
            endDateisEmpty.setAttribute('hidden','');
            noOfDaysisEmpty.setAttribute('hidden','');

            if (code.value == '' || name.value == '' || startDate.value == '' || endDate.value == '' || interval.value == '' || country.value == '' || currency.value == '' || payrollValuation.value == '') {
                if (code.value == '') {
                    codeisEmpty.removeAttribute('hidden');
                    code.setAttribute("class", "input--style-4-redBorder");
                }
                if (name.value == '') {
                    nameisEmpty.removeAttribute('hidden');
                    name.setAttribute("class", "input--style-4-redBorder");
                }
                if (startDate.value == '') {
                    startDateisEmpty.removeAttribute('hidden');
                    startDate.setAttribute("class", "input--style-4-redBorder");
                }
                if (endDate.value == '') {
                    endDateisEmpty.removeAttribute('hidden');
                    endDate.setAttribute("class", "input--style-4-redBorder");
                }

            }else if(($('#payrollValuation').val()=== "fixed")&&document.getElementById('noOfDays') =='' ){
                noOfDaysisEmpty.removeAttribute('hidden');
                noOfDays.setAttribute("class", "input--style-4-redBorder");
            } else if(isNaN(noOfDays.value)){
                $('#noOfDaysisEmpty').html('*should be a number');
                noOfDays.setAttribute("class", "input--style-4-redBorder");
                noOfDaysisEmpty.removeAttribute('hidden');
            }else {
                var taxSettlement;
                if(document.getElementById('annually').checked){
                    taxSettlement = 'annually';
                }else {
                    taxSettlement ='monthly';
                }
                var payrollStructModel = {
                    "code": code.value,
                    "name": name.value,
                    "startDate": startDate.value,
                    "endDate": endDate.value,
                    "interval": interval.value,
                    "country": country.value,
                    "currency": currency.value,
                    "payrollValuation":payrollValuation.value,
                    "taxSettlement":taxSettlement,
                    "noOfDays":noOfDays.value
                };
                var formData = JSON.stringify(payrollStructModel);
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: "post",
                    url: "http://localhost:8080/Payroll/PayrollStruct/addPayrollStruct",
                    data: formData,
                    success: function (response) {
                        $('#ResultOfpayrollStructCreation').modal('show');
                        $('#success_msg').removeAttr('hidden');
                    },
                    error: function (xhr) {
                        var errorMessage = xhr.responseJSON.message;
                        $('#success_msg').attr('hidden', '');
                        $('#ResultOfpayrollStructCreation').modal('show');
                        $('#fail_msg').removeAttr('hidden');
                        document.getElementById('fail_msg').innerHTML = "Error!!" + errorMessage;
                    }
                });
            }
        });
    });
})();       


