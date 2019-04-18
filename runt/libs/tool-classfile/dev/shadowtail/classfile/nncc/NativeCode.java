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
	
	/** Static field area register. */
	public static final int STATIC_FIELD_REGISTER =
		16384;
	
	/** The return value register. */
	public static final int RETURN_REGISTER =
		16386;
	
	/** The thrown exception register. */
	public static final int EXCEPTION_REGISTER =
		16388;
	
	/** Instructions for this code. */
	private final NativeInstruction[] _instructions;
	
	/** Line number table. */
	private final short[] _lines;
	
	/**
	 * Initializes the register code.
	 *
	 * @param __i The associated instructions.
	 * @param __l The lines for each instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public NativeCode(NativeInstruction[] __i, short[] __l)
		throws NullPointerException
	{
		__i = (__i == null ? new NativeInstruction[0] : __i.clone());
		for (NativeInstruction i : __i)
			if (i == null)
				throw new NullPointerException("NARG");
		
		this._instructions = __i;
		this._lines = (__l == null ? new short[0] : __l.clone());
	}
	
	/**
	 * Initializes the register code.
	 *
	 * @param __i The associated instructions.
	 * @param __l The lines for each instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public NativeCode(Collection<NativeInstruction> __i, short[] __l)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		NativeInstruction[] ii = __i.<NativeInstruction>toArray(
			new NativeInstruction[__i.size()]);
		for (NativeInstruction i : ii)
			if (i == null)
				throw new NullPointerException("NARG");
		
		this._instructions = ii;
		this._lines = (__l == null ? new short[0] : __l.clone());
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
	public final short[] lines()
	{
		return this._lines.clone();
	}
}

