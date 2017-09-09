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
	
	/** Package. */
	PACKAGE,
	
	/** Import statement. */
	IMPORT,
	
	/** Import wildcard. */
	IMPORT_WILDCARD,
	
	/** Declare an enumeration. */
	DECLARE_ENUM,
	
	/** Declare an interface. */
	DECLARE_INTERFACE,
	
	/** Class keyword. */
	KEYWORD_CLASS,
	
	/** New keyword. */
	KEYWORD_NEW,
	
	/** The default keyword. */
	KEYWORD_DEFAULT,
	
	/** Super keyword. */
	KEYWORD_SUPER,
	
	/** This keyword. */
	KEYWORD_THIS,
	
	/** Continue keyword. */
	KEYWORD_CONTINUE,
	
	/** For keyword. */
	KEYWORD_FOR,
	
	/** Switch keyword. */
	KEYWORD_SWITCH,
	
	/** Asset keyword. */
	KEYWORD_ASSERT,
	
	/** If keyword. */
	KEYWORD_IF,
	
	/** Do keyword. */
	KEYWORD_DO,
	
	/** Break keyword. */
	KEYWORD_BREAK,
	
	/** Throw keyword. */
	KEYWORD_THROW,
	
	/** Else keyword. */
	KEYWORD_ELSE,
	
	/** Return keyword. */
	KEYWORD_RETURN,
	
	/** Catch keyword. */
	KEYWORD_CATCH,
	
	/** Try keyword. */
	KEYWORD_TRY,
	
	/** Finally keyword. */
	KEYWORD_FINALLY,
	
	/** While keyword. */
	KEYWORD_WHILE,
	
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

