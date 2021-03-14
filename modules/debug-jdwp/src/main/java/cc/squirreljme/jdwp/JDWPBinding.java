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
	 * Updates the state as needed for debugging.
	 * 
	 * @param __state The debugger state.
	 * @param __what What gets updated?
	 * @since 2021/03/13
	 */
	void debuggerUpdate(JDWPState __state, JDWPUpdateWhat... __what);
	
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
}
