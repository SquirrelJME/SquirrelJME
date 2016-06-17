// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmepackages;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Path;
import net.multiphasicapps.manifest.JavaManifest;
import net.multiphasicapps.manifest.JavaManifestAttributes;
import net.multiphasicapps.manifest.JavaManifestException;
import net.multiphasicapps.zips.ZipFile;

/**
 * This contains information about a single binary or source package.
 *
 * @since 2016/06/15
 */
public class PackageInfo
{
	/** The package manifest. */
	protected final JavaManifest manifest;
	
	/** The path to the package. */
	protected final Path path;
	
	/** Is this a ZIP file? */
	protected final boolean iszip;
	
	/** The name of this package. */
	protected final PackageName name;
	
	/**
	 * Initializes the package information from the given ZIP.
	 *
	 * @parma __p The path to the package.
	 * @param __zip The ZIP file for the package data.
	 * @throws InvalidPackageException If this is not a valid package.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	public PackageInfo(Path __p, ZipFile __zip)
		throws InvalidPackageException, IOException, NullPointerException
	{
		this(__p, true, __loadManifestFromZIP(__zip));
	}
	
	/**
	 * Initializes the package information using the given manifest.
	 *
	 * @param __p The path to the package.
	 * @param __zz Is this a ZIP file?
	 * @param __man The manifest data.
	 * @throws InvalidPackageException If this is not a valid package.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	private PackageInfo(Path __p, boolean __zz, JavaManifest __man)
		throws InvalidPackageException, NullPointerException
	{
		// Check
		if (__p == null || __man == null)
			throw new NullPointerException("NARG");
		
		// Set
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
		ZipFile.FileEntry ent = __zip.get("META-INF/MANIFEST.MF");
		
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

