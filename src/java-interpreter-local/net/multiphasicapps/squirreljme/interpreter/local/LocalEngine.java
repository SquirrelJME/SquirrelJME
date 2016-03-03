// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter.local;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.squirreljme.interpreter.InterpreterClass;
import net.multiphasicapps.squirreljme.interpreter.InterpreterEngine;
import net.multiphasicapps.squirreljme.interpreter.InterpreterMethod;
import net.multiphasicapps.squirreljme.interpreter.InterpreterObject;
import net.multiphasicapps.squirreljme.interpreter.InterpreterThread;
import net.multiphasicapps.squirreljme.zips.StandardZIPFile;

/**
 * This is an extension of the interpreter engine which provides access to
 * JAR files for loading of classes.
 *
 * @since 2016/03/01
 */
public class LocalEngine
	extends InterpreterEngine
{
	/** The bootstrap class path. */
	protected final Set<Path> bootclasspath;
	
	/** The standard class path. */
	protected final Set<Path> classpath;
	
	/** The mapping of ZIP files. */
	private final Map<Path, StandardZIPFile> _zips =
		new HashMap<>();
	
	/**
	 * This initializes the local interpreter engine.
	 *
	 * @param __bcp The boot class path.
	 * @param __cp The standard class path.
	 * @param __main The main entry class.
	 * @param __args Program arguments to pass to main.
	 * @throws IllegalArgumentException If there are no bootstrap classpath
	 * paths available.
	 * @since 2016/03/01
	 */
	public LocalEngine(Set<Path> __bcp, Set<Path> __cp, String __main,
		String... __args)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__bcp == null || __cp == null || __main == null)
			throw new NullPointerException();
		
		// Force arguments to exist
		if (__args == null)
			__args = new String[0];
		
		// Copy classpaths
		bootclasspath = MissingCollections.<Path>unmodifiableSet(
			new LinkedHashSet<>(__bcp));
		classpath = MissingCollections.<Path>unmodifiableSet(
			new LinkedHashSet<>(__cp));
		
		// Need bootstrap classes
		if (bootclasspath.isEmpty())
			throw new IllegalArgumentException("No bootstrap classes.");
		
		// Find the main class
		InterpreterClass mainclass = loadClass(__main);
		if (mainclass == null)
			throw new IllegalArgumentException(String.format("The class " +
				"'%s' does not exist.", __main));
		
		// Find the main method
		InterpreterMethod mainmethod = mainclass.getMethod("main",
			"([Ljava/lang/String;)V");
		if (mainmethod == null || !mainmethod.isStatic() ||
			!mainmethod.isPublic())
			throw new IllegalArgumentException(String.format("The class " +
				"'%s' does not have method " +
				"'public static void main(String... foo)'."));
		
		// Create arguments for the main thread
		InterpreterObject pargs = spawnStringArray(__args);
		
		// Create main thread
		InterpreterThread mthread = createThread(mainmethod, pargs);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/02
	 */
	@Override
	public InputStream getResourceAsStream(String __res)
	{
		// Go through the classpaths to find the given resource
		for (int i = 0; i < 2; i++)
			for (Path base : (i == 0 ? bootclasspath : classpath))
			{
				// Get ZIP file
				StandardZIPFile szf = __getZip(base);
				
				// If not a ZIP then treat the file as a directory
				if (szf == null)
				{
					// If it is not a directory then it cannot be treated as
					// one
					if (!Files.isDirectory(base))
						return null;
					
					throw new Error("TODO");
				}
				
				// Otherwise read from the ZIP itself
				else
					throw new Error("TODO");
			}
		
		// Not found
		return null;
	}
	
	/**
	 * Attempts to open and cache the given path as a ZIP file.
	 *
	 * @param __p The path to open as a ZIP file.
	 * @return The ZIP file or {@code null} if it could not be opened as one.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/02
	 */
	private StandardZIPFile __getZip(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException();
		
		// Lock on the ZIP map
		synchronized (_zips)
		{
			// Is a ZIP already open for this?
			if (_zips.containsKey(__p))
				return _zips.get(__p);
			
			// Open file
			FileChannel fc = null;
			try
			{
				// Open file first
				fc = FileChannel.open(__p, StandardOpenOption.READ);
				
				// Otherwise try to load one
				StandardZIPFile szf = StandardZIPFile.open(fc);
				
				// It worked, so put it in and return it
				_zips.put(__p, szf);
				return szf;
			}
			
			// Failed to open
			catch (IOException ioe)
			{
				// Either does not exist or is not a (valid) ZIP
				_zips.put(__p, null);
				
				// Close the channel
				if (fc != null)
					try
					{
						fc.close();
					}
					
					// Failed to close
					catch (IOException ioeb)
					{
						RuntimeException toss = new RuntimeException(ioeb);
						toss.addSuppressed(ioe);
						throw toss;
					}
				
				// Print trace
				System.err.println("Failed to read the ZIP:");
				System.err.println(ioe);
				ioe.printStackTrace(System.err);
				
				// Nothing read
				return null;
			}
		}
	}
}

