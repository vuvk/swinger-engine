var Texture = com.vuvk.swinger.res.Texture;
var TEXTURES  = com.vuvk.swinger.res.TextureBank.WALLS;
var TextureArray = Java.type('com.vuvk.swinger.res.Texture[]');

var Material = com.vuvk.swinger.res.Material;
var MaterialArray = Java.type('com.vuvk.swinger.res.Material[]');
var MATERIALS = com.vuvk.swinger.res.MaterialBank.BANK;

var WallMaterial = com.vuvk.swinger.res.WallMaterial;
var WallMaterialArray = Java.type('com.vuvk.swinger.res.WallMaterial[]');
var WALL_MATERIALS = com.vuvk.swinger.res.WallMaterialBank.BANK;

var Map = com.vuvk.swinger.res.Map;
var Sprite = com.vuvk.swinger.objects.Sprite;
var Vector2 = com.vuvk.swinger.math.Vector2;
var Vector3 = com.vuvk.swinger.math.Vector3;
var AmmoType = com.vuvk.swinger.objects.weapon.AmmoType;
var Clip = com.vuvk.swinger.objects.Clip;

/**
 * Загрузить текстуры и создать материалы по информации из JSON-объекта
 * */
var loadTexturesAndMaterials = function(json) {
		
	/* количество текстур до добавления в движок */
	var texturesCount = TEXTURES.size;
        
    /* грузим текстуры */
    for (var i = 0; i < json.textures.length; ++i) {
		var path = json.textures[i];
		TEXTURES.add(new Texture(path));
    }
        
	/* Формируем материалы */
	for (var i = 0; i < json.materials.length; ++i) {
		var mat = json.materials[i];
		
		var frmNums = mat.textures;
		var frames = new TextureArray(frmNums.length);
		for (var j = 0; j < frmNums.length; ++j) {
			frames[j] = TEXTURES.get(texturesCount + frmNums[j]);
        }
            
        var material = new Material(frames, mat.animation_speed, mat.play_once);
		if (json.brigthness !== null && json.brigthness !== undefined) {
			material.setBrightness(brigthness);
		}
		MATERIALS.add(material);		
    }
}

/**
 * Прочитать текстовый файл с диска и вернуть содержимое в виде строки
 * */
var loadTextFromFile = function(path) {
	var Files = java.nio.file.Files;
	var Paths = java.nio.file.Paths;
	var String = java.lang.String;
		
	return new String(Files.readAllBytes(Paths.get(path)));
}

/**
 * Удалить все Си-подобные комментарии в тексте
 * */
var stripComments = function(text) {
	//print(text);
	text = text.replaceAll('(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)', '');
	//print(text);
	return text;
}

/**
 * Загрузить JSON-объект из файла
 * */
var loadJSON = function(path) {
	return JSON.parse(stripComments(loadTextFromFile(path)));
}


print("\tSprites...");

var materialsCount = MATERIALS.size;
var spritesParams = loadJSON("resources/maps/1/sprites.json");

print("\t\tTextures and materials...");
loadTexturesAndMaterials(spritesParams);

for (var i = 0; i < spritesParams.map.length; ++i) {
	var obj = spritesParams.map[i];
	
	var matNum = materialsCount + obj.material;
	var mat = MATERIALS.get(matNum);	
	var pos = new Vector3(obj.position[0], obj.position[1], obj.position[2]);
	
	if (obj.solid) {
		Map.SOLIDS[parseInt(pos.x)][parseInt(pos.y)] = true;
	}
	
	new Sprite(mat, pos);          
}

var Weapon = Java.type("com.vuvk.swinger.objects.weapon.Weapon");
   
print("\tWeapons...");

var materialsCount = MATERIALS.size;
var weaponsParams = loadJSON("resources/maps/1/weapons.json");

/* грузим текстуры */     
print("\t\tTextures and materials...");
loadTexturesAndMaterials(weaponsParams);

// читаем параметры оружия
var presets = [];
for (var i = 0; i < weaponsParams.config.length; ++i) {
	var config = weaponsParams.config[i];
	
	presets[i] = {
		material: MATERIALS.get(materialsCount + config.material),
		ammo_in_clip: config.ammo_in_clip
	};
}

/* классы оружия */
// пистолет
var Pistol = function(pos) {
	var WEAPON_NUM = 1;
	var AMMO_TYPE = AmmoType.PISTOL;
	
	var proto = Java.extend(Weapon, {
		getInstance: function() { 
			return com.vuvk.swinger.graphic.weapon_in_hand.PistolInHand.getInstance(); 
		}
	});
	
	return new proto(presets[1].material, pos, WEAPON_NUM, AMMO_TYPE);
}
// автоматическая винтовка
var Rifle = function(pos) {
	var WEAPON_NUM = 2;
	var AMMO_TYPE = AmmoType.PISTOL;
	
	var proto = Java.extend(Weapon, {
		getInstance: function() { 
			return com.vuvk.swinger.graphic.weapon_in_hand.RifleInHand.getInstance(); 
		}
	});
	
	return new proto(presets[2].material, pos, WEAPON_NUM, AMMO_TYPE);
}
// пулемет
var Minigun = function(pos) {
	var WEAPON_NUM = 3;
	var AMMO_TYPE = AmmoType.PISTOL;
	
	var proto = Java.extend(Weapon, {
		getInstance: function() { 
			return com.vuvk.swinger.graphic.weapon_in_hand.MinigunInHand.getInstance(); 
		}
	});
	
	return new proto(presets[3].material, pos, WEAPON_NUM, AMMO_TYPE);
}

/* расставляем оружие */
print("\t\tWeapons placing...");
for (var i = 0; i < weaponsParams.map.length; ++i) {   
	var map = weaponsParams.map[i];
	
	var pos = new Vector3(map.position[0], map.position[1], map.position[2]);
	
	switch (map.weapon) {
		case 1 :
			new Pistol(pos)
				.setAmmoInClip(presets[1].ammo_in_clip);
			break;
		case 2 :
			new Rifle(pos)
				.setAmmoInClip(presets[2].ammo_in_clip);
			break;
		case 3 :
			new Minigun(pos)
				.setAmmoInClip(presets[3].ammo_in_clip);
			break;
	}
}


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
		Map.DOORS[x][y] = -1;
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
			Map.DOORS[x][y] = doorNum;
			
		// дверь не была размещена
		} else {
			Map.DOORS[x][y] = -1;
		}                  
	} 
}

