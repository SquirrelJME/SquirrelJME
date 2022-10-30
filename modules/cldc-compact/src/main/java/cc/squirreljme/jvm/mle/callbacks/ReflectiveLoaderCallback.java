// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This interface is implemented with {@link ReflectionShelf} to allow for the
 * loading of classes at runtime rather than static AOT-compiled classes at
 * compile time.
 *
 * @since 2022/10/30
 */
@Api
public interface ReflectiveLoaderCallback
{
	/**
	 * Returns the class bytes which make up the given class.
	 * 
	 * @param __binaryName The binary name of the class.
	 * @return The bytes which make up the class or {@code null} if no such
	 * class exists.
	 * @throws MLECallError If the binary name is not valid, is an array, is
	 * a primitive type, or {@code null}.
	 * @since 2022/10/30
	 */
	@Api
	byte[] classBytes(String __binaryName)
		throws MLECallError;
}
