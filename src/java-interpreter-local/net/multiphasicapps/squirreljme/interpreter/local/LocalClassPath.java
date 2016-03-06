// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter.local;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.squirreljme.interpreter.InterpreterClassPath;
import net.multiphasicapps.squirreljme.zips.StandardZIPFile;

/**
 * This is a single local class path element.
 *
 * @since 2016/03/06
 */
public class LocalClassPath
	extends InterpreterClassPath
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
			throw new NullPointerException();
		
		// Set
		localengine = __le;
		path = __p;
		
		// Open file
		FileChannel fc = null;
		StandardZIPFile zippy = null;
		try
		{
			// Open file first
			fc = FileChannel.open(__p, StandardOpenOption.READ);
			
			// Otherwise try to load one
			zippy = StandardZIPFile.open(fc);
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
					RuntimeException toss = new RuntimeException(ioeb);
					toss.addSuppressed(ioe);
					throw toss;
				}
			
			// Print trace
			System.err.println("Failed to read the ZIP:");
			System.err.println(ioe);
			ioe.printStackTrace(System.err);
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
				// one
				if (!Files.isDirectory(path))
					return null;
				
				throw new Error("TODO");
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

