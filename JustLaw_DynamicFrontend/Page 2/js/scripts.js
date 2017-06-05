// Empty JS for your own code to be here

var activeClass = ["Pending"];
var successClass = ["Approved"];
var warningClass = ["Call in to confirm"];
var dangerClass = ["Declined"];


$(document).ready(function () {
  $.ajax({
    type: "GET",
    url: "http://localhost:8080/RESTfulExample/Cases/get"
  }).then(function (data) {

    var rows = "";
    var className;

    for (var i = 0; i < data.length; i++) {

      if (successClass.includes(data[i].proceedingDate)) {
        className = "success";
      } else if (warningClass.includes(data[i].proceedingDate)) {
        className = "warning";
      } else if (dangerClass.includes(data[i].proceedingDate)) {
        className = "danger";
      } else {
        className = "active";
      }

      rows +=
        "<tr class='" + className + "'>"
          + "<td><a href='../Page 3/page3.html'>" + data[i].caseId + "</a></td>"
          + "<td>" + data[i].court + "</td>"
          + "<td>" + data[i].summary + "</td>"
          + "<td>" + data[i].proceedingDate + "</td>"
        + "</tr>";
    }

    $('.table_row').append(rows);
  });
});



// <tr class="active">
//  <td>
//    <p class="case_id"></p>
//  </td>
//  <td>
//    <p class="court"></p>
//  </td>
//  <td>
//    <p class="summary"></p>
//  </td>
//  <td>
//    <p class="proceeding_date"></p>
//  </td>
//</tr>


//<table>
//  <tr>
//    <th>id</th><th>Name</th>
//  </tr>
//
//  <% for (var i = 0; i < data.length; i++) { %>
//    <tr>
//      <td><%= data[i].id %></td>
//      <td><%= data[i].name %></td>
//    </tr>    
//  <% } %>
//</table>
