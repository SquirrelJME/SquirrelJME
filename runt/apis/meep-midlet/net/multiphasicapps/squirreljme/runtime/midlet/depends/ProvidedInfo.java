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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteInfo;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteType;
import net.multiphasicapps.squirreljme.runtime.midlet.InvalidSuiteException;
import net.multiphasicapps.strings.StringUtils;
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
	 * Parses the given suite information and returns all of the provided
	 * resolutions for dependencies which are specified in the manifest.
	 *
	 * @param __info The suite information to parse.
	 * @return The parsed provided resolution information.
	 * @throws InvalidSuiteException If the manifest is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/20
	 */
	public static final ProvidedInfo of(SuiteInfo __info)
		throws InvalidSuiteException, NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		JavaManifestAttributes attr = __info.manifest().getMainAttributes();
		String value;
		
		// Defined configurations
		Set<Configuration> configs = new LinkedHashSet<>();
		value = attr.getValue("X-SquirrelJME-DefinedConfigurations");
		if (value != null)
			for (String s : StringUtils.basicSplit(" \t", value))
				configs.add(new Configuration(s));
		
		// Defined profiles
		Set<Profile> profiles = new LinkedHashSet<>();
		value = attr.getValue("X-SquirrelJME-DefinedProfiles");
		if (value != null)
			for (String s : StringUtils.basicSplit(" \t", value))
				profiles.add(new Profile(s));
		
		// Defined standards
		Set<Profile> standards = new LinkedHashSet<>();
		value = attr.getValue("X-SquirrelJME-DefinedStandards");
		if (value != null)
			for (String s : StringUtils.basicSplit(",", value))
				standards.add(new Standard(s.trim()));
		
		// Has internal project name?
		String internalname = attr.getValue(
			"X-SquirrelJME-InternalProjectName");
		
		// Initialize
		return new ProvidedInfo(__info, configs, profiles, standards,
			internalname);
	}
}

