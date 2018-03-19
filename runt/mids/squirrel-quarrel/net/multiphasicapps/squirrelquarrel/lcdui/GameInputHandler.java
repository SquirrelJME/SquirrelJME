// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

import javax.microedition.lcdui.KeyListener;
import net.multiphasicapps.squirrelquarrel.world.Tile;

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
		Tile.PIXEL_SIZE / 2;
	
	/** The game keys which are held down. */
	protected final boolean[] gamekeys =
		new boolean[GAME_ACTION_COUNT];
	
	/** Is on the automap dragging? */
	private volatile boolean _ondragmap;
	
	/** Disable automap drag. */
	private volatile boolean _nomapdrag;
	
	/**
	 * Initializes the input handler for the game.
	 *
	 * @since 2017/02/12
	 */
	public GameInputHandler()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyPressed(int __code, int __mods)
	{
		throw new todo.TODO();
		/*
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
		}*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyReleased(int __code, int __mods)
	{
		throw new todo.TODO();
		/*
		// Parse key and set the game action as being released
		GameInterface gameinterface = this.gameinterface;
		try
		{
			int ga = gameinterface.getGameAction(__code);
			if (ga >= 0 && ga < GAME_ACTION_COUNT)
				this.gamekeys[ga] = false;
		}
		
		// Ignore, not a valid game action
		catch (IllegalArgumentException e)
		{
		}
		
		// Increase the game speed
		if (__code == '=' || __code == '+')
			gameinterface.setGameSpeed(gameinterface.gameSpeed().faster());
		
		// Lower the game speed
		else if (__code == '-' || __code == '_')
			gameinterface.setGameSpeed(gameinterface.gameSpeed().slower());*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyRepeated(int __code, int __mods)
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
		/*
		__checkAutomapDrag(true, __x, __y);*/
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
		throw new todo.TODO();
		/*
		__checkAutomapDrag(false, __x, __y);*/
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
		throw new todo.TODO();
		/*
		// Cannot be dragging on the map
		this._ondragmap = false;
		this._nomapdrag = false;*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
		/*
		GameInterface gameinterface = this.gameinterface;
		boolean[] gamekeys = this.gamekeys;
		
		// Pan the viewport?
		double gsratio = gameinterface.gameSpeed().ratio();
		double xpan = _PANNING_SPEED * (gamekeys[Canvas.LEFT] ? -gsratio :
			(gamekeys[Canvas.RIGHT] ? gsratio : 0)),
			ypan = _PANNING_SPEED * (gamekeys[Canvas.UP] ? -gsratio :
			(gamekeys[Canvas.DOWN] ? gsratio : 0));
		gameinterface.translateViewport((int)xpan, (int)ypan);*/
	}
	
	/**
	 * Checks for dragging on the automap.
	 *
	 * @param __x The cursor X position.
	 * @param __y The cursor Y position.
	 * @param __drag Is this a drag?
	 * @since 2017/02/13
	 */
	private void __checkAutomapDrag(boolean __drag, int __x, int __y)
	{
		throw new todo.TODO();
		/*
		// Get the automap
		GameInterface gameinterface = this.gameinterface;
		Automap automap = gameinterface.automap();
		int amw = automap.width(),
			amh = automap.height(),
			vw = gameinterface.viewportWidth(),
			vh = gameinterface.viewportHeight(),
			amby = (vh - amh);
		
		// If the cursor is where the automap would be
		// However, if the automap was dragged on, always drag
		if (this._ondragmap ||
			(__x >= 0 && __x < amw && __y >= amby && __y < vh))
		{
			// Press was not on the map, so do not drag even if it enters
			// range
			if (__drag && this._nomapdrag)
				return;
			
			Level level = gameinterface.level();
			int lpxw = level.pixelWidth(),
				lpxh = level.pixelHeight();
			
			// Calculate viewport position
			double vx = (__x) * ((double)lpxw / amw),
				vy = (__y - amby) * ((double)lpxh / amh);
			gameinterface.setViewport(
				((int)vx) - (vw / 2),
				((int)vy) - (vh / 2));
			
			// Keep dragging on the map
			this._ondragmap = true;
		}
		
		// Initial click outside of the map, do not drag on it
		else
		{
			if (!__drag)
				this._nomapdrag = true;
		}*/
	}
}

