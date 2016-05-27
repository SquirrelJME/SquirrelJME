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

/**
 * This represents an object which exists within the virtual machine.
 *
 * @since 2016/04/27
 */
public class TerpObject
{
	/** The owning core. */
	protected final TerpCore core;
	
	/** The associated class type used. */
	protected final TerpClass niclass;
	
	/** The object's internal hash code. */
	protected final int hashcode;
	
	/** The string representation of this object. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes an object (but does not construct it) for usage by the
	 * virtual machine.
	 *
	 * @param __c The owning core of the given object.
	 * @param __cl The class to initialize the object for.
	 * @throws TerpException If the object could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public TerpObject(TerpCore __c, TerpClass __cl)
		throws TerpException, NullPointerException
	{
		this(__c, __cl, 0);
	}
	
	/**
	 * Initializes an object (but does not construct it) for usage by the
	 * virtual machine.
	 *
	 * @param __c The owning core of the given object.
	 * @param __cl The class to initialize the object for.
	 * @param __al The length of the array.
	 * @throws TerpException If the object could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public TerpObject(TerpCore __c, TerpClass __cl, int __al)
		throws TerpException, NullPointerException
	{
		// Check
		if (__c == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AN0j An attempt was made to allocate an array
		// for a class which is not an array. (The class)}
		if (__al != 0 && !__cl.isArray())
			throw new TerpException(__c, TerpException.Issue.NOT_AN_ARRAY,
				String.format("AN0j %s", __cl.thisName()));
		
		// {@squirreljme.error AN0i Attempted to allocate an array with a
		// negative length. (The array length)}
		if (__al < 0)
			throw new TerpException(__c, TerpException.Issue.NEGATIVE_ARRAY_LENGTH,
				String.format("AN0i %d", __al));
		
		// {@squirreljme.error AN0h Attempted to initialize an object which
		// is an instance of an abstract class. (The class)}
		if (__cl.flags().isAbstract())
			throw new TerpException(__c, TerpException.Issue.NEW_ABSTRACT,
				String.format("AN0h %s", __cl.thisName()));
		
		// Set
		this.core = __c;
		this.niclass = __cl;
		
		// Hashcode is the next object index
		this.hashcode = __c.nextHashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/13
	 */
	@Override
	public boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/13
	 */
	@Override
	public int hashCode()
	{
		return hashcode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/13
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = _string;
		String rv;
		
		// Needs initialization?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>(
				(rv = String.format("%s@%x", this.niclass.thisName(),
					this.hashcode)));
		
		// Return it
		return rv;
	}
}

