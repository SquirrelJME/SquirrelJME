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

/**
 * This represents a symbol as it appears to the {@link ClassLoader}.
 *
 * @since 2016/04/06
 */
public final class ClassLoaderNameSymbol
	extends __BaseSymbol__
{
	/** The cache. */
	static final __Cache__<ClassLoaderNameSymbol> _CACHE =
		new __Cache__<>(ClassLoaderNameSymbol.class,
		new __Cache__.__Create__<ClassLoaderNameSymbol>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/05/18
			 */
			@Override
			public ClassLoaderNameSymbol create(String __s)
			{
				return new ClassLoaderNameSymbol(__s);
			}
		});
	
	/** Is this an array? */
	protected final boolean isarray;
	
	/** The class name of this symbol. */
	private volatile Reference<ClassNameSymbol> _cname;
	
	/** The name of this resource. */
	private volatile Reference<String> _rcname;
	
	/**
	 * Initializes the class loader symbol.
	 *
	 * @param __s The class loader name.
	 * @throws IllegalSymbolException If the symbol contains invalid
	 * characters.
	 * @since 2016/04/06
	 */
	private ClassLoaderNameSymbol(String __s)
		throws IllegalSymbolException
	{
		this(__s, null);
	}
	
	/**
	 * Initializes the class loader symbol with an optional cache.
	 *
	 * @param __s The class loader name.
	 * @param __cl The optional class name to cache.
	 * @throws IllegalSymbolException If the symbol contains invalid
	 * characters.
	 * @since 2016/04/06
	 */
	ClassLoaderNameSymbol(String __s, ClassNameSymbol __cl)
		throws IllegalSymbolException
	{
		super(__s);
		
		// If not an array, it cannot contain any forward slashes
		isarray = ('[' == charAt(0));
		if (!isarray)
		{
			// {@squirreljme.error AL0e Non-array class loader names cannot
			// contain forward slashes. (This symbol; The illegal character)}
			int n = length();
			char c;
			for (int i = 0; i < n; i++)
				if ((c = charAt(i)) == '/')
					throw new IllegalSymbolException(String.format(
						"AL0e %s %c", this, c));
		}
		
		// Pre-cache?
		if (__cl != null)
			_cname = new WeakReference<>(__cl);
		
		// Check for valid class name, since this could be an array too
		asClassName();
	}
	
	/**
	 * Returns this class loader symbol as a class name symbol.
	 *
	 * @return The symbol as a class name.
	 * @since 2016/04/06
	 */
	public ClassNameSymbol asClassName()
	{
		// Get reference
		Reference<ClassNameSymbol> ref = _cname;
		ClassNameSymbol rv;
		
		// Needs to be created?
		if (ref == null || null == (rv = ref.get()))
		{
			// Arrays are treated like fields, otherwise the names of classes
			// get their dots replaced with slashes
			_cname = new WeakReference<>((rv = ClassNameSymbol.of(
				(isarray ? toString() : toString(). replace('.', '/')))));
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the name of this class name as a resoiurce.
	 *
	 * @return The resource name or {@code null} if an array.
	 * @since 2016/04/06
	 */
	public String resourceName()
	{
		// Not valid for arrays
		if (isarray)
			return null;
		
		// Get reference
		Reference<String> ref = _rcname;
		String rv;
		
		// Needs caching?
		if (ref == null || (null == (rv = ref.get())))
			_rcname = new WeakReference<>((rv = '/' + toString().
				replace('.', '/') + ".class"));
		
		// Return it
		return rv;
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
	public static ClassLoaderNameSymbol of(String __s)
		throws IllegalSymbolException, NullPointerException
	{
		return _CACHE.__of(__s);
	}
}

