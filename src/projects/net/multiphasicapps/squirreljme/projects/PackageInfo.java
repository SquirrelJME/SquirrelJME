// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestException;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This contains information about a single binary or source package.
 *
 * @since 2016/06/15
 */
public class PackageInfo
{
	/** The owning package list. */
	protected final PackageList plist;
	
	/** The package manifest. */
	protected final JavaManifest manifest;
	
	/** The path to the package. */
	protected final Path path;
	
	/** Is this a ZIP file? */
	protected final boolean iszip;
	
	/** The name of this package. */
	protected final PackageName name;
	
	/** The dependencies of this package. */
	private volatile Reference<Set<PackageInfo>> _depends;
	
	/** Optional dependencies. */
	private volatile Reference<Set<PackageInfo>> _odepends;
	
	/** Recursive dependencies. */
	private volatile Reference<Set<PackageInfo>> _rdepends;
	
	/**
	 * Initializes the package information from the given ZIP.
	 *
	 * @param __l The package list which contains this package.
	 * @param __p The path to the package.
	 * @param __zip The ZIP file for the package data.
	 * @throws InvalidPackageException If this is not a valid package.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	public PackageInfo(PackageList __l, Path __p, ZipFile __zip)
		throws InvalidPackageException, IOException, NullPointerException
	{
		this(__l, __p, true, __loadManifestFromZIP(__zip));
	}
	
	/**
	 * Initializes the package information using the given manifest.
	 *
	 * @param __l The package list which contains this package.
	 * @param __p The path to the package.
	 * @param __zz Is this a ZIP file?
	 * @param __man The manifest data.
	 * @throws InvalidPackageException If this is not a valid package.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	private PackageInfo(PackageList __l, Path __p, boolean __zz,
		JavaManifest __man)
		throws InvalidPackageException, NullPointerException
	{
		// Check
		if (__l == null || __p == null || __man == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.plist = __l;
		this.path = __p;
		this.iszip = __zz;
		this.manifest = __man;
		
		// Get main attributes
		JavaManifestAttributes main = __man.getMainAttributes();
		
		// {@squirreljme.error CI04 The package manifest does not specify the
		// package name, it is likely not a package. (The path to the package)}
		String rname = main.get("X-SquirrelJME-Name");
		if (rname == null)
			throw new InvalidPackageException(String.format("CI04 %s", __p));
		
		// Set name
		this.name = new PackageName(rname);
	}
	
	/**
	 * Returns all of the packages that this package depends on.
	 *
	 * @return The set of packages this package depends on.
	 * @throws IllegalStateException If a dependency is missing.
	 * @since 2016/06/25
	 */
	public final Set<PackageInfo> dependencies()
		throws IllegalStateException
	{
		return dependencies(false);
	}
	
	/**
	 * Returns all of the packages that this package depends on.
	 *
	 * @param __opt Select optional dependencies instead.
	 * @return The set of packages this package depends on. If optional
	 * dependencies are specified, they are only included if they exist.
	 * @throws IllegalStateException If a dependency is missing.
	 * @since 2016/07/22
	 */
	public final Set<PackageInfo> dependencies(boolean __opt)
		throws IllegalStateException
	{
		// Get
		Reference<Set<PackageInfo>> ref = (__opt ? this._odepends :
			this._depends);
		Set<PackageInfo> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target
			Set<PackageInfo> deps = new LinkedHashSet<>();
			
			// Read the manifest
			JavaManifest man = manifest();
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// Get the dependency field
			String pids = attr.get((__opt ? "X-SquirrelJME-Optional" :
				"X-SquirrelJME-Depends"));
			PackageList plist = this.plist;
			if (pids != null)
			{
				int n = pids.length();
				for (int i = 0; i < n; i++)
				{
					char c = pids.charAt(i);
					
					// Ignore whitespace
					if (c <= ' ')
						continue;
					
					// Find the next comma
					int j;
					for (j = i + 1; j < n; j++)
					{
						char d = pids.charAt(j);
						
						if (d == ',')
							break;
					}
					
					// Split string
					String spl = pids.substring(i, j).trim();
					
					// {@squirreljme.error CI01 A required dependency of a
					// package does not exist. (This package; The missing
					// dependency)}
					PackageInfo ddd = plist.get(spl);
					if (!__opt && ddd == null)
						throw new IllegalStateException(String.format(
							"CI01 %s %s", this.name, spl));
					
					// Add it
					else if (ddd != null)
						deps.add(ddd);
					
					// Skip
					i = j;
				}
			}
			
			// Lock
			rv = UnmodifiableSet.<PackageInfo>of(deps);
			ref = new WeakReference<>(rv);
			if (__opt)
				this._odepends = ref;
			else
				this._depends = ref;
		}
		
		// Return
		return rv;
	}
	
	/**
	 * Returns the main entry class for a general Java program.
	 *
	 * @return The main class to enter the program at or {@code null} if not
	 * found.
	 * @since 2016/06/20
	 */
	public final String mainClass()
	{
		return this.manifest.getMainAttributes().get("Main-Class");
	}
	
	/**
	 * Returns the manifest of this package.
	 *
	 * @return The package manifest.
	 * @since 2016/06/15
	 */
	public final JavaManifest manifest()
	{
		return this.manifest;
	}
	
	/**
	 * Returns the name of this package.
	 *
	 * @return The package name.
	 * @since 2016/06/15
	 */
	public final PackageName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the path to this package.
	 *
	 * @return The package path.
	 * @since 2016/06/19
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * Returns a set containing this package and every dependency of this
	 * package and the dependencies of the dependencies.
	 *
	 * @return The set of all dependencies.
	 * @since 2016/07/22
	 */
	public final Set<PackageInfo> recursiveDependencies()
	{
		// Get
		Reference<Set<PackageInfo>> ref = this._rdepends;
		Set<PackageInfo> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target
			Set<PackageInfo> deps = new LinkedHashSet<>();
			
			// Start at the root
			Deque<PackageInfo> deq = new LinkedList<>();
			deq.offerLast(this);
			
			// Keep going
			while (!deq.isEmpty())
			{
				// Ignore already calculated packages
				PackageInfo pi = deq.pollFirst();
				if (deps.contains(pi))
					continue;
				
				// Add the current package
				deps.add(pi);
				
				// Add all dependencies to the queue
				for (PackageInfo z : pi.dependencies())
					deq.offerLast(z);
			}
			
			// Lock
			rv = UnmodifiableSet.<PackageInfo>of(deps);
			this._rdepends = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/25
	 */
	@Override
	public String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Loads a manifest from the given ZIP file.
	 *
	 * @parma __zip The ZIP to load the manifest from.
	 * @return The parsed manifest data.
	 * @throws InvalidPackageException If the package is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	private static JavaManifest __loadManifestFromZIP(ZipFile __zip)
		throws InvalidPackageException, IOException, NullPointerException
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Find manifest file
		ZipEntry ent = __zip.get("META-INF/MANIFEST.MF");
		
		// {@squirreljme.error CI02 No manifest exists in the JAR.}
		if (ent == null)
			throw new InvalidPackageException("CI02");
		
		// Open input stream
		try (InputStream is = ent.open())
		{
			return new JavaManifest(is);
		}
		
		// {@squirreljme.error CI03 The manifest is not correctly formed.}
		catch (JavaManifestException e)
		{
			throw new InvalidPackageException("CI03", e);
		}
	}
}

