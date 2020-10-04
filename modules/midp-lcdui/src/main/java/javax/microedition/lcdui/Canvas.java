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

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UISpecialCode;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.event.EventTranslate;
import cc.squirreljme.runtime.lcdui.event.KeyNames;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;

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
	/**
	 * Every button that is possibly available.
	 * 
	 * The following buttons are shown:
	 * - Any from {@link #ACTIONS_NAVIGATION}.
	 * - {@link #GAME_A}.
	 * - {@link #GAME_B}.
	 * - {@link #GAME_C}.
	 * - {@link #GAME_D}.
	 */
	public static final int ACTIONS_ALL =
		-2;
	
	/**
	 * Directional buttons and fire to appear on the display screen.
	 * 
	 * The following buttons are shown:
	 * - {@link #UP}.
	 * - {@link #DOWN}.
	 * - {@link #LEFT}.
	 * - {@link #RIGHT}.
	 * - {@link #FIRE}.
	 */
	public static final int ACTIONS_NAVIGATION =
		-1;
	
	/** No actions are required. */
	public static final int ACTIONS_NONE =
		0;
	
	/**
	 * This is a game key for the down direction.
	 * 
	 * The bits of this key are {@code 0b110}, inverted from {@link #UP}.
	 */
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
	
	/** 
	 * This is a game key for the left direction.
	 * 
	 * The bits of this key are {@code 0b010}, inverted from {@link #RIGHT}.
	 */
	public static final int LEFT =
		2;
	
	/**
	 * This is a game key for the right direction.
	 * 
	 * The bits of this key are {@code 0b101}, inverted from {@link #LEFT}.
	 */
	public static final int RIGHT =
		5;
	
	/** 
	 * This is a game key for the up direction.
	 * 
	 * The bits of this key are {@code 0b001}, inverted from {@link #DOWN}.
	 */
	public static final int UP =
		1;
	
	/** The native display instance. */
	final UIItemBracket _uiCanvas;
	
	/** The key listener to use. */
	private KeyListener _keylistener;
	
	/** Is the rendering transparent or opaque? */
	boolean _transparent;
	
	/** Should this be ran full-screen? */
	volatile boolean _isFullScreen;
	
	/** Service repaint counter. */
	volatile int _paintCount;
	
	/** The actions which are required. */
	private int _requiredActions;
	
	/**
	 * Initializes the base canvas.
	 *
	 * @since 2016/10/08
	 */
	protected Canvas()
	{
		// Build new canvas
		UIItemBracket uiCanvas = UIFormShelf.itemNew(UIItemType.CANVAS);
		this._uiCanvas = uiCanvas;
		
		// Register self for future paint events
		StaticDisplayState.register(this, uiCanvas);
		
		// Show it on the form for this displayable
		UIFormShelf.formItemPosition(this._uiForm, uiCanvas, 0);
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
		return EventTranslate.keyCodeToGameAction(__kc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getHeight()
	{
		return Displayable.__getHeight(this);
	}
	
	/**
	 * Returns the key code for the given game action.
	 *
	 * @param __gc The game action to convert.
	 * @return The key code
	 * @throws IllegalArgumentException If the game action is not valid.
	 * @since 2019/04/14
	 */
	public int getKeyCode(int __gc)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB1a The specified game action is not valid.}
		int rv = EventTranslate.gameActionToKeyCode(__gc);
		if (rv == 0)
			throw new IllegalArgumentException("EB1a " + __gc);
		return rv;
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
	
	/**
	 * Gets the coordinates of a soft key of where it would be placed on the
	 * screen in relation to the canvas.
	 * 
	 * The returned coordinates may be negative and may be outside of the
	 * screen.
	 * 
	 * @param __sk The position to get, will be one of the soft key
	 * coordinates except for {@link Display#SOFTKEY_OFFSCREEN}.
	 * @throws IllegalArgumentException If the coordinates are not valid, or
	 * the border is {@link Display#SOFTKEY_OFFSCREEN}.
	 * @return The coordinates, these will be {@code [x, y, width, height]}.
	 * @since 2020/10/03
	 */
	public int[] getSoftkeyLabelCoordinates(int __sk)
		throws IllegalArgumentException
	{
		// Remove any rotation from the soft key
		Display display = this._display;
		if (display != null)
			__sk = display.__layoutProject(__sk);
		
		int index = (__sk & Display.SOFTKEY_INDEX_MASK);
		
		// {@squirreljme.error EB17 The placement is not valid or not supported
		// on this device/implementation. (The placement)}
		if (index == 0 || (__sk != Display._SOFTKEY_LEFT_COMMAND &&
			__sk != Display._SOFTKEY_RIGHT_COMMAND))
			throw new IllegalArgumentException("EB17 " + __sk);
		
		UIBackend backend = UIBackendFactory.getInstance();
		
		// Use the item's actual position
		int uiPos = Display.__layoutSoftKeyToPos(__sk);
		UIItemBracket item = backend.formItemAtPosition(this._uiForm, uiPos);
		if (item != null)
			return new int[]{
					backend.widgetPropertyInt(item,
						UIWidgetProperty.INT_X_POSITION),
					backend.widgetPropertyInt(item,
						UIWidgetProperty.INT_Y_POSITION),
					backend.widgetPropertyInt(item,
						UIWidgetProperty.INT_WIDTH),
					backend.widgetPropertyInt(item,
						UIWidgetProperty.INT_HEIGHT),
				};
		
		// Otherwise make a guess at where it could be located since it cannot
		// be well known
		int halfWidth = this.getWidth() / 2;
		int height = this.getHeight();
		switch (__sk)
		{
			case Display._SOFTKEY_LEFT_COMMAND:
				return new int[]{0, height, halfWidth, 16};
			
			case Display._SOFTKEY_RIGHT_COMMAND:
				return new int[]{halfWidth, height, halfWidth, 16};
			
			default:
				throw Debugging.oops(__sk);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getWidth()
	{
		return Displayable.__getWidth(this);
	}
	
	/**
	 * This checks whether the display has the capability to handle pointer
	 * events.
	 *
	 * @return {@code true} if pointer events are available.
	 * @since 2017/02/12
	 */
	@ApiDefinedDeprecated
	public boolean hasPointerEvents()
	{
		Display d = this._display;
		return (d != null ? d :
			Display.getDisplays(0)[0]).hasPointerEvents();
	}
	
	/**
	 * This checks whether the display has the capability to handle pointer
	 * motion events.
	 *
	 * @return {@code true} if pointer motion events are available.
	 * @since 2017/02/12
	 */
	@ApiDefinedDeprecated
	public boolean hasPointerMotionEvents()
	{
		Display d = this._display;
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
	 * {@inheritDoc}
	 * @since 2019/05/17
	 */
	@ImplementationNote("This is in SquirrelJME only and is used to provide " +
		"access to this flag.")
	protected boolean isFullscreen()
	{
		return this._isFullScreen;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/17
	 */
	@ImplementationNote("This is in SquirrelJME only and is used to provide " +
		"access to this flag.")
	protected boolean isTransparent()
	{
		return this._transparent;
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
	 * The clipping region when {@link #paint(Graphics)} is called will have
	 * its clip set to the region to be redrawn.
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
		
		// Request repainting
		UIBackend instance = UIBackendFactory.getInstance();
		
		// Send repaint properties
		instance.widgetProperty(this._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT,
			UISpecialCode.REPAINT_KEY_X | __x);
		instance.widgetProperty(this._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT,
			UISpecialCode.REPAINT_KEY_Y | __y);
		instance.widgetProperty(this._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT,
			UISpecialCode.REPAINT_KEY_WIDTH | __w);
		instance.widgetProperty(this._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT,
			UISpecialCode.REPAINT_KEY_HEIGHT | __h);
		
		// Execute the paint
		instance.widgetProperty(this._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT, 0);
	}
	
	/**
	 * This forces any pending repaint requests to be serviced immediately,
	 * blocking until paint has been called. If the canvas is not visible on
	 * the display or if there are no pending repaints then this call does
	 * nothing.
	 *
	 * Note that if the caller of this method and the paint method uses a lock
	 * then a deadlock may occur.
	 *
	 * @since 2019/04/14
	 */
	public final void serviceRepaints()
	{
		// If there is no current display then nothing can ever be repainted
		Display display = this._display;
		if (display == null)
			return;
		
		// Get the current paint count
		int count;
		synchronized (this)
		{
			count = this._paintCount;
		}
		
		// Initialize the waiter
		__PaintWait__ wait = new __PaintWait__();
		
		// Wait loop for repaints
		for (;;)
		{
			// Call this, as when it is complete the event loop would have ran
			this.repaint();
			display.callSerially(wait);
			
			// Did the paint counter change?
			synchronized (this)
			{
				if (this._paintCount != count)
					break;
			}
		}
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
		// Do nothing if the state is the same
		if (this._isFullScreen == __f)
			return;
		
		// Set new mode
		this._isFullScreen = __f;
		
		// Depending on full-screen either choose the first position or the
		// full-screen body of the form
		UIFormShelf.formItemPosition(this._uiForm, this._uiCanvas, (__f ?
			UIItemPosition.BODY : 0));
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
	 * assumed that {@link #repaint()} will cover every pixel and
	 * as such it will not be required for the background to be cleared or
	 * initialized.
	 *
	 * @param __opaque If {@code true} then opaque mode is enabled.
	 * @since 2017/02/12
	 */
	public void setPaintMode(boolean __opaque)
	{
		this._transparent = !__opaque;
	}
	
	/**
	 * Sets the required actions that are to be shown on the touch screen when
	 * this canvas is active.
	 * 
	 * @param __actions The actions to use.
	 * @throws IllegalArgumentException If the actions are not valid.
	 * @since 2020/10/03
	 */
	public void setRequiredActions(int __actions)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB18 Invalid action. {The action ID}) */
		if (__actions != Canvas.ACTIONS_ALL &&
			__actions != Canvas.ACTIONS_NAVIGATION &&
			__actions != Canvas.ACTIONS_NONE)
			throw new IllegalArgumentException("EB18 " + __actions);
		
		// Not a touch screen, so does not matter
		if (!this.hasPointerEvents())
			return;
		
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
	}
	
	/**
	 * Paints and forwards Graphics.
	 * 
	 * @param __gfx Graphics to draw.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @since 2020/09/21
	 */
	final void __paint(Graphics __gfx, int __sw, int __sh)
	{
		// Draw background?
		if (!this._transparent)
		{
			int old = __gfx.getAlphaColor();
			__gfx.setColor(UIBackendFactory.getInstance().metric(
				UIMetricType.COLOR_CANVAS_BACKGROUND));
			
			__gfx.fillRect(0, 0, __sw, __sh);
			
			__gfx.setAlphaColor(old);
		}
		
		// Forward Draw
		try
		{
			this.paint(__gfx);
		}
		
		// Handle repaint servicing
		finally
		{
			// Increment the paint counter, that it happened
			synchronized (this)
			{
				this._paintCount++;
			}
		}
	}
}

