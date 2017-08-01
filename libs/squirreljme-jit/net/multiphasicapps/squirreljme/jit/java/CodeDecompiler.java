// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;
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
	/** The maximum number of bytes the byte code may be. */
	private static final int _MAX_CODE_LENGTH =
		65535;
	
	/** The method flags. */
	protected final MethodFlags flags;
	
	/** The name of this method. */
	protected final MethodName name;
	
	/** The descriptor. */
	protected final MethodDescriptor type;
	
	/** The input stream of the code attribute. */
	protected final DataInputStream in;
	
	/** The constant pool. */
	protected final Pool pool;
	
	/** The target linker state to get the byte code. */
	protected final LinkerState linkerstate;
	
	/** The version number of the class. */
	protected final ClassVersion version;
	
	/** The class which this method is in. */
	protected final ClassName outerclass;
	
	/**
	 * Initializes the code decompiler.
	 *
	 * @param __f The flags for the method.
	 * @param __n The name of the method.
	 * @param __t The descriptor for the method.
	 * @param __in The input stream for the code's data.
	 * @param __pool The constant pool.
	 * @param __linkerstate The target linker state to write the code into.
	 * @param __ver The class version number.
	 * @param __cn The class this method is within.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/13
	 */
	public CodeDecompiler(MethodFlags __f, MethodName __n, MethodDescriptor __t,
		DataInputStream __in, Pool __pool, LinkerState __linkerstate,
		ClassVersion __ver, ClassName __cn)
		throws NullPointerException
	{
		// Check
		if (__f == null || __t == null || __in == null || __pool == null ||
			__linkerstate == null || __ver == null || __cn == null ||
			__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.flags = __f;
		this.name = __n;
		this.type = __t;
		this.in = __in;
		this.pool = __pool;
		this.linkerstate = __linkerstate;
		this.version = __ver;
		this.outerclass = __cn;
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
		Pool pool = this.pool;
		
		// The number of variables allocated to the method
		int maxstack = in.readUnsignedShort(),
			maxlocals = in.readUnsignedShort();
		
		// {@squirreljme.error JI1d The specified code length is not valid.
		// (The code length)}
		int codelen = in.readInt();
		if (codelen <= 0 || codelen > _MAX_CODE_LENGTH)
			throw new JITException(String.format("JI1d %d", codelen));
		
		// Read code buffer
		byte[] rawcode = new byte[codelen];
		in.readFully(rawcode);
		
		// Read exception handler table
		ExceptionHandlerTable eht = new ExceptionHandlerTable(in, pool,
			codelen);
		
		// Setup the byte code
		ByteCode code = new ByteCode(maxstack, maxlocals, rawcode, eht, pool);
		
		// Debug the method
		System.err.println("DEBUG -- --------------------------");
		for (Iterator<Instruction> ii = code.instructionIterator();
			ii.hasNext();)
			System.err.printf("DEBUG -- %s%n", ii.next());
		
		// Initialize the base stack map table
		StackMapTableBuilder smtbuilder = new StackMapTableBuilder(this.flags,
			this.name, this.type, this.outerclass, code, pool, maxstack,
			maxlocals);
		
		// The only attribute which needs to be handled is the stack map
		// table which can either be in the new or old form depending on the
		// class version
		int na = in.readUnsignedShort();
		String[] attr = new String[1];
		ClassVersion version = this.version;
		String wantmap = (version.useStackMapTable() ? "StackMapTable" :
			"StackMap");
		for (int i = 0; i < na; i++)
			try (DataInputStream ai = ClassDecompiler.__nextAttribute(in,
				pool, attr))
			{
				// Only the stack map which is compatible with this class
				// version is to be used
				if (!wantmap.equals(attr[0]))
					continue;
				
				throw new todo.TODO();
			}
		
		// Build the stack map table, it is used for the basic register
		// initialization for arguments along with being used for verification
		// so that the code operates correctly
		StackMapTable smt = smtbuilder.build();
		
		// Debug
		System.err.printf("DEBUG -- SMT: %s%n", smt);
		
		// After all of that, run through all byte code operations and
		// create an expanded byte code program contained within basic blocks
		// which are then used the processor. The expanded byte code is used
		// so that translators do not need to reimplement support for the more
		// complex byte code which can be prone to errors.
		if (true)
			throw new todo.TODO();
		
		throw new todo.TODO();
	}
}

