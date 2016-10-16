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
 * This is the base section for numerical values.
 *
 * @since 2016/10/02
 */
abstract class __SectionList__
	extends __Section__
{
	/** The current indentation level. */
	volatile int _indent;
	
	/** The character depth (characters used for the bullet). */
	volatile int _cdepth;
	
	/** Create new item entry? */
	volatile boolean _newitem =
		true;
	
	/**
	 * Initializes the base for lists.
	 *
	 * @param __mkd The owning writer.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/02
	 */
	__SectionList__(MarkdownWriter __mdw)
		throws IOException
	{
		super(__mdw);
		
		// If the section before this one is a list then
		__Section__ sectionbefore = this._sectionbefore;
		if (sectionbefore instanceof __SectionList__)
		{
			__SectionList__ was = (__SectionList__)sectionbefore;
			this._indent = was._indent + 1 + was._cdepth;
		}
		
		// Otherwise, start a new list
		else
			this._indent = 1;
	}
	
	/**
	 * Prints the list characters.
	 *
	 * @throws IOException On write errors.
	 * @since 2016/10/02
	 */
	abstract void __listCharacters()
		throws IOException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/02
	 */
	@Override
	void __process(char __c)
		throws IOException
	{
		// If writing a new item, write it
		MarkdownWriter writer = this.writer;
		if (this._newitem)
		{
			// Add new line
			writer.__put('\n', true);
			
			// Indent with spaces first
			int indent = this._indent;
			for (int i = 0; i < indent; i++)
				writer.__put(' ', true);
			
			// Add list characters
			__listCharacters();
			
			// Add sapce
			writer.__put(' ', true);
			
			// Do not write any more new items
			this._newitem = false;
		}
		
		// Indent?
		if (writer._column == 0)
		{
			int indent = this._indent + 1 + this._cdepth;
			for (int i = 0; i < indent; i++)
				writer.__put(' ', true);
		}
		
		// Write normal character
		if (__c > 0)
			writer.__put(__c, true);
	}
}

