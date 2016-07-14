// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;

/**
 * This interface is used to represent members.
 *
 * @param <I> The member identifier that this uses.
 * @param <F> The flag type for this member.
 * @since 2016/04/22
 */
public interface CIMember<I extends CIMemberID, F extends CIMemberFlags>
{
	/**
	 * Returns the flags associated with the member.
	 *
	 * @return The member flags.
	 * @since 2016/04/26
	 */
	public abstract F flags();
	
	/**
	 * Returns the name and type of the member.
	 *
	 * @return The member name and type.
	 * @since 2016/04/22
	 */
	public abstract I nameAndType();
	
	/**
	 * Returns the class which contains this member.
	 *
	 * @return The class containing this member.
	 * @since 2016/04/26
	 */
	public abstract CIClass outerClass();
}

