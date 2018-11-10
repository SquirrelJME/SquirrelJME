// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * This is a path set which is used to access files on the file system.
 *
 * @since 2017/11/28
 */
public final class FilePathSet
	implements CompilerPathSet
{
	/** The root directory of the path set. */
	protected final Path root;
	
	/**
	 * Initializes the path set over the given directory.
	 *
	 * @return __root The root directory
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public FilePathSet(Path __root)
		throws NullPointerException
	{
		if (__root == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.root = __root;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public void close()
		throws CompilerException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public CompilerInput input(String __n)
		throws CompilerException, NoSuchInputException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Store original name for the exception
		String origname = __n;
		
		// Resolve path elements
		Path path = this.root;
		while (!__n.isEmpty())
		{
			// Get name sub-fragment
			int sl = __n.indexOf('/');
			if (sl < 0)
				sl = __n.length();
			
			// Resolve it
			path = path.resolve(Paths.get(__n.substring(0, sl)));
			
			// Split down
			sl++;
			if (sl < __n.length())
				__n = __n.substring(sl);
			else
				break;
		}
		
		// {@squirreljme.error AQ06 The specified input does not exist in
		// the path set filesystem. (The name; The expected path)}
		if (Files.exists(path) && !Files.isDirectory(path))
			return new FileInput(path, origname);
		throw new NoSuchInputException(String.format("AQ06 %s %s", origname,
			path));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public Iterator<CompilerInput> iterator()
		throws CompilerException
	{
		// First iterate through every sub-directory and build a set of paths
		// from the root
		Deque<Path> queue = new ArrayDeque<>();
		queue.add(this.root);
		
		// Return value will be an array
		List<CompilerInput> rv = new ArrayList<>();
		
		// Process every directory
		while (!queue.isEmpty())
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(
				queue.removeFirst()))
			{
				for (Path p : ds)
				{
					// Handle directories later
					if (Files.isDirectory(p))
					{
						queue.addLast(p);
						continue;
					}
					
					// Add files to input
					rv.add(new FileInput(p,
						FilePathSet.__pathToName(root, p)));
				}
			}
			
			// {@squirreljme.error AQ07 Could not list directory contents.}
			catch (IOException e)
			{
				throw new CompilerException("AQ07", e);
			}
		
		// Return iterator over entries
		return Arrays.<CompilerInput>asList(rv.<CompilerInput>toArray(
			new CompilerInput[rv.size()])).iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public String toString()
	{
		return this.root.toString();
	}
	
	/**
	 * Converts a path found from the walk to a string used for package
	 * contents.
	 *
	 * @param __root Root directory.
	 * @param __sub Sub-path based on the root.
	 * @return The normalized name used for the contents key.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/01/29
	 */
	private static String __pathToName(Path __root, Path __sub)
		throws NullPointerException
	{
		// Check
		if (__root == null || __sub == null)
			throw new NullPointerException("NARG");
		
		// Calculate base
		Path base = __root.relativize(__sub);
		
		// Build it
		StringBuilder sb = new StringBuilder();
		for (Path comp : base)
		{
			// Directory slash
			if (sb.length() > 0)
				sb.append('/');
			
			// Add component
			sb.append(comp.toString());
		}
		
		// Translate it
		return sb.toString();
	}
}

