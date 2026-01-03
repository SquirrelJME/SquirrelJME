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
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import org.intellij.lang.annotations.Language;

import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Factory for creating {@link LinearScanEndPoint}.
 *
 * @since 2026/01/02
 */
@SquirrelJMEVendorApi
public class LinearScanEndPointFactory
	implements FileEndPointFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	public FileEndPoint connect(UriGenericPart __uri, int __mode,
		UriGenericPart __dotDot)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error GF0c Linear scan connection has an improper
		host. (The URI)} */
		UriAuthority auth = __uri.getAuthority();
		String fullHost = auth.host();
		if (fullHost == null ||
			!fullHost.startsWith(LinearScanEndPoint.DECODED_HOST) ||
			fullHost.length() <= LinearScanEndPoint.DECODED_HOST.length())
			throw new ConnectionNotFoundException(
				__error__("GF0c %s", __uri));
		
		// The desired URI that this is connecting to
		@Language("http-url-reference")
		String desiredUri = fullHost.substring(
			LinearScanEndPoint.DECODED_HOST.length());
		
		// This must be a readable connection
		Connection conn = Connector.open(desiredUri, Connector.READ);
		if (!(conn instanceof InputConnection))
		{
			conn.close();
			throw new ConnectionNotFoundException("RORO");
		}
		
		// Open the wrapped connection along with the linear stream
		// Attempt to open the wrapped connection
		return new LinearScanEndPoint(__uri, __mode,
			(InputConnection)conn, __dotDot);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	public boolean handleAuthority(UriAuthority __auth)
		throws NullPointerException
	{
		if (__auth == null)
			throw new NullPointerException("NARG");
		
		Debugging.debugNote("%s ?= %s / %s",
			LinearScanEndPoint.DECODED_HOST, __auth,
			__auth.host());
		
		// Ignore if no host was specified
		String host = __auth.host();
		if (host == null)
			return false;
		
		// Zips have a more unique way to specify the zip via the
		// authority by specifying the URI after the ://
		return host.startsWith(LinearScanEndPoint.DECODED_HOST);
	}
	
}
