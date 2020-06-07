// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * Represents a type from within the VM.
 *
 * @since 2020/06/02
 */
public class TypeObject
	extends AbstractGhostObject
{
	/** The class this provides. */
	protected final SpringClass classy;
	
	/**
	 * Initializes the type object.
	 *
	 * @param __cl The class to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/04
	 */
	public TypeObject(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classy = __cl;
	}
}
