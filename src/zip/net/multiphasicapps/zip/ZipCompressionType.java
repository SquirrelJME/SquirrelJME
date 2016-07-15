// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

/**
 * This represents the type of compression that is used in a ZIP file.
 *
 * @since 2016/07/15
 */
public enum ZipCompressionType
{
	/** Data is not compressed. */
	NO_COMPRESSION,
	
	/** Deflate algorithm. */
	DEFLATE,
	
	/** End. */
	;
}

