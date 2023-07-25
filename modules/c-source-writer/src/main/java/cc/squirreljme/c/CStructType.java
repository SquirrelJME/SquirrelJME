// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a structure type, which will have multiple members as types.
 *
 * @since 2023/06/06
 */
public class CStructType
	extends __CAbstractType__
{
	/** The kind of struct this is. */
	protected final CStructKind kind;
	
	/** The name of this structure type. */
	protected final CIdentifier name;
	
	/** Members within the struct. */
	protected final List<CVariable> members;
	
	/**
	 * Initializes the structure.
	 * 
	 * @param __kind The kind of struct this is.
	 * @param __name The struct name.
	 * @param __members The members of the struct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	CStructType(CStructKind __kind, CIdentifier __name,
		CVariable... __members)
		throws NullPointerException
	{
		if (__kind == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.kind = __kind;
		this.name = __name;
		this.members = UnmodifiableList.of(Arrays.asList(
			(__members != null ? __members.clone() : new CVariable[0])));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType constType()
		throws IllegalArgumentException
	{
		return CModifiedType.of(CConstModifier.CONST, this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public List<String> declareTokens(CIdentifier __name)
	{
		// This is just "struct/union whatever"
		List<String> result = new ArrayList<>(3);
		result.add((this.kind == CStructKind.STRUCT ? "struct" : "union"));
		result.add(this.name.identifier);
		
		// Is there an identifier?
		if (__name != null)
			result.add(__name.identifier);
			
		return UnmodifiableList.of(result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		/* {@squirreljme.error CW04 Cannot dereference a struct.} */
		throw new IllegalArgumentException("CW04");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof CStructType))
			return false;
		
		CStructType o = (CStructType)__o;
		return Objects.equals(this.name, o.name) &&
			this.members.equals(o.members) &&
			this.kind.equals(o.kind);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.name) ^
			this.members.hashCode() ^
			this.kind.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public boolean isPointer()
	{
		// Not a pointer type
		return false;
	}
	
	/**
	 * Returns the member by the index.
	 * 
	 * @param __dx The index to get.
	 * @return The variable that is the member.
	 * @throws IndexOutOfBoundsException If the member index is not valid.
	 * @since 2023/06/03
	 */
	public CVariable member(int __dx)
		throws IndexOutOfBoundsException
	{
		if (__dx < 0 || __dx >= this.members.size())
			throw new IndexOutOfBoundsException("IOOB");
		
		return this.members.get(__dx);
	}
	
	/**
	 * Returns the member by the given name.
	 * 
	 * @param __identifier The name of the member.
	 * @return The variable that is the member.
	 * @throws IllegalArgumentException If the member is not valid.
	 * @throws NoSuchElementException If no such member exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/06
	 */
	public CVariable member(String __identifier)
		throws IllegalArgumentException, NoSuchElementException,
			NullPointerException
	{
		return this.member(new CIdentifier(__identifier));
	}
	
	/**
	 * Returns the member by the given name.
	 * 
	 * @param __identifier The name of the member.
	 * @return The variable that is the member.
	 * @throws NoSuchElementException If no such member exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/06
	 */
	public CVariable member(CIdentifier __identifier)
		throws NoSuchElementException, NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		for (CVariable member : this.members)
			if (__identifier.equals(member.name))
				return member;
		
		throw new NoSuchElementException(
			String.format("NSEE %s in %s", __identifier, this.name));
	}
	
	/**
	 * Returns the members of the structure.
	 * 
	 * @return The structure members.
	 * @since 2023/07/03
	 */
	public List<CVariable> members()
	{
		return this.members;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		return CPointerType.of(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/03
	 */
	@Override
	public String toString()
	{
		return this.declareTokens(null).toString();
	}
}
