// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.interpreter;

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.callbacks.ReflectiveLoaderCallback;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Base class for any interpreter implementation.
 *
 * @since 2022/09/08
 */
public abstract class AotInterpreter
	implements ReflectiveLoaderCallback
{
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/30
	 */
	@Override
	public final byte[] classBytes(String __binaryName)
		throws MLECallError
	{
		// {@squirreljme.error RI01 Invalid binary name.}
		if (__binaryName == null || __binaryName.length() <= 0 ||
			__binaryName.charAt(0) == '[')
			throw new MLECallError("RI01");
		
		throw Debugging.todo();
	}
	
	/**
	 * Installs the interpreter.
	 * 
	 * @since 2022/09/11
	 */
	public final void install()
	{
		// Just register self accordingly
		ReflectionShelf.registerLoader(this);
	}
}
