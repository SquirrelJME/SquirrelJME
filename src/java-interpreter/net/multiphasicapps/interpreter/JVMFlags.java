// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This is the base class for a bit representation of flags which are used
 * by classes and members.
 *
 * @param <F> The type of flags to use.
 * @since 2016/03/19
 */
public abstract class JVMFlags<F extends JVMBitFlag>
	extends AbstractSet<F>
{
	/** All available flags. */
	protected final List<F> all;
	
	/** The flag class type. */
	protected final Class<F> type;
	
	/** The enabled flags. */
	protected final int bits;
	
	/**
	 * Initializes the base flag set.
	 *
	 * @param __b The bits which are set.
	 * @param __type The class type used for flags.
	 * @param __all All available flags, the set is directly used so it must
	 * not be modified.
	 * @throws JVMClassFormatError If a bit which has no associated flag is
	 * set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	public JVMFlags(int __b, Class<F> __type, List<F> __all)
		throws JVMClassFormatError, NullPointerException
	{
		// Check
		if (__all == null || __type == null)
			throw new NullPointerException("NARG");
		
		// All available flags
		all = __all;
		type = __type;
		bits = __b;
		
		// Clear all bits to make sure no unknown flags are set
		int rem = bits;
		for (F f : all)
			rem &= ~f.mask();
		if (rem != 0)
			throw new JVMClassFormatError(String.format("IN0a %x %x", bits,
				rem));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	public boolean contains(Object __o)
	{
		// If this is a flag then check the bits on it
		if (type.isInstance(__o))
			return (0 != (bits & ((JVMBitFlag)__o).mask()));
		
		// Use super call instead (performs iteration)
		return super.contains(__o);
	}
	
	/**
	 * Checks whether all of the given flags are set.
	 *
	 * @param __fs The flags to check.
	 * @return {@code true} if all are set.
	 * @throws NullPointerException On null arguments or if any array element
	 * is null.
	 * @since 2016/03/19
	 */
	public boolean containsAll(JVMBitFlag... __fs)
		throws NullPointerException
	{
		// Check
		if (__fs == null)
			throw new NullPointerException("NARG");
		
		// Go through it
		for (JVMBitFlag f : __fs)
		{
			// Crash on null
			if (f == null)
				throw new NullPointerException("NARG");
			
			// Must be instance
			if (!type.isInstance(f))
				return false;
			
			// If not set, then it is missing
			if (0 == (bits & f.mask()))
				return false;
		}
		
		// Ok
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	public Iterator<F> iterator()
	{
		return new Iterator<F>()
			{
				/** All available flags. */
				protected final ListIterator<F> it =
					all.listIterator();
				
				/** The next bit to use. */
				private volatile F _key;
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/19
				 */
				@Override
				public boolean hasNext()
				{
					for (;;)
					{
						// No flags left to check?
						if (!it.hasNext())
							return false;
					
						// Already know the key?
						F key = _key;
						if (key != null)
							return true;
						
						// Get the next entry in the iteration sequence
						key = it.next();
						
						// If the bit is set, use it
						if (0 != (bits & key.mask()))
						{
							_key = key;
							return true;
						}
					}
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/19
				 */
				@Override
				public F next()
				{
					// Fail if no next
					if (!hasNext())
						throw new NoSuchElementException("NSEE");
					
					// Geta and clear the key
					F key = _key;
					_key = null;
					
					// Return the key
					return key;
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/19
				 */
				@Override
				public void remove()
				{
					throw new UnsupportedOperationException("RORO");
				}
			};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	public int size()
	{
		return Integer.bitCount(bits);
	}
}

