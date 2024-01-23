// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents a potential ID size.
 *
 * @since 2024/01/22
 */
public enum JDWPIdKind
{
	/** Field ID. */
	FIELD_ID,
	
	/** Method ID. */
	METHOD_ID,
	
	/** Object ID. */
	OBJECT_ID,
	
	/** Reference type. */
	REFERENCE_TYPE_ID,
	
	/** Frame ID. */
	FRAME_ID,
	
	/** Thread ID. */
	THREAD_ID,
	
	/* End. */
	;
}
