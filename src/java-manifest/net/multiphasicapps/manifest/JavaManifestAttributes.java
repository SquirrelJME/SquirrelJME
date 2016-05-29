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
	extends AbstractMap<String, String>
{
	/**
	 * Initializes the manifest attributes.
	 *
	 * @since 2016/05/20
	 */
	JavaManifestAttributes()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public Set<Map.Entry<String, String>> entrySet()
	{
		throw new Error("TODO");
	}
}

