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
 * This represents a class which is exported.
 *
 * @since 2017/04/02
 */
public final class ClassExport
	implements Export
{
	/** The name of the exported class. */
	protected final ClassNameSymbol name;
	
	/** The flags for the exported class. */
	protected final ClassFlags flags;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the exported class.
	 *
	 * @param __n The name of the exported class.
	 * @param __f The flags for the exported class.
	 * @throws NullPointerException
	 * @since 2017/04/02
	 */
	public ClassExport(ClassNameSymbol __n, ClassFlags __f)
		throws NullPointerException
	{
		// Check
		if (__n == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.flags = __f;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/02
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ClassExport))
			return false;
		
		ClassExport o = (ClassExport)__o;
		return this.name.equals(o.name) && this.flags.equals(o.flags);
	}
	
	/**
	 * Returns the flags of the exported class.
	 *
	 * @return The exported class flags.
	 * @since 2017/04/02
	 */
	public ClassFlags flags()
	{
		return this.flags;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/02
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.flags.hashCode();
	}
	
	/**
	 * Returns the name of the exported class.
	 *
	 * @return The exported class name.
	 * @since 2017/04/02
	 */
	public ClassNameSymbol name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/02
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.name + ":" +
				this.flags));
		
		return rv;
	}
}

