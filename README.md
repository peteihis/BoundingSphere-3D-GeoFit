# BoundingSphere-3D-GeoFit

A method to define a bounding sphere in 3D-space by geometrical rules. The method produces perfect fit with 2 to 4 support points, within obtainable numerical accuracy. 

The method always does a minimum of four passes through the data.
1. Finds the AABB-center
2. Finds the most distant point from the AABB center. If sveral points are at equal distance, the first one found will be used.
3. Finds the most distant point from the previous point.

These two points are used to define the 'intial sphere'.

4. Tries to find the most distant point outside that sphere. If none are found, the sphere is ready. 

If the fouth pass found a point, the algorithm checks if one of the previously found points can be eliminated. If not, the point is added to the support list, the sphere is updated and a new most distant point is searched for. This procedure is repeated until points are not found outside the sphere. The number of passes is not limited in any way.

During the passes the algotithm only checks the squared distances between points. Evaluating the findings and recalculating the parameters happens only after each pass. This approach saves processing time especially with large numbers of points to check.

The initial version of the algorithm uses the `Vec3` and `Mat4` classes of `artofillusion.math` package. Mat4 is needed if the objecs are placed as instances in the model space. Point positions are given as `Vec3(x,y,z)` and vector functions are used on all calculations. Of course the code can easily be adapted to use any available vector and matrix packages.

