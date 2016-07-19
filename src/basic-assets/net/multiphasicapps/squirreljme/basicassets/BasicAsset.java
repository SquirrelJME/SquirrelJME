// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.basicassets;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This class provides access to the basic assets to be included in all
 * distributions of SquirrelJME.
 *
 * @since 2016/07/18
 */
public class BasicAsset
{
	/** Asset cache. */
	private static volatile Reference<Iterable<BasicAsset>> _ASSETS;
	
	/** The name of the asset. */
	protected final String name;
	
	/**
	 * Initializes the asset using the given resource name.
	 *
	 * @param __name The name of the asset.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/18
	 */
	private BasicAsset(String __name)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
	}
	
	/**
	 * Returns the name of the asset.
	 *
	 * @return The asset name.
	 * @since 2016/07/18
	 */
	public String name()
	{
		return this.name;
	}
	
	/**
	 * Opens the asset as a stream.
	 *
	 * @return The asset stream.
	 * @since 2016/07/18
	 */
	public InputStream open()
	{
		return BasicAsset.class.getResourceAsStream(this.name);
	}
	
	/**
	 * Obtains all of the basic assets which are available.
	 *
	 * @return The iterable sequence of basic assets.
	 * @since 2016/07/18
	 */
	public static Iterable<BasicAsset> getAssets()
	{
		Reference<Iterable<BasicAsset>> ref = _ASSETS;
		Iterable<BasicAsset> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Load assets
			List<BasicAsset> wrap = new LinkedList<>();
			wrap.add(new BasicAsset("readme.mkd"));
			wrap.add(new BasicAsset("license.mkd"));
			wrap.add(new BasicAsset("steven-gawroriski.gpg"));
			
			// Wrap it
			rv = UnmodifiableList.<BasicAsset>of(wrap);
			
			// Cache it
			_ASSETS = new WeakReference<>(rv);
		}
		
		// Return it
		return rv;
	}
}

