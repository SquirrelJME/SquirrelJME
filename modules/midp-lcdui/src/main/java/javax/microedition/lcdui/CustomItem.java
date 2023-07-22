// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
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
	
	/** The last height. */
	private int _lastHeight;
	
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
	 * {@inheritDoc}
	 * @since 2020/09/21
	 */
	@Override
	final void __paint(Graphics __gfx, int __sw, int __sh, int __special)
	{
		// Store the last dimensions
		this._lastHeight = __sw;
		this._lastHeight = __sh;
		
		// Draw background?
		if (!this._transparent)
		{
			int old = __gfx.getAlphaColor();
			__gfx.setColor(this.__backend().metric(
				this._displayable._display._uiDisplay, 
				UIMetricType.COLOR_CANVAS_BACKGROUND));
			
			__gfx.fillRect(0, 0, __sw, __sh);
			
			__gfx.setAlphaColor(old);
		}
		
		// Forward draw
		this.paint(__gfx, __sw, __sh);
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
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	boolean __propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
		UIBackend instance = this.__backend();
		
		// Only act on the canvas item
		if (!instance.equals(__item,
			this.__state(__CustomItemState__.class)._uiCanvas))
			return false;
		
		// Depends on the property
		switch (__intProp)
		{
				// Shown state changed?
			case UIWidgetProperty.INT_IS_SHOWN:
				if (__new == 0)
					this.hideNotify();
				else
					this.showNotify();
				return true;
			
				// New size?
			case UIWidgetProperty.INT_WIDTH:
			case UIWidgetProperty.INT_HEIGHT:
			case UIWidgetProperty.INT_WIDTH_AND_HEIGHT:
				if (__intProp == UIWidgetProperty.INT_WIDTH_AND_HEIGHT)
				{
					this._lastHeight = __old;
					this._lastHeight = __new;
				}
				else if (__intProp == UIWidgetProperty.INT_WIDTH)
				{
					__old = __new;
					this._lastWidth = __old;
					__new = this._lastHeight;
				}
				else
				{
					__old = this._lastWidth;
					this._lastHeight = __new;
				}
			
				this.sizeChanged(__old, __new);
				return true;
			
				// Un-Handled
			default:
				return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	final __CommonState__ __stateInit(UIBackend __backend)
		throws NullPointerException
	{
		return new __CustomItemState__(__backend, this);
	}
	
	/**
	 * Item state.
	 * 
	 * @since 2023/01/14
	 */
	static class __CustomItemState__
		extends Item.__ItemState__
	{
		/** The native display instance. */
		final UIItemBracket _uiCanvas;
		
		/**
		 * Initializes the backend state.
		 *
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/21
		 */
		__CustomItemState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
			
			// Build new canvas
			this._uiCanvas = __backend.itemNew(UIItemType.CANVAS);
		}
	}
}


