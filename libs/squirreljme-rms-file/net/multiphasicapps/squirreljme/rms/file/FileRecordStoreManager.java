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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.squirreljme.rms.RecordStoreManager;

/**
 * This is a record store manager which uses the local file system to manage
 * records.
 *
 * @since 2017/02/27
 */
public class FileRecordStoreManager
	extends RecordStoreManager
{
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.rms.file.recordstorepath=path
	 * This is used to specify an alternative default path where record stores
	 * will be located.}
	 */
	public static final String RECORD_STORE_PATH_PROPERTY =
		"net.multiphasicapps.squirreljme.rms.file.recordstorepath";
	
	/** The path where records are stored. */
	protected final Path path;
	
	/**
	 * Initializes the file backed record store manager using the default
	 * location.
	 *
	 * @since 2017/02/27
	 */
	public FileRecordStoreManager()
	{
		this(__defaultPath());
	}
	
	/**
	 * Initializes the file backed record store manager.
	 *
	 * @param __p The path where records are stored.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/27
	 */
	public FileRecordStoreManager(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.path = __p;
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

