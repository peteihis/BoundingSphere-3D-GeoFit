# Bencmarging GeoFit

For benchmarking I coded a couple of aditional things into the the package:
- The Ritter's algorithm as a Java class of it's own
- An AABB-center based method called `fastSphere()` in the `GeoFit`-class 

They both are so called two-pass algorithms. 

The implementation of Ritter's algorithm has two modes, that affect the behavior first pass: It keeps either the first found or the last found point that has the same coordinate value (max or min) when it is selecting the points of th einitial sphere. The implementation is adapted to work vith vectors instead of indivisual coordinate values. There is no real benefit in using vector as the algorithm does not use any of the most powerful vector functions. It just happens to be handy. 

The `GeoFit.fastSphere()` method is actually the two first passes of the `GeoFit.boundingSphere()` procedure. It produces a non-minimal enclosing sphere. Sometimes it may produce a perfect fit but the algrothm does not check that.

## Cases
<table>
  <tr>
    <th></th>
    <th colspan = 4>8-point cube</th>
    <th colspan = 4>Teapot</th>
  </tr>
  <tr>
    <th>Algorithm</th>
    <th>time</th><th>Pass</th><th>Supp</th><th>Radius</th>
    <th>time</th><th>Pass</th><th>Supp</th><th>Radius</th>
  </tr>
  <tr>
    <td>GeoFit</td>
    <td><small>0.4333</small></td><td>4</td><td>2</td><td>2.567</td>
    <td><small>0.4333</small></td><td>4</td><td>2</td><td>2.567</td>
  </tr>
  <tr>
    <td>AABB</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
  </tr>
  <tr>
    <td>Ritter F</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
  </tr>
  <tr>
    <td>Ritter L</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
  </tr>
</table>
<table>
  <tr>
    <th></th>
    <th colspan = 4>Bunny</th>
    <th colspan = 4>Horse</th>
  </tr>
  <tr>
    <th>Algorithm</th>
    <th>time</th><th>Pass</th><th>Supp</th><th>Radius</th>
    <th>time</th><th>Pass</th><th>Supp</th><th>Radius</th>
  </tr>
  <tr>
    <td>GeoFit</td>
    <td><small>0.4333</small></td><td>4</td><td>2</td><td>2.567</td>
    <td><small>0.4333</small></td><td>4</td><td>2</td><td>2.567</td>
  </tr>
  <tr>
    <td>AABB</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
  </tr>
  <tr>
    <td>Ritter F</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
  </tr>
  <tr>
    <td>Ritter L</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
    <td>0.4333</td><td>4</td><td>2</td><td>2.567</td>
  </tr>
</table>
