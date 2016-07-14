// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This class is used to write classes and resources that exist within a given
 * namespace (a JAR) so that loading and execution is performed as a single
 * unit. This increases efficiency as classes within a namespace can share
 * string constants and can individually be loaded for suites without requiring
 * other namespaces be present in the memory space.
 *
 * @since 2016/07/06
 */
public interface JITNamespaceWriter
	extends AutoCloseable
{
	/**
	 * This method is called when the JIT wants to output a new class that is
	 * to get native code generated for it. It is system dependent how a given
	 * system represents classes (a blob of executable bytes, or a loadable
	 * library).
	 *
	 * @param __cn The name of the class being written.
	 * @return A writer which is capable of writing the given JIT compiled
	 * class.
	 * @throws JITException If the class could not be output.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public abstract JITClassWriter beginClass(ClassNameSymbol __cn)
		throws JITException, NullPointerException;
	
	/**
	 * This method is called when the JIT or other system wants to create a
	 * resource in the current namespace so that resource data is included
	 * in the executable. Resources are streams which are accessible via
	 * {@link Class#getResourceAsStream(String)}.
	 *
	 * @param __name The name of the resource.
	 * @return An output stream for writing the raw resource data to.
	 * @throws JITException If the resource could not be written to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public abstract OutputStream beginResource(String __name)
		throws JITException, NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public abstract void close()
		throws JITException;
}

