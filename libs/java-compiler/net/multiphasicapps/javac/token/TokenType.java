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
	
	/** Keyword xxxxx. */
	KEYWORD_ABSTRACT,
	
	/** Keyword xxxxx. */
	KEYWORD_ASSERT,
	
	/** Keyword xxxxx. */
	KEYWORD_BOOLEAN,
	
	/** Keyword xxxxx. */
	KEYWORD_BREAK,
	
	/** Keyword xxxxx. */
	KEYWORD_BYTE,
	
	/** Keyword xxxxx. */
	KEYWORD_CASE,
	
	/** Keyword xxxxx. */
	KEYWORD_CATCH,
	
	/** Keyword xxxxx. */
	KEYWORD_CHAR,
	
	/** Keyword xxxxx. */
	KEYWORD_CLASS,
	
	/** Keyword xxxxx. */
	KEYWORD_CONTINUE,
	
	/** Keyword xxxxx. */
	KEYWORD_DEFAULT,
	
	/** Keyword xxxxx. */
	KEYWORD_DO,
	
	/** Keyword xxxxx. */
	KEYWORD_DOUBLE,
	
	/** Keyword xxxxx. */
	KEYWORD_ELSE,
	
	/** Keyword xxxxx. */
	KEYWORD_ENUM,
	
	/** Keyword xxxxx. */
	KEYWORD_EXTENDS,
	
	/** Keyword xxxxx. */
	KEYWORD_FINAL,
	
	/** Keyword xxxxx. */
	KEYWORD_FINALLY,
	
	/** Keyword xxxxx. */
	KEYWORD_FLOAT,
	
	/** Keyword xxxxx. */
	KEYWORD_FOR,
	
	/** Keyword xxxxx. */
	KEYWORD_IF,
	
	/** Keyword xxxxx. */
	KEYWORD_IMPLEMENTS,
	
	/** Keyword xxxxx. */
	KEYWORD_IMPORT,
	
	/** Keyword xxxxx. */
	KEYWORD_INSTANCEOF,
	
	/** Keyword xxxxx. */
	KEYWORD_INT,
	
	/** Keyword xxxxx. */
	KEYWORD_INTERFACE,
	
	/** Keyword xxxxx. */
	KEYWORD_LONG,
	
	/** Keyword xxxxx. */
	KEYWORD_NATIVE,
	
	/** Keyword xxxxx. */
	KEYWORD_NEW,
	
	/** Keyword xxxxx. */
	KEYWORD_PACKAGE,
	
	/** Keyword xxxxx. */
	KEYWORD_PRIVATE,
	
	/** Keyword xxxxx. */
	KEYWORD_PROTECTED,
	
	/** Keyword xxxxx. */
	KEYWORD_PUBLIC,
	
	/** Keyword xxxxx. */
	KEYWORD_RETURN,
	
	/** Keyword xxxxx. */
	KEYWORD_SHORT,
	
	/** Keyword xxxxx. */
	KEYWORD_STATIC,
	
	/** Keyword xxxxx. */
	KEYWORD_STRICTFP,
	
	/** Keyword xxxxx. */
	KEYWORD_SUPER,
	
	/** Keyword xxxxx. */
	KEYWORD_SWITCH,
	
	/** Keyword xxxxx. */
	KEYWORD_SYNCHRONIZED,
	
	/** Keyword xxxxx. */
	KEYWORD_THIS,
	
	/** Keyword xxxxx. */
	KEYWORD_THROW,
	
	/** Keyword xxxxx. */
	KEYWORD_THROWS,
	
	/** Keyword xxxxx. */
	KEYWORD_TRANSIENT,
	
	/** Keyword xxxxx. */
	KEYWORD_TRY,
	
	/** Keyword xxxxx. */
	KEYWORD_VOID,
	
	/** Keyword xxxxx. */
	KEYWORD_VOLATILE,
	
	/** Keyword xxxxx. */
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

