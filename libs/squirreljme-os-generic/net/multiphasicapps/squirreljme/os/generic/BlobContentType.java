// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.generic;

/**
 * This represents the type that a content is within the blob.
 *
 * @since 2016/07/27
 */
public enum BlobContentType
{
	/** Undefined. */
	UNDEFINED,
	
	/** A class. */
	CLASS,
	
	/** A resource. */
	RESOURCE,
	
	/** A compressed resource. */
	COMPRESSED_RESOURCE,
	
	/** End. */
	;
}

