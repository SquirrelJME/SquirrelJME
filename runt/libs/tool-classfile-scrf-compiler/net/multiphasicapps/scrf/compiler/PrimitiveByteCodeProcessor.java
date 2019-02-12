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
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.StackMapTable;
import net.multiphasicapps.classfile.StackMapTableState;
import net.multiphasicapps.scrf.MemoryType;
import net.multiphasicapps.scrf.RegisterCode;

/**
 * This is a primitive byte code processor which just performs direct
 * translation of stack based operations to a register based format but does
 * not perform any optimizations.
 *
 * @since 2019/01/21
 */
public class PrimitiveByteCodeProcessor
	extends ByteCodeProcessor
{
	/** The set of registers to work with. */
	protected final RegisterSet registers;
	
	/** The types and state of the stack will be initialized with this. */
	protected final StackMapTable smt;
	
	/** Explicit monitor register. */
	protected final WorkRegister rmonitor;
	
	/** The VTable of imports and exports. */
	protected final VTableBuilder vtable;
	
	/** Builder for register codes. */
	protected final RegisterCodeBuilder codebuilder;
	
	/**
	 * Initializes the primitive processor.
	 *
	 * @param __mp The method processor.
	 * @param __bc The input byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/21
	 */
	public PrimitiveByteCodeProcessor(MethodProcessor __mp, ByteCode __bc)
		throws NullPointerException
	{
		super(__mp, __bc);
		
		// Initialize the register set, it will store all the locals and the
		// stack variables. The virtual stack position is the end of the
		// local variables accordingly
		int ml = __bc.maxLocals(),
			ms = __bc.maxStack();
		RegisterSet rs = new RegisterSet(ml + ms, ml, ml + ms);
		this.registers = rs;
		
		// Get explicit special use registers
		this.rmonitor = rs.get(ml);
		
		// Cache the stack map table, it will be used much to initialize in
		// areas which registers are what
		this.smt = __bc.stackMapTable();
		
		// Initialize the code builder
		this.codebuilder = new RegisterCodeBuilder(__mp.vtable);
		
		// We need the vtable to refer to things
		this.vtable = __mp.vtable;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/21
	 */
	@Override
	public RegisterCode process()
		throws ClassProcessException
	{
		ByteCode input = this.input;
		StackMapTable smt = this.smt;
		RegisterSet registers = this.registers;
		RegisterCodeBuilder cb = this.codebuilder;
		VTableBuilder vtable = this.vtable;
		
		// If this is synchronized, we enter the monitor explicitly here
		boolean issync = this.methodprocessor.input.flags().isSynchronized();
		if (issync)
			throw new todo.TODO();
		
		// Process byte codes
		for (int vi = 0, vin = input.instructionCount(); vi < vin; vi++)
		{
			todo.DEBUG.note("@%-3d: %s", vi, input.getByIndex(vi));
			
			// The address of this instruction
			int iaddr = input.indexToAddress(vi);
			Instruction inst = input.getByIndex(vi);
			
			// Initialize the state of registers from the stack map state?
			StackMapTableState sms = smt.get(iaddr);
			if (sms != null)
				this.__setSMT(sms);
			
			// Depends on the instruction itself
			int vop;
			switch ((vop = PrimitiveByteCodeProcessor.__aliasOp(
				inst.operation())))
			{
					// Null reference
				case InstructionIndex.ACONST_NULL:
					cb.addConst(MemoryType.POINTER, registers.virtualPush(),
						0);
					break;
					
					// Load reference
				case InstructionIndex.ALOAD:
					cb.addCopy(MemoryType.POINTER,
						inst.<Integer>argument(0, Integer.class),
						registers.virtualPush());
					break;
					
					// Load reference (quick)
				case InstructionIndex.ALOAD_0:
				case InstructionIndex.ALOAD_1:
				case InstructionIndex.ALOAD_2:
				case InstructionIndex.ALOAD_3:
					cb.addCopy(MemoryType.POINTER,
						vop - InstructionIndex.ALOAD_0,
						registers.virtualPush());
					break;
					
					// Load static field
				case InstructionIndex.GETSTATIC:
					FieldReference sfr = inst.<FieldReference>argument(0,
						FieldReference.class);
					MemoryType sfmt = ProcessorUtils.memoryTypeOf(sfr);
					cb.addLoad(sfmt, vtable.addFieldReference(true, sfr),
						registers.virtualPush(sfr.memberType().stackWidth()));
					break;
					
					// Invoke special method (private/super/constructor)
				case InstructionIndex.INVOKESPECIAL:
					throw new todo.TODO();
				
					// Nop
				case InstructionIndex.NOP:
					cb.addNop();
					break;
					
					// Load from indexed local
				case JavaInstructionAlias.XLOAD:
					cb.addCopy(MemoryType.INTEGER,
						inst.<Integer>argument(0, Integer.class),
						registers.virtualPush());
					break;
					
					// Load from quick indexed local
				case JavaInstructionAlias.XLOAD_0:
				case JavaInstructionAlias.XLOAD_1:
				case JavaInstructionAlias.XLOAD_2:
				case JavaInstructionAlias.XLOAD_3:
					cb.addCopy(MemoryType.INTEGER,
						vop - JavaInstructionAlias.XLOAD_0,
						registers.virtualPush());
					break;
				
					// Unhandled, needs to be supported
				default:
					throw new todo.TODO(inst.toString());
			}
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the stack map table state.
	 *
	 * @param __s The state of the stack map.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/22
	 */
	private final void __setSMT(StackMapTableState __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		ByteCode input = this.input;
		RegisterSet registers = this.registers;
		
		// Copy local types
		int nl = input.maxLocals();
		for (int i = 0; i < nl; i++)
			registers.get(i).setJavaType(__s.getLocal(i).type());
		
		// Copy the stack
		int ns = __s.depth();
		registers.setJavaStackPos(nl + ns);
		for (int i = 0, o = nl; i < ns; i++, o++)
			registers.get(o).setJavaType(__s.getStack(i).type());
	}
	
	/**
	 * Aliases the operation so that certain instructions which are effectively
	 * the same are duplicated so that handlers need not be duplicated.
	 *
	 * @param __i The instruction to alias.
	 * @return The aliased instruction or {@code __i} if not aliased.
	 * @since 2019/01/23
	 */
	static final int __aliasOp(int __i)
	{
		switch (__i)
		{
			case InstructionIndex.ILOAD:
			case InstructionIndex.FLOAD:
				return JavaInstructionAlias.XLOAD;
			
			case InstructionIndex.ILOAD_0:
			case InstructionIndex.FLOAD_0:
				return JavaInstructionAlias.XLOAD_0;
			
			case InstructionIndex.ILOAD_1:
			case InstructionIndex.FLOAD_1:
				return JavaInstructionAlias.XLOAD_1;
			
			case InstructionIndex.ILOAD_2:
			case InstructionIndex.FLOAD_2:
				return JavaInstructionAlias.XLOAD_2;
			
			case InstructionIndex.ILOAD_3:
			case InstructionIndex.FLOAD_3:
				return JavaInstructionAlias.XLOAD_3;
			
				// Un-translated
			default:
				return __i;
		}
	}
}

