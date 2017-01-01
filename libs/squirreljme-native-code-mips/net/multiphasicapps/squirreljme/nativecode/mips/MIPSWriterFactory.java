// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.mips;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;

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
	public MIPSWriter create(NativeCodeWriterOptions __o, OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return new MIPSWriter(__o, __os);
	}
}

