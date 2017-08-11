// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.trans;

import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedByteCode;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This interface provides a service to locate and initialize translators.
 *
 * This class is used with the {@link ServiceLoader}.
 *
 * @since 2017/08/11
 */
public interface TranslatorService
{
	/**
	 * Creates a translator which outputs to the specified assembler.
	 *
	 * @param __o The output to translate to.
	 * @throws JITException If it could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public abstract ExpandedByteCode createTranslator(MachineCodeOutput __o)
		throws JITException, NullPointerException;
	
	/**
	 * Returns the name of this translator.
	 *
	 * @return The name of this translator.
	 * @since 2017/08/11
	 */
	public abstract String name();
}

