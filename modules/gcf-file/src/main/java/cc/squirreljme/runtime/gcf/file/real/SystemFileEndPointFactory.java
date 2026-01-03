// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.real;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
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
 * Provides access to {@link SystemFileEndPoint}.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class SystemFileEndPointFactory
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
		
		throw Debugging.todo();
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
		
		// There must be no actual host specified
		return __auth.host() == null;
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
		
		// Dot-dot is only useful for the root directory
		return __part.getPath().equals("/");
	}
}
