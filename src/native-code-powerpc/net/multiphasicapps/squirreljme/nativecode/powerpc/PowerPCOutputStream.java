// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.powerpc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;

/**
 * This is an output stream which writes PowerPC machine code.
 *
 * @since 2016/09/21
 */
public class PowerPCOutputStream
	extends ExtendedDataOutputStream
{
	/** The options used for the output. */
	protected final NativeCodeWriterOptions options;
	
	/**
	 * Initializes the machine code output stream.
	 *
	 * @param __o The options used for code generation.
	 * @param __os The output stream where bytes are written to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/21
	 */
	public PowerPCOutputStream(NativeCodeWriterOptions __o, OutputStream __os)
		throws NullPointerException
	{
		super(__os);
		
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.options = __o;
	}
}

