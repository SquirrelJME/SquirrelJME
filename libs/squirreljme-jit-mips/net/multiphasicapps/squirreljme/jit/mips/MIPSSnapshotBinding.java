// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.CacheState;
import net.multiphasicapps.squirreljme.jit.DataType;
import net.multiphasicapps.squirreljme.jit.SnapshotBinding;

/**
 * This is a binding for MIPS register or stack values which determines where
 * values exist.
 *
 * This class is immutable.
 *
 * @since 2017/02/19
 */
@Override
public final class MIPSSnapshotBinding
	extends MIPSBinding
	implements SnapshotBinding
{
	/** Is this a stack item? */
	protected final boolean isstack;
	
	/** The slot index. */
	protected final int index;
	
	/** The length of the stack. */
	protected final int stacklen;
	
	/** The stack offset. */
	protected final int stackoff;
	
	/** Registers which are associated with this binding. */
	private final MIPSRegister[] _registers;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the MIPS binding which snapshots an active binding
	 *
	 * @param __e The engine owning this binding.
	 * @param __b The binding to snapshot.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	MIPSSnapshotBinding(MIPSEngine __e, MIPSBinding __b)
		throws NullPointerException
	{
		super(__e);
		
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Copy
		this.isstack = __b.isStack();
		this.index = __b.index();
		this.stacklen = __b.stackLength();
		this.stackoff = __b.stackOffset();
		MIPSRegister[] registers = __b.registers();
		this._registers = (registers != null ? registers.clone() : null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/07
	 */
	@Override
	public MIPSRegister getRegister(int __i)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AM08 Register index out of bounds. (The index)}
		MIPSRegister[] registers = this._registers;
		if (registers == null)
			throw new IndexOutOfBoundsException(String.format("AM08 %d", __i));
		return this._registers[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/08
	 */
	@Override
	public int index()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/08
	 */
	@Override
	public boolean isStack()
	{
		return this.isstack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/07
	 */
	@Override
	public int numRegisters()
	{
		MIPSRegister[] registers = this._registers;
		if (registers == null)
			return 0;
		return this._registers.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public MIPSRegister[] registers()
	{
		// Clone
		MIPSRegister[] registers = this._registers;
		if (registers == null)
			return null;
		return this._registers.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/08
	 */
	@Override
	public int stackLength()
	{
		return this.stacklen;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/08
	 */
	@Override
	public int stackOffset()
	{
		return this.stackoff;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/19
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			MIPSRegister[] registers = this._registers;
			if (registers.length > 0)
				rv = Arrays.asList(registers).toString();
			else
			{
				int stackoffset = stackOffset();
				if (stackoffset != Integer.MIN_VALUE)
					rv = String.format("<%+d, %d>", stackOffset(),
						stackLength());
				else
					rv = String.format("---", stackOffset(),
						stackLength());
			}
			
			// Store
			this._string = new WeakReference<>(rv);
		}
		
		return rv;
	}
}

