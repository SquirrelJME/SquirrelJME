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
public abstract class Node
	extends Transformable
{
	@Api
	public static final int NONE =
		144;
	
	@Api
	public static final int ORIGIN =
		145;
	
	@Api
	public static final int X_AXIS =
		146;
	
	@Api
	public static final int Y_AXIS =
		147;
	
	@Api
	public static final int Z_AXIS =
		148;
	
	Node()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void align(Node __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Node getAlignmentReference(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getAlignmentTarget(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public float getAlphaFactor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Node getParent()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getScope()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean getTransformTo(Node __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isPickingEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isRenderingEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setAlignment(Node __a, int __b, Node __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setAlphaFactor(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setPickingEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setRenderingEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setScope(int __a)
	{
		throw Debugging.todo();
	}
}


