// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.m3g;

import java.util.Hashtable;

public class Graphics3D
{
	public static final int ANTIALIAS =
		2;
	
	public static final int DITHER =
		4;
	
	public static final int OVERWRITE =
		16;
	
	public static final int TRUE_COLOR =
		8;
	
	private Graphics3D()
	{
		super();
		throw new Error("TODO");
	}
	
	public int addLight(Light __a, Transform __b)
	{
		throw new Error("TODO");
	}
	
	public void bindTarget(Object __a)
	{
		throw new Error("TODO");
	}
	
	public void bindTarget(Object __a, boolean __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public void clear(Background __a)
	{
		throw new Error("TODO");
	}
	
	public Camera getCamera(Transform __a)
	{
		throw new Error("TODO");
	}
	
	public float getDepthRangeFar()
	{
		throw new Error("TODO");
	}
	
	public float getDepthRangeNear()
	{
		throw new Error("TODO");
	}
	
	public int getHints()
	{
		throw new Error("TODO");
	}
	
	public Light getLight(int __a, Transform __b)
	{
		throw new Error("TODO");
	}
	
	public int getLightCount()
	{
		throw new Error("TODO");
	}
	
	public Object getTarget()
	{
		throw new Error("TODO");
	}
	
	public int getViewportHeight()
	{
		throw new Error("TODO");
	}
	
	public int getViewportWidth()
	{
		throw new Error("TODO");
	}
	
	public int getViewportX()
	{
		throw new Error("TODO");
	}
	
	public int getViewportY()
	{
		throw new Error("TODO");
	}
	
	public boolean isDepthBufferEnabled()
	{
		throw new Error("TODO");
	}
	
	public void releaseTarget()
	{
		throw new Error("TODO");
	}
	
	public void render(Node __a, Transform __b)
	{
		throw new Error("TODO");
	}
	
	public void render(VertexBuffer __a, IndexBuffer __b, Appearance __c, 
		Transform __d)
	{
		throw new Error("TODO");
	}
	
	public void render(VertexBuffer __a, IndexBuffer __b, Appearance __c, 
		Transform __d, int __e)
	{
		throw new Error("TODO");
	}
	
	public void render(World __a)
	{
		throw new Error("TODO");
	}
	
	public void resetLights()
	{
		throw new Error("TODO");
	}
	
	public void setCamera(Camera __a, Transform __b)
	{
		throw new Error("TODO");
	}
	
	public void setDepthRange(float __a, float __b)
	{
		throw new Error("TODO");
	}
	
	public void setLight(int __a, Light __b, Transform __c)
	{
		throw new Error("TODO");
	}
	
	public void setViewport(int __a, int __b, int __c, int __d)
	{
		throw new Error("TODO");
	}
	
	public static final Graphics3D getInstance()
	{
		throw new Error("TODO");
	}
	
	public static final Hashtable getProperties()
	{
		throw new Error("TODO");
	}
}


