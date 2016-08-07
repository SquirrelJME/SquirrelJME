// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This is used to specify that any static method calls from the source class
 * are to be rewritten to they instead call the specified class. This is used
 * by the JIT so that calls to a specific set of static methods is replaced
 * with system dependent methods.
 *
 * @since 2016/08/07
 */
public final class JITStaticCallRewrite
{
	/** The source class. */
	protected final ClassNameSymbol from;
	
	/** The target class. */
	protected final ClassNameSymbol to;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the static call rewrite.
	 *
	 * @param __from The source class name.
	 * @param __to The destination class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/07
	 */
	public JITStaticCallRewrite(ClassNameSymbol __from, ClassNameSymbol __to)
		throws NullPointerException
	{
		// Check
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.from = __from;
		this.to = __to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/07
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Not this class?
		if (!(__o instanceof JITStaticCallRewrite))
			return false;
		
		// Cast
		JITStaticCallRewrite o = (JITStaticCallRewrite)__o;
		return this.from.equals(o.from) &&
			this.to.equals(o.to);
	}
	
	/**
	 * Returns the name of the class which should be rewritten.
	 *
	 * @return The class that is to be rewritten.
	 * @since 2016/08/07
	 */
	public final ClassNameSymbol from()
	{
		return this.from;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/07
	 */
	@Override
	public final int hashCode()
	{
		return this.from.hashCode() ^ this.to.hashCode();
	}
	
	/**
	 * Returns the name of the class it is being rewritten to.
	 *
	 * @return The class which should be called instead.
	 * @since 2016/08/07
	 */
	public final ClassNameSymbol to()
	{
		return this.to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/07
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = this.from + ">" + this.to));
		
		// Return
		return rv;
	}
}

