// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;
import net.multiphasicapps.sjmepackages.PackageName;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;

/**
 * This is the builder for native binaries.
 *
 * @since 2016/06/24
 */
public class Builder
{
	/** The package list to use. */
	protected final PackageList plist;
	
	/** The target architecture. */
	protected final String arch;
	
	/** The target operating system. */
	protected final String os;
	
	/** The target variant. */
	protected final String variant;
	
	/** The triplet. */
	protected final String triplet;
	
	/** The package that implements the JVM for the target triplet. */
	protected final PackageInfo toppackage;
	
	/**
	 * Initializes the builder for a native target.
	 *
	 * @param __pl The package list to use.
	 * @param __arch The target architecture.
	 * @param __os The target operating system.
	 * @param __var The target variant.
	 * @throws IllegalArgumentException If no package exists for the given
	 * triplet.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	public Builder(PackageList __pl, String __arch, String __os, String __var)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		// Check
		if (__pl == null || __arch == null || __os == null || __var == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.plist = __pl;
		this.arch = __arch;
		this.os = __os;
		this.variant = __var;
		
		// Build triplet
		String triplet;
		this.triplet = (triplet = __arch + "." + __os + "." + __var);
		
		// Go through all of the packages to find the one that specifies that
		// it is the JVM for the given triplet
		PackageInfo tpk = null;
		for (Map.Entry<PackageName, PackageInfo> e : __pl.entrySet())
		{
			// Get the manifest
			PackageInfo pi = e.getValue();
			JavaManifest man = pi.manifest();
			
			// If it does not exist, ignore
			if (man == null)
				continue;
			
			// Get the main attributes
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// See if the triplet properly exists
			String pott = attr.get("X-SquirrelJME-Target-OS");
			
			// Matches?
			if (triplet.equals(pott))
			{
				tpk = pi;
				break;
			}
		}
		
		// {@squirreljme.error DW06 No package (The used triplet)}
		if (tpk == null)
			throw new IllegalArgumentException(String.format("DW06 %s",
				triplet));
		
		// Set
		this.toppackage = tpk;
		
		// Go through all of the dependencies of the package and include them
		// for compilation
		Set<PackageInfo> pis = new LinkedHashSet<>();
		
		throw new Error("TODO");
	}
	
	/**
	 * Performs the actual build step.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/06/24
	 */
	public void build()
		throws IOException
	{
		throw new Error("TODO");
	}
}

