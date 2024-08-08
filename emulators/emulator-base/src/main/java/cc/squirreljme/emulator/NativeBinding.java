// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * This class manages the native bindings.
 *
 * @since 2020/02/25
 */
public final class NativeBinding
{
	/** Property for pre-loading libraries. */
	public static final String LIB_PRELOAD =
		"squirreljme.emulator.libpath";
	
	/** The path where the library is. */
	private static volatile Path loadedLibPath;
	
	/** The path where temporary libraries go. */
	private static volatile Path tempLibPath;
	
	static
	{
		// Try to set some properties for macOS, although it might not work if
		// too late
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
			"SquirrelJME");
		
		// If there is the Taskbar class if on Java 9 and up, we can set
		// some properties accordingly
		try
		{
			Class<?> taskbarClass = Class.forName("java.awt.Taskbar");
			
			// Get the current taskbar
			Method getTaskbarMethod = taskbarClass.getMethod(
				"getTaskbar");
			Object taskbar = getTaskbarMethod.invoke(null);
			
			// Set icon for SquirrelJME
			try (InputStream in = NativeBinding.class.getResourceAsStream(
				"icon.png"))
			{
				if (in != null)
				{
					Image icon = ImageIO.read(in);
					
					Method setIconImageMethod = taskbarClass.getMethod(
						"setIconImage", Image.class);
					setIconImageMethod.invoke(taskbar, icon);
				}
			}
		}
		catch (IOException|ReflectiveOperationException|SecurityException|
			UnsupportedOperationException ignored)
		{
		}
		
		// Load main library
		long loadNs = System.nanoTime();
		try
		{
			// Try to use a preloaded library, otherwise load it in
			Path libFile = NativeBinding.__checkPreload();
			if (libFile == null)
				libFile = NativeBinding.libFromResources(
					"emulator-base", true);
			
			// Store for later
			NativeBinding.loadedLibPath = libFile;
				
			// Debug
			System.err.printf("Java Version: %s%n",
				System.getProperty("java.version"));
			System.err.printf("Java Over-Layer: Loading %s...%n", libFile);
			
			// Try loading the library now
			System.load(libFile.toString());
			
			// Debug
			System.err.printf("Java Over-Layer: Binding methods...%n");
			
			// Bind methods
			if (NativeBinding.__bindMethods() != 0)
				throw new RuntimeException("Could not bind methods!");
			
			// Debug
			System.err.printf("Java Over-Layer: Methods bound!%n");
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not load library.", e);
		}
		
		// Track execution time
		finally
		{
			System.err.printf("Java Over-Layer: Loading took %dms%n",
				(System.nanoTime() - loadNs) / 1_000_000L);
		}
	}
	
	/**
	 * Binds methods accordingly.
	 *
	 * @since 2020/02/26
	 */
	private static native int __bindMethods();
	
	/**
	 * Tries to load the library from resources.
	 *
	 * @param __libBaseName The library base name.
	 * @param __map Should the name be mapped?
	 * @return The loaded library.
	 * @throws IOException On read/write errors.
	 * @since 2020/12/01
	 */
	public static Path libFromResources(String __libBaseName, boolean __map)
		throws IOException
	{
		// Find the library to load
		String libName;
		if (__map)
			libName = System.mapLibraryName(__libBaseName);
		else
			libName = __libBaseName;
		
		// Debug
		System.err.printf("Java Over-Layer: Locating %s...%n", libName);
		
		// Timing for extraction
		long startNs = System.nanoTime();
		
		// Copy resource to the output
		Path tempDir = null;
		Path libFile = null;
		try (InputStream in = NativeBinding.class.
			getResourceAsStream("/" + libName))
		{
			if (in == null)
				throw new IOException(String.format(
					"Library %s not found in resource.", libName));
			
			// Place all the native libraries in the same location
			tempDir = NativeBinding.tempLibPath;
			if (tempDir == null)
			{
				tempDir = Files.createTempDirectory("squirreljme-lib");
				NativeBinding.tempLibPath = tempDir;
			}
			
			// Store the library as the given file
			libFile = tempDir.resolve(libName);
			
			// Debug
			System.err.printf("Java Over-Layer: Extracting %s...%n",
				libName);
			
			// Write to the disk as we can only load there
			try (OutputStream out = Files.newOutputStream(libFile,
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE))
			{
				// Store here
				byte[] buf = new byte[262144];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					out.write(buf, 0, rc);
				}
				
				// Make sure it is on the disk
				out.flush();
			}
			
			// Attempt cleanup at shutdown.
			Runtime.getRuntime().addShutdownHook(
				new PathCleanup(libFile, tempDir));
		
			// Debug
			System.err.printf("Java Over-Layer: Extracted to %s...%n",
				libFile);
			
			// Use this path
			return libFile;
		}
		catch (IOException e)
		{
			// Try to clear the file
			try
			{
				if (libFile != null)
					Files.delete(libFile);
				
				if (tempDir != null)
					Files.delete(tempDir);
			}
			catch (IOException f)
			{
				e.addSuppressed(f);
			}
			
			throw new IOException("Could not copy native library.", e);
		}
		
		// Track execution time
		finally
		{
			System.err.printf("Java Over-Layer: Extraction took %dms%n",
				(System.nanoTime() - startNs) / 1_000_000L);
		}
	}
	
	/**
	 * Returns the loaded library path, if it does exist.
	 *
	 * @return The path to the library if it exists, or {@code null} if not.
	 * @since 2023/12/03
	 */
	public static Path loadedLibraryPath()
	{
		return NativeBinding.loadedLibPath;
	}
	
	/**
	 * Main entry point for the hosted emulator.
	 * 
	 * @param __args The program arguments.
	 * @throws Throwable On any exception.
	 * @since 2022/09/07
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Force this to be initialized
		new NativeBinding();
		
		// Extract main method to call
		String targetMain = __args[0];
		String[] targetArgs =
			Arrays.copyOfRange(__args, 1, __args.length);
		
		// Call main
		ReflectionShelf.invokeMain(TypeShelf.findType(targetMain), targetArgs);
	}
	
	/**
	 * Checks to see if the preloaded library is available.
	 * 
	 * @return The path to the library or {@code null} if not preloaded.
	 * @since 2020/12/01
	 */
	private static Path __checkPreload()
	{
		String libProp = System.getProperty(NativeBinding.LIB_PRELOAD);
		if (libProp == null)
			return null;
		
		Path path = Paths.get(libProp);
		if (Files.exists(path))
			return path;
		return null;
	}
}
