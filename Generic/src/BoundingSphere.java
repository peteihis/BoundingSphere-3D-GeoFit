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
    <b>BoundingSphere</b> is an enclosing volume to a set of points in 3D-space.
    A BoundingSphere may be defined by a variety of different tools and it
    has some simple methods for detecting interaction with other BoundingSpheres.

    @author Petri Ihalainen
    @author (peteihis)
    @version 0.02, May-04-2019, generic version

    PLEASE NOTE THAT MOST OF THE METHODS BELOW HAVE NOT BEEN FULLY TESTED!
*/

public class BoundingSphere
{
    public Vec3 center;
    public double radius;
    public int fit;

    /** Value for fit. */
    public static int UNKNOWN = -1, EXACT = 0, NON_MINIMAL = 1, APPROXIMATE = 2;

    /**
        Define an empty BoundingSphere
    */

    public BoundingSphere()
    {
        center      = new Vec3();
        radius      = 0.0;
        fit         = UNKNOWN;
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
        fit         = UNKNOWN;
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
        this.fit    = fit;
    }

    /**
        Check if another BoundingSphere is partially or entirely inside this BoundingSphere.
        Surface-to-surface distance 0.0 is not considered a collision.<p>

        NOT TESTED!
    */

    public boolean collides(BoundingSphere sphere)
    {
        return (center.distance(sphere.center) < radius + sphere.radius);
    }

    /**
        Check if the surface of another BoundingSphere is just touching the surface of this BoundingSphere.<p>

        NOT TESTED!
    */

    public boolean contacts(BoundingSphere sphere, double tolerance)
    {
        double centerToCenter = center.distance(sphere.center);
        double sumOfRadii  = radius + sphere.radius;
        return (centerToCenter > sumOfRadii - tolerance && centerToCenter < sumOfRadii + tolerance);
    }

    /**
        Check another BoundingSphere is entirely inside this BoundingSphere. Surfaces may still be in contact.<p>

        NOT TESTED!
    */

    public boolean contains(BoundingSphere sphere)
    {
        return (center.distance(sphere.center)+sphere.radius <= radius);
    }

    /**
        The direction in which direction the other BoundingSphere is.<p>

        NOT TESTED!
    */

    public Vec3 direction(BoundingSphere sphere)
    {
        Vec3 dir = sphere.center.minus(center);
        dir.normalize();
        return dir;
    }

    /**
        Minimum distance between the surface of this BoundingSphere and the surface of the other.
        A negative value is reported in case of collision.<p>

        NOT TESTED!
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