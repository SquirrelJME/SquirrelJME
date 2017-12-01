// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.depends;

import net.multiphasicapps.squirreljme.runtime.midlet.InvalidSuiteException;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This contains all of the information for dependencies which are provided
 * by an application or library.
 *
 * @since 2017/11/30
 */
public final class ProvidedInfo
{
	/**
	 * Parses the given manifest and returns all of the provided resolutions
	 * for dependencies which are specified in the manifest.
	 *
	 * @param __man The manifest to parse.
	 * @return The parsed provided resolution information.
	 * @throws InvalidSuiteException If the manifest is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/20
	 */
	public static final ProvidedInfo parseManifest(JavaManifest __man)
		throws InvalidSuiteException, NullPointerException
	{
		if (__man == null)
			throw new NullPointerException("NARG");
		
		JavaManifestAttributes attr = __man.getMainAttributes();
		
		throw new todo.TODO();
		/*
		if (__m == null)
			throw new NullPointerException("NARG");
		
		Set<ManifestedDependency> deps = new LinkedHashSet<>();
		
		// Configurations defined
		String configs = attr.getValue("X-SquirrelJME-DefinedConfigurations");
		if (configs != null)
			for (APIConfiguration conf : APIConfiguration.parseList(configs))
				deps.add(conf);
		
		// Profiles defined
		String profiles = attr.getValue("X-SquirrelJME-DefinedProfiles");
		if (profiles != null)
			for (APIProfile prof : APIProfile.parseList(profiles))
				deps.add(prof);
		
		// Standards defined
		String standards = attr.getValue("X-SquirrelJME-DefinedStandards");
		if (standards != null)
			for (String s : StringUtils.basicSplit(",", standards))
				deps.add(new APIStandard(s));
		
		// SquirrelJME project name specifier, not portable
		String sjmeipn = attr.getValue("X-SquirrelJME-InternalProjectName");
		if (sjmeipn != null)
		{
			MidletDependency dep = new MidletDependency(
				MidletDependencyType.PROPRIETARY,
				MidletDependencyLevel.REQUIRED,
				new MidletSuiteName("squirreljme.project@" + sjmeipn.trim()),
				DependencySet.__projectVendor(),
				MidletVersionRange.ANY_VERSION);
			
			// Includes required and optional
			deps.add(dep);
			deps.add(dep.toOptional());
		}
		
		// Handle liblets which may be provided.
		if (!"true".equals(attr.getValue("X-SquirrelJME-IsAPI")))
		{
			String name = attr.getValue("LIBlet-Name"),
				vendor = attr.getValue("LIBlet-Vendor"),
				version = attr.getValue("LIBlet-Version");
			
			// All three must be set and valid
			if (name != null && vendor != null && version != null)
			{
				MidletDependency dep = new MidletDependency(
					MidletDependencyType.LIBLET,
					MidletDependencyLevel.REQUIRED,
					new MidletSuiteName(name),
					new MidletSuiteVendor(vendor),
					MidletVersionRange.exactly(
						new MidletVersion(version)));
				
				// Includes required and optional
				deps.add(dep);
				deps.add(dep.toOptional());
			}
		}
		
		// Build
		return new DependencySet(deps);
		*/
	}
}

