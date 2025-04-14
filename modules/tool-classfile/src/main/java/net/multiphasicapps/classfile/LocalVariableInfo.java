// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.util.Objects;

/**
 * Contains local variable information.
 *
 * @since 2022/09/23
 */
public final class LocalVariableInfo
	implements Contexual
{
	/** Start PC address. */
	public final int startPc;
	
	/** The number of addresses this affects. */
	public final int length;
	
	/** The name of the field. */
	public final FieldName name;
	
	/** The field type. */
	public final FieldDescriptor type;
	
	/** The slot this variable is in. */
	public final int slot;
	
	/**
	 * Initializes the local variable information.
	 * 
	 * @param __startPc The start PC address.
	 * @param __length The length in the method.
	 * @param __name The name of the field.
	 * @param __type The type of the field.
	 * @param __slot The slot the variable is in.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/23
	 */
	public LocalVariableInfo(int __startPc, int __length, FieldName __name,
		FieldDescriptor __type, int __slot)
		throws NullPointerException
	{
		if (__name == null || __type == null)
			throw new NullPointerException("NARG");
		
		this.startPc = __startPc;
		this.length = __length;
		this.name = __name;
		this.type = __type;
		this.slot = __slot;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/23
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof LocalVariableInfo))
			return false;
		
		LocalVariableInfo that = (LocalVariableInfo)__o;
		return this.startPc == that.startPc && this.length == that.length &&
			this.slot == that.slot &&
			this.name.equals(that.name) && this.type.equals(that.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/23
	 */
	@Override
	public int hashCode()
	{
		return this.startPc ^ this.length ^ this.name.hashCode() ^
			this.type.hashCode() ^ this.slot;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/23
	 */
	@Override
	public String toString()
	{
		return "LocalVariableInfo{" + "startPc=" + this.startPc + 
			", length=" + this.length + ", name=" + this.name + 
			", type=" + this.type + ", slot=" + this.slot + '}';
	}
}
