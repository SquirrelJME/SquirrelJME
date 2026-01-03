// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.gcf.uri.UriAuthority;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import java.util.ServiceLoader;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for file end-point handlers.
 * 
 * Used with {@link ServiceLoader}.
 *
 * @since 2025/12/29
 */
@SquirrelJMEVendorApi
public interface FileEndPointFactory
{
	/**
	 * Connects to the given endpoint.
	 *
	 * @param __uri The URI to connect to.
	 * @param __mode The mode the endpoint is opened in.
	 * @param __dotDot The optional {@code ..} to use to return to the parent
	 * directory.
	 * @return The resultant endpoint connection.
	 * @throws ConnectionNotFoundException If the endpoint was not found.
	 * @throws IOException On any other error.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/29
	 */
	@SquirrelJMEVendorApi
	FileEndPoint connect(UriGenericPart __uri,
		@MagicConstant(flagsFromClass = Connector.class) int __mode,
		@Nullable UriGenericPart __dotDot)
		throws ConnectionNotFoundException, IOException,
			NullPointerException;
	
	/**
	 * Checks if this end-point handles the given endpoint.
	 *
	 * @param __auth The authority to check.
	 * @return If this is handled.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/29
	 */
	@SquirrelJMEVendorApi
	boolean handleAuthority(UriAuthority __auth)
		throws NullPointerException;
	
	/**
	 * Does this need assistance with {@code ..} if that is desired?
	 *
	 * @param __part Which part does this relate to?
	 * @return If this needs {@code ..} assistance.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	boolean needDotDot(UriGenericPart __part)
		throws NullPointerException;
}
