// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This represents the name of a source project.
 *
 * @since 2017/10/31
 */
public final class SourceName
	implements Comparable<SourceName>
{
	/** The name string. */
	protected final String name;
	
	/**
	 * Initailizes the source name.
	 *
	 * @param __n The name of the source.
	 * @throws InvalidSourceNameException If the name is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public SourceName(String __n)
		throws InvalidSourceNameException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Is test
		boolean istest = __n.endsWith(".test");
		if (istest)
			__n = __n.substring(0, __n.length() - ".test".length());
		
		// Check for valid characters and lowercase
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __n.length(); i < n; i++)
		{
			char c = __n.charAt(i);
			
			// {@squirreljme.error AU0r The project source name contains an
			// invalid character. (The specified character)}
			if (c <= ' ' || c >= 0x7F)
				throw new InvalidSourceNameException(
					String.format("AU0r %c", c));
			
			// Lowercase
			else if (c >= 'A' && c <= 'Z')
				c = (char)((c - 'A') + 'a');
			
			sb.append(c);
		}
		
		// If this is a test, then add the test
		if (istest)
			sb.append(".test");
		
		// Set
		this.name = sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/31
	 */
	@Override
	public int compareTo(SourceName __n)
	{
		return this.name.compareTo(__n.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/31
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof SourceName))
			return false;
		
		return this.name.equals(((SourceName)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/31
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * Does this refer to a test project?
	 *
	 * @return If this refers to a test project.
	 * @since 2018/03/06
	 */
	public final boolean isTest()
	{
		return this.name().endsWith(".test");
	}
	
	/**
	 * Returns the source name.
	 *
	 * @return The source name.
	 * @since 2018/03/06
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * Returns the path to the file.
	 *
	 * @return The file name of the path.
	 * @since 2017/11/23
	 */
	public final Path toFileName()
	{
		return Paths.get(this.name + ".jar");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/31
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * Checks whether the the given path refers to a valid binary.
	 *
	 * @param __p The path to check.
	 * @return If the given path is a binary path.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/23
	 */
	public static final boolean isBinaryPath(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Ignore no file name
		Path fn = __p.getFileName();
		if (fn == null)
			return false;
		
		// Only use certain extensions
		String base = fn.toString();
		return base.endsWith(".jar") || base.endsWith(".JAR");
	}
	
	/**
	 * Returns the source name for this binary path.
	 *
	 * @param __p The path to translate to the source name.
	 * @return The source name for this associated binary path.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/05
	 */
	public static final SourceName ofBinaryPath(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Try to determine the base name of the path
		String base = __p.getFileName().toString();
		if (isBinaryPath(__p))
			return new SourceName(base.substring(0, base.length() - 4));
		return new SourceName(base);
	}
}

