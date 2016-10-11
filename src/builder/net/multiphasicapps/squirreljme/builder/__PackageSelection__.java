// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.projects.ProjectGroup;
import net.multiphasicapps.squirreljme.projects.ProjectInfo;
import net.multiphasicapps.squirreljme.projects.ProjectList;
import net.multiphasicapps.squirreljme.projects.ProjectType;
import net.multiphasicapps.util.sorted.SortedTreeSet;

/**
 * This is used to select the packages which are to be compiled and output
 * into the SquirrelJME binary. Dependencies are included in the target as
 * needed.
 *
 * @since 2016/09/02
 */
class __PackageSelection__
{
	/** All projects to be included. */
	final Set<ProjectInfo> _all =
		new SortedTreeSet<>();
	
	/** The JVM classpath. */
	final Set<ProjectInfo> _jvm =
		new SortedTreeSet<>();
	
	/**
	 * Selects the packages which are to be used to become a part of the
	 * SquirrelJME binary.
	 *
	 * @param __conf The configuration to use.
	 * @param __bi The current build instance.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	__PackageSelection__(BuildConfig __conf, BuildInstance __bi)
		throws IOException, NullPointerException
	{
		// Check
		if (__conf == null || __bi == null)
			throw new NullPointerException("NARG");
		
		// Get the package list
		ProjectList plist = __conf.packageList();
		Set<ProjectInfo> jvm = this._jvm;
		Set<ProjectInfo> all = this._all;
		
		// {@squirreljme.error DW0k Cannot build the target because the Java
		// Virtual Machine project could not be found.}
		ProjectGroup vmgrp = plist.get("jvm");
		ProjectInfo jvmproj;
		if (vmgrp == null || (jvmproj = vmgrp.binary()) == null)
			throw new IllegalStateException("DW0k");
		jvm.addAll(plist.recursiveDependencies(ProjectType.BINARY,
			jvmproj.name(), false));
		
		// Tests?
		boolean tests = __conf.includeTests();
		
		// Get package groups to determine any extra packages to include
		// along with the JVM
		String[] groups = __bi.__packageGroup();
		int n = groups.length;
		for (ProjectGroup pgrp : plist.values())
		{
			// Only consider projects with binaries
			ProjectInfo pi = pgrp.binary();
			if (pi == null)
				continue;
			
			Set<String> pigs = pi.groups();
			
			// If this is a test package then include it
			if (tests && pigs.contains("tests"))
				all.addAll(plist.recursiveDependencies(ProjectType.COMPILED,
					pi.name(), false));
			
			// Add to JVM classpath?
			for (int i = 0; i < n; i++)
				if (pigs.contains(groups[i]))
				{
					jvm.addAll(plist.recursiveDependencies(
						ProjectType.COMPILED, pi.name(), false));
					break;
				}
		}
		
		// Add any extra projects to be included in the all set
		for (ProjectInfo p : __conf.extraProjects())
			all.addAll(plist.recursiveDependencies(ProjectType.BINARY,
				p.name(), false));
		
		// Add JVM packages to the all list
		all.addAll(jvm);
		
		// Note some things
		PrintStream out = System.err;
		out.printf("Will compile %d project(s)...%n", all.size());
		out.printf("DEBUG -- %s%n", all);
		out.printf("JVM Classpath contains %d project(s)...%n", jvm.size());
		out.printf("DEBUG -- %s%n", jvm);
	}
}

