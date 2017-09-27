// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.verifier;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.java.ClassName;

/**
 * This describes a check which is used to verify that the specified class can
 * extend the specified target class.
 *
 * @since 2017/09/01
 */
public final class CanExtendCheck
	implements VerificationCheck
{
	/** The source class. */
	protected final ClassName source;
	
	/** The target class. */
	protected final ClassName target;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the check.
	 *
	 * @param __s The source class.
	 * @param __t The target class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/01
	 */
	public CanExtendCheck(ClassName __s, ClassName __t)
		throws NullPointerException
	{
		// Check
		if (__s == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.source = __s;
		this.target = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof CanExtendCheck))
			return false;
		
		CanExtendCheck o = (CanExtendCheck)__o;
		return this.source.equals(o.source) &&
			this.target.equals(o.target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public int hashCode()
	{
		return this.source.hashCode() ^ this.target.hashCode();
	}
	
	/**
	 * Returns the source class.
	 *
	 * @return The source class.
	 * @since 2017/09/01
	 */
	public ClassName source()
	{
		return this.source;
	}
	
	/**
	 * Returns the target class.
	 *
	 * @return The target class.
	 * @since 2017/09/01
	 */
	public ClassName target()
	{
		return this.target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s extends %s?", this.source, this.target)));
		
		return rv;
	}
}

