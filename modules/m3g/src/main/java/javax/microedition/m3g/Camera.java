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

public class Camera
	extends Node
{
	public static final int GENERIC =
		48;
	
	public static final int PARALLEL =
		49;
	
	public static final int PERSPECTIVE =
		50;
	
	public Camera()
	{
		throw Debugging.todo();
	}
	
	public int getProjection(float[] __a)
	{
		throw Debugging.todo();
	}
	
	public int getProjection(Transform __a)
	{
		throw Debugging.todo();
	}
	
	public void setGeneric(Transform __a)
	{
		throw Debugging.todo();
	}
	
	public void setParallel(float __a, float __b, float __c, float __d)
	{
		throw Debugging.todo();
	}
	
	public void setPerspective(float __a, float __b, float __c, float __d)
	{
		throw Debugging.todo();
	}
}


