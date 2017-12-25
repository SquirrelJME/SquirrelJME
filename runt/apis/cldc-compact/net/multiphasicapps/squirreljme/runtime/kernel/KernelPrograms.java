// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class is used to manage the programs which are available for usage.
 *
 * Each program has an identifier which identitifies, that identifier is mapped
 * to a slot within the program list so that the same program will always have
 * the same identifier.
 *
 * @since 2017/12/14
 */
public abstract class KernelPrograms
{
	/**
	 * Initializes the program manager.
	 *
	 * @since 2017/12/14
	 */
	protected KernelPrograms()
	{
	}
	
	/**
	 * Returns the list of programs which are available.
	 *
	 * @param __by The task requesting the program list.
	 * @param __typemask A mask which is used to filter programs of a given
	 * type.
	 * @return An array containing the programs under the specified mask.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the operation is not permitted.
	 * @since 2017/12/11
	 */
	public final KernelProgram[] listPrograms(KernelTask __by, int __typemask)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0f The specified task is not permitted to
		// list available programs. (The task requesting the program list)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.LIST_PROGRAMS))
			throw new SecurityException(
				String.format("ZZ0f %s", __by));
		
		throw new todo.TODO();
	}
}

