/*  Copyright (C) 2019 by Petri Ihalainen */
/*  License: GPLv3                        */

package artofillusion.boundingsphere;

import artofillusion.*;
import artofillusion.math.*;
import java.util.ArrayList;

/**
	<b>BoundingSphere</b> is an enclosing volume to a set of points in 3D-space.
	A BoundingSphere may be defined by a variety of different tools and it 
	has some simple methods for detecting interaction with other BoundingSpheres.
	
	@author Petri Ihalainen
	@author (peteihis)
	@version 0.02, May-04-2019, for Art of Illusion
*/

public class BoundingSphere
{
	public Vec3 center;
	public double radius, error, solverTime;
	
	/** Preferably <code>'millisecond'</code> */
	public String timeUnit;
	
	/** The id-number of the object this BoundingSphere was created on. */
	public int infoID;
	
	/** Quality of the fit. The default is <code>'UNKNOWN'</code>. */
	public int fit;
	
	/** Value for the quality of the fit. */
	public static int UNKNOWN = -1, EXACT = 0, NON_MINIMAL = 1, APPROXIMATE = 2;
	
	/** The number of times the data was read during calculation of the parameters.*/
	public int passes;
	
	/** Where the points, that defined this BoundinSphere, were at the time. */
	public ArrayList<Vec3> supportPositions;
	
	//public ArrayList<Integer> supportIndices;
	
	/** The class, that creates the sphere, may write additional information here or the class using it may store data.*/
	public String metatext;

	/**
		Define an empty BoundingSphere
	*/
	
	public BoundingSphere()
	{
		center      = new Vec3();
		radius      = 0.0;
		infoID      = 0;
		fit         = UNKNOWN;
		solverTime  = 0.0;
		passes      = 0;
		timeUnit    = new String();
	}

	/**
		Create a predefined BoundingSphere
		
		@param center   The centerpoint of the sphere as <code>Vec3</code>
		@param radius   The radius of the sphere.
	*/

	public BoundingSphere(Vec3 center, double radius)
	{
		this.center = center;
		this.radius = radius;
		infoID      = 0;
		fit         = UNKNOWN;
		solverTime  = 0.0;
		passes      = 0;
		timeUnit    = new String();
	}

	/**
		Define a BoundingSphere
		
		@param center   The centerpoint of the sphere as <code>Vec3</code>
		@param radius   The radius of the sphere.
		@param fit      One of <code>UNKNOWN, EXACT, NON_MINIMAL, APPROXIMATE</code>
	*/

	public BoundingSphere(Vec3 center, double radius, int fit)
	{
		this.center = center;
		this.radius = radius;
		infoID      = 0;
		this.fit    = fit;
		solverTime  = 0.0;
		passes      = 0;
		timeUnit    = new String();
	}

	/**
		Define a BoundingSphere
		
		@param center   The centerpoint of the sphere as <code>Vec3</code>
		@param radius   The radius of the sphere.
		@param fit      One of <code>UNKNOWN, EXACT, NON_MINIMAL, APPROXIMATE</code>
		@param infoID   Optional. The ID-number of the objectinfo, the sphere was created for.
	*/

	public BoundingSphere(Vec3 center, double radius, int fit, int infoID)
	{
		this.center = center;
		this.radius = radius;
		this.fit    = fit;
		this.infoID = infoID;
		solverTime  = 0.0;
		passes      = 0;
		timeUnit    = new String();
	}

	/**
		Check if another BoundingSphere is partially or entirely inside this BoundingSphere.
		Surface-to-surface distance 0.0 is not considered a collision.
	*/
	
	public boolean collides(BoundingSphere sphere)
	{
		return (center.distance(sphere.center) < radius + sphere.radius);
	}

	/**
		Check if the surface of another BoundingSphere is just touching the surface of this BoundingSphere.
	*/
	
	public boolean contacts(BoundingSphere sphere, double tolerance)
	{
		double centerToCenter = center.distance(sphere.center);
		double sumOfRadii  = radius + sphere.radius;
		return (centerToCenter > sumOfRadii - tolerance && centerToCenter < sumOfRadii + tolerance);
	}

	/**
		Check another BoundingSphere is entirely inside this BoundingSphere. Surfaces may still be in contact.
	*/
	
	public boolean contains(BoundingSphere sphere)
	{
		return (center.distance(sphere.center)+sphere.radius <= radius);
	}
	
	/**
		The direction in which direction the other BoundingSphere is.
	*/
	
	public Vec3 direction(BoundingSphere sphere)
	{
		Vec3 dir = sphere.center.minus(center);
		dir.normalize();
		return dir;
	}
	
	/**
		Minimum distance between the surface of this BoundingSphere and the surface of the other.
		A negative value is reported in case of collision.
	*/
	
	public double distance(BoundingSphere sphere)
	{
		double centerToCenter = center.distance(sphere.center);
		double sumOfRadii  = radius + sphere.radius;
		return centerToCenter - sumOfRadii;
	}
	
	/** Get a verbal description of the BoundingSphere */
	
	@Override
	public String toString()
	{
		String description = new String();
		
		description = description + "BoundingSphere[";
		description = description + "Center: " + center.x + " " + center.y + " " + center.z + " ";
		description = description + "Radius: " + radius + " ";
		if (infoID != 0)
			description = description + "ObjectInfoId: " + infoID + " ";
		description = description + "Fit: ";
		if (fit == UNKNOWN)
			description = description + "UNKNOWN";
		if (fit == EXACT)
			description = description + "EXACT";
		if (fit == NON_MINIMAL)
			description = description + "NON_MINIMAL";
		if (fit == APPROXIMATE)
			description = description + "APPROXIMATE";
		description = description + "]";
		
		return description;
	}
}