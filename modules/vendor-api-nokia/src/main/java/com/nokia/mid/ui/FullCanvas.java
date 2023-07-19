// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * This the Nokia canvas, command buttons are forwarded to the key pressed
 * and released commands for the canvas.
 *
 * @since 2019/09/23
 */
@Api
public abstract class FullCanvas
	extends Canvas
{
	/** Virtual soft key 1. */
	static final Command _SOFT1 = new Command("Soft1", Command.SCREEN, 0);
	
	/** Virtual soft key 2. */
	static final Command _SOFT2 = new Command("Soft2", Command.SCREEN, 1);
	
	/** Virtual soft key 3. */
	static final Command _SOFT3 = new Command("Soft3", Command.SCREEN, 2);
	
	/** Down arrow. */
	@Api
	public static final int KEY_DOWN_ARROW = -2;
	
	/** End. */
	@Api
	public static final int KEY_END = -11;
	
	/** Left arrow. */
	@Api
	public static final int KEY_LEFT_ARROW = -3;
	
	/** Right arrow. */
	@Api
	public static final int KEY_RIGHT_ARROW = -4;
	
	/** Send. */
	@Api
	public static final int KEY_SEND = -10;
	
	/** Soft Key 1. */
	@Api
	public static final int KEY_SOFTKEY1 = -6;
	
	/** Soft Key 2. */
	@Api
	public static final int KEY_SOFTKEY2 = -7;
	
	/** Soft Key 3. */
	@Api
	public static final int KEY_SOFTKEY3 = -5;
	
	/** Up Arrow. */
	@Api
	public static final int KEY_UP_ARROW = -1;
	
	/**
	 * Initializes the base canvas and sets as full-screen.
	 *
	 * @since 2019/09/23
	 */
	@Api
	@ApiDefinedDeprecated
	public FullCanvas()
	{
		// Nokia API just says to call this instead, so this is done
		this.setFullScreenMode(true);
		
		// Since we need to simulate soft commands in the game, we have to
		// add our own commands and such to this.
		this.addCommand(FullCanvas._SOFT1);
		this.addCommand(FullCanvas._SOFT2);
		this.addCommand(FullCanvas._SOFT3);
		
		// Then use virtual command listener to forward
		this.setCommandListener(new __VirtualListener__());
	}
	
	/**
	 * Always throws {@link IllegalStateException} as commands are not
	 * supported in Nokia canvases.
	 *
	 * @param __c The command to add.
	 * @throws IllegalStateException Always.
	 * @since 2019/09/23
	 */
	@Override
	public void addCommand(Command __c)
	{
		// Since we are providing this special functionality we need to wrap
		// but still access these internal commands
		if (__c == FullCanvas._SOFT1 || __c == FullCanvas._SOFT2 || __c == FullCanvas._SOFT3)
		{
			super.addCommand(__c);
			return;
		}
		
		/* {@squirreljme.error EB2x Commands are not supported.} */
		throw new IllegalStateException("EB2x");
	}
	
	/**
	 * Always throws {@link IllegalStateException} as commands are not
	 * supported in Nokia canvases.
	 *
	 * @param __l The command to add.
	 * @throws IllegalStateException Always.
	 * @since 2019/09/23
	 */
	@Override
	public void setCommandListener(CommandListener __l)
	{
		// Since this is virtualized, we do want to handle this one!
		if (__l instanceof __VirtualListener__)
		{
			super.setCommandListener(__l);
			return;
		}
		
		/* {@squirreljme.error EB2y Commands are not supported.} */
		throw new IllegalStateException("EB2y");
	}
	
	/**
	 * Command listener to forward keys to the canvas.
	 *
	 * @since 2019/09/23
	 */
	private static final class __VirtualListener__
		implements CommandListener
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2019/09/23
		 */
		@Override
		public final void commandAction(Command __c, Displayable __d)
		{
			// Do nothing if this is some other thing
			if (!(__d instanceof FullCanvas))
				return;
			
			// Determine code to use
			int code;
			if (__c == FullCanvas._SOFT1)
				code = FullCanvas.KEY_SOFTKEY1;
			else if (__c == FullCanvas._SOFT2)
				code = FullCanvas.KEY_SOFTKEY2;
			else if (__c == FullCanvas._SOFT3)
				code = FullCanvas.KEY_SOFTKEY3;
				
				// Unknown?
			else
				return;
			
			// Press and release, since that is all we can do really!
			FullCanvas fc = (FullCanvas)__d;
			fc.keyPressed(code);
			fc.keyReleased(code);
		}
	}
}

