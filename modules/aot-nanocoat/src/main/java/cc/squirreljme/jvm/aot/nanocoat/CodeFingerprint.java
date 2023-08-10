// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.lang.ArrayUtils;
import cc.squirreljme.runtime.cldc.util.IntegerIntegerArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.Pool;

/**
 * Represents the fingerprint of a method to determine whether two methods
 * have the same byte code, this is not likely to occur unless within
 * trivial methods.
 *
 * @since 2023/08/09
 */
public final class CodeFingerprint
{
	/** We only care about the given set of tags, the rest are irrelevant. */
	private static final int _POOL_TAG_BITS =
		(1 << Pool.TAG_INTEGER) |
		(1 << Pool.TAG_FLOAT) |
		(1 << Pool.TAG_LONG) |
		(1 << Pool.TAG_DOUBLE) |
		(1 << Pool.TAG_CLASS) |
		(1 << Pool.TAG_STRING) |
		(1 << Pool.TAG_FIELDREF) |
		(1 << Pool.TAG_METHODREF);
	
	/** The fingerprint array. */
	private final int[] _fingerprint;
	
	/** The calculated hash code, since this can be heavy. */
	volatile int _hashCode;
	
	/**
	 * Determines the code fingerprint for the given method. 
	 *
	 * @param __code The method used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public CodeFingerprint(ByteCode __code)
		throws NullPointerException
	{
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// The fingerprint consists of all the arrays
		List<int[]> fingerprint = new ArrayList<>();
		
		// And the constant pool
		Pool pool = __code.pool();
		
		// Process each tag within the pool to determine if we care about it
		int[] poolTags = pool.tags();
		int poolSize = poolTags.length;
		for (int i = 0; i < poolSize; i++)
		{
			int tag = poolTags[i];
			
			// Alias interface method references to methods, in the end they
			// are the same and do not mean much, the instruction type when
			// the code is processed will determine what is used
			if (tag == Pool.TAG_INTERFACEMETHODREF)
				tag = Pool.TAG_METHODREF;
			
			// Alias simple number types which cannot throw exceptions on
			// their resolution
			else if (tag == Pool.TAG_INTEGER ||
				tag == Pool.TAG_FLOAT ||
				tag == Pool.TAG_LONG ||
				tag == Pool.TAG_DOUBLE)
				tag = -1;
			
			// Alias string and classes to simple objects where they can
			// throw an exception on their resolution
			else if (tag == Pool.TAG_CLASS ||
				tag == Pool.TAG_STRING)
				tag = -2;
			
			// Clear the tag if it is not one we care about
			if (tag > 0 && (CodeFingerprint._POOL_TAG_BITS & (1 << tag)) == 0)
				poolTags[i] = 0;
			else
				poolTags[i] = tag;
		}
		
		// We could have a bunch of trailing tags we do not care about, so
		// to handle this we just trim them off the end because they will
		// never get used by the processor and as such will never be valid
		while (poolSize > 0 && poolTags[poolSize - 1] == 0)
			poolSize--;
			
		// Need to resize?
		if (poolTags.length != poolSize)
			poolTags = Arrays.copyOf(poolTags, poolSize);
		
		// Store to the fingerprint
		fingerprint.add(poolTags);
		
		// Setup opcodes and store to the fingerprint
		int logicalLen = __code.instructionCount();
		int[] opCodes = new int[logicalLen];
		fingerprint.add(opCodes);
		
		// Now we go through the logical normalized code indexes, we use the
		// normalized instructions so that similar instructions such as
		// ALOAD_0 and ALOAD 0 are mapped to the same instruction, as it
		// happens in the processor. So this means if the only difference
		// between one method and another is that it uses LDC and another
		// uses LDC_W or LDC2_W, it is treated the same
		for (int logicalAddr = 0; logicalAddr < logicalLen; logicalAddr++)
		{
			// Get the normalized instruction and its raw details
			Instruction instruction = __code.getByIndex(logicalAddr)
				.normalize();
			int opCode = instruction.operation();
			int[] rawArgs = instruction.rawArguments();
			
			// Store opcode for fingerprinting
			opCodes[logicalAddr] = opCode;
			
			// The fingerprinting only cares about logical instructions, so
			// map raw arguments for jumps accordingly...
			switch (opCode)
			{
				case InstructionIndex.GOTO_W:
					rawArgs[0] = __code.addressToIndex(rawArgs[0]);
					break;
						
				case InstructionIndex.LOOKUPSWITCH:
					for (int i = 0, n = rawArgs.length; i < n; i++)
					{
						// Skip number of pairs and key slots
						if (i == 1 || (i >= 2 && ((i - 2) % 2) == 0))
							continue;
						
						rawArgs[i] = __code.addressToIndex(rawArgs[i]);
					}
					break;
					
				case InstructionIndex.TABLESWITCH:
					for (int i = 0, n = rawArgs.length; i < n; i++)
					{
						// Skip low and hi
						if (i == 1 || i == 2)
							continue;
						
						rawArgs[i] = __code.addressToIndex(rawArgs[i]);
					}
					break;
			}
			
			// Store to the fingerprint
			fingerprint.add(rawArgs);
		}
		
		// The fingerprint is the combined integer array with everything
		// in it, and as such becomes its signature
		this._fingerprint = ArrayUtils.flattenPrimitive(fingerprint);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/09
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof CodeFingerprint))
			return false;
		
		// Quickly compare by hash first, if it was previously calculated this
		// will cause future calculations to go much quicker
		CodeFingerprint o = (CodeFingerprint)__o;
		if (this.hashCode() != o.hashCode())
			return false;
		
		// The two arrays must be equal
		return Arrays.equals(this._fingerprint, o._fingerprint);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/09
	 */
	@Override
	public int hashCode()
	{
		int result = this._hashCode;
		if (result != 0)
			return result;
		
		// Calculate standard hash code
		result = new IntegerIntegerArray(this._fingerprint).hashCode();
		this._hashCode = result;
		return result;
	}
}
