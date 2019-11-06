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
