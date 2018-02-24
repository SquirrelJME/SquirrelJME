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
 * Sections may have flags which are used to determine how the data may be
 * accessed, initialized, or executed.
 *
 * @since 2018/02/23
 */
public enum SectionFlag
{
	/** Readable. */
	READ,
	
	/** Writable. */
	WRITE,
	
	/** Excutable. */
	EXECUTABLE,
	
	/** Initialize to zero. */
	INITIALIZED_TO_ZERO,
	
	/** The section is purely virtual and does not exist in the file. */
	VIRTUAL,
	
	/** End. */
	;
}

