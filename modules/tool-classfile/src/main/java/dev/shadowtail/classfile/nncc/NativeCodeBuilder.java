// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.JavaStackResult;
import dev.shadowtail.classfile.xlate.MathType;
import dev.shadowtail.classfile.xlate.StackJavaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InstructionJumpTarget;

/**
 * This is used to build {@link NativeCode} and add instructions to it.
 *
 * @since 2019/03/16
 */
public final class NativeCodeBuilder
{
	/** Label positions. */
	final Map<NativeCodeLabel, Integer> _labels =
		new LinkedHashMap<>();
	
	/** Temporary instruction layout. */
	final Map<Integer, Point> _points =
		new LinkedHashMap<>();
	
	/** Next address to use. */
	int _nextaddr;
	
	/**
	 * Initializes the code builder at the default start address.
	 *
	 * @since 2019/03/22
	 */
	public NativeCodeBuilder()
	{
		this._nextaddr = 0;
	}
	
	/**
	 * Initializes the code builder at the given start address.
	 *
	 * @param __pc The address to start at.
	 * @since 2019/03/22
	 */
	public NativeCodeBuilder(int __pc)
	{
		this._nextaddr = __pc;
	}
	
	/**
	 * Adds a new instruction.
	 *
	 * @param __op The operation to add.
	 * @param __args The arguments to the operation.
	 * @return The resulting temporary instruction.
	 * @throws IllegalArgumentException If the argument count is incorrect.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/16
	 */
	public final NativeInstruction add(int __op, Object... __args)
		throws IllegalArgumentException, NullPointerException
	{
		// Needed for argument format check
		ArgumentFormat[] afmt = NativeInstruction.argumentFormat(__op);
		int fnar = afmt.length;
		
		// Build instruction
		NativeInstruction rv = new NativeInstruction(__op, __args);
		
		// {@squirreljme.error JC0q Operation has an incorrect number of
		// arguments. (The instruction)}
		if (fnar != __args.length)
			throw new IllegalArgumentException("JC0q " + rv);
		
		// Check format
		for (int i = 0; i < fnar; i++)
		{
			// Cannot be null
			Object o = __args[i];
			if (o == null)
				throw new NullPointerException("NARG");
			
			// Integer value?
			int oi = ((o instanceof Number) ? ((Number)o).intValue() : -1);
			
			// Make sure values are good
			switch (afmt[i])
			{
					// {@squirreljme.error JC0r Use of register which is out
					// of range of the maximum register count.
					// (The instruction)}
				case VUREG:
					if (oi < 0 || oi >= NativeCode.MAX_REGISTERS)
						throw new IllegalArgumentException("JC0r " + rv);
					break;
				
					// {@squirreljme.error JC0s Cannot jump to a non-label.
					// (The instruction)}
				case VJUMP:
					if (!(o instanceof NativeCodeLabel))
						throw new IllegalArgumentException("JC0s " + rv);
					break;
			}
		}
		
		// Create instruction
		int atdx = this._nextaddr++;
		
		// Debug
		if (__Debug__.ENABLED)
			todo.DEBUG.note("@%d -> %s %s", atdx,
				NativeInstruction.mnemonic(__op), Arrays.asList(__args));
			
		// Store all information
		this._points.put(atdx, new Point(rv));
		return rv;
	}
	
	/**
	 * Adds a copy from one register to another.
	 *
	 * @param __from The source.
	 * @param __to The destination.
	 * @return The resulting instruction.
	 * @since 2019/04/12
	 */
	public final NativeInstruction addCopy(int __from, int __to)
	{
		return this.add(NativeInstructionType.COPY,
			__from, __to);
	}
	
	/**
	 * Adds an integer comparison instruction.
	 *
	 * @param __ct The type of comparison to make
	 * @param __a The first register.
	 * @param __b The register to compare against.
	 * @param __jt The target of the jump.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addIfICmp(CompareType __ct, int __a,
		int __b, NativeCodeLabel __jt)
		throws NullPointerException
	{
		if (__ct == null || __jt == null)
			throw new NullPointerException("NARG");
		
		// Build operation
		return this.add(NativeInstructionType.IF_ICMP |
			__ct.ordinal(), __a, __b, __jt);
	}
	
	/**
	 * Adds a jump if the given register is not zero. No reference clears are
	 * performed by this call.
	 *
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/11
	 */
	public final NativeInstruction addIfNonZero(int __a, NativeCodeLabel __jt)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		return this.addIfICmp(CompareType.NOT_EQUALS, __a,
			NativeCode.ZERO_REGISTER, __jt);
	}
	
	/**
	 * Adds a jump if the given register is positive.
	 *
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/30
	 */
	public final NativeInstruction addIfPositive(int __a, NativeCodeLabel __jt)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		return this.addIfICmp(CompareType.GREATER_THAN, __a,
			NativeCode.ZERO_REGISTER, __jt);
	}
	
	/**
	 * Adds a jump if the given register is zero. No reference clears are
	 * performed by this call.
	 *
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addIfZero(int __a, NativeCodeLabel __jt)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		return this.addIfICmp(CompareType.EQUALS, __a,
			NativeCode.ZERO_REGISTER, __jt);
	}
	
	/**
	 * Adds a goto which goes to the following location.
	 *
	 * @param __jt The target of the jump.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addGoto(NativeCodeLabel __jt)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		return this.addIfICmp(CompareType.TRUE, NativeCode.ZERO_REGISTER,
			NativeCode.ZERO_REGISTER, __jt);
	}
	
	/**
	 * Adds a math via constant operation.
	 *
	 * @param __jt The Java type.
	 * @param __mf The math function.
	 * @param __a Register A.
	 * @param __b Constant B.
	 * @param __c The result.
	 * @return The resulting register.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/08
	 */
	public final NativeInstruction addMathConst(StackJavaType __jt,
		MathType __mf, int __a, Number __b, int __c)
		throws NullPointerException
	{
		if (__jt == null || __mf == null || __b == null)
			throw new NullPointerException("NARG");
		
		int op;
		switch (__jt)
		{
			case INTEGER:
				op = NativeInstructionType.MATH_CONST_INT;
				break;
				
				// {@squirreljme.error JC0t May only do math on integer.}
			case LONG:
			case FLOAT:
			case DOUBLE:
				throw new RuntimeException("JC0t");
			
			default:
				throw new todo.OOPS(__jt.name());
		}
		
		// Build operation
		int rop = op | __mf.ordinal();
		return this.add(rop, __a, __b, __c);
	}
	
	/**
	 * Adds a math via register operation.
	 *
	 * @param __jt The Java type.
	 * @param __mf The math function.
	 * @param __a Register A.
	 * @param __b Register B.
	 * @param __c The result.
	 * @return The resulting register.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/08
	 */
	public final NativeInstruction addMathReg(StackJavaType __jt,
		MathType __mf, int __a, int __b, int __c)
		throws NullPointerException
	{
		if (__jt == null || __mf == null)
			throw new NullPointerException("NARG");
		
		int op;
		switch (__jt)
		{
			case INTEGER:
				op = NativeInstructionType.MATH_REG_INT;
				break;
				
				// {@squirreljme.error JC0u May only do math on integer.}
			case LONG:
			case FLOAT:
			case DOUBLE:
				throw new RuntimeException("JC0u");
			
			default:
				throw new todo.OOPS(__jt.name());
		}
		
		// Build operation
		int rop = op | __mf.ordinal();
		return this.add(rop, __a, __b, __c);
	}
	
	/**
	 * Adds memory offset by constant.
	 *
	 * @param __dt The data type used.
	 * @param __load Is this a load operation?
	 * @param __v The value to store.
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The generated instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/12
	 */
	public final NativeInstruction addMemoryOffConst(DataType __dt,
		boolean __load, int __v, int __p, int __o)
		throws NullPointerException
	{
		if (__dt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC0v Cannot access wide memory.}
		if (__dt.isWide())
			throw new IllegalArgumentException("JC0v");
		
		return this.add(NativeInstructionType.MEMORY_OFF_ICONST |
			(__load ? 0b1000 : 0) | __dt.ordinal(), __v, __p, __o);
	}
	
	/**
	 * Adds memory offset by constant, using Java format.
	 *
	 * @param __dt The data type used.
	 * @param __load Is this a load operation?
	 * @param __v The value to store.
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The generated instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public final NativeInstruction addMemoryOffConstJava(DataType __dt,
		boolean __load, int __v, int __p, int __o)
		throws NullPointerException
	{
		if (__dt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC0w Cannot access wide memory.}
		if (__dt.isWide())
			throw new IllegalArgumentException("JC0w");
		
		return this.add(NativeInstructionType.MEMORY_OFF_ICONST_JAVA |
			(__load ? 0b1000 : 0) | __dt.ordinal(), __v, __p, __o);
	}
	
	/**
	 * Adds memory offset by register.
	 *
	 * @param __dt The data type used.
	 * @param __load Is this a load operation?
	 * @param __v The value to load/store.
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The generated instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/12
	 */
	public final NativeInstruction addMemoryOffReg(DataType __dt,
		boolean __load, int __v, int __p, int __o)
		throws NullPointerException
	{
		if (__dt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC0x Cannot access wide memory.}
		if (__dt.isWide())
			throw new IllegalArgumentException("JC0x");
		
		// Generate
		return this.add(NativeInstructionType.MEMORY_OFF_REG |
			(__load ? 0b1000 : 0) | __dt.ordinal(), __v, __p, __o);
	}
	
	/**
	 * Adds memory offset by register, using Java format.
	 *
	 * @param __dt The data type used.
	 * @param __load Is this a load operation?
	 * @param __v The value to store.
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The generated instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public final NativeInstruction addMemoryOffRegJava(DataType __dt,
		boolean __load, int __v, int __p, int __o)
		throws NullPointerException
	{
		if (__dt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC0y Cannot access wide memory.}
		if (__dt.isWide())
			throw new IllegalArgumentException("JC0y");
		
		// Generate
		return this.add(NativeInstructionType.MEMORY_OFF_REG_JAVA |
			(__load ? 0b1000 : 0) | __dt.ordinal(), __v, __p, __o);
	}
	
	/**
	 * Builds the register code, all references to other portions in the
	 * code itself are resolved.
	 *
	 * @return The built register code.
	 * @since 2019/03/22
	 */
	public final NativeCode build()
	{
		// Labels which point to addresses
		Map<NativeCodeLabel, Integer> labels = this._labels;
		
		// If there are any jump points which refer to the instruction index
		// directly following it, then remove the jump.
		// Also possibly perform other modifications
		List<Point> in = new ArrayList<>(this._points.values());
		for (int i = in.size() - 1; i >= 0; i--)
		{
			// Get the instruction and its various properties
			Point point = in.get(i);
			NativeInstruction ri = point.instruction;
			int rie = ri.encoding();
			
			NativeCodeLabel jt = null;
			
			// Depends on the encoding
			switch (rie)
			{
				case NativeInstructionType.IF_ICMP:
					jt = (NativeCodeLabel)ri.argument(2);
					break;
					
					// Not a jump
				default:
					continue;
			}
			
			// Check if this points to the instruction directly following this.
			boolean ptonext = (jt != null && (i + 1) == labels.get(jt));
			
			// If it does point to the next instruction, we either delete it
			// or replace the instruction with another depending on ref_clear
			// and such
			if (ptonext)
			{
				// Remove this instruction, it is pointless
				in.remove(i);
				
				// Move all of the label values down
				for (Map.Entry<NativeCodeLabel, Integer> e :
					labels.entrySet())
				{
					int val = e.getValue();
					if (val > i)
						e.setValue(val - 1);
				}
			}
		}
		
		// Resulting tables of instructions
		int n = in.size();
		NativeInstruction[] tabni = new NativeInstruction[n];
		
		// Go through input instructions and map them to real instructions
		for (int i = 0; i < n; i++)
		{
			// Get input point
			Point point = in.get(i);
			
			// The instruction is re-processed potentially
			NativeInstruction inst = point.instruction;
			
			// Used to detect if the instruction actually changed
			boolean didchange = false;
			
			// Arguments may be re-translated if they contain jumps
			Object[] args = inst.arguments();
			for (int j = 0, jn = args.length; j < jn; j++)
			{
				Object a = args[j];
				
				// Map any labels to indexes
				if ((didchange |= (a instanceof NativeCodeLabel)))
				{
					// {@squirreljme.error JC0z The specified label was
					// never defined. (The label)}
					Integer rlp = labels.get((NativeCodeLabel)a);
					if (rlp == null)
						throw new IllegalArgumentException("JC0z " + a);
					
					args[j] = new InstructionJumpTarget(rlp);
				}
			}
			
			// If the instruction changed, use the new one
			tabni[i] = (didchange ? new NativeInstruction(inst.op, args) :
				inst);
		}
		
		// Build
		return new NativeCode(tabni);
	}
	
	/**
	 * Adds a label at the current position.
	 *
	 * @param __lo The locality.
	 * @param __dx The index.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final NativeCodeLabel label(String __lo, int __dx)
	{
		return this.label(new NativeCodeLabel(__lo, __dx), this._nextaddr);
	}
	
	/**
	 * Adds a label.
	 *
	 * @param __lo The locality.
	 * @param __dx The index.
	 * @param __pc The address to target.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final NativeCodeLabel label(String __lo, int __dx, int __pc)
	{
		return this.label(new NativeCodeLabel(__lo, __dx), __pc);
	}
	
	/**
	 * Adds a label at the current position.
	 *
	 * @param __l The label to add.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final NativeCodeLabel label(NativeCodeLabel __l)
	{
		return this.label(__l, this._nextaddr);
	}
	
	/**
	 * Adds a label.
	 *
	 * @param __pc The address to target.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final NativeCodeLabel label(NativeCodeLabel __l, int __pc)
	{
		// Debug
		if (__Debug__.ENABLED)
			todo.DEBUG.note("Label %s -> @%d", __l, __pc);
		
		// Add
		this._labels.put(__l, __pc);
		return __l;
	}
	
	/**
	 * Returns the target of the specified label.
	 *
	 * @param __n The label name.
	 * @param __dx The label index.
	 * @return The index of the instruction or {@code -1} if the label is not
	 * valid or the position is not yet known.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/04
	 */
	public final int labelTarget(String __n, int __dx)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return this.labelTarget(new NativeCodeLabel(__n, __dx));
	}
	
	/**
	 * Returns the target of the specified label.
	 *
	 * @param __l The label to get the target of.
	 * @return The index of the instruction or {@code -1} if the label is not
	 * valid or the position is not yet known.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public final int labelTarget(NativeCodeLabel __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		Integer rv = this._labels.get(__l);
		return (rv == null ? -1 : rv);
	}
	
	/**
	 * This stores the information for a single point in the native code.
	 *
	 * @since 2019/04/26
	 */
	public static final class Point
	{
		/** The instruction used. */
		public final NativeInstruction instruction;
		
		/**
		 * Initializes the instruction point.
		 *
		 * @param __i The instruction.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/26
		 */
		public Point(NativeInstruction __i)
			throws NullPointerException
		{
			if (__i == null)
				throw new NullPointerException("NARG");
			
			this.instruction = __i;
		}
	}
}

