// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.packets;

/**
 * This handles responses and additionally gives the ability to wait on
 * incoming responses to packets according to the key index. This allows
 * multiple threads to read from input key locks without blocking each other
 * when other threads need to themselves read responses. The more key locks
 * the less lock contention there will be.
 *
 * @since 2018/01/17
 */
final class __ResponseHandler__
{
	/**
	 * {@squirreljme.property cc.squirreljme.kernel.packets.responsekeycount=n
	 * This specifies the default number of key locks which will be used when
	 * waiting for a packet response. The higher the value the more locks will
	 * be available which means there will be a reduced chance of two threads
	 * waiting on the same key index. The value must be a power of two and be
	 * a positive integer.}
	 */
	public static final String KEY_COUNT_PROPERTY =
		"cc.squirreljme.kernel.packets.responsekeycount";
	
	/** The default key count. */
	public static final int DEFAULT_KEY_COUNT =
		Integer.highestOneBit(
		Math.max(Integer.getInteger(KEY_COUNT_PROPERTY, 8), 1));
	
	/** The key count. */
	protected final int count;
	
	/** The key mask. */
	protected final int mask;
	
	/**
	 * Initializes the response handler using the default key lock count.
	 *
	 * @since 2018/01/17
	 */
	__ResponseHandler__()
	{
		this(DEFAULT_KEY_COUNT);
	}
	
	/**
	 * Initializes the response handler using the specified key lock count.
	 *
	 * @param __kc The key lock count.
	 * @throws IllegalArgumentException If the count is zero, negative, or
	 * not a power of two.
	 * @since 2018/01/17
	 */
	__ResponseHandler__(int __kc)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AT0i Cannot have a response handler with a key
		// lock count of zero, a negative value, or not a power of two. (The
		// input value)}
		if (__kc <= 0 || Integer.bitCount(__kc) != 1)
			throw new IllegalArgumentException(String.format("AT0i %d", __kc));
		
		this.count = __kc;
		this.mask = __kc - 1;
		
		throw new todo.TODO();
	}
}

