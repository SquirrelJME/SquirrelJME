// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.m3g;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Hashtable;

@SuppressWarnings("ClassWithOnlyPrivateConstructors")
@Api
public class Graphics3D
{
	@Api
	public static final int ANTIALIAS =
		2;
	
	@Api
	public static final int DITHER =
		4;
	
	@Api
	public static final int OVERWRITE =
		16;
	
	@Api
	public static final int TRUE_COLOR =
		8;
	
	private Graphics3D()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int addLight(Light __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void bindTarget(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void bindTarget(Object __a, boolean __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void clear(Background __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Camera getCamera(Transform __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getDepthRangeFar()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getDepthRangeNear()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getHints()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Light getLight(int __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getLightCount()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Object getTarget()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getViewportHeight()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getViewportWidth()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getViewportX()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getViewportY()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isDepthBufferEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void releaseTarget()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void render(Node __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void render(VertexBuffer __a, IndexBuffer __b, Appearance __c, 
		Transform __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void render(VertexBuffer __a, IndexBuffer __b, Appearance __c, 
		Transform __d, int __e)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void render(World __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void resetLights()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setCamera(Camera __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setDepthRange(float __a, float __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setLight(int __a, Light __b, Transform __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setViewport(int __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static final Graphics3D getInstance()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static final Hashtable getProperties()
	{
		throw Debugging.todo();
	}
}


