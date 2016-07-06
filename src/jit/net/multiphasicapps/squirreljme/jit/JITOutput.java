// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This interface is defined by anything that wishes to output from the JIT
 * into an either ready to execute region of code and also potentially a cached
 * form for writing to the disk.
 *
 * @since 2016/07/04
 */
public interface JITOutput
{
	/**
	 * This method is called when the JIT wants to output a new class that is
	 * to get native code generated for it. It is system dependent how a given
	 * system represents classes (a blob of executable bytes, or a loadable
	 * library).
	 *
	 * @param __ns The namespace the class is in, this should be the name of
	 * the JAR file.
	 * @param __cn The name of the class being written.
	 * @return A writer which is capable of writing the given JIT compiled
	 * class.
	 * @throws JITException If the class could not be output.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public abstract JITClassWriter beginClass(String __ns,
		ClassNameSymbol __cn)
		throws JITException, NullPointerException;
}

