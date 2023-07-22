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
public class Camera
	extends Node
{
	@Api
	public static final int GENERIC =
		48;
	
	@Api
	public static final int PARALLEL =
		49;
	
	@Api
	public static final int PERSPECTIVE =
		50;
	
	@Api
	public Camera()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getProjection(float[] __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getProjection(Transform __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setGeneric(Transform __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setParallel(float __a, float __b, float __c, float __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setPerspective(float __a, float __b, float __c, float __d)
	{
		throw Debugging.todo();
	}
}


