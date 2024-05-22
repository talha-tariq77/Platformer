### Camera and Units

units are kg/m/s

- viewport width and height is a window into our 'world'

- 'world' contains all the bodies, and can be any size
ground's initial position is set as (0,0)

- bodies in world can be set to *any* position, even negative positions.

viewport width and height is different to WINDOW_WIDTH, WINDOW_HEIGHT
viewport width and height is the portion of our world shown to user at any time 
measured in same units as body size units

camera is set at an initial position in the world


difference between viewport width and height, and window_width and height
proportions effects the stretching of the image

### Body
a Body object's world centre position is returned via:
body.getWorldCentre() or body.getPosition()
(same thing, different Vector2 instances)

body.applyLinearImpulse(impulse, pos) and other force/impulse application methods

apply to the body regardless of position given, the application is as if the body has the position on it (I think)

so for example, applying impulse to pos (1000, 0) would create great torque

or could be due to wake=true

#### Impulse vs Forces
Impulses immediately change the velocity by the amount given
whereas forces do not

force takes into account mass
impulse does not

both impulses and forces acellerate?


# todo
textures and body, movement


shapes are not part of the dynamics system
used for collision detection only

ContactListener manages all collisions

damping prevents movement

fixtures 'attach' shapes to bodies, in the sense that they move with the body
but they are not part of the dynamics system, used for collision detection only.

friction fixtures


contact filtering removes some contacts from consideration in contact listener

contacts created on this basis

16 bits, short = 2 byte
or combines
can do | instead of & first , & second cat

cant use switch with conditionals

addr prob has the pointer to the C++ object

all Application Listener methods are called on same thread

all libgdx classes not thread safe unless explicitly stated

world class - box2d is thread safe - can run 'asynchronous queries'

creating platform specific functionality:
create the functionality for the platform
pass it into the ApplicationListener

via the platform's starter class
e.g. on desktop the Lwjgl3ApplicationConfiguration class

use interface pass for multiple implementations of same platform-dependent-functionality on different platforms

scene2d
built on top of box2d
for building UI
use input multiplexer to handle input before sent to game input handler
for HUDs

in OpenGl
texture bound (made the current texture)
then is drawn by the GPU

It is very common to draw a texture mapped to rectangular geometry.
It is also very common to draw the same texture or various regions of that texture many times.
It would be inefficient to send each rectangle one at a time to the GPU to be drawn.
Instead, many rectangles for the same texture can be described and sent to the GPU all at once.
This is what the SpriteBatch class does.


ctrl +shift+a = command


SpriteBatch is given a texture and coordinates for each rectangle to be drawn. It collects the geometry without submitting it to the GPU. If it is given a texture different than the last texture, then it binds the last texture, submits the collected geometry to be drawn, and begins collecting geometry for the new texture.

Changing textures every few rectangles that are drawn prevents SpriteBatch from batching much geometry. Also, binding a texture is a somewhat expensive operation. For these reasons, it is common to store many smaller images in a larger image and then draw regions of the larger image to both maximize geometry batching and avoid texture changes. See TexturePacker for more information.


The Texture class decodes an image file and loads it into GPU memory. The image file should be placed in the “assets” folder. The image’s dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons.


### Sprite

The Sprite class (source) describes both a texture region, the geometry where it will be drawn, and the color it will be drawn.

Note that Sprite mixes model information (position, rotation, etc) with view information (the texture being drawn). This makes Sprite inappropriate when applying a design pattern that wishes to strictly separate the model from the view. In that case, using Texture or TextureRegion may make more sense.

Also note that there is no Sprite constructor that is related to the position of the Sprite. calling Sprite(Texture, int, int, int, int) does not edit the position. It is necessary to call Sprite#setPosition(float,float) or else the sprite will be drawn at the default position of 0,0.

### Blending

Blending is enabled by default. This means that when a texture is drawn, translucent portions of the texture are merged with pixels already on the screen at that location.

When blending is disabled, anything already on the screen at that location is replaced by the texture. This is more efficient, so blending should always be disabled unless it is needed. E.g., when drawing a large background image over the whole screen, a performance boost can be gained by first disabling blending:


pack a bunch of images into one with packer
generate the atlas for this

use that to generate the texture regions of the image
then draw these regions
or put these regions into an animation

### texture packer
TexturePacker can pack all images for an application in one shot. Given a directory, it recursively scans for image files. For each directory of images TexturePacker encounters, it packs the images on to a larger texture, called a page. If the images in a directory don’t fit on the max size of a single page, multiple pages will be used.

Images in the same directory go on the same set of pages. If all images fit on a single page, no subdirectories should be used because with one page the app will only ever perform one texture bind. Otherwise, subdirectories can be used to segregate related images to minimize texture binds. Eg, an application may want to place all the “game” images in a separate directory from the “pause menu” images, since these two sets of images are drawn serially: all the game images are drawn (one bind), then the pause menu is drawn on top (another bind). If the images were in a single directory that resulted in more than one page, each page could contain a mix of game and pause menu images. This would cause multiple texture binds to render the game and pause menu instead of just one each.

#### subdirectories
Subdirectories are also useful to group images with related texture settings. Settings like runtime memory format (RGBA, RGB, etc) and filtering (nearest, linear, etc) are per texture. Images that need different per texture settings need to go on separate pages, so should be placed in separate subdirectories.

To use subdirectories for organization without TexturePacker outputting a set of pages for each subdirectory, see the combineSubdirectories setting.

To avoid subdirectory paths being used in image names in the atlas file, see the flattenPaths setting.


### TextureAtlas

The TexturePacker output is a directory of page images and a text file that describes all the images packed on the pages. This shows how to use the images in an application:


atlas = new TextureAtlas(Gdx.files.internal("packedimages/pack.atlas"));
AtlasRegion region = atlas.findRegion("imagename");
Sprite sprite = atlas.createSprite("otherimagename");
NinePatch patch = atlas.createPatch("patchimagename");

TextureAtlas reads the pack file and loads all the page images. TextureAtlas.AtlasRegions can be retrieved, which are TextureRegions that provides extra information about the packed image, such as the frame index or any whitespace that was stripped. Sprites and NinePatches can also be created. If whitespace was stripped, the created Sprite will actually be a TextureAtlas.AtlasSprite, which allows the sprite to be used (mostly) as if whitespace was never stripped.

Note that findRegion is not very fast, so the value returned should be stored rather than calling this method each frame. Also note that createSprite and createNinePatch allocate a new instance.

TextureAtlas holds on to all the page textures, disposing the TextureAtlas will dispose all the page textures.


can
draw texture regions directly


https://box2d.org/documentation/b2__common_8h.html#afc0f934dabffb1e83e081249133ad860

Polygons inherit a radius from b2Shape. The radius creates a skin around the polygon. The skin is used in stacking scenarios to keep polygons slightly separated. This allows continuous collision to work against the core polygon.

https://box2d.org/documentation/md__d_1__git_hub_box2d_docs_collision.html#autotoc_md38

use skin only for player (?)

