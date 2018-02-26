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
 * This represents a symbol which is imported from another object file or
 * potentially the same object file.
 *
 * @since 2018/02/23
 */
public final class ImportedSymbol
	extends Symbol
{
	/** The scope of this symbol. */
	protected final ImportedSymbol.Scope scope;
	
	/**
	 * Initializes the symbol.
	 *
	 * @param __name The name of the symbol.
	 * @param __scope The scope of the symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/25
	 */
	public ImportedSymbol(SymbolName __name, ImportedSymbol.Scope __scope)
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
	 * Returns the scope of this symbol.
	 *
	 * @return The symbol scope.
	 * @since 2018/02/25
	 */
	public final ImportedSymbol.Scope scope()
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
	 * This represents the scope of the imported symbol.
	 *
	 * @since 2018/02/23
	 */
	public static enum Scope
	{
		/** An import from the global space in the object. */
		GLOBAL,
		
		/** An import from the local static space. */
		STATIC,
		
		/** Dynamically imported at runtime. */
		DYNAMIC,
		
		/** End. */
		;
	}
}

