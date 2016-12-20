// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.inflate;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is used to output decompressed data to the specified output stream.
 *
 * @since 2016/12/20
 */
public class InflateOutputStream
	extends OutputStream
{
	/** Output stream to write to. */
	protected final OutputStream out;
	
	public InflateOutputStream(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __os;
	}
}

