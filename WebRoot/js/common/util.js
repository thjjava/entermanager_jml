function getObject(url){
	var d;
	$.ajax({
		type:'POST',
		url:url,
		async:false,
		success:function(data){
			d = data;
		}
  	});
	return d;
}