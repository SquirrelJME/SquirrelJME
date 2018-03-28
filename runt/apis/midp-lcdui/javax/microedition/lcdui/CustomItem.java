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
	
	@__SerializedEvent__
	protected abstract int getMinContentHeight();
	
	@__SerializedEvent__
	protected abstract int getMinContentWidth();
	
	@__SerializedEvent__
	protected abstract int getPrefContentHeight(int __a);
	
	@__SerializedEvent__
	protected abstract int getPrefContentWidth(int __a);
	
	@__SerializedEvent__
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
	
	/**
	 * This method is called after this has been hidden from the display,
	 * whether it was removed or concealed. This can be used to stop timers
	 * for example since they might not be needed when this is not visible.
	 *
	 * @since 2018/03/28
	 */
	@__SerializedEvent__
	protected void hideNotify()
	{
		// Implemented by sub-classes
	}
	
	protected final void invalidate()
	{
		throw new todo.TODO();
	}
	
	@__SerializedEvent__
	protected void keyPressed(int __a)
	{
		throw new todo.TODO();
	}
	
	@__SerializedEvent__
	protected void keyReleased(int __a)
	{
		throw new todo.TODO();
	}
	
	@__SerializedEvent__
	protected void keyRepeated(int __a)
	{
		throw new todo.TODO();
	}
	
	@__SerializedEvent__
	protected void pointerDragged(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	@__SerializedEvent__
	protected void pointerPressed(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	@__SerializedEvent__
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
	
	@__SerializedEvent__
	protected void showNotify()
	{
		// Implemented by sub-classes
	}
	
	@__SerializedEvent__
	protected void sizeChanged(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	@__SerializedEvent__
	protected boolean traverse(int __a, int __b, int __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	@__SerializedEvent__
	protected void traverseOut()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@__SerializedEvent__
	@Override
	final void __doPaint(Graphics __g, int __pw, int __ph)
	{
		this.paint(__g, __pw, __ph);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@__SerializedEvent__
	@Override
	final void __doShown(boolean __shown)
	{
		if (__shown)
			this.showNotify();
		else
			this.hideNotify();
	}
}


