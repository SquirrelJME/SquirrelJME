// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.magic;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is a rewritten stream which writes to the host standard error.
 *
 * @since 2016/04/16
 */
final class __StdErrOutputStream__
	extends OutputStream
{
	/**
	 * Initializes the standard error stream.
	 *
	 * @since 2016/04/16
	 */
	__StdErrOutputStream__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/16
	 */
	@Override
	public void write(int __b)
	{
		throw new ForbiddenMagicError();
	}
}

