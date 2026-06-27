// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import cc.squirreljme.runtime.gcf.file.FileEndPointConnection;
import cc.squirreljme.runtime.gcf.file.pseudo.LibraryEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import cc.squirreljme.runtime.gcf.uri.UriPart;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.ConnectionOption;
import javax.microedition.io.Connector;

import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Factory to open {@code resource://} URIs.
 *
 * @since 2021/11/30
 */
@SquirrelJMEVendorApi
public class ResourceConnectionFactory
	implements CustomConnectionFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@SuppressWarnings("resource")
	@Override
	@SquirrelJMEVendorApi
	public Connection connect(UriPart __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AH0i Resource URI does not start with triple
		// slash. (The URI part)}
		UriGenericPart part = __part.asGeneric();
		if (!part.getPath().startsWith("/") ||
			(part.getAuthority() != null &&
				!"".equals(part.getAuthority().toString())))
			throw new ConnectionNotFoundException(
				__error__("AH0i %s", __part));
		
		/* {@squirreljme.error AH0j There is no current classpath. (The URI) */
		JarPackageBracket[] classPath = JarPackageShelf.classPath();
		if (classPath == null || classPath.length == 0 ||
			classPath[classPath.length - 1] == null)
			throw new ConnectionNotFoundException(
				__error__("AH0j %s", __part));
		
		// Determine the base library connection to use
		UriGenericPart base = LibraryEndPoint.libraryPart(
			classPath[classPath.length - 1], null);
		
		// Use the library resource handler instead to open this specific
		// file, considering that if the VM in Standalone uses Java SE the
		// ZIP endpoint can be very broken
		return Connector.open("file:" + base.withPath(part.getPath()),
			__mode);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/27
	 */
	@Override
	public boolean implementsInterface(Class<? extends Connection> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.isAssignableFrom(FileEndPointConnection.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	@SquirrelJMEVendorApi
	public String scheme()
	{
		return "resource";
	}
}
