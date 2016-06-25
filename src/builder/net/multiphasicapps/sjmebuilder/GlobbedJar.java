// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.sjmepackages.PackageInfo;

/**
 * This represents an entire JAR which is full of class blobs and resource
 * blobs.
 *
 * The output file format of the glob is meant to be compact and simple.
 *
 * @since 2016/06/25
 */
public class GlobbedJar
{
	/** The path to the temporary directory. */
	protected final Path tempdir;
	
	/** The associated package information. */
	protected final PackageInfo pinfo;
	
	/** The resources that are a part of this JAR. */
	protected final Map<String, Path> resources =
		new HashMap<>();
	
	/** The JAR name. */
	protected final String name;
	
	/**
	 * Initializes the globbed JAR.
	 *
	 * @param __td The temporary output directory.
	 * @param __pi The package that it belongs to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	public GlobbedJar(Path __td, PackageInfo __pi)
		throws NullPointerException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.tempdir = __td;
		this.pinfo = __pi;
		
		// Determine the name used
		this.name = __pi.name().toString();
	}
	
	/**
	 * Creates a resource that will be placed in this globbed JAR.
	 *
	 * @param __name The name of the given resource.
	 * @return An output stream which writes to a temporary file where the
	 * resource data is placed before globbed Jar construction.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	public OutputStream createResource(String __name)
		throws IOException, NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Setup output
		Map<String, Path> resources = this.resources;
		try
		{
			// Setup temporary file
			Path p = Files.createTempFile(this.tempdir, this.name,
				__name.replace('/', '_'));
			
			// Record it
			resources.put(__name, p);
			
			// Open the file
			return Channels.newOutputStream(FileChannel.open(p,
				StandardOpenOption.WRITE));
		}
		
		// Failed to create, remove from the mapping
		catch (IOException|RuntimeException|Error e)
		{
			// Remove
			resources.remove(__name);
			
			// Retoss
			throw e;
		}
	}
}

