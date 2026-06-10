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
import cc.squirreljme.runtime.media.AbstractControl;
import javax.microedition.media.control.MetaDataControl;
import org.jetbrains.annotations.NotNull;

/**
 * This stores media metadata which is used for any author or copyright
 * information.
 *
 * @since 2026/06/10
 */
@SquirrelJMEVendorApi
public class AbstractMetaDataControl
	extends AbstractControl<MetaDataControl>
	implements MetaDataControl
{
	/** The composer of the media. */
	@SquirrelJMEVendorApi
	public static final String COMPOSER_KEY =
		MetaDataControl.AUTHOR_KEY;
	
	/** Values stored within the mapping. */
	private final MetaDataValues _values =
		new MetaDataValues();
	
	/**
	 * Initializes the metadata control.
	 *
	 * @param __out This is used by the player setting the metadata
	 * accordingly.
	 * @throws NullPointerException On null arguments or if the array has
	 * zero length.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	public AbstractMetaDataControl(@NotNull MetaDataValues[] __out)
		throws NullPointerException
	{
		super(MetaDataControl.class);
		
		if (__out == null || __out.length == 0)
			throw new NullPointerException("NARG");
		
		__out[0] = this._values;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public final String getKeyValue(String __k)
		throws IllegalArgumentException
	{
		// Yes this really does throw this exception here
		if (__k == null)
			throw new IllegalArgumentException("NARG");
		
		// Get the value
		String result;
		MetaDataValues values = this._values;
		synchronized (values)
		{
			result = values._values.get(__k);
		}
		
		// Not valid?
		if (result == null)
			throw new IllegalArgumentException("INVL");
		
		// Otherwise, return it
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public final String[] getKeys()
	{
		// Always use the current set of keys
		MetaDataValues values = this._values;
		synchronized (values)
		{
			return values._values.keySet()
				.toArray(new String[values._values.size()]);
		}
	}
}
