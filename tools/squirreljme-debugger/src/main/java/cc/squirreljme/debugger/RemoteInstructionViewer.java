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
import net.multiphasicapps.classfile.Pool;

/**
 * Views remote instructions.
 *
 * @since 2024/01/22
 */
public class RemoteInstructionViewer
	implements InstructionViewer
{
	/** The address of this instruction. */
	protected final int address;
	
	/** The byte code. */
	protected final byte[] byteCode;
	
	/** The constant pool, may be {@code null}. */
	protected final Pool pool;
	
	/**
	 * Initializes the instruction viewer.
	 *
	 * @param __pool The constant pool.
	 * @param __byteCode The method byte code.
	 * @param __address The address of this instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/23
	 */
	public RemoteInstructionViewer(Pool __pool, byte[] __byteCode,
		int __address)
		throws NullPointerException
	{
		if (__byteCode == null)
			throw new NullPointerException("NARG");
		
		this.pool = __pool;
		this.byteCode = __byteCode;
		this.address = __address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public int address()
	{
		return this.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public int length()
	{
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public String mnemonic()
	{
		return "nop";
	}
}
