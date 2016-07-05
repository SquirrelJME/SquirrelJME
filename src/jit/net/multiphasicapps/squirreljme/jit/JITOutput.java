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
	 * Places the result of the JIT directly into the given output stream in
	 * an implementation specific output form that is meant to be cached on
	 * the disk.
	 *
	 * @param __os The output stream where the cache goes.
	 * @throws IncompatibleOutputException If the output of the JIT does not
	 * support writing to a cached form on the disk.
	 * @since 2016/07/04
	 */
	public abstract void outputCache(OutputStream __os)
		throws IncompatibleOutputException;
	
	/**
	 * Places the result of JIT compilation into a form that is ready to be
	 * executed on the host system.
	 *
	 * @param __oe The future result of the compatible executable.
	 * @throws IncompatibleOutputException If the JIT cannot output to a ready
	 * to execute binary form.
	 * @since 2016/07/04
	 */
	public abstract void outputExecutable(CompatibleExecutable[] __oe)
		throws IncompatibleOutputException;
}

