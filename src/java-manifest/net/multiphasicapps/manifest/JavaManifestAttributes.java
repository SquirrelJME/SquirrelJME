// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.manifest;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

/**
 * This contains the attributes for a single section within the manifest file.
 *
 * This class is immutable.
 *
 * @since 2016/05/20
 */
public final class JavaManifestAttributes
	extends AbstractMap<JavaManifestKey, String>
{
	/** The key value pairs. */
	protected final Map<JavaManifestKey, String> pairs;
	
	/**
	 * Initializes the manifest attributes.
	 *
	 * @param __from The map to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	JavaManifestAttributes(Map<JavaManifestKey, String> __from)
		throws NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		
		// Copy
		this.pairs = UnmodifiableMap.<JavaManifestKey, String>of(
			new HashMap<>(__from));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public boolean containsKey(Object __o)
	{
		return this.pairs.containsKey(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public Set<Map.Entry<JavaManifestKey, String>> entrySet()
	{
		return this.pairs.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public String get(Object __o)
	{
		return this.pairs.get(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public int size()
	{
		return this.pairs.size();
	}
}

