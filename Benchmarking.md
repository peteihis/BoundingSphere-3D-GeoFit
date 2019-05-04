# Bencmarging GeoFit

For benchmarking I coded a couple of aditional things into the the package:
- The Ritter's algorithm as a Java class of it's own
- An AABB-center based method called `fastSphere()` in the `GeoFit`-class 

They both are so called two-pass algorithms. 

The implementation of Ritter's algorithm has two modes, that affect the behavior first pass: It keeps either the first found or the last found point with the same coordinate value when it is selecting the points of th einitial sphere. The implementation is adapted to work vith vectors instead of indivisual coordinate values. There is no real benefit in using vectors as the algorithm does not use any of the most powerful vector functions. It just happens to be handy. 

The `GeoFit.fastSphere()` method is actually the two first passes of the `GeoFit.boundingSphere()` procedure. It produces a non-minimal enclosing sphere. Sometimes it may produce a perfect fit but the algrothm does not check that.

## Test cases

... on the way....
