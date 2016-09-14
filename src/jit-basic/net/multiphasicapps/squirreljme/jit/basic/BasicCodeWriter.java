// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITCodeWriter;

/**
 * This is used to parse stack cached Java byte code to produce native machine
 * code from it.
 *
 * @since 2016/09/14
 */
public class BasicCodeWriter
	implements JITCodeWriter
{
	/** The owning namespace. */
	protected final BasicNamespaceWriter namespace;
	
	/** Where native machine code is to be placed. */
	protected final ExtendedDataOutputStream output;
	
	/** The constant pool to be utilized. */
	protected final BasicConstantPool pool;
	
	/** The current piece of code being written. */
	final __Code__ _code;
	
	/**
	 * Initializes the code writer.
	 *
	 * @param __ns The writer for namespaces.
	 * @param __o Where native machine code is to be placed.
	 * @param __c Method code table holder.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	BasicCodeWriter(BasicNamespaceWriter __ns, ExtendedDataOutputStream __o,
		__Code__ __c)
		throws NullPointerException
	{
		// Check
		if (__ns == null || __o == null || __c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __ns;
		this.output = __o;
		this._code = __c;
		this.pool = __ns._pool;
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
		throw new Error("TODO");
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
	public void primeArguments(boolean __eh, StackMapType[] __t)
		throws JITException, NullPointerException
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

