// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This contains the encoder for dual pools.
 *
 * @since 2019/07/17
 */
public final class DualPoolEncoder
{
	/**
	 * Not used.
	 *
	 * @since 2019/07/17
	 */
	private DualPoolEncoder()
	{
	}
	
	/**
	 * Encodes the dual pool to the given output stream and returns the
	 * result.
	 *
	 * @param __dp The dual-pool to encode.
	 * @param __out The stream to write to, if this is already a
	 * {@link DataOutputStream} then it will be used as such.
	 * @return The result with size information.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/17
	 */
	public static final DualPoolEncodeResult encode(
		DualClassRuntimePoolBuilder __dp, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__dp == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Use pre-existing data output or just wrap it?
		DataOutputStream dos = ((__out instanceof DataOutputStream) ?
			(DataOutputStream)__out : new DataOutputStream(__out));
		
		throw new todo.TODO();
	}
}

