// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * A {@link VMClassLibrary} which also provides direct raw access to resources.
 *
 * @since 2023/12/30
 */
public interface RawVMClassLibrary
	extends VMClassLibrary
{
	/**
	 * Reads raw data from the given Jar.
	 *
	 * @param __jarOffset The offset into the Jar.
	 * @param __b The buffer to write to.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @throws IllegalStateException If the data could not be read.
	 * @throws IndexOutOfBoundsException If the read is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/30
	 */
	void rawData(int __jarOffset, byte[] __b, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException,
			NullPointerException;
	
	/**
	 * The raw size of the library.
	 * 
	 * @return The raw library size.
	 * @throws IllegalStateException If the size could not be read.
	 * @since 2023/12/30
	 */
	int rawSize()
		throws IllegalStateException;
}
