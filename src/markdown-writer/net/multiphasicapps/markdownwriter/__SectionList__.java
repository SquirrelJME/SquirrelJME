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
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/02
	 */
	@Override
	void __process(char __c)
		throws IOException
	{
		throw new Error("TODO");
	}
}

