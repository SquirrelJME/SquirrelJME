// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.descriptors;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This represents a binary name as it appears in the Java class file.
 * Binary names are essentially a group of identifiers.
 *
 * Note that this does not accept arrays, for that use {@link ClassNameSymbol}
 * instead.
 *
 * @since 2016/03/14
 */
public final class BinaryNameSymbol
	extends __BaseSymbol__
	implements Iterable<IdentifierSymbol>, FieldBaseTypeSymbol
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Identifier count. */
	protected final int count;
	
	/** Identifier base offsets. */
	private final int[] _baseoffs;
	
	/** Identifier base lengths. */
	private final int[] _baselens;
	
	/** Identifier symbol cache. */
	private volatile Reference<IdentifierSymbol>[] _idents;
	
	/** As a list? */
	private volatile Reference<List<IdentifierSymbol>> _aslist;
	
	/**
	 * Initializes the binary name symbol.
	 *
	 * @param __s The string to use for the symbol.
	 * @throws IllegalSymbolException If it is not a valid binary name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/14
	 */
	public BinaryNameSymbol(String __s)
		throws IllegalSymbolException, NullPointerException
	{
		super(__s);
		
		// Check characters
		int n = length();
		int cc = 0;
		for (int i = 0; i < n; i++)
			switch (charAt(i))
			{
					// Illegal
				case '.':
				case ';':
				case '[':
					throw new IllegalSymbolException(String.format(
						"DS02 %s %d %c", this, i, charAt(i)));
					
					// Count slashes
				case '/':
					cc++;
					break;
				
					// Fine
				default:
					break;
			}
		
		// Set count
		count = cc + 1;
		
		// Calculate base offsets and lengths
		_baseoffs = new int[count];
		String base = toString();
		for (int v = base.indexOf('/'), q = 1; q < count;
			v = (base.indexOf('/', v + 1)))
			_baseoffs[q++] = v + 1;
		
		// Calculate lengths
		_baselens = new int[count];
		for (int i = 0; i < count; i++)
			_baselens[i] = ((i < count - 1 ? _baseoffs[i + 1] : n + 1) -
				_baseoffs[i]) - 1;
		
		// Initialize array
		_idents = __makeIDRefArray(count);
	}
	
	/**
	 * Initializes the binary name symbol from multiple identifier symbols.
	 *
	 * @param __ids The identifier symbols which make up the binary name.
	 * @throws IllegalSymbolException If it is not a valid binary name, this
	 * generally should never occur.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/14
	 */
	public BinaryNameSymbol(IdentifierSymbol... __ids)
		throws IllegalSymbolException, NullPointerException
	{
		this(__symbolsToBinaryName(__ids));
	}
	
	/**
	 * Exposes the list of identifiers as a list.
	 *
	 * @return The view of the identifiers as a list.
	 * @since 2016/03/14
	 */
	public List<IdentifierSymbol> asList()
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<List<IdentifierSymbol>> ref = _aslist;
			List<IdentifierSymbol> rv = null;
			
			// In a reference?
			if (ref != null)
				rv = ref.get();
			
			// Needs initialization?
			if (rv == null)
				_aslist = new WeakReference<>(
					(rv = new AbstractList<IdentifierSymbol>()
					{
						/**
						 * {@inheritDoc}
						 * @since 2106/03/14
						 */
						@Override
						public IdentifierSymbol get(int __i)
						{
							return BinaryNameSymbol.this.get(__i);
						}
						
						/**
						 * {@inheritDoc}
						 * @since 2106/03/14
						 */
						@Override
						public int size()
						{
							return count;
						}
					}));
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Gets the identifier symbol at the given index.
	 *
	 * @param __i The index to get the symbol at.
	 * @return The identifier at the given index.
	 * @throws IndexOutOfBoundsException If the index is negative or exceeds
	 * the number of identifiers.
	 * @since 2016/03/14
	 */
	public IdentifierSymbol get(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__i < 0 || __i >= count)
			throw new IndexOutOfBoundsException(String.format("DS03 %d",
				__i, count));
		
		// Lock on the array
		Reference<IdentifierSymbol>[] ids = _idents;
		synchronized (ids)
		{
			// Get reference at index
			Reference<IdentifierSymbol> ref = ids[__i];
			IdentifierSymbol rv = null;
			
			// Already cached?
			if (ref != null)
				rv = ref.get();
			
			// Cache it
			if (rv == null)
			{
				// base offset
				int bo = _baseoffs[__i];
				
				// Generate it
				ids[__i] = new WeakReference<>((rv =
					new IdentifierSymbol(subSequence(bo, bo + _baselens[__i]).
						toString())));
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/14
	 */
	@Override
	public Iterator<IdentifierSymbol> iterator()
	{
		return new Iterator<IdentifierSymbol>()
			{
				/** The current index. */
				private volatile int _dx;
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/14
				 */
				@Override
				public boolean hasNext()
				{
					return _dx < count;
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/14
				 */
				@Override
				public IdentifierSymbol next()
				{
					// There is a next?
					int next = _dx;
					if (next >= count)
						throw new NoSuchElementException("NSEE");
					
					// Get it
					IdentifierSymbol rv = get(next);
					
					// Set next next
					_dx = next + 1;
					
					// Return it
					return rv;
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/14
				 */
				@Override
				public void remove()
				{
					throw new UnsupportedOperationException("RORO");
				}
			};
	}
	
	/**
	 * Returns the last identifier symbol.
	 *
	 * @return The last identifier.
	 * @since 2016/03/14
	 */
	public IdentifierSymbol last()
	{
		return get(size() - 1);
	}
	
	/**
	 * Returns the number of identifiers.
	 *
	 * @return The identifier count.
	 * @since 2016/03/14
	 */
	public int size()
	{
		return count;
	}
	
	/**
	 * Used to prevent {@link SuppressWarnings} where it is not needed.
	 *
	 * @param __n The number of elements in the array.
	 * @return The generic array.
	 * @since 2016/03/14
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<IdentifierSymbol>[] __makeIDRefArray(int __n)
	{
		return ((Reference<IdentifierSymbol>[])((Object)new Reference[__n]));
	}
	
	/**
	 * Converts a group of identifier symbols to binary names.
	 *
	 * @param __ids Identifier symbols to convert to a binary name.
	 * @return The combined symbol for the given identfiers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/14
	 */
	private static String __symbolsToBinaryName(IdentifierSymbol... __ids)
		throws NullPointerException
	{
		// Start building
		StringBuilder sb = new StringBuilder();
		
		// Construct
		boolean slash = false;
		for (IdentifierSymbol is : __ids)
		{
			// Slash?
			if (slash)
				sb.append('/');
			slash = true;
			
			// Add identifier
			sb.append(Objects.<IdentifierSymbol>requireNonNull(is).toString());
		}
		
		// Finish it
		return sb.toString();
	}
}

