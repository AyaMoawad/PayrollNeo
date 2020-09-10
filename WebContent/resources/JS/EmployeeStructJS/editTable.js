var controller = (function () {
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
   
    var table = document.getElementById("EmpStructTable");
    var sortedArray = new Array();
    var children = new Array();
    var subs = new Array();
    var queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const code = urlParams.get('code');
    var arrayOfTotalChain;
    var arrangedArray=new Array();
    var children = new Array();
    var subs = new Array();
    
    
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get", //send it through get method
        url: location.href.split('/Payroll')[0]+"/Payroll/employeeStructure/showEmployeeStructure",
        data: {
            code: code
        },
        success: function (response) {
            if (response.theChain == null) {
                $('#employeeStructModal').modal('show');
            } else {
                arrayOfTotalChain = response.theChain;

                var today = new Date();
                var dd = String(today.getDate()).padStart(2, '0');
                var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                var yyyy = today.getFullYear();

                today = dd + '/' + mm + '/' + yyyy;
                $("#valid_date").val(today);

                $("tbody").remove();
                $('#EmpStructTable').append($('<tbody> <tr> </tr> </tbody>'));
                showTheEmpStructTable();
                $('#EmpStructTable').removeAttr('hidden');
                for (var c = 0; c < arrangedArray.length; c++) {
                    var startDateString = arrangedArray[c].startDate;
                    var sday = startDateString.slice(0, 3);
                    var smo = startDateString.slice(3, 6);
                    var syear = startDateString.slice(6, 11);
                    var modifiedStartDate = new Date(smo + '/' + sday + '/' + syear);
    
                    var endDateString = arrangedArray[c].endDate;
                    var eday = endDateString.slice(0, 3);
                    var emo = endDateString.slice(3, 6);
                    var eyear = endDateString.slice(6, 11);
                    var modifiedEndDate = new Date(emo + '/' + eday + '/' + eyear);
    
                    var validDate = $("#valid_date").val();
                    var vday = validDate.slice(0, 3);
                    var vmo = validDate.slice(3, 6);
                    var vyear = validDate.slice(6, 11);
                    var modifiedValidDate = new Date(vmo + '/' + vday + '/' + vyear);
    
                    if (((modifiedStartDate > modifiedValidDate) && (modifiedEndDate <
                        modifiedValidDate)) || (modifiedStartDate > modifiedValidDate)  ||(modifiedEndDate <
                            modifiedValidDate) ) {
                        var trCode = arrangedArray[c].code;
                        $('#' + trCode).remove();
                    }
                }
                if(($("#EmpStructTable > tbody > tr").length-1)==0){
                    $('#EmpStructTable').attr('hidden','');
                    $('#tableIsEmptyMSG').removeAttr('hidden','');
                }

            }
        },
        error: function (xhr) {
        }

    });

    function showTheEmpStructTable() {
        sortedArray = new Array();
        children = new Array();
        subs = new Array();
        arrangedArray=new Array();
        for (var x = 0; x < arrayOfTotalChain.length; x++) {
            if (arrayOfTotalChain[x].hasParent == false) {
                sortedArray[0] = arrayOfTotalChain[x];
            } else if (arrayOfTotalChain[x].hasChild == true) {
                subs.push(arrayOfTotalChain[x]);
            } else {
                children.push(arrayOfTotalChain[x]);
            }
        }
        sortedArray = sortedArray.concat(subs).concat(children);

        arrangeTheTable();
        for (var counter = 0; counter < arrangedArray.length; counter++) {
            var theCode = code;
            var theHrefForEdit = 'editEmpStructData.html?code='
                + arrangedArray[counter].code + '&theCode='
                + theCode;
            var theHrefFordelemit = 'delemitEmpStructData.html?code='
                + arrangedArray[counter].code
                + '&theCode='
                + theCode;
            var theHrefFordelete = 'deleteEmpStructData.html?code='
                + arrangedArray[counter].code
                + '&theCode='
                + theCode;

            var theHrefForAddSub = 'addSubParent.html?code='    
            + arrangedArray[counter].code
            + '&theCode='
            + theCode;

            var row = table.insertRow(-1);
            row.id = arrangedArray[counter].code;
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            var cell4 = row.insertCell(3);
            var cell5 = row.insertCell(4);
            var cell6 = row.insertCell(5);
            var cell7 = row.insertCell(6);
            if (arrangedArray[counter].hasParent == false) {
                cell3.innerHTML = "Parent";
                cell4.innerHTML = "N/A";
                row.setAttribute('class', 'parent');
                var theHrefForCopy = 'copyEmpStructData.html?code='
                    + arrangedArray[counter].code
                    + '&theCode='
                    + theCode;
                var theHrefFordeleteParent = 'deleteEmpStructParentData.html?code='
                    + arrangedArray[counter].code
                    + '&theCode='
                    + theCode;
                    cell7.innerHTML = "<a href=" + theHrefForEdit
                    + ">Edit    </a>" + "<a href="
                    + theHrefFordelemit + ">   Delimit</a>"
                    + "<a href=" + theHrefFordeleteParent
                    + ">   Delete</a>" + "<a href="
                    + theHrefForCopy + ">   Copy</a>"
                    +"<a href=" +theHrefForAddSub+ ">Add </a>" ;
                    row.removeAttribute('hidden');
            } else if (arrangedArray[counter].hasChild == true) {
                cell3.innerHTML = "SubParent";
                cell7.innerHTML = "<a href=" + theHrefForEdit
                    + ">Edit    </a>" + "<a href="
                    + theHrefFordelemit + ">   Delimit</a>"
                    + "<a href=" + theHrefFordelete
                    + ">   Delete</a>"
                    +"<a href=" +theHrefForAddSub
                    + ">Add</a>" ;       
                    cell4.innerHTML = arrangedArray[counter].parentCode;        
            } else {
                cell3.innerHTML = "Child";
                cell7.innerHTML = "<a href=" + theHrefForEdit
                    + ">Edit    </a>" + "<a href="
                    + theHrefFordelemit + ">   Delimit</a>"
                    + "<a href=" + theHrefFordelete
                    + ">   Delete</a>";
                    cell4.innerHTML = arrangedArray[counter].parentCode;
            }
            cell1.innerHTML = arrangedArray[counter].code;
            cell2.innerHTML = arrangedArray[counter].name;
            cell5.innerHTML = arrangedArray[counter].startDate;
            cell6.innerHTML = arrangedArray[counter].endDate;

        } $('#theEmpStruct').removeAttr('hidden');
    }

    function arrangeTheTable(){
        var noOfChildren;
        var noOfSubParents;
        var parentCode;
        var oldParentCode;
        var childFounded ;
        var parent_Code;
        for(var b=0;b<sortedArray.length;b++){
            if(sortedArray[b].hasParent==false){
                parent_Code=sortedArray[b].code;
                var index=b;
            }       
        }
         for(;index<sortedArray.length;index++){
            parentCode=sortedArray[index].code;
            arrangedArray.push(sortedArray[index]);
            console.log(sortedArray[index]);
            var isThisParent = sortedArray[index].hasChild;
            sortedArray.splice(index,1);
            if(isThisParent){
                oldParentCode=parentCode;
                noOfChildren=0;
                noOfSubParents=0;
                childFounded =false;
                for(var i=0;i<sortedArray.length;i++){
                    if(parentCode.localeCompare(sortedArray[i].parentCode)==0 && sortedArray[i].hasChild ==false ){
                        index=i-1;
                        noOfChildren++;
                        childFounded =true;
                    }
                    if(parentCode.localeCompare(sortedArray[i].parentCode)==0 && sortedArray[i].hasChild ==true ){
                        noOfSubParents++;
                    }
                }
                if(noOfChildren>0){ noOfChildren--;}
                if(!childFounded){
                    for(var i=0;i<sortedArray.length;i++){
                        if(parentCode.localeCompare(sortedArray[i].parentCode)==0){
                            index=i-1;
                        }
                    }if(noOfSubParents>0){ noOfSubParents--;}
                }
                
            }else if(noOfChildren!=0){
                for(var i=0;i<sortedArray.length;i++){
                    if(oldParentCode.localeCompare(sortedArray[i].parentCode)==0 && sortedArray[i].hasChild ==false ){
                        index=i-1;
                        noOfChildren--;
                        childFounded =true;
                    }
                }
            }else if(noOfSubParents!=0){
                for(var i=0;i<sortedArray.length;i++){
                    if(oldParentCode.localeCompare(sortedArray[i].parentCode)==0){
                        index=i-1;
                        noOfSubParents--;
                    }
                }
            }else{
                for(var i=0;i<sortedArray.length;i++){
                    if(parent_Code.localeCompare(sortedArray[i].parentCode)==0){
                        index=i-1;
                        break;
                    }else{index=-1;}
                }
            }
            
         }
         if(!sortedArray.length==0){
            console.log(arrangedArray);
            arrangeTheTable();
         }
    }


    jQuery(document).ready(function ($) {

        $("#modalOkButton").click(function (e) {
            location = 'editEmpStruct.html';

        });

        $("#valid_date").on('apply.daterangepicker', function () {
            $('#tableIsEmptyMSG').attr('hidden','');
            $("tbody").remove();
            $('#EmpStructTable').append($('<tbody> <tr> </tr> </tbody>'));
            showTheEmpStructTable();
            $('#EmpStructTable').removeAttr('hidden');
            for (var c = 0; c < arrangedArray.length; c++) {
                var startDateString = arrangedArray[c].startDate;
                var sday = startDateString.slice(0, 3);
                var smo = startDateString.slice(3, 6);
                var syear = startDateString.slice(6, 11);
                var modifiedStartDate = new Date(smo + '/' + sday + '/' + syear);

                var endDateString = arrangedArray[c].endDate;
                var eday = endDateString.slice(0, 3);
                var emo = endDateString.slice(3, 6);
                var eyear = endDateString.slice(6, 11);
                var modifiedEndDate = new Date(emo + '/' + eday + '/' + eyear);

                var validDate = $("#valid_date").val();
                var vday = validDate.slice(0, 3);
                var vmo = validDate.slice(3, 6);
                var vyear = validDate.slice(6, 11);
                var modifiedValidDate = new Date(vmo + '/' + vday + '/' + vyear);

                if (((modifiedStartDate > modifiedValidDate) && (modifiedEndDate <
                    modifiedValidDate)) || (modifiedStartDate > modifiedValidDate)  ||(modifiedEndDate <
                        modifiedValidDate) ) {
                    var trCode = arrangedArray[c].code;
                    $('#' + trCode).remove();
                }
            }
            if(($("#EmpStructTable > tbody > tr").length-1)==0){
                $('#EmpStructTable').attr('hidden','');
                $('#tableIsEmptyMSG').removeAttr('hidden','');
            }
        });

        $("#modalOkButton").click(function (e) {
            location = 'editEmpStruct.html';
        });

    });
})();