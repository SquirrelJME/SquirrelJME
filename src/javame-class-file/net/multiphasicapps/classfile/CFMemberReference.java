// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.descriptors.IllegalSymbolException;
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This represents a reference type.
 *
 * @param V The symbol
 * @since 2016/03/15
 */
public abstract class CFMemberReference<V extends MemberTypeSymbol>
	extends CFConstantEntry
{
	/** The type to cast the type as. */
	protected final Class<V> castas;
	
	/** The class index. */
	protected final int classdx;
	
	/** The name and type index. */
	protected final int natdx;
	
	/**
	 * This initializes
	 *
	 * @param __icp The owning constant pool.
	 * @param __dis The constant data to load in.
	 * @param __cl The class to cast the type to.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	CFMemberReference(CFConstantPool __icp,
		DataInputStream __dis, Class<V> __cl)
		throws IOException, NullPointerException
	{
		super(__icp);
		
		// Check
		if (__dis == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		castas = __cl;
		
		// Read in
		classdx = __rangeCheck(__dis.readUnsignedShort());
		natdx = __rangeCheck(__dis.readUnsignedShort());
	}
	
	/**
	 * Returns the utilized class name.
	 *
	 * @return The class name for the member reference.
	 * @since 2016/03/15
	 */
	public final CFClassName className()
	{
		return pool.<CFClassName>getAs(classdx, CFClassName.class);
	}
	
	/**
	 * Returns the descriptor of this member.
	 *
	 * @return The method descriptor.
	 * @since 2016/03/29
	 */
	@SuppressWarnings({"unchecked"})
	public final V memberType()
	{
		return (V)nameAndType().getValue();
	}
	
	/**
	 * Returns the name and type of this member.
	 *
	 * @return The member name and type.
	 * @since 2016/03/29
	 */
	public final CFNameAndType nameAndType()
	{
		return pool.<CFNameAndType>getAs(natdx, CFNameAndType.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/15
	 */
	@Override
	public String toString()
	{
		return className() + "::" + nameAndType();
	}
}

