// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.singularexport;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.projects.ProjectInfo;

/**
 * Data that may be used and generated during processing.
 *
 * @since 2016/09/30
 */
class __Data__
{
	/** The services used. */
	final Map<String, List<String>> _services =
		new HashMap<>();
	
	/** Resources which are defined in the virtual resource table. */
	final Map<String, String> _resources =
		new HashMap<>();
	
	/**
	 * Adds a resource to the resource list and returns the virtualized
	 * name of it.
	 *
	 * @param __pi The project owning the resource.
	 * @param __n The name of the resource.
	 * @return The virtualize name of the resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	String __addResource(ProjectInfo __pi, String __n)
		throws NullPointerException
	{
		// Check
		if (__pi == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Build virtual resource name representation
		String rv = "$squirreljme$" + __pi.name() + "$/" + __n;
		
		// Add 
		this._resources.put(rv, __n);
		
		// Return the virtual name
		return rv;
	}
	
	/**
	 * Loads services from the service list.
	 *
	 * @param __fn The file name containing the services.
	 * @param __r The reader for the service list.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/29
	 */
	void __servicesFile(String __fn, BufferedReader __r)
		throws IOException, NullPointerException
	{
		// Check
		if (__fn == null || __r == null)
			throw new NullPointerException("NARG");
		
		// Get list
		Map<String, List<String>> services = this._services;
		
		// Determine list usage
		String want = __fn.substring("META-INF/services/".length());
		List<String> list = services.get(want);
		
		// Create if missing
		if (list == null)
			services.put(want, (list = new ArrayList<>()));
		
		// Add services to the list
		String s;
		while (null != (s = __r.readLine()))
			list.add(s);
	}
}

