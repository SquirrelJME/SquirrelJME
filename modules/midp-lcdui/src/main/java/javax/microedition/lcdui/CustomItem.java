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

import cc.squirreljme.runtime.lcdui.SerializedEvent;

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
	
	/** Is the rendering transparent or opaque? */
	boolean _transparent;
	
	protected CustomItem(String __a)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected abstract int getMinContentHeight();
	
	@SerializedEvent
	protected abstract int getMinContentWidth();
	
	@SerializedEvent
	protected abstract int getPrefContentHeight(int __a);
	
	@SerializedEvent
	protected abstract int getPrefContentWidth(int __a);
	
	/**
	 * This is called when this is to be painted. The clipping area will
	 * be set to the area that needs updating and as such drawing should only
	 * occur within the region. Any pixels drawn outside of the clipping area
	 * might not be updated and may have no effect when drawing.
	 *
	 * If this is transparent then the background will automatically be filled
	 * appropriately with a color or image, otherwise in opaque mode it is
	 * assumed that pixels in the clipping region will be drawn on.
	 *
	 * @param __g The graphics to draw into.
	 * @param __w The width of the item.
	 * @param __h The height of the item.
	 * @since 2018/03/28
	 */
	@SerializedEvent
	protected abstract void paint(Graphics __g, int __w, int __h);
	
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
	@SerializedEvent
	protected void hideNotify()
	{
		// Implemented by sub-classes
	}
	
	protected final void invalidate()
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected void keyPressed(int __a)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected void keyReleased(int __a)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected void keyRepeated(int __a)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected void pointerDragged(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected void pointerPressed(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
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
	
	/**
	 * Sets the painting mode of the canvas.
	 *
	 * If transparent mode is enabled, then the implementation (not the end
	 * developer) will fill the background with a suitable color or image
	 * (which is unspecified).
	 *
	 * If opaque mode (which is the default) is enabled then it will be
	 * assumed that {@link #repaint(Graphics)} will cover every pixel and as
	 * such it will not be required for the background to be cleared or
	 * initialized.
	 *
	 * @param __opaque If {@code true} then opaque mode is enabled.
	 * @since 2018/03/28
	 */
	public void setPaintMode(boolean __opaque)
	{
		this._transparent = !__opaque;
	}
	
	@SerializedEvent
	protected void showNotify()
	{
		// Implemented by sub-classes
	}
	
	@SerializedEvent
	protected void sizeChanged(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected boolean traverse(int __a, int __b, int __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected void traverseOut()
	{
		throw new todo.TODO();
	}
}


