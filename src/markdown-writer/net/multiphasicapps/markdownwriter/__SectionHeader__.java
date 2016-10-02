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
	 * @param __mkd The owning writer.
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
		level = (__abs ? Math.min(1, __level) : Math.min(1, level + __level));
		this._level = level;
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
		writer.__put(__c, true);
	}
}

