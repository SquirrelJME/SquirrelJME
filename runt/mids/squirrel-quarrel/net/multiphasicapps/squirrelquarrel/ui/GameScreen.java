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
import net.multiphasicapps.squirrelquarrel.player.Player;
import net.multiphasicapps.squirrelquarrel.world.World;

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
	
	/** The player being drawn. */
	protected final Player player;
	
	/** The viewport for the screen. */
	protected final Viewport viewport;
	
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
	 * @param __p The player being drawn.
	 * @throws NullPointerException On null arguments.
	 * @since 2108/03/19
	 */
	public GameScreen(GameLooper __g, Player __p)
		throws NullPointerException
	{
		if (__g == null || __p == null)
			throw new NullPointerException("NARG");
		
		this.looper = __g;
		this.player = __p;
		
		// Initialize the viewport
		World world = __g.game().world();
		this.viewport = new Viewport(world.pixelWidth(),
			world.pixelHeight());
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
		
		// Adjust the viewport accordingly
		this.viewport.setSize(__w, __h);
	}
	
	/**
	 * Returns the height.
	 *
	 * @return The height.
	 * @since 2018/03/22
	 */
	public final int height()
	{
		return this._sh;
	}
	
	/**
	 * Returns the player being drawn.
	 *
	 * @return The player to draw.
	 * @since 2018/03/22
	 */
	public final Player player()
	{
		return this.player;
	}
	
	/**
	 * Returns the viewport of the view.
	 *
	 * @return The game viewport.
	 * @since 2018/03/22
	 */
	public final Viewport viewport()
	{
		return this.viewport;
	}
	
	/**
	 * Returns the width.
	 *
	 * @return The width.
	 * @since 2018/03/22
	 */
	public final int width()
	{
		return this._sw;
	}
	
	/**
	 * Returns the X position.
	 *
	 * @return The X position.
	 * @since 2018/03/22
	 */
	public final int x()
	{
		return this._sx;
	}
	
	/**
	 * Returns the Y position.
	 *
	 * @return The Y position.
	 * @since 2018/03/22
	 */
	public final int y()
	{
		return this._sy;
	}
}

