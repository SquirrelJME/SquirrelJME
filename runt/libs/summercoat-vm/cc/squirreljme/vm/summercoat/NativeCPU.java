// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.nncc.NativeInstruction;
import dev.shadowtail.classfile.nncc.NativeInstructionType;

/**
 * This represents a native CPU which may run within its own thread to
 * execute code that is running from within the virtual machine.
 *
 * @since 2019/04/21
 */
public final class NativeCPU
	implements Runnable
{
	/** The memory to read/write from. */
	protected final WritableMemory memory;
	
	/** The execution address. */
	int _pc;
	
	/**
	 * Initializes the native CPU.
	 *
	 * @param __mem The memory space.
	 * @param __pc The initial program counter address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public NativeCPU(WritableMemory __mem, int __pc)
		throws NullPointerException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
		
		this.memory = __mem;
		this._pc = __pc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void run()
	{
		WritableMemory memory = this.memory;
		int pc = this._pc;
		
		// Run execution in a loop
		try
		{
			// Handle instructions
			for (;;)
			{
				// Read operation
				int op = memory.memReadByte(pc);
				
				// Debug
				todo.DEBUG.note("@%08x -> (%02x) %s", pc,
					op, NativeInstruction.mnemonic(op));
				
				throw new todo.TODO();
			}
		}
		
		// Make sure the PC address is always stored
		finally
		{
			this._pc = pc;
		}
	}
}

