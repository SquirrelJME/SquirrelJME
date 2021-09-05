// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.base;

import cc.squirreljme.jvm.summercoat.ld.pack.TableOfContents;

/**
 * Writer for {@link TableOfContents}.
 *
 * @since 2021/09/03
 */
public final class TableOfContentsWriter
{
	/** The number of entries per table of contents entry. */ 
	protected final int spanLength;
	
	/**
	 * Initializes the table of contents writer.
	 * 
	 * @param __numTocProperties The number of properties per individual
	 * table of content entries.
	 * @throws IllegalArgumentException If the specified amount is zero or
	 * negative.
	 * @since 2021/09/05
	 */
	public TableOfContentsWriter(int __numTocProperties)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AJ01 Zero or negative table of contents
		// entries specified.}
		if (__numTocProperties <= 0)
			throw new IllegalArgumentException("AJ01");
		
		this.spanLength = __numTocProperties;
	}
}
