// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.InputConnection;

/**
 * Connects to resources which are on the class path.
 *
 * I am assuming that i-mode does not support or does not recommend that
 * {@link Class#getResourceAsStream(String)} be used.
 *
 * @since 2021/11/30
 */
public class ResourceConnection
	implements InputConnection
{
	/** The pivot class for resource lookup. */
	private final Class<?> pivot;
	
	/** The name of the resource to open. */
	private final String rcName;
	
	/**
	 * Initializes the resource connection.
	 *
	 * @param __pivot The pivot class.
	 * @param __rcName The resource name.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/01
	 */
	public ResourceConnection(Class<?> __pivot, String __rcName)
		throws NullPointerException
	{
		if (__pivot == null || __rcName == null)
			throw new NullPointerException("NARG");
		
		this.pivot = __pivot;
		this.rcName = __rcName;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	public void close()
		throws IOException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	public DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(this.openInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		return this.pivot.getResourceAsStream(this.rcName);
	}
}
