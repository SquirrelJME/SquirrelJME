// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.cw;

import net.multiphasicapps.squirreljme.java.ci.CIMemberFlags;
import net.multiphasicapps.squirreljme.java.ci.CIMemberID;

/**
 * This is the base class for output members.
 *
 * @param <I> The identifier of the member.
 * @param <F> The flags used for the member.
 * @since 2016/06/21
 */
public abstract class OutputMember<I extends CIMemberID,
	F extends CIMemberFlags>
{
	/** The lock. */
	protected final Object lock;
	
	/** The outer class. */
	protected final OutputClass outerclass;
	
	/** The identifier of this member. */
	protected final I id;
	
	/** The class used for flags. */
	protected final Class<F> flagtype;
	
	/** The flags for this member. */
	private volatile F _flags;
	
	/**
	 * Initializes the output member.
	 *
	 * @param __oc The output class.
	 * @param __id The member identifier.
	 * @param __ft The type used for flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/21
	 */
	OutputMember(OutputClass __oc, I __id, Class<F> __ft)
		throws NullPointerException
	{
		// Check
		if (__oc == null || __id == null || __ft == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CO03 The specified class does not extend the
		// flags used for members.}
		if (__ft.isAssignableFrom(CIMemberFlags.class))
			throw new ClassCastException("CO03");
		
		// Set
		this.outerclass = __oc;
		this.lock = __oc.__lock();
		this.id = __id;
		this.flagtype = __ft;
	}
	
	/**
	 * Sets the flags which are used for the given member.
	 *
	 * @param __fl The flags to use.
	 * @throws ClassCastException If the input flags were not of the correct
	 * class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/21
	 */
	public final void setFlags(F __fl)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__fl == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._flags = this.flagtype.cast(__fl);
		}
	}
}

