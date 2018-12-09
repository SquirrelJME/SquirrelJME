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

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;

/**
 * This is the factory which is used to create instances of
 * {@code SourceManager} which provides access to specific timespaces
 * depending on the needed context.
 *
 * @since 2017/11/10
 */
public class SourceManagerFactory
{
	/** The root of the source tree. */
	protected final Path root;
	
	/**
	 * Initializes the source manager.
	 *
	 * @param __root The root of the source tree.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/14
	 */
	public SourceManagerFactory(Path __root)
		throws NullPointerException
	{
		if (__root == null)
			throw new NullPointerException("NARG");
		
		this.root = __root;
	}
	
	/**
	 * Obtains the source manager which uses the specified types.
	 *
	 * @param __t The timespace to source projects from.
	 * @return The source manager over those packages.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/14
	 */
	public SourceManager get(TimeSpaceType __t)
		throws IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Only look for specific timespaces
		Set<TimeSpaceType> want = new HashSet<>();
		int basei = __t.ordinal();
		for (TimeSpaceType t : TimeSpaceType.values())
			if (t.ordinal() <= basei)
				want.add(t);
		
		// Go through root files searching for directories containing timespace
		// manifests
		Set<Path> from = new SortedTreeSet<>();
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(this.root))
		{
			for (Path p : ds)
			{
				// Ignore non-directories
				if (!Files.isDirectory(p))
					continue;
				
				// Read manifest
				JavaManifest man;
				try (InputStream is = Files.newInputStream(
					p.resolve("TIMESPACE.MF"), StandardOpenOption.READ))
				{
					man = new JavaManifest(is);
				}
				
				catch (NoSuchFileException e)
				{
					continue;
				}
				
				// Ignore unspecified timespaces
				String stype = man.getMainAttributes().getValue(
					"X-SquirrelJME-Timespace-Type");
				if (stype == null)
					continue;
				
				// Ignore unknown timespaces
				TimeSpaceType type = TimeSpaceType.ofString(stype);
				if (type == null)
					continue;
				
				// If this is a desired namespace, use that
				if (want.contains(type))
					from.add(p);
			}	
		}
		
		// Setup source manager
		return new SourceManager(__t,
			from.<Path>toArray(new Path[from.size()]));
	}
}

