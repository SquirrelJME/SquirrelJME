// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * This class contains the configuration of the virtual machine.
 *
 * Due to the way the kernel initialization works this class might not
 * actually be initialized through it constructors and as such it should not
 * have any code within it.
 *
 * @since 2019/04/20
 */
public final class JVMConfiguration
{
	/** The address of the ROM file containing definitions and code. */
	public int romaddr;
	
	/** The starting address of free memory. */
	public int memaddr;
	
	/** The amount of memory that is available for the VM to use. */
	public int memsize;
}

