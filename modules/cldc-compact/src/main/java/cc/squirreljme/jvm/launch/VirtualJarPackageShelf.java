// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.InputStream;

/**
 * Virtual package shelf interface as a wrapper on top of
 * {@link JarPackageShelf}.
 *
 * @since 2024/01/06
 */
public interface VirtualJarPackageShelf
{
	/**
	 * As {@link JarPackageShelf#equals(JarPackageBracket, JarPackageBracket)}.
	 * 
	 * @param __a The first JAR.
	 * @param __b The second JAR.
	 * @return If these are equal or not.
	 * @throws MLECallError If either argument is {@code null}.
	 * @since 2024/01/06
	 */
	boolean equals(
		JarPackageBracket __a, JarPackageBracket __b)
		throws MLECallError;
	
	/**
	 * As {@link JarPackageShelf#libraries()}.
	 * 
	 * @return The libraries that are currently available.
	 * @since 2024/01/06
	 */
	JarPackageBracket[] libraries();
	
	/**
	 * As {@link JarPackageShelf#libraryPath(JarPackageBracket)}.
	 * 
	 * @param __jar The JAR to get the path of.
	 * @return The path of the given JAR.
	 * @throws MLECallError If the JAR is not valid.
	 * @since 2024/01/06
	 */
	String libraryPath(JarPackageBracket __jar)
		throws MLECallError;
	
	/**
	 * As {@link JarPackageShelf#openResource(JarPackageBracket, String)}.
	 *
	 * @param __jar The JAR to open.
	 * @param __rc The resource to load from the given JAR.
	 * @return Input stream to the resource, may be {@code null} if it does
	 * not exist.
	 * @throws MLECallError If the JAR is not valid or the resource was not
	 * specified.
	 * @since 2024/01/06
	 */
	InputStream openResource(JarPackageBracket __jar, String __rc)
		throws MLECallError;
}
