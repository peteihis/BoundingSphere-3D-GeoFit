/*
    Copyright (C) 2019 by Petri Ihalainen
    License: GPLv3
    Disclaimer: The author will not take resposibility of any consequences
    of using, modifying, handling or redistributing this software.
*/

package artofillusion.boundingsphere;

import artofillusion.*;
import artofillusion.math.*;
import artofillusion.object.*;
import java.util.ArrayList;

/**
	<b>Ritter2</b> is an implementation of "An efficient bounding sphere V.2" by Jack Ritter 1990, 
	as described in the published document on 
	<a href = "https://www.researchgate.net/publication/242453691_An_Efficient_Bounding_Sphere">www.researchgate.net</a>.<p>
	
	Variables have been renamed and values for x, y and z are not calculated as separate 
	entities. Vectors are used instead. Also this method recognizes if the first pass produces 
	the final sphere. In that case the fit is marked "EXACT", otherwise "NON_MINIMAL".<p>
	
	You can use <code>mode(int mode)</code> to have the first pass use the <b>first found</b> or 
	the <b>last found</b> points to define the extemities in each coordinate direction. 
	Which to use was not instructed on the description.<p>
	
	@author Petri Ihalainen
	@author (peteihis)
	@version 0.02, May-04-2019, for Art of Illusion
*/

public class Ritter
{
	private Vec3 vMinX, vMaxX, vMinY, vMaxY, vMinZ, vMaxZ;
	private Vec3 center;
	private double radius, radiusAtPass1;
	private int fit, passes;
	private long t0, t1;
	private int mode;

	/** Value for <code>mode()</code> */
	public static int FIRST = 1, LAST = 2;
	
	/** Create the solver */
	
	public Ritter()
	{
		mode = FIRST;
	}

	/** Set wether to use the first or the last found of similar points in 'pass1'. */
	
	public int mode(int mode)
	{
		if (mode == LAST)
			this.mode = LAST;
		else
			this.mode = FIRST;
		return mode;
	}

	/** Check the current mode. */
	
	public int mode()
	{
		return mode;
	}

	/** Create a boundincg sphere for a set of 3D-points. */
	
	public BoundingSphere boundingSphere(Vec3[] vertex)
	{
		passes = 0;
		if (mode == FIRST)
			pass1(vertex);
		else
			pass1UseLast(vertex);
		pass2(vertex);
		if (radius == radiusAtPass1)
			fit = BoundingSphere.EXACT;
		else
			fit = BoundingSphere.NON_MINIMAL;

		BoundingSphere bou = new BoundingSphere(center, radius, fit);
		bou.solverTime = (t1-t0)*1e-6;
		bou.timeUnit = "millisecond";
		bou.passes = passes;
		bou.metatext = "Created by Ritter1.boundingSphere()";
		
		return bou;
	}

	/** Calculate a boundincg sphere for an object on in a scene. 
	    If no preview mesh is available the bounding box is used.*/
	
	public BoundingSphere boundingSphere(ObjectInfo info)
	{
		passes = 0;
		if (info.getPreviewMesh() == null)
		{
			if (mode == FIRST)
				pass1(info.getBounds().getCorners());
			else
				pass1UseLast(info.getBounds().getCorners());
			pass2(info.getBounds().getCorners());
		}
		else
		{
			if (mode == FIRST)
				pass1(info.getPreviewMesh().vert);
			else
				pass1UseLast(info.getPreviewMesh().vert);
			pass2(info.getPreviewMesh().vert);
		}
		Mat4 toScene = info.getCoords().fromLocal();
		if (radius == radiusAtPass1)
			fit = BoundingSphere.EXACT;
		else
			fit = BoundingSphere.NON_MINIMAL;
		BoundingSphere bou = new BoundingSphere(toScene.times(center), radius, fit, info.getId());
		bou.solverTime = (t1-t0)*1e-6;
		bou.timeUnit = "millisecond";
		bou.passes = passes;
		bou.metatext = "Created by Ritter1.boundingSphere()";

		return bou;
	}

	/** Create a boundincg sphere for a selection of objects. 
	    If no preview mesh is available for an object, the bounding box is used. */

	public BoundingSphere boundingSphere(ArrayList<ObjectInfo> infoList)
	{
		passes = 0;
		ArrayList<Vec3> vertexList = new ArrayList<Vec3>();
		Mat4 toScene;
		Vec3[] vObj;
		for (ObjectInfo info: infoList)
		{
			toScene = info.getCoords().fromLocal();
			if (info.getPreviewMesh() == null)
				vObj = info.getBounds().getCorners();
			else
				vObj = info.getPreviewMesh().vert;
			for(Vec3 v: vObj)
				vertexList.add(toScene.times(v));
		}
		
		Vec3[] vertex = new Vec3[vertexList.size()];
		for(int i = 0; i < vertex.length; i++)
			vertex[i] = vertexList.get(i);

		if (mode == FIRST)
			pass1(vertex);
		else
			pass1UseLast(vertex);
		pass2(vertex);

		BoundingSphere bou = new BoundingSphere(center, radius, fit);
		bou.solverTime = (t1-t0)*1e-6;
		bou.timeUnit = "millisecond";
		bou.passes = passes;
		bou.metatext = "Created by Ritter1.boundingSphere()";
		
		return bou;
	}

	private void pass1(Vec3[] vertex)
	{
		passes++;
		t0 = System.nanoTime();
		vMinX = vMaxX = vMinY =  vMaxY = vMinZ = vMaxZ = vertex[0];
		
		for (Vec3 v: vertex)
		{
			if (v.x < vMinX.x) vMinX = v;
			if (v.x > vMaxX.x) vMaxX = v;
			if (v.y < vMinY.y) vMinY = v;
			if (v.y > vMaxY.y) vMaxY = v;
			if (v.z < vMinZ.z) vMinZ = v;
			if (v.z > vMaxZ.z) vMaxZ = v;
		}
		Vec3 A = vMinX;
		Vec3 B = vMaxX;
		if (A.distance2(B) < vMinY.distance2(vMaxY))
		{
			A = vMinY;
			B = vMaxY;
		}
		if (A.distance2(B) < vMinZ.distance2(vMaxZ))
		{
			A = vMinZ;
			B = vMaxZ;
		}
		center = A.plus(B).times(0.5);
		radius = Math.max(center.distance(A), center.distance(B));
		radiusAtPass1 = radius;
	}

	private void pass1UseLast(Vec3[] vertex)
	{
		passes++;
		t0 = System.nanoTime();
		vMinX = vMaxX = vMinY =  vMaxY = vMinZ = vMaxZ = vertex[0];
		
		for (Vec3 v: vertex)
		{
			if (v.x <= vMinX.x) vMinX = v;
			if (v.x >= vMaxX.x) vMaxX = v;
			if (v.y <= vMinY.y) vMinY = v;
			if (v.y >= vMaxY.y) vMaxY = v;
			if (v.z <= vMinZ.z) vMinZ = v;
			if (v.z >= vMaxZ.z) vMaxZ = v;
		}
		Vec3 A = vMinX;
		Vec3 B = vMaxX;
		if (A.distance2(B) < vMinY.distance2(vMaxY))
		{
			A = vMinY;
			B = vMaxY;
		}
		if (A.distance2(B) < vMinZ.distance2(vMaxZ))
		{
			A = vMinZ;
			B = vMaxZ;
		}
		center = A.plus(B).times(0.5);
		radius = Math.max(center.distance(A), center.distance(B));
		radiusAtPass1 = radius;
	}

	private void pass2(Vec3[] vertex)
	{
		passes++;
		double r2 = radius*radius;
		double r2v, move, rv;
		Vec3 vFromCenter;
		for (Vec3 v: vertex)
		{
			vFromCenter = v.minus(center);
			r2v = vFromCenter.length2();
			if (r2v > r2)
			{
				rv = vFromCenter.length();
				radius = (radius + rv)*0.5;
				r2 = radius*radius;
				move = rv-radius;
				center = (center.times(radius).plus(v.times(move))).times(1.0/rv);
			}
		}
		t1 = System.nanoTime();
	}
}
