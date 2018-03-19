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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.KeyListener;
import net.multiphasicapps.squirrelquarrel.game.Game;
import net.multiphasicapps.squirrelquarrel.game.GameLooper;
import net.multiphasicapps.squirrelquarrel.ui.FrameSync;
import net.multiphasicapps.squirrelquarrel.ui.SplitScreen;

/**
 * This class provides an interface to the game, allowing for input to be
 * handled along with the game itself.
 *
 * @since 2017/02/08
 */
public final class GameInterface
	extends Canvas
	implements FrameSync
{
	/** The game being played. */
	protected final GameLooper looper;
	
	/** Splitscreen. */
	protected final SplitScreen splitscreen;
	
	/**
	 * Initializes the game interface.
	 *
	 * @param __g The game being played.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public GameInterface(GameLooper __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		
		// Setup details
		setTitle("Squirrel Quarrel");
		
		this.looper = __g;
		this.splitscreen = new SplitScreen(__g);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/19
	 */
	public final void frameRepaintRequest(int __framenum)
	{
		// Just have it get repainted
		this.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/18
	 */
	@Override
	public void paint(Graphics __g)
	{
		// Get the canvas size
		int cw = this.getWidth(),
			ch = this.getHeight();
		
		// Configure splitscreens
		SplitScreen splitscreen = this.splitscreen;
		splitscreen.configure(cw, ch);
		
		//throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void pointerDragged(int __x, int __y)
	{
		throw new todo.TODO();
		/*
		this.inputhandler.pointerDragged(__x, __y);*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void pointerPressed(int __x, int __y)
	{
		throw new todo.TODO();
		/*
		this.inputhandler.pointerPressed(__x, __y);*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void pointerReleased(int __x, int __y)
	{
		throw new todo.TODO();
		/*
		this.inputhandler.pointerReleased(__x, __y);*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	protected void sizeChanged(int __w, int __h)
	{
		// Super-class might do some things
		super.sizeChanged(__w, __h);
		
		throw new todo.TODO();
	}
}

