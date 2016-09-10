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

import net.multiphasicapps.squirreljme.classformat.CodeDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodDescriptionStream;

/**
 * This bridges the method description stream to the method writer.
 *
 * @since 2016/09/10
 */
class __MethodWriter__
	extends __MemberWriter__
	implements MethodDescriptionStream
{
	/** The method writer to write to. */
	protected final JITMethodWriter methodwriter;
	
	/**
	 * Initializes the method writer.
	 *
	 * @param __jit The running JIT.
	 * @param __nsw The namespace writer.
	 * @param __cw The owning class writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	__MethodWriter__(JIT __jit, JITNamespaceWriter __nsw, __ClassWriter__ __cw,
		JITMethodWriter __jmw)
		throws NullPointerException
	{
		super(__jit, __nsw, __cw);
		
		// Check
		if (__jmw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.methodwriter =__jmw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public CodeDescriptionStream code()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public void endMember()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public void noCode()
	{
		throw new Error("TODO");
	}
}

