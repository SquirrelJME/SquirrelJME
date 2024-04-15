// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
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
	 * @param __mdw The owning writer.
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
	void __endSection()
		throws IOException
	{
		// Make sure we do the starting space
		this.__justStarted();
		
		super.__endSection();
		
		// Add newline after the first column
		MarkdownWriter writer = this.writer;
		if (writer._column > 0)
			writer.__put('\n', true);
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
		this.__justStarted();
		
		// If this is whitespace then do not print it on the first column ever
		if (MarkdownWriter.__isWhitespace(__c) && writer._column == 0)
			return;
		
		// Write character
		if (__c > 0)
			writer.__put(__c, true);
	}
	
	/**
	 * Performs actions for when the paragraph was just started.
	 * 
	 * @throws IOException On write errors.
	 * @since 2022/08/30
	 */
	private void __justStarted()
		throws IOException
	{
		// If the paragraph just started then add a spacing newline before
		// its content
		MarkdownWriter writer = this.writer;
		if (this._juststarted)
		{
			// Add blank lines before section
			while (writer._zeroLines < 1) 
				writer.__put('\n', true);
			
			// No longer started
			this._juststarted = false;
		}
	}
}

