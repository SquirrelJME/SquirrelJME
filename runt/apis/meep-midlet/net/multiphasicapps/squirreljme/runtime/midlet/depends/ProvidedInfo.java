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
import net.multiphasicapps.collections.ArrayUtils;
import net.multiphasicapps.collections.EmptySet;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.collections.UnmodifiableSet;
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
	/** Configurations. */
	protected final Set<Configuration> configurations;
	
	/** Profiles. */
	protected final Set<Profile> profiles;
	
	/** Standards. */
	protected final Set<Standard> standards;
	
	/** Internal project name. */
	protected final String internalname;
	
	/** Typed suite. */
	protected final TypedSuite typedsuite;
	
	/**
	 * Initializes the provided dependency info.
	 *
	 * @param __confs The configurations provided.
	 * @param __profs The profiles provided.
	 * @param __stds The standards provided.
	 * @param __intname The internal project name.
	 * @param __typedsuite The suite information with the type.
	 * @since 2017/12/30
	 */
	public ProvidedInfo(Configuration[] __confs,
		Profile[] __profs, Standard[] __stds,
		String __intname, TypedSuite __typedsuite)
	{
		this((__confs == null ? null : Arrays.<Configuration>asList(__confs)),
			(__profs == null ? null : Arrays.<Profile>asList(__profs)),
			(__stds == null ? null : Arrays.<Standard>asList(__stds)),
			__intname, __typedsuite);
	}
	
	/**
	 * Initializes the provided dependency info.
	 *
	 * @param __confs The configurations provided.
	 * @param __profs The profiles provided.
	 * @param __stds The standards provided.
	 * @param __intname The internal project name.
	 * @param __typedsuite The suite information with the type.
	 * @since 2017/12/30
	 */
	public ProvidedInfo(Collection<Configuration> __confs,
		Collection<Profile> __profs, Collection<Standard> __stds,
		String __intname, TypedSuite __typedsuite)
	{
		this.configurations = (__confs == null || __confs.isEmpty() ?
			EmptySet.<Configuration>empty() :
			UnmodifiableSet.<Configuration>of(new SortedTreeSet<>(__confs)));
		this.profiles = (__profs == null || __profs.isEmpty() ?
			EmptySet.<Profile>empty() :
			UnmodifiableSet.<Profile>of(new SortedTreeSet<>(__profs)));
		this.standards = (__stds == null || __stds.isEmpty() ?
			EmptySet.<Standard>empty() :
			UnmodifiableSet.<Standard>of(new SortedTreeSet<>(__stds)));
		this.internalname = __intname;
		this.typedsuite = __typedsuite;
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
		Set<Standard> standards = new LinkedHashSet<>();
		value = attr.getValue("X-SquirrelJME-DefinedStandards");
		if (value != null)
			for (String s : StringUtils.basicSplit(",", value))
				standards.add(new Standard(s.trim()));
		
		// Has internal project name?
		String internalname = attr.getValue(
			"X-SquirrelJME-InternalProjectName");
		
		// This provides a library or otherwise
		TypedSuite typedsuite = new TypedSuite(
			__info.type(), __info.suite());
		
		// Initialize
		return new ProvidedInfo(configs, profiles, standards, internalname,
			typedsuite);
	}
	
	/**
	 * Returns the used configurations.
	 *
	 * @return The configurations.
	 * @since 2017/12/31
	 */
	public final Set<Configuration> configurations()
	{
		return this.configurations;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the internal name.
	 *
	 * @return The internal name or {@code null} if there is none.
	 * @since 2017/12/31
	 */
	public final String internalName()
	{
		return this.internalname;
	}
	
	/**
	 * Returns the profiles.
	 *
	 * @return The profiles.
	 * @since 2017/12/31
	 */
	public final Set<Profile> profiles()
	{
		return this.profiles;
	}
	
	/**
	 * Returns the standards.
	 *
	 * @return The standards.
	 * @since 2017/12/31
	 */
	public final Set<Standard> standards()
	{
		return this.standards;
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
	 * Returns the typed suite.
	 *
	 * @return The typed suite or {@code null} if there is none.
	 * @since 2017/12/31
	 */
	public final TypedSuite typedSuite()
	{
		return this.typedsuite;
	}
}

