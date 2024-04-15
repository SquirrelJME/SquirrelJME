// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * This is used to build games which can then be run and such.
 *
 * @since 2019/07/01
 */
public final class GameBuilder
{
	/** The game seed. */
	private volatile long _seed;
	
	/** The map size. */
	private volatile MapSize _mapsize;
	
	/** The number of players used. */
	private volatile int _numplayers;
	
	/**
	 * Initializes a base game builder.
	 *
	 * @since 2019/07/01
	 */
	public GameBuilder()
	{
		// Instead of using the time directly, derive it from a seed
		Random initseed = new Random((System.currentTimeMillis() * 37L) +
			System.nanoTime());
		this._seed = ((long)initseed.nextInt() << 32) ^ initseed.nextInt();
		
		// Set default parameters
		this._mapsize = MapSize.DEFAULT;
		this._numplayers = 2;
	}
	
	/**
	 * Builds the actual game.
	 *
	 * @return The resulting game.
	 * @since 2019/07/01
	 */
	public final Game build()
	{
		synchronized (this)
		{
			return new Game(this._seed,
				this._mapsize,
				this._numplayers);
		}
	}
	
	/**
	 * Sets the number of players in the game.
	 *
	 * @param __n The number of players to have.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the player count is out of range.
	 * @since 2019/07/01
	 */
	public final GameBuilder players(int __n)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error BE0s Out of range player count. (The count)} */
		if (__n < 1 || __n > Players.MAX_PLAYERS)
			throw new IllegalArgumentException("BE0s " + __n);
		
		synchronized (this)
		{
			this._numplayers = __n;
			return this;
		}
	}
	
	/**
	 * Sets the seed to use for the game.
	 *
	 * @param __v The seed to use.
	 * @return {@code this}.
	 * @since 2019/07/01
	 */
	public final GameBuilder seed(long __v)
	{
		synchronized (this)
		{
			this._seed = __v;
			return this;
		}
	}
	
	/**
	 * Builds and plays back a game from a replay.
	 *
	 * @param __in The input stream.
	 * @return The resulting game.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/01
	 */
	public static final Game fromReplay(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}

