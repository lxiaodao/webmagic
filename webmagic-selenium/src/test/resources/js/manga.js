var imgs=document.getElementsByClassName('page-break no-gaps');

var srcstring="";
var videoArray = new Array();
for (var i = 0; i < imgs.length; i++) {    
    var myimg = imgs[i].getElementsByTagName('img')[0];
    var playword = {"id":"","src":""};
    playword.id=myimg.id;
    playword.src=myimg.src;    
    videoArray.push(playword);
    /* srcstring+=myimg.src+","; */
}      
return JSON.stringify(videoArray).toString();




