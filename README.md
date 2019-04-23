# BoundingSphere-3D-GeoFit

A method to define a bounding sphere in 3D-space by geometrical rules.

The method produces perfect fit with 2 to 4 support points, within the obtainable numerical accuracy. In the simpler _(2-point)_ cases it can come close to reaching the speed of Ritter's algorithm. In more challenging cases time consumption of the solver will usually be multipied only a few times over.

The algorithm needs the points to fit the sphere over as 3D-vector array, defined by "Vec3" in artofillusion.math package by Peter Eastman. Several of the functions of Vec3 are used in the process.



