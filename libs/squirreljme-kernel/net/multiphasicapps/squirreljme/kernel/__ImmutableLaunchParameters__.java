// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.Arrays;
import java.util.Map;

/**
 * This class contains immutable launch parameters which were constructed by
 * the builder.
 *
 * @since 2016/11/08
 */
final class __ImmutableLaunchParameters__
	implements KernelLaunchParameters
{
	/** System property keys. */
	private final String[] _spkeys;
	
	/** System property values. */
	private final String[] _spvals;
	
	/** Command line values. */
	private final String[] _cmdline;
	
	/**
	 * Constructs the launch parameters.
	 *
	 * @param __b The source builder.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	__ImmutableLaunchParameters__(KernelLaunchParametersBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Add all system properties
		Map<String, String> properties = __b._properties;
		String[] pk = properties.keySet().<String>toArray(
			new String[properties.size()]);
		Arrays.sort(pk);
		int pn = pk.length;
		String[] pv = new String[pn];
		for (int i = 0; i < pn; i++)
			pv[i] = properties.get(pk[i]);
		this._spkeys = pk;
		this._spvals = pv;
		
		// Setup command line
		this._cmdline = __b._cmdline.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/16
	 */
	@Override
	public String[] getCommandLine()
	{
		return this._cmdline.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/08
	 */
	@Override
	public String getSystemProperty(String __k)
	{
		// Null will never be found
		if (__k == null)
			return null;
		
		// Search for it in the array, which is sorted
		int dx = Arrays.binarySearch(this._spkeys, __k);
		
		// Not found
		if (dx < 0)
			return null;
		
		// Otherwise use the value
		return this._spvals[dx];
	}
}

