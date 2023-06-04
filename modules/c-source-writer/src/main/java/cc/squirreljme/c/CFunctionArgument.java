// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * Argument for a C function.
 *
 * @since 2023/05/30
 */
public class CFunctionArgument
{
	/** The type of this argument. */
	public final CType type;
	
	/** The name of this argument. */
	public final String name;
	
	/**
	 * Initializes a function argument.
	 * 
	 * @param __type The type used.
	 * @param __name The name of the argument.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/30
	 */
	public CFunctionArgument(CType __type, String __name)
		throws NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @isnce 2023/05/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof CFunctionArgument))
			return false;
		
		CFunctionArgument o = (CFunctionArgument)__o;
		return this.name.equals(o.name) &&
			this.type.equals(o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @isnce 2023/05/30
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * Initializes a function argument.
	 * 
	 * @param __type The type used.
	 * @param __name The name of the argument.
	 * @return The created argument.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/30
	 */
	public static CFunctionArgument of(CType __type, String __name)
		throws NullPointerException
	{
		return new CFunctionArgument(__type, __name);
	}
}
