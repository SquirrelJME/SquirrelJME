// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.MethodReference;

/**
 * Represents a linkage for special invocations, which have a source and a
 * target.
 *
 * @since 2023/06/03
 */
public final class InvokeSpecialLinkage
	implements Linkage
{
	/** The source method name and type. */
	public final MethodNameAndType source;
	
	/** The target method. */
	public final MethodReference target;
	
	/**
	 * Initializes the linkage.
	 * 
	 * @param __source The source method name and type.
	 * @param __target The target reference.
	 * @throws IllegalArgumentException If the target is an interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/03
	 */
	public InvokeSpecialLinkage(MethodNameAndType __source,
		MethodReference __target)
		throws IllegalArgumentException, NullPointerException
	{
		if (__source == null || __target == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error NC09 Invoke special target cannot be an
		// interface reference.}
		if (__target.isInterface())
			throw new IllegalArgumentException("NC09");
		
		this.source = __source;
		this.target = __target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/03
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof InvokeSpecialLinkage))
			return false;
		
		InvokeSpecialLinkage o = (InvokeSpecialLinkage)__o;
		return this.source.equals(o.source) &&
			this.target.equals(o.target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/03
	 */
	@Override
	public int hashCode()
	{
		return this.source.hashCode() ^ this.target.hashCode();
	}
}
