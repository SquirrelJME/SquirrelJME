// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.bin.Fragment;
import net.multiphasicapps.squirreljme.jit.bin.LinkerState;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class is used to decompile the Java byte code in the Code attribute and
 * place the modified byte codes into the target linker state.
 *
 * @since 2017/07/13
 */
public class CodeDecompiler
{
	/** The method flags. */
	protected final MethodFlags flags;
	
	/** The descriptor. */
	protected final MethodDescriptor type;
	
	/** The input stream of the code attribute. */
	protected final DataInputStream in;
	
	/** The constant pool. */
	protected final Pool pool;
	
	/** The target linker state to get the byte code. */
	protected final LinkerState linkerstate;
	
	/**
	 * Initializes the code decompiler.
	 *
	 * @param __f The flags for the method.
	 * @param __t The descriptor for the method.
	 * @param __in The input stream for the code's data.
	 * @param __pool The constant pool.
	 * @param __linkerstate The target linker state to write the code into.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/13
	 */
	public CodeDecompiler(MethodFlags __f, MethodDescriptor __t,
		DataInputStream __in, Pool __pool, LinkerState __linkerstate)
		throws NullPointerException
	{
		// Check
		if (__f == null || __t == null || __in == null || __pool == null ||
			__linkerstate == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.flags = __f;
		this.type = __t;
		this.in = __in;
		this.pool = __pool;
		this.linkerstate = __linkerstate;
	}
	
	/**
	 * Runs the decompiler and recompiles Java byte code to native machine
	 * code.
	 *
	 * @return The recompiled fragment containing the target machine code.
	 * @throws IOException On read errors.
	 * @throws JITException On malformed or illegal method code.
	 * @since 2017/07/13
	 */
	public Fragment run()
		throws IOException, JITException
	{
		DataInputStream in = this.in;
		
		// The number of variables allocated to the method
		int maxstack = in.readUnsignedShort(),
			maxlocals = in.readUnsignedShort();
		
		throw new todo.TODO();
	}
}

