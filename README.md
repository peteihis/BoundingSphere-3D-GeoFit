# BoundingSphere-3D-GeoFit

An algorithm to define a bounding sphere in 3D-space by geometrical rules. The algorithm produces perfect fit with 2 to 4 support points, within obtainable numerical accuracy.

Currently the main algoritm is coded in the `GeoFit.boundigSphere()`-method. The GeoFit-class also provides a secondary method `fastSphere()`, which is a simple two pass method, that produces a non-minimal enclosing sphere. 

The origins of this algorithm are in Art of Illusion development environment. The full AoI-plugin set can be found in the ArtOfIllusion folder. The generic version contains a core set, that may be more suitable for further editing/adapting. The set requires a math package with 3D-Vectors for the algorithm to work.

## How it works

The algoritm works in two distictive steps: First finding a smallest possible sphere, that _could_ enclose all of the data poins. Then, if necessary, it will start to find more support points that _could_ be used to create a sphere, that is just large enough to encose all of the date. This step naturally favors the smalles possible of the available choises.

### The initializing phase

This phase always does four passes through the data:
1. Finds the AABB-center
2. Finds the most distant point from the AABB center. If sveral points are at equal distance, the first one found will be used.  
3. Finds the most distant point from the previous point.

These two points are used to define the 'intial sphere'.

4. Tries to find the most distant point outside that sphere. If none are found, the sphere is ready. 

### The fitting phase
If the fourth pass found a point, the algorithm checks if one of the previously found points can be eliminated. If so, the new support set is taken as the "current candidate" and the paramaters are updated. If not, the point is added to the support list, the parameters are updated and a new most distant point is searched for. This procedure is repeated until points are not found outside the sphere. The number of passes is not limited in any way.

During the passes the algorithm only uses the squared distance between the last calculates center point and each data point. The sphere center and the squared radius are updated in an evaluation step after each pass. The final value for the radius of the sphere is calculated as one of the last things in the process.

## Development

The first version (0.01 still named GeoFinder) of the code got finalised the 20th of April 2019 and was posted to SourceForge https://sourceforge.net/p/aoi/discussion/47782/thread/59419e028f/ for testing as a plugin to Art of Illusion, along with a script to test it.

The AoI version of the algorithm uses the `Vec3` and `Mat4` classes of `artofillusion.math` package. `Mat4` is needed if the objecs are placed as instances in the model space. Point positions are given as `Vec3(x,y,z)` and vector functions are used on calculations. Of course the code can easily be adapted to use any available vector and matrix packages, that have the required mathods available.

## Current status

The version **0.02 for Art of Illusion** was updated to the forum on the 4th of May 2019. This version comes with methods to fit a sphere on 
- a set of 3D points as `Vec3[]`
- an object that is placed in a scene
- a selection of objects in a scene

## Future plans

I'm planning on doing some bechmarking next. Hopefully, one day, there will be a stand-alone .jar to demo it. :)
