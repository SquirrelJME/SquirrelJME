// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionJumpTargets;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This represents the state of the byte code.
 *
 * @since 2019/04/06
 */
public class ByteCodeState
{
	/** The positions of all the stack information. */
	public final Map<Integer, JavaStackState> stacks =
		new LinkedHashMap<>();
		
	/** Addresses where the natural entry to the operation is poisoned. */
	public final Map<Integer, StateOperations> stackpoison =
		new LinkedHashMap<>();
	
	/** Stack collisions when a jump to the target does collide. */
	public final Map<Integer, JavaStackEnqueueList> stackcollides =
		new LinkedHashMap<>();
	
	/** Java instruction. */
	public Instruction instruction;
	
	/** Simplified instruction. */
	public SimplifiedJavaInstruction simplified;
	
	/** The resulting stack. */
	public JavaStackState stack;
	
	/** The result of the operation. */
	public JavaStackResult result;
	
	/** Can an exception handler be called? */
	public boolean canexception;
	
	/** Class name. */
	public ClassName classname;
	
	/** The method descriptor. */
	public MethodDescriptor methodtype;
	
	/** The method name. */
	public MethodName methodname;
	
	/** Exception ranges. */
	public ExceptionHandlerRanges exceptionranges;
	
	/** The current source line being processed. */
	public int line =
		-1;
	
	/** The last address processed. */
	public int lastaddr =
		-1;
	
	/** The current address being processed. */
	public int addr =
		-1;
	
	/** The address of the following instruction. */
	public int followaddr =
		-1;
	
	/** Jump targets for the instruction. */
	public InstructionJumpTargets jumptargets;
	
	/** Reverse jump targets for this instruction. */
	public InstructionJumpTargets reversejumptargets;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public String toString()
	{
		return String.format("{i=%s, si=%s, jss=%s, jsr=%s, ce=%b, " +
			"ln=%d, la=%d, a=%d, fa=%d, jt=%s, rjt=%s, cl=%s, mn=%s, mt=%s}",
			this.instruction,
			this.simplified,
			this.stack,
			this.result,
			this.canexception,
			this.line,
			this.lastaddr,
			this.addr,
			this.followaddr,
			this.jumptargets,
			this.reversejumptargets,
			this.classname,
			this.methodname,
			this.methodtype);
	}
}

