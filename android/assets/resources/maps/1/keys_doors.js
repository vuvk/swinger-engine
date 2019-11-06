var Key = com.vuvk.swinger.objects.Key;
var Door = com.vuvk.swinger.objects.Door;

print("\tKeys and Doors...");
        
var materialsCount = MATERIALS.size;        
var keysDoorsParams = loadJSON("resources/maps/1/keys_doors.json");
        
/* грузим текстуры */     
print("\t\tTextures and materials...");
loadTexturesAndMaterials(keysDoorsParams);
        
// читаем параметры ключей
var keys_presets = [];
for (var i = 0; i < keysDoorsParams.keys_config.length; ++i) {
	var config = keysDoorsParams.keys_config[i];
	
	keys_presets[i] = {
		material: MATERIALS.get(materialsCount + config.material)
	};
}

/* расставляем ключи */      
print("\t\tKeys placing...");
for (var i = 0; i < keysDoorsParams.keys_map.length; ++i) {   
	var map = keysDoorsParams.keys_map[i];
	
	var keyNum = map.key;
	var pos = new Vector3(map.position[0], map.position[1], map.position[2]);
	
	new Key(keys_presets[keyNum].material, pos, keyNum);
}     
                 
        
// читаем параметры дверей
var doors_presets = [];
for (var i = 0; i < keysDoorsParams.doors_config.length; ++i) {
	var config = keysDoorsParams.doors_config[i];
	
	doors_presets[i] = {		
		material: MATERIALS.get(materialsCount + config.material),
		keyNum: (config.key !== undefined) ? config.key : -1
	};
}
       
/* расставляем двери */   
print("\t\tDoors placing...");             
for (var x = 0; x < Map.WIDTH; ++x) {
	for (var y = 0; y < Map.HEIGHT; ++y) {
		Map.DOORS_MAP[x][y] = -1;
	}
}

for (var i = 0; i < keysDoorsParams.doors_map.length; ++i) {   
	var map = keysDoorsParams.doors_map[i];
	
	var doorNum = map.door;  
    var x = map.position[0],
		y = map.position[1];
            
	// расставляем твердые объекты там, где двери
	if (doorNum >= 0) {
		// точки сегмента двери
		var a = null,
			b = null;

		// и создаем сегмент двери, если она размещена правильно
		// горизонтальная дверь?
		if (x > 0 && x < Map.WIDTH - 1) {
			if (Map.WALLS_MAP[0][x - 1][y] >= 0 && Map.WALLS_MAP[0][x + 1][y] >= 0) {
				a = new Vector2(x + 0, y + 0.5);
				b = new Vector2(x + 1, y + 0.5);
			}
		}

		// вертикальная дверь?
		if (y > 0 && y < Map.HEIGHT - 1) {
			if (Map.WALLS_MAP[0][x][y - 1] >= 0 && Map.WALLS_MAP[0][x][y + 1] >= 0) {
				a = new Vector2(x + 0.5, y + 0);
				b = new Vector2(x + 0.5, y + 1);
			}
		}

		// нужно ставить дверь
		if (a != null && b != null) {
			var door = new Door(a, b, doors_presets[doorNum].material, doors_presets[doorNum].keyNum);
			Map.SEGMENTS[x][y] = door;
			Map.SOLIDS[x][y] = true;            
			Map.DOORS_MAP[x][y] = doorNum;
			
		// дверь не была размещена
		} else {
			Map.DOORS_MAP[x][y] = -1;
		}                  
	} 
}
