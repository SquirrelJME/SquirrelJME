// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is single card which resides within a deck and uses the specified face.
 * Each card which contains a face represents a single instruction if it is
 * defined as such.
 *
 * This class is not thread safe.
 *
 * @see Deck
 * @see Face
 * @since 2017/04/01
 */
public final class Card
{
	/** The owning deck. */
	protected final Deck deck;
	
	/**
	 * Internally initializes the card.
	 *
	 * @param __d The owning deck.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/01
	 */
	Card(Deck __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.deck;
	}
}

