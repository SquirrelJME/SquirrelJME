// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.common;

/**
 * This contains structured information on a character which has been
 * decoded. This is used for font rendering.
 *
 * @since 2017/10/24
 */
public final class DecodedCharacter
{
	/** The primary and visible codepoint. */
	public final int codepoint;
	
	/**
	 * Initializes the decoded character.
	 *
	 * @param __cp The code point of the character.
	 * @since 2017/10/24
	 */
	public DecodedCharacter(int __cp)
	{
		this.codepoint = __cp;
	}
}

