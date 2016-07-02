// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This represents a link to another symbol.
 *
 * @param <S> The symbol type used for the reference.
 * @since 2016/07/02
 */
abstract class __SymbolReference__<S extends MemberTypeSymbol>
{
	/** The associated class. */
	protected final ClassNameSymbol memberclass;
	
	/** The linked member name. */
	protected final IdentifierSymbol membername;
	
	/** The linked member type. */
	protected final S membertype;
	
	/** The hash code of this reference. */
	protected final int hashcode;
	
	/**
	 * Initializes the reference to another symbol.
	 *
	 * @param __cn The class being referenced.
	 * @param __mn The referenced member name.
	 * @param __mt The referenced member type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/02
	 */
	private __SymbolReference__(ClassNameSymbol __cn, IdentifierSymbol __mn,
		S __mt)
		throws NullPointerException
	{
		// Check
		if (__cn == null || __mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.memberclass = __cn;
		this.membername = __mn;
		this.membertype = __mt;
		this.hashcode = __cn.hashCode() ^ __mn.hashCode() ^ __mt.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/02
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be the same
		if (!(__o instanceof __SymbolReference__))
			return false;
		
		// Check
		__SymbolReference__ o = (__SymbolReference__)__o;
		return this.hashcode == o.hashcode &&
			this.memberclass.equals(o.memberclass) &&
			this.membername.equals(o.membername) &&
			this.membertype.equals(o.membertype);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/02
	 */
	@Override
	public int hashCode()
	{
		return this.hashcode;
	}
	
	/**
	 * Returns the class the member is in.
	 *
	 * @return The class of the member.
	 * @since 2016/07/02
	 */
	public final ClassNameSymbol memberClass()
	{
		return this.memberclass;
	}
	
	/**
	 * Returns the name of the referenced member.
	 *
	 * @return The name of the member.
	 * @since 2016/07/02
	 */
	public final IdentifierSymbol memberName()
	{
		return this.membername;
	}
	
	/**
	 * Returns the type of the referenced member.
	 *
	 * @return The type of the member.
	 * @since 2016/07/02
	 */
	public final S memberType()
	{
		return this.membertype;
	}
	
	/**
	 * This is a reference to another field.
	 *
	 * @since 2016/07/02
	 */
	static final class __Field__
		extends __SymbolReference__<FieldSymbol>
	{
		/**
		 * Initializes the reference to another method.
		 *
		 * @param __i Is this an interface?
		 * @param __cn The class being referenced.
		 * @param __mn The referenced method name.
		 * @param __mt The referenced method type.
		 * @since 2016/07/02
		 */
		__Field__(boolean __i, ClassNameSymbol __cn,
			IdentifierSymbol __mn, FieldSymbol __mt)
		{
			super(__cn, __mn, __mt);
		}
	}
	
	/**
	 * This is a reference to another method.
	 *
	 * @since 2016/07/02
	 */
	static final class __Method__
		extends __SymbolReference__<MethodSymbol>
	{
		/** Is this an interface? */
		protected final boolean isinterface;
		
		/**
		 * Initializes the reference to another method.
		 *
		 * @param __i Is this an interface?
		 * @param __cn The class being referenced.
		 * @param __mn The referenced method name.
		 * @param __mt The referenced method type.
		 * @since 2016/07/02
		 */
		__Method__(boolean __i, ClassNameSymbol __cn,
			IdentifierSymbol __mn, MethodSymbol __mt)
		{
			super(__cn, __mn, __mt);
			
			this.isinterface = __i;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/02
		 */
		@Override
		public boolean equals(Object __o)
		{
			if (!(__o instanceof __Method__) || !super.equals(__o))
				return false;
			
			// Check
			return this.isinterface == ((__Method__)__o).isinterface;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/02
		 */
		@Override
		public int hashCode()
		{
			return super.hashCode() ^ (this.isinterface ? 1 : 0);
		}
		
		/**
		 * Is this a reference to an interface?
		 *
		 * @return If this is an interface reference.
		 * @since 2016/07/02
		 */
		public boolean isInterface()
		{
			return this.isinterface;
		}
	}
}

