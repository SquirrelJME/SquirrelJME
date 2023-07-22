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
public class CompositingMode
	extends Object3D
{
	@Api
	public static final int ALPHA =
		64;
	
	@Api
	public static final int ALPHA_ADD =
		65;
	
	@Api
	public static final int MODULATE =
		66;
	
	@Api
	public static final int MODULATE_X2 =
		67;
	
	@Api
	public static final int REPLACE =
		68;
	
	@Api
	public CompositingMode()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getAlphaThreshold()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getBlending()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getDepthOffsetFactor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getDepthOffsetUnits()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isAlphaWriteEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isColorWriteEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isDepthTestEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isDepthWriteEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setAlphaThreshold(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setAlphaWriteEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setBlending(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setColorWriteEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setDepthOffset(float __a, float __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setDepthTestEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setDepthWriteEnable(boolean __a)
	{
		throw Debugging.todo();
	}
}


