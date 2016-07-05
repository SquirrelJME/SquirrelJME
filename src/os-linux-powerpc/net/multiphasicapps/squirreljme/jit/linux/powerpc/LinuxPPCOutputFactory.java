// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.linux.powerpc;

import net.multiphasicapps.squirreljme.jit.JITOutputFactory;

/**
 * This output factory targets PowerPC Linux systems.
 *
 * @since 2016/07/04
 */
public class LinuxPPCOutputFactory
	extends JITOutputFactory
{
	/**
	 * Factory which targets generic Linux.
	 *
	 * @since 2016/07/04
	 */
	public LinuxPPCOutputFactory()
	{
		this("generic");
	}
	
	/**
	 * A factory which targets a given variant of Linux.
	 *
	 * @param __var The variant to target.
	 * @sine 2016/07/04
	 */
	public LinuxPPCOutputFactory(String __var)
	{
		super("powerpc", "linux", __var);
	}
}

