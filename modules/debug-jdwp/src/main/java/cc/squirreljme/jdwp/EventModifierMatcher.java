// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Matcher for event modifiers, if any.
 *
 * @since 2021/03/14
 */
@Deprecated
public interface EventModifierMatcher
{
	/**
	 * Matches for the given event.
	 * 
	 * @param __mod The modifier to check.
	 * @return If this is a match.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/14
	 */
	boolean isMatch(EventModifier __mod)
		throws NullPointerException;
	
	/**
	 * May this match against the given modifiers?
	 * 
	 * @param __mod The modifier to check.
	 * @return If this can be used to match.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/14
	 */
	boolean mayMatch(EventModifier __mod)
		throws NullPointerException;
}
