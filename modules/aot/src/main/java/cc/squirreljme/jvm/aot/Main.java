// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.vm.JarClassLibrary;
import cc.squirreljme.vm.SummerCoatJarLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ServiceLoader;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * Main entry point for the AOT compilation backend.
 *
 * @since 2020/11/21
 */
public class Main
{
	/**
	 * Finds the given backend.
	 * 
	 * @param __compiler The compiler to select.
	 * @return The backend.
	 * @throws IllegalArgumentException If the backend is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/22
	 */
	public static Backend findBackend(String __compiler)
		throws IllegalArgumentException, NullPointerException
	{
		if (__compiler == null)
			throw new NullPointerException("NARG");
		
		// Find matching backend with the given name
		for (Backend backend : ServiceLoader.load(Backend.class))
			if (__compiler.equals(backend.name()))
				return backend;
		
		/* {@squirreljme.error AE04 The given compiler does not exist.
		(The compiler)} */
		throw new IllegalArgumentException("AE04 " + __compiler);
	}
	
	/**
	 * Main entry point for the compiler interface.
	 * 
	 * @param __args Arguments to the main class.
	 * @throws Throwable On any exception.
	 * @since 2023/10/14
	 */
	public static void main(String... __args)
		throws Throwable
	{
		try
		{
			Main.mainWrapped(__args);
		}
		catch (Throwable t)
		{
			t.printStackTrace(System.err);
			
			if (t instanceof Error)
				throw (Error)t;
			
			throw t;
		}
	}
	
	/**
	 * Main entry point for the compiler interface.
	 * 
	 * @param __args Arguments to the main class.
	 * @throws IOException On read errors.
	 * @since 2020/11/21
	 */
	public static void mainWrapped(String... __args)
		throws IOException
	{
		// Push all arguments to the queue
		Deque<String> args = new LinkedList<>(Arrays.asList(__args));
		
		// Selection commands
		String compiler = null;
		String name = "undefined";
		String mode = null;
		String clutterLevel = null;
		String sourceSet = null;
		String originalLibHash = null;
		
		// Parse input arguments
		while (!args.isEmpty())
		{
			String arg = args.removeFirst();
			
			// Compiler to select
			if (arg.startsWith("-Xcompiler:"))
				compiler = arg.substring("-Xcompiler:".length());
			
			// The name of the JAR being compiled
			else if (arg.startsWith("-Xname:"))
				name = arg.substring("-Xname:".length());
			
			// The current clutter level
			else if (arg.startsWith("-XclutterLevel"))
				clutterLevel = arg.substring("-XclutterLevel:".length());
			
			// The source set being compiled
			else if (arg.startsWith("-XsourceSet"))
				sourceSet = arg.substring("-XsourceSet:".length());
			
			// Original library hash code
			else if (arg.startsWith("-XoriginalLibHash:"))
				originalLibHash = arg.substring("-XoriginalLibHash:".length());
			
			// End of switches
			else if (!arg.startsWith("-"))
			{
				mode = arg;
				break;
			}
			
			/* {@squirreljme.error AE01 Unknown argument. (The argument)} */
			else
				throw new IllegalArgumentException("AE01 " + arg);
		}
		
		/* {@squirreljme.error AE03 Mode was not specified.} */
		if (mode == null)
			throw new IllegalArgumentException("AE03");
		
		// Find the backend to use
		Backend backend = Main.findBackend(compiler);
		
		// Store into settings
		AOTSettings aotSettings = new AOTSettings(compiler,
			name, mode, sourceSet, clutterLevel, originalLibHash);
		
		// Use explicit input/output
		try (InputStream in = new StandardInputStream();
			OutputStream out = System.out)
		{
			// Which mode should occur?
			switch (mode)
			{
					// Compile code
				case "compile":
					Main.mainCompile(aotSettings, backend, in, out, args);
					break;
					
					// Dump the result of "compile"
				case "dumpCompile":
					Main.dumpCompile(aotSettings, backend, in, out);
					break;
					
					// Link multiple libraries into one
				case "rom":
					Main.mainRom(aotSettings, backend, out, args);
					break;
				
				/* {@squirreljme.error AE02 Unknown mode. (The mode)} */
				default:
					throw new IllegalArgumentException("AE02 " + mode);
			}
		}
	}
	
	/**
	 * Dumps the result of the compilation to a readable text format used
	 * for debugging.
	 *
	 * @param __aotSettings AOT settings.
	 * @param __backend The backend to use.
	 * @param __inGlob The input glob.
	 * @param __out Where to write the output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/16
	 */
	public static void dumpCompile(AOTSettings __aotSettings,
		Backend __backend, InputStream __inGlob,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__backend == null || __inGlob == null || __out == null ||
			__aotSettings == null)
			throw new NullPointerException("NARG");
		
		// Read in the entire contents of the data
		byte[] dump;
		try (InputStream in = __inGlob)
		{
			dump = StreamUtils.readAll(in);
		}
		
		// Dump the output
		try (PrintStream out = new PrintStream(__out, true))
		{
			__backend.dumpGlob(__aotSettings, dump, out);
		}
	}
	
	/**
	 * Handles the main compilation stage.
	 *
	 * @param __aotSettings AOT settings.
	 * @param __backend The backend to use.
	 * @param __inZip The input stream of the input ZIP.
	 * @param __outGlob The output stream of the Glob.
	 * @param __args The arguments to use.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/22
	 */
	public static void mainCompile(AOTSettings __aotSettings,
		Backend __backend, InputStream __inZip,
		OutputStream __outGlob, Deque<String> __args)
		throws IOException, NullPointerException
	{
		if (__backend == null || __aotSettings == null || __args == null ||
			__inZip == null || __outGlob == null)
			throw new NullPointerException("NARG");
		
		// Parse compilation arguments
		CompileSettings settings = CompileSettings.parse(__args);
		
		// Setup glob for final linking
		try (LinkGlob glob = __backend.linkGlob(__aotSettings, settings,
			__outGlob))
		{
			// Starting linking
			glob.initialize();
			
			// Read input JAR and perform inline compilation
			try (InputStream in = __inZip;
				 ZipStreamReader zip = new ZipStreamReader(in))
			{
				// Process JAR entries and compile them into individual class
				// fragments
				for (;;)
					try (ZipStreamEntry entry = zip.nextEntry())
					{
						// No more entries to process
						if (entry == null)
							break;
						
						// Ignore directories
						String name = entry.name();
						if (name.endsWith("/"))
							continue;
						
						// Compile resource file?
						if (!Main.__isValidClass(name))
						{
							// If not the manifest, do not keep a copy of it
							InputStream actual;
							boolean isManifest =
								name.equals("META-INF/MANIFEST.MF");
							boolean isTestList =
								name.equals("META-INF/services/net." +
									"multiphasicapps.tac.TestInterface");
							if (!isManifest && !isTestList)
								actual = entry;
							
							// Otherwise do make a copy of it
							else
							{
								// Load this into the glob, if it cares
								byte[] data = StreamUtils.readAll(entry);
								try (InputStream rawIn =
									 new ByteArrayInputStream(data))
								{
									if (isManifest)
										glob.rememberManifest(
											new JavaManifest(rawIn));
									else
										glob.rememberTests(
											StreamUtils.readAllLines(rawIn,
												"utf-8"));
								}
								
								// Use this for the resource read
								actual = new ByteArrayInputStream(data);
							}
							
							// Build the resource
							__backend.compileResource(settings, glob, name,
								actual);
						}
						
						// Compile class file?
						else
							__backend.compileClass(settings, glob,
								name.substring(0,
									name.length() - ".class".length()),
								entry);
					}
			}
			
			// Linking stage is finished
			glob.finish();
		}
	}
	
	/**
	 * Links the ROM together as one.
	 *
	 * @param __aotSettings AOT settings.
	 * @param __backend The backend to use.
	 * @param __out Where the resultant ROM is to be written.
	 * @param __args The arguments to the ROM linking.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/27
	 */
	public static void mainRom(AOTSettings __aotSettings, Backend __backend,
		OutputStream __out, Deque<String> __args)
		throws IOException, NullPointerException
	{
		if (__backend == null || __args == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Parse rom arguments
		RomSettings settings = RomSettings.parse(__args);
		
		// Load all libraries
		Collection<VMClassLibrary> libs = new LinkedList<>();
		while (!__args.isEmpty())
		{
			String arg = __args.removeFirst();
			
			// Determine the correct kind of library to load
			VMClassLibrary lib;
			if (SummerCoatJarLibrary.isSqc(arg))
				lib = new SummerCoatJarLibrary(Paths.get(arg));
			else
				lib = JarClassLibrary.of(Paths.get(arg));
			
			libs.add(lib);
		}
		
		/* {@squirreljme.error AE08 No libraries specified to link together.} */
		if (libs.isEmpty())
			throw new IllegalArgumentException("AE08");
		
		// Extra arrays accordingly
		VMClassLibrary[] vmLibs = libs.toArray(
			new VMClassLibrary[libs.size()]);
		
		// Perform combined linking
		__backend.rom(__aotSettings, settings, __out, vmLibs);
	}
	
	/**
	 * Is this a valid class?
	 * 
	 * @param __name The file name.
	 * @return If this is a valid class.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	private static boolean __isValidClass(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// If not a class file, ignore
		if (!__name.endsWith(".class"))
			return false;
		
		// Check for invalid characters
		for (int i = 0, n = __name.length() - 6; i < n; i++)
		{
			char c = __name.charAt(i);
			
			// Only these are invalid
			if (c == '-' || c == '.' || c == ';' || c == '[')
				return false;
		}
		
		// No failure, so is a valid class
		return true;
	}
}
