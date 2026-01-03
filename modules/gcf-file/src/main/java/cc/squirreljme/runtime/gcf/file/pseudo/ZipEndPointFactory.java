// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.file.FileEndPointFactory;
import cc.squirreljme.runtime.gcf.uri.UriAuthority;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Connects to {@link ZipEndPoint}.
 *
 * @since 2025/12/30
 */
@SquirrelJMEVendorApi
public class ZipEndPointFactory
	implements FileEndPointFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public FileEndPoint connect(UriGenericPart __uri, int __mode,
		UriGenericPart __dotDot)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error GF0b Zip connection has an improper host.
		(The URI)} */
		UriAuthority auth = __uri.getAuthority();
		String fullHost = auth.host();
		if (fullHost == null ||
			!fullHost.startsWith(ZipEndPoint.DECODED_HOST) ||
			fullHost.length() <= ZipEndPoint.DECODED_HOST.length())
			throw new ConnectionNotFoundException(
				__error__("GF0b %s", __uri));
		
		// The desired zip, which is just an escaped recursive URI to the
		// underlying file data
		String desireZip = fullHost.substring(
			ZipEndPoint.DECODED_HOST.length());
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
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
		
		// Zips have a more unique way to specify the zip via the
		// authority by specifying the URI after the ://
		return host.startsWith(ZipEndPoint.DECODED_HOST);
	}
	
}
