
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
