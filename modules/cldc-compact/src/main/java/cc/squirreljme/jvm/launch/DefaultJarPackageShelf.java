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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.InputStream;

/**
 * Default shelf which just wraps {@link JarPackageShelf}.
 *
 * @since 2024/01/06
 */
public class DefaultJarPackageShelf
	implements VirtualJarPackageShelf
{
	/**
	 * {@inheritDoc}
	 * @since 2024/01/06
	 */
	@Override
	public boolean equals(JarPackageBracket __a, JarPackageBracket __b)
		throws MLECallError
	{
		return JarPackageShelf.equals(__a, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/06
	 */
	@Override
	public JarPackageBracket[] libraries()
	{
		return JarPackageShelf.libraries();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/06
	 */
	@Override
	public String libraryPath(JarPackageBracket __jar)
		throws MLECallError
	{
		return JarPackageShelf.libraryPath(__jar);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/06
	 */
	@Override
	public InputStream openResource(JarPackageBracket __jar, String __rc)
		throws MLECallError
	{
		return JarPackageShelf.openResource(__jar, __rc);
	}
}
