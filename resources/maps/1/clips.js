
print("\tCreate clips from JS?..");

// import classes
var Vector3 = Java.type('io.github.vuvk.swinger.math.Vector3');
var Texture = Java.type('io.github.vuvk.swinger.res.Texture');
var Material = Java.type('io.github.vuvk.swinger.res.Material');
var AmmoType = Java.type('io.github.vuvk.swinger.objects.weapon.AmmoType');
var Clip = Java.type('io.github.vuvk.swinger.objects.items.Clip');

// paths to texture files
var PATH_SMALL_CLIP = "resources/pics/items/clips/small_clip.png";
var PATH_BIG_CLIP = "resources/pics/items/clips/big_clip.png";

// textures of clips
var TXR_SMALL_CLIP = new Texture(PATH_SMALL_CLIP);
var TXR_BIG_CLIP = new Texture(PATH_BIG_CLIP);

// materials of clips
var MAT_SMALL_CLIP = new Material(TXR_SMALL_CLIP);
var MAT_BIG_CLIP = new Material(TXR_BIG_CLIP);

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

// place small clips
for each (var pos in smallClipPositions) {
    new Clip(MAT_SMALL_CLIP, pos, AmmoType.PISTOL, 25);
}

// place big clips
for each (var pos in bigClipPositions) {
    new Clip(MAT_BIG_CLIP, pos, AmmoType.PISTOL, 100);
}

