// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Class statuc.
 *
 * @since 2021/03/16
 */
public enum JDWPClassStatus
{
	/** Class unloaded. */
	UNLOAD(0),
	
	/** Class Verified. */
	VERIFIED(1),
	
	/** Class prepared. */
	PREPARED(2),
	
	/** Class initialized. */
	INITIALIZED(4),
	
	/** Error state. */
	ERROR(8),
	
	/* End. */
	;
	
	/** The ID. */
	public final int id;
	
	/** The bits to set this. */
	public final int bits;
	
	/**
	 * Initializes the class type.
	 * 
	 * @param __id The ID.
	 * @since 2021/03/13
	 */
	JDWPClassStatus(int __id)
	{
		this.id = __id;
		this.bits = Math.max(0, __id - 1);
	}
}
