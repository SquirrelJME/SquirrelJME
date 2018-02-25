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
 * Names for common sections.
 *
 * @since 2018/02/24
 */
public interface SectionNames
{
	/** Static class information. */
	public static final String CLASSES =
		".squirreljme.classes";
	
	/** Table of class pointers which are implemented by classes. */
	public static final String IMPLEMENTED =
		".squirreljme.implemented";
}

