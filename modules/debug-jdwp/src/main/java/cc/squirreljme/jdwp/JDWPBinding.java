// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * This interface is used by anything that JDWP needs to communicate with,
 * other virtual machines or otherwise to perform actions.
 *
 * @since 2021/03/08
 */
public interface JDWPBinding
{
	/**
	 * Returns the virtual machine description.
	 * 
	 * @return The virtual machine description.
	 * @since 2021/03/13
	 */
	String vmDescription();
	
	/**
	 * The virtual machine name, as {@code java.vm.name}.
	 * 
	 * @return The virtual machine name, as {@code java.vm.name}.
	 * @since 2021/03/13
	 */
	String vmName();
	
	/**
	 * The virtual machine version, as {@code java.vm.version}.
	 * 
	 * @return The virtual machine version, as {@code java.vm.version}.
	 * @since 2021/03/13
	 */
	String vmVersion();
	
	/**
	 * Updates the groups.
	 * 
	 * @param __groups The groups to update.
	 * @since 2021/03/13
	 */
	void jdwpUpdateThreadGroups(JDWPThreadGroups __groups);
}
