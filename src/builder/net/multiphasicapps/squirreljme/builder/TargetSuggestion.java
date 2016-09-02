// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

/**
 * This contains a suggestion for a target.
 *
 * @since 2016/07/22
 */
public final class TargetSuggestion
	implements Comparable<TargetSuggestion>
{
	/** The triplet. */
	protected final JITTriplet triplet;
	
	/** The name. */
	protected final String name;
	
	/**
	 * Initializes the suggestion.
	 *
	 * @param __t The target triplet.
	 * @param __n The name associated with it.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public TargetSuggestion(JITTriplet __t, String __n)
		throws NullPointerException
	{
		// Check
		if (__t == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.triplet = __t;
		this.name = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	public int compareTo(TargetSuggestion __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Sort by triplet first.
		int rv = this.triplet.toString().compareTo(__o.triplet.toString());
		if (rv != 0)
			return rv;
		
		// Compare the name next
		return this.name.compareTo(__o.name);
	}
	
	/**
	 * Returns the name.
	 *
	 * @return The name.
	 * @since 2016/07/22
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * Returns the triplet.
	 *
	 * @return The triplet.
	 * @since 2016/07/22
	 */
	public final JITTriplet triplet()
	{
		return this.triplet;
	}
}

