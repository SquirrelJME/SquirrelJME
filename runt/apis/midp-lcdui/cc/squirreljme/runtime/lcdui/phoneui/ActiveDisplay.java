// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;
import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.lcdui.gfx.EnforcedDrawingAreaGraphics;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Ticker;
import java.util.Arrays;

/**
 * This contains the display display along with the internal image buffer.
 *
 * @since 2019/05/16
 */
public final class ActiveDisplay
{
	/** The width of the display. */
	protected final int width;
	
	/** The height of the display. */
	protected final int height;
	
	/** The displayable to draw. */
	volatile Displayable _current;
	
	/** The drawing method to use. */
	volatile DrawingMethod _drawing;
	
	/** The action method to use. */
	volatile ActionMethod _action;
	
	/** Current drawing state. */
	volatile State _state;
	
	/** Vibrate the display? */
	private volatile boolean _invibrate;
	
	/** Vibration end time. */
	private volatile long _vibrateend;
	
	/** Vibration cycle. */
	private volatile boolean _vibratecycle;
	
	/** Content X Area. */
	int _contentx;
	
	/** Content Y Area. */
	int _contenty;
	
	/** Width of the content area. */
	int _contentwidth;
	
	/** Height of the content area. */
	int _contentheight;
	
	/** The title to use. */
	String _title;
	
	/**
	 * Initializes the active display.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2019/05/16
	 */
	public ActiveDisplay(int __w, int __h)
	{
		// Set sizes
		this.width = __w;
		this.height = __h;
		
		// Default content area size
		this._contentwidth = __w;
		this._contentheight = __h - (StandardMetrics.TITLE_BAR_HEIGHT +
			StandardMetrics.COMMAND_BAR_HEIGHT +
			StandardMetrics.TICKER_BAR_HEIGHT);
	}
	
	/**
	 * Activates the display.
	 *
	 * @param __d The displayable to be drawn when requested.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	public final void activate(Displayable __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// If the displayable has not changed, ignore
		Displayable current = this._current;
		if (current == __d)
			return;
		
		// Set
		this._current = __d;
		this._drawing = DrawingMethod.of(__d.getClass());
		this._action = ActionMethod.of(__d.getClass());
		this._state = new State();
		
		// Realize the dimensions
		this.realize(PhoneUI._IGNORE_REALIZATION);
	}
	
	/**
	 * Executes the numbered command on the given display.
	 *
	 * @param __c The command to execute.
	 * @since 2019/05/18
	 */
	public final void command(int __c)
	{
		// Display must be active
		Displayable current = this._current;
		if (current == null)
			return;
		
		// Forward action, if it was not handled by this display then fallback
		// to the displayable commands
		CommandListener l = ((ExposedDisplayable)current).getCommandListener();
		if (!this._action.command(current, this._state, __c))
		{
			// Get all commands
			Command[] commands = current.getCommands();
			int numcommands = commands.length;
			
			// Call command function
			if (__c >= 0 && __c < numcommands)
				l.commandAction(commands[__c], current);
		}
	}
	
	/**
	 * Requests that the program be terminated.
	 *
	 * @since 2019/05/18
	 */
	public final void exitRequest()
	{
		// Ignore if nothing is set
		Displayable current = this._current;
		if (current == null)
			return;
		
		// Search through commands for an exit one to execute
		CommandListener l = ((ExposedDisplayable)current).getCommandListener();
		if (l != null)
			for (Command c : current.getCommands())
				if (c.getCommandType() == Command.EXIT)
				{
					// Execute command
					l.commandAction(c, current);
					
					// Done
					return;
				}
		
		// Otherwise just terminate the application
		System.exit(0);
	}
	
	/**
	 * Key action has been performed.
	 *
	 * @param __d The display ID.
	 * @param __ty The type of key event.
	 * @param __kc The key code.
	 * @param __ch The key character, {@code -1} is not valid.
	 * @param __time Timecode.
	 * @since 2019/05/18
	 */
	public final void keyEvent(int __ty, int __kc, int __ch, int __time)
	{
		// Display must be active
		Displayable current = this._current;
		if (current == null)
			return;
		
		// Forward action
		this._action.keyEvent(current, this._state, __ty, __kc, __ch, __time);
	}
	
	/**
	 * Paints whatever is in the active display.
	 *
	 * @param __g The backend graphics to draw on.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2019/05/16
	 */
	public final void paint(Graphics __g, int __x, int __y, int __w, int __h)
	{
		// Get display details
		int dw = this.width,
			dh = this.height;
		
		// Current displayable to draw
		ExposedDisplayable current = (ExposedDisplayable)this._current;
		
		// Realize new content area coordinates
		int[] ua = new int[4];
		if (this.realize(ua))
			current.sizeChanged(ua[2], ua[3]);
		
		// Extract coordinates
		int ux = ua[0],
			uy = ua[1],
			uw = ua[2],
			uh = ua[3];
		
		// If not full-screen, then draw title bar and maybe the command bar
		if (uh != dh)
		{
			// Remember default parameters
			Font oldfont = __g.getFont();
			int oldcolor = __g.getColor();
			
			// Draw background
			__g.setColor(StandardMetrics.BACKGROUND_BAR_COLOR);
			__g.fillRect(0, 0, dw, StandardMetrics.TITLE_BAR_HEIGHT);
			
			// Set font
			__g.setFont(Font.getFont("sansserif", 0,
				StandardMetrics.TITLE_BAR_HEIGHT));
			
			// Draw title text
			String title = this._title;
			__g.setColor(StandardMetrics.FOREGROUND_BAR_COLOR);
			__g.drawString(title, 0, 0, Graphics.TOP | Graphics.LEFT);
			__g.drawString(title, 1, 0, Graphics.TOP | Graphics.LEFT);
			
			// Draw the ticker?
			Ticker ticker = (current == null ? null : current.getTicker());
			if (ticker != null)
			{
				// Background
				__g.setColor(StandardMetrics.BACKGROUND_TICKER_COLOR);
				__g.fillRect(0, StandardMetrics.TITLE_BAR_HEIGHT,
					dw, StandardMetrics.TICKER_BAR_HEIGHT);
				
				// Draw ticker text
				__g.setColor(StandardMetrics.FOREGROUND_TICKER_COLOR);
				__g.drawString(ticker.getString(),
					0, StandardMetrics.TITLE_BAR_HEIGHT,
					Graphics.TOP | Graphics.LEFT);
			}
			
			// Get commands that are used, this is used to figure out if the
			// command bar needs to be drawn
			Command[] commands = (current == null ? new Command[0] :
				current.getCommands());
			int numcommands = commands.length;
			if (numcommands > 0)
			{
				// Base Y position
				int cy = dh - StandardMetrics.COMMAND_BAR_HEIGHT;
				
				// Draw background
				__g.setColor(StandardMetrics.BACKGROUND_BAR_COLOR);
				__g.fillRect(0, cy, dw, StandardMetrics.COMMAND_BAR_HEIGHT);
			}
			
			// Restore parameters
			__g.setFont(oldfont);
			__g.setColor(oldcolor);
		}
		
		// Setup enforced graphics
		Graphics ug = new EnforcedDrawingAreaGraphics(__g,
			ux, uy, uw, uh);
		
		// If nothing is being shown, just show the version info
		if (current == null)
		{
			// Draw box
			ug.setColor(0x0000FF);
			ug.fillRect(0, 0, uw, uh);
			
			// Draw some layout text
			ug.setColor(0xFFFF00);
			ug.setFont(Font.getFont("sansserif", 0, 16));
			ug.drawString("SquirrelJME " + SquirrelJME.RUNTIME_VERSION + "\n" +
				"(C) Stephanie Gawroriski\n" +
				"https://squirreljme.cc/\nLicensed w/ the GPLv3!", 0, 0, 0);
			ug.drawString("SquirrelJME", 1, 0, 0);
		}
		
		// Normal painting
		else
		{
			// If the displayable is transparent then, we fill in the
			// background for the application
			if (current.isTransparent())
			{
				// Use background color instead
				int oldcolor = ug.getColor();
				ug.setColor(StandardMetrics.TRANSPARENT_COLOR);
				
				// Fill
				ug.fillRect(0, 0, uw, uh);
				
				// Restore
				ug.setColor(oldcolor);
			}
			
			// Paint
			State state = this._state;
			this._drawing.paint((Displayable)current, state, ug, uw, uh);
			
			// Draw the focus box
			State.Box focusbox = state.focusbox;
			if (focusbox.x >= 0)
			{
				// Draw primary color
				ug.setColor(StandardMetrics.FOCUS_A_COLOR);
				ug.setAlpha(0x7F);
				ug.setStrokeStyle(Graphics.SOLID);
				ug.drawRect(focusbox.x, focusbox.y, focusbox.w, focusbox.h);
				
				// Draw secondary color
				ug.setColor(StandardMetrics.FOCUS_B_COLOR);
				ug.setAlpha(0xFF);
				ug.setStrokeStyle(Graphics.DOTTED);
				ug.drawRect(focusbox.x, focusbox.y, focusbox.w, focusbox.h);
				
				// Restore stroke
				ug.setStrokeStyle(Graphics.SOLID);
			}
		}
		
		// Clear clip for status symbols and focus
		__g.setClip(0, 0, dw, dh);
		
		// Switch to the symbol font
		Font sf = Font.getFont("symbol", 0, StandardMetrics.TITLE_BAR_HEIGHT);
		__g.setFont(sf);
		int xa = sf.charWidth('#'),
			sx = dw - 2;
		
		// Currently vibrating the display?
		if (this._invibrate)
		{
			// Stop vibrating?
			long nowtime = System.nanoTime();
			if (nowtime >= this._vibrateend)
				this._invibrate = false;
			
			// Visual vibration, just a basic symbol
			else
			{
				// Switch to symbol font
				__g.setFont(Font.getFont("symbol", 0, 
					StandardMetrics.TITLE_BAR_HEIGHT));
				
				// Draw vibrate symbol
				__g.setColor(StandardMetrics.VIBRATE_COLOR);
				__g.drawChar('#', sx, 0, Graphics.RIGHT | Graphics.TOP);
				sx -= xa;
			}
		}
	}
	
	/**
	 * Pointer event has occured.
	 *
	 * @param __ty The type of pointer event.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __time Timecode.
	 * @since 2019/05/18
	 */
	public final void pointerEvent(int __ty, int __x, int __y, int __time)
	{
		// Display must be active
		Displayable current = this._current;
		if (current == null)
			return;
		
		// Forward action
		this._action.pointerEvent(current, this._state, __ty, __x, __y,
			__time);
	}
	
	/**
	 * Realizes the size of the content area.
	 *
	 * @param __dims The output content area dimensions.
	 * @return {@code true} if the area has been resized.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/17
	 */
	public final boolean realize(int[] __dims)
		throws NullPointerException
	{
		if (__dims == null)
			throw new NullPointerException("NARG");
		
		// Screen size
		int dw = this.width,
			dh = this.height;
		
		// User area
		int ux, uy, uw, uh;
		
		// Current displayable to draw
		ExposedDisplayable current = (ExposedDisplayable)this._current;
		
		// Full-screen uses the entire screen
		if (current != null && current.isFullscreen())
		{
			ux = 0;
			uy = 0;
			uw = dw;
			uh = dh;
		}
		
		// Otherwise, space is lost to the title bar and commands
		else
		{
			// Get commands that are used, this is used to figure out if the
			// command bar needs to be drawn
			Command[] commands = (current == null ? new Command[0] :
				current.getCommands());
			int numcommands = commands.length;
			
			// Drawing the ticker?
			int drawticker = (current != null && current.getTicker() != null ?
				StandardMetrics.TICKER_BAR_HEIGHT : 0);
			
			// Clip dimensions
			ux = 0;
			uy = StandardMetrics.TITLE_BAR_HEIGHT + drawticker;
			uw = dw;
			uh = dh - (StandardMetrics.TITLE_BAR_HEIGHT +
				(numcommands > 0 ? StandardMetrics.COMMAND_BAR_HEIGHT : 0) +
				drawticker);
		}
		
		// Has the area changed?
		boolean rv = false;
		int oldux = this._contentx,
			olduy = this._contenty,
			olduw = this._contentwidth,
			olduh = this._contentheight;
		if (ux != oldux || uy != olduy || uw != olduw || uh != olduh)
		{
			// Did change
			rv = true;
			
			// Set new fields
			this._contentx = ux;
			this._contenty = uy;
			this._contentwidth = uw;
			this._contentheight = uh;
		}
		
		// Set output user area dimensions
		__dims[0] = ux;
		__dims[1] = uy;
		__dims[2] = uw;
		__dims[3] = uh;
		
		// Has this changed at all?
		return rv;
	}
	
	/**
	 * Vibrates the display for the given number of milliseconds.
	 *
	 * @param __ms The number of milliseconds to vibrate for.
	 * @since 2019/05/17
	 */
	public final void vibrate(int __ms)
	{
		// Stop vibrating
		if (__ms <= 0)
			this._invibrate = false;
		
		// Otherwise vibrate
		else
		{
			this._invibrate = true;
			this._vibrateend = System.nanoTime() + (__ms * 1000000L);
		}
	}
}

