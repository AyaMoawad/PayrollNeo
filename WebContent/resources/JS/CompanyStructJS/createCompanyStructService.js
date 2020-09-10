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
	  var formData=JSON.stringify(companyObjectsArray);
	$.ajax({
		  headers: {
			  'Accept': 'application/json',
			  'Content-Type': 'application/json'
		  },
		  type: "POST",
		  url:"http://localhost:8080/Payroll/companyStructure/addCompanyStructure",
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
  var x = 0;
  var companyObjectsArray = Array();
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
	var code =document.getElementById("companystruct_code");
	var name =document.getElementById("companystruct_name");
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
	
	if(companyObjectsArray.length===0){
	  addEmpObject(false,"",true,codeValue,nameValue,startDateValue,endDateValue);
	}
	else{
	  var lastParent = companyObjectsArray[companyObjectsArray.length - 1]
	  var lastParentValue = lastParent.code;
	  addEmpObject(true,lastParentValue	,true,codeValue,nameValue,startDateValue,endDateValue);
	}
	
	
  }
 
  // in case of submission condition "we reached a child"
  if (document.getElementById('noCheck').checked) {
	var lastParent;
	var lastParentValue;
	var code =document.getElementById("companystruct_code");
	var name =document.getElementById("companystruct_name");
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
  
		 for(var i=empObjectsArray.length-1 ;i<empObjectsArray.length;i-- ){
			 if(empObjectsArray[i].hasChild===true){
		 lastParent =empObjectsArray[i];
		 lastParentValue = lastParent.code;
		 break;
		 
	   }
  
	 }
  
	}else {		
	    lastParent = companyObjectsArray[companyObjectsArray.length - 1]
	    lastParentValue = lastParent.code;
  
	}
	
  
	addEmpObject(true,lastParentValue,false,codeValue,nameValue,startDateValue,endDateValue);
  
	
  
	$('#exampleModalCenter').modal('show'); 
	
	  console.log(companyObjectsArray);
  }
}
  
  function addEmpObject(hasParent,parentCode,hasChild,code ,name ,startDate,endDate){
  var code =code;
  var name = name;
  var startDate = startDate;
  var endDate = endDate;
  var empObject = {
	"code" : code,
	"name" :name,
	"startDate" :startDate,
	"endDate":endDate,
	"hasParent":hasParent,
	"parentCode":parentCode,
	"hasChild":hasChild	
	
  }
  companyObjectsArray[x] = empObject;
  x++;
  document.getElementById("companystruct_code").value="";
  document.getElementById("companystruct_name").value="";
  document.getElementById("start_date").value="";
  document.getElementById("end_date").value="";
  console.log(companyObjectsArray);
  }
  
  function sendToDB(){
  
  
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
	  var x = 0;
   companyObjectsArray = Array();
  
   
	}
  
	else{
	  document.getElementById('buttonSubmit').setAttribute("data-toggle","modal");
	  document.getElementById('buttonSubmit').setAttribute("data-target","#errorUniqueModal");
	}
	};
  
	xhttp.open("POST", "http://localhost:8080/Payroll/companyStructure/addCompanyStructure", true);
	xhttp.setRequestHeader("Content-type", "application/json");
  
	xhttp.send(JSON.stringify(companyObjectsArray));
  }
  
  
  function resetForm(){
  document.getElementById("companystruct_code").value="";
  document.getElementById("companystruct_name").value="";
  document.getElementById("start_date").value="";
  document.getElementById("end_date").value="";
  newChildInSameLevel = true;
  }
  
  
  
  function BackToCreateStructureClean(){
	window.location = '../../index.html'; 
  }