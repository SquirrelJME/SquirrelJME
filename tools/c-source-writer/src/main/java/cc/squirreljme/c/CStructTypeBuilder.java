// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.std.CTypeProvider;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder for struct types.
 *
 * @since 2023/06/06
 */
public final class CStructTypeBuilder
{
	/** The kind of struct this is. */
	protected final CStructKind kind;
	
	/** The name of the struct. */
	protected final CIdentifier name;
	
	/** Members within the struct. */
	protected final Map<CIdentifier, CVariable> members =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the struct __builder.
	 * 
	 * @param __kind The kind of struct this is.
	 * @param __name The name of this struct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	private CStructTypeBuilder(CStructKind __kind, CIdentifier __name)
		throws NullPointerException
	{
		if (__kind == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.kind = __kind;
		this.name = __name;
	}
	
	/**
	 * Builds the structure type.
	 * 
	 * @return The resultant struct.
	 * @since 2023/06/12
	 */
	public CStructType build()
	{
		Map<CIdentifier, CVariable> members = this.members;
		return new CStructType(this.kind, this.name,
			members.values().toArray(new CVariable[members.size()]));
	}
	
	/**
	 * Adds a struct member.
	 * 
	 * @param __type The member type.
	 * @param __name The member name.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	public CStructTypeBuilder member(CType __type, String __name)
		throws NullPointerException
	{
		return this.member(__type, CIdentifier.of(__name));
	}
	
	/**
	 * Adds a struct member.
	 * 
	 * @param __type The member type.
	 * @param __name The member name.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public CStructTypeBuilder member(CTypeProvider __type, String __name)
		throws NullPointerException
	{
		return this.member(__type, CIdentifier.of(__name));
	}
	
	/**
	 * Adds a struct member.
	 * 
	 * @param __type The member type.
	 * @param __name The member name.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the member already exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	public CStructTypeBuilder member(CType __type, CIdentifier __name)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CW06 Member already exists.} */
		Map<CIdentifier, CVariable> members = this.members;
		if (members.get(__name) != null)
			throw new IllegalArgumentException("CW06");
		
		members.put(__name, CVariable.of(__type, __name));
		
		return this;
	}
	
	/**
	 * Adds a struct member.
	 * 
	 * @param __type The member type.
	 * @param __name The member name.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the member already exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public CStructTypeBuilder member(CTypeProvider __type, CIdentifier __name)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return this.member(__type.type(), __name);
	}
	
	/**
	 * Declares member of struct.
	 *
	 * @param __var The variable to declare.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	public CStructTypeBuilder member(CVariable __var)
		throws NullPointerException
	{
		if (__var == null)
			throw new NullPointerException("NARG");
		
		return this.member(__var.type, __var.name);
	}
	
	/**
	 * Initializes the struct __builder.
	 * 
	 * @param __kind The kind of struct this is.
	 * @param __name The name of this struct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	public static CStructTypeBuilder builder(CStructKind __kind, String __name)
		throws NullPointerException
	{
		return CStructTypeBuilder.builder(__kind, CIdentifier.of(__name));
	}
	
	/**
	 * Initializes the struct __builder.
	 * 
	 * @param __kind The kind of struct this is.
	 * @param __name The name of this struct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	public static CStructTypeBuilder builder(CStructKind __kind,
		CIdentifier __name)
		throws NullPointerException
	{
		if (__kind == null || __name == null)
			throw new NullPointerException("NARG");
		
		return new CStructTypeBuilder(__kind, __name);
	}
}
