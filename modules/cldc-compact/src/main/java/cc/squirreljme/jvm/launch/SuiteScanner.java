// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.jvm.suite.SuiteUtils;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is a scanner which can read all the application groups that are
 * available.
 *
 * @since 2020/12/28
 */
public final class SuiteScanner
{
	/** The shelf to access. */
	protected final VirtualJarPackageShelf shelf;
	
	/** Allow parallel scanning? */
	protected final boolean parallel;
	
	/**
	 * Initializes the base suite scanner.
	 * 
	 * @param __parallel Allow parallel scanning?
	 * @since 2020/12/28
	 */
	public SuiteScanner(boolean __parallel)
	{
		this(__parallel, new DefaultJarPackageShelf());
	}
	
	/**
	 * Initializes the suite scanner.
	 *
	 * @param __parallel Allow parallel scanning?
	 * @param __shelf The shelf to initialize from.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public SuiteScanner(boolean __parallel, VirtualJarPackageShelf __shelf)
		throws NullPointerException
	{
		if (__shelf == null)
			throw new NullPointerException("NARG");
		
		this.shelf = __shelf;
		this.parallel = __parallel;
	}
	
	/**
	 * Scans all the available suites and returns information that is needed
	 * for them to properly launch.
	 * 
	 * @return The state of scanned suites.
	 * @since 2020/12/28
	 */
	public AvailableSuites scanSuites()
	{
		return this.scanSuites(null);
	}
	
	/**
	 * Scans all the available suites and returns information that is needed
	 * for them to properly launch.
	 * 
	 * @param __listener The listener for suites as they are scanned, used to
	 * indicate progress.
	 * @return The state of scanned suites.
	 * @since 2020/12/29
	 */
	public AvailableSuites scanSuites(SuiteScanListener __listener)
	{
		// Get all the available libraries
		JarPackageBracket[] jars = this.shelf.libraries();
		int numJars = jars.length;
		
		// Load the names of all JARs and map to brackets, this is later used
		// for i-mode lookup.
		Map<String, JarPackageBracket> nameToJar = new HashMap<>();
		synchronized (nameToJar)
		{
			for (JarPackageBracket jar : jars)
			{
				String name = this.shelf.libraryPath(jar);
				if (name != null)
				{
					// Full path
					nameToJar.put(name, jar);
					
					// Short path
					nameToJar.put(SuiteUtils.baseName(name), jar);
				}
			}
		}
		
		// Libraries are lazily handled on launching
		__Libraries__ libs = new __Libraries__();
		
		// Applications which should be loaded
		List<Application> result = new LinkedList<>();
		
		// How many CPUs are available? This is so we can split the suite
		// loading operations across multiple threads at once for faster
		// scanning... On SpringCoat scans can take a while, so we want to
		// make it as fast as we can...
		int numThreads;
		if (this.parallel)
			numThreads = Math.max(1, (int)RuntimeShelf.vmStatistic(
				VMStatisticType.CPU_THREAD_COUNT));
		else
			numThreads = 1;
		
		// Locate programs via the library path, single threaded, so we do not
		// need to anything more complex
		__SuiteScannerCounter__ jarIndexCount = new __SuiteScannerCounter__();
		if (numThreads <= 1)
			this.__loadStripe(__listener, jars, numJars, nameToJar,
				libs, result, 0, numJars, jarIndexCount);
		
		// Stripe loads to each CPU that is available
		else
		{
			// Counter for stripe load status
			__SuiteScannerCounter__ done = new __SuiteScannerCounter__();
			
			// Divide 
			int baseSplitLen = Math.max(1, numJars / numThreads);
			int actualSplits = 0;
			for (int at = 0, stripe = 0; at < numJars; at += baseSplitLen,
					stripe++)
			{
				// Ignore the first stripe, it is run in this thread
				if (stripe == 0)
					continue;
				
				// Make it actual!
				actualSplits++;
				
				// Setup thread and start it
				Thread thread = new Thread(new __SuiteScannerStripe__(done,
					__listener, jars, numJars, nameToJar, libs, result, at,
					Math.min(numJars, at + baseSplitLen), jarIndexCount,
					this),
					"SquirrelJMESuiteScanStripe-" + stripe);
				thread.start();
			}
			
			// Run our first stripe in this thread, so we do not waste it
			this.__loadStripe(__listener, jars, numJars, nameToJar,
				libs, result, 0, baseSplitLen, jarIndexCount);
			
			// Wait until everything is done
			for (;;)
				synchronized (done)
				{
					// Is everything done?
					if (done._count >= actualSplits)
						break;
					
					// Not everything was done, just wait around a bit
					try
					{
						done.wait(1000);
					}
					catch (InterruptedException ignored)
					{
					}
				}
		}
		
		// Finalize suite list
		synchronized (result)
		{
			return new AvailableSuites(this.shelf, libs,
				result.<Application>toArray(new Application[result.size()]));
		}
	}
	
	/**
	 * Loads a stripe of JARs.
	 * 
	 * @param __listener The listener used.
	 * @param __jars The jars to load.
	 * @param __numJars The number of maximum JARs.
	 * @param __nameToJar The name to JAR mapping.
	 * @param __libs The library cache.
	 * @param __result The output result.
	 * @param __startPos The start position to run the scan
	 * @param __endPos The end position, exclusive.
	 * @param __jarIndexCount The JAR index counter, for multi-threaded
	 * accuracy in the totals.
	 * @since 2022/10/03
	 */
	void __loadStripe(SuiteScanListener __listener,
		JarPackageBracket[] __jars, int __numJars,
		Map<String, JarPackageBracket> __nameToJar, __Libraries__ __libs,
		List<Application> __result, int __startPos, int __endPos,
		__SuiteScannerCounter__ __jarIndexCount)
	{
		// Process stripe
		for (int i = __startPos, end = Math.min(__endPos, __numJars);
			 i < end; i++)
		{
			JarPackageBracket jar = __jars[i];
			
			// Get an accurate count for this JAR, if there are invalid JARs
			// the numbers could potentially be skipped
			int accurateJarIndex;
			synchronized (__jarIndexCount)
			{
				accurateJarIndex = __jarIndexCount._count++;
			}
			
			// Ignore non-JARs
			String libPath = this.shelf.libraryPath(jar);
			if (!SuiteUtils.isJar(libPath))
				continue;
			
			// Debug
			Debugging.debugNote("Checking %s...", libPath);
			
			// Setup parser state
			ApplicationParserState state = new ApplicationParserState(
				__listener, __numJars, __libs, __nameToJar, __result,
				accurateJarIndex, jar, this.shelf);
			
			// Scan for all the application types accordingly
			for (ApplicationParser parser : ApplicationParser.values())
				parser.parse(state);
		}
	}
}

