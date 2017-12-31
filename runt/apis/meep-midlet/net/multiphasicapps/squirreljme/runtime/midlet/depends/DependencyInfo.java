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
 * This contains the information which specifies all of the dependencies which
 * and application depends on.
 *
 * @since 2017/11/30
 */
public final class DependencyInfo
{
	/** The configuration. */
	protected final Configuration config;
	
	/** Profiles to use. */
	private final Set<Profile> profiles;
	
	/** Dependencies. */
	private final Set<SuiteDependency> depends;
	
	/**
	 * Initializes the dependency information.
	 *
	 * @param __config The configuration to use.
	 * @param __profiles The profiles to implement.
	 * @param __deps The dependencies to use.
	 * @since 2017/11/30
	 */
	public DependencyInfo(Configuration __config, Profile[] __profiles,
		SuiteDependency[] __deps)
	{
		this(__config,
			(__profiles == null ? null : Arrays.<Profile>asList(__profiles)),
			(__deps == null ? null : Arrays.<SuiteDependency>asList(__deps)));
	}
	
	/**
	 * Initializes the dependency information.
	 *
	 * @param __config The configuration to use.
	 * @param __profiles The profiles to implement.
	 * @param __deps The dependencies to use.
	 * @since 2017/11/30
	 */
	public DependencyInfo(Configuration __config,
		Collection<Profile> __profiles, Collection<SuiteDependency> __deps)
	{
		this.config = __config;
		this.profiles = (__profiles == null ? EmptySet.<Profile>empty() :
			UnmodifiableSet.<Profile>of(new SortedTreeSet<>(__profiles)));
		this.depends = (__deps == null ? EmptySet.<SuiteDependency>empty() :
			UnmodifiableSet.<SuiteDependency>of(new SortedTreeSet<>(__deps)));
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
	 * Checks if the dependency information is empty.
	 *
	 * @return If the dependency info is empty.
	 * @since 2017/11/30
	 */
	public final boolean isEmpty()
	{
		return this.config == null &&
			this.profiles.isEmpty() &&
			this.depends.isEmpty();
	}
	
	/**
	 * Matches this dependency information to see if any of the provided
	 * fields would statisfy the dependencies that are needed.
	 *
	 * @param __prov The provided information.
	 * @return The result of the match.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public final MatchResult match(ProvidedInfo __prov)
		throws NullPointerException
	{
		if (__prov == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns dependency information which contains no optional
	 * dependencies.
	 *
	 * @return Dependency information with no optionals.
	 * @since 2017/11/30
	 */
	public final DependencyInfo noOptionals()
	{
		// Ignore if there are no dependencies
		Set<SuiteDependency> depends = this.depends;
		if (depends.isEmpty())
			return this;
		
		// Get all non-optional dependencies
		Set<SuiteDependency> instead = new LinkedHashSet<>();
		for (SuiteDependency dep : depends)
			if (dep.isRequired())
				instead.add(dep);
		
		// There are no optional dependencies
		if (instead.size() == depends.size())
			return this;
		
		// Create new
		return new DependencyInfo(this.config, this.profiles, instead);
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
	 * Parses the given suite information and returns the dependency
	 * information.
	 *
	 * @param __info The suite information to parse.
	 * @return The parsed dependency information.
	 * @throws InvalidSuiteException If the dependencies is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/20
	 */
	public static final DependencyInfo of(SuiteInfo __info)
		throws InvalidSuiteException, NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		JavaManifestAttributes attr = __info.manifest().getMainAttributes();
		String value;
		
		// The CLDC library to use
		Configuration config = null;
		value = attr.getValue("microedition-configuration");
		if (value != null)
			config = new Configuration(value.trim());
		
		// Profiles needed to run
		Set<Profile> profiles = new LinkedHashSet<>();
		value = attr.getValue("microedition-configuration");
		if (value != null)
			for (String s : StringUtils.basicSplit(" \t", value))
				profiles.add(new Profile(s));
		
		// Parse entries in sequential order
		SuiteType type = __info.type();
		Set<SuiteDependency> dependencies = new LinkedHashSet<>();
		for (int i = 1; i >= 1; i++)
		{
			// Stop if no more values are read
			value = attr.getValue(type.dependencyKey(i));
			if (value == null)
				break;
			
			// Decode dependency
			dependencies.add(new SuiteDependency(value));
		}
		
		// Build
		return new DependencyInfo(config, profiles, dependencies);
	}
}

