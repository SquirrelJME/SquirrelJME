// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

/**
 * Represents the disposal method on how subsequent frames are to appear in
 * animated GIFs.
 *
 * @since 2022/07/06
 */
public enum __GIFDisposeMethod__
{
	/** None specified, nothing has to be done. */
	NONE_SPECIFIED(0),
	
	/** Do not dispose the image. */
	KEEP(1),
	
	/** Restore background color. */
	RESTORE_BACKGROUND_COLOR(2),
	
	/** Restore the previous frame, before everything was drawn over. */
	RESTORE_PREVIOUS_FRAME(3),
	
	/* End. */
	;
	
	/** The ID of the disposal method. */
	public final int id;
	
	/**
	 * Initializes the enumeration.
	 * 
	 * @param __id The ID of the disposal method.
	 * @since 2022/07/06
	 */
	__GIFDisposeMethod__(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * Determines the disposal method.
	 * 
	 * @param __i The value to get.
	 * @return The value of the given index or {@link #NONE_SPECIFIED}
	 * if it is the default or unknown.
	 * @since 2022/07/09
	 */
	public static __GIFDisposeMethod__ valueOf(int __i)
	{
		switch (__i)
		{
			case 1: return __GIFDisposeMethod__.KEEP;
			case 2: return __GIFDisposeMethod__.RESTORE_BACKGROUND_COLOR;
			case 3: return __GIFDisposeMethod__.RESTORE_PREVIOUS_FRAME;
		}
		
		// Default to none otherwise
		return __GIFDisposeMethod__.NONE_SPECIFIED;
	}
}
