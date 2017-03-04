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

import java.util.Arrays;
import net.multiphasicapps.squirreljme.jit.Binding;

/**
 * This is the base interface for bindings for the MIPS JIT.
 *
 * @since 2017/03/03
 */
public abstract class MIPSBinding
	implements Binding
{
	/**
	 * Returns the currently assigned registers.
	 *
	 * @return The assigned registers.
	 * @since 2017/03/03
	 */
	public abstract MIPSRegister[] registers();
	
	/**
	 * Returns the length of the value on the stack.
	 *
	 * @return The stack length or {@link Integer#MIN_VALUE} if it is not
	 * valid.
	 * @since 2017/03/03
	 */
	public abstract int stackLength();
	
	/**
	 * Returns the offset on the stack the value will be placed.
	 *
	 * @return The stack offset or {@link Integer#MIN_VALUE} if it is not
	 * valid.
	 * @since 2017/03/03
	 */
	public abstract int stackOffset();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MIPSBinding))
			return false;
		
		MIPSBinding o = (MIPSBinding)__o;
		return this.stackOffset() == o.stackOffset() &&
			this.stackLength() == o.stackLength() &&
			Arrays.equals(this.registers(), o.registers());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public int hashCode()
	{
		int rv = 0;
		MIPSRegister[] registers = this.registers();
		for (int i = 0, n = registers.length; i < n; i++)
			rv ^= registers[i].hashCode();
		return rv ^ this.stackOffset() ^ (~this.stackLength());
	}
	
}

