// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;


public abstract class CustomItem
	extends Item
{
	protected static final int KEY_PRESS =
		4;
	
	protected static final int KEY_RELEASE =
		8;
	
	protected static final int KEY_REPEAT =
		16;
	
	protected static final int NONE =
		0;
	
	protected static final int POINTER_DRAG =
		128;
	
	protected static final int POINTER_PRESS =
		32;
	
	protected static final int POINTER_RELEASE =
		64;
	
	protected static final int TRAVERSE_HORIZONTAL =
		1;
	
	protected static final int TRAVERSE_VERTICAL =
		2;
	
	protected CustomItem(String __a)
	{
		throw new todo.TODO();
	}
	
	protected abstract int getMinContentHeight();
	
	protected abstract int getMinContentWidth();
	
	protected abstract int getPrefContentHeight(int __a);
	
	protected abstract int getPrefContentWidth(int __a);
	
	protected abstract void paint(Graphics __a, int __b, int __c);
	
	public int getGameAction(int __a)
	{
		throw new todo.TODO();
	}
	
	protected final int getInteractionModes()
	{
		throw new todo.TODO();
	}
	
	public int getKeyCode(int __action)
	{
		throw new todo.TODO();
	}
	
	protected void hideNotify()
	{
		throw new todo.TODO();
	}
	
	protected final void invalidate()
	{
		throw new todo.TODO();
	}
	
	protected void keyPressed(int __a)
	{
		throw new todo.TODO();
	}
	
	protected void keyReleased(int __a)
	{
		throw new todo.TODO();
	}
	
	protected void keyRepeated(int __a)
	{
		throw new todo.TODO();
	}
	
	protected void pointerDragged(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	protected void pointerPressed(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	protected void pointerReleased(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	protected final void repaint()
	{
		throw new todo.TODO();
	}
	
	protected final void repaint(int __a, int __b, int __c, int __d)
	{
		throw new todo.TODO();
	}
	
	public void setKeyListener(KeyListener __kl)
	{
		throw new todo.TODO();
	}
	
	public void setPaintMode(boolean __opaque)
	{
		throw new todo.TODO();
	}
	
	protected void showNotify()
	{
		throw new todo.TODO();
	}
	
	protected void sizeChanged(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	protected boolean traverse(int __a, int __b, int __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	protected void traverseOut()
	{
		throw new todo.TODO();
	}
}


