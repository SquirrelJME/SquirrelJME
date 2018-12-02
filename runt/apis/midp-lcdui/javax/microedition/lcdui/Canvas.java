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

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.lcdui.common.CommonColors;
import cc.squirreljme.runtime.lcdui.event.EventType;
import cc.squirreljme.runtime.lcdui.event.KeyNames;
import cc.squirreljme.runtime.lcdui.event.NonStandardKey;
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
	protected abstract void paint(Graphics __g);
	
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
		switch (__kc)
		{
				// Map these keys to standard keys
			case KEY_UP:		return UP;
			case KEY_DOWN:		return DOWN;
			case KEY_LEFT:		return LEFT;
			case KEY_RIGHT:		return RIGHT;
			
				// Map these character keys to specific keys
			case ' ':			return FIRE;
			case 'a': case 'A':
			case 'h': case 'H':	return GAME_A;
			case 's': case 'S':
			case 'j': case 'J':	return GAME_B;
			case 'd': case 'D':
			case 'k': case 'K':	return GAME_C;
			case 'f': case 'F':
			case 'l': case 'L':	return GAME_D;
			
				// Virtually mapped game keys, likely from a VM running on top
			case NonStandardKey.VGAME_UP:		return UP;
			case NonStandardKey.VGAME_DOWN:		return DOWN;
			case NonStandardKey.VGAME_LEFT:		return LEFT;
			case NonStandardKey.VGAME_RIGHT:	return RIGHT;
			case NonStandardKey.VGAME_FIRE:		return FIRE;
			case NonStandardKey.VGAME_A:		return GAME_A;
			case NonStandardKey.VGAME_B:		return GAME_B;
			case NonStandardKey.VGAME_C:		return GAME_C;
			case NonStandardKey.VGAME_D:		return GAME_D;
			
				// Invalid
			default:
				return 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getHeight()
	{
		return this.__defaultHeight();
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
		return this.__defaultWidth();
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
		this.repaint(0, 0, this.getWidth(), this.getHeight());
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
		
		// Tell the display to repaint itself
		Display display = this.getCurrentDisplay();
		if (display != null)
			NativeDisplayAccess.postEvent(
				EventType.DISPLAY_REPAINT.ordinal(),
				display._nid, __x, __y, __w, __h);
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
		// Do nothing if already fullscreen
		if (this._isfullscreen == __f)
			return;
		
		// Set new mode
		this._isfullscreen = __f;
		
		// If we are the child of a display then we need to update the draw
		// slice of the display so that way stuff like command buttons and
		// such are chopped off
		__Widget__ parent = this._parent;
		if (parent != null && parent instanceof Display)
			((Display)parent).__updateDrawChain();
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
	
	/**
	 * This is called when the canvas has been shown.
	 *
	 * @since 2018/12/02.
	 */
	@SerializedEvent
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
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	@SerializedEvent
	void __doKeyAction(int __type, int __kc, char __ch, int __time)
	{
		// Since we really only care about actions, if we happen to detect
		// that the character is one of these symbols map them to the
		// appropriate key.
		// Keycodes for the same characters might not be mapped to the actual
		// event in the event of natural keyboards.
		if (__ch == '#')
			__kc = KEY_POUND;
		else if (__ch == '*')
			__kc = KEY_STAR;
		
		// Canvases only care about keycodes and not characters, so only
		// do something if the key code is actually valid!
		if (__kc == NonStandardKey.UNKNOWN)
			return;
		
		// Depends on the action
		switch (__type)
		{
			case _KEY_PRESSED:
				this.keyPressed(__kc);
				break;
			
			case _KEY_REPEATED:
				this.keyRepeated(__kc);
				break;
			
			case _KEY_RELEASED:
				this.keyReleased(__kc);
				break;
			
			default:
				break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	@SerializedEvent
	void __doPointerAction(int __type, int __x, int __y, int __time)
	{
		switch (__type)
		{
			case _POINTER_DRAGGED:
				this.pointerDragged(__x, __y);
				break;
			
			case _POINTER_PRESSED:
				this.pointerPressed(__x, __y);
				break;
			
			case _POINTER_RELEASED:
				this.pointerReleased(__x, __y);
				break;
			
			default:
				break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	@SerializedEvent
	void __doShown(boolean __shown)
	{
		// Needed for isShown()
		super.__doShown(__shown);	
		
		if (__shown)
			this.showNotify();
		else
			this.hideNotify();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	void __drawChain(Graphics __g)
	{
		__DrawChain__ dc = this._drawchain;
		
		// Setup an enforced draw region to prevent programs from drawing
		// outside of the canvas
		
		// Drawing this widget transparently? This just draws a color below
		// it accordingly
		if (this._transparent)
		{
			// The graphics object gets the color pre-initialized so make sure
			// to restore it after the paint
			int old = __g.getAlphaColor();
			
			// Fill the area accordingly
			__g.setAlphaColor(CommonColors.CANVAS_BACKGROUND);
			__g.fillRect(__g.getClipX(), __g.getClipY(),
				__g.getClipWidth(), __g.getClipHeight());
			
			// Restore old color
			__g.setAlphaColor(old);
		}
		
		// Draw whatever the canvas wants drawn on this
		try
		{
			this.paint(__g);
		}
		
		// Ignore any exceptions user code makes here
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	int __supportBit()
	{
		// Canvases are always supported
		return 0xFFFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	final void __updateDrawChain(__DrawSlice__ __sl)
	{
		// We will be using the same drawing area as the slice we were given
		// whatever it was
		__DrawChain__ dc = this._drawchain;
		dc.set(__sl);
	}
}

