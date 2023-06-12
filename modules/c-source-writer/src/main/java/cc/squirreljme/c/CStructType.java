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
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a structure type, which will have multiple members as types.
 *
 * @since 2023/06/06
 */
public class CStructType
	extends __CAbstractType__
{
	/** The name of this structure type. */
	protected final String name;
	
	/** Members within the struct. */
	protected final List<CVariable> members;
	
	/**
	 * Initializes the structure.
	 * 
	 * @param __name The struct name.
	 * @param __members The members of the struct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	CStructType(String __name, CVariable... __members)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
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
	 * @since 2023/06/06
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		// {@squirreljme.error CW04 Cannot dereference a struct.}
		throw new IllegalArgumentException("CW04");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public boolean isPointer()
	{
		throw Debugging.todo();
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
		if (__dx < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw Debugging.todo();
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
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/12
	 */
	@Override
	public List<String> tokens(CBasicTokenSet __set)
		throws NotTokenizableException, NullPointerException
	{
		if (__set == null)
			throw new NullPointerException("NARG");
		
		List<String> result;
		
		// Declaring a member of a struct, depends on if it is anonymous or not
		if (__set == CBasicTokenSet.STRUCT_MEMBER)
		{
			if (this.name == null)
				return this.tokens(CBasicTokenSet.STRUCT_DECLARATION);
			return this.tokens(CBasicTokenSet.STRUCT_DECLARATION);
		}
		
		// Declaring a struct is simple enough
		else if (__set == CBasicTokenSet.STRUCT_DECLARATION)
		{
			// Cannot tokenize an anonymous struct
			if (this.name == null)
				throw new NotTokenizableException(__set);
			
			result = Arrays.asList("struct", this.name);
		}
		
		// Definition of a struct
		else if (__set == CBasicTokenSet.STRUCT_DEFINITION)
		{
			// Open struct
			result = new ArrayList<>(
				this.tokens(CBasicTokenSet.STRUCT_DECLARATION));
			result.add("{");
			
			// Add each member
			List<CVariable> members = this.members;
			for (int i = 0, n = members.size(); i < n; i++)
			{
				CVariable var = members.get(i);
				
				result.addAll(var.tokens(CBasicTokenSet.STRUCT_MEMBER));
				result.add(";");
			}
			
			// Close struct
			result.add("}");
		}
		
		// Invalid
		else
			throw new NotTokenizableException(__set);
		
		return UnmodifiableList.of(result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/12
	 */
	@Override
	public List<String> tokens(CNamedTokenSet __set, CIdentifier __name)
		throws NotTokenizableException, NullPointerException
	{
		throw Debugging.todo();
	}
}
