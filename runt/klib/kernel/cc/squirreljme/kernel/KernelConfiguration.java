// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

import java.lang.ref.Reference;

/**
 * This interface is used to initialize the kernel with a configuration which
 * initializes some of its parameters as needed.
 *
 * @since 2018/01/03
 */
public interface KernelConfiguration
{
	/**
	 * Maps the given service for the client class to the server class.
	 *
	 * @param __sv The service to map.
	 * @return The class name of the server class for initializing client
	 * classes, if no mapping is available then {@code null} is used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/03
	 */
	public abstract String mapService(String __sv)
		throws NullPointerException;
	
	/**
	 * Returns an iterable which is used to return the services which this
	 * configuration defines services for.
	 *
	 * @return An iterable containing the client classes which this
	 * implementation supplies servers for.
	 * @since 2018/01/03
	 */
	public abstract Iterable<String> services();
	
	/**
	 * Returns the task which represents the kernel itself.
	 *
	 * @param __k The owning kernel.
	 * @return The task for the system.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/03
	 */
	public abstract KernelTask systemTask(Reference<Kernel> __k)
		throws NullPointerException;
}

