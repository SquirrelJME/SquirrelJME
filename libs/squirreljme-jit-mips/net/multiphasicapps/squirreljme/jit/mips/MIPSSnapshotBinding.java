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
import net.multiphasicapps.squirreljme.jit.SnapshotBinding;

/**
 * This is a binding for MIPS register or stack values which determines where
 * values exist.
 *
 * This class is immutable.
 *
 * @since 2017/02/19
 */
public final class MIPSSnapshotBinding
	extends MIPSBinding
	implements SnapshotBinding
{
	/** The stack offset. */
	protected final int stackoffset;
	
	/** The stack length. */
	protected final int stacklength;
	
	/** Registers which are associated with this binding. */
	private final MIPSRegister[] _registers;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the MIPS binding which snapshots an active binding
	 *
	 * @param __b The binding to snapshot.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	MIPSSnapshotBinding(MIPSBinding __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Copy
		MIPSRegister[] registers = __b.registers();
		this._registers = (registers != null ? registers.clone() : null);
		this.stackoffset = __b.stackOffset();
		this.stacklength = __b.stackLength();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public MIPSRegister[] registers()
	{
		// Stack only
		if (this.stacklength > 0)
			return null;
		
		// Clone
		return this._registers.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public int stackLength()
	{
		return this.stacklength;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public int stackOffset()
	{
		return this.stackoffset;
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
			int stacklength = this.stacklength;
			if (stacklength <= 0)
				rv = Arrays.asList(this._registers).toString();
			else
				rv = String.format("<%+d, %d>", this.stackoffset,
					stacklength);
			
			// Store
			this._string = new WeakReference<>(rv);
		}
		
		return rv;
	}
}

