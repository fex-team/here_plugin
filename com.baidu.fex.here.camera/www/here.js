var exec = require("cordova/exec");

module.exports = {
	openCamera:function(maskUrl,onsuccess,onerror){
		cordova.exec(onsuccess, onerror, "Here", "camera", [{"maskUrl":maskUrl}]);
	}
}