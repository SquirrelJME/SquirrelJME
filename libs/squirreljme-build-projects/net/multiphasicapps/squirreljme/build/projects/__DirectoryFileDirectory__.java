// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.UnmodifiableMap;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.base.FileEntryNotFoundException;

/**
 * This is a file directory which wraps a directory tree and provides access
 * to its files.
 *
 * @since 2016/12/21
 */
class __DirectoryFileDirectory__
	implements FileDirectory
{
	/** Files in the directory. */
	protected final Map<String, Path> files;
	
	/**
	 * Initializes the file directory from the given directory.
	 *
	 * @param __p The path to wrap in the file directory.
	 * @throws IOException If the file directory could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/21
	 */
	__DirectoryFileDirectory__(Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Start at the root
		Deque<Path> q = new ArrayDeque<>();
		q.offer(__p);
		
		// Process queued directories
		Map<String, Path> files = new SortedTreeMap<>();
		Path dp;
		while (null != (dp = q.pollFirst()))
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(dp))
			{
				for (Path f : ds)
				{
					// Handle directories in another loop
					if (Files.isDirectory(f))
						q.offer(f);
					
					// Add the file to the map
					else
						files.put(__zipName(__p, f), f);
				}
			}
		
		// Store
		this.files = UnmodifiableMap.<String, Path>of(files);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public void close()
		throws IOException
	{
		// Has no effect
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public boolean contains(String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Check if it is in the file mapping
		return files.containsKey(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public Iterator<String> iterator()
	{
		return this.files.keySet().iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public InputStream open(String __fn)
		throws IOException, NullPointerException
	{
		// Check
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AT09 The file does not exist in the file
		// directory. (The file name which was requested)}
		Map<String, Path> files = this.files;
		Path p = files.get(__fn);
		if (p == null)
			throw new IOException(String.format("AT09 %s", __fn));
		
		// Open it
		try
		{
			return Channels.newInputStream(FileChannel.open(p,
				StandardOpenOption.READ));
		}
		
		// {@squirreljme.error AT0o Could not find the specified file entry.
		// (The entry name)}
		catch (NoSuchFileException e)
		{
			throw new FileEntryNotFoundException(
				String.format("AT0o %s", __fn), e);
		}
	}
	
	/**
	 * Calculates the name that a file would appear as inside of a ZIP file.
	 *
	 * @param __root The root path.
	 * @param __p The file to add.
	 * @return The ZIP compatible name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	private static String __zipName(Path __root, Path __p)
		throws NullPointerException
	{
		// Check
		if (__root == null || __p == null)
			throw new NullPointerException();
		
		// Calculate relative name
		Path rel = __root.toAbsolutePath().relativize(__p.toAbsolutePath());
		
		// Build name
		StringBuilder sb = new StringBuilder();
		for (Path comp : rel)
		{
			// Prefix slash
			if (sb.length() > 0)
				sb.append('/');
			
			// Add component
			sb.append(comp);
		}
		
		// Return it
		return sb.toString();
	}
}

