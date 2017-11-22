// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import net.multiphasicapps.squirreljme.runtime.midlet.MidletDependency;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This represents a set of dependencies, since dependencies may be required
 * or optional.
 *
 * @since 2017/11/17
 */
public final class DependencyList
	extends AbstractList<MidletDependency>
	implements RandomAccess
{
	/** Dependencies that are used. */
	private final MidletDependency[] _deps;
	
	/**
	 * Initializes the dependency set from the given manifest.
	 *
	 * @param __m The manifest to parse.
	 * @throws InvalidDependencyException If the dependency list is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public DependencyList(JavaManifest __m)
		throws InvalidDependencyException, NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		JavaManifestAttributes attr = __m.getMainAttributes();
		
		// Normally required, configuration specifies CLDC and such
		String config = attr.getValue("microedition-configuration");
		if (config != null)
			throw new todo.TODO();
		
		// Normally required, this may or might not exist but normally when
		// binaries are generated any dependencies that rely on APIs will
		// be transformed to this
		String profiles = attr.getValue("microedition-profile");
		if (profiles != null)
			throw new todo.TODO();
		
		// Determine the prefix to use, for MIDlets or liblets
		String prefix = (attr.getValue("midlet-name") != null ?
			"midlet-dependency-" : "liblet-dependency-");
		
		// Parse entries in sequential order
		List<MidletDependency> deps = new ArrayList<>();
		for (int i = 1; i >= 1; i++)
		{
			// Stop if no more values are read
			String value = attr.getValue(prefix + i);
			if (value == null)
				break;
			
			throw new todo.TODO();
		}
		
		// Set
		this._deps = deps.<MidletDependency>toArray(
			new MidletDependency[deps.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/21
	 */
	@Override
	public MidletDependency get(int __i)
	{
		return this._deps[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/21
	 */
	@Override
	public int size()
	{
		return this._deps.length;
	}
}

