// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * This is the base class for all connection types.
 *
 * @since 2019/05/06
 */
public interface Connection
	extends Closeable
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public abstract void close()
		throws IOException;
}


