// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Miniature intern holding table, used as a basic implementation of a hash
 * table for storing intern strings.
 *
 * @since 2022/06/19
 */
final class __InternMini__
{
	/** The size of the minor intern table. */
	private static final int _MINOR_TABLE_SIZE =
		16;
	
	/** The mask of the minor intern table. */
	private static final int _MINOR_TABLE_MASK =
		15;
	
	/** Internal interns. */
	private final __InternMicro__[] _interns =
		new __InternMicro__[__InternMini__._MINOR_TABLE_SIZE];
	
	/**
	 * Performs internal intern logic for the string.
	 * 
	 * @param __hashCode The hash code.
	 * @param __str The string to intern.
	 * @return The interned string.
	 * @since 2022/06/19
	 */
	String __intern(int __hashCode, String __str)
	{
		// Find the actual micro table to use
		int minorKey = __hashCode & __InternMini__._MINOR_TABLE_MASK;
		__InternMicro__[] interns = this._interns;
		__InternMicro__ intern;
		synchronized (this)
		{
			intern = interns[minorKey];
			if (intern == null)
				interns[minorKey] = (intern = new __InternMicro__());
		}
		
		// Perform intern logic on this
		return intern.__intern(__str);
	}
	
	/**
	 * Intern micro table, for individual linked list treads.
	 * 
	 * @since 2022/06/19
	 */
	private static final class __InternMicro__
	{
		/** Size bump for the array. */
		private static final int _SIZE_BUMP =
			8;
		
		/** Interned strings that are available. */
		private volatile Reference<String>[] _store =
			__InternMicro__.newArray(__InternMicro__._SIZE_BUMP);
		
		/**
		 * Initializes the table.
		 * 
		 * @since 2022/06/19
		 */
		__InternMicro__()
		{
		}
		
		/**
		 * Interns the given string.
		 * 
		 * @param __str The string to intern.
		 * @return The interned string.
		 * @since 2022/06/19
		 */
		String __intern(String __str)
		{
			// Need to lock on ourselves so that we only touch our own table
			// Previously a LinkedList was used however that can cause lots
			// of objects to be allocated for intern strings and arrays are
			// a bit cheaper and save more memory since the linked list nodes
			// can take up extra memory.
			synchronized (this)
			{
				// Scan through and try to find a reference, but if missing we
				// want to use the free slot that is available
				int freeSlot = -1;
				Reference<String>[] store = this._store;
				int storeLen = store.length;
				for (int i = 0; i < storeLen; i++)
				{
					Reference<String> ref = store[i];
					
					// If nothing is here, we can claim it later if we never
					// find anything
					if (ref == null)
					{
						freeSlot = i;
						continue;
					}
					
					// Check to see if the other string is missing, then we
					// can just wipe the reference away
					String other = ref.get();
					if (other == null)
					{
						// Wipe reference so it gets thrown away
						store[i] = null;
						
						// Counts as a free slot
						freeSlot = i;
						continue;
					}
					
					// If this string matches, then it is the interned string
					// so use it!
					if (__str.equals(other))
						return other;
				}
				
				// If there is no free slot then the array is too small, so
				// grow it accordingly
				if (freeSlot < 0)
				{
					// Setup new array and copy there
					Reference<String>[] newStore = __InternMicro__.newArray(
						storeLen + __InternMicro__._SIZE_BUMP);
					System.arraycopy(store, 0,
						newStore, 0, storeLen);
					
					// Use this reference instead
					this._store = newStore;
					store = newStore;
					
					// Free slot is at the end of the length
					freeSlot = storeLen;
				}
				
				// Store weak reference to the string so that it is kept
				// cached and referencable until it is not needed anymore
				store[freeSlot] = new WeakReference<>(__str);
				
				// This string becomes interned now
				__str._quickFlags |= String._QUICK_INTERN;
				return __str;
			}
		}
		
		/**
		 * Allocates a new array.
		 * 
		 * @param __len The array length.
		 * @return The reference type.
		 * @since 2022/06/19
		 */
		@SuppressWarnings("unchecked")
		private static Reference<String>[] newArray(int __len)
		{
			return (Reference<String>[])new Reference[__len];
		}
	}
}
