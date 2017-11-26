// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableSet;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This contains a set of dependencies.
 *
 * @since 2017/11/26
 */
public final class DependencySet
	extends AbstractSet<ManifestedDependency>
{
	/** The input set of dependencies. */
	protected final Set<ManifestedDependency> dependencies;
	
	/**
	 * Initializes the dependency set.
	 *
	 * @param __d The input dependencies.
	 * @throws ClassCastException If the input dependencies are not of the
	 * required class.
	 * @throws NullPointerException On null arguments or if any dependency
	 * specified is null.
	 * @since 2017/11/26
	 */
	public DependencySet(ManifestedDependency... __d)
		throws ClassCastException, NullPointerException
	{
		this(Arrays.<ManifestedDependency>asList(__d));
	}
	
	/**
	 * Initializes the dependency set.
	 *
	 * @param __d The input dependencies.
	 * @throws ClassCastException If the input dependencies are not of the
	 * required class.
	 * @throws NullPointerException On null arguments or if any dependency
	 * specified is null.
	 * @since 2017/11/26
	 */
	public DependencySet(Collection<ManifestedDependency> __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		Set<ManifestedDependency> rv = new LinkedHashSet<>();
		for (ManifestedDependency dep : __d)
			if (dep == null)
				throw new NullPointerException("NARG");
			else
				rv.add(dep);
		
		this.dependencies = UnmodifiableSet.<ManifestedDependency>of(rv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public boolean contains(Object __o)
	{
		return this.dependencies.contains(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public Iterator<ManifestedDependency> iterator()
	{
		return this.dependencies.iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public int size()
	{
		return this.dependencies.size();
	}
	
	/**
	 * Parses the specified manifest and returns a dependency set which
	 * specifies the dependencies which this manifest implements and as such
	 * specifies those which are required (or optional) for it to operate.
	 *
	 * @param __m The manifest to parse.
	 * @return The set of dependencies this implements.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	public static DependencySet neededByManifest(JavaManifest __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		JavaManifestAttributes attr = __m.getMainAttributes();
		Set<ManifestedDependency> deps = new LinkedHashSet<>();
		
		// Normally required, configuration specifies CLDC and such
		String config = attr.getValue("microedition-configuration");
		if (config != null)
			deps.add(new APIConfiguration(config.trim()));
		
		// Normally required, this may or might not exist but normally when
		// binaries are generated any dependencies that rely on APIs will
		// be transformed to this
		String profiles = attr.getValue("microedition-profile");
		if (profiles != null)
			for (APIProfile dep : APIProfile.parseList(profiles))
				deps.add(dep);
		
		// Determine the prefix to use, for MIDlets or liblets
		String prefix = (attr.getValue("midlet-name") != null ?
			"midlet-dependency-" : "liblet-dependency-");
		
		// Parse entries in sequential order
		for (int i = 1; i >= 1; i++)
		{
			// Stop if no more values are read
			String value = attr.getValue(prefix + i);
			if (value == null)
				break;
			
			// Decode dependency
			deps.add(new MidletDependency(value));
		}
		
		// Build
		return new DependencySet(deps);
	}
	
	/**
	 * Parses the specified manifest and returns a dependency set which
	 * specifies the dependencies which are implemented by the given manifest.
	 *
	 * @param __m The manifest to parse.
	 * @return The set of implemented dependencies.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	public static DependencySet providedByManifest(JavaManifest __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

