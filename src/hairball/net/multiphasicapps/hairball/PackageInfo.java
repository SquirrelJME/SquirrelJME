// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.hairball;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * This contains package information.
 *
 * @since 2016/02/28
 */
public class PackageInfo
{
	/** The package root. */
	protected final Path root;
	
	/** Is this package valid? */
	protected final boolean valid;
	
	/**
	 * The path to the package root.
	 *
	 * @param __p Package root.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/02/28
	 */
	public PackageInfo(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException();
		
		// Set
		root = __p;
		
		// Guessed manifest location
		Path mfp = root.resolve("META-INF").resolve("MANIFEST.MF");
		
		// Read the manifest
		boolean iv = false;
		try (FileChannel fc = FileChannel.open(mfp, StandardOpenOption.READ);
			BufferedReader br = new BufferedReader(new InputStreamReader(
				Channels.newInputStream(fc), "utf-8")))
		{
			// Read line by line
			for (;;)
			{
				// Read
				String ln = br.readLine();
				
				// EOF?
				if (ln == null)
					break;
				
				// 
			}
			
			// Is valid
			iv = true;
		}
		
		// Failed to read
		catch (IOException ioe)
		{
		}
		
		// Set validity
		valid = iv;
	}
	
	/**
	 * Is this a valid package?
	 *
	 * @return {@code true} if it is.
	 * @since 2016/02/28
	 */
	public boolean isValid()
	{
		return valid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
	 */
	@Override
	public String toString()
	{
		return root.getFileName().toString();
	}
	
	/**
	 * Performs an ASCII lowercase on the given string.
	 *
	 * @param __in Input string to lowercase.
	 * @return The lowercased string using only ASCII data.
	 * @since 2016/02/28
	 */
	private static final String __asciiLowerCase(String __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException();
		
		// Output
		StringBuilder sb = new StringBuilder();
		
		// Go through input string and lowercase
		int n = __in.length();
		for (int i = 0; i < n; i++)
		{
			char c = __in.charAt(i);
			
			// Lower it
			if (c >= 'A' && c <= 'Z')
				c = (char)('a' + (c - 'A'));
			
			sb.append(c);
		}
		
		// Build it
		return sb.toString();
	}
}

