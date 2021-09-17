// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

/**
 * This represents the type of enqueue to perform.
 *
 * @since 2019/04/06
 */
public enum JavaStackEnqueueType
{
	/** Normal state. */
	NORMAL,
	
	/** Cached state. */
	CACHED,
	
	/* End. */
	;
}

