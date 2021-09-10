
print("\tCreate clips from JS?..");

// import classes
var Integer = Java.type("java.lang.Integer")
var Vector3 = Java.type("io.github.vuvk.swinger.math.Vector3");
var Texture = Java.type("io.github.vuvk.swinger.res.Texture");
var Material = Java.type("io.github.vuvk.swinger.res.Material");
var AmmoType = Java.type("io.github.vuvk.swinger.objects.weapon.AmmoType");
var AmmoPack = Java.type("io.github.vuvk.swinger.graphic.weapon_in_hand.AmmoPack");
var Sprite = Java.type("io.github.vuvk.swinger.objects.Sprite");
var Player = Java.type("io.github.vuvk.swinger.objects.mortals.Player");
//var Clip = Java.type("io.github.vuvk.swinger.objects.items.Clip");

// paths to texture files
var PATH_SMALL_CLIP = "resources/pics/items/clips/small_clip.png";
var PATH_BIG_CLIP = "resources/pics/items/clips/big_clip.png";

// textures of clips
var TXR_SMALL_CLIP = new Texture(PATH_SMALL_CLIP);
var TXR_BIG_CLIP = new Texture(PATH_BIG_CLIP);

// materials of clips
var MAT_SMALL_CLIP = new Material(TXR_SMALL_CLIP);
var MAT_BIG_CLIP = new Material(TXR_BIG_CLIP);

// create class of Clips
var Clip = function(material, position, ammoType, volume) {
    this.ammoType = ammoType;
    this.volume = volume;

    var SpriteAdapter = Java.extend(Sprite);    
    var extended = new SpriteAdapter(material, position) {
        update: function() {
            var __super = Java.super(extended);
                    
            __super.update();           
            
            var player = Player.getInstance();
            if (player != null && player.getPos().distance(__super.getPos()) < 0.5) {
                var curAmmo = AmmoPack.getNum(ammoType);
                var maxAmmo = ammoType.getMax();

                if (curAmmo < maxAmmo) {
                    AmmoPack.setNum(ammoType, Integer.parseInt(curAmmo + volume));

                    //AudioSystem.newSound(SoundBank.SOUND_BUFFER_GET_AMMO).playOnce();

                    __super.destroy();
                }
            }
        }
    };
    
    return this;
};

// ---------------------------
// positions of clips
// ---------------------------
var smallClipPositions = [
    new Vector3(11.5, 3.5, 0.0),
    new Vector3(11.5, 5.5, 0.0)
];

var bigClipPositions = [
    new Vector3(12.5, 4.5, 0.0)
];

// ---------------------------
// placing clips objects
// ---------------------------
for each (var pos in smallClipPositions) {
    new Clip(MAT_SMALL_CLIP, pos, AmmoType.PISTOL, 25);
}

for each (var pos in bigClipPositions) {
    new Clip(MAT_BIG_CLIP, pos, AmmoType.PISTOL, 100);
}

