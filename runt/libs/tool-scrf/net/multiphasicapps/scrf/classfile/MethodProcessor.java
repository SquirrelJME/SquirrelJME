// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.classfile;

import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.scrf.ILCode;

/**
 * This is used to process a single method within a class file.
 *
 * @since 2019/02/16
 */
public final class MethodProcessor
{
	/** The owning processor. */
	protected final ClassFileProcessor classprocessor;
	
	/** The method to process. */
	protected final Method input;
	
	/** The input byte code. */
	protected final ByteCode bytecode;
	
	/**
	 * Initializes the method processor.
	 *
	 * @param __cp The class processor.
	 * @param __in The input method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public MethodProcessor(ClassFileProcessor __cp, Method __in)
		throws NullPointerException
	{
		if (__cp == null || __in == null)
			throw new NullPointerException("NARG");
		
		this.classprocessor = __cp;
		this.input = __in;
		this.bytecode = __in.byteCode();
	}
	
	/**
	 * Processes the given method.
	 *
	 * @return The resulting method code.
	 * @since 2019/02/16
	 */
	public final ILCode process()
	{
		ByteCode bytecode = this.bytecode;
		
		// Process each instruction
		for (Instruction inst : bytecode)
		{
			// Debug
			todo.DEBUG.note("Instruction: %s", inst);
			
			switch (inst.operation())
			{
					// {@squirreljme.error AV05 Unhandled instruction. (The
					// instruction)}
				default:
					throw new RuntimeException("AV05 " + inst);
			}
		}
		
		throw new todo.TODO();
	}
}

