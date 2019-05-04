
// A scritp to test bounding sphere algorithms.
// The scritp has the produced spheres drawn into the scene.

import artofillusion.boundingsphere.*;

if (window.getSelectedObjects().size() == 0)
{
	println ("\nSelect one or more objects first!\n")
	return;
}

// Create the tools

geofit = new GeoFit();
ritter = new Ritter();

// Create bounding spheres

if (window.getSelectedObjects().size() == 1)
{
	info = window.getSelectedObjects().get(0);
	name = info.getName();
	boundS = geofit.boundingSphere(info);
	fastS  = geofit.fastSphere(info);
	ritterF = ritter.boundingSphere(info);
	ritter.mode(Ritter.LAST);
	ritterL = ritter.boundingSphere(info);
}
else
{
	infoList = window.getSelectedObjects();
	name = "Selection of " + infoList.size() + " objects";
	boundS = geofit.boundingSphere(infoList);
	fastS  = geofit.fastSphere(infoList);
	ritterF = ritter.boundingSphere(infoList);
	ritter.mode(Ritter.LAST);
	ritterL = ritter.boundingSphere(infoList);
}

// ...And print out some of their proiperties

println("\n" + name);
println("\nSolverTimes in ms");
println("  BoundingSphere " + boundS.solverTime);
println("  FastSphere     " + fastS.solverTime);
println("  Ritter FIRST   " + ritterF.solverTime);
println("  Ritter LAST    " + ritterL.solverTime);

println("\nRadii");
println("  BoundingSphere " + boundS.radius);
println("  FastSphere     " + fastS.radius);
println("  Ritter FIRST   " + ritterF.radius);
println("  Ritter LAST    " + ritterL.radius);

println("\nPasses");
println("  BoundingSphere " + boundS.passes);
println("  FastSphere     " + fastS.passes);
println("  Ritter FIRST   " + ritterF.passes);
println("  Ritter LAST    " + ritterL.passes);

println("\nSolverTimes in comparison to Ritter.boundingSphere in mode FIRST");
println("  BoundingSphere " + Math.round((boundS.solverTime/ritterF.solverTime)*100)*0.01);
println("  FastSphere     " + Math.round((fastS.solverTime/ritterF.solverTime)*100)*0.01);
println("  Ritter LAST    " + Math.round((ritterL.solverTime/ritterF.solverTime)*100)*0.01);

println("\nOversizes in comparison to GeoFit.boundingSphere()");
println("  FastSphere     " + Math.round((fastS.radius/boundS.radius-1)*1000)*0.1   + "%");
println("  Ritter FIRST   " + Math.round((ritterF.radius/boundS.radius-1)*1000)*0.1 + "%");
println("  Ritter LAST    " + Math.round((ritterL.radius/boundS.radius-1)*1000)*0.1 + "%");

println("\nGeoFit.boundingSphere\n  error: " + boundS.error + "\n  supports: " + boundS.supportPositions.size());

// Draw the spheres into the scene

s = new Sphere(boundS.radius, boundS.radius, boundS.radius)
c = new CoordinateSystem();
c.setOrigin(boundS.center);
window.addObject(s, c, name + " BoundingSphere", null);

s = new Sphere(fastS.radius, fastS.radius, fastS.radius)
c = new CoordinateSystem();
c.setOrigin(fastS.center);
window.addObject(s, c, name + " FastSphere", null);

s = new Sphere(ritterF.radius, ritterF.radius, ritterF.radius)
c = new CoordinateSystem();
c.setOrigin(ritterF.center);
window.addObject(s,c, name + " Ritter FIRST", null);

s = new Sphere(ritterL.radius, ritterL.radius, ritterL.radius)
c = new CoordinateSystem();
c.setOrigin(ritterL.center);
window.addObject(s, c, name + " Ritter LAST", null);

window.updateImage();
