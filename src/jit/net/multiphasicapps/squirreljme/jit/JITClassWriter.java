// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This is used to write class details on output.
 *
 * @since 2016/07/06
 */
public interface JITClassWriter
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * Records the class flags.
	 *
	 * @param __cf The class flags.
	 * @throws JITException If the flags could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/18
	 */
	public abstract void classFlags(JITClassFlags __cf)
		throws JITException, NullPointerException;
	
	/**
	 * Records class interfaces.
	 *
	 * @param __ins The class interfaces.
	 * @throws JITException If the interfaces could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public abstract void interfaceClasses(ClassNameSymbol... __ins)
		throws JITException, NullPointerException;
	
	/**
	 * Records the name of the super-class of the class being decoded.
	 *
	 * @param __cn The name of the super class, may be {@code null} if there
	 * is none.
	 * @throws JITException If the super-class could not be written.
	 * @since 2016/07/22
	 */
	public abstract void superClass(ClassNameSymbol __cn)
		throws JITException;
}

