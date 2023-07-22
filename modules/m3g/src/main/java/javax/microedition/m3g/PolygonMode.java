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
public class PolygonMode
	extends Object3D
{
	@Api
	public static final int CULL_BACK =
		160;
	
	@Api
	public static final int CULL_FRONT =
		161;
	
	@Api
	public static final int CULL_NONE =
		162;
	
	@Api
	public static final int SHADE_FLAT =
		164;
	
	@Api
	public static final int SHADE_SMOOTH =
		165;
	
	@Api
	public static final int WINDING_CCW =
		168;
	
	@Api
	public static final int WINDING_CW =
		169;
	
	@Api
	public PolygonMode()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getCulling()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getShading()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getWinding()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isLocalCameraLightingEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isPerspectiveCorrectionEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isTwoSidedLightingEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setCulling(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setLocalCameraLightingEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setPerspectiveCorrectionEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setShading(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setTwoSidedLightingEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setWinding(int __a)
	{
		throw Debugging.todo();
	}
}


