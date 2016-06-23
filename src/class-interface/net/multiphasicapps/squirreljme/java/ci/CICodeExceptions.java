// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import net.multiphasicapps.squirreljme.java.symbols.BinaryNameSymbol;

/**
 * This describes all of the exceptions that are handled in the specified
 * method's code attribute.
 *
 * @since 2016/05/08
 */
public final class CICodeExceptions
	extends AbstractList<CICodeException>
{
	/** The constant pool for references. */
	protected final CIPool pool;
	
	/** The buffer containing the handlers. */
	protected final CIByteBuffer buffer;
	
	/** The number of exceptions. */
	protected final int count;
	
	/** Exception references. */
	private final Reference<CICodeException>[] _refs;
	
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
	CICodeExceptions(CIPool __pool, int __numh, CIByteBuffer __buf)
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
	public CICodeException get(int __i)
	{
		// Check
		if (__i < 0 || __i >= count)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock references
		Reference<CICodeException>[] refs = _refs;
		synchronized (refs)
		{
			// Check reference
			Reference<CICodeException> ref = refs[__i];
			CICodeException rv;
			
			// In reference?
			if (ref == null || null == (rv = ref.get()))
			{
				// Calculate positio
				CIByteBuffer buf = buffer;
				int base = (8 * __i);
				
				// Is there a binary name?
				CIUTF utf = pool.<CIUTF>nullableAs(buffer.readUnsignedShort(
					base, 6), CIUTF.class);
				
				// Build it
				refs[__i] = new WeakReference<>((rv = new CICodeException(
					buffer.readUnsignedShort(base, 0),
					buffer.readUnsignedShort(base, 2),
					buffer.readUnsignedShort(base, 4),
					(utf == null ? null : utf.asBinaryName()))));
			}
			
			// Return it
			return rv;
		}
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
	private Reference<CICodeException>[] __makeArray(int __len)
	{
		return (Reference<CICodeException>[])((Object)new Reference[__len]);
	}
}

