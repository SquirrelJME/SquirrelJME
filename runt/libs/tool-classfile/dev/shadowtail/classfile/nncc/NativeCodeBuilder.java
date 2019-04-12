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
	/** Temporary instruction layout. */
	final Map<Integer, NativeInstruction> _instructions =
		new LinkedHashMap<>();
	
	/** Label positions. */
	final Map<NativeCodeLabel, Integer> _labels =
		new LinkedHashMap<>();
	
	/** Current line number table. */
	final List<Integer> _lines =
		new ArrayList<>();
	
	/** Next address to use. */
	int _nextaddr;
	
	/** Current line address. */
	int _cursrcline =
		-1;
	
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
		// {@squirreljme.error JC2q Operation has an incorrect number of
		// arguments. (The mnemonic; The input arguments)}
		if (NativeInstruction.argumentCount(__op) != __args.length)
			throw new IllegalArgumentException("JC2q " +
				NativeInstruction.mnemonic(__op) + " " + __args.length);
		
		for (Object o : __args)
			if (o == null)
				throw new NullPointerException("NARG");
		
		// Create instruction
		int atdx = this._nextaddr++;
		NativeInstruction rv = new NativeInstruction(__op, __args);
		
		// Debug
		todo.DEBUG.note("@%d -> %s %s", atdx,
			NativeInstruction.mnemonic(__op), Arrays.asList(__args));
		
		// Store and return the instruction, it will have the address
		this._instructions.put(atdx, rv);
		this._lines.add(this._cursrcline);
		return rv;
	}
	
	/**
	 * Adds array access.
	 *
	 * @param __dt The data type.
	 * @param __ld Is a load being done?
	 * @param __v The value to read/write.
	 * @param __in The array instance.
	 * @param __dx The array index.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/12
	 */
	public final NativeInstruction addArrayAccess(DataType __dt, boolean __ld,
		int __v, int __in, int __dx)
		throws NullPointerException
	{
		if (__dt == null)
			throw new NullPointerException("NARG");
		
		return this.add(NativeInstructionType.ARRAY_ACCESS | __dt.ordinal() |
			(__ld ? 0b1000 : 0), __v, __in, __dx);
	}
	
	/**
	 * Adds conversion from one type to another.
	 *
	 * @param __fromt The source type.
	 * @param __from The source register.
	 * @param __tot The target type.
	 * @param __to The target register.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/12
	 */
	public final NativeInstruction addConversion(StackJavaType __fromt,
		int __from, StackJavaType __tot, int __to)
		throws NullPointerException
	{
		if (__fromt == null || __tot == null)
			throw new NullPointerException("NARG");
		
		return this.add(NativeInstructionType.CONVERSION |
			(__fromt.ordinal() << 2) | (__tot.ordinal()), __from, __to);
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
		return this.addConversion(StackJavaType.INTEGER, __from,
			StackJavaType.INTEGER, __to);
	}
	
	/**
	 * Adds a copy from one register to another, using wide values.
	 *
	 * @param __from The source.
	 * @param __to The destination.
	 * @return The resulting instruction.
	 * @since 2019/04/12
	 */
	public final NativeInstruction addCopyWide(int __from, int __to)
	{
		return this.addConversion(StackJavaType.LONG, __from,
			StackJavaType.LONG, __to);
	}
	
	/**
	 * Adds jump if the given register is an instance of the given class.
	 *
	 * @param __cn The class name to check.
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addIfClass(ClassName __cn, int __a,
		NativeCodeLabel __jt)
		throws NullPointerException
	{
		return this.addIfClass(__cn, __a, __jt, false);
	}
	
	/**
	 * Adds jump if the given register is an instance of the given class.
	 *
	 * @param __cn The class name to check.
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @param __rc Reference clear.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addIfClass(ClassName __cn, int __a,
		NativeCodeLabel __jt, boolean __rc)
		throws NullPointerException
	{
		if (__cn == null || __jt == null)
			throw new NullPointerException("NARG");
		
		return this.add((__rc ? NativeInstructionType.IFCLASS_REF_CLEAR :
			NativeInstructionType.IFCLASS), __cn, __a, __jt);
	}
	
	/**
	 * Adds an integer comparison instruction. No references will ever be
	 * cleared if the comparison succeeds.
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
		return this.addIfICmp(__ct, __a, __b, __jt, false);
	}
	
	/**
	 * Adds an integer comparison instruction.
	 *
	 * @param __ct The type of comparison to make
	 * @param __a The first register.
	 * @param __b The register to compare against.
	 * @param __jt The target of the jump.
	 * @param __rc If true then a {@code REF_CLEAR} is performed if the jump
	 * succeeds.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addIfICmp(CompareType __ct, int __a,
		int __b, NativeCodeLabel __jt, boolean __rc)
		throws NullPointerException
	{
		if (__ct == null || __jt == null)
			throw new NullPointerException("NARG");
			
		// Build operation
		return this.add(NativeInstructionType.IF_ICMP |
			(__rc ? 0b1000 : 0) | __ct.ordinal(), __a, __b, __jt);
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
		return this.addIfNonZero(__a, __jt, false);
	}
	
	/**
	 * Adds a jump if the given register is not zero.
	 *
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @param __rc If the condition succeeds, do a {@code REF_CLEAR}.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/11
	 */
	public final NativeInstruction addIfNonZero(int __a, NativeCodeLabel __jt,
		boolean __rc)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		return this.addIfICmp(CompareType.NOT_EQUALS, __a,
			NativeCode.ZERO_REGISTER, __jt, __rc);
	}
	
	/**
	 * Adds jump if the given register is not an instance of the given class.
	 *
	 * @param __cn The class name to check.
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addIfNotClass(ClassName __cn, int __a,
		NativeCodeLabel __jt)
		throws NullPointerException
	{
		return this.addIfNotClass(__cn, __a, __jt, false);
	}
	
	/**
	 * Adds jump if the given register is not an instance of the given class.
	 *
	 * @param __cn The class name to check.
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @param __rc Reference clear.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addIfNotClass(ClassName __cn, int __a,
		NativeCodeLabel __jt, boolean __rc)
		throws NullPointerException
	{
		if (__cn == null || __jt == null)
			throw new NullPointerException("NARG");
		
		return this.add((__rc ? NativeInstructionType.IFNOTCLASS_REF_CLEAR :
			NativeInstructionType.IFNOTCLASS), __cn, __a, __jt);
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
		return this.addIfZero(__a, __jt, false);
	}
	
	/**
	 * Adds a jump if the given register is zero.
	 *
	 * @param __a The register to check.
	 * @param __jt The target of the jump.
	 * @param __rc If the condition succeeds, do a {@code REF_CLEAR}.
	 * @return The resulting instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	public final NativeInstruction addIfZero(int __a, NativeCodeLabel __jt,
		boolean __rc)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		return this.addIfICmp(CompareType.EQUALS, __a,
			NativeCode.ZERO_REGISTER, __jt, __rc);
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
				
			case LONG:
				op = NativeInstructionType.MATH_CONST_LONG;
				break;
				
			case FLOAT:
				op = NativeInstructionType.MATH_CONST_FLOAT;
				break;
				
			case DOUBLE:
				op = NativeInstructionType.MATH_CONST_DOUBLE;
				break;
			
			default:
				throw new todo.OOPS(__jt.name());
		}
		
		// Build operation
		return this.add(op | __mf.ordinal(), __a, __b, __c);
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
				
			case LONG:
				op = NativeInstructionType.MATH_REG_LONG;
				break;
				
			case FLOAT:
				op = NativeInstructionType.MATH_REG_FLOAT;
				break;
				
			case DOUBLE:
				op = NativeInstructionType.MATH_REG_DOUBLE;
				break;
			
			default:
				throw new todo.OOPS(__jt.name());
		}
		
		// Build operation
		return this.add(op | __mf.ordinal(), __a, __b, __c);
	}
	
	/**
	 * Adds memory offset by register.
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
	public final NativeInstruction addMemoryOffReg(DataType __dt,
		boolean __load, int __v, int __p, int __o)
		throws NullPointerException
	{
		if (__dt == null)
			throw new NullPointerException("NARG");
		
		// Generate
		return this.add(NativeInstructionType.MEMORY_OFF_REG |
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
		// Working area for arguments
		List<Object> workargs = new ArrayList<>();

		// Labels which point to addresses
		Map<NativeCodeLabel, Integer> labels = this._labels;
		
		// If there are any jump points which refer to the instruction index
		// directly following it, then remove the jump.
		// If these jumps are ref clearing ones, then replace with a refclear
		// Also possibly perform other modifications
		List<NativeInstruction> in = new ArrayList<>(
			this._instructions.values());
		List<Integer> lines = new ArrayList<>(this._lines);
		for (int i = in.size() - 1; i >= 0; i--)
		{
			// Get the instruction and its various properties
			NativeInstruction ri = in.get(i);
			int rie = ri.encoding();
			
			NativeCodeLabel jt = null;
			boolean refclear = false;
			
			// Depends on the encoding
			switch (rie)
			{
				case NativeInstructionType.IF_ICMP:
					jt = (NativeCodeLabel)ri.argument(2);
					refclear = ((ri.operation() & 0b1000) != 0);
					break;
					
				case NativeInstructionType.IFNOTCLASS:
				case NativeInstructionType.IFCLASS:
					jt = (NativeCodeLabel)ri.argument(2);
					break;
					
				case NativeInstructionType.IFNOTCLASS_REF_CLEAR:
				case NativeInstructionType.IFCLASS_REF_CLEAR:
					jt = (NativeCodeLabel)ri.argument(2);
					refclear = true;
					break;
				
					// Not a jump
				default:
					continue;
			}
			
			// Check if this points to the instruction directly following
			// this. Do not consider refclears since with that logic it is
			// only cleared when the condition is met
			boolean ptonext = (!refclear && jt != null &&
				(i + 1) == labels.get(jt));
			
			// If it does point to the next instruction, we either delete it
			// or replace the instruction with another depending on ref_clear
			// and such
			if (ptonext)
			{
				// Replace with a ref_clear
				if (refclear)
					in.set(i, new NativeInstruction(
						NativeInstructionType.REF_CLEAR));
				
				// Delete and move all down
				else
				{
					// Remove this instruction, it is pointless
					in.remove(i);
					lines.remove(i);
					
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
		}
		
		// Output instructions
		List<NativeInstruction> out = new ArrayList<>();
		
		// Go through input instructions and map them to real instructions
		for (NativeInstruction i : in)
		{
			// Fill in working arguments, with translated labels
			workargs.clear();
			for (Object a : i._args)
			{
				// Map any labels to indexes
				if (a instanceof NativeCodeLabel)
				{
					// {@squirreljme.error JC35 The specified label was
					// never defined. (The label)}
					Integer rlp = labels.get((NativeCodeLabel)a);
					if (rlp == null)
						throw new IllegalArgumentException("JC35 " + a);
					
					a = new InstructionJumpTarget(rlp);
				}
				
				workargs.add(a);
			}
			
			// Build instruction
			out.add(new NativeInstruction(i.op, workargs));
		}
		
		// Translate line number table
		int no = Math.min(out.size(), lines.size());
		short[] xlines = new short[no];
		for (int i = 0; i < no; i++)
			xlines[i] = lines.get(i).shortValue();
		
		// Debug
		for (int i = 0, n = out.size(); i < n; i++)
			todo.DEBUG.note("@%-2d L%d:%s", i, xlines[i] & 0xFFFF,
				out.get(i));
		
		// Build
		return new NativeCode(out, xlines);
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
	 * @param __pc The address to target.
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
	 * Sets the current source line.
	 *
	 * @param __l The line to set.
	 * @since 2019/03/23
	 */
	public final void setSourceLine(int __l)
	{
		this._cursrcline = __l;
	}
}

