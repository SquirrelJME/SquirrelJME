// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import java.io.InputStream;
import java.io.IOException;

/**
 * This class represents a class library which represents a single JAR file
 * whether it exists on the disk or is virtually provided.
 *
 * @since 2018/09/13
 */
public interface SpringClassLibrary
{
	/**
	 * Returns the name of this library.
	 *
	 * @return The library name.
	 * @since 2018/09/13
	 */
	public abstract String name();
	
	/**
	 * Opens the specified resource as a stream.
	 *
	 * @param __rc The name of the resource to open.
	 * @return The stream to the resource data or {@code null} if it is not
	 * valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/13
	 */
	public abstract InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException;
}

