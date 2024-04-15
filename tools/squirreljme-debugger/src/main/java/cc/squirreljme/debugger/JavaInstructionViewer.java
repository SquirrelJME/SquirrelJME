// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionMnemonics;

/**
 * A standard instruction viewer using SquirrelJME's {@link Instruction}.
 *
 * @since 2024/01/21
 */
public class JavaInstructionViewer
	implements InstructionViewer
{
	/** The instruction this wraps. */
	protected final Instruction instruction;
	
	/**
	 * Initializes the viewer.
	 *
	 * @param __instruction The instruction to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public JavaInstructionViewer(Instruction __instruction)
		throws NullPointerException
	{
		if (__instruction == null)
			throw new NullPointerException("NARG");
		
		this.instruction = __instruction;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/21
	 */
	@Override
	public int address()
	{
		return this.instruction.address();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/26
	 */
	@Override
	public Object[] arguments()
	{
		return this.instruction.arguments();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public int length()
	{
		return this.instruction.length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/21
	 */
	@Override
	public String mnemonic()
	{
		return this.instruction.mnemonic();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public int mnemonicId()
	{
		return this.instruction.operation();
	}
}
