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
import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.List;
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
	implements Iterable<IdentifierSymbol>
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
		
		// Cannot be blank
		int n = length();
		if (n <= 0)
			throw new IllegalSymbolException(toString());
		
		// Check characters
		int cc = 0;
		for (int i = 0; i < n; i++)
			switch (charAt(i))
			{
					// Illegal
				case '.':
				case ';':
				case '[':
					throw new IllegalSymbolException(toString());
					
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
	 * {@inheritDoc}
	 * @since 2016/03/14
	 */
	@Override
	public Iterator<IdentifierSymbol> iterator()
	{
		throw new Error("TODO");
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

