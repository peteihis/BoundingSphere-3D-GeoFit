/*  
    Copyright (C) 2019 by Petri Ihalainen
    License: GPLv3
    Disclaimer: The author will not take resposibility of any consequences 
    of using, modifying, handling or redistributing this software.
*/

package peteihis.tools3D.boundingsphere;

import artofillusion.math.Vec3; // Replace by math package with 3D-vectors
import java.util.ArrayList;

/**
	<b>GeoFit</b> is an algorithm to define a minimal bounding sphere for a set of points 
	in 3D-space, applying a few simple geometrical rules.<p>
	
	The algoritm always produces perfect fit <i>(or so it seems so far)</i> within obtainable 
	numerical accuracy.<p>
	
	@author Petri Ihalainen
	@author (peteihis)
	@version 0.02, May-04-2019, generic version
*/

public class GeoFit
{
	private double radius2; // Squared radius
	private Vec3 center, exo;
	private int fit;
	private ArrayList<Vec3> supports;
	
	/** Create the solver. */
	
	public GeoFit()
	{}
	
	/** Calculate a bounding sphere for a set of vertices. */
	
	public BoundingSphere boundingSphere(Vec3[] vertex)
	{
		calculateParameters(vertex);
		BoundingSphere bou = new BoundingSphere(center, Math.sqrt(radius2), fit);
		return bou;
	}

	/** 
		This is the main loop of the algoritm. 
	*/

	private void calculateParameters(Vec3[] vertex)
	{
		if (vertex.length == 1)
		{
			radius2 = 0;
			center = vertex[0];
			return;
		}

		// Get initial 2-support sphere. If all fit in we are done.
		
		supports = new ArrayList<Vec3>();
		center = boxCenter(vertex);

		supports.add(mostDistant(center, 0.0, vertex));
		supports.add(mostDistant(supports.get(0), center.distance2(supports.get(0)), vertex));
		center = supports.get(0).plus(supports.get(1)).times(0.5);
		radius2 = Math.max(center.distance2(supports.get(0)), (center.distance2(supports.get(1))));
		exo = mostDistant(center, radius2, vertex);
		
		if (exo == null)
			return;

		// Go checking points, that were left outside. Favor smallest size, that can be obtained with 
		// lowest count of points. Increase number of supports only if a sub set can not enclose all.

		int supportCount = supports.size(); // = 2
		
		while (supportCount < 5 && exo != null)
		{
			while (supports.size() == supportCount && exo != null)
			{
				supports.add(exo);
				checkSubGroups(supports);
				exo = mostDistant(center, radius2, vertex);
			}
			supportCount = supports.size();
		}

		if (supportCount < 5 && exo == null)
			fit = BoundingSphere.EXACT;
		else
			fit = BoundingSphere.APPROXIMATE; // This point should never be reached. 
	}

	/**
		Check if a smaller set of supports can produce a sphere, that
		encoloses all the suggested supports. If so, the smallest possible 
		one is selected and the obsolete enclosed support excluded.
	*/
	
	private void checkSubGroups(ArrayList<Vec3> supports)
	{
		int size = supports.size();
		
		// Create subsets, where one of the supports is missing.
		// We are skippiung the last one, because we already know that the 
		// last added point does not fit the set, that was there before it.
		
		Vec3[][] groups = new Vec3[size-1][size-1];   // sub sets
		Vec3[] cnt = new Vec3[size-1];                // centers
		double[] r2  = new double[size-1];            // squared radii

		int n;
		for (int i = 0; i < groups.length; i++)
		{
			n = 0;
			for (int j = 0; j < size; j++)
			{
				if (j != i)
				{
					groups[i][n] = supports.get(j);
					n++;
				}
			}
		}

		// Calculate parameters for the sub sets and check if they can enclose
		// the entire set. At the same time, make note of the smallest one that 
		// can enclose all.

		double r2j, r2fit = Double.POSITIVE_INFINITY;
		int enclosing = -1;
		boolean[] allFit = new boolean[size];

		for (int i = 0; i < groups.length; i++) // The last one is the one that brought us here anyway...
		{
			cnt[i] = centerOf(groups[i]);
			r2[i] = radius2Of(cnt[i], groups[i]);
			allFit[i] = true;

			// Check if the one that was left out of this set fits in.
			
			if (cnt[i].distance2(supports.get(i)) > r2[i])
				allFit[i] = false;

			// Check is if this group is allFit and smaller than the previous allFit
			
			if (allFit[i] && r2[i] < r2fit)
			{
				r2fit = r2[i];
				enclosing = i;
			}
		}

		// Set the sphere parameters to the smallest enclosing subset or 
		// if one was not found, recalculate parameters with the entire set.

		if (enclosing > -1)
		{
			supports.clear();
			for (Vec3 g: groups[enclosing])
				supports.add(g);
			center = cnt[enclosing];
			radius2 = r2[enclosing];
		}
		else
		{
			if (supports.size() == 2)
				center = (supports.get(0).plus(supports.get(1)).times(0.5));
			else if (supports.size() == 3)
				center = centerOf3(supports.get(0), supports.get(1), supports.get(2));
			else
				center = centerOf4(supports.get(0), supports.get(1), supports.get(2), supports.get(3));
			radius2 = 0.0;
			for(Vec3 s: supports)
				radius2 = Math.max(radius2, center.distance2(s));
		}
	}

	/**
		Finds the most distant point from <pre>vecFrom</pre>. The distance of the found point 
		must be greater than <pre>Math.sqrt(limitDist2)</pre> or the method returns null.
		The square of the distance is used for speed and to reduce numerical error.
		The <pre>limitDist2</pre> should not be calculated backwards as square of a measured 
		distance, but as <pre>Vec3.distance2()</pre>.

		@param vecFrom    the point to measure from
		@param limitDist2 the 2nd power of the limit distance
		@param vertex     the group of vectors to select from
	*/

	private Vec3 mostDistant(Vec3 vecFrom, double limitDist2, Vec3[] vertex)
	{
		Vec3 mostDistant = null;
		double r2last = limitDist2;
		double r2v;
		for (Vec3 v : vertex)
		{
			r2v = vecFrom.distance2(v);
			if (r2v > r2last)
			{
				mostDistant = v;
				r2last = r2v;
			}
		}
		return mostDistant;
	}

	/**
		Calculates the "box center" (AABB center) for a set of vertices.
	*/
	
	public Vec3 boxCenter(Vec3[] vertex)
	{
		double minX, maxX, minY, maxY, minZ, maxZ;
		
		// The classical way would be to set the min and max values to the opposite 
		// infinity, but we can as well start at one of the existing points.
		
		minX = maxX = vertex[0].x; 
		minY = maxY = vertex[0].y; 
		minZ = maxZ = vertex[0].z; 
		
		for (Vec3 v: vertex)
		{
			minX = Math.min(minX, v.x);
			maxX = Math.max(maxX, v.x);
			minY = Math.min(minY, v.y);
			maxY = Math.max(maxY, v.y);
			minZ = Math.min(minZ, v.z);
			maxZ = Math.max(maxZ, v.z);
		}
		
		return new Vec3((maxX+minX)*0.5, (maxY+minY)*0.5, (maxZ+minZ)*0.5);
	}

	/** 
		Find the circumcenter of up to 4 support points.
		In 3D-space 4 is the highest possible number of 
		supports that are needed to define a sphere.
	*/

	private Vec3 centerOf(Vec3[] v)
	{
		if (v.length == 2)
			return (v[0].plus(v[1]).times(0.5));
		if (v.length == 3)
			return centerOf3(v[0], v[1], v[2]);
		return centerOf4(v[0], v[1], v[2], v[3]);
	}
	
	/** 
		Find the circumcenter of 3 support points.
	*/
	
	private Vec3 centerOf3(Vec3 v1, Vec3 v2, Vec3 v3)
	{
		// I did not mark down, where I got this. 
		// It is based on calculating barycentric weights 
		// for the corner points.

		double a2 = v2.distance2(v3);
		double b2 = v3.distance2(v1);
		double c2 = v1.distance2(v2);
		double wA = a2*(b2+c2-a2);
		double wB = b2*(c2+a2-b2);
		double wC = c2*(a2+b2-c2);
		double wSum = wA+wB+wC;

		return v1.times(wA).plus(v2.times(wB).plus(v3.times(wC))).times(1.0/wSum);
	}

	/**
		Find the circumcenter of 4 support points.
	*/

	private Vec3 centerOf4(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4)
	{
		// ...And I can't remember how I came up with this one either...
		// Anyway, the problem is simplified into two points and a straight line 
		// on which the center point is at equal distance from both of the two.

		Vec3 faceCenter = centerOf3(v1, v2, v3);
		Vec3 faceCenterTov4  = v4.minus(faceCenter);
		Vec3 dirDelta = (v2.minus(v1).cross(v3.minus(v1)));
		dirDelta.normalize();
		
		double r2face = (faceCenter.distance2(v1)+faceCenter.distance2(v2)+faceCenter.distance2(v3))/3.0;
		double delta = (faceCenterTov4.length2()-r2face)/(2*faceCenterTov4.dot(dirDelta));

		return faceCenter.plus(dirDelta.times(delta));
	}

	/**
		Find the maximum squared distance from c to each of vert.
	*/

	private double radius2Of(Vec3 c, Vec3[] vert)
	{
		double r2 = 0.0;
		for(Vec3 v: vert)
			r2 = Math.max(r2, c.distance2(v));
		return r2;
	}

	/**
		Calculate the difference between the largest and smallest
		center-to-support distances.
	*/

	private double error()
	{
		double r2min = radius2;
		for(Vec3 s: supports)
			r2min = Math.min(r2min, center.distance2(s));
		return Math.sqrt(radius2)-Math.sqrt(r2min);
	}
}
