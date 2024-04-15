// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * Indicates the kind of struct.
 *
 * @since 2023/06/24
 */
public enum CStructKind
{
	/** A standard struct. */
	STRUCT,
	
	/** A union. */
	UNION,
	
	/* End. */
	;
}
