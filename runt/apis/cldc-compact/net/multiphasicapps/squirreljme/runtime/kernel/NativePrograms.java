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

/**
 * This interface is used as the raw low-level access to the program manager
 * on the host system.
 *
 * @since 2017/12/25
 */
public interface NativePrograms
{
	/**
	 * Lists programs which are currently available on the underlying
	 * set of programs.
	 *
	 * @return An array containing the available programs.
	 * @since 2017/12/25
	 */
	public abstract NativeProgram[] list();
}

