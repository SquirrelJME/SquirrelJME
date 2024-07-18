// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import org.jetbrains.annotations.Async;

@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
@Api
public abstract class CustomItem
	extends Item
{
	@Api
	protected static final int KEY_PRESS =
		4;
	
	@Api
	protected static final int KEY_RELEASE =
		8;
	
	@Api
	protected static final int KEY_REPEAT =
		16;
	
	@Api
	protected static final int NONE =
		0;
	
	@Api
	protected static final int POINTER_DRAG =
		128;
	
	@Api
	protected static final int POINTER_PRESS =
		32;
	
	@Api
	protected static final int POINTER_RELEASE =
		64;
	
	@Api
	protected static final int TRAVERSE_HORIZONTAL =
		1;
	
	@Api
	protected static final int TRAVERSE_VERTICAL =
		2;
	
	/** Is the rendering transparent or opaque? */
	boolean _transparent;
	
	/** The listener to use for key events. */
	KeyListener _keyListener;
	
	/** The default key listener implementation. */
	private KeyListener _defaultKeyListener;
	
	/** The last width. */
	private int _lastWidth;
	
	protected CustomItem(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected abstract int getMinContentHeight();
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected abstract int getMinContentWidth();
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected abstract int getPrefContentHeight(int __a);
	
	@Api
	@SerializedEvent
	@Async.Execute
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
	@Api
	@SerializedEvent
	@Async.Execute
	protected abstract void paint(Graphics __g, int __w, int __h);
	
	@Api
	public int getGameAction(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	protected final int getInteractionModes()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getKeyCode(int __action)
	{
		throw Debugging.todo();
	}
	
	/**
	 * This method is called after this has been hidden from the display,
	 * whether it was removed or concealed. This can be used to stop timers
	 * for example since they might not be needed when this is not visible.
	 *
	 * @since 2018/03/28
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	protected void hideNotify()
	{
		// Implemented by sub-classes
	}
	
	@Api
	protected final void invalidate()
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void keyPressed(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void keyReleased(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void keyRepeated(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void pointerDragged(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void pointerPressed(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void pointerReleased(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	protected final void repaint()
	{
		throw Debugging.todo();
	}
	
	@Api
	protected final void repaint(int __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the key listener which is used to handle key events.
	 *
	 * If this is set then {@link #keyPressed(int)}, {@link #keyReleased(int)},
	 * and {@link #keyRepeated} will still be called.
	 *
	 * @param __kl The key listener to use, {@code null} clears it.
	 * @since 2020/10/16
	 */
	@Api
	public void setKeyListener(KeyListener __kl)
	{
		this._keyListener = __kl;
	}
	
	/**
	 * Sets the painting mode of the canvas.
	 *
	 * If transparent mode is enabled, then the implementation (not the end
	 * developer) will fill the background with a suitable color or image
	 * (which is unspecified).
	 *
	 * If opaque mode (which is the default) is enabled then it will be
	 * assumed that {@link CustomItem#repaint()} will cover every pixel and as
	 * such it will not be required for the background to be cleared or
	 * initialized.
	 *
	 * @param __opaque If {@code true} then opaque mode is enabled.
	 * @since 2018/03/28
	 */
	@Api
	public void setPaintMode(boolean __opaque)
	{
		this._transparent = !__opaque;
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void showNotify()
	{
		// Implemented by sub-classes
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void sizeChanged(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected boolean traverse(int __a, int __b, int __c, int[] __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void traverseOut()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the default key listener implementation for this class.
	 * 
	 * @return The default key listener.
	 * @since 2020/10/16
	 */
	final KeyListener __defaultKeyListener()
	{
		KeyListener rv = this._defaultKeyListener;
		if (rv == null)
			this._defaultKeyListener =
				(rv = new __CustomItemDefaultKeyListener__(this));
		
		return rv;
	}
}


