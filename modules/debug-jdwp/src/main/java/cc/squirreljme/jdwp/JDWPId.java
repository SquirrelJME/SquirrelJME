// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a remote identifier.
 *
 * @since 2024/01/22
 */
public final class JDWPId
	implements Comparable<JDWPId>
{
	/** The ID used for this. */
	public final long id;
	
	/**
	 * Initializes the ID.
	 *
	 * @param __kind The kind of ID this is.
	 * @param __id The ID to use.
	 * @since 2024/01/22
	 */
	private JDWPId(JDWPIdKind __kind, long __id)
		throws NullPointerException
	{
		if (__kind == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public int compareTo(@NotNull JDWPId __o)
	{
		long a = this.id;
		long b = __o.id;
		
		if (a < b)
			return -1;
		else if (a > b)
			return 1;
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		else if (!(__o instanceof JDWPId))
			return false;
		
		return this.id == ((JDWPId)__o).id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public int hashCode()
	{
		long id = this.id;
		return ((int)(id >>> 32)) | ((int)id);
	}
	
	/**
	 * Returns the ID as an integer value.
	 *
	 * @return The integer ID value.
	 * @since 2024/01/22
	 */
	public int intValue()
	{
		return (int)this.id;
	}
	
	/**
	 * Returns the ID as a long value.
	 *
	 * @return The long ID value.
	 * @since 2024/01/22
	 */
	public long longValue()
	{
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public String toString()
	{
		return Long.toString(this.id);
	}
	
	/**
	 * Returns an ID with the given value.
	 *
	 * @param __id The ID value.
	 * @return The remote ID value.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public static JDWPId of(JDWPIdKind __kind, long __id)
		throws NullPointerException
	{
		return new JDWPId(__kind, __id);
	}
}
