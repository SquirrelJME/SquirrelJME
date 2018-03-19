// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.game;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the state for a single game.
 *
 * @since 2017/02/08
 */
public class Game
	implements Runnable
{
	/** Random number generator for games. */
	protected final GameRandom random;
	
	/** The level data. */
	protected final Level level;
	
	/** Units in the game. */
	private final List<Unit> _units =
		new ArrayList<>();
	
	/** Players in the game. */
	private final Player[] _players =
		new Player[PlayerColor.NUM_PLAYERS];
	
	/** The current game frame. */
	private volatile int _framenum;
	
	/**
	 * Shared initialization.
	 *
	 * @since 2017/02/14
	 */
	{
		// Initialize players
		Player[] players = this._players;
		for (int i = 0, n = players.length; i < n; i++)
			players[i] = new Player(this, PlayerColor.of(i));
	}
	
	/**
	 * Initializes a game with the default initialization rules.
	 *
	 * @since 2017/02/08
	 */
	public Game()
	{
		this(new InitialSettingsBuilder().build());
	}
	
	/**
	 * Initializes the game with the given initial settings.
	 *
	 * @param __is The settings to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/09
	 */
	public Game(InitialSettings __is)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Setup random number generator
		this.random = new GameRandom(__is.seed());
		
		// Initialize the level using the initial settings
		Level level = new Level(this, __is);
		this.level = level;
		
		// Add start location for the first player as a test, which is
		// transformed accordingly
		__createUnit(SpawnPlacementType.BUILDING, UnitType.START_LOCATION,
			(Unit.Pointer)null, 128, 128);
	}
	
	/**
	 * Initializes a game from a previous game serialization such as a saved
	 * game or replay.
	 *
	 * @param __is The input stream to read the game from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	public Game(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Wrap in a data input stream
		DataInputStream input = ((__is instanceof DataInputStream) ?
			(DataInputStream)__is : new DataInputStream(__is));
		
		// Re-initialize the random number generator
		GameRandom random = new GameRandom(0);
		random.setRawSeed((((long)input.readInt()) << 32L) |
			input.readInt());
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the current game frame.
	 *
	 * @return The game frame.
	 * @since 2017/02/10
	 */
	public int frameCount()
	{
		return this._framenum;
	}
	
	/**
	 * Returns the level.
	 *
	 * @since 2017/02/10
	 */
	public Level level()
	{
		return this.level;
	}
	
	/**
	 * Returns the player by the given index.
	 *
	 * @param __i The index of the player to get.
	 * @return The player for the given index.
	 * @throws IllegalArgumentException If the index is not within bounds.
	 * @since 2017/02/14
	 */
	public Player player(int __i)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BE01 Invalid player index. (The index)}
		if (__i < 0 || __i >= PlayerColor.NUM_PLAYERS)
			throw new IllegalArgumentException(String.format("BE01 %d", __i));
		
		return this._players[__i];
	}
	
	/**
	 * Returns the player by the given color.
	 *
	 * @param __p The color to get the player of.
	 * @return The player for the given color.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/14
	 */
	public Player player(PlayerColor __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return this._players[__p.ordinal()];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void run()
	{
		// Get current frame
		int framenum = this._framenum;
		
		// Run the level logic (includes the megatiles)
		this.level.__run(framenum);
		
		// Run the player logic for each player
		Player[] players = this._players;
		for (int i = 0, n = players.length; i < n; i++)
			players[i].__run(framenum);
		
		// Run the unit logic for every unit
		List<Unit> units = this._units;
		for (int i = 0, n = units.size(); i < n; i++)
			if (units.get(i).__run(framenum))
				units.set(i, null);
		
		// Increase the game frame
		this._framenum = framenum + 1;
	}
	
	/**
	 * Creates a new unit.
	 *
	 * @param __spt How the unit should be placed on the map.
	 * @param __t The type of unit to spawn.
	 * @param __creator The creating unit, may be {@code null}.
	 * @param __x The target X position.
	 * @param __y The target Y position.
	 * @return The created unit or {@code null} if it could not be created.
	 * @throws NullPointerException If no spawn type or unit type was
	 * specified.
	 * @since 2017/02/16
	 */
	final Unit __createUnit(SpawnPlacementType __spt, UnitType __t,
		Unit __creator, int __x, int __y)
		throws NullPointerException
	{
		return __createUnit(__spt, __t, (__creator != null ?
			__creator.pointer() : null), __x, __y);
	}
	
	/**
	 * Creates a new unit.
	 *
	 * @param __spt How the unit should be placed on the map.
	 * @param __t The type of unit to spawn.
	 * @param __creator The creating unit, may be {@code null}.
	 * @param __x The target center X position.
	 * @param __y The target center Y position.
	 * @return The created unit or {@code null} if it could not be created.
	 * @throws NullPointerException If no spawn type or unit type was
	 * specified.
	 * @since 2017/02/16
	 */
	final Unit __createUnit(SpawnPlacementType __spt, UnitType __t,
		Unit.Pointer __creator, int __x, int __y)
		throws NullPointerException
	{
		// Check
		if (__spt == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Setup unit
		Unit rv = new Unit(this);
		UnitInfo info = __t.info();
		
		// Morph to the given unti type
		rv.morph(__t);
		
		// Determine the location where the unit is to be placed
		int px, py;
		switch (__spt)
		{
				// No restriction
			case FORCED:
				px = __x;
				py = __y;
				break;
				
				// Building, 
			case BUILDING:
				px = info.placeBuilding(false, __x);
				py = info.placeBuilding(true, __y);
				break;
				
				// Normal placement
			case NORMAL:
				throw new todo.TODO();
				
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
		
		// Move unit to the specified position
		rv.__move(px, py);
		
		// Try to replace an existing null with this new unit
		List<Unit> units = this._units;
		boolean didset = false;
		for (int n = units.size(), i = n - 1; i >= 0; i++)
			if (units.get(i) == null)
			{
				units.set(i, rv);
				didset = true;
				break;
			}
		
		// Otherwise place at end
		if (!didset)
			units.add(rv);
		
		// Link unit into the map
		rv.__link(true);
		
		// Return it
		return rv;
	}
}

