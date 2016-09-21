// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

import java.io.OutputStream;

/**
 * This factory is used to initialize native code writers.
 *
 * @since 2016/09/10
 */
public interface NativeCodeWriterFactory
{
	/**
	 * Creates the native code writer to be used for native code output.
	 *
	 * @param __n The options for the native code generator.
	 * @param __os The output streamm where instructions are written to.
	 * @return The native code writer for native code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	public abstract NativeCodeWriter create(NativeCodeWriterOptions __o,
		OutputStream __os)
		throws NullPointerException;
}

