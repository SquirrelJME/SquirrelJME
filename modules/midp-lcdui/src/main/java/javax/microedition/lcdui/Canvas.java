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
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UISpecialCode;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.event.EventTranslate;
import cc.squirreljme.runtime.lcdui.event.KeyNames;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import cc.squirreljme.runtime.lcdui.scritchui.ScritchLcdUiUtils;
import java.lang.ref.WeakReference;
import org.jetbrains.annotations.Async;

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
@Api
public abstract class Canvas
	extends Displayable
{
	/** The maximum number of times to wait when servicing repaints. */
	private static final int _REPAINT_STOP =
		5;
	
	/** The amount of time to wait when servicing repaints. */
	private static final int _REPAINT_DELAY =
		16;
	
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
	@Api
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
	@Api
	public static final int ACTIONS_NAVIGATION =
		-1;
	
	/** No actions are required. */
	@Api
	public static final int ACTIONS_NONE =
		0;
	
	/**
	 * This is a game key for the down direction.
	 * 
	 * The bits of this key are {@code 0b110}, inverted from {@link #UP}.
	 */
	@Api
	public static final int DOWN =
		6;
	
	/** This is a game key for the fire button. */
	@Api
	public static final int FIRE =
		8;
	
	/** This is a game key for the A button. */
	@Api
	public static final int GAME_A =
		9;
	
	/** This is a game key for the B button. */
	@Api
	public static final int GAME_B =
		10;
	
	/** This is a game key for the C button. */
	@Api
	public static final int GAME_C =
		11;
	
	/** This is a game key for the D button. */
	@Api
	public static final int GAME_D =
		12;
	
	/** Backspace keyboard key. */
	@Api
	public static final int KEY_BACKSPACE =
		8;
	
	/** Delete keyboard key. */
	@Api
	public static final int KEY_DELETE =
		127;
	
	/** Down arrow keyboard key. */
	@Api
	public static final int KEY_DOWN =
		-2;
	
	/** Enter keyboard key. */
	@Api
	public static final int KEY_ENTER =
		10;
	
	/** Escape keyboard key. */
	@Api
	public static final int KEY_ESCAPE =
		27;
	
	/** Left arrow keyboard key. */
	@Api
	public static final int KEY_LEFT =
		-3;
	
	/** Number pad zero keyboard key. */
	@Api
	public static final int KEY_NUM0 =
		48;
	
	/** Number pad one keyboard key. */
	@Api
	public static final int KEY_NUM1 =
		49;
	
	/** Number pad two keyboard key. */
	@Api
	public static final int KEY_NUM2 =
		50;
	
	/** Number pad three keyboard key. */
	@Api
	public static final int KEY_NUM3 =
		51;
	
	/** Number pad four keyboard key. */
	@Api
	public static final int KEY_NUM4 =
		52;
	
	/** Number pad five keyboard key. */
	@Api
	public static final int KEY_NUM5 =
		53;
	
	/** Number pad six keyboard key. */
	@Api
	public static final int KEY_NUM6 =
		54;
	
	/** Number pad seven keyboard key. */
	@Api
	public static final int KEY_NUM7 =
		55;
	
	/** Number pad eight keyboard key. */
	@Api
	public static final int KEY_NUM8 =
		56;
	
	/** Number pad nine keyboard key. */
	@Api
	public static final int KEY_NUM9 =
		57;
	
	/** Pound/hash keyboard key. */
	@Api
	public static final int KEY_POUND =
		35;
	
	/** Right arrow keyboard key. */
	@Api
	public static final int KEY_RIGHT =
		-4;
	
	/** The select key. */
	@Api
	public static final int KEY_SELECT =
		-5;
	
	/** The space bar keyboard key. */
	@Api
	public static final int KEY_SPACE =
		32;
	
	/** The star/asterisk keyboard key. */
	@Api
	public static final int KEY_STAR =
		42;
	
	/** The horizontal tab keyboard key. */
	@Api
	public static final int KEY_TAB =
		9;
	
	/** The up arrow key. */
	@Api
	public static final int KEY_UP =
		-1;
	
	/** 
	 * This is a game key for the left direction.
	 * 
	 * The bits of this key are {@code 0b010}, inverted from {@link #RIGHT}.
	 */
	@Api
	public static final int LEFT =
		2;
	
	/**
	 * This is a game key for the right direction.
	 * 
	 * The bits of this key are {@code 0b101}, inverted from {@link #LEFT}.
	 */
	@Api
	public static final int RIGHT =
		5;
	
	/** 
	 * This is a game key for the up direction.
	 * 
	 * The bits of this key are {@code 0b001}, inverted from {@link #DOWN}.
	 */
	@Api
	public static final int UP =
		1;
	
	/** Lock for repaints and servicing repaints. */
	private final Object _repaintLock =
		new Object();
	
	/** Callback for repainting. */
	private final __ExecCanvasRepainter__ _repainter;
	
	/** The current image buffer. */
	volatile Image _buffer;
	
	/** The key listener to use. */
	KeyListener _keyListener;
	
	/** Is the rendering transparent or opaque? */
	boolean _isOpaque =
		true;
	
	/** Should this be run full-screen? */
	volatile boolean _isFullScreen;
	
	/** The number of pending paints. */
	volatile int _pendingPaints;
	
	/** The default key listener implementation. */
	private KeyListener _defaultKeyListener;
	
	/** The actions which are required. */
	private int _requiredActions;
	
	/**
	 * Initializes the base canvas.
	 *
	 * @since 2016/10/08
	 */
	@Api
	protected Canvas()
	{
		DisplayableState state = this._state;
		ScritchInterface scritchApi = state.scritchApi();
		ScritchPanelInterface panelApi = scritchApi.panel();
		ScritchPanelBracket scritchPanel = state.scritchPanel();
		
		// Set listener for painting
		panelApi.setPaintListener(scritchPanel,
			new __ExecCanvasPaint__(this));
		
		// Listener for visibility changes
		scritchApi.component().setVisibleListener(scritchPanel,
			new __ExecCanvasVisible__(this));
		
		// Input events
		panelApi.setInputListener(scritchPanel,
			new __ExecCanvasInput__(this));
		
		// Canvases take focus and inputs, so enable it
		// There should also be the default so that it is selected without
		// needing to tab to it
		panelApi.enableFocus(scritchPanel, true, true);
		
		// Setup repaint callback
		this._repainter = new __ExecCanvasRepainter__(this);
	}
	
	/**
	 * This is called when this is to be painted. The clipping area will
	 * be set to the area that needs updating and as such drawing should only
	 * occur within the region. Any pixels drawn outside the clipping area
	 * might not be updated and may have no effect when drawing.
	 *
	 * If this is transparent then the background will automatically be filled
	 * appropriately with a color or image, otherwise in opaque mode it is
	 * assumed that pixels in the clipping region will be drawn on.
	 *
	 * @param __g The graphics to draw into.
	 * @since 2018/03/28
	 */
	@Api
	@SerializedEvent
	@Async.Execute
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
	@Api
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
		// Use the actual buffer size first
		Image buffer = this._buffer;
		if (buffer != null)
			return buffer.getHeight();
		
		// Otherwise, fallback to the owning or default display
		return ScritchLcdUiUtils.lcduiDisplaySize(this._state,
			true);
	}
	
	/**
	 * Returns the key code for the given game action.
	 *
	 * @param __gc The game action to convert.
	 * @return The key code
	 * @throws IllegalArgumentException If the game action is not valid.
	 * @since 2019/04/14
	 */
	@Api
	public int getKeyCode(int __gc)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1a The specified game action is not valid.} */
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
	@Api
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
	@Api
	public int[] getSoftkeyLabelCoordinates(int __sk)
		throws IllegalArgumentException
	{
		// Remove any rotation from the soft key
		Display display = this._display;
		if (display != null)
			__sk = display.__layoutProject(__sk);
		
		int index = (__sk & Display.SOFTKEY_INDEX_MASK);
		
		/* {@squirreljme.error EB17 The placement is not valid or not supported
		on this device/implementation. (The placement)} */
		if (index == 0 || (__sk != Display._SOFTKEY_LEFT_COMMAND &&
			__sk != Display._SOFTKEY_RIGHT_COMMAND))
			throw new IllegalArgumentException("EB17 " + __sk);
		
		UIBackend backend = this.__backend();
		
		// Use the item's actual position
		int uiPos = Display.__layoutSoftKeyToPos(__sk);
		UIItemBracket item = backend.formItemAtPosition(
			this.__state(__DisplayableState__.class)._uiForm, uiPos);
		if (item != null)
			return new int[]{
					backend.widgetPropertyInt(item,
						UIWidgetProperty.INT_X_POSITION, 0),
					backend.widgetPropertyInt(item,
						UIWidgetProperty.INT_Y_POSITION, 0),
					backend.widgetPropertyInt(item,
						UIWidgetProperty.INT_WIDTH, 0),
					backend.widgetPropertyInt(item,
						UIWidgetProperty.INT_HEIGHT, 0),
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
		// Use the actual buffer size first
		Image buffer = this._buffer;
		if (buffer != null)
			return buffer.getWidth();
		
		// Otherwise, fallback to the owning or default display
		return ScritchLcdUiUtils.lcduiDisplaySize(this._state,
			false);
	}
	
	/**
	 * This checks whether the display has the capability to handle pointer
	 * events.
	 *
	 * @return {@code true} if pointer events are available.
	 * @since 2017/02/12
	 */
	@Api
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
	@Api
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
	@Api
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
	@Api
	@SerializedEvent
	@Async.Execute
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
	@Api
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
	@Api
	@SerializedEvent
	@Async.Execute
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
	@Api
	@SerializedEvent
	@Async.Execute
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
	@Api
	@SerializedEvent
	@Async.Execute
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
	@Api
	@SerializedEvent
	@Async.Execute
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
	@Api
	@SerializedEvent
	@Async.Execute
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
	@Api
	@SerializedEvent
	@Async.Execute
	protected void pointerReleased(int __x, int __y)
	{
		// Does nothing by default
	}
	
	/**
	 * Equivalent to {@code repaint(0, 0, getWidth(), getHeight())}.
	 *
	 * @since 2017/02/10
	 */
	@Api
	public final void repaint()
	{
		// We need to actually queue the repaint rather than doing the
		// repaint in the event loop potentially because MIDP expects it
		// to be queued.
		this._state.scritchApi().eventLoop().executeLater(this._repainter);
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
	@Api
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		// Do nothing
		if (__w <= 0 || __h <= 0)
			return;
		
		// Request repainting
		UIBackend instance = this.__backend();
		
		// Send repaint properties
		instance.widgetProperty(this.__state(__CanvasState__.class)._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT, 0,
				UISpecialCode.REPAINT_KEY_X | __x);
		instance.widgetProperty(this.__state(__CanvasState__.class)._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT, 0,
				UISpecialCode.REPAINT_KEY_Y | __y);
		instance.widgetProperty(this.__state(__CanvasState__.class)._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT, 0,
				UISpecialCode.REPAINT_KEY_WIDTH | __w);
		instance.widgetProperty(this.__state(__CanvasState__.class)._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT, 0,
				UISpecialCode.REPAINT_KEY_HEIGHT | __h);
		
		// Count pending paints up before we signal the final repaint
		synchronized (this._repaintLock)
		{
			this._pendingPaints++;
		}
		
		// Execute the paint
		instance.widgetProperty(this.__state(__CanvasState__.class)._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_REPAINT, 0,
			UISpecialCode.REPAINT_EXECUTE);
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
	@Api
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public final void serviceRepaints()
	{
		// If there is no current display then nothing can ever be repainted
		Display display = this._display;
		if (display == null)
			return;
		
		// Lock on display since that is where the main serialized event loop
		// happens. Do stop after a number of runs in case we get a stuck
		// repaint that never happens, sometimes a system repaint happens
		// before we can really check to see that this really happened.
		Object repaintLock = this._repaintLock;
		for (int i = 0; i < Canvas._REPAINT_STOP; i++)
			synchronized (repaintLock)
			{
				// No repaints are left to be performed, stop now
				if (this._pendingPaints <= 0)
					return;
				
				// Otherwise, wait for a signal on paints
				try
				{
					// 16ms is the number of time between frames at 60FPS
					repaintLock.wait(Canvas._REPAINT_DELAY);
				}
				
				// Ignore any interruptions and just continue waiting 
				catch (InterruptedException ignored)
				{
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
	 * @param __f If {@code true} then fullscreen mode should be used.
	 * @since 2017/02/28
	 */
	@Api
	public void setFullScreenMode(boolean __f)
	{
		// Do nothing if the state is the same
		if (this._isFullScreen == __f)
			return;
		
		// Set new mode
		this._isFullScreen = __f;
		
		// Depending on full-screen either choose the first position or the
		// full-screen body of the form
		UIBackend backend = this.__backend();
		backend.formItemPosition(
			this.__state(__DisplayableState__.class)._uiForm,
			this.__state(__CanvasState__.class)._uiCanvas, (__f ?
			UIItemPosition.BODY : 0));
		
		// Update form title
		this.__updateFormTitle(true, __f);
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
	@Api
	public void setKeyListener(KeyListener __kl)
	{
		this._keyListener = __kl;
	}
	
	/**
	 * Sets the painting mode of the canvas.
	 *
	 * If transparent mode is enabled ({@code false}), then the implementation
	 * (not the end developer) will fill the background with a suitable color
	 * or image (which is unspecified).
	 *
	 * If opaque mode (which is the default) is enabled then it will be
	 * assumed that {@link #repaint()} will cover every pixel and
	 * as such it will not be required for the background to be cleared or
	 * initialized.
	 *
	 * @param __opaque If {@code true} then opaque mode is enabled.
	 * @since 2017/02/12
	 */
	@Api
	public void setPaintMode(boolean __opaque)
	{
		this._isOpaque = __opaque;
	}
	
	/**
	 * Sets the required actions that are to be shown on the touch screen when
	 * this canvas is active.
	 * 
	 * @param __actions The actions to use.
	 * @throws IllegalArgumentException If the actions are not valid.
	 * @since 2020/10/03
	 */
	@Api
	public void setRequiredActions(int __actions)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB18 Invalid action. (The action ID)} */
		if (__actions != Canvas.ACTIONS_ALL &&
			__actions != Canvas.ACTIONS_NAVIGATION &&
			__actions != Canvas.ACTIONS_NONE)
			throw new IllegalArgumentException("EB18 " + __actions);
		
		// Not a touch screen, so does not matter
		if (!this.hasPointerEvents())
			return;
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/24
	 */
	@Override
	public void setTitle(String __t)
	{
		// Set the title
		super.setTitle(__t);
		
		// Update the form's title
		this.__updateFormTitle(true, this._isFullScreen);
	}
	
	/**
	 * This is called when the canvas has been shown.
	 *
	 * @since 2018/12/02.
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	protected void showNotify()
	{
		// Implemented by subclasses
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Api
	@Override
	@SerializedEvent
	@Async.Execute
	protected void sizeChanged(int __w, int __h)
	{
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
				(rv = new __CanvasDefaultKeyListener__(this));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	void __execRevalidate(DisplayState __parent)
	{
		// Setup super first
		super.__execRevalidate(__parent);
		
		// Get the display scale to determine how the canvas should be drawn
		DisplayScale scale = __parent.display()._scale;
		
		// Get the current texture size of the window
		int w = Math.max(1, scale.textureW());
		int h = Math.max(1, scale.textureH());
		
		// Set absolute bounds of the canvas
		DisplayableState state = this._state;
		state.scritchApi().container().setBounds(
			__parent.scritchWindow(),
			state.scritchPanel(), 0, 0, w, h);
		
		// Setup new image with a raw buffer, if scaling is required
		if (scale.requiresBuffer())
			this._buffer = new Image(new int[w * h], 0, w * h, w, h,
				true, false);
		else
			this._buffer = null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	boolean __isPainted()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/21
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@Override
	final void __paint(Graphics __gfx, int __sw, int __sh, int __special)
	{
		// Not on screen?
		DisplayState display = this._state.currentDisplay();
		if (display == null)
			return;
		
		// The target graphics to use
		Graphics subGfx;
		
		// Is a buffer used for scaling?
		Image buffer;
		if (!display.display()._scale.requiresBuffer())
		{
			// No buffer is used
			buffer = null;
			
			// Use the directly passed graphics
			subGfx = __gfx;
		}
		else
		{
			// Use this buffer
			buffer = this._buffer;
		
			// Only paint if there is a buffer
			if (buffer == null)
				return;
			
			// Draw onto this image instead for scaling
			subGfx = buffer.getGraphics();
		}
		
		// Draw background?
		if (!this._isOpaque)
		{
			// Determine the color to draw
			int bgColor = this._state.scritchApi().environment().lookAndFeel()
				.panelBackgroundColor();
			
			// Draw entire background
			subGfx.setAlphaColor(bgColor | 0xFF_000000);
			subGfx.fillRect(0, 0, __sw, __sh);
			
			// Use a default pen color
			int fgColor = this._state.scritchApi().environment().lookAndFeel()
				.panelForegroundColor();
			subGfx.setAlphaColor(fgColor | 0xFF_000000);
		}
		
		// Forward Draw
		try
		{
			// Use buffer paint
			this.paint(subGfx);
			
			// Draw image accordingly with scaling, if needed
			if (buffer != null)
				__gfx.drawImage(buffer, 0, 0, 0);
		}
		
		// Handle repaint servicing
		finally
		{
			// We repainted the canvas, so reduce the pending paint counter
			Object repaintLock = this._repaintLock;
			synchronized (repaintLock)
			{
				// Drop the count, if there is any
				int pending = this._pendingPaints;
				if (pending > 0)
				{
					// Clear all paints, since this could have been called
					// multiple times, and we may have done one
					this._pendingPaints = 0;
					
					// Signal that a repaint was done
					repaintLock.notifyAll();
				}
			}
		}
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
			this.__state(__CanvasState__.class)._uiCanvas))
			return false;
		
		// Depends on the property
		switch (__intProp)
		{
				// Shown state changed?
			case UIWidgetProperty.INT_IS_SHOWN:
				if (__new == 0)
					this.hideNotify();
				else
					this.__showNotifyCanvas();
				return true;
			
				// New width?
			case UIWidgetProperty.INT_WIDTH:
				this.sizeChanged(__new, this.getHeight());
				return true;
				
				// Height changed?
			case UIWidgetProperty.INT_HEIGHT:
				this.sizeChanged(this.getWidth(), __new);
				return true;
				
				// Both changed?
			case UIWidgetProperty.INT_WIDTH_AND_HEIGHT:
				this.sizeChanged(__old, __new);
				return true;
			
				// Un-Handled
			default:
				return false;
		}
	}
	
	/**
	 * Notifies that this canvas has been shown.
	 * 
	 * @since 2021/11/28
	 */
	final void __showNotifyCanvas()
	{
		// Signal focus on this canvas since it has been shown
		UIBackend backend = this.__backend();
		backend.widgetProperty(this.__state(__CanvasState__.class)._uiCanvas,
			UIWidgetProperty.INT_SIGNAL_FOCUS, 0, 0);
		
		// Call the notification handler
		this.showNotify();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	final __CommonState__ __stateInit(UIBackend __backend)
		throws NullPointerException
	{
		return new __CanvasState__(__backend, this);
	}
	
	/**
	 * File selector state.
	 * 
	 * @since 2023/01/14
	 */
	static class __CanvasState__
		extends Displayable.__DisplayableState__
	{
		/** The native display instance. */
		final UIItemBracket _uiCanvas;
		
		/**
		 * Initializes the backend state.
		 *
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/14
		 */
		__CanvasState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
			
			// Build new canvas
			UIItemBracket uiCanvas = __backend.itemNew(UIItemType.CANVAS);
			this._uiCanvas = uiCanvas;
			
			// Register self for future paint events
			StaticDisplayState.register(__self, uiCanvas);
			
			// Show it on the form for this displayable
			__backend.formItemPosition(this._uiForm, uiCanvas, 0);
		}
	}
}

