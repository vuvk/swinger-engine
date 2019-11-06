
print("\tClips...");

var clipsParams = loadJSON("resources/maps/1/clips.json");

var materialsCount = MATERIALS.size;
loadTexturesAndMaterials(clipsParams);

var presets = [];

for (var i = 0; i < clipsParams.config.length; ++i) {
	var config = clipsParams.config[i];
	
	presets[i] = {};
	
	presets[i].material = MATERIALS.get(materialsCount + config.material);		
	switch (config.type) {
		case 1:
			presets[i].type = AmmoType.PISTOL;
			break;
		case 2:
			presets[i].type = AmmoType.SHOTGUN;
			break;
		case 3:
			presets[i].type = AmmoType.ROCKET;
			break;
		default:
			presets[i].type = AmmoType.NOTHING;
			break;                
	}
	
	presets[i].volume = config.volume;
}        

for (var i = 0; i < clipsParams.map.length; ++i) {
	var obj = clipsParams.map[i];
	var num = obj.clip;		
	var preset = presets[num];
	
	var pos = new Vector3(obj.position[0], obj.position[1], obj.position[2]);
	
	new Clip(preset.material, pos, preset.type, preset.volume);
}
