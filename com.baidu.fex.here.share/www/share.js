function getBase64FromImageUrl(URL,callback) {
		
		var img = new Image();
		img.crossOrigin = 'anonymous';
		img.src = URL;
		img.onload = function() {

			var canvas = document.createElement("canvas");
			canvas.width = this.width;
			canvas.height = this.height;

			var ctx = canvas.getContext("2d");
			ctx.drawImage(this, 0, 0);

			var dataURL = canvas.toDataURL("image/png");
			dataURL = dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
			callback && callback(dataURL);

		}
		
	}
	
	function launch(type,data,scene){
		cordova.exec(function() {
			}, function() {
			}, "com.baidu.fex.here.share", "wechat", [{
				"type" : type,
				"data" : data,
				"scene" : scene
			}]);
	}

	module.exports = {
		wechat : function(data, scene) {
			var type;
			if (/^http:\/\//.test(data)) {
				type = "datauri";
				getBase64FromImageUrl(data,function(base64){
					launch(type,base64,scene);
				});
			}else{
				type = "file";
				launch(type,data,scene);
			}

			
		},
		WECHAT_SCENE : {
			session : 0,
			timeline : 1
		}
	}