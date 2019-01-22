// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

/**
 * This class manages the set of registers to work with.
 *
 * @since 2019/01/21
 */
public final class RegisterSet
{
	/** Virtual stack base, which register starts the virtual stack. */
	protected final int vstackbase;
	
	/** Work registers. */
	private final WorkRegister[] _registers;
	
	/**
	 * Initializes the register set.
	 *
	 * @param __n The number of registers to store.
	 * @param __vsb The virtual stack base index, used to simulate the Java
	 * stack.
	 * @since 2019/01/21
	 */
	public RegisterSet(int __n, int __vsb)
	{
		// Initialize work registers
		WorkRegister[] registers = new WorkRegister[__n];
		for (int i = 0; i < __n; i++)
			registers[i] = new WorkRegister(i);
		
		// Set
		this._registers = registers;
		this.vstackbase = __vsb;
	}
	
	/**
	 * Returns the register at the given index.
	 *
	 * @param __i The index to get.
	 * @return The register at the given index.
	 * @since 2019/01/22
	 */
	public final WorkRegister get(int __i)
	{
		return this._registers[__i];
	}
}

