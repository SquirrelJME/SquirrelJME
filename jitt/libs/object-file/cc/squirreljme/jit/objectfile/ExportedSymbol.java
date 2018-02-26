// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

/**
 * This represents a symbol which is contained within the object file and
 * what it exports.
 *
 * @since 2018/02/23
 */
public final class ExportedSymbol
	extends Symbol
{
	/** The scope of this symbol. */
	protected final ExportedSymbol.Scope scope;
	
	/**
	 * Initializes the symbol.
	 *
	 * @param __name The name of the symbol.
	 * @param __scope The scope of the symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/25
	 */
	public ExportedSymbol(SymbolName __name, ExportedSymbol.Scope __scope)
		throws NullPointerException
	{
		super(__name);
		
		if (__scope == null)
			throw new NullPointerException("NARG");
		
		this.scope = __scope;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Imports the symbol so that it may be used as such.
	 *
	 * @return The imported variant of this symbol.
	 * @since 2018/02/23
	 */
	public final ImportedSymbol importSymbol()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the scope of this symbol.
	 *
	 * @return The symbol scope.
	 * @since 2018/02/25
	 */
	public final ExportedSymbol.Scope scope()
	{
		return this.scope;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This represents the scope of the exported symbol.
	 *
	 * @since 2018/02/23
	 */
	public static enum Scope
	{
		/** Globally visible, the same object file also sees this. */
		GLOBAL,
		
		/** Visible only to the same object file. */
		STATIC,
		
		/** End. */
		;
	}
}

