// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

/**
 * This represents the type of token which was parsed.
 *
 * @since 2017/09/04
 */
public enum TokenType
{
	/** A comment. */
	COMMENT,
	
	/** End. */
	;
	
	/**
	 * Is this a comment?
	 *
	 * @return If this is a comment or not.
	 * @since 2017/09/06
	 */
	public final boolean isComment()
	{
		switch (this)
		{
				// Comments
			case COMMENT:
				return true;
			
				// Not a comment
			default:
				return false;
		}
	}
}

