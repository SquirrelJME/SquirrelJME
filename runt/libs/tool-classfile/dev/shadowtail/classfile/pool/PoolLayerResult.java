// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is the result of a constant pool layering on top of another.
 *
 * @since 2019/08/24
 */
public final class PoolLayerResult
{
	/**
	 * Writes one of the specified layers to the given stream.
	 *
	 * @param __runtime Is the run-time layer to be exported?
	 * @param __os The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/25
	 */
	public final void writeLayer(boolean __runtime, OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Layers the given pool onto the other.
	 *
	 * @param __pool The source pool.
	 * @param __onto The pool to layer on top of.
	 * @return The layered pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/24
	 */
	public static final PoolLayerResult layerPool(
		DualClassRuntimePoolBuilder __pool, DualClassRuntimePoolBuilder __onto)
		throws NullPointerException
	{
		if (__pool == null || __onto == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

