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
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.classfile.StackMapTable;
import net.multiphasicapps.classfile.StackMapTableState;
import net.multiphasicapps.scrf.building.DynTableBuilder;
import net.multiphasicapps.scrf.building.ILCodeBuilder;
import net.multiphasicapps.scrf.CodeLocation;
import net.multiphasicapps.scrf.FixedMemoryLocation;
import net.multiphasicapps.scrf.ILCode;
import net.multiphasicapps.scrf.InvokeType;
import net.multiphasicapps.scrf.RegisterLocation;

/**
 * This is used to process a single method within a class file.
 *
 * @since 2019/02/16
 */
public final class MethodProcessor
{
	/** The owning processor. */
	protected final ClassFileProcessor classprocessor;
	
	/** Dynamic table builder. */
	protected final DynTableBuilder dyntable;
	
	/** The method to process. */
	protected final Method input;
	
	/** The input byte code. */
	protected final ByteCode bytecode;
	
	/** Where all the code will be placed. */
	protected final ILCodeBuilder codebuilder =
		new ILCodeBuilder();
	
	/** The stack map table. */
	protected final StackMapTable stackmap;
	
	/** The Java register/stack state. */
	protected final JavaState state;
	
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
		this.dyntable = __cp.dyntable;
		
		ByteCode bc;
		this.bytecode = (bc = __in.byteCode());
		this.stackmap = bc.stackMapTable();
		this.state = new JavaState(bc.maxLocals(), bc.maxStack());
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
		StackMapTable stackmap = this.stackmap;
		JavaState state = this.state;
		
		// Process each instruction
		for (Instruction inst : bytecode)
		{
			// Debug
			todo.DEBUG.note("Instruction: %s", inst);
			
			// If there is a defined stack map table state (this will be for
			// any kind of branch or exception handler), load that so it can
			// be worked from
			StackMapTableState smts = stackmap.get(inst.address());
			if (smts != null)
				state.fromState(smts);
			
			// Process instruction
			this.__processInstruction(inst);
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Processes the given instruction converting into intermediate code.
	 *
	 * @param __i The instruction to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/17
	 */
	private final void __processInstruction(Instruction __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Needed to store instruction data
		ILCodeBuilder ilcb = this.codebuilder;
		
		// Depends on the operation to process
		int op;
		switch ((op = __i.operation()))
		{
				// Load null reference
			case InstructionIndex.ACONST_NULL:
				this.__runConst(new JavaType(
					new FieldDescriptor("Ljava/lang/Object;")),
					new FixedMemoryLocation(0));
				break;
				
				// Load reference
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
				this.__runLoad(op - InstructionIndex.ALOAD_0);
				break;
				
				// Read static field
			case InstructionIndex.GETSTATIC:
				this.__runGetField(true, __i.<FieldReference>argument(0,
					FieldReference.class));
				break;
				
				// Invoke static method
			case InstructionIndex.INVOKESTATIC:
				this.__runInvoke(InvokeType.STATIC, __i.
					<MethodReference>argument(0, MethodReference.class));
				break;
			
				// {@squirreljme.error AV05 Unhandled instruction. (The
				// instruction)}
			default:
				throw new RuntimeException("AV05 " + __i);
		}
	}
	
	/**
	 * Pushes the given constant value to the stack.
	 *
	 * @param __t The type to push.
	 * @param __v The value to push.
	 * @return The code location.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/23
	 */
	private final CodeLocation __runConst(JavaType __t, Object __v)
		throws NullPointerException
	{
		if (__t == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Push type to the stack
		JavaStateResult sp = this.state.stackPush(__t);
		
		// Generate instruction
		return this.codebuilder.addConst(sp.register, __v);
	}
	
	/**
	 * Handles get of field.
	 *
	 * @param __s Is this static?
	 * @param __f Field reference.
	 * @return The location of added code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	private final CodeLocation __runGetField(boolean __s, FieldReference __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Always will be pushed
		RegisterLocation dest = this.state.stackPush(new JavaType(
			__f.memberType())).register;
		
		// Static field is an absolute address, while instance fields are
		// always relative
		if (__s)
			return this.codebuilder.addRead(dest,
				this.dyntable.addField(__s, __f));
		throw new todo.TODO();
	}
	
	/**
	 * Handles invocation of a method reference.
	 *
	 * @param __t The type of invocation to perform.
	 * @param __m The target method.
	 * @return The location of the added code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	private final CodeLocation __runInvoke(InvokeType __t, MethodReference __m)
		throws NullPointerException
	{
		if (__t == null || __m == null)
			throw new NullPointerException("NARG");
		
		JavaState state = this.state;
		
		// Pop all method arguments from the stack to determine what to pass to
		// the method argument size
		MethodDescriptor md = __m.memberType();
		int nargs = md.argumentCount() + (__t == InvokeType.STATIC ? 0 : 1);
		RegisterLocation[] args = new RegisterLocation[nargs];
		for (int i = nargs - 1; i >= 0; i--)
			args[i] = state.stackPop().register;
		
		// If there is a return value then push an extra thing to the stack
		RegisterLocation rv = (md.hasReturnValue() ?
			state.stackPush(new JavaType(md.returnValue())).register :
			new RegisterLocation(-1));
		
		// Invoke this method
		return this.codebuilder.addInvoke(this.dyntable.addInvoke(__t, __m),
			rv, args);
	}
	
	/**
	 * Loads from local variable.
	 *
	 * @param __from The variable to load from.
	 * @return The location of the added code.
	 * @since 2019/02/17
	 */
	private final CodeLocation __runLoad(int __from)
	{
		// Need to read local variable
		JavaState state = this.state;
		JavaStateResult lf = state.localGet(__from);
		
		// And additionally push it to the stack
		JavaStateResult sp = state.stackPush(lf.type);
		
		// Add copy operation
		return this.codebuilder.addCopy(sp.register, lf.register);
	}
}
