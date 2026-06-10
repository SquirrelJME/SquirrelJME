// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.control;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.CharSequenceUtils;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Map;

/**
 * This stores the actual values for metadata.
 *
 * @since 2026/06/10
 */
@SquirrelJMEVendorApi
public final class MetaDataValues
{
	/** Values stored within the mapping. */
	final Map<String, String> _values =
		new SortedTreeMap<>(CharSequenceUtils.<String>comparatorIgnoreCase());
	
	/**
	 * Not externally instantiated.
	 *
	 * @since 2026/06/10
	 */
	MetaDataValues()
	{
	}
	
	/**
	 * Sets the key to the given value.
	 *
	 * @param __k The key to set.
	 * @param __v The value to set.
	 * @throws NullPointerException If the key is {@code null}.
	 * @since 2026/06/10
	 */
	@SquirrelJMEVendorApi
	public final void set(String __k, String __v)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._values.put(__k, __v);
		}
	}
}
