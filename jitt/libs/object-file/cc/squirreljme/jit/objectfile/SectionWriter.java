// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

/**
 * This class is used to write data to sections.
 *
 * @since 2018/02/24
 */
public final class SectionWriter
{
	/** The section to write to. */
	protected final Section section;
	
	/** Properties of the data being written. */
	protected final DataProperties dataproperties;
	
	/**
	 * Initializes the section writer.
	 *
	 * @param __s The section to write to.
	 * @param __dp The properties of the data to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	SectionWriter(Section __s, DataProperties __dp)
		throws NullPointerException
	{
		if (__s == null || __dp == null)
			throw new NullPointerException("NARG");
		
		this.section = __s;
		this.dataproperties = __dp;
	}
}

