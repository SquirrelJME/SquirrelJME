// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This represents a member which exists within a class.
 *
 * @param <S> The symbol type.
 * @param <K> The key type.
 * @since 2016/04/22
 */
public abstract class NIMember
{
	/** The owning class. */
	protected final NIMember outerclass;
	
	/**
	 * Initializes the class member.
	 *
	 * @param __oc The outer class.
	 * @since 2016/04/22
	 */
	public NIMember(NIClass __oc)
		throws NullPointerException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		// Set
		outerclass = __oc;
	}
}

