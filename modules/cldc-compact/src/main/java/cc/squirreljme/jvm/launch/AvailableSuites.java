// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import java.util.ArrayList;
import java.util.List;

/**
 * This contains the list of suites and libraries.
 *
 * @since 2020/12/28
 */
public final class AvailableSuites
{
	/** The shelf to use when accessing Jars. */
	private final VirtualJarPackageShelf shelf;
	
	/** The applications available. */
	private final Application[] _apps;
	
	/** The lazy loaded library set. */
	private final __Libraries__ _libraries;
	
	/**
	 * Initializes the available suites.
	 * 
	 * @param __shelf The shelf to use for access.
	 * @param __libs The libraries to use for lazy initialization.
	 * @param __apps The applications that are available.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	AvailableSuites(VirtualJarPackageShelf __shelf, __Libraries__ __libs,
		Application... __apps)
		throws NullPointerException
	{
		if (__shelf == null || __libs == null || __apps == null)
			throw new NullPointerException("NARG");
		
		this.shelf = __shelf;
		this._libraries = __libs;
		this._apps = __apps.clone();
	}
	
	/**
	 * Returns the detected applications.
	 *
	 * @return The detected applications.
	 * @since 2024/01/06
	 */
	public Application[] applications()
	{
		return this._apps.clone();
	}
	
	/**
	 * Finds all applications associated with the given Jar.
	 *
	 * @param __jar The jar to get.
	 * @return The resultant applications.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public Application[] findApplications(JarPackageBracket __jar)
		throws NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		// Scan through every application to find matches
		List<Application> result = new ArrayList<>(6);
		for (Application app : this._apps)
			if (app.jar == __jar || this.shelf.equals(app.jar, __jar))
				result.add(app);
		
		// Return the result of it
		return result.toArray(new Application[result.size()]);
	}
}
