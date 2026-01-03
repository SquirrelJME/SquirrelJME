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
 * Provides access to {@link AllVolumesEndPoint}.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class AllVolumesEndPointFactory
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
		
		return new AllVolumesEndPoint(__uri, __mode, __dotDot);
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
		
		// Must be a specific hostname
		return AllVolumesEndPoint.DECODED_HOST.equals(host);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	public boolean needDotDot(UriGenericPart __part)
		throws NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// This does not need assistance with dot-dot, it just points
		// to the current URI as there is only ever the root directory
		return false;
	}
}
