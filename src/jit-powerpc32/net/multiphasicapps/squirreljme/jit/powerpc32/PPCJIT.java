// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.powerpc32;

import java.io.InputStream;
import net.multiphasicapps.squirreljme.jit.JIT;
import net.multiphasicapps.squirreljme.jit.JITFactory;

/**
 * This is a JIT which outputs 32-bit PowerPC machine code.
 *
 * @since 2016/07/02
 */
public class PPCJIT
	extends JIT
{
	/**
	 * Initializes the JIT.
	 *
	 * @param __fp The producer which generated this JIT.
	 * @apram __ns The namespace of the class.
	 * @param __ic The input stream of the class data.
	 * @since 2016/07/03
	 */
	public PPCJIT(JITFactory.Producer __fp, String __ns, InputStream __ic)
	{
		super(__fp, __ns, __ic);
	}
}

