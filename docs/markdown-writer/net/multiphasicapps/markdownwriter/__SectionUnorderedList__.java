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
 * This is the unordered list section which is used to place non-numbered
 * bullet point lists.
 *
 * @since 2016/10/02
 */
class __SectionUnorderedList__
	extends __SectionList__
{
	/**
	 * Initializes the unordered list section.
	 *
	 * @param __mkd The owning writer.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/02
	 */
	__SectionUnorderedList__(MarkdownWriter __mdw)
		throws IOException
	{
		super(__mdw);
		
		// Single character is used
		this._cdepth = 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/02
	 */
	@Override
	void __listCharacters()
		throws IOException
	{
		this.writer.__put('*', true);
	}
}

