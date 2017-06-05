var searchButton = document.getElementById('button');
console.log("1");
searchButton.addEventListener('click', search);
console.log("2");

function search() {
	console.log("Search is running");
	var searchTerms = $('#input').val();
    console.log(searchTerms);
	
}

// console.log("get.js loaded");
// var searchTest = $('#input').val();
// console.log(searchTest);

// //Listen when a button, with a class of "myButton", is clicked
// //You can use any jQuery/JavaScript event that you'd like to trigger the call
// $('#button').click(function () {
//     //Get the searchTerms and urlencode them
//     var searchTerms = $('#input').serialize();
//     console.log(searchTerms);
//     //Send the AJAX call to the server
//     $.ajax({
//         //The URL to process the request
//         'url': 'localhost:8080/search?' + searchTerms,
//         //The type of request, also known as the "method" in HTML forms
//         //Can be 'GET' or 'POST'
//         'type': 'GET',
//         //Any post-data/get-data parameters
//         //This is optional
//         'data': {
//             'paramater1': 'value',
//             'parameter2': 'another value'
//         },
//         //The response from the server
//         'success': function (data) {
//             //You can use any jQuery/JavaScript here!!!
//             if (data == "success") {
//                 alert('request sent!');
//             }
//         }
//     });
// });