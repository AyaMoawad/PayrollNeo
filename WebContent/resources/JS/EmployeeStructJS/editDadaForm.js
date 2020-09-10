(function ($) {
    'use strict';
    /*==================================================================
        [ Daterangepicker ]*/
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
    
        $(window).on('click',function(){
            isClick = 0;
        });
    
        $(myCalendar).on('apply.daterangepicker',function(ev, picker){
            isClick = 0;
            $(this).val(picker.startDate.format('DD/MM/YYYY'));
    
        });
    
        $('.js-btn-calendar').on('click',function(e){
            e.stopPropagation();
    
            if(isClick === 1) isClick = 0;
            else if(isClick === 0) isClick = 1;
    
            if (isClick === 1) {
                myCalendar.focus();
            }
        });
    
        $(myCalendar).on('click',function(e){
            e.stopPropagation();
            isClick = 1;
        });
    
        $('.daterangepicker').on('click',function(e){
            e.stopPropagation();
        });
    
    
    } catch(er) {console.log(er);}
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
    

})(jQuery);

var controller = (function () {
    queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const code = urlParams.get('code');
    const theCode = urlParams.get('theCode');
    var model;


    
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get",
        url: location.href.split('/Payroll')[0]+"/Payroll/employeeStructure/getEmployeeStructureElement",
        data: {
            code:code
        },
        success: function (response) {
            model=response.theModel;  
            $("#empstruct_code").val(model.code);    
            $("#empstruct_name").val(model.name); 
            $("#start_date").val(model.startDate); 
            $("#end_date").val(model.endDate);
            var hasParent=model.hasParent; 
            var hasChild=model.hasChild;
            if(hasParent && !hasChild){
                $('#changeToSubParentBtn').removeAttr('hidden','')
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
            location='showEditTable.html?code='+theCode;
        });
        //functions for the change to subParent btn//
        $("#changeToSubParentBtn").mouseenter(function () {
            $(this).removeClass('btn-primary');
            $(this).addClass('bg-success');
        });

        $("#changeToSubParentBtn").mouseout(function () {
            $(this).removeClass('bg-success');
            $(this).addClass('btn-primary');
        });

        $("#changeToSubParentBtn").click(function (e) {
            location='changeToSubParent.html?code='+code+'&theCode='+theCode;
        });
       
        $("#buttonSubmit").click(function (e) {
           
            var empObject = {
                "code" :model.code,
                "name" :$("#empstruct_name").val(),
                "startDate" :$("#start_date").val(),
                "endDate":$("#end_date").val(),
                "hasParent":model.hasParent,
                "parentCode":model.parentCode,
                "hasChild":model.hasChild}
            var formData=JSON.stringify(empObject);
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                type: "put",
                url:location.href.split('/Payroll')[0]+"/Payroll/employeeStructure/updateEmployeeStructure",
                data :formData,
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
