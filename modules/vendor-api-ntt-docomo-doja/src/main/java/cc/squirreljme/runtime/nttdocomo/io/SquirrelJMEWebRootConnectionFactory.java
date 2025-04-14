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
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import cc.squirreljme.runtime.gcf.HTTPAddress;
import cc.squirreljme.runtime.gcf.HTTPClientConnection;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.ConnectionOption;
import org.intellij.lang.annotations.Language;

/**
 * This is a factory that creates an HTTP connection which
 * are used for DoJa applications to access their web resources. Note that this
 * is intended to work standalone and not require internet access.
 *
 * @see SquirrelJMEWebRootManager
 * @since 2022/10/07
 */
@SquirrelJMEVendorApi
public class SquirrelJMEWebRootConnectionFactory
	implements CustomConnectionFactory
{
	/** The base scheme for this connection. */
	@Language("http-url-reference")
	@SquirrelJMEVendorApi
	public static final String URI_SCHEME =
		"squirreljme+doja";
	
	/** Webroot suffix. */
	@SquirrelJMEVendorApi
	public static final String WEBROOT_EXTENSION =
		"webroot";
	
	/** Manager state. */
	private static volatile SquirrelJMEWebRootManager _MANAGER;
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public Connection connect(String __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		// Debug
		Debugging.debugNote("DoJa connect(%s)", __part);
		
		// Get the existing manager, create one if not yet created
		SquirrelJMEWebRootManager manager;
		synchronized (SquirrelJMEWebRootConnectionFactory.class)
		{
			manager = SquirrelJMEWebRootConnectionFactory._MANAGER;
			if (manager == null)
			{
				// Attempt to locate the web root
				JarPackageBracket jar =
					SquirrelJMEWebRootConnectionFactory.__findWebRoot();
				
				// {@squirreljme.error AH0x There is no .webroot.jar for this
				// software.}
				if (jar == null)
					throw new ConnectionNotFoundException("AH0x");
				
				manager = new SquirrelJMEWebRootManager(jar);
				SquirrelJMEWebRootConnectionFactory._MANAGER = manager;
			}
		}
		
		// Setup non-network traversing HTTP connection to the manager
		// Uses DoJa specific HttpConnection
		try
		{
			return new DoJaHttpConnectionAdapter(new HTTPClientConnection(
				SquirrelJMEWebRootConnectionFactory.__fixAddress(manager,
					__part),
				new SquirrelJMEWebRootHTTPAgentConnector(manager)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public String scheme()
	{
		return SquirrelJMEWebRootConnectionFactory.URI_SCHEME;
	}
	
	/**
	 * Attempts to locate the webroot JAR to obtain resources from it.
	 * 
	 * @return The web root JAR or {@code null} if not found.
	 * @since 2022/10/11
	 */
	private static JarPackageBracket __findWebRoot()
	{
		// Get the Jar we launched with
		JarPackageBracket[] classPath = JarPackageShelf.classPath();
		JarPackageBracket launchJar = classPath[classPath.length - 1];
		
		// If there is no launch path, we cannot find the web root
		Debugging.todoNote("Add libraryName?");
		String launchPath = JarPackageShelf.libraryPath(launchJar);
		if (launchPath == null || launchPath.isEmpty())
			return null;
		
		// We need to know the extension of this
		int lastDot = launchPath.lastIndexOf('.');
		if (lastDot < 0)
			return null;
		
		// This is our extension
		String extension = launchPath.substring(lastDot + 1);
		
		// Our desired webroot is out string plus that extension
		String desirePath = launchPath.substring(0, lastDot) +
			"." + SquirrelJMEWebRootConnectionFactory.WEBROOT_EXTENSION + "." +
			extension;
		
		// Go through all the JARs and find this one
		for (JarPackageBracket library : JarPackageShelf.libraries())
			if (desirePath.equals(JarPackageShelf.libraryPath(library)))
				return library;
		
		// Not found
		return null;
	}
	
	/**
	 * Some SoJa software may be hardcoded in terms of character and address
	 * lengths so that it is unable to figure out or determine the hostname
	 * for an address.
	 * 
	 * @param __manager The manager used.
	 * @param __part The URI part to decode.
	 * @return The fixed or potentially normalized HTTP address.
	 * @throws ConnectionNotFoundException If there is no valid address.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/11
	 */
	private static HTTPAddress __fixAddress(
		SquirrelJMEWebRootManager __manager, String __part)
		throws IOException, NullPointerException
	{
		if (__manager == null || __part == null)
			throw new NullPointerException("NARG");
		
		// Some addresses might be missing their hostname due to truncation
		// and expected hardcoded URI or hostname lengths.
		HTTPAddress added = HTTPAddress.fromUriPartUnchecked(
			"//squirreljme/" + __part);
		HTTPAddress normal = HTTPAddress.fromUriPartUnchecked(__part);
		
		// If we add the hostname so that the URI path leads to an actual
		// valid file then we use this added onto path as a basis to obtain
		// whatever resource we are trying to get.
		if (added != null && __manager.pathExists(added.file))
			return added;
		
		// Normal path is valid
		if (normal != null)
			return normal;
		
		// {@squirreljme.error AH0y No path is valid. (The path)}
		throw new ConnectionNotFoundException("AH0y " + __part);
	}
}
