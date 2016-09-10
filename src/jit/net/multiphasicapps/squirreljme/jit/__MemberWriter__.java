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

import net.multiphasicapps.squirreljme.classformat.MemberDescriptionStream;

/**
 * This bridges the member description stream to member writing.
 *
 * @since 2016/09/10
 */
abstract class __MemberWriter__
	implements MemberDescriptionStream
{
	/** The owning JIT. */
	protected final JIT jit;
	
	/** The namespace writer used. */
	protected final JITNamespaceWriter namespace;
	
	/** The owning class writer. */
	final __ClassWriter__ classwriter;
	
	/**
	 * Initializes the base member writer.
	 *
	 * @param __jit The running JIT.
	 * @param __nsw The namespace writer.
	 * @param __cw The owning class writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	__MemberWriter__(JIT __jit, JITNamespaceWriter __nsw, __ClassWriter__ __cw)
		throws NullPointerException
	{
		// Check
		if (__jit == null || __nsw == null || __cw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
		this.namespace = __nsw;
		this.classwriter = __cw;
	}
}

