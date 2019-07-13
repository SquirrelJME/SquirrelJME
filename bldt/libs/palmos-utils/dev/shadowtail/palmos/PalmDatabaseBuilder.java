// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.palmos;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is used to build PalmOS databases and resource databases.
 *
 * @since 2019/07/13
 */
public final class PalmDatabaseBuilder
{
	/** The type of database to create. */
	protected final PalmDatabaseType type;
	
	/**
	 * Initializes the database builder.
	 *
	 * @param __type The database type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public PalmDatabaseBuilder(PalmDatabaseType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
	}
	
	/**
	 * Adds the specified entry.
	 *
	 * @param __type The entry type.
	 * @param __id The ID to use.
	 * @return The stream to the entry data.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final OutputStream addEntry(String __type, int __id)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the creator of the database.
	 *
	 * @param __creat The creator to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setCreator(String __creat)
		throws NullPointerException
	{
		if (__creat == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the name of the database.
	 *
	 * @param __name The name to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setName(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the type of the database.
	 *
	 * @param __type The type to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setType(String __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Writes the database to the output.
	 *
	 * @param __out The output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final void write(OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

