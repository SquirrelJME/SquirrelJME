// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifestAttributes;

/**
 * This represents a single source project which contains the source code for
 * a single project.
 *
 * @since 2017/10/31
 */
public final class Source
{
	/** The name of the source. */
	protected final SourceName name;
	
	/** The path to the source code root. */
	protected final Path root;
	
	/** The manifest for the source code. */
	protected final JavaManifest manifest;
	
	/** Dependencies that this source code relies on. */
	private volatile Reference<DependencySet> _dependencies;
	
	/** The approximate binary manifest. */
	private volatile Reference<JavaManifest> _approxbm;
	
	/** The approximate binary dependency set. */
	private volatile Reference<DependencySet> _approxds;
	
	/** Last modified time of the source code. */
	private volatile long _lastmodtime =
		Long.MIN_VALUE;
	
	/**
	 * Initializes the project source.
	 *
	 * @param __name The name of the source.
	 * @param __p The path to the source code.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public Source(SourceName __name, Path __p)
		throws IOException, NullPointerException
	{
		if (__name == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.root = __p;
		
		// Load manifest
		try (InputStream in = Files.newInputStream(__p.resolve("META-INF").
			resolve("MANIFEST.MF"), StandardOpenOption.READ))
		{
			this.manifest = new JavaManifest(in);
		}
	}
	
	/**
	 * Returns the manifest which would be used to approximate how the binary
	 * manifest would be generated.
	 *
	 * @return The approximated binary manifest.
	 * @since 2017/11/17
	 */
	public final JavaManifest approximateBinaryManifest()
	{
		Reference<JavaManifest> ref = this._approxbm;
		JavaManifest rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Need the input manifest
			JavaManifest rman = manifest();
			JavaManifestAttributes rattr = rman.getMainAttributes();
			MutableJavaManifest wman = new MutableJavaManifest();
			MutableJavaManifestAttributes wattr = wman.getMainAttributes();
			
			// Handle fields for the main attributes
			int depdx = 1;
			for (Map.Entry<JavaManifestKey, String> e : rattr.entrySet())
			{
				JavaManifestKey k = e.getKey();
				String v = e.getValue();
				
				// Depends on the key, these are lowercase
				switch (k.toString())
				{
						// Dependencies
					case "x-squirreljme-depends":
						throw new todo.TODO();
					
						// Unhandled
					default:
						wattr.put(k, v);
						break;
				}
			}
			
			// Copy other attributes that may exist
			for (Map.Entry<String, JavaManifestAttributes> e : rman.entrySet())
				wman.put(e.getKey(),
					new MutableJavaManifestAttributes(e.getValue()));
			
			// Build
			this._approxbm = new WeakReference<>((rv = wman.build()));
		}
		
		return rv;
	}
	
	/**
	 * This returns a dependency set which is approximated from the
	 * approximated manifest for the binary.
	 *
	 * @return The approximated dependency set.
	 * @since 2017/11/17
	 */
	public final DependencySet approximateBinaryDependencySet()
	{
		Reference<DependencySet> ref = this._approxds;
		DependencySet rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._approxds = new WeakReference<>(
				(rv = new DependencySet(approximateBinaryManifest())));
		
		return rv;
	}
	
	/**
	 * Returns the set of dependencies which are needed for this project to
	 * build and operate correctly.
	 *
	 * @return The set of dependencies.
	 * @since 2017/11/17
	 */
	public final DependencySet dependencies()
	{
		// This is exactly the same as the approximate binary dependencies
		return approximateBinaryDependencySet();
	}
	
	/**
	 * Returns the time that the source code was last modified.
	 *
	 * @return The last modification date of the source code.
	 * @throws IOException On read errors.
	 * @since 2017/11/06
	 */
	public long lastModifiedTime()
	{
		// Could be pre-cached
		long rv = this._lastmodtime;
		if (rv != Long.MIN_VALUE)
			return rv;
		
		// Need to go through every single file in every directory, the date of
		// the newest file is used
		rv = Long.MIN_VALUE;
		try
		{
			// Start at the root directory
			Deque<Path> queue = new ArrayDeque<>();
			queue.add(this.root);
			
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
						
						// Use the newer file time
						FileTime ft = Files.getLastModifiedTime(p);
						if (ft != null)
						{
							long now = ft.toMillis();
							if (now > rv)
								rv = now;
						}
					}
				}
			
			// Cache for next time
			this._lastmodtime = rv;
			return rv;
		}
		
		// Error so the time cannot be known
		catch (IOException e)
		{
			return Long.MIN_VALUE;
		}
	}
	
	/**
	 * The source manifest.
	 *
	 * @return The source manifest.
	 * @since 2017/11/17
	 */
	public final JavaManifest manifest()
	{
		return this.manifest;
	}
}

