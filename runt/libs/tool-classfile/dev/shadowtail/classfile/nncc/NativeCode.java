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

import dev.shadowtail.classfile.nncc.NearNativeByteCodeHandler;
import dev.shadowtail.classfile.xlate.ByteCodeProcessor;
import java.util.Collection;
import java.util.Iterator;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.collections.UnmodifiableIterator;

/**
 * This is similar to {@link ByteCode} except that it instead of using a
 * stack for intermediate Java operations, this instead uses registers. This
 * provides a more concise and easier to use format by virtual machines.
 *
 * @see ByteCode
 * @since 2019/03/09
 */
public final class NativeCode
	implements Iterable<NativeInstruction>
{
	/** The zero register. */
	public static final int ZERO_REGISTER =
		0;
	
	/** The return value register (two slots, 1 + 2). */
	public static final int RETURN_REGISTER =
		1;
	
	/** The exception register. */
	public static final int EXCEPTION_REGISTER =
		3;
	
	/** The pointer containing static field data. */
	public static final int STATIC_FIELD_REGISTER =
		4;
	
	/** Global class table. */
	public static final int CLASS_TABLE_REGISTER =
		5;
	
	/** Register which represents the current thread of execution. */
	public static final int THREAD_REGISTER =
		6;
	
	/** Base for local registers (arguments start here). */
	public static final int LOCAL_REGISTER_BASE =
		8;
	
	/** The register containing the constant pool. */
	public static final int POOL_REGISTER =
		9;
	
	/** The register which contains the next pool pointer to use. */
	public static final int NEXT_POOL_REGISTER =
		10;
	
	/** Contains the information address of the current method. */
	@Deprecated
	public static final int WHERE_IS_THIS =
		11;
	
	/** Volatile A register. */
	public static final int VOLATILE_A_REGISTER =
		12;
	
	/** Volatile B register. */
	public static final int VOLATILE_B_REGISTER =
		13;
	
	/** Volatile S register (sticky). */
	public static final int VOLATILE_S_REGISTER =
		14;
	
	/** Copy of this for synchronized method purposes. */
	public static final int MONITOR_TARGET_REGISTER =
		15;
	
	/** The register of the first argument. */
	public static final int ARGUMENT_REGISTER_BASE =
		16;
	
	/** This bit XORed NEG bit == set, means memory access is volatile. */
	@Deprecated
	public static final int MEMORY_OFF_VOLATILE_BIT =
		0x80000000;
	
	/**
	 * This bit is just used to signal that the offset is negative. After the
	 * volatility has been determined, the volatile bit is set to the value
	 * of this bit then execution continues.
	 */
	public static final int MEMORY_OFF_NEG_BIT =
		0x40000000;
	
	/** Mask to access the offset value. */
	public static final int MEMORY_OFF_VALUE_MASK =
		0x7FFFFFFF;
	
	/** Instructions for this code. */
	private final NativeInstruction[] _instructions;
	
	/** Line number table. */
	@Deprecated
	private final short[] _lines;
	
	/** Java operation table. */
	@Deprecated
	private final byte[] _jops;
	
	/** Java PC table. */
	@Deprecated
	private final byte[] _jpcs;
	
	/**
	 * Initializes the register code.
	 *
	 * @param __i The associated instructions.
	 * @param __l The lines for each instruction.
	 * @param __jo Java operation index.
	 * @param __jp Java PC addresses.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public NativeCode(NativeInstruction[] __i, short[] __l, byte[] __jo,
		byte[] __jp)
		throws NullPointerException
	{
		__i = (__i == null ? new NativeInstruction[0] : __i.clone());
		for (NativeInstruction i : __i)
			if (i == null)
				throw new NullPointerException("NARG");
		
		this._instructions = __i;
		this._lines = (__l == null ? new short[0] : __l.clone());
		this._jops = (__jo == null ? new byte[0] : __jo.clone());
		this._jpcs = (__jp == null ? new byte[0] : __jp.clone());
	}
	
	/**
	 * Gets the instruction at this index.
	 *
	 * @param __dx The index to get.
	 * @return The register at this index.
	 * @since 2019/03/26
	 */
	public final NativeInstruction get(int __dx)
	{
		return this._instructions[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/23
	 */
	@Override
	public final Iterator<NativeInstruction> iterator()
	{
		return UnmodifiableIterator.<NativeInstruction>of(
			this._instructions);
	}
	
	/**
	 * The table containing which instructions map to which Java address.
	 *
	 * @return The Java address table.
	 * @since 2019/04/26
	 */
	@Deprecated
	public final byte[] javaAddresses()
	{
		return this._jpcs.clone();
	}
	
	/**
	 * The table containing the table of instructions that map to which
	 * Java instruction.
	 *
	 * @return The Java operation table.
	 * @since 2019/04/26
	 */
	@Deprecated
	public final byte[] javaOperations()
	{
		return this._jops.clone();
	}
	
	/**
	 * Returns the length of the register code.
	 *
	 * @return The register code length.
	 * @since 2019/03/26
	 */
	public final int length()
	{
		return this._instructions.length;
	}
	
	/**
	 * The line number table.
	 *
	 * @return The line number table.
	 * @since 2018/03/24
	 */
	@Deprecated
	public final short[] lines()
	{
		return this._lines.clone();
	}
}

