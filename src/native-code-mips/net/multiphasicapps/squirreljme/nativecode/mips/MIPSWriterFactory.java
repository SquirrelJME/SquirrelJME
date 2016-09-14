// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.mips;

import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;

/**
 * This is a factory which creates native code writers for MIPS based
 * systems.
 *
 * @since 2016/09/10
 */
public class MIPSWriterFactory
	implements NativeCodeWriterFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public MIPSWriter create()
	{
		return new MIPSWriter();
	}
}

