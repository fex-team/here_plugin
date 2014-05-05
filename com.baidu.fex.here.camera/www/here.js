var exec = require("cordova/exec");

module.exports = {
	openCamera:function(list,onsuccess,onerror){
		cordova.exec(onsuccess, onerror, "Here", "camera", [list]);
	}
}