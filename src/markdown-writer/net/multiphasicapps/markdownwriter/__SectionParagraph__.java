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

import java.io.IOException;

/**
 * This is for writing normal text that exists within a paragraph.
 *
 * @since 2016/10/02
 */
class __SectionParagraph__
	extends __Section__
{
	/** Just started the paragraph? */
	volatile boolean _juststarted =
		true;
	
	/**
	 * Initializes the paragraph.
	 *
	 * @param __mkd The owning writer.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/02
	 */
	__SectionParagraph__(MarkdownWriter __mdw)
		throws IOException
	{
		super(__mdw);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/02
	 */
	@Override
	void __process(char __c)
		throws IOException
	{
		// If the paragraph just started then add a spacing newline before
		// its content
		MarkdownWriter writer = this.writer;
		if (this._juststarted)
		{
			// Write line
			writer.__put('\n', true);
			
			// No longer started
			this._juststarted = false;
		}
		
		// If this is whitespace then do not print it on the first column ever
		if (__Section__.__isWhitespace(__c) && writer._column == 0)
			return;
		
		// Write character
		writer.__put(__c, true);
	}
}

