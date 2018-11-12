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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This represents a binary name which consists of a class which is
 * separted internally by forwarded slashes.
 *
 * @since 2017/09/27
 */
public final class BinaryName
	implements Comparable<BinaryName>
{
	/** The identifiers in the name. */
	private final ClassIdentifier[] _identifiers;
	
	/** String representation. */
	private Reference<String> _string;
	
	/** The package this is in. */
	private Reference<BinaryName> _package;
	
	/**
	 * Initializes the binary name.
	 *
	 * @param __n The name to initialize.
	 * @throws InvalidClassFormatException If the binary name is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	public BinaryName(String __n)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Split
		List<ClassIdentifier> id = new ArrayList<>();
		for (int i = 0, n = __n.length(); i < n;)
		{
			// Identifiers are always split by forward slashes like UNIX
			// paths
			int ns = __n.indexOf('/', i);
			if (ns < 0)
				ns = n;
			
			// Split in
			id.add(new ClassIdentifier(__n.substring(i, ns)));
			
			// Skip
			i = ns + 1;
		}
		
		this._identifiers = id.<ClassIdentifier>toArray(
			new ClassIdentifier[id.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public int compareTo(BinaryName __o)
	{
		ClassIdentifier[] a = this._identifiers,
			b = __o._identifiers;
		
		// Compare lengths first
		int an = a.length,
			bn = b.length;
		int rv = an - bn;
		if (rv != 0)
			return rv;
		
		// Then individual units
		for (int i = 0; i < an; i++)
		{
			ClassIdentifier x = a[i],
				y = b[i];
			
			rv = x.compareTo(y);
			if (rv != 0)
				return rv;
		}
		
		// Matches
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof BinaryName))
			return false;
		
		BinaryName o = (BinaryName)__o;
		return Arrays.equals(this._identifiers, o._identifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public int hashCode()
	{
		int rv = 0;
		for (ClassIdentifier i : this._identifiers)
			rv ^= i.hashCode();
		return rv;
	}
	
	/**
	 * Returns the binary name of the package this class is within.
	 *
	 * @return The binary name of the owning package.
	 * @since 2017/10/09
	 */
	public BinaryName inPackage()
	{
		Reference<BinaryName> ref = this._package;
		BinaryName rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder();
			ClassIdentifier[] identifier = this._identifiers;
			
			for (int i = 0, n = identifier.length - 1; i < n; i++)
			{
				if (i > 0)
					sb.append('/');
				sb.append(identifier[i]);
			}
			
			this._package = new WeakReference<>(
				(rv = new BinaryName(sb.toString())));
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder();
			
			for (ClassIdentifier i : this._identifiers)
			{
				if (sb.length() > 0)
					sb.append('/');
				sb.append(i.identifier());
			}
			
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

