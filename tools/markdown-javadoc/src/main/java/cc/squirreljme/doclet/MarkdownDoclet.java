// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

import com.sun.javadoc.RootDoc;

/**
 * This contains the older standard Doclet for API generation.
 *
 * @since 2022/08/23
 */
public class MarkdownDoclet
	implements Runnable
{
	/**
	 * Initializes the doclet handler.
	 * 
	 * @param __rootDoc The root document to write into.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/23
	 */
	public MarkdownDoclet(RootDoc __rootDoc)
		throws NullPointerException
	{
		if (__rootDoc == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/23
	 */
	@Override
	public void run()
	{
		throw new Error("TODO");
	}
}
