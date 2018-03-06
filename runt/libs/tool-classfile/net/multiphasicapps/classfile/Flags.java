// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This is the base class for all flag collections.
 *
 * @param <F> The flag type.
 * @since 2016/04/23
 */
public abstract class Flags<F extends Flag>
	extends AbstractSet<F>
{
	/** The class type to use. */
	protected final Class<F> cast;
	
	/** The set ordinals. */
	protected final int setbits;
	
	/** The slower access set. */
	private final Set<F> _flags;
	
	/**
	 * Initializes the flag set.
	 *
	 * @param __cl The class type of the flag.
	 * @param __fl The input flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	Flags(Class<F> __cl, F[] __fl)
		throws NullPointerException
	{
		this(__cl, Arrays.<F>asList(__fl));
	}
	
	/**
	 * Initializes the flag set.
	 *
	 * @param __cl The class type of the flag.
	 * @param __fl The input flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	Flags(Class<F> __cl, Iterable<F> __fl)
	{
		// Check
		if (__cl == null || __fl == null)
			throw new NullPointerException("NARG");
		
		// Set
		cast = __cl;
		
		// Go through all input flags
		Set<F> to = new HashSet<>();
		int bits = 0;
		for (F f : __fl)
		{
			// Get ordinal
			int o = __cl.cast(f).ordinal();
			
			// Set it
			bits |= (1 << o);
			
			// Add to flag set
			to.add(f);
		}
		
		// Lock in
		this.setbits = bits;
		this._flags = to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/23
	 */
	@Override
	public final boolean contains(Object __o)
	{
		// Quick bit check?
		if (cast.isInstance(__o))
			return 0 != (this.setbits & (1 << (((Flag)__o).ordinal())));
		
		// Fallback
		return this._flags.contains(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/23
	 */
	@Override
	public final Iterator<F> iterator()
	{
		return new __Iterator__<F>(this._flags.iterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/23
	 */
	@Override
	public final int size()
	{
		return this._flags.size();
	}
	
	/**
	 * Decodes the specified bitfield and returns the used flags.
	 *
	 * @param <F> The type of flags to decode.
	 * @param __i The input bitfield.
	 * @param __f The flag values to decode.
	 * @return The flags specified in the bitfield.
	 * @throws InvalidClassFormatException If extra flags were specified.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/13
	 */
	static <F extends Flag> Iterable<F> __decode(int __i, F[] __f)
		throws InvalidClassFormatException, NullPointerException
	{
		// Find all matching flags in the bitfield
		List<F> fl = new ArrayList<>(__f.length);
		for (F f : __f)
		{
			int v = f.javaBitMask();
			if (0 != (__i & v))
			{
				fl.add(f);
				__i ^= v;
			}
		}
		
		// {@squirreljme.error JC0x An undefined flag has been specified.
		// (The extra bitfield flags)}
		if (__i != 0)
			throw new InvalidClassFormatException(String.format("JC0x %02x", __i));
		
		return fl;
	}
	
	/**
	 * Iterates over flags.
	 *
	 * @since 2017/01/28
	 */
	private static final class __Iterator__<F>
		implements Iterator<F>
	{
		/** The iterator used. */
		protected final Iterator<F> iterator;
		
		/**
		 * Wraps the iterator.
		 *
		 * @param __it The iterator to wrap.
		 * @since 2017/01/28
		 */
		private __Iterator__(Iterator<F> __it)
		{
			this.iterator = __it;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/01/28
		 */
		@Override
		public boolean hasNext()
		{
			return this.iterator.hasNext();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/01/28
		 */
		@Override
		public F next()
		{
			return this.iterator.next();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/01/28
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

