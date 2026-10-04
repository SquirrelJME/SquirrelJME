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
	 * @param __mdw The owning writer.
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

