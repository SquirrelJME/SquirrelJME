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
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.hil.HighLevelBlock;
import net.multiphasicapps.squirreljme.jit.hil.HighLevelProgram;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigKey;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITProcessor;
import net.multiphasicapps.squirreljme.jit.verifier.VerificationChecks;

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
	
	/** The process doing the compilation. */
	protected final JITProcessor processor;
	
	/** The version number of the class. */
	protected final ClassVersion version;
	
	/** The class which this method is in. */
	protected final ClassName outerclass;
	
	/** The type used for this. */
	protected final FieldDescriptor thistype;
	
	/** The stack map table. */
	private volatile StackMapTable _smt;
	
	/** Variable state used for knowing where variables are. */
	private volatile VariableState _varstate;
	
	/** The next initialization key to use. */
	private volatile int _nextinitkey;
	
	/**
	 * Initializes the code decompiler.
	 *
	 * @param __f The flags for the method.
	 * @param __n The name of the method.
	 * @param __t The descriptor for the method.
	 * @param __in The input stream for the code's data.
	 * @param __pool The constant pool.
	 * @param __p The processor doing compilation.
	 * @param __ver The class version number.
	 * @param __cn The class this method is within.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/13
	 */
	public CodeDecompiler(MethodFlags __f, MethodName __n,
		MethodDescriptor __t, DataInputStream __in, Pool __pool,
		JITProcessor __p, ClassVersion __ver, ClassName __cn)
		throws NullPointerException
	{
		// Check
		if (__f == null || __t == null || __in == null || __pool == null ||
			__p == null || __ver == null || __cn == null ||
			__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.flags = __f;
		this.name = __n;
		this.type = __t;
		this.in = __in;
		this.pool = __pool;
		this.processor = __p;
		this.version = __ver;
		this.outerclass = __cn;
		
		// The this type is the type of the outer class
		this.thistype = __cn.asField();
	}
	
	/**
	 * Runs the decompiler and recompiles Java byte code to native machine
	 * code.
	 *
	 * @return The high level program for this code.
	 * @throws IOException On read errors.
	 * @throws JITException On malformed or illegal method code.
	 * @since 2017/07/13
	 */
	public HighLevelProgram run()
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
		
		// Load the stack map table
		StackMapTable smt = __locateStackMapTable(code);
		this._smt = smt;
		
		// Debug
		System.err.printf("DEBUG -- SMT: %s%n", smt);
		System.err.printf("DEBUG -- EHT: %s%n", eht);
		System.err.printf("DEBUG -- BBr: %s%n", code.basicBlocks());
		
		// Expand the byte code to a simpler format and unroll exceptions so
		// that they are exactly like normal code
		return __expand(code, smt, eht);
	}
	
	/**
	 * Expands the byte code.
	 *
	 * @param __code The method byte code.
	 * @param __smt The stack map table.
	 * @param __eht The exception handler table.
	 * @return The resulting program.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/08
	 */
	private HighLevelProgram __expand(ByteCode __code, StackMapTable __smt,
		ExceptionHandlerTable __eht)
		throws NullPointerException
	{
		// Check
		if (__code == null || __smt == null || __eht == null)
			throw new NullPointerException("NARG");
		
		// Initialize variable state
		VariableState varstate = new VariableState(__smt, __code.maxStack(),
			__code.maxLocals());
		this._varstate = varstate;
		
		// Initialize high level program which will be given instructions
		// that represent the program code
		HighLevelProgram rv = new HighLevelProgram();
		
		// Expand the entry point of the program
		__expandEntryPoint(rv);
		
		// If any address has exception handlers then each unique group
		// must be expanded so that if an exception does exist they can
		// have their tables expanded virtually.
		Set<ExceptionHandlerKey> xkeys = new LinkedHashSet<>();
		
		// After all of that, run through all byte code operations and
		// create an expanded byte code program contained within basic
		// blocks which are then used the processor. The expanded byte
		// code is used so that translators do not need to reimplement
		// support for the more complex byte code which can be prone to
		// errors.
		for (BasicBlock bb : __code.basicBlocks())
			__expandBasicBlock(bb, rv, xkeys);
			
		// Expand exception handlers if any were used
		for (ExceptionHandlerKey ek : xkeys)
			throw new todo.TODO();
		
		return rv;
	}
	
	/**
	 * Expands basic blocks which use standard instructions.
	 *
	 * @param __hlp The target high level program.
	 * @param __xk The exception handler keys.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/12
	 */
	private final void __expandBasicBlock(BasicBlock __bb,
		HighLevelProgram __hlp, Set<ExceptionHandlerKey> __xk)
		throws NullPointerException
	{
		// Check
		if (__bb == null || __hlp == null || __xk == null)
			throw new NullPointerException("NARG");
		
		// Go through instructions for the block and parse them
		HighLevelBlock block = __hlp.createBlock(__bb.jumpTarget());
		for (Instruction i : __bb)
		{
			// Debug
			System.err.printf("DEBUG -- Decode IN %s%n", i);
			
			// Depends on the operation
			int op;
			switch ((op = i.operation()))
			{
				case InstructionIndex.ALOAD_0:
				case InstructionIndex.ALOAD_1:
				case InstructionIndex.ALOAD_2:
				case InstructionIndex.ALOAD_3:
					__genLoadObject(block, op - InstructionIndex.ALOAD_0);
					break;
				
				case InstructionIndex.INVOKEINTERFACE:
				case InstructionIndex.INVOKESPECIAL:
				case InstructionIndex.INVOKESTATIC:
				case InstructionIndex.INVOKEVIRTUAL:
					__genInvokeMethod(block, i);
					break;
				
					// {@squirreljme.error JI2g The specified instruction
					// is not implemented. (The instruction)}
				default:
					throw new JITException(String.format("JI2g %s", i));
			}
		}
		
		// Finish basic block output
		throw new todo.TODO();
	}
	
	/**
	 * Expands the entry point of the method.
	 *
	 * @param __hlp The target high level program.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/12
	 */
	private final void __expandEntryPoint(HighLevelProgram __hlp)
		throws NullPointerException
	{
		// Check
		if (__hlp == null)
			throw new NullPointerException("NARG");
		
		// Enter the entry point
		HighLevelBlock block = __hlp.createBlock(
			SpecialBasicBlockKey.ENTRY_POINT);
		
		// Count objects which were passed to the method
		VariableState varstate = this._varstate;
		VariableTread locals = varstate.locals;
		for (int i = 0, n = varstate.maxLocals(); i < n; i++)
		{
			TypedVariable tv = locals.getTypedVariable(i);
			if (tv.isObject())
				block.appendCountReference(tv, true);
		}
		
		// If the method is synchronized, setup a special basic block
		// that acts as the method entry point which copies to a
		// special register and generates an enter of a monitor
		if (flags.isSynchronized())
		{
			// If this is an instance method then copy the this reference to
			// a copied this so that the monitor can be exited when this
			// method finishes
			if (flags.isInstance())
				block.appendCopy(locals.getTypedVariable(1),
					Variable.SYNCHRONIZED);
			
			// Otherwise need to get the pointer to the current class object
			// and lock on that instead
			else
				throw new todo.TODO();
			
			// Enter the monitor on the synchronized object
			throw new todo.TODO();
		}
		
		// Generate jump to the real method entry point
		block.appendUnconditionalJump(new JumpTarget(0));
	}
	
	/**
	 * This generates the required code for invoking methods.
	 *
	 * @param __bl The output block.
	 * @param __i The instruction for the method invocation.
	 * @throws JITException If the method could not be invoked.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	private void __genInvokeMethod(HighLevelBlock __bl, Instruction __i)
		throws JITException, NullPointerException
	{
		// Check
		if (__bl == null || __i == null)
			throw new NullPointerException("NARG");
		
		VariableState varstate = this._varstate;
		
		throw new todo.TODO();
	}
	
	/**
	 * Loads the specified object from the local variables onto the stack.
	 *
	 * @param __bl The output block.
	 * @param __src The source local to copy from.
	 * @throws JITException If the source cannot be copied because it is not
	 * an object.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/02
	 */
	private void __genLoadObject(HighLevelBlock __bl, int __src)
		throws JITException, NullPointerException
	{
		// Check
		if (__bl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.erorr JI2j Cannot load the specified variable
		// because it is not an object. (The source variable)}
		VariableState varstate = this._varstate;
		TypedVariable var = varstate.locals().getTypedVariable(__src);
		if (var == null || !var.isObject())
			throw new JITException(String.format("JI2j %s", var));
		
		// Push onto the stack
		Variable dv = varstate.stack().push(var);
		__bl.appendCopy(var, dv);
		
		// The object needs to be reference counted so it is not garbage
		// collected
		// Note that nothing needs to be counted down because the variable is
		// never replaced
		__bl.appendCountReference(varstate.getTypedVariable(dv), true);
	}
	
	/**
	 * Locates the stack map table within the code.
	 *
	 * @param __code The byte code for the method.
	 * @return The stack map table.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/08
	 */
	private StackMapTable __locateStackMapTable(ByteCode __code)
		throws IOException, NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Initialize the base stack map table
		StackMapTableBuilder smtbuilder = new StackMapTableBuilder(this.flags,
			this.name, this.type, this.outerclass, __code);
		
		// The only attribute which needs to be handled is the stack map
		// table which can either be in the new or old form depending on the
		// class version
		DataInputStream in = this.in;
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
		return smtbuilder.build();
	}
	
	/**
	 * Returns the next initialization key.
	 *
	 * @return The next initialization key.
	 * @since 2017/09/02
	 */
	private InitializationKey __nextInitKey()
	{
		return new InitializationKey(++this._nextinitkey);
	}
}

