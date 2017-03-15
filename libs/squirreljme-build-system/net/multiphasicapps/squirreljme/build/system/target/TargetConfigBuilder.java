// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system.target;

import java.util.Set;
import net.multiphasicapps.util.sorted.SortedTreeSet;

/**
 * Thie class is used to setup configurations which are used to adjust which
 * projects are included into the target along with a few other detauils.
 *
 * This class is not thread safe.
 *
 * @since 2017/03/13
 */
public class TargetConfigBuilder
{
	/** Group projects to include. */
	final Set<String> _groups =
		new SortedTreeSet<>();
	
	/**
	 * Initializes the target config builder.
	 *
	 * @since 2017/03/15
	 */
	public TargetConfigBuilder()
	{
		// Also include the required group
		this._groups.add("required");
	}
	
	/**
	 * Adds the specified group to be compiled.
	 *
	 * @param __s The group to add to be built.
	 * @throws NullPointerException If no group was specified.
	 * @since 2017/03/15
	 */
	public void addGroup(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Add
		this._groups.add(__lowercase(__s));
	}
	
	/**
	 * Builds the target configuration.
	 *
	 * @return The target configuration.
	 * @since 2017/03/13
	 */
	public final TargetConfig build()
	{
		return new TargetConfig(this);
	}
	
	/**
	 * Lowercases the specified string.
	 *
	 * @param __s The string to lowercase.
	 * @return The string that is lowercased.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/15
	 */
	static final String __lowercase(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Convert to lowercase
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			if (c >= 'A' && c <= 'Z')
				c = (char)((c - 'A') + 'a');
			
			sb.append(c);
		}
		
		return sb.toString();
	}
}

