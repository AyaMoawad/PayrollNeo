var controller = (function () {

    var table = document.getElementById("CompanyStructTable");
    var sortedArray = new Array();
    var children = new Array();
    var subs = new Array();
    var queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const code = urlParams.get('code');
    var arrayOfTotalChain;
    var arrangedArray = new Array();
    var children = new Array();
    var subs = new Array();
    var payTypes;

    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "get", //send it through get method
        url: location.href.split('/Payroll')[0]+"/Payroll/companyStructure/showCompanyStructure",
        data: {
            code: code
        },
        success: function (response) {
            if (response.theChain == null) {
                $('#CompanyStructModal').modal('show');
            } else {
                arrayOfTotalChain = response.theChain;

                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: "get", //send it through get method
                    url: location.href.split('/Payroll')[0]+"/Payroll/payType/getAllPayTypes",
                    success: function (response) {
                        if (response == null || response == '') {
                            payTypes = null;
                        } else {
                            payTypes = response;
                            for (var p = 0; p < payTypes.length; p++) {
                                $("#CompanyStructTable>thead>tr").append("<th>Paytype/"+payTypes[p].code+"</th>");
                            }
                        }
                        $("tbody").remove();
                        $('#CompanyStructTable').append($('<tbody> <tr> </tr> </tbody>'));
                        showTheEmpStructTable();
                        $('#CompanyStructTable').removeAttr('hidden');
                    },
                    error: function (xhr) {
                    }
                });
            }
        },
        error: function (xhr) {
        }
    });

    function showTheEmpStructTable() {
        sortedArray = new Array();
        children = new Array();
        subs = new Array();
        arrangedArray = new Array();
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
        var payTypesCheckBoxs = " ";
        arrangeTheTable();
        for (var counter = 0; counter < arrangedArray.length; counter++) {
            var row = table.insertRow(-1);
            row.id = arrangedArray[counter].code;
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            var cell4 = row.insertCell(3);
            var cell5 = row.insertCell(4); //payTypes check boxes XD

            if (arrangedArray[counter].hasParent == false) {
                cell3.innerHTML = "Parent";
                cell4.innerHTML = "N/A";
                row.setAttribute('class', 'parent');
            } else if (arrangedArray[counter].hasChild == true) {
                cell3.innerHTML = "SubParent";
                cell4.innerHTML = arrangedArray[counter].parentCode;
            } else {
                cell3.innerHTML = "Child";
                cell4.innerHTML = arrangedArray[counter].parentCode;
            }
            cell1.innerHTML = arrangedArray[counter].code;
            cell2.innerHTML = arrangedArray[counter].name;

            if (payTypes == null) {
                cell5.innerHTML = "No paytypes created yet";
            } else {
                var arrayOfEmpCodes;
                var EmpCodes;

                arrayOfEmpCodes = new Array();
                arrayOfEmpCodes.push(arrangedArray[counter].code);
                EmpCodes = JSON.stringify(arrayOfEmpCodes);
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: "POST",
                    async: false,
                    url:location.href.split('/Payroll')[0]+"/Payroll/companyStructure/getAllPaytypesAssignedToCompanyStruct",
                    data: EmpCodes,
                    success: function (response) {
                        var theAssignedPayTypes;
                        theAssignedPayTypes == new Array();
                        theAssignedPayTypes = response;
                        for (var p = 0; p < payTypes.length; p++) {
                            if (theAssignedPayTypes[0].code.localeCompare(row.id) == 0
                                && theAssignedPayTypes[0].payTypeCodes.includes(payTypes[p].code)) {
                                    var cell5 = row.insertCell(4+p);
                                    cell5.innerHTML= "<input checked id=\"" + payTypes[p].code + "-" + row.id + "\"" + "type=\"checkbox\"  autocomplete=\"off\"> " ;
                            } else {
                                var cell5 = row.insertCell(4+p);
                                cell5.innerHTML= "<input id=\"" + payTypes[p].code + "-" + row.id + "\"" + "type=\"checkbox\"  autocomplete=\"off\"> "  ;
                            }
                            
                        }
                        $(row).find('td:last-child').remove();
                    },
                    error: function (xhr) {
                    }
                });

            }
        }
        $('#theCompanyStruct').removeAttr('hidden');
    }

    function arrangeTheTable() {
        var noOfChildren;
        var noOfSubParents;
        var parentCode;
        var oldParentCode;
        var childFounded;
        var parent_Code;
        for (var b = 0; b < sortedArray.length; b++) {
            if (sortedArray[b].hasParent == false) {
                parent_Code = sortedArray[b].code;
                var index = b;
            }
        }
        for (; index < sortedArray.length; index++) {
            parentCode = sortedArray[index].code;
            arrangedArray.push(sortedArray[index]);
            var isThisParent = sortedArray[index].hasChild;
            sortedArray.splice(index, 1);
            if (isThisParent) {
                oldParentCode = parentCode;
                noOfChildren = 0;
                noOfSubParents = 0;
                childFounded = false;
                for (var i = 0; i < sortedArray.length; i++) {
                    if (parentCode.localeCompare(sortedArray[i].parentCode) == 0 && sortedArray[i].hasChild == false) {
                        index = i - 1;
                        noOfChildren++;
                        childFounded = true;
                    }
                    if (parentCode.localeCompare(sortedArray[i].parentCode) == 0 && sortedArray[i].hasChild == true) {
                        noOfSubParents++;
                    }
                }
                if (noOfChildren > 0) { noOfChildren--; }
                if (!childFounded) {
                    for (var i = 0; i < sortedArray.length; i++) {
                        if (parentCode.localeCompare(sortedArray[i].parentCode) == 0) {
                            index = i - 1;
                        }
                    } if (noOfSubParents > 0) { noOfSubParents--; }
                }

            } else if (noOfChildren != 0) {
                for (var i = 0; i < sortedArray.length; i++) {
                    if (oldParentCode.localeCompare(sortedArray[i].parentCode) == 0 && sortedArray[i].hasChild == false) {
                        index = i - 1;
                        noOfChildren--;
                        childFounded = true;
                    }
                }
            } else if (noOfSubParents != 0) {
                for (var i = 0; i < sortedArray.length; i++) {
                    if (oldParentCode.localeCompare(sortedArray[i].parentCode) == 0) {
                        index = i - 1;
                        noOfSubParents--;
                    }
                }
            } else {
                for (var i = 0; i < sortedArray.length; i++) {
                    if (parent_Code.localeCompare(sortedArray[i].parentCode) == 0) {
                        index = i - 1;
                        break;
                    } else { index = -1; }
                }
            }

        }
        if (!sortedArray.length == 0) {
            arrangeTheTable();
        }
    }

    var unassignedModel;
    var listOfUnAssignedModel = new Array();
    var y = 0;
    var assignmentModel;
    var listOfModels = new Array();
    var x = 0;
    var empStructCode;
    var listOfAssignedPayTypes;
    var j; var i;
    function addUnAssignedModel(unassignedModel) {
        listOfUnAssignedModel[y] = unassignedModel;
        y++;
    }
    function saveUnAssignedModelValues(code, listOfPayTypes) {
        unassignedModel = {
            "code": code,
            "payTypeCodes": listOfPayTypes
        }
        addUnAssignedModel(unassignedModel);
    }
    function addModel(assignmentModel) {
        listOfModels[x] = assignmentModel;
        x++;
    }
    function saveModelValues(code, listOfPayTypes) {
        assignmentModel = {
            "code": code,
            "payTypeCodes": listOfPayTypes
        }
        addModel(assignmentModel);
    }

    jQuery(document).ready(function ($) {

        $("#modalOkButton").click(function (e) {
            location = 'assignPayTypeToCompanyStruct.html';

        });

        $("#buttonSubmit").mouseenter(function () {
            $(this).removeClass('btn-primary');
            $(this).addClass('bg-success');
        });

        $("#buttonSubmit").mouseout(function () {
            $(this).removeClass('bg-success');
            $(this).addClass('btn-primary');
        });

        $("#buttonSubmit").click(function () {
            y = 0;
            listOfUnAssignedModel = new Array();
            x = 0;
            listOfModels = new Array();
            $('#CompanyStructTable > tbody  > tr').each(function () {
                empStructCode = this.id;
                if (empStructCode != '' || empStructCode != null || empStructCode != "undefined") {
                    listOfAssignedPayTypes = new Array();
                    listOfUnAssigned = new Array();
                    for (i = 0; i < payTypes.length; i++) {
                        if (document.getElementById(payTypes[i].code + "-" + empStructCode) != null
                            && document.getElementById(payTypes[i].code + "-" + empStructCode).checked
                        ) {
                            listOfAssignedPayTypes.push(payTypes[i].code);
                        } else if (document.getElementById(payTypes[i].code + "-" + empStructCode) != null
                            && !document.getElementById(payTypes[i].code + "-" + empStructCode).checked
                        ) {
                            listOfUnAssigned.push(payTypes[i].code);
                        }
                    }
                    saveUnAssignedModelValues(empStructCode, listOfUnAssigned);
                    saveModelValues(empStructCode, listOfAssignedPayTypes);

                }
            });
            listOfModels.shift();
            listOfUnAssignedModel.shift();
            var formData = JSON.stringify(listOfModels);
            var data = JSON.stringify(listOfUnAssignedModel);
            console.log(listOfModels);
            console.log(listOfUnAssignedModel);

            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                type: "POST",
                url: location.href.split('/Payroll')[0]+"/Payroll/companyStructure/removePaytypeFromCompanyStuct",
                data: data,
                success: function (response) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        type: "POST",
                        url: location.href.split('/Payroll')[0]+"/Payroll/companyStructure/assignPaytypeToCompanyStruct",
                        data: formData,
                        success: function (response) {
                            $('#ResultOfCompanyStructAssignment').modal('show');
                            $('#success_msg').removeAttr('hidden');
                        },
                        error: function (xhr) {
                            var errorMessage = xhr.responseJSON;
                            $('#ResultOfCompanyStructAssignment').modal('show');
                            $('#fail_msg').removeAttr('hidden');
                            document.getElementById('fail_msg').innerHTML = "Error!!" + errorMessage;
                        }
                    });
                },
                error: function (xhr) {
                    var errorMessage = xhr.responseJSON;
                    $('#ResultOfCompanyStructAssignment').modal('show');
                    $('#fail_msg').removeAttr('hidden');
                    document.getElementById('fail_msg').innerHTML = "Error!!" + errorMessage;
                }
            });
        });

    });
})();
