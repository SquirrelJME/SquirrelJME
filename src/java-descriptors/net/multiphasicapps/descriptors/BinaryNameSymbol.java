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
	implements Iterable<IdentifierSymbol>, FieldBaseTypeSymbol,
		__ClassNameCompatible__
{
	/** The cache. */
	static final __Cache__<BinaryNameSymbol> _CACHE =
		new __Cache__<>(BinaryNameSymbol.class,
		new __Cache__.__Create__<BinaryNameSymbol>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/05/18
			 */
			@Override
			public BinaryNameSymbol create(String __s)
			{
				return new BinaryNameSymbol(__s);
			}
		});
	
	/** The default package. */
	public static final BinaryNameSymbol DEFAULT_PACKAGE =
		new BinaryNameSymbol(false);
	
	/** The special package. */
	public static final BinaryNameSymbol SPECIAL_PACKAGE =
		new BinaryNameSymbol(false);
	
	/** The throwable class. */
	public static final BinaryNameSymbol THROWABLE =
		of("java/lang/Throwable");
	
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
	
	/** As a class name? */
	private volatile Reference<ClassNameSymbol> _clname;
	
	/** The parent package. */
	private volatile Reference<BinaryNameSymbol> _parent;
	
	/**
	 * Initializes the binary name symbol.
	 *
	 * @param __s The string to use for the symbol.
	 * @throws IllegalSymbolException If it is not a valid binary name.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __cls}.
	 * @since 2016/04/04
	 */
	private BinaryNameSymbol(String __s)
		throws IllegalSymbolException, NullPointerException
	{
		super(__s);
		
		// Check characters
		int n = length();
		int cc = 0;
		for (int i = 0; i < n; i++)
			switch (charAt(i))
			{
					// {@squirreljme.error AL02 The binary name symbol contains
					// an illegal character. (The symbol; The character index;
					// The illegal character)}
				case '.':
				case ';':
				case '[':
					throw new IllegalSymbolException(String.format(
						"AL02 %s %d %c", this, i, charAt(i)));
					
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
	private BinaryNameSymbol(IdentifierSymbol... __ids)
		throws IllegalSymbolException, NullPointerException
	{
		this(__symbolsToBinaryName(__ids));
	}
	
	/**
	 * Initializes the default and special packages.
	 *
	 * @param __ign Ignored.
	 * @since 2016/04/15
	 */
	private BinaryNameSymbol(boolean __ign)
	{
		super(false);
		
		// Default package is blank
		count = 0;
		_baseoffs = _baselens = new int[0];
	}
	
	/**
	 * Returns this binary name symbol as a class name symbol.
	 *
	 * @return The class name symbol representation of this.
	 * @since 2016/04/04
	 */
	public ClassNameSymbol asClassName()
	{
		// Get reference
		Reference<ClassNameSymbol> ref = _clname;
		ClassNameSymbol rv;
		
		// Create cache if required
		if (ref == null || null == (rv = ref.get()))
			_clname = new WeakReference<>(
				(rv = ClassNameSymbol.of(toString())));
		
		// Return it
		return rv;
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
		synchronized (_baseoffs)
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
	 * {@inheritDoc}
	 * @since 2016/04/15
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check super first
		if (!super.equals(__o))
			return false;
		
		// The default and special packages never compare to each other
		if ((this == DEFAULT_PACKAGE && __o == SPECIAL_PACKAGE) ||
			(this == SPECIAL_PACKAGE && __o == DEFAULT_PACKAGE))
			return false;
		
		// Is the same
		return true;
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
		// {@squirreljme.error Request for a binary name identifier which is
		// not within bounds. (The index; The number of identifiers)}
		if (__i < 0 || __i >= count)
			throw new IndexOutOfBoundsException(String.format("AL03 %d",
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
	 * Returns the parent package of this binary name symbol.
	 *
	 * @return The package containing this.
	 * @since 2016/04/15
	 */
	public BinaryNameSymbol parentPackage()
	{
		// Keep specials
		if (this == DEFAULT_PACKAGE || this == SPECIAL_PACKAGE)
			return this;
		
		// If there is not count, use the default package
		if (count <= 0)
			return DEFAULT_PACKAGE;
		
		// Lock
		synchronized (_baseoffs)
		{
			// Get reference
			Reference<BinaryNameSymbol> ref = _parent;
			BinaryNameSymbol rv;
			
			// Needs to be cached?
			if (ref == null || null == (rv = ref.get()))
			{
				// Setup string
				StringBuilder sb = new StringBuilder();
				
				// Add all but the last count
				int n = count - 1;
				for (int i = 0; i < n; i++)
				{
					if (i > 0)
						sb.append('/');
					sb.append(get(i));
				}
				
				// Lock it in
				_parent = new WeakReference<>((rv = of(sb.toString())));
			}
			
			// Return it
			return rv;
		}
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
	 * Creates a symbol for the given string or returns a pre-cached variant
	 * of the string.
	 *
	 * @param __s The string to create a symbol for.
	 * @return The symbol.
	 * @throws IllegalSymbolException If the symbol is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	public static BinaryNameSymbol of(String __s)
		throws IllegalSymbolException, NullPointerException
	{
		return _CACHE.__of(__s);
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

