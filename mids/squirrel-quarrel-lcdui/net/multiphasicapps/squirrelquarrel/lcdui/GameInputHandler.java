// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.KeyListener;
import net.multiphasicapps.squirrelquarrel.MegaTile;

/**
 * This handles the input for the game such as which keys are held down and
 * buttons for perfomring actions.
 *
 * @since 2017/02/12
 */
public class GameInputHandler
	implements KeyListener, Runnable
{
	/** The maximum number of supported game actions. */
	public static final int GAME_ACTION_COUNT =
		16;
	
	/** Level panning speed. */
	private static final int _PANNING_SPEED =
		MegaTile.TILE_PIXEL_SIZE / 2;
	
	/** The game interface to interact with. */
	protected final GameInterface gameinterface;
	
	/** The game keys which are held down. */
	protected final boolean[] gamekeys =
		new boolean[GAME_ACTION_COUNT];
	
	/**
	 * Initializes the input handler for the game.
	 *
	 * @param __gi The owning game interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/12
	 */
	public GameInputHandler(GameInterface __gi)
		throws NullPointerException
	{
		// Check
		if (__gi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.gameinterface = __gi;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyPressed(int __code, int __mods)
	{
		// Parse key and set the game action as being pressed
		try
		{
			int ga = this.gameinterface.getGameAction(__code);
			if (ga >= 0 && ga < GAME_ACTION_COUNT)
				this.gamekeys[ga] = true;
		}
		
		// Ignore, not a valid game action
		catch (IllegalArgumentException e)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyReleased(int __code, int __mods)
	{
		// Parse key and set the game action as being released
		try
		{
			int ga = this.gameinterface.getGameAction(__code);
			if (ga >= 0 && ga < GAME_ACTION_COUNT)
				this.gamekeys[ga] = false;
		}
		
		// Ignore, not a valid game action
		catch (IllegalArgumentException e)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyRepeated(int __code, int __mods)
	{
	}
	
	/**
	 * Call when the pointer is dragged.
	 *
	 * @param __x X position.
	 * @param __y Y position.
	 * @since 2017/02/12
	 */
	protected void pointerDragged(int __x, int __y)
	{
		System.err.printf("DEBUG -- Dragged (%d, %d)%n", __x, __y);
	}
	
	/**
	 * Call when the pointer is pressed.
	 *
	 * @param __x X position.
	 * @param __y Y position.
	 * @since 2017/02/12
	 */
	protected void pointerPressed(int __x, int __y)
	{
		System.err.printf("DEBUG -- Pressed (%d, %d)%n", __x, __y);
	}
	
	/**
	 * Call when the pointer is released.
	 *
	 * @param __x X position.
	 * @param __y Y position.
	 * @since 2017/02/12
	 */
	protected void pointerReleased(int __x, int __y)
	{
		System.err.printf("DEBUG -- Released (%d, %d)%n", __x, __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void run()
	{
		GameInterface gameinterface = this.gameinterface;
		boolean[] gamekeys = this.gamekeys;
		
		// Pan the viewport?
		int xpan = _PANNING_SPEED * (gamekeys[Canvas.LEFT] ? -1 :
			(gamekeys[Canvas.RIGHT] ? 1 : 0)),
			ypan = _PANNING_SPEED * (gamekeys[Canvas.UP] ? -1 :
			(gamekeys[Canvas.DOWN] ? 1 : 0));
		gameinterface.translateViewport(xpan, ypan);
	}
}

