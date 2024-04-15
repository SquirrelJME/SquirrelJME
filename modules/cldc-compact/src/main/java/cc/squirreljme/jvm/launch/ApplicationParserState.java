// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.suite.EntryPoint;
import cc.squirreljme.jvm.suite.SuiteInfo;
import cc.squirreljme.jvm.suite.SuiteUtils;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * This contains the state of the application parser.
 *
 * @since 2024/01/06
 */
public final class ApplicationParserState
{
	/** The current Jar. */
	protected final JarPackageBracket jar;
	
	/** The listener used. */
	protected final SuiteScanListener listener;
	
	/** The Jar Index. */
	protected final int jarDx;
	
	/** The number of available jars. */
	protected final int numJars;
	
	/** The shelf to use. */
	protected final VirtualJarPackageShelf shelf;
	
	/** Library set. */
	private final __Libraries__ _libs;
	
	/** The name to Jar mapping. */
	private final Map<String, JarPackageBracket> _nameToJar;
	
	/** The resultant applications. */
	private final List<Application> _result;
	
	/**
	 * Initializes the parser state.
	 *
	 * @param __listener The listener to use for output.
	 * @param __numJars The number of Jars being parsed.
	 * @param __libs The input libraries.
	 * @param __nameToJar The name to Jar mappings.
	 * @param __result The resultant output for applications.
	 * @param __accurateJarIndex The accurate index of this specific Jar.
	 * @param __jar The Jar being parsed.
	 * @param __shelf The shelf to use.
	 * @throws NullPointerException
	 * @since 2024/01/06
	 */
	public ApplicationParserState(SuiteScanListener __listener, int __numJars,
		__Libraries__ __libs, Map<String, JarPackageBracket> __nameToJar,
		List<Application> __result,
		int __accurateJarIndex, JarPackageBracket __jar,
		VirtualJarPackageShelf __shelf)
		throws NullPointerException
	{
		if (__libs == null || __nameToJar == null || __shelf == null ||
			__result == null || __jar == null)
			throw new NullPointerException("NARG");
		
		this.jar = __jar;
		this.listener = __listener;
		this.jarDx = __accurateJarIndex;
		this.numJars = __numJars;
		this.shelf = __shelf;
		this._nameToJar = __nameToJar;
		this._libs = __libs;
		this._result = __result;
	}
	
	/**
	 * Adds an application to the output.
	 *
	 * @param __app The application to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public void addApplication(Application __app)
		throws NullPointerException
	{
		if (__app == null)
			throw new NullPointerException("NARG");
		
		List<Application> result = this._result;
		synchronized (result)
		{
			result.add(__app);
		}
	}
	
	/**
	 * Finds the first sibling with the given extensions. 
	 *
	 * @param __jarName The Jar name.
	 * @param __exts The extensions used.
	 * @return The found scratch pad binary, if found.
	 * @since 2023/07/21
	 */
	public JarPackageBracket findFirstSibling(String __jarName,
		String... __exts)
		throws NullPointerException
	{
		if (__jarName == null || __exts == null || __exts.length == 0)
			throw new NullPointerException("NARG");
		
		Map<String, JarPackageBracket> nameToJar = this._nameToJar;
		synchronized (nameToJar)
		{
			// Go through each and try to find it
			for (String ext : __exts)
			{
				if (ext == null)
					throw new NullPointerException("NARG");
				
				JarPackageBracket maybe = nameToJar.get(
					ScannerUtils.siblingByExt(__jarName, ext));
				if (maybe != null)
				{
					// Try with short name instead
					maybe = nameToJar.get(ScannerUtils.siblingByExt(
						SuiteUtils.baseName(__jarName), ext));
					if (maybe != null)
						return maybe;
				}
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Attempts to locate the scratch pad binary.
	 *
	 * @param __jarName The Jar name.
	 * @return The found scratch pad binary, if found.
	 * @since 2023/07/21
	 */
	public JarPackageBracket findIModeScratchPad(String __jarName)
		throws NullPointerException
	{
		if (__jarName == null)
			throw new NullPointerException("NARG");
		
		return this.findFirstSibling(__jarName, ".sto", ".sp");
	}
	
	/**
	 * Returns the library path of the current Jar.
	 *
	 * @return The resultant path.
	 * @since 2024/01/06
	 */
	public String libraryPath()
	{
		return this.libraryPath(this.jar);
	}
	
	/**
	 * Returns the library path of the given Jar.
	 *
	 * @param __jar The Jar to get the path of.
	 * @return The resultant path.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public String libraryPath(JarPackageBracket __jar)
		throws NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		return this.shelf.libraryPath(__jar);
	}
	
	/**
	 * Initializes a new i-mode application.
	 *
	 * @param __adfProps The ADF properties.
	 * @param __extraSysProps Any extra system properties needed at launch.
	 * @return The resultant i-mode application.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public IModeApplication newIModeApplication(Map<String, String> __adfProps,
		Map<String, String> __extraSysProps)
		throws NullPointerException
	{
		if (__adfProps == null || __extraSysProps == null)
			throw new NullPointerException("NARG");
		
		return new IModeApplication(this.jar, this._libs, __adfProps,
			this.libraryPath(), __extraSysProps);
	}
	
	/**
	 * Initializes a new Java application.
	 *
	 * @param __info The information for this.
	 * @param __entry The entry point of the application.
	 * @return The resultant application.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public JavaApplication newJavaApplication(SuiteInfo __info,
		EntryPoint __entry)
		throws NullPointerException
	{
		if (__info == null || __entry == null)
			throw new NullPointerException("NARG");
		
		// Setup library
		return new JavaApplication(__info, this.jar,
			this._libs, __entry);
	}
	
	/**
	 * Open a resource in the current Jar.
	 *
	 * @param __rcName The resource name to open.
	 * @return The resultant resource or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public InputStream openResource(String __rcName)
		throws NullPointerException
	{
		return this.openResource(null, __rcName);
	}
	
	/**
	 * Open a resource in the specified Jar or the current Jar.
	 *
	 * @param __inJar If not {@code null} then open the resource in the
	 * given Jar, otherwise the default Jar will be used.
	 * @param __rcName The resource name to open.
	 * @return The resultant resource or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public InputStream openResource(JarPackageBracket __inJar,
		String __rcName)
		throws NullPointerException
	{
		if (__rcName == null)
			throw new NullPointerException("NARG");
		
		return this.shelf.openResource(
			(__inJar == null ? this.jar : __inJar), __rcName);
	}
	
	/**
	 * Registers a library for later dependency handling.
	 * 
	 * @param __info The JAR information.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public void register(SuiteInfo __info)
	{
		this._libs.__register(__info, this.jar);
	}
	
	/**
	 * Specifies that the application has been scanned for this Jar.
	 *
	 * @param __app The application to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public void scanned(Application __app)
		throws NullPointerException
	{
		if (__app == null)
			throw new NullPointerException("NARG");
		
		SuiteScanListener listener = this.listener;
		if (listener != null)
			listener.scanned(__app, this.jarDx, this.numJars);
	}
}
