// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.library;

import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This interface is used to represent members.
 *
 * @param <I> The member identifier that this uses.
 * @since 2016/04/22
 */
public interface NLMember<I extends NLMemberID>
{
	/**
	 * Returns the name and type of the member.
	 *
	 * @return The member name and type.
	 * @since 2016/04/22
	 */
	public abstract I nameAndType();
}

