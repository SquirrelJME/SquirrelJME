// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.localinterp;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.StringTokenizer;
import net.multiphasicapps.interpreter.JVMClassPath;
import net.multiphasicapps.zips.StandardZIPFile;

/**
 * This is a single local class path element.
 *
 * @since 2016/03/06
 */
public class LocalClassPath
	extends JVMClassPath
{
	/** The local engine used. */
	protected final LocalEngine localengine;
	
	/** The used path. */
	protected final Path path;
	
	/** The ZIP file containing the JAR (potentially). */
	protected final StandardZIPFile zip;
	
	/**
	 * This is a local class path element.
	 *
	 * @param __le The local engine used.
	 * @param __p The path to the JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/06
	 */
	public LocalClassPath(LocalEngine __le, Path __p)
		throws NullPointerException
	{
		super(__le);
		
		// Check
		if (__le == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Set
		localengine = __le;
		path = __p;
		
		// Open file
		FileChannel fc = null;
		StandardZIPFile zippy = null;
		try
		{
			// Open file first
			fc = FileChannel.open(path, StandardOpenOption.READ);
			
			// Otherwise try to load one
			zippy = StandardZIPFile.open(fc);
			
			// Note it
			System.err.println("DEBUG -- Opened ZIP '" + path +
				"' (" + zippy + ").");
		}
		
		// Failed to open
		catch (IOException ioe)
		{
			// Close the channel
			if (fc != null)
				try
				{
					fc.close();
				}
				
				// Failed to close
				catch (IOException ioeb)
				{
					ioe.addSuppressed(ioeb);
				}
			
			// Print trace
			ioe.printStackTrace(System.err);
			
			// {@squirreljme.error LI0b Could not read the given path as a
			// ZIP file, it is either not a ZIP or is malformed. (The
			// file path)}
			System.err.printf("LI0b %s", path);
		}
		
		// Set ZIP
		zip = zippy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/02
	 */
	@Override
	public InputStream getResourceAsStream(String __res)
	{
		// Could fail to read
		try
		{
			// If not a ZIP, read from the file system
			if (zip == null)
			{
				// If it is not a directory then it cannot be treated as
				// one.
				if (!Files.isDirectory(path))
					return null;
				
				// Tokenize the input path resource
				Path at = path;
				StringTokenizer st = new StringTokenizer(__res, "/");
				while (st.hasMoreTokens())
				{
					// Resolve new path on this
					Path next = at.resolve(st.nextToken());
					
					// Get the parent of the new path, if it is not set or it
					// is not at, then the input path is not valid.
					Path par = next.getParent();
					if (par == null || !at.equals(par))
						return null;
					
					// Set new location
					at = next;
				}
				
				// Try opening a resource
				return Channels.newInputStream(FileChannel.open(
					at, StandardOpenOption.READ));
			}
			
			// Otherwise read from the ZIP file
			else
			{
				// Find resource
				StandardZIPFile.FileEntry ent = zip.get(__res);
				
				// Not found?
				if (ent == null)
					return null;
				
				// Open it
				return ent.open();
			}
		}
		
		// Cannot get resource, treat as null
		catch (IOException ioe)
		{
			// Print problem
			ioe.printStackTrace(System.err);
			
			// Failed
			return null;
		}
	}
}

