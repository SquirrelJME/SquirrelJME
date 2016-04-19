// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import net.multiphasicapps.classfile.CFClassName;
import net.multiphasicapps.classfile.CFConstantValue;
import net.multiphasicapps.classfile.CFFieldReference;
import net.multiphasicapps.classfile.CFLDCLoadable;
import net.multiphasicapps.classfile.CFMethodReference;

/**
 * This determines the arguments which an operation uses.
 *
 * @since 2016/04/15
 */
final class __OpArguments__
{
	/**
	 * Do not call.
	 *
	 * @since 2016/04/15
	 */
	private __OpArguments__()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Reads the arguments for the given operation.
	 *
	 * @return __op The operation to read arguments for.
	 * @return The arguments of the operation or {@code null} if there are
	 * none.
	 * @since 2016/04/15
	 */
	static Object[] __getArguments(CPOp __op)
	{
		// Get the opcode
		int opcode = __op.instructionCode();
		
		// Single signed byte
		if (opcode == 16)
			return new Object[]
				{
					Integer.valueOf(__op.__readSByte(1))
				};
		
		// Single unsigned byte
		else if ((opcode >= 21 && opcode <= 25) ||
			(opcode >= 54 && opcode <= 58) ||
			(opcode == 188))
			return new Object[]
				{
					Integer.valueOf(__op.__readUByte(1))
				};
		
		// Single signed short
		else if ((opcode == 17) ||
			(opcode >= 153 && opcode <= 167) ||
			(opcode >= 198 && opcode <= 199))
			return new Object[]
				{
					Integer.valueOf(__op.__readSShort(1))
				};
		
		// Single unsigned short
		else if ((opcode >= 50197 && opcode <= 50201) ||
			(opcode >= 50230 && opcode <= 50234))
			return new Object[]
				{
					Integer.valueOf(__op.__readUShort(1))
				};
		
		// Single signed int
		else if (opcode == 20)
			return new Object[]
				{
					Integer.valueOf(__op.__readSInt(1))
				};
		
		// Increment/Decrement local
		else if (opcode == 132)
			return new Object[]
				{
					Integer.valueOf(__op.__readUByte(1)),
					Integer.valueOf(__op.__readSByte(2))
				};
		
		// Wide Increment/Decrement local
		else if (opcode == 50308)
			return new Object[]
				{
					Integer.valueOf(__op.__readUShort(1)),
					Integer.valueOf(__op.__readSShort(3))
				};
		
		// Narrow Pool constant (narrow)
		else if (opcode == 18)
			return new Object[]
				{
					__op.program().constantPool().<CFLDCLoadable.Narrow>getAs(
						__op.__readUByte(1), CFLDCLoadable.Narrow.class).
						getValue()
				};
		
		// Wide pool constant (narrow)
		else if (opcode == 19)
			return new Object[]
				{
					__op.program().constantPool().<CFLDCLoadable.Narrow>getAs(
						__op.__readUShort(1), CFLDCLoadable.Narrow.class).
						getValue()
				};
		
		// Wide pool constant (wide)
		else if (opcode == 20)
			return new Object[]
				{
					__op.program().constantPool().<CFLDCLoadable.Wide>getAs(
						__op.__readUShort(1), CFLDCLoadable.Wide.class).
						getValue()
				};
		
		// Single field reference
		else if ((opcode >= 178 && opcode <= 179) ||
			(opcode >= 180 && opcode <= 181))
			return new Object[]
				{
					__op.program().constantPool().<CFFieldReference>getAs(
						__op.__readUShort(1), CFFieldReference.class)
				};
		
		// Single method reference
		else if (opcode >= 182 && opcode <= 186)
			return new Object[]
				{
					__op.program().constantPool().<CFMethodReference>getAs(
						__op.__readUShort(1), CFMethodReference.class)
				};
		
		// Single class
		else if ((opcode == 187) ||
			(opcode == 189) ||
			(opcode >= 192 && opcode <= 193))
			return new Object[]
				{
					__op.program().constantPool().<CFClassName>getAs(
						__op.__readUShort(1), CFClassName.class).symbol()
				};
		
		// New multi-dimentional array
		else if (opcode == 197)
			return new Object[]
				{
					__op.program().constantPool().<CFClassName>getAs(
						__op.__readUShort(1), CFClassName.class).symbol(),
					__op.__readUByte(2)
				};
		
		// Table switch
		else if (opcode == 170)
			throw new Error("TODO");
		
		// Lookup switch
		else if (opcode == 171)
			throw new Error("TODO");
		
		// Assume no arguments
		else
			return null;
	}
}

