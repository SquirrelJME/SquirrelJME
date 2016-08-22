// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.fs;

/**
 * Represents a basic non-permission attribute which may be associated with
 * a file or directory.
 *
 * @since 2016/08/21
 */
public enum NativeBasicAttribute
{
	/** Compress the file. */
	COMPRESS,
	
	/** Archive the file. */
	ARCHIVE,
	
	/** The file is hidden. */
	HIDDEN,
	
	/** The file is considered to be a system file. */
	SYSTEM,
	
	/** The file is a symbolic link. */
	SYMBOLIC_LINK,
	
	/** The file is a hard link. */
	HARD_LINK,
	
	/** The file has one or more resource forks. */
	RESOURCE_FORKS,
	
	/** Backupable. */
	BACKUP,
	
	/** Bundlable. */
	BUNDLE,
	
	/** Recyclable. */
	RECYCLABLE,
	
	/** Reset after install. */
	RESET_AFTER_INSTALL,
	
	/** Launchable. */
	LAUNCHABLE,
	
	/** Resource database. */
	RESOURCE_DATABASE,
	
	/** End. */
	;
}

