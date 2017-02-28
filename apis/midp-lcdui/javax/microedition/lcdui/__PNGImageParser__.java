// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This class parses PNG images.
 *
 * @since 2017/02/28
 */
class __PNGImageParser__
{
	/** The input source. */
	protected final DataInputStream in;
	
	/**
	 * Initializes the PNG parser.
	 *
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	__PNGImageParser__(DataInputStream __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
	}
	
	/**
	 * Parses the PNG image data.
	 *
	 * @return The read image.
	 * @since 2017/02/28
	 */
	Image __parse()
		throws IOException
	{
		DataInputStream in = this.in;
		
		throw new todo.TODO();
	}
}

