// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

/**
 * This is used to describe how a local variable is accessed.
 *
 * @since 2016/05/12
 */
public final class NBCLocalAccess
{
	/** The index to read or write. */
	protected final int index;
	
	/** Is the value written here? */
	protected final boolean write;
	
	/** The type of variable to read or write. */
	protected final NBCVariableType type;
	
	/**
	 * Initailizes the local variable access.
	 *
	 * @param __dx The index of the local.
	 * @param __write Is this value written?
	 * @param __t The type of value which is read or written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	NBCLocalAccess(int __dx, boolean __write, NBCVariableType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.index = __dx;
		this.write = __write;
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the index of the written variable.
	 *
	 * @return The index accessed.
	 * @since 2016/05/13
	 */
	public int getIndex()
	{
		return this.index;
	}
	
	/**
	 * Is the local variable read?
	 *
	 * @return {@code true} if the local is read.
	 * @since 2016/05/13
	 */
	public boolean isRead()
	{
		return !this.write;
	}
	
	/**
	 * Is the local variable written to?
	 *
	 * @return {@code true} if the local is written to.
	 * @since 2016/05/13
	 */
	public boolean isWritten()
	{
		return this.write;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the type of value which was read or written to.
	 *
	 * @return The type of value to read or write.
	 * @since 2016/05/13
	 */
	public NBCVariableType type()
	{
		return this.type;
	}
}

