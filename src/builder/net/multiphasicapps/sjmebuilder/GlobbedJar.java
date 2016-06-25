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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	}
}

