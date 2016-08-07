// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.chv.linux;

import net.multiphasicapps.squirreljme.emulator.CPUState;
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.emulator.HypoVisor;
import net.multiphasicapps.squirreljme.emulator.VonNeumannAddressing;

/**
 * This implements the base Linux hypovisor since all Linux systems for every
 * architecture share a very common base.
 *
 * @since 2016/07/30
 */
public abstract class LinuxHypoVisor
	implements HypoVisor
{
	/** The initial ELF binary. */
	private volatile byte[] _initelf;
	
	/** The arguments to the starting program. */
	private volatile String[] _initargs;
	
	/** The owning emulator. */
	private volatile Emulator _emulator;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/06
	 */
	@Override
	public final void init(Emulator __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Set emulator
		this._emulator = __e;
		
		// {@squirreljme.error CB02 Initial program has not been set.}
		byte[] initelf = this._initelf;
		String[] initargs = this._initargs;
		if (initelf == null || initargs == null)
			throw new IllegalStateException("CB02");
		
		// Spawn program (not a fork)
		spawn(initelf, initargs, new String[0]);
	}
	
	/**
	 * Sets the init program to be ran when the emulator requests hypovisor
	 * initial state setup.
	 *
	 * @param __elf The executable bytes.
	 * @param __args The arguments to the executable.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public final void setInit(byte[] __elf, String... __args)
		throws NullPointerException
	{
		// Check
		if (__elf == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Copy
		this._initelf = __elf.clone();
		this._initargs = __args.clone();
	}
	
	/**
	 * Spawns the specified process using the given binary and arguments.
	 *
	 * @param __elf The executable to load and execute.
	 * @param __args The arguments to the program.
	 * @param __env The starting environment.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/06
	 */
	public final LinuxProcess spawn(byte[] __elf, String[] __args,
		String[] __env)
		throws NullPointerException
	{
		// Check
		if (__elf == null || __args == null || __env == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CB03 Cannot spawn a process because an emulator
		// was not set.}
		Emulator emulator = this._emulator;
		if (emulator == null)
			throw new IllegalStateException("CB03");
		
		// Create initial CPU state
		CPUState cs = emulator.createCPUState();
		
		// {@squirreljme.error CB01 Linux requires a CPU which has a Von
		// Neumann architecture (data and instructions are in the same
		// address space).}
		if (!(cs instanceof VonNeumannAddressing))
			throw new IllegalStateException("CB01");
		VonNeumannAddressing vna = (VonNeumannAddressing)cs;
		
		throw new Error("TODO");
	}
}

