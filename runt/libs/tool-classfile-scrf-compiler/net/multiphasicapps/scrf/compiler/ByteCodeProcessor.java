// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.scrf.RegisterCode;

/**
 * This is the base class for byte code processors.
 *
 * @since 2019/01/21
 */
public abstract class ByteCodeProcessor
{
	/** The owning method processor. */
	protected final MethodProcessor methodprocessor;
	
	/** The input byte code. */
	protected final ByteCode input;
	
	/**
	 * Initializes the base processor.
	 *
	 * @param __mp The method processor.
	 * @param __bc The input byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/21
	 */
	public ByteCodeProcessor(MethodProcessor __mp, ByteCode __bc)
		throws NullPointerException
	{
		if (__mp == null || __bc == null)
			throw new NullPointerException("NARG");
		
		this.methodprocessor = __mp;
		this.input = __bc;
	}
	
	/**
	 * Processes the byte code and turns it into register code.
	 *
	 * @return The resulting register code.
	 * @throws ClassProcessException If the processing could not be done.
	 * @since 2019/01/21
	 */
	public abstract RegisterCode process()
		throws ClassProcessException;
	
	/**
	 * Processes the byte code and provides a result.
	 *
	 * @param __mp The method processor.
	 * @param __bc The input byte code.
	 * @throws ClassProcessException If the class could not be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/21
	 */
	public static final RegisterCode process(MethodProcessor __mp,
		ByteCode __bc)
		throws ClassProcessException, NullPointerException
	{
		if (__mp == null || __bc == null)
			throw new NullPointerException("NARG");
		
		return new PrimitiveByteCodeProcessor(__mp, __bc).process();
	}
}

