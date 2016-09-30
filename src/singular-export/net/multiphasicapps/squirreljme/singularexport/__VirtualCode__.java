// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.singularexport;

import net.multiphasicapps.squirreljme.classformat.CodeDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.MethodLinkage;
import net.multiphasicapps.squirreljme.classformat.StackMapType;

/**
 * This handles the decoded byte code and initializes a wrapped representation
 * of it.
 *
 * @since 2016/09/30
 */
class __VirtualCode__
	implements CodeDescriptionStream
{
	/** The method to handle code for. */
	final __Method__ _method;
	
	/**
	 * Initializes the code handler.
	 *
	 * @param __m The method to place code for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	__VirtualCode__(__Method__ __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._method = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void atInstruction(int __code, int __pos)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void codeLength(int __n)
	{
		// Ignore because the code length may change
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void copy(StackMapType __type, CodeVariable __from,
		CodeVariable __to)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void invokeMethod(MethodLinkage __link, int __d,
		CodeVariable __rv, StackMapType __rvt, CodeVariable[] __cargs,
		StackMapType[] __targ)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void jumpTargets(int[] __t)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void variableCounts(int __ms, int __ml)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void variableTypes(byte[] __l, byte[] __s)
	{
		// Ignore
	}
}

