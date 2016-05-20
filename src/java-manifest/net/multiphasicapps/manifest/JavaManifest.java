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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * This contains decoders for the standard Java manifest format.
 *
 * This class is immutable.
 *
 * @since 2016/05/20
 */
public final class JavaManifest
	extends AbstractMap<String, String>
{
	/**
	 * Decodes the manifest from the given input stream, it is treated as
	 * UTF-8 as per the JAR specification.
	 *
	 * @param __is The input stream for the manifest data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public JavaManifest(InputStream __is)
		throws IOException, NullPointerException
	{
		this(new InputStreamReader(__is, "utf-8"));
	}
	
	/**
	 * Decodes the manifest from the given reader.
	 *
	 * @param __r The characters which make up the manifest.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public JavaManifest(Reader __r)
		throws IOException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
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

