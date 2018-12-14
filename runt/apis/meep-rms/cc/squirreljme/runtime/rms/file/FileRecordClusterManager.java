// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms.file;

import cc.squirreljme.runtime.rms.RecordCluster;
import cc.squirreljme.runtime.rms.RecordClusterManager;
import cc.squirreljme.runtime.rms.RecordStoreOwner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.microedition.rms.RecordStoreException;

/**
 * This is a record store manager which uses the local file system to manage
 * records.
 *
 * @since 2017/02/27
 */
@Deprecated
public class FileRecordClusterManager
	extends RecordClusterManager
{
	/**
	 * {@squirreljme.property
	 * cc.squirreljme.rms.file.recordstorepath=path
	 * This is used to specify an alternative default path where record stores
	 * will be located.}
	 */
	public static final String RECORD_STORE_PATH_PROPERTY =
		"cc.squirreljme.rms.file.recordstorepath";
	
	/** The path where records are stored. */
	protected final Path path;
	
	/**
	 * Initializes the file backed record store manager using the default
	 * location.
	 *
	 * @throws RecordStoreException If there was a problem initializing the
	 * record store.
	 * @since 2017/02/27
	 */
	public FileRecordClusterManager()
		throws RecordStoreException
	{
		this(__defaultPath());
	}
	
	/**
	 * Initializes the file backed record store manager.
	 *
	 * @param __p The path where records are stored.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If there was a problem initializing the
	 * record store.
	 * @since 2017/02/27
	 */
	public FileRecordClusterManager(Path __p)
		throws RecordStoreException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.path = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/28
	 */
	@Override
	protected RecordCluster internalOpen(RecordStoreOwner __o)
		throws RecordStoreException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new FileRecordCluster(__o, this.path);
	}
	
	/**
	 * Returns the default path where record stores are located.
	 *
	 * @return The path where record stores are located.
	 * @since 2017/02/27
	 */
	private static final Path __defaultPath()
	{
		// If the record store path is used then use that location insted
		String prop = System.getProperty(RECORD_STORE_PATH_PROPERTY);
		if (prop != null)
			return Paths.get(prop);
		
		// Otherwise use another directory for the records
		prop = System.getProperty("user.home");
		if (prop != null)
			return Paths.get(prop).resolve(".squirreljme");
		
		// Otherwise use the default path
		return Paths.get("");
	}
}

