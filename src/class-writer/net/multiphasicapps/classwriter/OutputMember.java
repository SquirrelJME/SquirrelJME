// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classwriter;

import net.multiphasicapps.squirreljme.ci.CIMemberID;

/**
 * This is the base class for output members.
 *
 * @param <I> The identifier of the member.
 * @since 2016/06/21
 */
public abstract class OutputMember<I extends CIMemberID>
{
	/** The outer class. */
	protected final OutputClass outerclass;
	
	/** The identifier of this member. */
	protected final I id;
	
	/**
	 * Initializes the output member.
	 *
	 * @param __oc The output class.
	 * @param __id The member identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/21
	 */
	OutputMember(OutputClass __oc, I __id)
		throws NullPointerException
	{
		// Check
		if (__oc == null || __id == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.outerclass = __oc;
		this.id = __id;
	}
}

