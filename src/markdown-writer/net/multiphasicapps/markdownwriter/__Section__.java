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

import java.util.Deque;

/**
 * This is the base class for all sections within the markdown writer, this is
 * used to modify how data is written to the output file when it is written.
 *
 * The constructor for a section will automatically set itself as the newest
 * section and initialize it from a previous one.
 *
 * @since 2016/10/02
 */
abstract class __Section__
{
	/** The target writer. */
	protected final MarkdownWriter writer;
	
	/** The section that was at the top. */
	protected final __Section__ sectionbefore;
	
	/** The current text style to use. */
	volatile MarkdownTextStyle _style =
		MarkdownTextStyle.NORMAL;
	
	/**
	 * Initializes a new section.
	 *
	 * @param __mdw The writer where characters are placed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/02
	 */
	__Section__(MarkdownWriter __mdw)
		throws NullPointerException
	{
		// Check
		if (__mdw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.writer = __mdw;
		
		// Is there anything above this?
		__Section__ sectionbefore = __mdw._section;
		this.sectionbefore = sectionbefore;
	}
}

