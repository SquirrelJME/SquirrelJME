// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.WeakReference;

/**
 * Iterator over weak hash map entry keys.
 *
 * @since 2023/02/09
 */
final class __WeakHashMapIterator__<K, V>
	implements Iterator<Map.Entry<K, V>>
{
	/** The iterator this is using. */
	private final Iterator<Map.Entry<__WeakHashMapKey__<K>, V>> _iterator;
	
	/** Is there an entry waiting for us? */
	private __WeakHashMapEntry__<K, V> _waiting;
	
	/**
	 * Initializes the iterator.
	 * 
	 * @param __iterator The iterator over entries.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/09
	 */
	__WeakHashMapIterator__(
		Iterator<Map.Entry<__WeakHashMapKey__<K>, V>> __iterator)
		throws NullPointerException
	{
		if (__iterator == null)
			throw new NullPointerException("NARG");
		
		this._iterator = __iterator;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public boolean hasNext()
	{
		try
		{
			// There has to be something waiting for us here, since we delete
			// entries on iteration we need to check this
			this.__next();
			
			// If this is here then we have something waiting
			return true;
		}
		
		// If this happens, then there is actually nothing
		catch (NoSuchElementException ignored)
		{
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public Map.Entry<K, V> next()
		throws NoSuchElementException
	{
		// Pop in next, if there is one... will return the waiting item
		__WeakHashMapEntry__<K, V> result = this.__next();
		
		// We took what was waiting, so remove that now
		this._waiting = null;
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public void remove()
		throws IllegalStateException, UnsupportedOperationException
	{
		this._iterator.remove();
	}
	
	/**
	 * Queues the next item
	 * 
	 * @return The next entry, will be {@link #_waiting}.
	 * @since 2023/02/09
	 */
	private __WeakHashMapEntry__<K, V> __next()
	{
		// If there is something already waiting, do that
		if (this._waiting != null)
			return this._waiting;
		
		// Keep poking the iterator for new entries, if there are any
		Iterator<Map.Entry<__WeakHashMapKey__<K>, V>> iterator =
			this._iterator;
		for (;;)
		{
			// Get next entry, if this fails we stop as the iteration is empty
			Map.Entry<__WeakHashMapKey__<K>, V> next = iterator.next();
			
			// Get the details here
			__WeakHashMapKey__<K> wrappedKey = next.getKey();
			WeakReference<K> ref = wrappedKey._ref;
			
			// If reference is null, then this key represents null
			if (ref == null)
				return new __WeakHashMapEntry__<>(null, next);
			
			// Get the key, if it is null then we just got it GCed
			K key = ref.get();
			if (key == null)
			{
				// Just remove this from the set now...
				iterator.remove();
				
				// Try again because we cannot continue here...
				continue;
			}
			
			// Wrap it and set a waiting down
			this._waiting = new __WeakHashMapEntry__<>(key, next);
			return this._waiting;
		}
	}
}
