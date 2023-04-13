// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.suite.EntryPoint;
import cc.squirreljme.jvm.suite.EntryPoints;
import cc.squirreljme.jvm.suite.InvalidSuiteException;
import cc.squirreljme.jvm.suite.SuiteInfo;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	/** Data resource name. */
	private static final String _DATA_RESOURCE =
		"$DATA$";
	
	/**
	 * Not used.
	 * 
	 * @since 2020/12/28
	 */
	private SuiteScanner()
	{
	}
	
	/**
	 * Scans all the available suites and returns information that is needed
	 * for them to properly launch.
	 * 
	 * @return The state of scanned suites.
	 * @since 2020/12/28
	 */
	public static AvailableSuites scanSuites()
	{
		return SuiteScanner.scanSuites(null);
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
	public static AvailableSuites scanSuites(SuiteScanListener __listener)
	{
		// Get all the available libraries
		JarPackageBracket[] jars = JarPackageShelf.libraries();
		int numJars = jars.length;
		
		// Load the names of all JARs and map to brackets, this is later used
		// for i-mode lookup.
		Map<String, JarPackageBracket> nameToJar = new HashMap<>();
		synchronized (nameToJar)
		{
			for (JarPackageBracket jar : jars)
			{
				String name = JarPackageShelf.libraryPath(jar);
				if (name != null)
					nameToJar.put(name, jar);
			}
		}
		
		// Libraries are lazily handled on launching
		__Libraries__ libs = new __Libraries__();
		
		// Applications which should be loaded
		List<Application> result = new LinkedList<>();
		
		// How many CPUs are available? This is so we can split the suite
		// loading operations across multiple threads at once for faster
		// scanning... On SpringCoat scans can take awhile so we want to
		// make it as fast as we can...
		int numThreads = Math.max(1,
			(int)RuntimeShelf.vmStatistic(VMStatisticType.CPU_THREAD_COUNT));
		
		// Locate programs via the library path, single threaded, so we do not
		// need to anything more complex
		__SuiteScannerCounter__ jarIndexCount = new __SuiteScannerCounter__();
		if (numThreads <= 1)
			SuiteScanner.__loadStripe(__listener, jars, numJars, nameToJar,
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
					Math.min(numJars, at + baseSplitLen), jarIndexCount),
					"SquirrelJMESuiteScanStripe-" + stripe);
				thread.start();
			}
			
			// Run our first stripe in this thread, so we do not waste it
			SuiteScanner.__loadStripe(__listener, jars, numJars, nameToJar,
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
			return new AvailableSuites(libs, result
				.<Application>toArray(new Application[result.size()]));
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
	static void __loadStripe(SuiteScanListener __listener,
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
			String libPath = JarPackageShelf.libraryPath(jar);
			if (!libPath.endsWith(".jar") && !libPath.endsWith(".JAR") &&
				!libPath.endsWith(".kjx") && !libPath.endsWith(".KJX"))
				continue;
			
			// Debug
			Debugging.debugNote("Checking %s...", libPath);
			
			// Scan for multiple application types, since it is very possible
			// for an application to be in hybrid form (such as MIDP/i-mode)
			SuiteScanner.__scanJava(__listener, __numJars, __libs, __result,
				accurateJarIndex, jar);
			SuiteScanner.__scanIMode(__listener, __numJars, __libs,
				__nameToJar, __result, accurateJarIndex, jar);
			SuiteScanner.__scanIModeJVLite2(__listener, __numJars, __libs,
				__nameToJar, __result, accurateJarIndex, jar);
		}
	}
	
	/**
	 * Scans for an i-mode application and loads them.
	 * 
	 * @param __listener The listener used for load events.
	 * @param __nameToJar The name to JAR mapping, used to find the JAM file.
	 * @param __result The target applications.
	 * @param __jarDx The JAR index.
	 * @param __jar The JAR to check.
	 * @since 2021/06/013
	 */
	private static void __scanIMode(SuiteScanListener __listener,
		int __numJars, __Libraries__ __libs,
		Map<String, JarPackageBracket> __nameToJar,
		List<Application> __result,
		int __jarDx, JarPackageBracket __jar)
	{
		// Try to determine what our JAM would be called
		String jarName = JarPackageShelf.libraryPath(__jar);
		String jamName = SuiteScanner.__siblingByExt(jarName, ".jam");
		
		// Determine the name of the JAM file to load
		JarPackageBracket jam;
		synchronized (__nameToJar)
		{
			jam = __nameToJar.get(jamName);
		}
		
		// If there is no JAM file, this cannot be an i-mode application
		if (jam == null)
			return;
		
		// Load the ADF/JAM descriptor that describes this application
		Map<String, String> adfProps = new LinkedHashMap<>();
		try (InputStream jamIn = JarPackageShelf.openResource(jam,
			SuiteScanner._DATA_RESOURCE))
		{
			// Missing? Cannot be an i-mode application
			if (jamIn == null)
				return;
			
			// Parse by text
			__AdfUtils__.__parseAdfText(adfProps, jamIn);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		// Load application
		try
		{
			Application app = new IModeApplication(__jar, __libs, adfProps);
			synchronized (app)
			{
				__result.add(app);
			}
			
			// Indicate that it was scanned
			if (__listener != null)
				__listener.scanned(app, __jarDx, __numJars);
		}
		catch (InvalidSuiteException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Scans for a JV-Lite2 i-Mode application, which is in binary form.
	 * 
	 * @param __listener The listener used for load events.
	 * @param __nameToJar The name to JAR mapping, used to find the JAM file.
	 * @param __result The target applications.
	 * @param __jarDx The JAR index.
	 * @param __jar The JAR to check.
	 * @since 2023/04/10
	 */
	private static void __scanIModeJVLite2(SuiteScanListener __listener,
		int __numJars, __Libraries__ __libs,
		Map<String, JarPackageBracket> __nameToJar,
		List<Application> __result,
		int __jarDx, JarPackageBracket __jar)
	{
		// We need to locate the binary form of the ADF
		String jarName = JarPackageShelf.libraryPath(__jar);
		String adfName = SuiteScanner.__siblingByExt(jarName, ".adf");
		
		// Determine the name of the JAM file to load
		JarPackageBracket binaryAdf;
		synchronized (__nameToJar)
		{
			binaryAdf = __nameToJar.get(adfName);
		}
		
		// If there is no ADF file, this cannot be an i-mode application
		if (binaryAdf == null)
			return;
		
		// Decode the Binary ADF information
		Map<String, String> adfProps = new LinkedHashMap<>();
		try (InputStream binaryAdfIn = JarPackageShelf.openResource(binaryAdf,
			SuiteScanner._DATA_RESOURCE))
		{
			// Missing? Cannot be an i-mode application
			if (binaryAdfIn == null)
				return;
			
			// Parse using binary format
			__AdfUtils__.__parseAdfBinary(adfProps, binaryAdfIn);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		// Additional i-mode specific properties?
		Map<String, String> extraSysProps = new LinkedHashMap<>();
		
		// Try to locate the scratchpad seed archive
		String stoName = SuiteScanner.__siblingByExt(jarName, ".sto");
		JarPackageBracket binarySto;
		synchronized (__nameToJar)
		{
			binarySto = __nameToJar.get(stoName);
		}
		
		// Store where the scratchpad seed should be found
		if (binarySto != null)
			extraSysProps.put(
				IModeApplication.SEED_SCRATCHPAD_PREFIX + ".0",
				JarPackageShelf.libraryPath(binarySto));
		
		// Load application
		try
		{
			Application app = new IModeApplication(__jar, __libs, adfProps,
				extraSysProps);
			synchronized (app)
			{
				__result.add(app);
			}
			
			// Indicate that it was scanned
			if (__listener != null)
				__listener.scanned(app, __jarDx, __numJars);
		}
		catch (InvalidSuiteException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Scans for Java applications in the given JAR.
	 * 
	 * @param __listener The listener used.
	 * @param __numJars The number of JARs.
	 * @param __libs The available support libraries.
	 * @param __result Where applications go.
	 * @param __jarDx The Jar Index.
	 * @param __jar The JAR to check.
	 * @since 2021/06/13
	 */
	private static void __scanJava(SuiteScanListener __listener,
		int __numJars, __Libraries__ __libs, List<Application> __result,
		int __jarDx, JarPackageBracket __jar)
	{
		// Try to read the manifest from the given JAR and process the
		// suite information
		SuiteInfo info;
		JavaManifest man;
		try (InputStream rc = JarPackageShelf.openResource(__jar,
			"META-INF/MANIFEST.MF"))
		{
			// If no manifest exists, might not be a JAR
			if (rc == null)
			{
				Debugging.debugNote("No META-INF/MANIFEST.MF in %s...",
					JarPackageShelf.libraryPath(__jar));
				
				return;
			}
			
			man = new JavaManifest(rc);
			info = new SuiteInfo(man);
		}
		
		// Prevent bad JARs and files from messing things up
		catch (IOException|InvalidSuiteException|MLECallError e)
		{
			e.printStackTrace();
			return;
		}
		
		switch (info.type())
		{
			// Handle library
			case LIBLET:
			case SQUIRRELJME_API:
				__libs.__register(info, __jar);
				return;
			
			// Handle application
			case MIDLET:
				// Setup application information for all possible entry
				// points
				for (EntryPoint e : new EntryPoints(man))
				{
					// Load application
					JavaApplication app =
						new JavaApplication(info, __jar, __libs, e);
					synchronized (__result)
					{
						__result.add(app);
					}
					
					// Indicate that it was scanned
					if (__listener != null)
						__listener.scanned(app, __jarDx, __numJars);
				}
				return;
			
			// Unknown?
			default:
				throw Debugging.oops();
		}
	}
	
	/**
	 * Returns a sibling file with the same base name but a differing
	 * extension.
	 * 
	 * @param __jar The other Jar to check.
	 * @param __ext The extension to map to.
	 * @return The sibling file based on the extension.
	 * @since 2023/04/10
	 */
	private static String __siblingByExt(String __jar, String __ext)
	{
		boolean lower = __jar.endsWith(".jar");
		boolean upper = __jar.endsWith(".JAR");
		
		// Does not end with it? Just append it
		if (!lower && !upper)
			return __jar + __ext;
		
		// Remove old extension and just append the new one
		String baseName = __jar.substring(0, __jar.length() - 4);
		if (upper)
			return baseName + __ext.toUpperCase();
		return baseName + __ext;
	}
}

