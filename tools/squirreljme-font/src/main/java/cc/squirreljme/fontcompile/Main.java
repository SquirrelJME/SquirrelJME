// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile;

import cc.squirreljme.emulator.NativeBinding;
import cc.squirreljme.fontcompile.in.FontInfo;
import cc.squirreljme.fontcompile.in.bdf.BdfFontInfo;
import cc.squirreljme.fontcompile.in.sfdir.SfdFontInfo;
import cc.squirreljme.fontcompile.out.FontCompiler;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * Main entry point for the font compiler.
 *
 * @since 2024/05/17
 */
public class Main
{
	static
	{
		// We need to poke native binding, so it loads our emulation backend
		NativeBinding.loadedLibraryPath();
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Input arguments.
	 * @throws IOException On read/write errors.
	 * @since 2024/05/17
	 */
	public static void main(String... __args)
		throws IOException
	{
		if (__args == null || __args.length != 2 ||
			__args[0] == null || __args[1] == null)
			throw new IllegalArgumentException(
				"Usage: [in.bdf/in.strike] [out.sqf]");
		
		// Input and output files
		Path inBase = Paths.get(__args[0]);
		Path outSqf = Paths.get(__args[1]);
		
		// Parse input font
		FontInfo inFont;
		if (Files.isDirectory(inBase))
			inFont = SfdFontInfo.parse(inBase);
		else
			inFont = BdfFontInfo.parse(inBase);
		
		// Compile the font
		Path temp = Files.createTempFile("font", ".sqf");
		try
		{
			// Setup output for compilation
			try (OutputStream out = Files.newOutputStream(temp,
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE))
			{
				// Setup compiler
				FontCompiler compiler = new FontCompiler(inFont, out);
				
				// Perform compilation
				compiler.run();
			}
			
			// Was a success, so move over
			Files.move(temp, outSqf, StandardCopyOption.REPLACE_EXISTING);
		}
		finally
		{
			// Either failed or finished, in which case it gets deleted
			// if it is still lying around for some reason
			Files.deleteIfExists(temp);
		}
	}
}
