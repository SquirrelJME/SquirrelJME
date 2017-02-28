// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.m3g;


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
		super();
		throw new todo.TODO();
	}
	
	public final void align(Node __a)
	{
		throw new todo.TODO();
	}
	
	public Node getAlignmentReference(int __a)
	{
		throw new todo.TODO();
	}
	
	public int getAlignmentTarget(int __a)
	{
		throw new todo.TODO();
	}
	
	public float getAlphaFactor()
	{
		throw new todo.TODO();
	}
	
	public Node getParent()
	{
		throw new todo.TODO();
	}
	
	public int getScope()
	{
		throw new todo.TODO();
	}
	
	public boolean getTransformTo(Node __a, Transform __b)
	{
		throw new todo.TODO();
	}
	
	public boolean isPickingEnabled()
	{
		throw new todo.TODO();
	}
	
	public boolean isRenderingEnabled()
	{
		throw new todo.TODO();
	}
	
	public void setAlignment(Node __a, int __b, Node __c, int __d)
	{
		throw new todo.TODO();
	}
	
	public void setAlphaFactor(float __a)
	{
		throw new todo.TODO();
	}
	
	public void setPickingEnable(boolean __a)
	{
		throw new todo.TODO();
	}
	
	public void setRenderingEnable(boolean __a)
	{
		throw new todo.TODO();
	}
	
	public void setScope(int __a)
	{
		throw new todo.TODO();
	}
}


