// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.base;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This interface describes a namespace output which writes a single namespace
 * to a single output file.
 *
 * @since 2016/09/28
 */
@Deprecated
public interface JITNamespaceOutputSingle
	extends JITNamespaceOutput
{
	/**
	 * This returns an output stream which is used to write the namespace
	 * binary cache which stores an executable result.
	 *
	 * @param __n The name of the namespace cache to write.
	 * @return An output stream to the executable cache.
	 * @throws IOException On write errors.
	 * @throws JITException If it could not be created for another reason.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	public abstract OutputStream outputSingle(String __n)
		throws IOException, JITException, NullPointerException;
}

