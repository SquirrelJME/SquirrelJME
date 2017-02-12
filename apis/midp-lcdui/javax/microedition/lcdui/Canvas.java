// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import net.multiphasicapps.squirreljme.lcdui.DisplayCanvasConnector;
import net.multiphasicapps.squirreljme.lcdui.DisplayConnector;
import net.multiphasicapps.squirreljme.lcdui.DisplayInstance;
import net.multiphasicapps.squirreljme.lcdui.PointerEventType;

/**
 * The canvas acts as the base class for primary display interfaces that
 * require more customized draw handling.
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
	
	/** This is a game key for the left direction. */
	public static final int LEFT =
		2;
	
	/** This is a game key for the right direction. */
	public static final int RIGHT =
		5;
	
	/** This is a game key for the up direction. */
	public static final int UP =
		1;
	
	/** The connector to this canvas. */
	private volatile DisplayCanvasConnector _connector;
	
	/**
	 * Initializes the base canvas.
	 *
	 * @since 2016/10/08
	 */
	protected Canvas()
	{
	}
	
	protected abstract void paint(Graphics __a);
	
	public int getGameAction(int __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getHeight()
	{
		DisplayInstance instance = this._instance;
		if (instance != null)
			return instance.getHeight();
		return 1;
	}
	
	public int getKeyCode(int __a)
	{
		throw new Error("TODO");
	}
	
	public String getKeyName(int __a)
	{
		throw new Error("TODO");
	}
	
	public int[] getSoftkeyLabelCoordinates(int __p)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getWidth()
	{
		DisplayInstance instance = this._instance;
		if (instance != null)
			return instance.getWidth();
		return 1;
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
		Display d = getCurrentDisplay();
		if (d == null)
			d = Display.getDisplays(0)[0];
		
		return d.hasPointerEvents();
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
		Display d = getCurrentDisplay();
		if (d == null)
			d = Display.getDisplays(0)[0];
		
		return d.hasPointerMotionEvents();
	}
	
	public boolean hasRepeatEvents()
	{
		throw new Error("TODO");
	}
	
	protected void hideNotify()
	{
		throw new Error("TODO");
	}
	
	public boolean isDoubleBuffered()
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
		repaint(0, 0, getWidth(), getHeight());
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
		DisplayInstance instance = this._instance;
		if (instance != null)
			instance.repaint(__x, __y, __w, __h);
	}
	
	public final void serviceRepaints()
	{
		throw new Error("TODO");
	}
	
	public void setFullScreenMode(boolean __a)
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
	
	public void setRequiredActions(int __actions)
	{
		throw new Error("TODO");
	}
	
	protected void showNotify()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	protected void sizeChanged(int __w, int __h)
	{
		super.sizeChanged(__w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	DisplayConnector __connector()
	{
		// Use the same connector, create if missing
		DisplayCanvasConnector rv = this._connector;
		if (rv == null)
			this._connector = (rv = new __Connector__());
		return rv;
	}
	
	/**
	 * This allows the display engine to communicate with this canvas class.
	 *
	 * @since 2017/02/08
	 */
	final class __Connector__
		implements DisplayCanvasConnector
	{
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void keyEvent(int __code, int __mods)
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/08
		 */
		@Override
		public void paint(Graphics __g)
		{
			Canvas.this.paint(__g);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void pointerEvent(PointerEventType __t, int __x, int __y)
			throws NullPointerException
		{
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
			
			// Depends
			switch (__t)
			{
				case DRAGGED:
					Canvas.this.pointerDragged(__x, __y);
					return;
				
				case PRESSED:
					Canvas.this.pointerPressed(__x, __y);
					return;
				
				case RELEASED:
					Canvas.this.pointerReleased(__x, __y);
					return;
				
					// Should not happen
				default:
					throw new RuntimeException("OOPS");
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/10
		 */
		@Override
		public void sizeChanged(int __w, int __h)
		{
			Canvas.this.sizeChanged(__w, __h);
		}
	}
}

