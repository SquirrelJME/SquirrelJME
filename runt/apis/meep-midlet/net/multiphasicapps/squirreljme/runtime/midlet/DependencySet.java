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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableSet;
import net.multiphasicapps.strings.StringUtils;
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
	/** Project vendor reference. */
	private static volatile Reference<MidletSuiteVendor> _PVENDOR;
	
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
	 * Returns a dependency set which is a conjuction of this set and the
	 * specified set. The resulting set will only include dependencies which
	 * match in both sets.
	 *
	 * @param __o The other set to conjucate with.
	 * @return The conjuction of the two sets.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/27
	 */
	public DependencySet conjunction(DependencySet __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		Set<ManifestedDependency> rv = new LinkedHashSet<>();
		Set<ManifestedDependency> fill = new LinkedHashSet<>(__o);
		
		// Go through self
		ManifestedDependency e;
		for (ManifestedDependency d : this)
		{
			Iterator<ManifestedDependency> it = fill.iterator();
			while (it.hasNext())
				if (__isCompatible(d, (e = it.next())))
				{
					rv.add(d);
					
					// Remove dependency from the fill because it has been
					// found, so there is no need to check it again for
					// matches in the future
					it.remove();
				}
		}
		
		return new DependencySet(rv);
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
		
		JavaManifestAttributes attr = __m.getMainAttributes();
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
	}
	
	/**
	 * Checks if the two manifested dependencies are compatible to each other.
	 *
	 * @param __a The first dependency.
	 * @param __b The second dependency.
	 * @return If they are equal or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/27
	 */
	private static boolean __isCompatible(ManifestedDependency __a,
		ManifestedDependency __b)
		throws NullPointerException
	{
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");
		
		// They are exactly the same
		if (__a.equals(__b))
			return true;
		
		// Midlet may depend on a standard, which is represented a bit
		// differently
		if (((__a instanceof MidletDependency) &&
				(__b instanceof APIStandard)) ||
			((__a instanceof APIStandard) &&
				(__b instanceof MidletDependency)))
		{
			MidletDependency a;
			APIStandard b;
			
			// Make sure these are always facing the same way
			if (__a instanceof MidletDependency)
			{
				a = (MidletDependency)__a;
				b = (APIStandard)__b;
			}
			else
			{
				a = (MidletDependency)__b;
				b = (APIStandard)__a;
			}
			
			throw new todo.TODO();
		}
		
		// Dependency on another dependency
		else if ((__a instanceof MidletDependency) &&
			(__b instanceof MidletDependency))
		{
			MidletDependency a = (MidletDependency)__a;
			MidletDependency b = (MidletDependency)__b;
			
			// Non-equal types
			if (!Objects.equals(a.type(), b.type()))
				return false;
			
			// Non-equal names?
			if (!Objects.equals(a.name(), b.name()))
				return false;
			
			// Non-equal vendors
			if (!Objects.equals(a.vendor(), b.vendor()))
				return false;
			
			// Ranges are more complicated to check
			MidletVersionRange va = a.version();
			MidletVersionRange vb = b.version();
		
			// Both have no ranges specified or one or the other has any
			// version
			if ((va == null && vb == null) ||
				MidletVersionRange.ANY_VERSION.equals(va) ||
				MidletVersionRange.ANY_VERSION.equals(vb))
				return true;
			
			// Compare ranges of the two
			if (va != null && vb != null)
				return va.inRange(vb);
		}
		
		// No match
		return false;
	}
	
	/**
	 * Returns the project vendor.
	 *
	 * @return The project vendor.
	 * @since 2017/11/26
	 */
	private static MidletSuiteVendor __projectVendor()
	{
		Reference<MidletSuiteVendor> ref = _PVENDOR;
		MidletSuiteVendor rv;
		
		if (ref == null || null == (rv = ref.get()))
			_PVENDOR = new WeakReference<>(
				(rv = new MidletSuiteVendor("Stephanie Gawroriski")));
			
		return rv;
	}
}

