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
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This acts as the middle layer between the class format decoded class files
 * and the JIT that is used to generate native code.
 *
 * @since 2016/09/14
 */
class __CodeWriter__
	implements AutoCloseable, CodeDescriptionStream
{
	/** The owning JIT. */
	protected final JIT jit;
	
	/** The namespace writer. */
	protected final JITNamespaceWriter namespace;
	
	/** The method writer. */
	protected final __MethodWriter__ methodwriter;
	
	/** The code writer. */
	protected final JITCodeWriter codewriter;
	
	/**
	 * Initialize the code writer.
	 *
	 * @param __jit The JIT used for compilation.
	 * @param __nsw The namespace writer.
	 * @param __mw The owning method writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	__CodeWriter__(JIT __jit, JITNamespaceWriter __nsw, __MethodWriter__ __mw,
		JITCodeWriter __jcw)
		throws NullPointerException
	{
		// Check
		if (__jit == null || __nsw == null || __mw == null || __jcw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
		this.namespace = __nsw;
		this.methodwriter = __mw;
		this.codewriter = __jcw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void atInstruction(int __code, int __pos)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void close()
		throws JITException
	{
		// Forward
		this.codewriter.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void jumpTargets(int[] __t)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void variableCounts(int __ms, int __ml)
	{
		throw new Error("TODO");
	}
}

