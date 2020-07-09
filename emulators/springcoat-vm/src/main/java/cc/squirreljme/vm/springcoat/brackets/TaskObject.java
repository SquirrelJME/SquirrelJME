// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * Represents a task object.
 *
 * @since 2020/07/08
 */
public final class TaskObject
	extends AbstractGhostObject
{
	/** The machine. */
	protected final SpringMachine machine;
	
	/**
	 * Initializes the task reference.
	 * 
	 * @param __machine The machine to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/08
	 */
	public TaskObject(SpringMachine __machine)
		throws NullPointerException
	{
		if (__machine == null)
			throw new NullPointerException("NARG");
		
		this.machine = __machine;
	}
	
	/**
	 * Returns the machine.
	 * 
	 * @return The machine.
	 * @since 2020/07/08
	 */
	public final SpringMachine getMachine()
	{
		return this.machine;
	}
}
