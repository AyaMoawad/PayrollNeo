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
    
    $("#SendToDB").click(function (e) {
        queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const code = urlParams.get('code');
     
        var formData=JSON.stringify(CompanyObjectsArray);
        


    $.ajax({
          headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
          },
          type: "POST",
          url:"http://localhost:8080/Payroll/companyStructure/copyCompanyStructure?code="+code,
          data :formData,
          success: function (response) {
           
                $('#ResultOfCompanyStructCreation').modal('show'); 
                 $('#success_msg').removeAttr('hidden');
              
          },
          error: function (xhr) {
           var errorMessage = xhr.responseJSON.message;
           $('#ResultOfCompanyStructCreation').modal('show'); 
            $('#fail_msg').removeAttr('hidden');
            document.getElementById('fail_msg').innerHTML = "Error!!"+errorMessage;
          }
      });
      
  });
  
  
  
  
  
  })(jQuery);


  var controller = (function () {
    queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const code = urlParams.get('code');
    var arrayOfTotalChain;
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = dd+ '/' + mm + '/' + yyyy;
    newCode=code+'_'+today;
    $('#Companystruct_code').val(newCode);
    $('#Companystruct_code').attr('disabled','');
    
    jQuery(document).ready(function ($) {
        $("#buttonSubmit").mouseenter(function () {
            $(this).removeClass('btn-primary');
            $(this).addClass('bg-success');
        });

        $("#buttonSubmit").mouseout(function () {
            $(this).removeClass('bg-success');
            $(this).addClass('btn-primary');
        });
        
    });
})();




  var x = 0;
  var CompanyObjectsArray = Array();
  var newChildInSameLevel = false;
  
  "use strict";
  
  function yesnoCheck() {
    if (document.getElementById('yesCheck').checked) {
    document.getElementById('buttonSubmit').innerHTML = 'Next';
  
    } 
  if (document.getElementById('noCheck').checked) {
    document.getElementById('buttonSubmit').innerHTML = 'Submit';
  
  
  
  }
  }
  
  function NextOrSubmit(){
  // in case of the next condition
  
  if (document.getElementById('yesCheck').checked) {    	
    var code =document.getElementById("Companystruct_code");
    var name =document.getElementById("Companystruct_name");
    var startDate =document.getElementById("start_date");
    var endDate =document.getElementById("end_date");
    
    // validation for empty fields in the form
  if(code.value =='' || name.value =='' || startDate.value =='' ||endDate.value ==''){
  
    if(code.value==''){
      var codeisEmpty =document.getElementById("codeisEmpty");
      codeisEmpty.removeAttribute('hidden');
      code.setAttribute("class", "input--style-4-redBorder");
    }
   
     if(name.value ==''){
      var nameisEmpty =document.getElementById("nameisEmpty");
      nameisEmpty.removeAttribute('hidden');
      name.setAttribute("class", "input--style-4-redBorder");
    }
     if(startDate.value ==''){
      var startDateisEmpty =document.getElementById("startDateisEmpty");
      startDateisEmpty.removeAttribute('hidden');
      startDate.setAttribute("class", "input--style-4-redBorder");
    }
     if(endDate.value ==''){
      var endDateisEmpty =document.getElementById("endDateisEmpty");
      endDateisEmpty.removeAttribute('hidden');
      endDate.setAttribute("class", "input--style-4-redBorder");
      return;
    }
    return;
  }
  
  
    var codeValue = code.value;
    var nameValue = name.value;
    var startDateValue = startDate.value;
    var endDateValue = endDate.value;
    $('#Companystruct_code').removeAttr('disabled');
    
    $('#codeisEmpty').attr('hidden','');
    $('#nameisEmpty').attr('hidden','');
    $('#startDateisEmpty').attr('hidden','');
    $('#endDateisEmpty').attr('hidden','');

    code.setAttribute("class", "input--style-4");
    name.setAttribute("class", "input--style-4");
    startDate.setAttribute("class", "input--style-4");
    endDate.setAttribute("class", "input--style-4");
    



    if(CompanyObjectsArray.length===0){
      addCompanyObject(false,"",true,codeValue,nameValue,startDateValue,endDateValue);
    }
    else{
      var lastParent = CompanyObjectsArray[CompanyObjectsArray.length - 1]
      var lastParentValue = lastParent.code;
      addCompanyObject(true,lastParentValue	,true,codeValue,nameValue,startDateValue,endDateValue);
    }
    
    
  }
  // in case of submission condition "we reached a child"
  if (document.getElementById('noCheck').checked) {
    var lastParent;
    var lastParentValue
    var code =document.getElementById("Companystruct_code");
    var name =document.getElementById("Companystruct_name");
    var startDate =document.getElementById("start_date");
    var endDate =document.getElementById("end_date");
  
     // validation for empty fields in the form
  if(code.value =='' || name.value =='' || startDate.value =='' ||endDate.value ==''){
  
    if(code.value==''){
      var codeisEmpty =document.getElementById("codeisEmpty");
      codeisEmpty.removeAttribute('hidden');
      code.setAttribute("class", "input--style-4-redBorder");
  
    }
   
     if(name.value ==''){
      var nameisEmpty =document.getElementById("nameisEmpty");
      nameisEmpty.removeAttribute('hidden');
      name.setAttribute("class", "input--style-4-redBorder");
    }
     if(startDate.value ==''){
      var startDateisEmpty =document.getElementById("startDateisEmpty");
      startDateisEmpty.removeAttribute('hidden');
      startDate.setAttribute("class", "input--style-4-redBorder");
    }
     if(endDate.value ==''){
      var endDateisEmpty =document.getElementById("endDateisEmpty");
      endDateisEmpty.removeAttribute('hidden');
      endDate.setAttribute("class", "input--style-4-redBorder");
      return;
    }
    return;
  }
  
    var codeValue = code.value;
    var nameValue = name.value;
    var startDateValue = startDate.value;
    var endDateValue = endDate.value;
    
  
  
    if(newChildInSameLevel){
  
         for(var i=CompanyObjectsArray.length-1 ;i<CompanyObjectsArray.length;i-- ){
             if(CompanyObjectsArray[i].hasChild===true){
         lastParent =CompanyObjectsArray[i];
         lastParentValue = lastParent.code;
         break;
         
       }
  
     }
  
    }else {
  
       lastParent = CompanyObjectsArray[CompanyObjectsArray.length - 1]
       lastParentValue = lastParent.code;
  
    }
    
  
    addCompanyObject(true,lastParentValue,false,codeValue,nameValue,startDateValue,endDateValue);
  
    
  
    $('#exampleModalCenter').modal('show'); 
    
      console.log(CompanyObjectsArray);
  }
  }
  
  function addCompanyObject(hasParent,parentCode,hasChild,code ,name ,startDate,endDate){
  var code =code;
  var name = name;
  var startDate = startDate;
  var endDate = endDate;
  var CompanyObject = {
    "code" : code,
    "name" :name,
    "startDate" :startDate,
    "endDate":endDate,
    "hasParent":hasParent,
    "parentCode":parentCode,
    "hasChild":hasChild	
    
  }
  CompanyObjectsArray[x] = CompanyObject;
  x++;
  document.getElementById("Companystruct_code").value="";
  document.getElementById("Companystruct_name").value="";
  document.getElementById("start_date").value="";
  document.getElementById("end_date").value="";
  console.log(CompanyObjectsArray);
  }
  
  function sendToDB(){
  
  
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      var x = 0;
   empObjectsArray = Array();
  
   
    }
  
    else{
      document.getElementById('buttonSubmit').setAttribute("data-toggle","modal");
      document.getElementById('buttonSubmit').setAttribute("data-target","#errorUniqueModal");
    }
    };
  
    xhttp.open("POST", "http://localhost:8080/Payroll/companyStructure/copyCompanyStructure", true);
    xhttp.setRequestHeader("Content-type", "application/json");
  
    xhttp.send(JSON.stringify(CompanyObjectsArray));
  }
  
  
  function resetForm(){
  document.getElementById("Companystruct_code").value="";
  document.getElementById("Companystruct_name").value="";
  document.getElementById("start_date").value="";
  document.getElementById("end_date").value="";
  newChildInSameLevel = true;
  }
  
  
  
  function BackToCreateStructureClean(){
    window.location = '../../index.html'; 
  }