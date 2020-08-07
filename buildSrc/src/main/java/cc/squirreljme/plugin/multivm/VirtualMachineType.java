// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Project;

/**
 * Represents the type of virtual machine to run.
 *
 * @since 2020/08/06
 */
public enum VirtualMachineType
	implements VirtualMachineTaskProvider
{
	/** Hosted virtual machine. */
	HOSTED("Hosted"),
	
	/** SpringCoat virtual machine. */
	SPRINGCOAT("SpringCoat"),
	
	/** SummerCoat virtual machine. */
	SUMMERCOAT("SummerCoat"),
	
	/* End. */
	;
	
	/** The proper name of the VM. */
	public final String properName;
	
	/**
	 * Returns the proper name of the virtual machine.
	 * 
	 * @param __properName The proper name of the VM.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	VirtualMachineType(String __properName)
		throws NullPointerException
	{
		if (__properName == null)
			throw new NullPointerException("NARG");
		
		this.properName = __properName;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public String outputLibraryName(Project __project, String __sourceSet)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public String vmName(VMNameFormat __format)
		throws NullPointerException
	{
		String properName = this.properName;
		switch (__format)
		{
			case LOWERCASE:
				return properName.toLowerCase();
				
			case CAMEL_CASE:
				return Character.toLowerCase(properName.charAt(0)) +
					properName.substring(1);
				
			case PROPER_NOUN:
			default:
				return properName;
		}
	}
}
