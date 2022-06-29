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

public abstract class Node
	extends Transformable
{
	public static final int NONE =
		144;
	
	public static final int ORIGIN =
		145;
	
	public static final int X_AXIS =
		146;
	
	public static final int Y_AXIS =
		147;
	
	public static final int Z_AXIS =
		148;
	
	Node()
	{
		throw Debugging.todo();
	}
	
	public final void align(Node __a)
	{
		throw Debugging.todo();
	}
	
	public Node getAlignmentReference(int __a)
	{
		throw Debugging.todo();
	}
	
	public int getAlignmentTarget(int __a)
	{
		throw Debugging.todo();
	}
	
	public float getAlphaFactor()
	{
		throw Debugging.todo();
	}
	
	public Node getParent()
	{
		throw Debugging.todo();
	}
	
	public int getScope()
	{
		throw Debugging.todo();
	}
	
	public boolean getTransformTo(Node __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	public boolean isPickingEnabled()
	{
		throw Debugging.todo();
	}
	
	public boolean isRenderingEnabled()
	{
		throw Debugging.todo();
	}
	
	public void setAlignment(Node __a, int __b, Node __c, int __d)
	{
		throw Debugging.todo();
	}
	
	public void setAlphaFactor(float __a)
	{
		throw Debugging.todo();
	}
	
	public void setPickingEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	public void setRenderingEnable(boolean __a)
	{
		throw Debugging.todo();
	}
	
	public void setScope(int __a)
	{
		throw Debugging.todo();
	}
}


