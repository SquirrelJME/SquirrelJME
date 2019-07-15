// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.jvm.Assembly;

/**
 * This represents a standard hash map.
 *
 * @since 2019/06/24
 */
public class HashMap<K, V>
{
	/** The number of buckets to use. */
	private static final int _BUCKET_COUNT =
		16;
	
	/** Mask for buckets. */
	private static final int _BUCKET_MASK =
		0xF;
	
	/** Buckets available. */
	private final __Bucket__[] _buckets =
		new __Bucket__[_BUCKET_COUNT];
	
	/**
	 * Checks if the map contains the given key.
	 *
	 * @param __k The key to check.
	 * @return If a key is contained in it.
	 * @since 2019/06/24
	 */
	public boolean containsKey(Object __k)
	{
		// Calculate hash code
		int hash = __k.hashCode();
		
		// Find existing bucket
		__Bucket__ bucket = this._buckets[hash & _BUCKET_MASK];
		if (bucket == null)
			return false;
		
		// Go through items
		for (__Item__ i : bucket._items)
			if (i != null && i._hash == hash && HashMap.__equals(__k, i._key))
				return true;
		
		// Not found
		return false;
	}
	
	/**
	 * Gets a value from the map.
	 *
	 * @param __k The key to get.
	 * @return The value.
	 * @since 2019/06/24
	 */
	@SuppressWarnings({"unchecked"})
	public V get(K __k)
	{
		// Calculate hash code
		int hash = __k.hashCode();
		
		// Find existing bucket
		__Bucket__ bucket = this._buckets[hash & _BUCKET_MASK];
		if (bucket == null)
			return null;
		
		// Go through items
		for (__Item__ i : bucket._items)
			if (i != null && i._hash == hash && HashMap.__equals(__k, i._key))
				return (V)i._value;
		
		// Not found
		return null;
	}
	
	/**
	 * Puts an item into the map.
	 *
	 * @param __k The key.
	 * @param __v The value.
	 * @return The old item, if any.
	 * @since 2019/06/24
	 */
	@SuppressWarnings({"unchecked"})
	public V put(K __k, V __v)
	{
		// Calculate hash code
		int hash = __k.hashCode(),
			bkid = hash & _BUCKET_MASK;
		
		// Find existing bucket, create if missing
		__Bucket__ bucket = this._buckets[bkid];
		if (bucket == null)
			this._buckets[bkid] = (bucket = new __Bucket__());
		
		// Check if it already exists in the map
		__Item__[] items = bucket._items;
		int n = items.length,
			empty = -1;
		for (int i = 0; i < n; i++)
		{
			__Item__ it = items[i];
			
			// Is empty slot, use it
			if (it == null)
				empty = i;
			
			// Matching key, replace
			else if (it._hash == hash && HashMap.__equals(__k, it._key))
			{
				V rv = (V)it._value;
				it._value = __v;
				return rv;
			}
		}
		
		// Add new item at the end
		__Item__[] newitems = new __Item__[n + 1];
		for (int i = 0; i < n; i++)
			newitems[i] = items[i];
		__Item__ it = new __Item__(hash, __k);
		newitems[n] = it;
		it._value = __v;
		
		// Store new items
		bucket._items = newitems;
		
		// There was no previous old value
		return null;
	}
	
	/**
	 * Returns if two objects are equal to each other.
	 *
	 * @param __a The first object.
	 * @param __b The second object.
	 * @return If they are equal or not.
	 * @since 2019/06/24
	 */
	private static final boolean __equals(Object __a, Object __b)
	{
		// Either side is null
		if (__a == null || __b == null)
			return (__a == __b);
		
		// Use equality check
		return __a.equals(__b);
	}
	
	/**
	 * This represents a single bucket within the map.
	 *
	 * @since 2019/06/24
	 */
	private static final class __Bucket__
	{
		/** Items within the bucket. */
		__Item__[] _items =
			new __Item__[_BUCKET_COUNT];
	}
	
	/**
	 * A single item within a bucket.
	 *
	 * @since 2019/06/24
	 */
	private static final class __Item__
	{
		/** The hashcode. */
		final int _hash;
		
		/** The key. */
		final Object _key;
		
		/** The value. */
		Object _value;
		
		/**
		 * Initializes the item.
		 *
		 * @param __h The hash.
		 * @param __k The key.
		 */
		__Item__(int __h, Object __k)
		{
			this._hash = __h;
			this._key = __k;
		}
	}
}

