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
import net.multiphasicapps.squirreljme.jit.java.MethodHandle;

/**
 * This is used to verify that the source method can allocate the given class.
 *
 * @since 2017/09/22
 */
public final class CanAllocateCheck
	implements VerificationCheck
{
	/** The source method. */
	protected final MethodHandle source;
	
	/** The target class to be allocated. */
	protected final ClassName target;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the check.
	 *
	 * @param __src The method performing the allocation.
	 * @param __cl The target class to be allocated.
	 * @throws NullPointerException On nullarguments.
	 * @since 2017/09/22
	 */
	public CanAllocateCheck(MethodHandle __src, ClassName __cl)
		throws NullPointerException
	{
		if (__src == null || __cl == null)
			throw new NullPointerException("NARG");
		
		this.source = __src;
		this.target = __cl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof CanAllocateCheck))
			return false;
		
		CanAllocateCheck o = (CanAllocateCheck)__o;
		return this.source.equals(o.source) &&
			this.target.equals(o.target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public int hashCode()
	{
		return this.source.hashCode() ^
			this.target.hashCode();
	}
	
	/**
	 * Returns the source method performing the allocation.
	 *
	 * @return The source method for allocation.
	 * @since 2017/09/22
	 */
	public MethodHandle source()
	{
		return this.source;
	}
	
	/**
	 * Returns the target class to be allocated.
	 *
	 * @return The target class to allocate.
	 * @since 2017/09/22
	 */
	public ClassName target()
	{
		return this.target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Can %s allocate %s?", this.source, this.target)));
		
		return rv;
	}
}

