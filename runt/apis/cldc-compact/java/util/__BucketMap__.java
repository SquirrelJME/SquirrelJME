// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is a bucket map which acts as the raw internal hash table
 * implementation.
 *
 * @see HashMap
 * @see HashSet
 * @see LinkedHashMap
 * @see LinkedHashSet
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2018/10/07
 */
final class __BucketMap__<K, V>
	extends AbstractMap<K, V>
{
	/** Special holder for when backing for a set. */
	static final Object _TAKEN =
		new Object();
	
	/** The default capacity. */
	static final int _DEFAULT_CAPACITY =
		16;
	
	/** The default load factor. */
	static final float _DEFAULT_LOAD =
		0.75F;
	
	/** Is this bucket map ordered? */
	protected final boolean ordered;
	
	/** Is this bucket map in accessed order? */
	protected final boolean accessorder;
	
	/** Track put order? */
	protected final boolean trackputorder;
	
	/** The load factor. */
	protected final float loadfactor;
	
	/** Linked order of entries. */
	private final LinkedList<__BucketMapEntry__<K, V>> _links;
	
	/** The entry chains for each element. */
	__BucketMapEntry__<K, V>[][] _buckets;
	
	/** The hashcode divisor for buckets. */
	int _bucketdiv;
	
	/** The number of elements in the map. */
	int _size;
	
	/** The current capacity. */
	int _capacity;
	
	/** The size threshold before a rebuild is done. */
	int _loadthreshold;
	
	/** Modification count. */
	int _modcount;
	
	/**
	 * Initializes the map with the default capacity and load factor.
	 *
	 * @param __o Is the backing iterator ordered?
	 * @since 2018/10/07
	 */
	__BucketMap__(boolean __o)
	{
		this(__o, false, __BucketMap__._DEFAULT_CAPACITY,
			__BucketMap__._DEFAULT_LOAD);
	}
	
	/**
	 * Initializes the map with the given capacity and the default load factor.
	 *
	 * @param __o Is the backing iterator ordered?
	 * @param __cap The capacity used.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2018/10/07
	 */
	__BucketMap__(boolean __o, int __cap)
	{
		this(__o, false, __cap, __BucketMap__._DEFAULT_LOAD);
	}
	
	/**
	 * Initializes the map with the given capacity and load factor.
	 *
	 * @param __o Is the backing iterator ordered?
	 * @param __ao Is access order used additionally?
	 * @param __cap The capacity used.
	 * @param __load The load factor used.
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2018/10/07
	 */
	__BucketMap__(boolean __o, boolean __ao, int __cap, float __load)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ28 The initial capacity of the map cannot be
		// negative.}
		if (__cap < 0)
			throw new IllegalArgumentException("ZZ28");
		
		// {@squirreljme.error ZZ29 The load factor must be a positive value.}
		if (__load <= 0.0F)
			throw new IllegalArgumentException("ZZ29");
		
		this.ordered = __o;
		this.accessorder = (__ao = (__o && __ao));
		this.trackputorder = (__o && !__ao);
		this.loadfactor = __load;
		this._buckets = __BucketMap__.<K, V>__newBucket(__cap);
		this._bucketdiv = __cap;
		this._capacity = __cap;
		this._loadthreshold = (int)(__cap * __load);
		
		// Set linked list for ordered storage if it is used
		this._links = ((__o || __ao) ?
			new LinkedList<__BucketMapEntry__<K, V>>() : null);
	}
	
	/**
	 * Gets the entry for the given key.
	 *
	 * @param __k The key to get.
	 * @return The entry for the given or {@code null} if none exists.
	 * @since 2018/10/08
	 */
	public final __BucketMapEntry__<K, V> getEntry(Object __k)
	{
		// Where to look in the table?
		int hash = (__k == null ? 0 : __k.hashCode());
		int div = (hash & 0x7FFF_FFFF) % this._bucketdiv;
		
		// If the chain does not exist then do not bother at all
		__BucketMapEntry__<K, V>[] chain = this._buckets[div];
		if (chain == null)
			return null;
		
		// Go through the chain and find the matching entry
		for (__BucketMapEntry__<K, V> e : chain)
		{
			// Ignore blank entries
			if (e == null)
				continue;
			
			// Has the wrong hashcode
			if (hash != e._keyhash)
				continue;
			
			// If the objects actually match, it is found
			if (Objects.equals(e._key, __k))
			{
				// In access order?
				if (this.accessorder)
					throw new todo.TODO();	
				
				return e;
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/01
	 */
	@Override
	public final Set<Map.Entry<K, V>> entrySet()
	{
		return new __EntrySet__();
	}
	
	/**
	 * Returns the chain that the hashed object is within for the bucket.
	 *
	 * @param __k The key.
	 * @return The key for the given entry.
	 * @since 2018/10/07
	 */
	public final __BucketMapEntry__<K, V> putEntry(K __k)
	{
		__BucketMapEntry__<K, V>[][] buckets = this._buckets;
		int bucketdiv = this._bucketdiv;
		
		// Used to determine if we rebuild
		int size = this._size,
			nextsize = size + 1;
		
		// Hypothetically putting a new entry could cause the threshold to be
		// hit, so just in this case a new entry would be put so rebuild
		// the hash table. The buckets need to remain the same object
		// references for iteration.
		if (nextsize >= this._loadthreshold)
		{
			// Indicate re-balance
			todo.DEBUG.note("Rebalancing bucket map");
			
			// Double the number of buckets
			int newbucketdiv = (bucketdiv * 2);
			__BucketMapEntry__<K, V>[][] newbuckets =
				__BucketMap__.<K, V>__newBucket(newbucketdiv);
			
			// Go through every source bucket and redistribute entries
			for (int i = 0; i < bucketdiv; i++)
			{
				// Ignore empty chains
				__BucketMapEntry__<K, V>[] chain = buckets[i];
				if (chain == null)
					continue;
				
				// Go through chain and re-add entries, we do not need to
				// worry about object equality since if something is in the
				// map it is already unique!
				for (__BucketMapEntry__<K, V> e : chain)
				{
					// Was an entry which was removed, ignore
					if (e == null)
						continue;
					
					// Determine the new placement for it
					int hash = e._keyhash,
						div = (hash & 0x7FFF_FFFF) % newbucketdiv;
					
					// Get the new chain for it
					__BucketMapEntry__<K, V>[] newchain = newbuckets[div];
					if (newchain == null)
						newchain = __BucketMap__.<K, V>__newChain(1);
					else
					{
						// Need to setup new chain
						int cn = newchain.length;
						__BucketMapEntry__<K, V>[] newnewchain =
							__BucketMap__.<K, V>__newChain(cn + 1);
						
						// Copy all the old chain stuff over
						for (int j = 0; j < cn; j++)
							newnewchain[j] = newchain[j];
						
						// Use this chain
						newchain = newnewchain;
					}
					
					// Store entry in the last spot
					newchain[newchain.length - 1] = e;
					
					// New chan was created so update it naturally
					newbuckets[div] = newchain;
				}
			}
			
			// Map was modified, in case hashCode() fails!
			this._modcount++;
			
			// Store new data for later
			this._buckets = newbuckets;
			this._bucketdiv = newbucketdiv;
			this._capacity = newbucketdiv;
			this._loadthreshold = (int)(newbucketdiv * this.loadfactor);
			
			// Use these new properties and continue on
			buckets = newbuckets;
			bucketdiv = newbucketdiv;
		}
		
		// Where to look in the table?
		int hash = (__k == null ? 0 : __k.hashCode());
		int div = (hash & 0x7FFF_FFFF) % bucketdiv;
		
		// This will be set depending on the situation
		__BucketMapEntry__<K, V> rv;
		
		// No entries exist in the chain, we can just create one
		__BucketMapEntry__<K, V>[] chain = buckets[div];
		if (chain == null)
		{
			// Setup chain
			chain = __BucketMap__.<K, V>__newChain(1);
			buckets[div] = chain;
			
			// Fill
			chain[0] = (rv = new __BucketMapEntry__<K, V>(__k));
			
			// Add to order?
			if (this.trackputorder)
				this._links.add(rv);
			
			// Map is modified
			this._modcount++;
			
			// Size would have been increased at this point
			this._size = nextsize;
			
			return rv;
		}
		
		// Go through and find if there was a pre-existing item
		int nulldx = -1,
			n = chain.length;
		for (int i = 0; i < n; i++)
		{
			__BucketMapEntry__<K, V> e = chain[i];
			
			// If no entry is here remember this blank spot in the event
			// nothing is ever found
			if (e == null)
			{
				if (nulldx < 0)
					nulldx = i;
				continue;
			}
			
			// Has the wrong hashcode
			if (hash != e._keyhash)
				continue;
			
			// If the objects actually match, it is found
			if (Objects.equals(__k, e._key))
				return e;
		}
		
		// Found a blank spot, we can just put the entry here
		if (nulldx >= 0)
			chain[nulldx] = (rv = new __BucketMapEntry__<K, V>(__k));
		
		// Otherwise, increase the chain and use that instead
		else
		{
			// Copy the old chain over
			__BucketMapEntry__<K, V>[] dup =
				__BucketMap__.<K, V>__newChain(n + 1);
			for (int i = 0; i < n; i++)
				dup[i] = chain[i];
			
			// Set at end
			dup[n] = (rv = new __BucketMapEntry__<K, V>(__k));
			
			// Use this chain again
			buckets[div] = dup;
		}
		
		// Map has been modified
		this._modcount++;
		
		// Add to order?
		if (this.trackputorder)
			this._links.add(rv);
		
		// Size would have been increased at this point
		this._size = nextsize;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public final V remove(Object __k)
	{
		__BucketMapEntry__<K, V> rv = this.removeEntry(__k, false);
		if (rv != null)
			return rv._value;
		return null;
	}
	
	/**
	 * Removes the specified key from this map.
	 *
	 * Note that because iterators need to keep the same order, the entries
	 * cannot be shuffled or the map rebuilt.
	 *
	 * @param __k The key to remove.
	 * @param __preunlinked If this is true then the link chain will not have
	 * the entry removed by this method, it is assumed it was already
	 * removed by an iterator.
	 * @return 
	 */
	public final __BucketMapEntry__<K, V> removeEntry(Object __k,
		boolean __preunlinked)
	{
		// Where to look in the table?
		int hash = (__k == null ? 0 : __k.hashCode());
		int div = (hash & 0x7FFF_FFFF) % this._bucketdiv;
		
		// If the chain does not exist then do not bother at all
		__BucketMapEntry__<K, V>[] chain = this._buckets[div];
		if (chain == null)
			return null;
		
		// Go through the chain and find the matching entry
		for (int i = 0, n = chain.length; i < n; i++)
		{
			// Ignore blank entries
			__BucketMapEntry__<K, V> e = chain[i];
			if (e == null)
				continue;
			
			// Has the wrong hashcode
			if (hash != e._keyhash)
				continue;
			
			// If the objects actually match, it is found so it must be
			// removed
			if (Objects.equals(e._key, __k))
			{
				// Removing an entry from the chain is as simple as just
				// setting it to null. We do not need to move entries around
				// since that can be a bit slow
				chain[i] = null;
				
				// If this was not pre-unlinked from the iterator call then
				// it will be removed from the chain accordingly
				if (!__preunlinked)
				{
					Collection<__BucketMapEntry__<K, V>> links = this._links;
					if (links != null)
						links.remove(e);
				}	
				
				// Size goes down
				this._size--;
				
				// Map has been modified
				this._modcount++;
				
				// This entry was removed, so it gets returned by the map
				return e;
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/08
	 */
	@Override
	public final int size()
	{
		return this._size;
	}
	
	/**
	 * Clears the bucket map.
	 *
	 * @since 2018/11/05
	 */
	final void __clear()
	{
		// Set all buckets to null so they are empty
		__BucketMapEntry__<K, V>[][] buckets = this._buckets;
		for (int i = 0, n = buckets.length; i < n; i++)
			buckets[i] = null;
		
		// Set size to zero
		this._size = 0;
		
		// Clear the linked list if there is one
		LinkedList<__BucketMapEntry__<K, V>> links = this._links;
		if (links != null)
			links.clear();
		
		// Modification count goes up
		this._modcount++;
	}
	
	/**
	 * Return the iterator over the map entries.
	 *
	 * @return The map iterator.
	 * @since 2018/11/01
	 */
	final Iterator<Map.Entry<K, V>> __iterator()
	{
		if (__BucketMap__.this.ordered)
			return new __IteratorLinkedOrder__();
		return new __IteratorBucketOrder__();
	}
	
	/**
	 * Creates a new bucket array.
	 *
	 * @param <K> Key type.
	 * @param <V> Value type.
	 * @param __n The length.
	 * @return The array.
	 * @since 2018/10/08
	 */
	@SuppressWarnings({"unchecked"})
	private static <K, V> __BucketMapEntry__<K, V>[][] __newBucket(int __n)
	{
		return (__BucketMapEntry__<K, V>[][])
			((Object)new __BucketMapEntry__[__n][]);
	}
	
	/**
	 * Creates a new chain array.
	 *
	 * @param <K> Key type.
	 * @param <V> Value type.
	 * @param __n The length.
	 * @return The array.
	 * @since 2018/10/08
	 */
	@SuppressWarnings({"unchecked"})
	private static <K, V> __BucketMapEntry__<K, V>[] __newChain(int __n)
	{
		return (__BucketMapEntry__<K, V>[])
			((Object)new __BucketMapEntry__[__n]);
	}
	
	/**
	 * Implements the entry set over the map, this iterates in a given order.
	 *
	 * @since 2018/11/01
	 */
	final class __EntrySet__
		extends AbstractSet<Map.Entry<K, V>>
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public final void clear()
		{
			__BucketMap__.this.__clear();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public final Iterator<Map.Entry<K, V>> iterator()
		{
			return __BucketMap__.this.__iterator();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public final int size()
		{
			return __BucketMap__.this._size;
		}
	}
	
	/**
	 * Base iterator.
	 *
	 * @since 2018/11/01
	 */
	abstract class __IteratorBase__
		implements Iterator<Map.Entry<K, V>>
	{
		/** The mod init this iterator is at, to detect modifications. */
		int _atmod =
			__BucketMap__.this._modcount;
		
		/**
		 * Checks if the map's internal structure modification count has
		 * changed.
		 *
		 * @throws ConcurrentModificationException If the map was modified.
		 * @since 2018/10/13
		 */
		final void __checkModified()
			throws ConcurrentModificationException
		{
			// {@squirreljme.error ZZ2a Backing map has been modified.}
			if (this._atmod != __BucketMap__.this._modcount)
				throw new ConcurrentModificationException("ZZ2a");
		}
	}
	
	/**
	 * Iterator over the entries in this map.
	 *
	 * @since 2018/10/13
	 */
	final class __IteratorBucketOrder__
		extends __IteratorBase__
	{
		/** The current bucket this is at. */
		int _bucketat;
		
		/** The current chain link this is at. */
		int _chainat;
		
		/** The cached next entry. */
		__BucketMapEntry__<K, V> _next;
		
		/** The last entry. */
		__BucketMapEntry__<K, V> _last;
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/13
		 */
		@Override
		public final boolean hasNext()
		{
			// Check modification
			this.__checkModified();
			
			// Already cached, do not need to check anything more
			if (this._next != null)
				return true;
			
			// No more buckets remain?
			int bucketat = this._bucketat,
				bucketdiv = __BucketMap__.this._bucketdiv;
			if (bucketat >= bucketdiv)
				return false;
			
			// Get the current chain
			__BucketMapEntry__<K, V>[][] buckets = __BucketMap__.this._buckets;
			
			// We can store the current location parameters at the end rather
			// than every time (keeps everything in locals)
			int chainat = this._chainat;
			try
			{
				// We might try looking at the next bucket if we reach the end
				// of this chain.
				for (;;)
				{
					__BucketMapEntry__<K, V>[] chain = buckets[bucketat];
					
					// No more chain links remain? Or there is no chain?
					int chaindiv = (chain == null ? -1 : chain.length);
					if (chainat >= chaindiv)
					{
						// Reset to start of next bucket
						bucketat++;
						chainat = 0;
						
						// No more buckets to look in
						if (bucketat >= bucketdiv)
							return false;
						
						// Try again
						continue;
					}
					
					// Will use the next chain
					int oldchainat = chainat++;
					
					// If no link was here try again
					__BucketMapEntry__<K, V> link = chain[oldchainat];
					if (link == null)
						continue;
					
					// Cache that link for returning
					this._next = link;
					return true;
				}
			}
			
			// Store properties
			finally
			{
				this._bucketat = bucketat;
				this._chainat = chainat;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/13
		 */
		@Override
		public final Map.Entry<K, V> next()
			throws NoSuchElementException
		{
			// {@squirreljme.error ZZ2b Map has no more entries remaining.}
			if (!this.hasNext())
				throw new NoSuchElementException("ZZ2b");
			
			// hasNext() caches this
			__BucketMapEntry__<K, V> rv = this._next;
			this._next = null;
			this._last = rv;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/13
		 */
		@Override
		public final void remove()
		{
			// Check modification
			this.__checkModified();
			
			// No last element was nexted
			__BucketMapEntry__<K, V> last = this._last;
			if (last == null)
				throw new IllegalStateException("NSEE");
			
			// Remove from the map but we never unlinked it, so if there is
			// a link it will be scanned and removed accordingly
			if (__BucketMap__.this.removeEntry(last.getKey(), false) != last)
				throw new RuntimeException("OOPS");
			
			// The map likely was structurally modified so use the new state
			this._atmod = __BucketMap__.this._modcount;
		}
	}
	
	/**
	 * Iterator over the linked order in this map.
	 *
	 * @since 2018/11/01
	 */
	final class __IteratorLinkedOrder__
		extends __IteratorBase__
	{
		/** The iterator for entries in linked order. */
		final Iterator<__BucketMapEntry__<K, V>> _iterator =
			__BucketMap__.this._links.iterator();
		
		/** The last returned entry, for removal. */
		__BucketMapEntry__<K, V> _last;
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public final boolean hasNext()
		{
			// Check for modification
			this.__checkModified();
			
			// Just if it has a next anyway
			return this._iterator.hasNext();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public final Map.Entry<K, V> next()
		{
			// Check for modification
			this.__checkModified();
			
			// Use the direct next entry
			__BucketMapEntry__<K, V> rv = this._iterator.next();
			this._last = rv;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public final void remove()
		{
			// Check for modification
			this.__checkModified();
			
			// No last element was nexted
			__BucketMapEntry__<K, V> last = this._last;
			if (last == null)
				throw new IllegalStateException("NSEE");
			
			// Clear last because it will be invalid
			this._last = null;
			
			// Try removing it from the link first, if the state is bad then
			// we cannot remove the link. Has to be removed from the iterator
			// because otherwise there will be a concurrent modification
			// exception thrown when modification is detected.
			try
			{
				this._iterator.remove();
			}
			
			// Just rethrow this, this means the entry was already removed
			catch (IllegalStateException e)
			{
				throw e;
			}
			
			// Otherwise, remove the entry from the map but hint that it was
			// already removed from the ordered list
			// The entry being mismatched to the key should not happen ever
			// but if it does then something is very wrong
			if (__BucketMap__.this.removeEntry(last.getKey(), true) != last)
				throw new RuntimeException("OOPS");
			
			// The map likely was structurally modified so use the new state
			this._atmod = __BucketMap__.this._modcount;
		}
	}
}

