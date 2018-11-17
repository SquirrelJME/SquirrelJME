// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.build.vm;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.BinaryManager;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.Source;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMSuiteManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeSet;

/**
 * The manager for suites that use the build system.
 *
 * @since 2018/10/26
 */
public final class BuildSuiteManager
	implements VMSuiteManager
{
	/** The project manager which is used. */
	protected final ProjectManager manager;
	
	/** The timespace to act in. */
	protected final TimeSpaceType timespace;
	
	/** Loaded libraries. */
	private final Map<String, VMClassLibrary> _libraries =
		new HashMap<>();
	
	/**
	 * Initializes the suite manager.
	 *
	 * @param __pm The project manager that is used.
	 * @param __ts The timespace to act in.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/16
	 */
	public BuildSuiteManager(ProjectManager __pm, TimeSpaceType __ts)
		throws NullPointerException
	{
		if (__pm == null || __ts == null)
			throw new NullPointerException("NARG");
		
		this.manager = __pm;
		this.timespace = __ts;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/26
	 */
	@Override
	public final String[] listLibraryNames()
	{
		Set<String> rv = new SortedTreeSet<>();
		
		TimeSpaceType timespace = this.timespace;
		ProjectManager manager = this.manager;
		
		// Could fail
		try
		{
			// Add sources
			for (Source s : manager.sourceManager(timespace))
				rv.add(s.name().toString() + ".jar");
			
			// Add binaries
			for (Binary b : manager.binaryManager(timespace))
				rv.add(b.name().toString() + ".jar");
		}
		
		// {@squirreljme.error BA01 Could not list suites available.}
		catch (IOException e)
		{
			throw new RuntimeException("BA01", e);
		}
		
		return rv.<String>toArray(new String[rv.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/26
	 */
	@Override
	public final VMClassLibrary loadLibrary(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Remove the extension
		if (__s.endsWith(".jar"))
			__s = __s.substring(0, __s.length() - 4);
		
		// Lock
		Map<String, VMClassLibrary> libraries = this._libraries;
		synchronized (libraries)
		{
			// Pre-cached already?
			VMClassLibrary rv = libraries.get(__s);
			if (rv != null)
				return rv;
			
			// Build the binaries for this finding the matching one
			for (Binary b : this.manager.build(this.timespace, __s))
				if (__s.equals(b.name().toString()))
				{
					libraries.put(__s, (rv = new BuildClassLibrary(b)));
					return rv;
				}
			
			// {@squirreljme.error BA02 No such library exists. (The requested
			// library)}
			throw new RuntimeException(String.format("BA02 %s", __s));
		}
	}
}

