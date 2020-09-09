// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

/**
 * Check to see if the ROM is valid.
 *
 * @since 2020/08/23
 */
public class CheckRomShouldBuild
	implements Spec<Task>
{
	/** The virtual machine specifier. */
	protected final VMSpecifier vmType;
	
	/**
	 * Checks if a ROM should be built.
	 * 
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public CheckRomShouldBuild(VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__vmType == null)
			throw new NullPointerException("NARG");
		
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		return this.vmType.hasRom();
	}
}
