// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.ui;

import net.multiphasicapps.squirrelquarrel.game.GameLooper;

/**
 * This represents a single game screen which contains a viewport and
 * rendering information.
 *
 * @since 2018/03/19
 */
public final class GameScreen
{
	/** The game being looped. */
	protected final GameLooper looper;
	
	/** The screen X position. */
	private volatile int _sx;
	
	/** The screen Y position. */
	private volatile int _sy;
	
	/** The screen width. */
	private volatile int _sw;
	
	/** The screen height. */
	private volatile int _sh;
	
	/**
	 * Initializes this game screen.
	 *
	 * @param __g The game to draw.
	 * @throws NullPointerException On null arguments.
	 * @since 2108/03/19
	 */
	public GameScreen(GameLooper __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this.looper = __g;
	}
	
	/**
	 * Configures this screen.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/19
	 */
	public final void configure(int __x, int __y, int __w, int __h)
	{
		this._sx = __x;
		this._sy = __y;
		this._sw = __w;
		this._sh = __h;
	}
}

