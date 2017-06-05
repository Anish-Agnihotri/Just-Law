var search = document.getElementById('search');
var button = document.getElementById('button');
var input = document.getElementById('input');

function loading() {
	console.log("Search is loading");
	search.classList.add('loading');
	
	setTimeout(function() {
		search.classList.remove('loading');
	}, 1500);
}

button.addEventListener('click', loading);

input.addEventListener('keydown', function() {
	if(event.keyCode == 13) loading();
});search.j

