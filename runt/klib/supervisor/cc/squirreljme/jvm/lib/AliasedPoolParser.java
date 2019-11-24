// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.io.BinaryBlob;

/**
 * This class is used to provide access to the aliased pool where entries are.
 *
 * @since 2019/10/13
 */
public final class AliasedPoolParser
	extends AbstractPoolParser
{
	/** The blob. */
	protected final BinaryBlob blob;
	
	/** The inherited pool data. */
	protected final AbstractPoolParser inherited;
	
	/**
	 * Initializes the aliased pool parser.
	 *
	 * @param __b The blob for aliased data.
	 * @param __inh The inherited pool to source real data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/17
	 */
	public AliasedPoolParser(BinaryBlob __b, AbstractPoolParser __inh)
		throws NullPointerException
	{
		if (__b == null || __inh == null)
			throw new NullPointerException("NARG");
		
		this.blob = __b;
		this.inherited = __inh;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/24
	 */
	@Override
	public final BinaryBlob entryData(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		throw new todo.TODO();
	}
}

