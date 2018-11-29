// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf;

import java.io.OutputStream;
import java.io.IOException;
import net.multiphasicapps.pcftosqf.pcf.PCFFont;

/**
 * Converts to SQF format from another font.
 *
 * @since 2018/11/27
 */
public class SQFConverter
{
	/** The input PCF font. */
	protected final PCFFont pcf;
	
	/**
	 * Initializes the converter
	 *
	 * @param __pcf The PCF font to convert.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public SQFConverter(PCFFont __pcf)
		throws NullPointerException
	{
		if (__pcf == null)
			throw new NullPointerException("NARG");
		
		this.pcf = __pcf;
	}
	
	/**
	 * Converts the font to SQF.
	 *
	 * @param __os The output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public final void convertTo(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

