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
	FIELD_ID(0),
	
	/** Method ID. */
	METHOD_ID(1),
	
	/** Object ID. */
	OBJECT_ID(2),
	
	/** Reference type. */
	REFERENCE_TYPE_ID(3),
	
	/** Frame ID. */
	FRAME_ID(4),
	
	/** Thread ID. */
	THREAD_ID(JDWPIdKind.invert(2)),
	
	/** Unknown. */
	UNKNOWN(JDWPIdKind.invert(2)),
	
	/* End. */
	;
	
	/** The number of natural ID kinds. */
	public static final int NUM_KINDS =
		5;
	
	/** The position of this kind. */
	public final int position;
	
	/**
	 * Initializes the kind.
	 *
	 * @param __position The position of this.
	 * @since 2024/01/22
	 */
	JDWPIdKind(int __position)
	{
		this.position = __position;
	}
	
	/**
	 * Inverts the position value.
	 *
	 * @param __i The value to invert.
	 * @return The inverted value.
	 * @since 2024/01/23
	 */
	public static int invert(int __i)
	{
		return (-(__i) - 1);
	}
}
