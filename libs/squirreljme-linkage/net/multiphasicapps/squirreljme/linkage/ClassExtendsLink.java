// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.linkage;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This is used to represent extending links from one class to another.
 *
 * @since 2017/04/03
 */
public final class ClassExtendsLink
	implements Linkage
{
	/** The source class from the link. */
	protected final ClassExport from;
	
	/** The class being extended. */
	protected final ClassNameSymbol extending;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the extending link.
	 *
	 * @param __from The class linking from.
	 * @param __to The class being extended.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/03
	 */
	public ClassExtendsLink(ClassExport __from, ClassNameSymbol __to)
		throws NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.from = __from;
		this.extending = __to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/03
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ClassExtendsLink))
			return false;
		
		ClassExtendsLink o = (ClassExtendsLink)__o;
		return this.from.equals(o.from) && this.extending.equals(o.extending);
	}
	
	/**
	 * Returns the class that this extends.
	 *
	 * @return The class this this extends.
	 * @since 2017/04/03
	 */
	public ClassNameSymbol extending()
	{
		return this.extending;
	}
	
	/**
	 * Returns the class that is doing the extending.
	 *
	 * @return The class that is extending.
	 * @since 2017/04/03
	 */
	public ClassExport from()
	{
		return this.from;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/03
	 */
	@Override
	public int hashCode()
	{
		return this.from.hashCode() ^ this.extending.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/03
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.from + " extends " +
				this.extending));
		
		return rv;
	}
}

