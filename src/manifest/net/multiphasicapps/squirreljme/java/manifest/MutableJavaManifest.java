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

import java.io.IOException;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a mutable version of {@link JavaManifest}.
 *
 * @since 2016/09/19
 */
public class MutableJavaManifest
	extends AbstractMap<String, MutableJavaManifestAttributes>
{
	/** Main attributes. */
	protected final Map<String, MutableJavaManifestAttributes> attributes =
		new LinkedHashMap<>();
	
	/**
	 * This initializes a new empty manifest.
	 *
	 * @since 2016/09/19
	 */
	public MutableJavaManifest()
	{
		// Always add a main attribute
		this.attributes.put("", new MutableJavaManifestAttributes());
	}
	
	/**
	 * Initializes a mutable manifest which is a copy of an existing manifest.
	 *
	 * @param __man The manifest to copy attributes from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	public MutableJavaManifest(JavaManifest __man)
		throws NullPointerException
	{
		// Check
		if (__man == null)
			throw new NullPointerException("NARG");
		
		// Go through and add
		for (Map.Entry<String, JavaManifestAttributes> e : __man.entrySet())
		{
			// Create new attribute set
			MutableJavaManifestAttributes attr;
			put(e.getKey(), (attr = new MutableJavaManifestAttributes()));
			
			// Copy values
			for (Map.Entry<JavaManifestKey, String> f :
				e.getValue().entrySet())
				attr.put(f.getKey(), f.getValue());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public final Set<Map.Entry<String, MutableJavaManifestAttributes>>
		entrySet()
	{
		return this.attributes.entrySet();
	}
	
	/**
	 * Returns the mapping of main attributes.
	 *
	 * @return The main attribute mapping.
	 * @since 2016/09/19
	 */
	public final MutableJavaManifestAttributes getMainAttributes()
	{
		return get("");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public final MutableJavaManifestAttributes put(String __k,
		MutableJavaManifestAttributes __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Put
		return this.attributes.put(__k, __v);
	}
	
	/**
	 * Writes the manifest data to the given output stream.
	 *
	 * @param __os The stream to get the manifest data written to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	public final void write(OutputStream __os)
		throws IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

