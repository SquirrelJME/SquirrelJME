// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.linux.powerpc32;

import net.multiphasicapps.squirreljme.jit.JITOSModifier;

/**
 * Modifies the JIT for 32-bit PowerPC systems.
 *
 * @since 2016/07/03
 */
public class LinuxPPC32Modifier
	extends JITOSModifier
{
	/**
	 * Initializes the modifier.
	 *
	 * @since 2016/07/03
	 */
	public LinuxPPC32Modifier()
	{
		super("powerpc32", "linux");
	}
}

