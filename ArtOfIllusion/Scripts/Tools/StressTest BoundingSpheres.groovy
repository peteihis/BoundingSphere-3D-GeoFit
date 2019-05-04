
// This is a mass test to bounding sphere algorithms.
// The test creates spherical random direction vector clouds, where the lengths of 
// the vectors vary slightly. Then it has the bounding sphere algorithms 
// prodice bounding spheres on them ana repots processing times and in the case of 
// GeoFit bounding sphere some other data.
 
import artofillusion.boundingsphere.*;

// ===    USER PARAMETERS     ===

n = 100000; // number of vectors in a cloud
f = 0.001;  // relative randomization
runs = 20;  // number of vector clouds

//n = 32;   // alternative values
//f = 1.0;

// === END OF USER PARAMETERS ===

rand = new Random(0);

Vec3 randomDirectionVector()
{
	Vec3 v = new Vec3();
	while (v.length() > 1.0 || v.length() == 0.0)
	{
		v.x = rand.nextDouble()*2.0-1.0;
		v.y = rand.nextDouble()*2.0-1.0;
		v.z = rand.nextDouble()*2.0-1.0;
	}
	v. normalize();
	return v;
}

geofit = new GeoFit();
ritter = new Ritter();

bsSum = 0; fsSum = 0; rFSum = 0; rLSum = 0;

Thread.start
{
	println("\nCreating " + runs + " spherical vector clouds of " + n + " points \nwith " + f + " relative randomization...");
	Vec3[][] rndSpheres = new Vec3[runs][n];

	for (run = 0; run < runs; run++)
		for (i = 0; i < n; i++)
		{
			rndSpheres[run][i] = randomDirectionVector();
			rndSpheres[run][i].scale(1.0 + rand.nextDouble()*f);
		}

	println("\nGeofit.boundingSphere()\nSolver time\tPasses\tSupports");

	for (run = 0; run < runs; run++)
	{
		bSphere = geofit.boundingSphere(rndSpheres[run]);
		bsSum += bSphere.solverTime;
		st = bSphere.solverTime.toString();
		if (st.length() > 9)
			st = st.substring(0, 9);
		println(st + "\t" + bSphere.passes + "\t" + bSphere.supportPositions.size());
	}

	println("\nGeoFit.fastphere()\nSolver time");

	for (run = 0; run < runs; run++)
	{
		bSphere = geofit.fastSphere(rndSpheres[run]);
		fsSum += bSphere.solverTime;
		st = bSphere.solverTime.toString();
		if (st.length() > 12)
			st = st.substring(0, 12);
		println(st);
	}

	println("\nRitter.boundingSphere() mode LAST\nSolver time");

	for (run = 0; run < runs; run++)
	{
		bSphere = ritter.boundingSphere(rndSpheres[run]);
		rFSum += bSphere.solverTime;
		st = bSphere.solverTime.toString();
		if (st.length() > 12)
			st = st.substring(0, 12);
		println(st);
	}

	println("\nRitter.boundingSphere() mode LAST\nSolver time");

	ritter.mode(ritter.LAST);
	for (run = 0; run < runs; run++)
	{
		bSphere = ritter.boundingSphere(rndSpheres[run]);
		rLSum += bSphere.solverTime;
		st = bSphere.solverTime.toString();
		if (st.length() > 12)
			st = st.substring(0, 12);
		println(st);
	}

	println("\nAverage relative times in comparison to Ritter in mode FIRST:");
	println("   GeoFit.boundingSphere() \t" + bsSum/rFSum);
	println("   GeoFit.fastSphere()     \t" + fsSum/rFSum);
	println("   Ritter in mode LAST     \t" + rLSum/rFSum);

} // thread	

