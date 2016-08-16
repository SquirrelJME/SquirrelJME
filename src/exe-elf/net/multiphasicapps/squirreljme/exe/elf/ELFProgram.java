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

import java.util.HashSet;
import java.util.Set;

/**
 * This is a single program in the program header which can be loaded or not
 * loaded, this is used by the actual system.
 *
 * @since 2016/08/16
 */
public final class ELFProgram
	extends __ELFBaseEntry__
{
	/** Flags for the header. */
	protected final Set<ELFProgramFlag> flags =
		new HashSet<>();
	
	/**
	 * Initializes the program header.
	 *
	 * @param __eo The owning ELF.
	 * @since 2016/08/16
	 */
	ELFProgram(ELFOutput __eo)
	{
		super(__eo);
	}
	
	/**
	 * Sets the flags for the program.
	 *
	 * @param __f The flags to set, if this is {@code null} or the first
	 * element is {@code null} they are cleared.
	 * @since 2016/08/16
	 */
	public void setFlags(ELFProgramFlag... __f)
	{
		// Lock
		synchronized (this.lock)
		{
			// Clear flags?
			Set<ELFProgramFlag> flags = this.flags;
			if (__f == null || __f[0] == null)
				flags.clear();
			
			// Set
			else
				for (ELFProgramFlag q : __f)
					flags.add(q);
		}
	}
}

