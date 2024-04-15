// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This can be set on a stream which sets the default endianess on methods
 * that do not read using a specified endianess.
 *
 * @since 2016/07/10
 */
public enum DataEndianess
{
	/** Big endian. */
	BIG,
	
	/** Little endian. */
	LITTLE,
	
	/** End. */
	;
}

