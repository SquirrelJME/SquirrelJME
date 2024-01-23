// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.event;

import java.util.Objects;

/**
 * Only valid in a given field.
 *
 * @since 2021/04/17
 */
public final class JDWPHostFieldOnly
{
	/** The type the field is in. */
	public final Object type;
	
	/** The field index. */
	public final int fieldDx;
	
	/**
	 * Represents a field only match.
	 * 
	 * @param __type The type containing the field.
	 * @param __fieldDx The field index.
	 * @since 2021/04/17
	 */
	public JDWPHostFieldOnly(Object __type, int __fieldDx)
	{
		this.type = __type;
		this.fieldDx = __fieldDx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof JDWPHostFieldOnly))
			return false;
		
		JDWPHostFieldOnly o = (JDWPHostFieldOnly)__o;
		return this.type == o.type &&
			this.fieldDx == o.fieldDx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.type) ^
			this.fieldDx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public String toString()
	{
		return String.format("FieldOnly[type=%s;fieldDx=%d]",
			this.type, this.fieldDx);
	}
}
