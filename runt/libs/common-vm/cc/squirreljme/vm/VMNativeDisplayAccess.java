// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;
import cc.squirreljme.runtime.lcdui.event.NonStandardKey;
import cc.squirreljme.runtime.lcdui.gfx.AcceleratedGraphics;
import cc.squirreljme.runtime.lcdui.gfx.GraphicsFunction;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import cc.squirreljme.runtime.lcdui.gfx.SerializedGraphics;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

/**
 * This class provides the framebuffer needed by SquirrelJME which is backed
 * on top of LCDUI itself. This just provides a single display.
 *
 * @since 2018/11/17
 */
public class VMNativeDisplayAccess
{
	/** The pixel format to use for the framebuffer. */
	public static final PixelFormat FRAMEBUFFER_PIXELFORMAT =
		PixelFormat.INTEGER_RGB888;
	
	/** The display to back on, lazily initialized to prevent crashing. */
	Display _usedisplay;
	
	/** The framebuffer RGB data. */
	volatile int[] _fbrgb;
	
	/** The framebuffer width. */
	volatile int _fbw =
		-1;
	
	/** The framebuffer height. */
	volatile int _fbh =
		-1;
	
	/** The canvas for display. */
	volatile VMCanvas _canvas;
	
	/** Doing a repaint? */
	volatile boolean _repaint;
	
	/** Repaint parameters. */
	volatile int _repaintx,
		_repainty,
		_repaintw,
		_repainth;
	
	/** Accelerated graphics instance. */
	volatile Graphics _accelgfx;
	
	/** The state count of the framebuffer. */
	volatile int _statecount;
	
	/** Event callback. */
	volatile NativeDisplayEventCallback _callback;
	
	/**
	 * Initialize and/or reset accelerated graphics operations.
	 *
	 * @param __id The display to initialize for.
	 * @return {@code true} if acceleration is supported.
	 * @since 2018/11/19
	 */
	public final boolean accelGfx(int __id)
	{
		if (__id != 0)
			return false;
		
		// Check for framebuffer update
		this.__checkFramebuffer();
		
		// Setup instance
		int fbw = this._fbw;
		this._accelgfx = FRAMEBUFFER_PIXELFORMAT.createGraphics(
			this._fbrgb, null, fbw, this._fbh, false, fbw, 0, 0, 0);
		return true;
	}
	
	/**
	 * Performs accelerated graphics operation.
	 *
	 * @param __id The display ID.
	 * @param __func The function to call.
	 * @param __args Arguments to the operation.
	 * @return The result of the operation.
	 * @since 2018/11/19
	 */
	public final Object accelGfxFunc(int __id, int __func, Object... __args)
	{
		if (__id != 0)
			return null;
		
		// Deserialize and forward
		return SerializedGraphics.deserialize(this._accelgfx,
			GraphicsFunction.of(__func), __args);
	}

	/**
	 * Returns the capabilities of the display.
	 *
	 * @param __id The display ID.
	 * @return The capabilities of the display.
	 * @since 2018/11/17
	 */
	public final int capabilities(int __id)
	{
		// Only a single display is supported
		if (__id != 0)
			return 0;
		
		// Just directly pass the capabilities of this display
		return this.__display().getCapabilities();
	}
	
	/**
	 * Requests that the display should be repainted.
	 *
	 * @param __id The display ID.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/12/03
	 */
	public final void displayRepaint(int __id,
		int __x, int __y, int __w, int __h)
	{
		if (__id != 0)
			return;
		
		this.__checkFramebuffer().repaint(__x, __y, __w, __h);
	}
	
	/**
	 * Returns the object representing the framebuffer data.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer array.
	 * @since 2018/11/18
	 */
	public final Object framebufferObject(int __id)
	{
		if (__id != 0)
			return null;
		
		// Check for framebuffer update
		this.__checkFramebuffer();
		
		// Return the raw RGB array
		return this._fbrgb;
	}
	
	/**
	 * Specifies that the framebuffer has been painted.
	 *
	 * @param __id The display ID.
	 * @since 2018/11/18
	 */
	public final void framebufferPainted(int __id)
	{
		if (__id != 0)
			return;
		
		this.__checkFramebuffer().repaint();
	}
	
	/**
	 * Returns the palette of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The palette of the framebuffer.
	 * @since 2018/11/18
	 */
	public final int[] framebufferPalette(int __id)
	{
		if (__id != 0)
			return null;
		
		// No palette is used
		return null;
	}
	
	/**
	 * Returns the parameters of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer parameters.
	 * @since 2018/11/18
	 */
	public final int[] framebufferParameters(int __id)
	{
		if (__id != 0)
			return null;
		
		// Check for framebuffer update
		this.__checkFramebuffer();
		
		// Fill in basic parameters
		int[] rv = new int[NativeDisplayAccess.NUM_PARAMETERS];
		
		// These are the only used fields
		int fbw = this._fbw,
			fbh = this._fbh;
		
		// Build parameters
		rv[NativeDisplayAccess.PARAMETER_PIXELFORMAT] =
			FRAMEBUFFER_PIXELFORMAT.ordinal();
		rv[NativeDisplayAccess.PARAMETER_BUFFERWIDTH] = fbw;
		rv[NativeDisplayAccess.PARAMETER_BUFFERHEIGHT] = fbh;
		rv[NativeDisplayAccess.PARAMETER_ALPHA] = 0;
		rv[NativeDisplayAccess.PARAMETER_PITCH] = fbw;
		rv[NativeDisplayAccess.PARAMETER_OFFSET] = 0;
		rv[NativeDisplayAccess.PARAMETER_VIRTXOFF] = 0;
		rv[NativeDisplayAccess.PARAMETER_VIRTYOFF] = 0;
		
		return rv;
	}
	
	/**
	 * Returns the state count of this framebuffer which is used to detect
	 * when the parameters have changed, where they must all be recalculated
	 * (that is the framebuffer wrapper must be recreated).
	 *
	 * @param __id The display ID.
	 * @return The state count for the framebuffer.
	 * @since 2018/12/02
	 */
	public final int framebufferStateCount(int __id)
	{
		if (__id != 0)
			return -1;
		
		// Check for framebuffer update
		this.__checkFramebuffer();
		
		return this._statecount;
	}
	
	/**
	 * Is the specified display upsidedown?
	 *
	 * @param __id The ID of the display.
	 * @return If the display is upsidedown.
	 * @since 2018/11/17
	 */
	public final boolean isUpsideDown(int __id)
	{
		if (__id != 0)
			return false;
		
		// Only certain orientations are considered upside-down
		switch (this.__display().getOrientation())
		{
			case Display.ORIENTATION_LANDSCAPE_180:
			case Display.ORIENTATION_PORTRAIT_180:
				return true;
			
				// Not upsidedown
			default:
				return false;
		}
	}
	
	/**
	 * Returns the number of displays which are available.
	 *
	 * @return The number of available displays.
	 * @since 2018/11/17
	 */
	public final int numDisplays()
	{
		return 1;
	}
	
	/**
	 * Registers the event callback.
	 *
	 * @param __e The event to call.
	 * @since 2018/12/03
	 */
	public final void registerEventCallback(NativeDisplayEventCallback __e)
	{
		// Do not change to self!
		NativeDisplayEventCallback old = this._callback;
		if (old == __e)
			return;	
		
		// Tell the old handler that the callback is now gone
		if (old != null)
			try
			{
				old.lostCallback();
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		
		// Set new
		this._callback = __e;
	}
	
	/**
	 * Sets the title of the display.
	 *
	 * @param __id The display ID.
	 * @param __t The title to use.
	 * @since 2018/11/18
	 */
	public final void setDisplayTitle(int __id, String __t)
	{
		if (__id != 0)
			return;
		
		this.__checkFramebuffer().setTitle(__t);
	}
	
	/**
	 * Checks if the framebuffer needs updating.
	 *
	 * @return The canvas used.
	 * @since 2018/11/18
	 */
	private final VMCanvas __checkFramebuffer()
	{
		// Need to create the canvas?
		VMCanvas canvas = this._canvas;
		if (canvas == null)
		{
			// Display this canvas
			this._canvas = (canvas = new VMCanvas());
			this._usedisplay.setCurrent(canvas);
			
			// Listener for function commands
			canvas.setCommandListener(new __CommandListener__());
			
			// Add function key wrappers to forward commands which have
			// occurred
			for (int i = 1; i <= 24; i++)
				canvas.addCommand(new Command("F" + i, Command.SCREEN,
					i - 1));
			
			// Add exit command
			canvas.addCommand(new Command("Exit", Command.EXIT,
				Integer.MAX_VALUE));
			
			// Make this canvas full screen so that the commands go away
			canvas.setFullScreenMode(true);
			
			// Assume that the client will draw over every pixel, note that
			// if the client has this done with a false, then the client will
			// clear the framebuffer while drawing over it.
			canvas.setPaintMode(true);
		}
		
		// Properties have changed? Recreate the buffer data
		int cw = canvas.getWidth(),
			ch = canvas.getHeight();
		if (this._fbrgb == null || cw != this._fbw || ch != this._fbh)
		{
			int n = cw * ch;
			int[] fbrgb;
			this._fbrgb = (fbrgb = new int[n]);
			this._fbw = cw;
			this._fbh = ch;
			
			// Initialize the framebuffer to white
			for (int i = 0; i < n; i++)
				fbrgb[i] = 0xFFFFFF;
			
			// Increase the state count
			this._statecount++;
		}
		
		return canvas;
	}
	
	/**
	 * Returns the display this is currently using.
	 *
	 * @return The currently backed display.
	 * @since 2018/11/17
	 */
	private final Display __display()
	{
		Display rv = this._usedisplay;
		if (rv == null)
			this._usedisplay = (rv = Display.getDisplays(0)[0]);
		return rv;
	}
	
	/**
	 * Listens for commands.
	 *
	 * @since 2018/11/18
	 */
	final class __CommandListener__
		implements CommandListener
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void commandAction(Command __c, Displayable __d)
		{
			// Exiting the VM?
			if (__c.getCommandType() == Command.EXIT)
				VMNativeDisplayAccess.this._callback.exitRequest(0);
			
			// Function menu key
			else if (__c.getLabel().startsWith("F"))
				VMNativeDisplayAccess.this._callback.command(0,
					__c.getPriority());
		}
	}
	
	/**
	 * Contains the canvas which is drawn on.
	 *
	 * @since 2018/11/18
	 */
	public final class VMCanvas
		extends Canvas
	{
		/** Key time index tracking. */
		private volatile int _keyindex;
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void hideNotify()
		{
			VMNativeDisplayAccess.this._callback.shown(0, 0);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyPressed(int __code)
		{
			this.__postKey(NativeDisplayEventCallback.KEY_PRESSED, __code);
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyReleased(int __code)
		{
			this.__postKey(NativeDisplayEventCallback.KEY_RELEASED, __code);
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyRepeated(int __code)
		{
			this.__postKey(NativeDisplayEventCallback.KEY_REPEATED, __code);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void paint(Graphics __g)
		{
			int x = __g.getClipX(),
				y = __g.getClipY(),
				w = __g.getClipWidth(),
				h = __g.getClipHeight();
			
			// Call paint code
			VMNativeDisplayAccess.this._callback.paintDisplay(0,
				x, y, w, h);
			
			// Just draw the raw RGB data
			int fbw = VMNativeDisplayAccess.this._fbw;
			__g.drawRGB(VMNativeDisplayAccess.this._fbrgb,
				0,
				fbw,
				0,
				0,
				fbw,
				VMNativeDisplayAccess.this._fbh,
				false);
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void pointerDragged(int __x, int __y)
		{
			VMNativeDisplayAccess.this._callback.pointerEvent(0,
				NativeDisplayEventCallback.POINTER_DRAGGED, __x, __y,
				++this._keyindex);
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void pointerPressed(int __x, int __y)
		{
			VMNativeDisplayAccess.this._callback.pointerEvent(0,
				NativeDisplayEventCallback.POINTER_PRESSED, __x, __y,
				++this._keyindex);
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void pointerReleased(int __x, int __y)
		{
			VMNativeDisplayAccess.this._callback.pointerEvent(0,
				NativeDisplayEventCallback.POINTER_RELEASED, __x, __y,
				++this._keyindex);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void showNotify()
		{
			VMNativeDisplayAccess.this._callback.shown(0, 1);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void sizeChanged(int __w, int __h)
		{
			// The framebuffer will need to be redone
			VMNativeDisplayAccess.this.__checkFramebuffer();
			
			// Post
			VMNativeDisplayAccess.this._callback.sizeChanged(0,
				__w, __h);
		}
		
		/**
		 * Post key event.
		 *
		 * @param __et The event type to post.
		 * @param __kc The keycode used.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/12/01
		 */
		final void __postKey(int __et, int __kc)
			throws NullPointerException
		{
			// Try to map to a game key if possible
			try
			{
				switch (this.getGameAction(__kc))
				{
					case Canvas.UP:
						__kc = NonStandardKey.VGAME_UP;
						break;
						
					case Canvas.DOWN:
						__kc = NonStandardKey.VGAME_DOWN;
						break;
						
					case Canvas.LEFT:
						__kc = NonStandardKey.VGAME_LEFT;
						break;
						
					case Canvas.RIGHT:
						__kc = NonStandardKey.VGAME_RIGHT;
						break;
					
					case Canvas.FIRE:
						__kc = NonStandardKey.VGAME_FIRE;
						break;
					
					case Canvas.GAME_A:
						__kc = NonStandardKey.VGAME_A;
						break;
					
					case Canvas.GAME_B:
						__kc = NonStandardKey.VGAME_B;
						break;
					
					case Canvas.GAME_C:
						__kc = NonStandardKey.VGAME_C;
						break;
					
					case Canvas.GAME_D:
						__kc = NonStandardKey.VGAME_D;
						break;
					
						// Unknown, do not remap!
					default:
						break;
				}
			}
			
			// Ignore unknown game keys
			catch (IllegalArgumentException e)
			{
			}
			
			// Post event
			VMNativeDisplayAccess.this._callback.keyEvent(0,
				__et, __kc, 0, ++this._keyindex);
		}
	}
}

