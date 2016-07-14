// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang.c;

import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.lang.LangNamespaceWriter;

/**
 * This is a namespace writer which targets the C programming language.
 *
 * @since 2016/07/09
 */
public class CLangNamespaceWriter
	extends LangNamespaceWriter
{
	/**
	 * The namespace writer for C code.
	 *
	 * @param __ns The namespace being written.
	 * @param __config The configuration used.
	 * @since 2016/07/09
	 */
	public CLangNamespaceWriter(String __ns,
		JITOutputConfig.Immutable __config)
	{
		super(__ns, __config);
	}
}

