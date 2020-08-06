// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

/**
 * Represents the type of virtual machine to run.
 *
 * @since 2020/08/06
 */
public enum VirtualMachineType
{
	/** Native virtual machine. */
	NATIVE("Native"),
	
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
	 * Returns the proper name of the virtual machine.
	 * 
	 * @param __firstLower Should the first letter be lowercase?
	 * @return The proper name of the VM.
	 * @since 2020/08/06
	 */
	public final String properName(boolean __firstLower)
	{
		if (__firstLower)
			return Character.toLowerCase(this.properName.charAt(0)) +
				this.properName.substring(1);
		return this.properName;
	}
}
