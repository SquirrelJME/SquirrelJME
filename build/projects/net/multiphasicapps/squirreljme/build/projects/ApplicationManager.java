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
public abstract class ApplicationManager
	extends __Namespace__
{
	/**
	 * Initializes the application manager.
	 *
	 * @param __pm The owning project manager.
	 * @param __libs Source Liblets available to the application manager. 
	 * @param __mids Source MIDlets available to the application manager.
	 * @throws IllegalStateException If there is a hash collision between
	 * multiple midlets and liblets.
	 * @throws IOException On read errors.
	 * @since 2016/11/20
	 */
	ApplicationManager(ProjectManager __pm, Iterable<Path> __libs,
		Iterable<Path> __mids)
		throws IllegalStateException, IOException, NullPointerException
	{
		super(__pm, __libs, __mids);
		
		throw new Error("TODO");
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
			throw new Error("TODO");
			/*
			int n = projects.size(), i = 0;
			int[] rv = new int[n];
			for (Integer h : projects.keySet())
				rv[i++] = h;
			return rv;*/
		}
	}
}

