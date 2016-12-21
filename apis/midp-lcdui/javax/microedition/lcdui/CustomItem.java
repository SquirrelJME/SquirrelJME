// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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
		throw new Error("TODO");
	}
	
	protected abstract int getMinContentHeight();
	
	protected abstract int getMinContentWidth();
	
	protected abstract int getPrefContentHeight(int __a);
	
	protected abstract int getPrefContentWidth(int __a);
	
	protected abstract void paint(Graphics __a, int __b, int __c);
	
	public int getGameAction(int __a)
	{
		throw new Error("TODO");
	}
	
	protected final int getInteractionModes()
	{
		throw new Error("TODO");
	}
	
	public int getKeyCode(int __action)
	{
		throw new Error("TODO");
	}
	
	protected void hideNotify()
	{
		throw new Error("TODO");
	}
	
	protected final void invalidate()
	{
		throw new Error("TODO");
	}
	
	protected void keyPressed(int __a)
	{
		throw new Error("TODO");
	}
	
	protected void keyReleased(int __a)
	{
		throw new Error("TODO");
	}
	
	protected void keyRepeated(int __a)
	{
		throw new Error("TODO");
	}
	
	protected void pointerDragged(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	protected void pointerPressed(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	protected void pointerReleased(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	protected final void repaint()
	{
		throw new Error("TODO");
	}
	
	protected final void repaint(int __a, int __b, int __c, int __d)
	{
		throw new Error("TODO");
	}
	
	public void setKeyListener(KeyListener __kl)
	{
		throw new Error("TODO");
	}
	
	public void setPaintMode(boolean __opaque)
	{
		throw new Error("TODO");
	}
	
	protected void showNotify()
	{
		throw new Error("TODO");
	}
	
	protected void sizeChanged(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	protected boolean traverse(int __a, int __b, int __c, int[] __d)
	{
		throw new Error("TODO");
	}
	
	protected void traverseOut()
	{
		throw new Error("TODO");
	}
}


