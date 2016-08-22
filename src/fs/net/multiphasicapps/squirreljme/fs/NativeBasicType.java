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
 * This represents the basic type of what a file is on the filesystem.
 *
 * @since 2016/08/21
 */
public enum NativeBasicType
{
	/** A directory. */
	DIRECTORY,
	
	/** A normal file. */
	FILE,
	
	/** A character device. */
	CHAR_DEV,
	
	/** A block device. */
	BLOCK_DEV,
	
	/** A socket. */
	SOCKET,
	
	/** End. */
	;
}

