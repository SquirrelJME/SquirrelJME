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
 * This section is used for the printing of headers.
 *
 * @since 2016/10/02
 */
class __SectionHeader__
	extends __Section__
{
	/**
	 * Initializes the section header.
	 *
	 * @param __mdw The owning writer.
	 * @param __abs Absolutely set the specified level if {@code true},
	 * otherwise level adjustment will be relative.
	 * @param __level The level to set to if absolute or relative from a
	 * previous level.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/02
	 */
	__SectionHeader__(MarkdownWriter __mdw, boolean __abs, int __level)
		throws IOException
	{
		super(__mdw);
		
		// Modify level
		int level = this._level;
		level = (__abs ? Math.max(1, __level) : Math.max(1, level + __level));
		this._level = level;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/02
	 */
	@Override
	void __endSection()
		throws IOException
	{
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
		// If on column zero, add the header bits
		MarkdownWriter writer = this.writer;
		int level = this._level;
		if (writer._column == 0)
		{
			for (int i = 0; i < level; i++)
				writer.__put('#', true);
			writer.__put(' ', true);
		}
		
		// Add character
		if (__c > 0)
			writer.__put(__c, true);
	}
}

