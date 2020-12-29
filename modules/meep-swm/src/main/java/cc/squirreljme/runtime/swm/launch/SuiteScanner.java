// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm.launch;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.swm.InvalidSuiteException;
import cc.squirreljme.runtime.swm.SuiteInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.tool.manifest.JavaManifest;

/**
 * This is a scanner which can read all of the application groups that are
 * available.
 *
 * @since 2020/12/28
 */
public final class SuiteScanner
{
	/**
	 * Not used.
	 * 
	 * @since 2020/12/28
	 */
	private SuiteScanner()
	{
	}
	
	/**
	 * Scans all of the available suites and returns imformation that is needed
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
	 * Scans all of the available suites and returns imformation that is needed
	 * for them to properly launch.
	 * 
	 * @param __listener The listener for suites as they are scanned, used to
	 * indicate progress.
	 * @return The state of scanned suites.
	 * @since 2020/12/29
	 */
	public static AvailableSuites scanSuites(SuiteScanListener __listener)
	{
		// Get all of the available libraries
		JarPackageBracket[] jars = JarPackageShelf.libraries();
		int numJars = jars.length;
		
		// Libraries are lazily handled on launching
		__Libraries__ libs = new __Libraries__();
		
		// Applications which should be loaded
		List<Application> result = new LinkedList<>();
		
		// Locate programs via the library path
		for (int i = 0, n = jars.length; i < n; i++)
		{
			JarPackageBracket jar = jars[i];
			
			// Debug
			Debugging.debugNote("Checking %s...",
				JarPackageShelf.libraryPath(jar));
			
			// Try to read the manifest from the given JAR and process the
			// suite information
			SuiteInfo info;
			JavaManifest man;
			try (InputStream rc = JarPackageShelf.openResource(jar,
				"META-INF/MANIFEST.MF"))
			{
				// If no manifest exists, might not be a JAR
				if (rc == null)
					continue;
				
				man = new JavaManifest(rc);
				info = new SuiteInfo(man);
			}
			catch (IOException | InvalidSuiteException e)
			{
				e.printStackTrace();
				
				continue;
			}
			
			switch (info.type())
			{
				// Handle library
				case LIBLET:
				case SQUIRRELJME_API:
					libs.__register(info, jar);
					break;
				
				// Handle application
				case MIDLET:
					// Setup application information
					Application app = new Application(info, jar, libs);
					result.add(app);
					
					// Indicate that it was scanned
					if (__listener != null)
						__listener.scanned(app, i, numJars);
					break;
				
				// Unknown?
				default:
					throw Debugging.oops();
			}
		}
		
		// Finalize suite list
		return new AvailableSuites(libs, result
			.<Application>toArray(new Application[result.size()]));
	}
}
