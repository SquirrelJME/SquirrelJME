// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.lang.ArrayUtils;
import cc.squirreljme.runtime.cldc.util.ByteArrayList;
import cc.squirreljme.runtime.cldc.util.IntegerIntegerArray;
import cc.squirreljme.runtime.cldc.util.IntegerList;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.JavaStackShuffleType;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.Pool;
import net.multiphasicapps.classfile.StackMapTableEntry;
import net.multiphasicapps.classfile.StackMapTablePairs;
import net.multiphasicapps.classfile.StackMapTableState;
import net.multiphasicapps.io.CRC32Calculator;

/**
 * Represents the fingerprint of a method to determine whether two methods
 * have the same byte code, this is not likely to occur unless within
 * trivial methods.
 *
 * @since 2023/08/09
 */
public final class CodeFingerprint
	implements Comparable<CodeFingerprint>
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
	
	/** The checksum of the fingerprint. */
	volatile int _checkSum;
	
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
		StackMapTablePairs stackMapPairs = __code.stackMapTableFull();
		
		// Record the entry type local variables
		StackMapTableState initLocals = stackMapPairs.get(0).input;
		int usedLocals;
		int maxLocals = initLocals.maxLocals();
		int[] localTypes = new int[maxLocals];
		for (usedLocals = 0; usedLocals < maxLocals; usedLocals++)
		{
			StackMapTableEntry local = initLocals.getLocal(usedLocals);
			
			// Stop at nothing, no pun intended
			if (local.type().isNothing())
				break;
			
			// Skip top types
			if (local.type().isTop())
				continue;
			
			// Record the used primitive type here
			localTypes[usedLocals] = JvmPrimitiveType.of(
				local.type()).ordinal();
		}
		
		// Add initial locals, which is from method arguments
		fingerprint.add((usedLocals == maxLocals ? localTypes :
			Arrays.copyOf(localTypes, usedLocals)));
		
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
		
		// Setup opcodes and store to the fingerprint
		int logicalLen = __code.instructionCount();
		int[] opCodes = new int[logicalLen];
		fingerprint.add(opCodes);
		
		// Which pool entries are actually used?
		Set<Integer> usedPool = new SortedTreeSet<>();
		
		// Forgetting raw arguments
		int[] forgetRawArgs = new int[0];
		
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
			// Additionally, for any instructions which utilize pool entries,
			// set up a list of the ones that are actually used instead of
			// using rawArgs
			switch (opCode)
			{
					// For stack shuffle operations, because of the nature
					// of long/double and such, along with references, and
					// also as well types being in their own operand stack,
					// we need to record the fingerprint for these
				case InstructionIndex.POP:
				case InstructionIndex.POP2:
				case InstructionIndex.DUP:
				case InstructionIndex.DUP_X1:
				case InstructionIndex.DUP_X2:
				case InstructionIndex.DUP2:
				case InstructionIndex.DUP2_X1:
				case InstructionIndex.DUP2_X2:
				case InstructionIndex.SWAP:
					{
						// Need to find the details of the input and such
						StackMapTableState input = stackMapPairs.get(
							instruction.address()).input;
						JavaStackShuffleType shuffle =
							JavaStackShuffleType.ofOperation(opCode);
						JavaStackShuffleType.Function function =
							shuffle.findShuffleFunction(input);
						
						// Map to Java types on the given stack
						JavaType[] types = function.in.javaTypes(input);
						int n = types.length;
						rawArgs = new int[n];
						for (int i = 0; i < n; i++)
							rawArgs[i] = JvmPrimitiveType.of(types[i])
								.ordinal();
					}
					break;
					
				case InstructionIndex.ANEWARRAY:
				case InstructionIndex.CHECKCAST:
				case InstructionIndex.GETFIELD:
				case InstructionIndex.GETSTATIC:
				case InstructionIndex.INSTANCEOF:
				case InstructionIndex.INVOKEINTERFACE:
				case InstructionIndex.INVOKESPECIAL:
				case InstructionIndex.INVOKESTATIC:
				case InstructionIndex.INVOKEVIRTUAL:
				case InstructionIndex.LDC2_W:
				case InstructionIndex.LDC:
				case InstructionIndex.LDC_W:
				case InstructionIndex.NEW:
				case InstructionIndex.PUTFIELD:
				case InstructionIndex.PUTSTATIC:
					// We can use the raw arguments here directly because they
					// do match with the pool
					for (int rawArg : rawArgs)
						usedPool.add(rawArg);
					
					// Forget raw arguments because they have no effect here,
					// and we only care about pool entries
					rawArgs = forgetRawArgs;
					break;
					
					// This is the only one that is special
				case InstructionIndex.MULTIANEWARRAY:
					// Before we forget the argument, store it into the used
					// entries
					usedPool.add(rawArgs[0]);
					
					// Clear first argument, because that is the pool index and
					// we do not exactly need that right now
					rawArgs[0] = 0;
					break;
					
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
		
		// Determine the number of pool entries we will be keeping
		int keepPool = usedPool.size();
		
		// Setup base pool tag list
		int[] useTags = new int[keepPool + 1];
		useTags[0] = useTags.length;
		
		// Copy in the tag set
		int[] usedPoolInt = new IntegerList(usedPool).toIntegerArray();
		for (int i = 0, n = keepPool; i < n; i++)
			useTags[1 + i] = usedPoolInt[i];
		
		// Store to the fingerprint, always first because this one is very
		// important as a baseline setup
		fingerprint.add(0, usedPoolInt);
		
		// The fingerprint is the combined integer array with everything
		// in it, and as such becomes its signature
		this._fingerprint = ArrayUtils.flattenPrimitive(fingerprint);
	}
	
	/**
	 * Returns the checksum of the code fingerprint.
	 *
	 * @return The fingerprint checksum.
	 * @since 2023/08/13
	 */
	public int checkSum()
	{
		int result = this._checkSum;
		if (result != 0)
			return result;
		
		// Need to load this into a byte array for calculation
		int[] fingerprint = this._fingerprint;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
			4 * fingerprint.length);
			 DataOutputStream dos = new DataOutputStream(baos))
		{
			// Write fingerprint data
			for (int i : fingerprint)
				dos.writeInt(i);
			dos.flush();
			
			// Calculate standard checksum
			result = CRC32Calculator.calculateZip(baos.toByteArray());
			this._checkSum = result;
			return result;
		}
		catch (IOException __e)
		{
			throw new IllegalStateException(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/02
	 */
	@Override
	public int compareTo(CodeFingerprint __b)
	{
		int[] a = this._fingerprint;
		int[] b = __b._fingerprint;
		
		// Compare lengths first
		int len = a.length;
		int result = len - b.length;
		if (result != 0)
			return result;
		
		// The individual elements
		for (int i = 0; i < len; i++)
		{
			result = a[i] - b[i];
			if (result != 0)
				return result;
		}
		
		// Is equal
		return 0;
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
		if (this.hashCode() != o.hashCode() ||
			this.checkSum() != o.checkSum())
			return false;
		
		// The two arrays must be equal
		return Arrays.equals(this._fingerprint, o._fingerprint);
	}
	
	/**
	 * Returns the fingerprint data.
	 *
	 * @return The fingerprint data.
	 * @since 2023/09/03
	 */
	public int[] fingerprint()
	{
		return this._fingerprint.clone();
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
	
	/**
	 * The fingerprint length.
	 *
	 * @return The fingerprint length.
	 * @since 2023/08/13
	 */
	public int length()
	{
		return this._fingerprint.length;
	}
}
