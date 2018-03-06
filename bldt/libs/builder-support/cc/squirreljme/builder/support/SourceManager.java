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
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.collections.UnmodifiableCollection;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;

/**
 * This class is used to provide access to source code that is available as
 * compilation sources.
 *
 * @since 2017/10/31
 */
public final class SourceManager
	implements Iterable<Source>
{
	/** Sources which are available. */
	private final Map<SourceName, Source> _sources;
	
	/**
	 * Initializes the source code manager.
	 *
	 * @param __roots The root directories which contain namespaces where
	 * source projects are located.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public SourceManager(Path... __roots)
		throws IOException, NullPointerException
	{
		if (__roots == null)
			throw new NullPointerException("NARG");
		
		// Go through all the roots and detect the namespaces which are
		// valid
		Map<ProjectType, Set<Path>> namespaces = new SortedTreeMap<>();
		for (Path p : __roots)
			__scanNamespaces(p, namespaces);
		
		// Debug
		System.err.printf("DEBUG -- Found namespaces %s%n", namespaces);
		
		// Go through all namespaces and initialize any projects which may be
		// within directories
		Map<SourceName, Source> sources = new SortedTreeMap<>();
		for (Map.Entry<ProjectType, Set<Path>> e : namespaces.entrySet())
		{
			ProjectType type = e.getKey();
			
			// Go through path and decode projects
			for (Path p : e.getValue())
				__scanSources(p, sources, type);
		}
		
		// Set
		this._sources = sources;
	}
	
	/**
	 * Obtains the source by the given project name.
	 *
	 * @param __n The name of the source project.
	 * @return The source for the given project.
	 * @throws NoSuchSourceException If no source exists for the given name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public final Source get(String __n)
		throws NoSuchSourceException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return get(new SourceName(__n));
	}
	
	/**
	 * Obtains the source by the given project name.
	 *
	 * @param __n The name of the source project.
	 * @return The source for the given project.
	 * @throws NoSuchSourceException If no source exists for the given name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public final Source get(SourceName __n)
		throws NoSuchSourceException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AU0k No such source project exists. (The name
		// of the source project)}
		Source rv = this._sources.get(__n);
		if (rv == null)
			throw new NoSuchSourceException(String.format("AU0k %s", __n));
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/23
	 */
	@Override
	public final Iterator<Source> iterator()
	{
		return UnmodifiableCollection.<Source>of(this._sources.values()).
			iterator();
	}
	
	/**
	 * This scans the given directory for namespace directories and adds them
	 * to the given output path.
	 *
	 * @param __base The base path to scan for namespaces.
	 * @param __out The output map for namespace scans.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	private static final void __scanNamespaces(Path __base,
		Map<ProjectType, Set<Path>> __out)
		throws IOException, NullPointerException
	{
		if (__base == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Go through directories
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__base))
		{
			for (Path p : ds)
			{
				// Ignore non-directories
				if (!Files.isDirectory(p))
					continue;
				
				// Obtain the manifest
				JavaManifestAttributes attr;
				try (InputStream in = Files.newInputStream(
					p.resolve("NAMESPACE.MF"), StandardOpenOption.READ))
				{
					attr = new JavaManifest(in).getMainAttributes();
				}
				
				// No file here
				catch (NoSuchFileException e)
				{
					continue;
				}
				
				// Obtain the namespace type, ignore if none was specified
				String type = attr.getValue("X-SquirrelJME-Namespace-Type");
				if (type == null)
					continue;
				
				// See if it is a valid project type
				ProjectType ptype = ProjectType.ofString(type);
				if (ptype == null)
					continue;
				
				// Store into the map this path
				Set<Path> put = __out.get(ptype);
				if (put == null)
					__out.put(ptype, (put = new SortedTreeSet<>()));
				put.add(p);
			}
		}
	}
	
	/**
	 * This scans the given directory for source projects and adds them to the
	 * given map.
	 *
	 * @param __base The base directory to scan.
	 * @param __out The output map where projects are placed.
	 * @param __type The type 
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	private static final void __scanSources(Path __base,
		Map<SourceName, Source> __out, ProjectType __type)
		throws IOException, NullPointerException
	{
		if (__base == null || __out == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Go through directories
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__base))
		{
			for (Path p : ds)
			{
				// Ignore non-directories
				if (!Files.isDirectory(p))
					continue;
				
				// Could fail
				try
				{
					// Determine source name
					SourceName name = new SourceName(
						p.getFileName().toString());
					
					// Ignore test projects, they are only referred to once
					// there is a base supporting project
					if (name.isTest())
						continue;
					
					// Initialize source project, check that the manifest
					// exists so invalid projects are not created
					BasicSource src = new BasicSource(name, p, __type);
					if (src.sourceManifest() == null)
						continue;
					
					// Store it
					__out.put(name, src);
					
					// If a test project exists, initialize it
					SourceName tn = new SourceName(name.name() + ".test");
					Path tr = p.resolveSibling(tn.name());
					if (Files.isRegularFile(tr.resolve("META-INF").resolve(
						"TEST.MF")))
						__out.put(tn, new TestSource(tr, src));
				}
				
				// Ignore
				catch (InvalidSourceException e)
				{
					continue;
				}
			}
		}
	}
}

