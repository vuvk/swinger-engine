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
