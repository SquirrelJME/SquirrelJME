// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci.standard;

import net.multiphasicapps.squirreljme.java.ci.CIClass;
import net.multiphasicapps.squirreljme.java.ci.CIMember;
import net.multiphasicapps.squirreljme.java.ci.CIMemberFlags;
import net.multiphasicapps.squirreljme.java.ci.CIMemberID;

/**
 * This is the base class for .
 *
 * @param <I> The identifier for the member.
 * @param <F> The flags used for the member.
 * @since 2016/04/26
 */
public abstract class CISMember<I extends CIMemberID,
	F extends CIMemberFlags>
	implements CIMember<I, F>
{
	/** The class containing this member. */
	protected final CIClass outerclass;
	
	/** The identifier and type of this member. */
	protected final I id;
	
	/** The flags for this member. */
	protected final F flags;
	
	/**
	 * Initializes the base member information.
	 *
	 * @param __oc The outer class.
	 * @param __id The identifier of the member.
	 * @param __fl The member flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/26
	 */
	CISMember(CISClass __oc, I __id, F __fl)
		throws NullPointerException
	{
		// Check
		if (__oc == null || __id == null || __fl == null)
			throw new NullPointerException("NARG");
		
		// Set
		outerclass = __oc;
		id = __id;
		flags = __fl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/26
	 */
	@Override
	public F flags()
	{
		return flags;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/26
	 */
	@Override
	public I nameAndType()
	{
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/26
	 */
	@Override
	public CIClass outerClass()
	{
		return outerclass;
	}
}

