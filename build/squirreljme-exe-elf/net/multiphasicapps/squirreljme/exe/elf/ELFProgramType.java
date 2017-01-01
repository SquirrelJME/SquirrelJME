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
 * This represents a program type that may be set within a program header
 * within the ELF.
 *
 * @since 2016/08/16
 */
public interface ELFProgramType
{
	/**
	 * Returns the identifier for this type.
	 *
	 * @return The identifier.
	 * @since 2016/08/16
	 */
	public abstract int identifier();
}

