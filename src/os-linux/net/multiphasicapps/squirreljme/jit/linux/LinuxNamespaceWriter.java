// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.linux;

import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.traditional.
	TraditionalNamespaceWriter;

/**
 * This is the base class for Linux based namespace writers, the code here
 * must be architecture independent.
 *
 * @since 2016/07/08
 */
public abstract class LinuxNamespaceWriter
	extends TraditionalNamespaceWriter
{
	/**
	 * Initializes the Linux namespace writer.
	 *
	 * @param __ns The namespace being written.
	 * @param __conf The output configuration.
	 * @since 2016/07/08
	 */
	public LinuxNamespaceWriter(String __ns, JITOutputConfig.Immutable __conf)
	{
		super(__ns, __conf);
	}
}

