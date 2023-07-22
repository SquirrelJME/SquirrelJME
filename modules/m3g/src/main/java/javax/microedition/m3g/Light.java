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
public class Light
	extends Node
{
	@Api
	public static final int AMBIENT =
		128;
	
	@Api
	public static final int DIRECTIONAL =
		129;
	
	@Api
	public static final int OMNI =
		130;
	
	@Api
	public static final int SPOT =
		131;
	
	@Api
	public Light()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getColor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getConstantAttenuation()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getIntensity()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getLinearAttenuation()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getMode()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getQuadraticAttenuation()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getSpotAngle()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getSpotExponent()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setAttenuation(float __a, float __b, float __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setColor(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setIntensity(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setMode(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setSpotAngle(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setSpotExponent(float __a)
	{
		throw Debugging.todo();
	}
}


