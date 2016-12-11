// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.Channels;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class is used to manage applications which are either MIDlets or
 * LIBlets.
 *
 * @since 2016/11/20
 */
public class ApplicationManager
{
	/** The owning project manager. */
	protected final ProjectManager projectman;
	
	/** Projects which are available for usage mapped by their suite ID. */
	private final Map<Integer, ApplicationProject> _projects;
	
	/**
	 * Initializes the application manager.
	 *
	 * @param __pm The owning project manager.
	 * @param __libs Source Liblets available to the application manager. 
	 * @param __mids Source MIDlets available to the application manager.
	 * @throws IllegalStateException If there is a hash collision between
	 * multiple midlets and liblets.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/20
	 */
	ApplicationManager(ProjectManager __pm, Iterable<Path> __libs,
		Iterable<Path> __mids)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__pm == null || __libs == null || __mids == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projectman = __pm;
		
		if (true)
			throw new Error("TODO");
		
		// Initialize projects depending on whether they are libraries or
		// midlets
		Map<Integer, ApplicationProject> projects = new SortedTreeMap<>();
		for (Iterator<Path> ll = __libs.iterator(), mm = __mids.iterator();;)
		{
			// Get the next path element to initialize
			Path rp;
			boolean ismidlet = false;
			if (ll.hasNext())
				rp = ll.next();
			else if ((ismidlet = mm.hasNext()))
				rp = mm.next();
			else
				break;
			
			// Go through the namespace and load projects in it
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(rp))
			{
				for (Path pp : ds)
				{
					// Ignore non-directories
					if (!Files.isDirectory(pp))
						continue;
					
					// Read the manifest
					JavaManifest man;
					try
					{
						man = ProjectManager.__readManifest(
							pp.resolve("META-INF").resolve("MANIFEST.MF"));
					}
					
					// Ignore
					catch (NoSuchFileException e)
					{
						continue;
					}
					
					// Initialize project
					ApplicationProject proj = (ismidlet ?
						new MidletProject(this, pp, man) :
						new LibletProject(this, pp, man));
			
					// {@squirreljme.error AT03 Hash collision between multiple
					// projects that share the same suite identification value.
					// (The old project; The new project)}
					int hash = proj.hashCode();
					ApplicationProject old;
					if (null != (old = projects.put(hash, proj)))
						throw new IllegalStateException(String.format(
							"AT03 %s %s",
							old.midletSuiteId(), proj.midletSuiteId()));
				}
			}
		}
		
		// Set
		this._projects = projects;
	}
	
	/**
	 * Returns the list of suite hashes currently available.
	 *
	 * @return An array of suite hashes.
	 * @since 2016/11/20
	 */
	public final int[] suiteHashes()
	{
		Map<Integer, ApplicationProject> projects = this._projects;
		synchronized (projects)
		{
			int n = projects.size(), i = 0;
			int[] rv = new int[n];
			for (Integer h : projects.keySet())
				rv[i++] = h;
			return rv;
		}
	}
}

