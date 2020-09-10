var arrayOfTotalChain;
var table = document.getElementById("PayrollStructTable");
var row;
$.ajax({
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    },
    type: "get", 
    url: "http://localhost:8080/Payroll/PayrollStruct/getAllPayrollStruct",
    success: function (response) {
        if (response == null || response=='') {
            $('#choosePayrollStruct').attr('hidden', '');
            $('#tableIsEmptyMSG').removeAttr('hidden', '');
        } else {
            $('#choosePayrollStruct').removeAttr('hidden', '');
            arrayOfTotalChain = response;
            $('#PayrollStructTable').append($('<tbody> <tr> </tr> </tbody>'));

            for (var counter = 0; counter < arrayOfTotalChain.length; counter++) {
                row = table.insertRow(-1);
                $(row).addClass("theTblRows");
                row.id = arrayOfTotalChain[counter].code;
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                cell1.innerHTML = arrayOfTotalChain[counter].code;
                cell2.innerHTML = arrayOfTotalChain[counter].name;
            } $('#PayrollStructTable').removeAttr('hidden');
        }
    },
    error: function (xhr) {
    }
});


function myFunction() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("PayrollStructTable");
    tr = table.getElementsByTagName("tr");
  
    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[0];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  }


var controller = (function () {
    jQuery(document).ready(function ($) {
        var code;
        var oldID = null;
        var newId;
        $('#exampleModalCenter').on('show.bs.modal', function(e) {
            $(" tr" ).on( "click", function( event ) {
                newId = $(this).attr('id');
                if (oldID != null) {
                    theId = '#' + oldID;
                    $(theId).removeClass('bg-success');
                }
                oldID = newId;
                console.log(oldID);
                $(this).addClass('bg-success');
            });
        });
        
        $("#choosePayrollStruct").click(function () {
            if (oldID != null) {
                $(".input--style-4").val(oldID);
                $('#exampleModalCenter').modal('hide');
            }
        }); 

        $("#buttonSubmit").mouseenter(function () {
            $(this).removeClass('btn-primary');
            $(this).addClass('bg-success');
        });

        $("#buttonSubmit").mouseout(function () {
            $(this).removeClass('bg-success');
            $(this).addClass('btn-primary');
        });

        $("#buttonSubmit").click(function (e) {
            code = $(".input--style-4").val();
            window.location = 'showPayrollStructElement.html?code='+code;
            
        });
    });
})();
