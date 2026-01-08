// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.file.FileEndPointFactory;
import cc.squirreljme.runtime.gcf.uri.UriAuthority;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import org.intellij.lang.annotations.MagicConstant;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Provides access to {@link LibraryEndPoint}.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class LibraryEndPointFactory
	implements FileEndPointFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public FileEndPoint connect(UriGenericPart __uri,
		@MagicConstant(flagsFromClass = Connector.class) int __mode,
		UriGenericPart __dotDot)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error GF04 Library connection has an improper host.
		(The URI)} */
		UriAuthority auth = __uri.getAuthority();
		String fullHost = auth.host();
		if (fullHost == null ||
			!fullHost.startsWith(LibraryEndPoint.DECODED_HOST) ||
			fullHost.length() <= LibraryEndPoint.DECODED_HOST.length())
			throw new ConnectionNotFoundException(
				__error__("GF04 %s", __uri));
		
		// The desired index/name, is everything at the end
		String desireName = fullHost.substring(
			LibraryEndPoint.DECODED_HOST.length());
		
		// Match by ID?
		int desireId = -1;
		if (!desireName.isEmpty() && desireName.charAt(0) >= '0' &&
			desireName.charAt(0) <= '9')
			try
			{
				desireId = Integer.parseInt(desireName, 10);
			}
			catch (NumberFormatException ignored)
			{
			}
		
		// Go through libraries to determine which one to actually use
		JarPackageBracket jar = null;
		JarPackageBracket[] libs = JarPackageShelf.libraries();
		for (int n = libs.length, i = 0; i < n; i++)
		{
			// Jar happens to be blank? Skip
			JarPackageBracket lib = libs[i];
			if (lib == null)
				continue;
			
			// Matches this ID?
			if (i == desireId)
			{
				jar = lib;
				break;
			}
			
			// If the Jar has no known path, skip
			String path = JarPackageShelf.libraryPath(lib);
			if (path == null)
				continue;
			
			// Find the last slash for the basename, if applicable
			int ls = Math.max(path.lastIndexOf('/'),
				path.lastIndexOf('\\'));
			if (ls >= 0)
				path = path.substring(ls + 1);
			
			// Is this the Jar?
			if (path.equals(desireName))
			{
				jar = lib;
				break;
			}
		}
		
		/* {@squirreljme.error GF05 Could not find the specified Jar.
		(The URI)} */
		if (jar == null)
			throw new ConnectionNotFoundException(
				__error__("GF05 %s", __uri));
		
		// Open connection
		return new LibraryEndPoint(jar, __uri, __mode, __dotDot);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public boolean handleAuthority(UriAuthority __auth)
		throws NullPointerException
	{
		if (__auth == null)
			throw new NullPointerException("NARG");
		
		// Ignore if no host was specified
		String host = __auth.host();
		if (host == null)
			return false;
		
		// Libraries have a more unique way to specify the library via the
		// authority by specifying the index/name after the ://
		return host.startsWith(LibraryEndPoint.DECODED_HOST);
	}
	
}
