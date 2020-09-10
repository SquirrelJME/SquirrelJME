// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Returns the inputs for the ROM.
 *
 * @since 2020/08/23
 */
public class VMRomInputs
	implements Callable<Iterable<Path>>
{
	/** The source set used. */
	protected final String sourceSet;
	
	/** The task to generate for. */
	protected final VMRomTask task;
	
	/** The type of virtual machine used. */
	protected final VMSpecifier vmType;
	
	/**
	 * Initializes the handler.
	 * 
	 * @param __task The task to create for.
	 * @param __sourceSet The source set to use.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public VMRomInputs(VMRomTask __task,
		String __sourceSet, VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__task == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public Iterable<Path> call()
	{
		throw new Error("TODO -- MultiVMRomInputs");
	}
}
