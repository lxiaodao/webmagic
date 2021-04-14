function cutcontent(){
	var videoArray = new Array();

	
	var omain = document.getElementsByClassName('reading-content');
	var number=arguments[0];
	
	if (omain[0].querySelector('div.page-break no-gaps')) {
		var imgsList = omain[0].querySelectorAll('div.page-break no-gaps');
		for (var i = 0; i < imgsList.length; i++) {
		        
			    var myimg = imgsList[i].getElementsByTagName('img')[0];
		        var playword = {"id":"","src":""};
		        playword.id=myimg.id;
		        playword.src=myimg.src;
		        
		        videoArray.push(playword);
		      
		 }      
	}
	return JSON.stringify(videoArray);
}
