// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;

/**
 * This describes all of the exceptions that are handled in the specified
 * method's code attribute.
 *
 * @since 2016/05/08
 */
public final class NCICodeExceptions
	extends AbstractList<NCICodeException>
{
	/** The constant pool for references. */
	protected final NCIPool pool;
	
	/** The buffer containing the handlers. */
	protected final NCIByteBuffer buffer;
	
	/** The number of exceptions. */
	protected final int count;
	
	/** Exception references. */
	private final Reference<NCICodeException>[] _refs;
	
	/**
	 * Initializes the set of exceptions.
	 *
	 * @param __pool The constant pool.
	 * @param __numh The number of exception handlers.
	 * @param __buf The buffer pertaining to the exception handlers.
	 * @throws IndexOutOfBoundsException If the number of exception handlers
	 * is negative.
	 * @since 2016/05/08
	 */
	NCICodeExceptions(NCIPool __pool, int __numh, NCIByteBuffer __buf)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__pool == null || __buf == null)
			throw new NullPointerException("NARG");
		if (__numh < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Set
		pool = __pool;
		buffer = __buf;
		
		// Setup
		count = __numh;
		_refs = __makeArray(count);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public NCICodeException get(int __i)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public int size()
	{
		return count;
	}
	
	/**
	 * Makes an array and casts it.
	 *
	 * @param __len The desired array length.
	 * @return A reference array.
	 * @since 2016/05/08
	 */
	@SuppressWarnings({"unchecked"})
	private Reference<NCICodeException>[] __makeArray(int __len)
	{
		return (Reference<NCICodeException>[])((Object)new Reference[__len]);
	}
}

