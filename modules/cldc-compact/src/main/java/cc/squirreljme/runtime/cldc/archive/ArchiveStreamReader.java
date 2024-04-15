// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.archive;

import java.io.Closeable;
import java.io.IOException;

/**
 * Reader for any type of archive stream.
 *
 * @since 2022/08/20
 */
public interface ArchiveStreamReader
	extends Closeable
{
	/**
	 * Returns the next entry in the stream.
	 * 
	 * @return The next entry in the stream or {@code null}.
	 * @throws IOException On read errors.
	 * @since 2022/08/20
	 */
	ArchiveStreamEntry nextEntry()
		throws IOException;
}
