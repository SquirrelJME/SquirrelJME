// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.FileAddress;
import java.io.IOException;
import java.io.InputStream;

/**
 * This acts as a virtual web root HTTP server that is used for DoJa based
 * titles to access their web based resources and otherwise. Since these titles
 * themselves have content on a remote server end, this allows that to still
 * run accordingly.
 * 
 * This will keep HTTP state accordingly and also handle any potential CGI
 * requests for any files that should be served over CGI for example.
 * 
 * Software expects this to be HTTP compatible accordingly.
 *
 * @since 2022/10/07
 */
@SquirrelJMEVendorApi
public class SquirrelJMEWebRootManager
{
	/** The JAR that makes up our webroot. */
	@SquirrelJMEVendorApi
	protected final JarPackageBracket jar;
	
	/**
	 * Initializes the web root.
	 * 
	 * @param __jar The JAR that makes up the web root.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/11
	 */
	@SquirrelJMEVendorApi
	public SquirrelJMEWebRootManager(JarPackageBracket __jar)
		throws NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		this.jar = __jar;
	}
	
	/**
	 * Checks whether the given path exists within the virtual webroot.
	 * 
	 * @param __file The file to check.
	 * @return If the path exists in the virtual webroot.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/11
	 */
	@SquirrelJMEVendorApi
	public boolean pathExists(FileAddress __file)
		throws IOException, NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		// Remove all starting slashes
		String jarPath = __file.toString();
		while (jarPath.startsWith("/"))
			jarPath = jarPath.substring(1);
		
		// Debug
		Debugging.debugNote("pathExists(%s) -> %s", __file, jarPath);
		
		// If the path is blank, it always exists and likely leads to an index
		if (jarPath.isEmpty())
			return true;
		
		// Look within the JAR for the resource by attempting to open it, it
		// will exist if the stream is not null
		try (InputStream in = JarPackageShelf.openResource(this.jar, jarPath))
		{
			return in != null;
		}
	}
}
