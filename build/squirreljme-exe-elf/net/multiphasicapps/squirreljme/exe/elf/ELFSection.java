// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

/**
 * These are sections which are used by the object linker and dumping
 * utilities, these are not required for execution.
 *
 * @since 2016/08/16
 */
public final class ELFSection
	extends __ELFBaseEntry__
{
	/**
	 * Initializes the section.
	 *
	 * @param __eo The owning ELF.
	 * @since 2016/08/16
	 */
	ELFSection(ELFOutput __eo)
	{
		super(__eo);
	}
}

