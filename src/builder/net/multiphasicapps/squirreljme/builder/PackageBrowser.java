// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITNamespaceBrowser;
import net.multiphasicapps.squirreljme.projects.PackageInfo;
import net.multiphasicapps.squirreljme.projects.PackageList;

/**
 * This is a browser which goes through packages and is able to search through
 * namespaces for compilation.
 *
 * @since 2016/09/10
 */
public class PackageBrowser
	implements JITNamespaceBrowser
{
	/** The list of packages available. */
	protected final PackageList plist;
	
	/** The owning build instance. */
	private final BuildInstance _instance;
	
	/**
	 * Initializes the package browser.
	 *
	 * @param __bi The build instance.
	 * @param __pl The package list.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/20
	 */
	PackageBrowser(BuildInstance __bi, PackageList __pl)
		throws NullPointerException
	{
		// Check
		if (__bi == null || __pl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._instance = __bi;
		this.plist = __pl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public JITNamespaceBrowser.Directory directoryOf(String __ns)
		throws IOException, JITException, NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public Iterator<String> listNamespaces()
		throws IOException
	{
		throw new Error("TODO");
	}
}

