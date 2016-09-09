// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.jit.JITObjectProperties;

/**
 * This is used to create instances of the native code writer.
 *
 * @since 2016/09/09
 */
public abstract class GenericNativeCodeWriterFactory
	implements JITObjectProperties
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public String[] properties()
	{
		throw new Error("TODO");
	}
}

