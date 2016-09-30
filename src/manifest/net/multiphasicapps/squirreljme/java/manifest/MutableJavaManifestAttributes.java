// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.manifest;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a mutable set of attributes which exist within a manifest.
 *
 * @since 2016/09/19
 */
public class MutableJavaManifestAttributes
	extends AbstractMap<JavaManifestKey, String>
{
	/** The manifest values. */
	protected final Map<JavaManifestKey, String> values =
		new LinkedHashMap<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public final Set<Map.Entry<JavaManifestKey, String>> entrySet()
	{
		return this.values.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public String put(JavaManifestKey __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return this.values.put(__k, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/29
	 */
	@Override
	public String remove(Object __k)
	{
		return this.values.remove(__k);
	}
}

