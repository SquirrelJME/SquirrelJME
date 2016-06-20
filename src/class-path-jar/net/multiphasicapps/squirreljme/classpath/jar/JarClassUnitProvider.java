// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath.jar;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;

/**
 * This is the base class for any facility which needs to provide class units
 * as JAR files. This class must be extended and the sub-classes must implement
 * the required methods needed to locate {@link SeekableByteChannel}s.
 *
 * The implementation of this class uses keys to create and maintain references
 * to JAR files without requiring that they be opened while the object exists
 * and is referenced in memory.
 *
 * @since 2016/05/25
 */
public abstract class JarClassUnitProvider
	extends ClassUnitProvider
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Mapping of class unit keys to actual class units. */
	private final Map<String, Reference<JarClassUnit>> _cache =
		new HashMap<>();
	
	/**
	 * Creates a new class unit using the given key.
	 *
	 * @param __k The key to use for the class unit, {@code null} keys are not
	 * valid.
	 * @return The class unit which is associated with the given key,
	 * {@code null} may be returned if the key is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/28
	 */
	protected abstract JarClassUnit createClassUnit(String __k)
		throws NullPointerException;
	
	/**
	 * Returns the collection of keys which are available.
	 *
	 * @return The collection of available keys.
	 * @since 2016/05/26
	 */
	protected abstract Collection<String> keyCollection();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final ClassUnit[] classUnits()
	{
		// Lock
		synchronized (this.lock)
		{
			// Obtian all the possible keys
			Collection<String> keys = keyCollection();
			
			// The output array of JAR units
			List<JarClassUnit> units = new LinkedList<>();
			
			// Go through the map and remove any collected references
			Map<String, Reference<JarClassUnit>> cache = this._cache;
			Iterator<Map.Entry<String, Reference<JarClassUnit>>> it =
				cache.entrySet().iterator();
			while (it.hasNext())
			{
				// Get key and value
				Map.Entry<String, Reference<JarClassUnit>> e = it.next();
				String k = e.getKey();
				Reference<JarClassUnit> v = e.getValue();
				
				// In the target mapping?
				boolean want = keys.contains(k);
				
				// If the reference was cleared, remove the key
				JarClassUnit ju;
				if (v == null || null == (ju = v.get()))
					it.remove();
				
				// Otherwise if it is wanted, add to the output
				else if (want)
					units.add(ju);
			}
			
			// Go through all keys to find entries which were not in the
			// original map.
			for (String k : keys)
				if (!cache.containsKey(k))
				{
					// Create unit
					JarClassUnit v = createClassUnit(k);
					if (v == null)
						continue;
					
					// Add it
					units.add(v);
					cache.put(k, new WeakReference<>(v));
				}
			
			// Return the list as an array
			return units.<JarClassUnit>toArray(
				new JarClassUnit[units.size()]);
		}
	}
}

