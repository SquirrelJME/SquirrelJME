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
 * This represents a symbol which is used to refer to a section, an absolute
 * address, or an address relative to a section. Symbols may either be
 * exported or they may be imported.
 *
 * @since 2018/02/23
 */
public abstract class Symbol
{
	/** The name of this symbol. */
	protected final SymbolName name;
	
	/**
	 * Internally initialized.
	 *
	 * @param __name The name of this symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	Symbol(SymbolName __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Returns the name of this symbol.
	 *
	 * @return The symbol name.
	 * @since 2018/02/25
	 */
	public final SymbolName name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public abstract String toString();
}

