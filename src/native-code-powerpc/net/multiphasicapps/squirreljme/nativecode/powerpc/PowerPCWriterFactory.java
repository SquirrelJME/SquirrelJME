// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.powerpc;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;

/**
 * This is a factory which creates factories for PowerPC based systems.
 *
 * @since 2016/09/10
 */
public class PowerPCWriterFactory
	implements NativeCodeWriterFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public PowerPCWriter create(NativeCodeWriterOptions __o, OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return new PowerPCWriter(__o, __os);
	}
}

