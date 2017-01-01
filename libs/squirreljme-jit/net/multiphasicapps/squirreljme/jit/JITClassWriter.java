// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.ClassDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.ClassFlags;
import net.multiphasicapps.squirreljme.classformat.ConstantPool;
import net.multiphasicapps.squirreljme.classformat.FieldFlags;
import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This is used to write class details on output.
 *
 * @since 2016/07/06
 */
public interface JITClassWriter
	extends AutoCloseable, ClassDescriptionStream
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/18
	 */
	@Override
	public abstract JITFieldWriter field(FieldFlags __f, IdentifierSymbol __n,
		FieldSymbol __t)
		throws JITException, NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public abstract JITMethodWriter method(MethodFlags __f,
		IdentifierSymbol __n, MethodSymbol __t)
		throws JITException, NullPointerException;
}

