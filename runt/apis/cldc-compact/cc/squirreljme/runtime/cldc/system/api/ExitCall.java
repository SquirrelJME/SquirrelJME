// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.api;

import cc.squirreljme.runtime.cldc.system.SystemFunction;

/**
 * Interface for {@link SystemFunction#EXIT}.
 *
 * @since 2018/03/14
 */
public interface ExitCall
	extends Call
{
	/**
	 * Exit the virtual machine with the given exit code.
	 *
	 * This function does not return unless an exception is thrown.
	 *
	 * @param __e The exit code to use.
	 * @throws SecurityException If exit is not permitted.
	 * @since 2018/03/01
	 */
	public abstract void exit(int __e)
		throws SecurityException;
}

