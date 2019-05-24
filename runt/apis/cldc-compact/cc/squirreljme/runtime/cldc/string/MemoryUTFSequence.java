// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.string;

/**
 * This is a sequence which accesses memory and uses the UTF encoded form.
 *
 * @since 2019/04/30
 */
@Deprecated
public final class MemoryUTFSequence
	extends BasicSequence
{
	/** The memory pointer. */
	protected final int pointer;
	
	/**
	 * Initializes the sequence.
	 *
	 * @param __p The pointer.
	 * @since 2019/04/30
	 */
	public MemoryUTFSequence(int __p)
	{
		this.pointer = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/30
	 */
	@Override
	public final char charAt(int __i)
		throws StringIndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/30
	 */
	@Override
	public final int length()
	{
		throw new todo.TODO();
	}
}

