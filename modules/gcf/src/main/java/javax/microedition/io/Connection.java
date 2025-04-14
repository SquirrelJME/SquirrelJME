// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.Closeable;
import java.io.IOException;

/**
 * This is the base class for all connection types.
 *
 * @see Connector
 * @since 2019/05/06
 */
@Api
public interface Connection
	extends Closeable
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	void close()
		throws IOException;
}


