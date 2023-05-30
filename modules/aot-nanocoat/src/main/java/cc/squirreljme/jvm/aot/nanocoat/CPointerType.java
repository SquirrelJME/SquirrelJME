// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a pointer type.
 *
 * @since 2023/05/29
 */
public class CPointerType
	implements CType
{
	/**
	 * Cache of pointer types since there will be lots, the list itself defines
	 * the pointer level.
	 */
	private static final Map<CBasicType, List<CPointerType>> _CACHE =
		new EnumTypeMap<>(CBasicType.class, CBasicType.values());
	
	/** The root type. */
	protected final CType root;
	
	/** The number of pointers used. */
	protected final int numPointers;
	
	/** The string representation of this type. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the pointer type.
	 * 
	 * @param __type The type to use.
	 * @param __numPointers The number of pointers on the type.
	 * @throws IllegalArgumentException If the number of pointers is zero
	 * or negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	private CPointerType(CType __type, int __numPointers)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error NC02 There cannot be zero or negative pointers.}
		if (__numPointers <= 0)
			throw new IllegalArgumentException("NC02");
		
		this.root = __type.rootType();
		this.numPointers = __type.pointerLevel() + __numPointers;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		if (this.numPointers == 1)
			return this.root;
		return CPointerType.of(this.root, this.numPointers - 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public int hashCode()
	{
		return this.root.hashCode() + this.numPointers;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof CPointerType))
			return false;
		
		CPointerType o = (CPointerType)__o;
		return this.root.equals(o.root) && this.numPointers == o.numPointers;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public int pointerLevel()
	{
		return this.numPointers;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		return CPointerType.of(this.root, this.numPointers + 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType rootType()
	{
		return this.root;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public String token()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			StringBuilder sb = new StringBuilder(this.root.token());
			for (int i = 0, n = this.numPointers; i < n; i++)
				sb.append('*');
			
			rv = sb.toString();
			this._string = new WeakReference<>(rv);
		}
			
		return rv;
	}
	
	/**
	 * Initializes the pointer type.
	 * 
	 * @param __type The type to use.
	 * @param __numPointers The number of pointers on the type.
	 * @throws IllegalArgumentException If the number of pointers is zero
	 * or negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public static CType of(CType __type, int __numPointers)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error NC04 There cannot be zero or negative pointers.}
		if (__numPointers <= 0)
			throw new IllegalArgumentException("NC04");
		
		// If the root type is not a basic type, we always want to classify
		// pointer levels based on that for cache purposes
		if (!(__type instanceof CBasicType))
			return CPointerType.of(__type.rootType(),
				__type.pointerLevel() + __numPointers);
		
		// We always operate on the basic type
		CBasicType basicType = (CBasicType)__type;
		
		// Check to see if the type is in the cache first
		Map<CBasicType, List<CPointerType>> cache = CPointerType._CACHE;
		synchronized (CPointerType.class)
		{
			// Get list, create if missing
			List<CPointerType> sub = cache.get(basicType);
			if (sub == null)
			{
				sub = new ArrayList<>();
				cache.put(basicType, sub);
			}
			
			// List cannot fit the requested pointer count?
			while (sub.size() <= __numPointers)
				sub.add(null);
			
			// If this has already been created use it
			CPointerType rv = sub.get(__numPointers);
			if (rv != null)
				return rv;
			
			// Store new type into the cache for later
			rv = new CPointerType(basicType, __numPointers);
			sub.set(__numPointers, rv);
			
			return rv;
		}
	}
}
