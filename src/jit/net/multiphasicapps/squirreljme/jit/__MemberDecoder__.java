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

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;

/**
 * This is the base class for members to decode.
 *
 * @since 2016/08/27
 */
abstract class __MemberDecoder__
	extends __HasAttributes__
{
	/** The output class writer. */
	protected final JITClassWriter classwriter;
	
	/** The input stream. */
	protected final DataInputStream input;
	
	/** The constant pool. */
	protected final JITConstantPool pool;
	
	/** The owning class flags. */
	protected final JITClassFlags classflags;
	
	/**
	 * Initializes the base decoder.
	 *
	 * @param __cw The class writer to write to.
	 * @param __di The data input stream for the class file.
	 * @param __pool The constant pool.
	 * @param __cf The owning class flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	__MemberDecoder__(JITClassWriter __cw, DataInputStream __di,
		JITConstantPool __pool, JITClassFlags __cf)
		throws NullPointerException
	{
		// Check
		if (__cw == null || __di == null || __pool == null || __cf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classwriter = __cw;
		this.input = __di;
		this.pool = __pool;
		this.classflags = __cf;
	}
	 
	/**
	 * Decodes the actual member data.
	 *
	 * @throws IOException On read errors.
	 * @since 2016/08/27
	 */
	abstract void __decode()
		throws IOException;
}

