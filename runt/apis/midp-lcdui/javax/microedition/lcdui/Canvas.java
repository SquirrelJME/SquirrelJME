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

import cc.squirreljme.runtime.lcdui.event.EventType;
import cc.squirreljme.runtime.lcdui.event.KeyNames;
import cc.squirreljme.runtime.lcdui.gfx.BasicGraphics;
import cc.squirreljme.runtime.lcdui.SerializedEvent;

/**
 * The canvas acts as the base class for primary display interfaces that
 * require more customized draw handling.
 *
 * It is not recommended to use a lookup table between keycodes and actions at
 * initialization time. The reason for this is that it is possible for the
 * device to enter different modes or be associated with different
 * {@link Display}s which have different action mappings.
 *
 * @since 2016/10/08
 */
public abstract class Canvas
	extends Displayable
{
	public static final int ACTIONS_ALL =
		-2;
	
	public static final int ACTIONS_NAVIGATION =
		-1;
	
	public static final int ACTIONS_NONE =
		0;
	
	/** This is a game key for the down direction. */
	public static final int DOWN =
		6;
	
	/** This is a game key for the fire button. */
	public static final int FIRE =
		8;
	
	/** This is a game key for the A button. */
	public static final int GAME_A =
		9;
	
	/** This is a game key for the B button. */
	public static final int GAME_B =
		10;
	
	/** This is a game key for the C button. */
	public static final int GAME_C =
		11;
	
	/** This is a game key for the D button. */
	public static final int GAME_D =
		12;
	
	public static final int KEY_BACKSPACE =
		8;
	
	public static final int KEY_DELETE =
		127;
	
	public static final int KEY_DOWN =
		-2;
	
	public static final int KEY_ENTER =
		10;
	
	public static final int KEY_ESCAPE =
		27;
	
	public static final int KEY_LEFT =
		-3;
	
	public static final int KEY_NUM0 =
		48;
	
	public static final int KEY_NUM1 =
		49;
	
	public static final int KEY_NUM2 =
		50;
	
	public static final int KEY_NUM3 =
		51;
	
	public static final int KEY_NUM4 =
		52;
	
	public static final int KEY_NUM5 =
		53;
	
	public static final int KEY_NUM6 =
		54;
	
	public static final int KEY_NUM7 =
		55;
	
	public static final int KEY_NUM8 =
		56;
	
	public static final int KEY_NUM9 =
		57;
	
	public static final int KEY_POUND =
		35;
	
	public static final int KEY_RIGHT =
		-4;
	
	public static final int KEY_SELECT =
		-5;
	
	public static final int KEY_SPACE =
		32;
	
	public static final int KEY_STAR =
		42;
	
	public static final int KEY_TAB =
		9;
	
	/** The up arrow key. */
	public static final int KEY_UP =
		-1;
	
	/** This is a game key for the left direction. */
	public static final int LEFT =
		2;
	
	/** This is a game key for the right direction. */
	public static final int RIGHT =
		5;
	
	/** This is a game key for the up direction. */
	public static final int UP =
		1;
	
	/** The key listener to use. */
	private KeyListener _keylistener;
	
	/** Is the rendering transparent or opaque? */
	boolean _transparent;
	
	/** Should this be ran full-screen? */
	volatile boolean _isfullscreen;
	
	/**
	 * Initializes the base canvas.
	 *
	 * @since 2016/10/08
	 */
	protected Canvas()
	{
	}
	
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
	 * @since 2018/03/28
	 */
	@SerializedEvent
	@Override
	protected abstract void paint(Graphics __a);
	
	/**
	 * Returns the action which is associated with the given key.
	 *
	 * @param __kc The key code to get the action for.
	 * @return The action associated with the given key or {@code 0} if no
	 * action is associated with the key.
	 * @throws IllegalArgumentException If the specified keycode is not valid.
	 * @since 2017/02/12
	 */
	public int getGameAction(int __kc)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
		/*
		DisplayInstance instance = this._instance;
		if (instance != null)
			return instance.getActionForKey(__kc);
		
		// If no display is bound, treat as unknown
		return 0;*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getHeight()
	{
		return this.__getHeight();
	}
	
	public int getKeyCode(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the name for a key.
	 *
	 * @param __a The name to get the key for.
	 * @return The name of the given key.
	 * @throws IllegalArgumentException If the key is not valid.
	 * @since 2017/02/12
	 */
	public String getKeyName(int __a)
		throws IllegalArgumentException
	{
		return KeyNames.getKeyName(__a);
	}
	
	public int[] getSoftkeyLabelCoordinates(int __p)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getWidth()
	{
		return this.__getWidth();
	}
	
	/**
	 * This checks whether the display has the capability to handle pointer
	 * events.
	 *
	 * @return {@code true} if pointer events are available.
	 * @since 2017/02/12
	 */
	@Deprecated
	public boolean hasPointerEvents()
	{
		Display d = __currentDisplay();
		return (d != null ? d : Display.getDisplays(0)[0]).hasPointerEvents();
	}
	
	/**
	 * This checks whether the display has the capability to handle pointer
	 * motion events.
	 *
	 * @return {@code true} if pointer motion events are available.
	 * @since 2017/02/12
	 */
	@Deprecated
	public boolean hasPointerMotionEvents()
	{
		Display d = __currentDisplay();
		return (d != null ? d : Display.getDisplays(0)[0]).
			hasPointerMotionEvents();
	}
	
	/**
	 * Always returns {@code true} because all implementations must support
	 * repeat events.
	 *
	 * @return Always {@code true}.
	 * @since 2017/02/12
	 */
	public boolean hasRepeatEvents()
	{
		return true;
	}
	
	/**
	 * This method is called after this has been hidden from the display,
	 * whether it was removed or concealed. This can be used to stop timers
	 * for example since they might not be needed when this is not visible.
	 *
	 * @since 2018/03/28
	 */
	@SerializedEvent
	@Override
	protected void hideNotify()
	{
		// Implemented by sub-classes
	}
	
	/**
	 * This method always returns {@code true} because all implementations
	 * must double buffer canvases.
	 *
	 * @return {@code true}.
	 * @since 2017/05/13
	 */
	public boolean isDoubleBuffered()
	{
		return true;
	}
	
	/**
	 * This is called when a key has been pressed.
	 *
	 * @param __code The key code, the character is not modified by modifiers.
	 * @since 2017/02/12
	 */
	@SerializedEvent
	protected void keyPressed(int __code)
	{
		// Does nothing, implemented by sub-classes
	}
	
	/**
	 * This is called when a key has been released.
	 *
	 * @param __code The key code, the character is not modified by modifiers.
	 * @since 2017/02/12
	 */
	@SerializedEvent
	protected void keyReleased(int __code)
	{
		// Does nothing, implemented by sub-classes
	}
	
	/**
	 * This is called when a key has been repeated.
	 *
	 * @param __code The key code, the character is not modified by modifiers.
	 * @since 2017/02/12
	 */
	@SerializedEvent
	protected void keyRepeated(int __code)
	{
		// Does nothing, implemented by sub-classes
	}
	
	/**
	 * This is called when the pointer is being dragged across the canvas, a
	 * drag is when there is movement 
	 *
	 * This requires that motion events are supported which can be known by
	 * calling {@link #hasPointerMotionEvents()}.
	 *
	 * @param __x The X coordinate of the pointer, on the canvas origin.
	 * @param __y The Y coordinate of the pointer, on the canvas origin.
	 * @since 2017/02/12
	 */
	@SerializedEvent
	protected void pointerDragged(int __x, int __y)
	{
		// Does nothing by default
	}
	
	/**
	 * This is called when the pointer has been pressed on the canvas.
	 *
	 * This requires that pointer events are supported which can be known by
	 * calling {@link #hasPointerEvents()}.
	 *
	 * @param __x The X coordinate of the pointer, on the canvas origin.
	 * @param __y The Y coordinate of the pointer, on the canvas origin.
	 * @since 2017/02/12
	 */
	@SerializedEvent
	protected void pointerPressed(int __x, int __y)
	{
		// Does nothing by default
	}
	
	/**
	 * This is called when the pointer has been released on the canvas.
	 *
	 * This requires that pointer events are supported which can be known by
	 * calling {@link #hasPointerEvents()}.
	 *
	 * @param __x The X coordinate of the pointer, on the canvas origin.
	 * @param __y The Y coordinate of the pointer, on the canvas origin.
	 * @since 2017/02/12
	 */
	@SerializedEvent
	protected void pointerReleased(int __x, int __y)
	{
		// Does nothing by default
	}
	
	/**
	 * Equivalent to {@code repaint(0, 0, getWidth(), getHeight())}.
	 *
	 * @since 2017/02/10
	 */
	public final void repaint()
	{
		// A remote repaint call is performed for the canvas so it is
		// possible that the width/height are not valid. Internally the code
		// will clip the rectangle to be in bounds.
		this.repaint(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * Requests that the specified region of the canvas be repainted.
	 *
	 * The clipping region when {@link #paint()} is called will have its clip
	 * set to the region to be redrawn.
	 *
	 * It is unspecified whether the drawing operation will happen immedietely,
	 * be enqueued, or not happen at all (for example if the canvas is
	 * currently being painted).
	 *
	 * A width or height with a negative value or zero does nothing.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2017/02/10
	 */
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		// Do nothing
		if (__w <= 0 || __h <= 0)
			return;
		
		// Send repaint
		throw new todo.TODO();
		/*
		LcdServiceCall.<VoidType>call(VoidType.class,
			LcdFunction.WIDGET_REPAINT, this._handle, __x, __y, __w, __h);
		*/
	}
	
	public final void serviceRepaints()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This specifies that the canvas should enter full screen mode which
	 * takes up the maximum amount of space that is possible on the display.
	 *
	 * Note that this might not use the entire screen.
	 *
	 * This method may permit access to the framebuffer directly on the device
	 * which may enable accelerated drawing if supported by the underlying
	 * display engine.
	 *
	 * Note that the fullscreen mode is treated
	 *
	 * @param __f If {@code true} then fullscreen mode should be used.
	 * @since 2017/02/28
	 */
	public void setFullScreenMode(boolean __f)
	{
		throw new todo.TODO();
		/*
		// Use global lock because there are many operations to be performed
		// especially if this display is bound
		synchronized (DisplayManager.GLOBAL_LOCK)
		{
			// If there is no change of state, do nothing
			boolean wasfullscreen = this._isfullscreen;
			if (wasfullscreen == __f)
				return;
			
			// Set the new fullscreen action
			this._isfullscreen = __f;
			
			// Fullscreen mode can be set before setCurrent(), so this is
			// treated as a flag
			Display current = __currentDisplay();
			if (current != null)
				__doFullscreen(__f);
		}*/
	}
	
	/**
	 * Sets the key listener which is used to handle key events.
	 *
	 * If this is set then {@link #keyPressed(int)}, {@link #keyReleased(int)},
	 * and {@link #keyRepeated} will still be called.
	 *
	 * @param __kl The key listener to use, {@code null} clears it.
	 * @since 2017/02/12
	 */
	public void setKeyListener(KeyListener __kl)
	{
		this._keylistener = __kl;
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
	 * @since 2017/02/12
	 */
	public void setPaintMode(boolean __opaque)
	{
		this._transparent = !__opaque;
	}
	
	public void setRequiredActions(int __actions)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	@Override
	protected void showNotify()
	{
		// Implemented by sub-classes
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	@SerializedEvent
	protected void sizeChanged(int __w, int __h)
	{
		super.sizeChanged(__w, __h);
	}
	
	/**
	 * Performs the action of making this canvas fullscreen or not.
	 *
	 * @param __full Is fullscreen mode used?
	 * @since 2017/10/01
	 */
	void __doFullscreen(boolean __full)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/28
	 */
	@Override
	int __getTransparentColor()
	{
		// The color for canvas backgrounds is always white
		if (this._transparent)
			return 0xFFFFFFFF;
		return 0;
	}
}

