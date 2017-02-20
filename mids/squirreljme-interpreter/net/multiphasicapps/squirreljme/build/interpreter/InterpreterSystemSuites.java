// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.projects.InvalidProjectException;
import net.multiphasicapps.squirreljme.build.projects.NamespaceType;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectBinary;
import net.multiphasicapps.squirreljme.build.projects.ProjectName;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParameters;
import net.multiphasicapps.squirreljme.kernel.SuiteDataAccessor;
import net.multiphasicapps.squirreljme.kernel.SystemInstalledSuites;

/**
 * This class manages and determines which suites are used and auto-installed
 * in the system class path.
 *
 * Note that the suites to use are selected by the kernel launch parameters and
 * as such any system suites by their project name which are not specified
 * will not appear in the class path apart from the built-in defaults.
 *
 * @since 2016/12/17
 */
public class InterpreterSystemSuites
	extends SystemInstalledSuites
{
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.interpreter.select=pkg,...
	 * This selects the API project which are to be used to select which
	 * APIs are available for the interpreter to provide.}
	 */
	public static final String SELECT_PROPERTY =
		"net.multiphasicapps.squirreljme.interpreter.select";
	
	/** APIs to keep loaded at any one time. */
	private final __ProjectAccessor__[] _accessors;
	
	/**
	 * Initializes the system suite manager.
	 *
	 * @param __ai The interpreter which runs the system.
	 * @param __klp The launch parameters for the kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/17
	 */
	public InterpreterSystemSuites(AutoInterpreter __ai,
		KernelLaunchParameters __klp)
		throws NullPointerException
	{
		// Check
		if (__ai == null || __klp == null)
			throw new NullPointerException("NARG");
		
		// Parse properites 
		Set<ProjectName> toload = new LinkedHashSet<>();
		{
			// Always force cldc-compact to be specified
			// Also include the midlet system
			toload.add(new ProjectName("cldc-compact"));
			toload.add(new ProjectName("meep-midlet"));
			
			// Parse projects to add
			String prop = Objects.toString(
				__klp.getSystemProperty(SELECT_PROPERTY), "");
			int n = prop.length();
			for (int i = 0; i < n;)
			{
				// Find next comma
				int nc = prop.indexOf(',', i);
				if (nc < 0)
					nc = n;
				
				// Split off and add
				try
				{
					toload.add(new ProjectName(prop.substring(i, nc).trim()));
				}
				
				// Ignore
				catch (InvalidProjectException e)
				{
				}
				
				// Set next
				i = nc + 1;
			}
		}
		
		// Go through system projects
		Set<ProjectBinary> projects = new LinkedHashSet<>();
		for (Project p : __ai.projectManager())
		{
			// Only accept APIs
			if (p.type() != NamespaceType.API)
				continue;
			
			// Not a project to load?
			if (!toload.contains(p.name()))
				continue;
			
			// Add it for loading
			ProjectBinary bin = p.binary();
			projects.add(bin);
			
			// Add dependencies
			for (ProjectBinary bd : bin.binaryDependencies(true))
				projects.add(bd);
		}
		
		// Load all binaries and create accessors for them
		int n = projects.size();
		__ProjectAccessor__[] accessors = new __ProjectAccessor__[n];
		Iterator<ProjectBinary> it = projects.iterator();
		for (int i = 0; i < n; i++)
			accessors[i] = new __ProjectAccessor__(__ai, it.next());
		this._accessors = accessors;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/16
	 */
	public Iterator<SuiteDataAccessor> iterator()
	{
		return new __AccessorIterator__();
	}
	
	/**
	 * This is used to iterate over the system class path suites.
	 *
	 * @since 2017/01/16
	 */
	private final class __AccessorIterator__
		implements Iterator<SuiteDataAccessor>
	{
		/** Accessor for the suites. */
		private final __ProjectAccessor__[] _accessors =
			InterpreterSystemSuites.this._accessors;
		
		/** The next suite to return. */
		private volatile int _next =
			this._accessors.length - 1;
		
		/**
		 * {@inheritDoc}
		 * @since 2017/01/16
		 */
		@Override
		public boolean hasNext()
		{
			return this._next >= 0;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/01/16
		 */
		@Override
		public SuiteDataAccessor next()
		{
			// Get next index
			int next = this._next;
			if (next <= -1)
				throw new NoSuchElementException("NSEE");
			
			// Set and return it
			this._next = next - 1;
			return this._accessors[next];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/01/16
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

