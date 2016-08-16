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

