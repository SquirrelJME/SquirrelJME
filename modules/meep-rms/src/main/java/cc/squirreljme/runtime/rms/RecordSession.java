// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import javax.microedition.rms.RecordStoreException;

/**
 * Records and keeps a session for a record store.
 *
 * @since 2025/04/20
 */
@SquirrelJMEVendorApi
public class RecordSession
	implements AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @throws RecordStoreException If the record could not be closed.
	 * @since 2025/04/20
	 */
	@Override
	public void close()
		throws RecordStoreException
	{
		throw Debugging.todo();
	}
}
