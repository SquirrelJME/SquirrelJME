// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This example project handles events and allows for some interaction based
 * on buttons and such.
 *
 * @since 2018/12/01
 */
public class Events
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Setup canvas
		DemoCanvas cv = new DemoCanvas();
		
		// Set display to the canvas
		Display.getDisplay(this).setCurrent(cv);
	}
	
	/**
	 * Converts the game code to a string.
	 *
	 * @param __gc The game code.
	 * @return The string for this game code.
	 * @since 2018/12/02
	 */
	static final String __gamecodeToString(int __gc)
	{
		switch (__gc)
		{
			case 0:					return "Unknown";
			
			case Canvas.UP:			return "Up";
			case Canvas.DOWN:		return "Down";
			case Canvas.LEFT:		return "Left";
			case Canvas.RIGHT:		return "Right";
			
			case Canvas.FIRE:		return "Fire";
			
			case Canvas.GAME_A:		return "A";
			case Canvas.GAME_B:		return "B";
			case Canvas.GAME_C:		return "C";
			case Canvas.GAME_D:		return "D";
			
			default:				return "Invalid";
		}
	}
	
	/**
	 * The demo canvas which does the event handling and other things.
	 *
	 * @since 2018/12/01
	 */
	static public final class DemoCanvas
		extends Canvas
	{
		/** The number of times this was shown. */
		volatile int _numshown;
		
		/** The number of times this was hidden. */
		volatile int _numhides;
		
		/** Pointer position. */
		volatile int _pointerx,
			_pointery;
		
		/** Pointer event type. */
		volatile PointerType _pointertype;
		
		/** The number of key events which happened. */
		volatile int _numkeys;
		
		/** The last key event. */
		volatile int _keycode;
		
		/** The last key done. */
		volatile KeyboardType _keyboardtype;
		
		/**
		 * Initializes the canvas.
		 *
		 * @since 2018/12/01
		 */
		{
			// Setup title
			this.setTitle("Events");
			
			// Draw as opaque, so we do not need to update everything
			this.setPaintMode(false);
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void hideNotify()
		{
			this._numhides++;
			
			// Repaint to update stuff
			this.repaint();
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyPressed(int __code)
		{
			this._keycode = __code;
			this._keyboardtype = KeyboardType.PRESSED;
			this._numkeys++;
			
			// Report event
			System.err.printf("Key Pressed: %d%n", __code);
			
			// Repaint to update stuff
			this.repaint();
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyReleased(int __code)
		{
			this._keycode = __code;
			this._keyboardtype = KeyboardType.RELEASED;
			this._numkeys++;
			
			// Report event
			System.err.printf("Key Released: %d%n", __code);
			
			// Repaint to update stuff
			this.repaint();
		}
			
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyRepeated(int __code)
		{
			this._keycode = __code;
			this._keyboardtype = KeyboardType.REPEATED;
			this._numkeys++;
			
			// Report event
			System.err.printf("Key Repeated: %d%n", __code);
			
			// Repaint to update stuff
			this.repaint();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void paint(Graphics __g)
		{
			Font font = __g.getFont();
			int height = font.getHeight(),
				x = 2,
				y = 2 - height,
				keycode = this._keycode,
				gamecode = -1;
			
			// Get the key name, but it might not even be valid
			String keyname = null;
			try
			{
				keyname = this.getKeyName(keycode);
				gamecode = this.getGameAction(keycode);
			}
			catch (IllegalArgumentException e)
			{
			}
			
			// Draw text info
			__g.drawString(String.format("Showns: %d", this._numshown),
				x, y += height, 0);
			__g.drawString(String.format("Hides: %d", this._numhides),
				x, y += height, 0);
			__g.drawString(String.format("Pointer: (%d, %d) %s",
				this._pointerx, this._pointery, this._pointertype),
				x, y += height, 0);
			__g.drawString(String.format(
				"Key: \"%s\" (Code=%d Game=%s) %s [Count=%d]",
				keyname, keycode, Events.__gamecodeToString(gamecode),
				this._keyboardtype, this._numkeys),
				x, y += height, 0);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void pointerDragged(int __x, int __y)
		{
			this._pointertype = PointerType.DRAGGED;
			this._pointerx = __x;
			this._pointery = __y;
			
			// Repaint to update stuff
			this.repaint();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void pointerPressed(int __x, int __y)
		{
			this._pointertype = PointerType.PRESSED;
			this._pointerx = __x;
			this._pointery = __y;
			
			// Repaint to update stuff
			this.repaint();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void pointerReleased(int __x, int __y)
		{
			this._pointertype = PointerType.RELEASED;
			this._pointerx = __x;
			this._pointery = __y;
			
			// Repaint to update stuff
			this.repaint();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void showNotify()
		{
			this._numshown++;
			
			// Repaint to update stuff
			this.repaint();
		}
	}
	
	/**
	 * The type of keyboard event which happened.
	 *
	 * @since 2018/12/01
	 */
	static public enum KeyboardType
	{
		/** Pressed. */
		PRESSED,
		
		/** Released. */
		RELEASED,
		
		/** Repeated. */
		REPEATED,
		
		/** End. */
		;
	}
	
	/**
	 * The type of pointer event which happened.
	 *
	 * @since 2018/12/01
	 */
	static public enum PointerType
	{
		/** Pressed. */
		PRESSED,
		
		/** Released. */
		RELEASED,
		
		/** Dragged. */
		DRAGGED,
		
		/** End. */
		;
	}
}

