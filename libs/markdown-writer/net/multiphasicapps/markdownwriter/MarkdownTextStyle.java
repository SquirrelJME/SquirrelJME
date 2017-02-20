// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.markdownwriter;

/**
 * This describes the type of text style that may be chosen for output.
 *
 * @since 2016/09/13
 */
public enum MarkdownTextStyle
{
	/** Normal style. */
	NORMAL(true),
	
	/** Strong. */
	STRONG(true),
	
	/** Emphasis. */
	EMPHASIS(true),
	
	/** Strong Emphasis. */
	STRONG_EMPHASIS(true),
	
	/** Source code. */
	CODE(false),
	
	/** End. */
	;
	
	/** Is this a normal text style? */
	protected final boolean normal;
	
	/**
	 * Initializes the style information.
	 *
	 * @param __n Is this style considered for non-code text.
	 * @since 2016/09/13
	 */
	private MarkdownTextStyle(boolean __n)
	{
		this.normal = __n;
	}
	
	/**
	 * Is this style for normal text?
	 *
	 * @return {@code true} if the style is for normal text.
	 * @since 2016/09/13
	 */
	public final boolean isNormal()
	{
		return this.normal;
	}
}

