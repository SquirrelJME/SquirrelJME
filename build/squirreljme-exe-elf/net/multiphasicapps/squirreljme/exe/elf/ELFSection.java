// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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

