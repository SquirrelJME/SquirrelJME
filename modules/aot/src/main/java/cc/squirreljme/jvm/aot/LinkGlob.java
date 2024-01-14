// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.jvm.manifest.JavaManifest;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Base interface which describes a glob of linked binary.
 *
 * @since 2020/11/22
 */
public interface LinkGlob
	extends Closeable
{
	/**
	 * Indicates that compilation is complete and the final binary should
	 * be output.
	 * 
	 * @throws IOException On read/write errors.
	 * @since 2020/11/22
	 */
	void finish()
		throws IOException;
	
	/**
	 * Indicates that the compilation step is soon to start.
	 * 
	 * @throws IOException On read/write errors.
	 * @since 2023/05/28
	 */
	void initialize()
		throws IOException;
	
	/**
	 * Potentially remembers the main manifest, if this is important.
	 *
	 * @param __manifest The manifest to remember.
	 * @since 2023/07/25
	 */
	void rememberManifest(JavaManifest __manifest);
	
	/**
	 * Remembers the lists of tests.
	 *
	 * @param __tests The list of tests.
	 * @since 2023/07/25
	 */
	void rememberTests(List<String> __tests);
}
