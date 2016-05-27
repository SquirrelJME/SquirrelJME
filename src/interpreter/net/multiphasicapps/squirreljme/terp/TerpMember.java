// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.ci.CIMember;
import net.multiphasicapps.squirreljme.ci.CIMemberFlags;
import net.multiphasicapps.squirreljme.ci.CIMemberID;

/**
 * This represents a member which exists within a class.
 *
 * @param <M> The base member type
 * @since 2016/04/22
 */
public abstract class TerpMember<M extends CIMember>
{
	/** The execution core. */
	protected final TerpCore core;
	
	/** The owning class. */
	protected final TerpClass outerclass;
	
	/** The base member data. */
	protected final M base;
	
	/** The string representation of this member. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the class member.
	 *
	 * @param __oc The outer class.
	 * @param __m The base member.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/22
	 */
	public TerpMember(TerpClass __oc, M __m)
		throws NullPointerException
	{
		// Check
		if (__oc == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Set
		outerclass = __oc;
		core = __oc.core();
		base = __m;
	}
	
	/**
	 * Returns the flags of this member.
	 *
	 * @return The member flags.
	 * @since 2016/04/26
	 */
	public abstract CIMemberFlags flags();
	
	/**
	 * Returns the base member.
	 *
	 * @return The member base.
	 * @since 2016/04/26
	 */
	public final M base()
	{
		return base;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/27
	 */
	@Override
	public String toString()
	{
		// Get reference
		Reference<String> ref = _string;
		String rv;
		
		// Needs caching?
		CIMemberID nat = base.nameAndType();
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = outerclass.thisName() + "::" +
				nat.name() + ":" + nat.type()));
		
		// Return the given string
		return rv;
	}
}

