// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.m3g;

import cc.squirreljme.runtime.cldc.debug.Debugging;
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
		throw Debugging.todo();
	}
	
	public int addLight(Light __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	public void bindTarget(Object __a)
	{
		throw Debugging.todo();
	}
	
	public void bindTarget(Object __a, boolean __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public void clear(Background __a)
	{
		throw Debugging.todo();
	}
	
	public Camera getCamera(Transform __a)
	{
		throw Debugging.todo();
	}
	
	public float getDepthRangeFar()
	{
		throw Debugging.todo();
	}
	
	public float getDepthRangeNear()
	{
		throw Debugging.todo();
	}
	
	public int getHints()
	{
		throw Debugging.todo();
	}
	
	public Light getLight(int __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	public int getLightCount()
	{
		throw Debugging.todo();
	}
	
	public Object getTarget()
	{
		throw Debugging.todo();
	}
	
	public int getViewportHeight()
	{
		throw Debugging.todo();
	}
	
	public int getViewportWidth()
	{
		throw Debugging.todo();
	}
	
	public int getViewportX()
	{
		throw Debugging.todo();
	}
	
	public int getViewportY()
	{
		throw Debugging.todo();
	}
	
	public boolean isDepthBufferEnabled()
	{
		throw Debugging.todo();
	}
	
	public void releaseTarget()
	{
		throw Debugging.todo();
	}
	
	public void render(Node __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	public void render(VertexBuffer __a, IndexBuffer __b, Appearance __c, 
		Transform __d)
	{
		throw Debugging.todo();
	}
	
	public void render(VertexBuffer __a, IndexBuffer __b, Appearance __c, 
		Transform __d, int __e)
	{
		throw Debugging.todo();
	}
	
	public void render(World __a)
	{
		throw Debugging.todo();
	}
	
	public void resetLights()
	{
		throw Debugging.todo();
	}
	
	public void setCamera(Camera __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	public void setDepthRange(float __a, float __b)
	{
		throw Debugging.todo();
	}
	
	public void setLight(int __a, Light __b, Transform __c)
	{
		throw Debugging.todo();
	}
	
	public void setViewport(int __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	public static final Graphics3D getInstance()
	{
		throw Debugging.todo();
	}
	
	public static final Hashtable getProperties()
	{
		throw Debugging.todo();
	}
}


