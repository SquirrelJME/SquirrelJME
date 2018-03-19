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

/*import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.squirrelquarrel.Game;
import net.multiphasicapps.squirrelquarrel.GameSpeed;
import net.multiphasicapps.squirrelquarrel.Level;
import net.multiphasicapps.squirrelquarrel.MegaTile;
import net.multiphasicapps.squirrelquarrel.Player;
import net.multiphasicapps.squirrelquarrel.TerrainType;
import net.multiphasicapps.squirrelquarrel.Unit;
import net.multiphasicapps.squirrelquarrel.UnitDeletedException;
import net.multiphasicapps.squirrelquarrel.UnitInfo;
import net.multiphasicapps.squirrelquarrel.UnitType;*/

/**
 * This class provides an interface to the game, allowing for input to be
 * handled along with the game itself.
 *
 * @since 2017/02/08
 */
public class GameInterface
	extends Canvas
{
	/** The game being played. */
	protected final GameLooper looper;
	
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
		
		this.looper = __g;
		
		// Setup details
		setTitle("Squirrel Quarrel");
		
		// Use self as the key listener
		throw new todo.TODO();
		/*
		this.setKeyListener(this.inputhandler);*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/18
	 */
	@Override
	public void paint(Graphics __g)
	{
		throw new todo.TODO();
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

