// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.profiler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Base interface for writing snapshots, note that these cannot be shared and
 * cannot be used over again as they may retain their own state.
 *
 * @since 2023/08/19
 */
public interface SnapshotWriter
{
	/**
	 * Performs writing to the output. 
	 *
	 * @throws IOException On write errors.
	 * @since 2023/08/19
	 */
	void write()
		throws IOException;
}
