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
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import net.multiphasicapps.io.hexdumpstream.HexDumpOutputStream;
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
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.builder.hexdump=(true/false)
	 * Sets whether or not created cached content should be hexdumped to the
	 * console for debugging purposes.}
	 */
	private static final boolean _HEX_DUMP_OUTPUT =
		Boolean.getBoolean("net.multiphasicapps.squirreljme.builder.hexdump");

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
	 * @since 2016/09/11
	 */
	@Override
	public OutputStream createCache(String __n)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Create
		OutputStream rv = Channels.newOutputStream(FileChannel.open(
			this._instance.temporaryDirectory().resolve(__n + ".squ"),
			StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW));
		
		// Hexdump-wrap?
		if (_HEX_DUMP_OUTPUT)
			return new HexDumpOutputStream(rv, System.err);
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public JITNamespaceBrowser.Directory directoryOf(String __ns)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DW04 The specified namespace does not end in the
		// JAR extension. (The namespace with the incorrect extension)}
		if (!__ns.endsWith(".jar"))
			throw new JITException(String.format("DW04 %s", __ns));
		
		// Remove it
		String base = __ns.substring(0, __ns.length() - 4);
		
		// {@squirreljme.error DW05 The specified package does not exist. (The
		// package name)}
		PackageInfo pi = this.plist.get(base);
		if (pi == null)
			throw new JITException(String.format("DW05 %s", base));
		
		// Create directory
		return new __Directory__(pi.path());
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

