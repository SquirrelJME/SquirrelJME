// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	/* Class Verified. */
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
	
	/**
	 * Initializes the class type.
	 * 
	 * @param __id The ID.
	 * @since 2021/03/13
	 */
	JDWPClassStatus(int __id)
	{
		this.id = __id;
	}
}
