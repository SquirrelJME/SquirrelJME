// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This represents a plain object within the virtual machine.
 *
 * @since 2019/04/17
 */
public class PlainObject
	implements Instance
{
	/** The loaded class this is. */
	public final LoadedClass loadedclass;
	
	/**
	 * Initializes the plain object.
	 *
	 * @param __cl The class this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public PlainObject(LoadedClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.loadedclass = __cl;
	}
}

