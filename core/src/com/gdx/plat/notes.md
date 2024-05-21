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


