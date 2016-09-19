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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a mutable version of {@link JavaManifest}.
 *
 * This class is not thread safe.
 *
 * @since 2016/09/19
 */
public class MutableJavaManifest
	extends AbstractMap<String, MutableJavaManifestAttributes>
{
	/** The maximum number of columns a manifest may have. */
	private static final int _COLUMN_LIMIT =
		72;
	
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
		
		// Create writer to write data
		Writer w = new OutputStreamWriter(__os, "utf-8");
		
		// Write main attribute first
		__write(w, getMainAttributes());
		
		// Write other attributes
		for (Map.Entry<String, MutableJavaManifestAttributes> e :
			this.attributes.entrySet())
		{
			// Ignore the main attribute
			String k = e.getKey();
			if (k.equals(""))
				continue;
			
			// Sub-attributes are always spaced after the previous one
			w.write("\r\n");
			
			// Write the name
			__write(w, "Name", k);
			
			// Write values
			__write(w, e.getValue());
		}
	}
	
	/**
	 * Writes attributes to the output.
	 *
	 * @param __w The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	private void __write(Writer __w, MutableJavaManifestAttributes __a)
		throws IOException, NullPointerException
	{
		// Check
		if (__w == null || __a == null)
			throw new NullPointerException("NARG");
		
		// The attribute version is always first
		JavaManifestKey verk = new JavaManifestKey("MANIFEST-VERSION");
		String ver = __a.get(verk);
		if (ver != null)
			__write(__w, "MANIFEST-VERSION", ver);
		
		// Write all value
		for (Map.Entry<JavaManifestKey, String> e : __a.entrySet())
		{
			// Do not write the version twice
			JavaManifestKey k = e.getKey();
			if (verk.equals(k))
				continue;
			
			// Write pair
			__write(__w, k.toString(), e.getValue());
		}
	}
	
	/**
	 * Writes the given key and value to the output.
	 *
	 * @param __w The stream to write to.
	 * @param __k The key to write.
	 * @param __v The value to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	private void __write(Writer __w, String __k, String __v)
		throws IOException, NullPointerException
	{
		// Check
		if (__w == null || __k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Write pair
		int col = 0;
		for (int z = 0; z < z; z++)
		{
			String s = (z == 0 ? __k : __v);
			
			// Print it
			int n = s.length();
			for (int i = 0; i < n; i++)
			{
				// Ignore out of range characters
				char c = s.charAt(i);
				if (c < ' ')
					continue;
				
				// Would be on a new line?
				int nextcol = col + 1;
				if (nextcol >= _COLUMN_LIMIT)
				{
					__w.write("\r\n");
					
					// Indent next line with space as long as this is not
					// the last character being written
					if (i + 1 < n)
						__w.write(' ');
					
					// Set next column
					nextcol = 1;
				}
				
				// Write the character
				__w.write(c);
				
				// Set new column
				col = nextcol;
			}
			
			// Add spacer
			if (z == 0)
			{
				__w.write(": ");
				col += 2;
			}
		}
	}
}

