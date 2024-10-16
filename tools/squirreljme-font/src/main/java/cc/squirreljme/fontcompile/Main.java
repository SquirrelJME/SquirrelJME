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
import cc.squirreljme.fontcompile.out.CompiledFont;
import cc.squirreljme.fontcompile.out.FontCompiler;
import cc.squirreljme.fontcompile.out.SqfWriter;
import cc.squirreljme.fontcompile.out.rc.SqfResourceWriter;
import cc.squirreljme.fontcompile.out.source.SqfSourceWriter;
import cc.squirreljme.fontcompile.out.struct.SqfFontStruct;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

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
				"Usage: [in.bdf|in.strike] [out.sqf|-|out.c|-.c]");
		
		// Writing to stdout?
		boolean stdout = "-".equals(__args[1]) ||
			"-.c".equals(__args[1]);
		
		// Writing C source code?
		boolean cSource = __args[1].endsWith(".c");
		
		// Input and output files
		Path inBase = Paths.get(__args[0]);
		Path outSqf = (stdout ? null : Paths.get(__args[1]));
		
		// Parse input font
		FontInfo inFont;
		if (Files.isDirectory(inBase))
			inFont = SfdFontInfo.parse(inBase);
		else
			inFont = BdfFontInfo.parse(inBase);
		
		// Compile the font
		Path temp = null;
		try
		{
			// Only create temporary file if not writing to stdout
			if (!stdout)
				temp = Files.createTempFile("font", ".sqf");
			
			// Setup output for compilation
			try (OutputStream out = (stdout ? System.out :
				Files.newOutputStream(temp,
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE)))
			{
				// Setup compiler
				FontCompiler compiler = new FontCompiler(inFont);
				
				// Perform compilation
				CompiledFont compiled = compiler.run();
				
				// Parse into structured binary format
				SqfFontStruct[] structs = SqfFontStruct.parse(compiled);
				
				// Write all the structs
				try (SqfWriter writer = (cSource ? new SqfSourceWriter(out) :
					 new SqfResourceWriter(out)))
				{
					writer.write(structs);
				}
				
				// Make sure it is flushed
				out.flush();
			}
			
			// Was a success, so move over
			if (!stdout)
			{
				// Make sure resultant directories exist
				Files.createDirectories(outSqf.getParent());
				
				// Move it over
				Files.move(temp, outSqf,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		finally
		{
			// Either failed or finished, in which case it gets deleted
			// if it is still lying around for some reason
			if (temp != null)
				Files.deleteIfExists(temp);
		}
	}
}
