// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.jit.LinkTable;

/**
 * This is used to build a target executable for compilation.
 *
 * @since 2017/05/29
 */
public class TargetBuilder
{
	/** The target link table which contains the binary linkages. */
	protected final LinkTable linktable =
		new LinkTable();
	
	/**
	 * Initializes the target builder.
	 *
	 * @param __template The template file to load.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/29
	 */
	public TargetBuilder(String __template)
		throws IOException, NullPointerException
	{
		// Check
		if (__template == null)
			throw new NullPointerException("NARG");
		
		// Parse template files
		Map<String, String> options = new HashMap<>();
		__parse(options, __template);
		
		throw new todo.TODO();
	}
	
	/**
	 * Generates the output for the given target to the specified output
	 * stream.
	 *
	 * @param __os The stream to write the executable to.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/29
	 */
	public void run(OutputStream __os)
		throws IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 *
	 *
	 * @param __opt The target options.
	 * @param __in The input file to parse.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/29
	 */
	private static void __parse(Map<String, String> __opt, String __in)
		throws IOException, NullPointerException
	{
		// Check
		if (__opt == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Read input resource as a manifest
		try (InputStream is = TargetClass.class.getResourceAsStream(
			"template/" + __in))
		{
			// {@squirreljme.error AO06 The specified template does not
			// exist.}
			if (is == null)
				throw new IOException(String.format("AO06 %s"));
			
			// Parse the manifest
			JavaManifest man = new JavaManifest(is);
			for (Map.Entry<JavaManifestKey, String> e :
				man.getMainAttributes().entrySet())
			{
				throw new todo.TODO();
			}
		}
	}
}

