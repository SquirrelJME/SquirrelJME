// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.depends;

/**
 * This class contains the results of a dependency match.
 *
 * @since 2017/11/30
 */
public final class MatchResult
{
	/**
	 * Returns {@code true} if there have been matches in the result.
	 *
	 * @return Has there been any matches?
	 * @since 2017/11/30
	 */
	public final boolean hasMatches()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the dependency information which contains every dependency
	 * which has been matched.
	 *
	 * @return The dependency information containing only matched items.
	 * @since 2017/11/30
	 */
	public final DependencyInfo matched()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the dependency information which contains every dependency
	 * which has yet to be matched.
	 *
	 * @return The dependency information containing only unmatched items.
	 * @since 2017/11/30
	 */
	public final DependencyInfo unmatched()
	{
		throw new todo.TODO();
	}
}

