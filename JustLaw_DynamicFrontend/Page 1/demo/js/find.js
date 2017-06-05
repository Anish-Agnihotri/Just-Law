console.log("find.js loaded");

$("#button") // todo: hook up the enter key for search

SUMMARY_PAGE_DOM = null;

//Get the text in the search bar when the search button is clicked
$(function () {
    $("#button").click(function () {
        var searchText = encodeURI(document.getElementById("input").value); //Get and URLencode the search bar text
        console.log("Search is working. Input:" + " " + searchText);

        if (searchText.trim()==="") { return; } // Don't search on an empty bar.

        //Make the GET request
        var url = 'http://localhost:8080/search?searchString=' + searchText;
        console.log(url);
        $.ajax({
            url: url,
            type: "GET"
        }).then(function (data) {
            console.log(data);
            SUMMARY_PAGE_DOM = data;
            window.location.href = "../../../Website/Page%202/index.html"
        });
    });

});