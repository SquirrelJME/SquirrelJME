// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.narf.classfile.NCFClass;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIException;
import net.multiphasicapps.narf.classinterface.NCILookup;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zips.StandardZIPFile;

/**
 * This provides the means of locating classes which exist somewhere on the
 * classpath.
 *
 * @since 2016/04/20
 */
public class NILibrary
	extends NCILookup
{
	/** The boot classpath. */
	protected final Set<Path> bootpath;
	
	/** The classpath. */
	protected final Set<Path> classpath;
	
	/** Is a path form? */
	protected final Set<Path> isadir;
	
	/** Loaded ZIP files. */
	protected final Map<Path, StandardZIPFile> zips;
	
	/**
	 * Initializes the interpreter library which uses the real filesystem or
	 * ZIP files for class data.
	 *
	 * @param __bootcp The boot classpath.
	 * @param __cp The program classpath.
	 * @since 2016/04/20
	 */
	public NILibrary(Set<Path> __bootcp, Set<Path> __cp)
		throws NullPointerException
	{
		// Check
		if (__bootcp == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Setup boot classpath
		bootpath = UnmodifiableSet.<Path>of(new LinkedHashSet<>(__bootcp));
		
		// Setup standard classpath
		classpath = UnmodifiableSet.<Path>of(new LinkedHashSet<>(__cp));
		
		// Go through all paths and determine if they are directories or
		// ZIPS
		Set<Path> id = new HashSet<>();
		Map<Path, StandardZIPFile> zs = new HashMap<>();
		for (int i = 0; i < 2; i++)
			for (Path p : (i == 0 ? bootpath : classpath))
			{
				// Is this a directory?
				if (Files.isDirectory(p))
				{
					id.add(p);
					
					// Do not try reading as a ZIP
					continue;
				}
				
				// Otherwise load ZIP
				FileChannel fc = null;
				try
				{
					// Open file
					fc = FileChannel.open(p, StandardOpenOption.READ);
					
					// Set as ZIP
					zs.put(p, StandardZIPFile.open(fc));
				}
				
				// Failed to read
				catch (IOException e)
				{
					// Close the ZIP
					if (fc != null)
						try
						{
							fc.close();
						}
						
						// Failed to close that
						catch (IOException f)
						{
							e.addSuppressed(f);
						}
					
					// {@squirreljme.error AN09 Could not load the given path
					// as a ZIP file. (The path to the ZIP.)}
					throw new IllegalArgumentException(String.format("AN09 %s",
						p), e);
				}
			}
		
		// Lock in
		isadir = UnmodifiableSet.<Path>of(id);
		zips = UnmodifiableMap.<Path, StandardZIPFile>of(zs);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/21
	 */
	@Override
	protected NCIClass internalClassLookup(BinaryNameSymbol __bn)
		throws NCIException, NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Use the bootclasspath
		NCIClass rv;
		if (null != (rv = __loadClass(__bn, bootpath, true)))
			return rv;
		
		// Otherwise use the normal class
		return __loadClass(__bn, classpath, false);
	}
	
	/**
	 * Internally loads a given class.
	 *
	 * @param __bn The binary name of the class.
	 * @param __paths The set of paths to look in.
	 * @param __boot If {@code true} then this is looking in the boot class
	 * path.
	 * @return The loaded class or {@code null} if it does not exist.
	 * @throws NCIException If it failed to be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	private NCIClass __loadClass(BinaryNameSymbol __bn, Set<Path> __paths,
		boolean __boot)
		throws NCIException, NullPointerException
	{
		// Check
		if (__bn == null || __paths == null)
			throw new NullPointerException("NARG");
		
		// Go through the paths
		for (Path p : __paths)
		{
			try
			{
				// Lookin in a directory
				if (isadir.contains(p))
				{
					// Resolve the path going to the class
					Path res = p;
					int n = __bn.size();
					for (int i = 0; i < n - 1; i++)
						res = res.resolve(__bn.get(i).toString());
					
					// Resolve the last with the .class extension
					res = res.resolve(__bn.get(n - 1) + ".class");
					
					// Load file stream
					boolean wasopened = false;
					try (InputStream is = Channels.newInputStream(
						FileChannel.open(res, StandardOpenOption.READ)))
					{
						wasopened = true;
						return new NCFClass(is);
					}
					
					// Failed to open
					catch (IOException e)
					{
						// Since there is no {@code FileNotFoundException} then
						// if opening failed, the reason could be anything. So
						// if the body was never entered then it is possible
						// that the file does not exist at all. So in that case
						// ignore it.
						if (!wasopened && !Files.exists(res))
							continue;
						
						// Otherwise some other error, fail
						throw e;
					}
				}
			
				// Look in a ZIP
				else
				{
					// Get the ZIP
					StandardZIPFile zip = zips.get(p);
					
					// Build ZIP file name
					StringBuilder fn = new StringBuilder();
					int n = __bn.size();
					for (int i = 0; i < n; i++)
					{
						if (i > 0)
							fn.append("/");
						fn.append(__bn.get(i));
					}
					
					// Append class
					fn.append(".class");
					
					// See if it exists
					StandardZIPFile.FileEntry zf = zip.get(fn.toString());
					if (zf == null)
						continue;
					
					// Open it
					try (InputStream is = zf.open())
					{
						return new NCFClass(is);
					}
				}
			}
			
			// Failed read or other exception
			catch (NCIException|IOException e)
			{
				// {@squirreljme.error AN0a Failed to read a class from the
				// given path. (The binary name; The path read from)}
				throw new NCIException(NCIException.Issue.READ_ERROR,
					String.format("AN0a %s %s", __bn, p), e);
			}
		}
		
		// Not found
		return null;
	}
}

