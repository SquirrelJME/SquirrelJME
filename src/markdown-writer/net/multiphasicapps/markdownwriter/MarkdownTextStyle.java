// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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
	NORMAL,
	
	/** Strong. */
	STRONG,
	
	/** Emphasis. */
	EMPHASIS,
	
	/** Strong Emphasis. */
	STRONG_EMPHASIS,
	
	/** Monospaced (code). */
	MONOSPACED,
	
	/** End. */
	;
}

