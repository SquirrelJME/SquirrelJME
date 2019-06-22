// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This is a helper class used to read the configuration.
 *
 * @since 2019/06/22
 */
public final class ConfigReader
{
	/** The configuration base. */
	protected final int configbase;
	
	/**
	 * Initializes the configuration reader.
	 *
	 * @param __configbase The configuration base.
	 * @since 2019/06/22
	 */
	public ConfigReader(int __configbase)
	{
		this.configbase = __configbase;
	}
	
	/**
	 * Loads a key/value mapping of the given key, the resulting array will
	 * always be even and be key/value pairs.
	 *
	 * @param __key The key to locate.
	 * @return The resulting map, will be an empty array if no values were
	 * found.
	 * @since 2019/06/22
	 */
	public final String[] loadKeyValueMap(int __key)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Loads the given string for the given key.
	 *
	 * @param __key The key to load.
	 * @return The resulting string or {@code null} if it is not set.
	 * @since 2019/06/22
	 */
	public final String loadString(int __key)
	{
		return JVMFunction.jvmLoadString(this.search(__key));
	}
	
	/**
	 * Loads multiple string values.
	 *
	 * @param __key The key to locate.
	 * @return The resulting string values or {@code null} if there are none.
	 * @since 2019/06/22
	 */
	public final String[] loadStrings(int __key)
	{
		// Locate the key pointer
		int dp = this.search(__key);
		if (dp == 0)
			return null;
		
		// Read string count
		int n = Assembly.memReadJavaShort(dp, 0) & 0xFFFF;
		dp += 2;
		
		// Build resulting array
		String[] rv = new String[n];
		for (int i = 0; i < n; i++)
		{
			// Need to read the string length for skipping
			int strlen = Assembly.memReadJavaShort(dp, 0) & 0xFFFF;
			
			// Decode and store string
			rv[i] = JVMFunction.jvmLoadString(dp);
			
			// Skip
			dp += strlen + 2;
		}
		
		// Use this
		return rv;
	}
	
	/**
	 * Scans through the configuration space.
	 *
	 * @param __key The key to locate.
	 * @param __at The current at position of the scanner, {@code 0} will
	 * start a new search.
	 * @return The value pointer of the given item, or {@code 0} if there are
	 * no more entries.
	 * @since 2019/06/22
	 */
	public final int scan(int __key, int __at)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Searches the configuration space for the given key and returns it's
	 * value.
	 *
	 * @param __key The key to search for.
	 * @return The pointer to the configuration value or {@code 0} if it was
	 * not found.
	 * @since 2019/06/19
	 */
	public final int search(int __key)
	{
		// Seek through items
		for (int seeker = this.configbase;;)
		{
			// Read key and size
			int key = Assembly.memReadJavaShort(seeker,
					Constants.CONFIG_KEY_OFFSET),
				len = Assembly.memReadJavaShort(seeker,
					Constants.CONFIG_SIZE_OFFSET) & 0xFFFF;
			
			// Stop?
			if (key == ConfigRomType.END)
				break;
			
			// Found here?
			if (key == __key)
				return seeker + Constants.CONFIG_HEADER_SIZE;
			
			// Skip otherwise
			seeker += Constants.CONFIG_HEADER_SIZE + len;
		}
		
		// Not found
		return 0;
	}
}

