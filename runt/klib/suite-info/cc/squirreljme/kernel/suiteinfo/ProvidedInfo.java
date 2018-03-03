// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.suiteinfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.collections.ArrayUtils;
import net.multiphasicapps.collections.EmptySet;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.collections.UnmodifiableSet;
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
	/** Provided fields. */
	protected final Set<MarkedProvided> provided;
	
	/**
	 * Initializes the provided dependency info.
	 *
	 * @param __provs The provided set.
	 * @since 2017/12/31
	 */
	public ProvidedInfo(MarkedProvided... __provs)
	{
		this((__provs == null ? EmptySet.<MarkedProvided>empty() :
			Arrays.<MarkedProvided>asList(__provs)));
	}
	
	/**
	 * Initializes the provided dependency info.
	 *
	 * @param __provs The provided set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public ProvidedInfo(Collection<MarkedProvided> __provs)
		throws NullPointerException
	{
		if (__provs == null)
			throw new NullPointerException("NARG");
		
		Set<MarkedProvided> provided = new LinkedHashSet<>();
		for (MarkedProvided p : __provs)
			if (p == null)
				throw new NullPointerException("NARG");
			else
				provided.add(p);
		this.provided = UnmodifiableSet.<MarkedProvided>of(provided);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof ProvidedInfo))
			return false;
		
		return this.provided.equals(((ProvidedInfo)__o).provided);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final int hashCode()
	{
		return this.provided.hashCode();
	}
	
	/**
	 * Returns the provided.
	 *
	 * @return The provided.
	 * @since 2017/12/31
	 */
	public final Set<MarkedProvided> provided()
	{
		return this.provided;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
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
		
		Set<MarkedProvided> provided = new LinkedHashSet<>();
		JavaManifestAttributes attr = __info.manifest().getMainAttributes();
		String value;
		
		// Defined configurations
		value = attr.getValue("X-SquirrelJME-DefinedConfigurations");
		if (value != null)
			for (String s : StringUtils.basicSplit(" \t", value))
				provided.add(new Configuration(s));
		
		// Defined profiles
		value = attr.getValue("X-SquirrelJME-DefinedProfiles");
		if (value != null)
			for (String s : StringUtils.basicSplit(" \t", value))
				provided.add(new Profile(s));
		
		// Defined standards
		value = attr.getValue("X-SquirrelJME-DefinedStandards");
		if (value != null)
			for (String s : StringUtils.basicSplit(",", value))
				provided.add(new Standard(s.trim()));
		
		// Has internal project name?
		String internalname = attr.getValue(
			"X-SquirrelJME-InternalProjectName");
		if (internalname != null)
			provided.add(new InternalName(internalname));
		
		// This provides a library or otherwise
		provided.add(new TypedSuite(__info.type(), __info.suite()));
		
		// Initialize
		return new ProvidedInfo(provided);
	}
}

