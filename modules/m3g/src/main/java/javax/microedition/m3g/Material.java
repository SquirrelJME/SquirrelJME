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

@Api
public class Material
	extends Object3D
{
	@Api
	public static final int AMBIENT =
		1024;
	
	@Api
	public static final int DIFFUSE =
		2048;
	
	@Api
	public static final int EMISSIVE =
		4096;
	
	@Api
	public static final int SPECULAR =
		8192;
	
	@Api
	public Material()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getColor(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getShininess()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isVertexColorTrackingEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setColor(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setShininess(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setVertexColorTrackingEnable(boolean __a)
	{
		throw Debugging.todo();
	}
}


