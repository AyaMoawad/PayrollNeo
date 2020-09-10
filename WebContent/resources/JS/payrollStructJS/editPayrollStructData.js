
queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const code = urlParams.get('code');
var theInterval;
var theCountry;
var theCurrency;
var thePayrollVal;
var taxSettlement;

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
        $('#payrollStruct_company').val('');

        theInterval = response.interval;
        thePayrollVal=response.payrollValuation;
        theCountry=response.country;
        theCurrency=response.currency;
        
        taxSettlement = response.taxSettlement;
        if(theCountry=== "Egypt") {
            $('#taxSettlementId').removeAttr('hidden','');
            if(taxSettlement==='monthly'){
                $('#monthly').addClass('active');
            }else{
                $('#annually').addClass('active');
            }
        } 

        if(response.payrollValuation=== "fixed") {
            $('#fixedDays').removeAttr('hidden','');
            $('#noOfDays').val(response.noOfDays);
        } 

    },
    error: function (xhr) {
        console.log(xhr);
    }
});

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
    var interval;
    var country;
    var currency;
    var payrollVal;

    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: "http://localhost:8080/Payroll/lookUps/getCountries",

        success: function (response) {
            country = response;
            for (var i = 0; i < country.length; i++) {
                if (theCountry.localeCompare(country[i].name)==0) {
                    $('#country').append($('<option selected>').val(country[i].name).text(country[i].name));
                } else {
                    $('#country').append($('<option>').val(country[i].name).text(country[i].name));
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
            interval = response;
            for (var i = 0; i < interval.length; i++) {
                if (theInterval.localeCompare(interval[i].name)==0) {
                    $('#interval').append($('<option selected>').val(interval[i].name).text(interval[i].name));
                } else {
                    $('#interval').append($('<option>').val(interval[i].name).text(interval[i].name));
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
            currency = response;
            for (var i = 0; i < currency.length; i++) {
                if (theCurrency.localeCompare(currency[i].name)==0) {
                    $('#currency').append($('<option selected>').val(currency[i].name).text(currency[i].name));
                } else {
                    $('#currency').append($('<option>').val(currency[i].name).text(currency[i].name));
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
            payrollVal = response;
            for (var i = 0; i < payrollVal.length; i++) {
                if (thePayrollVal.localeCompare(payrollVal[i].name)==0) {
                    $('#payrollValuation').append($('<option selected>').val(payrollVal[i].name).text(payrollVal[i].name));
                } else {
                    $('#payrollValuation').append($('<option>').val(payrollVal[i].name).text(payrollVal[i].name));
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
            queryString = window.location.search;
            const urlParams = new URLSearchParams(queryString);
            const code = urlParams.get('code');
            window.location ='showPayrollStructElement.html?code='+code;
        });

        $("#buttonSubmit").click(function (e) {
            var code = document.getElementById("payrollStruct_code");
            var name = document.getElementById("payrollStruct_name");
            var startDate = document.getElementById("start_date");
            var endDate = document.getElementById("end_date");
            var intervalName = document.getElementById("interval");
            var countryName = document.getElementById("country");
            var currencyName = document.getElementById("currency");
            var payrollValuationName = document.getElementById("payrollValuation");
            var numberOfFixedDays =  document.getElementById("noOfDays");
            var noOfDaysEmpty = document.getElementById("noOfDaysEmpty");
            noOfDaysEmpty.setAttribute('hidden','');
            numberOfFixedDays.setAttribute("class", "input--style-4");;
            document.getElementById('noOfDaysEmpty').innerHTML ="* Required Field ";

            //validations 
            if (code.value == '' || name.value == '' || startDate.value == '' || endDate.value == '' ) {
                if (code.value == '') {
                    var codeisEmpty = document.getElementById("codeisEmpty");
                    codeisEmpty.removeAttribute('hidden');
                    code.setAttribute("class", "input--style-4-redBorder");
                }
                if (name.value == '') {
                    var nameisEmpty = document.getElementById("nameisEmpty");
                    nameisEmpty.removeAttribute('hidden');
                    name.setAttribute("class", "input--style-4-redBorder");
                }
                if (startDate.value == '') {
                    var startDateisEmpty = document.getElementById("startDateisEmpty");
                    startDateisEmpty.removeAttribute('hidden');
                    startDate.setAttribute("class", "input--style-4-redBorder");
                }
                if (endDate.value == '') {
                    var endDateisEmpty = document.getElementById("endDateisEmpty");
                    endDateisEmpty.removeAttribute('hidden');
                    endDate.setAttribute("class", "input--style-4-redBorder");
                }
            } else if(payrollValuationName.value == "fixed" && numberOfFixedDays.value ==''){
                noOfDaysEmpty.removeAttribute('hidden','');
                numberOfFixedDays.setAttribute("class", "input--style-4-redBorder");
            }else if(payrollValuationName.value == "fixed" && isNaN(numberOfFixedDays.value)){
                noOfDaysEmpty.removeAttribute('hidden','');
                numberOfFixedDays.setAttribute("class", "input--style-4-redBorder");
                document.getElementById('noOfDaysEmpty').innerHTML ="Should be a number";

            }else {
                var taxSettlementName;
                if($('#annually').hasClass('active')){
                    taxSettlementName = 'annually';
                }else {
                    taxSettlementName ='monthly';
                }


                var payrollStructModel = {
                    "code": code.value,
                    "name": name.value,
                    "startDate": startDate.value,
                    "endDate": endDate.value,
                    "interval": intervalName.value,
                    "country": countryName.value,
                    "currency": currencyName.value,
                    "payrollValuation":payrollValuationName.value,
                    "taxSettlement":taxSettlementName,
                    "noOfDays":numberOfFixedDays.value
                };
                var formData = JSON.stringify(payrollStructModel);


                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: "put",
                    url: "http://localhost:8080/Payroll/PayrollStruct/updatePayrollStruct",
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






