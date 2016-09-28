// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe;

import net.multiphasicapps.squirreljme.jit.base.JITConfig;

/**
 * This is used to create instances of {@link ExecutableOutput} which creates
 * natively executable binaries.
 *
 * @since 2016/09/28
 */
public interface ExecutableOutputFactory
{
	/**
	 * Creates an executable for output.
	 *
	 * @param __conf The configuration to use for the output.
	 * @return The executable output.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/28
	 */
	public abstract ExecutableOutput createExecutable(JITConfig __conf)
		throws NullPointerException;
}

