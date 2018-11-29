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

import cc.squirreljme.runtime.lcdui.font.SQFFont;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.pcftosqf.pcf.PCFFont;

/**
 * This is the main entry point for the font conversion.
 *
 * @since 2018/11/27
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws Throwable On any exception.
	 * @since 2018/11/27
	 */
	public static final void main(String... __args)
		throws Throwable
	{
		byte[] sqf;
		try (InputStream in = Files.newInputStream(Paths.get(__args[0])))
		{
			// Read PCF Font
			PCFFont pcf = PCFFont.read(in);
			
			// Convert to SQF
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				// Convert to SQF
				new SQFConverter(pcf).convertTo(baos);
				
				// Use those bytes
				sqf = baos.toByteArray();
			}
			
			// Check that the font is valid
			try (ByteArrayInputStream bais = new ByteArrayInputStream(sqf))
			{
				SQFFont.read(bais);
			}
		}
		
		// Write output SQF
		System.out.write(sqf);
	}
}

