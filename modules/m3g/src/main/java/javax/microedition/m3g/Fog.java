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
public class Fog
	extends Object3D
{
	@Api
	public static final int EXPONENTIAL =
		80;
	
	@Api
	public static final int LINEAR =
		81;
	
	@Api
	public Fog()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getColor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getDensity()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getFarDistance()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getMode()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getNearDistance()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setColor(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setDensity(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setLinear(float __a, float __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setMode(int __a)
	{
		throw Debugging.todo();
	}
}


