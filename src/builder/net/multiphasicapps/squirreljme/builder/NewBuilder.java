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
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;

/**
 * This builds the given target configuration and generates a binary.
 *
 * @since 2016/07/22
 */
public class NewBuilder
{
	/** Logging output. */
	protected final PrintStream out;
	
	/** The configuration. */
	protected final BuildConfig config;
	
	/** The target builder. */
	protected final TargetBuilder targetbuilder;
	
	/** The package list. */
	protected final PackageList packagelist;
	
	/**
	 * Initializes the new builder code.
	 *
	 * @param __conf The build configuration.
	 * @param __tb The target builder.
	 * @param __pl The package list.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public NewBuilder(PrintStream __out, BuildConfig __conf,
		TargetBuilder __tb, PackageList __pl)
		throws NullPointerException
	{
		// Check
		if (__out == null || __conf == null || __tb == null || __pl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __out;
		this.config = __conf;
		this.targetbuilder = __tb;
		this.packagelist = __pl;
	}
	
	/**
	 * Performs the actual build.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/07/22
	 */
	public void build()
		throws IOException
	{
		PrintStream out = this.out;
		
		// Determine the packages to build
		out.println("Selecting projects...");
		Set<PackageInfo> buildprojects = __selectProjects();
		
		// Print them all
		out.printf("Will compile %d project(s)...%n", buildprojects.size());
		System.err.printf("DEBUG -- %s%n", buildprojects);
		
		throw new Error("TODO");
	}
	
	/**
	 * Selects the projects that should be built.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/07/22
	 */
	private final Set<PackageInfo> __selectProjects()
		throws IOException
	{
		// Setup
		Set<PackageInfo> rv = new LinkedHashSet<>();
		PackageList packagelist = this.packagelist;
		
		// Always add the JVM
		PackageInfo jvmproj = packagelist.get("jvm");
		rv.addAll(jvmproj.recursiveDependencies());
		
		// Return
		return rv;
	}
}

