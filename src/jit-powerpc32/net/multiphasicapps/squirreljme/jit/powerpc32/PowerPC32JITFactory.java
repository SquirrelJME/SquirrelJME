// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.powerpc32;

import net.multiphasicapps.squirreljme.jit.JITFactory;

/**
 * This factory produces JITs which generate 32-bit PowerPC code.
 *
 * @since 2016/07/02
 */
public class PowerPC32JITFactory
	extends JITFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/02
	 */
	@Override
	public String architectureName()
	{
		return "powerpc32";
	}
}

