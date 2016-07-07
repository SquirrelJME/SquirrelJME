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
	 * This begins a namespace which may contain resources and classes. All
	 * classes and resources within a namespace are combined within a single
	 * unit and share the same data tables.
	 *
	 * @param __ns The namespace to output.
	 * @throws JITException If starting a namespace could not be performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public abstract JITNamespaceWriter beginNamespace(String __ns)
		throws JITException, NullPointerException;
}

