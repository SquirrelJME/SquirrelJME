// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.rms.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.squirreljme.rms.RecordCluster;
import net.multiphasicapps.squirreljme.rms.RecordStoreOwner;

/**
 * This is a cluster which is backed by the filesystem.
 *
 * @since 2017/02/28
 */
public class FileRecordCluster
	extends RecordCluster
{
	/** The root where record stores exist. */
	protected final Path path;
	
	/**
	 * Initializes the file record cluster.
	 *
	 * @param __o The owner of the cluster.
	 * @param __p The base cluster path.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/26
	 */
	public FileRecordCluster(RecordStoreOwner __o, Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Derive prefix
		throw new Error("TODO");
	}
}

