function setImagePreviews(imagesObj,divid) {
    var dd = document.getElementById(divid);
    dd.style.display="block";
    //dd.innerHTML = "";
    console.log(dd.innerHTML);
    //var len = dd.getElementsByTagName('img').length;
    var t = (new Date()).getTime();
    var fileList = imagesObj.files;
    for (var i = 0; i < fileList.length; i++) {
    	var pic = '<div id="imgDiv_'+ t +'" class="imgDiv"><img id="'+divid+'_img'+ t +'" />';
		pic += '<a href="javascript:delImg('+ t +')" class="imgDel">&times;</a></div>';
		dd.innerHTML += pic;
        //dd.innerHTML += "<div style='float:left;border:2px solid #95B8E7;margin-right:10px;margin-bottom:10px' > <img id='"+divid+"_img" + (i+len) + "'  /> </div>";
        var imgObjPreview = document.getElementById(divid+"_img"+ t ); 
        if (imagesObj.files && imagesObj.files[i]) {
            //火狐下，直接设img属性
            imgObjPreview.style.display = 'block';
            imgObjPreview.style.width = '100px';
            imgObjPreview.style.height = '100px';
            imgObjPreview.src = window.URL.createObjectURL(imagesObj.files[i]);
        }else {
            //IE下，使用滤镜
            imagesObj.select();
            var imgSrc = document.selection.createRange().text; //运用IE滤镜获取数据;
            //alert(imgSrc);
            var localImagId = document.getElementById("img" + i);
            //必须设置初始大小
            localImagId.style.width = "100px";
            localImagId.style.height = "100px";
            //图片异常的捕捉
            try {
                localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader( true,sizingMethod=scale,src = imgSrc)";  //scale：缩放图片以适应对象的尺寸边界。
            }
            catch (e) {
                alert("您上传的图片格式不正确，请重新选择!");
                return false;
            }
            imgObjPreview.style.display = 'none';
            document.selection.empty(); //在当前网页下不能选择对象,也就是鼠标不能选中 
        }
    }  
    return true;
}