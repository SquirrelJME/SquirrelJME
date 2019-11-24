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
	 * Returns the entry this was aliased to.
	 *
	 * @param __dx The index to get.
	 * @return The index this was aliased to.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the class format is not valid.
	 * @since 2019/11/24
	 */
	public final int entryAliasedIndex(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		// The size is stored in the zero index, so this ensures that
		// zero always maps to zero!
		if (__dx == 0)
			return 0;
		
		// Read size of this pool and check the bounds
		BinaryBlob blob = this.blob;
		int count = blob.readJavaInt(0);
		if (__dx < 0 || __dx >= count || count < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Read the entry index now
		return blob.readJavaInt(__dx * 4);
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
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/24
	 */
	@Override
	public final int entryType(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.inherited.entryType(this.entryAliasedIndex(__dx));
	}
}

