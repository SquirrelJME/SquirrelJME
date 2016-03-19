// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.cache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class provides static methods which are used to check references and to
 * initialize new values if references are {@code null}.
 *
 * Reflection is not required for this to work, other than
 * {@link Class#newInstance()}.
 *
 * @since 2016/03/19
 */
public final class Cache
{
	/** This contains initializer storage because only one is needed. */
	private static final Map<Class<Instantiable<?, ?>>,
		Reference<Instantiable<?, ?>>> _INIT_STORE =
		new WeakHashMap<>();
	
	/**
	 * The cache cannot be initialized!
	 *
	 * @since 2016/03/19
	 */
	private Cache()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * This takes an input reference and check to see if it is valid, and if
	 * it is not then a value is initialized and a new reference is created.
	 *
	 * @param <O> The owning class of the reference, this may be {@link Object}
	 * if the usage is static.
	 * @param <Z> The type of value to reference.
	 * @param __own The owner of the reference, this is passed to the
	 * initializer as the first argument, may be {@code null} if there is none.
	 * @param __ref The pre-existing reference which is checked to see if a
	 * value is contained.
	 * @param __out This is where the value is stored for returning by the
	 * calling method.
	 * @param __init This class is the initializer, it is instantiated for the
	 * creation of the given object.
	 * @return A reference wrapping the cached value, this should be stored
	 * in a local field for later usage and passed to this method.
	 * @throws IllegalInstantiableException If the instantiator is not valid.
	 * @throws NullPointerException If no output array was specified or the
	 * initializer was not specified.
	 * @since 2016/03/19
	 */
	@SuppressWarnings({"unchecked"})
	public static <O, Z> Reference<Z> cache(O __own, Reference<Z> __ref,
		Z[] __out, Class<Instantiable<O, Z>> __init)
		throws IllegalInstantiableException, NullPointerException
	{
		// Check
		if (__out == null || __init == null)
			throw new NullPointerException("NARG");
		
		// Look for an initializer in storage
		Instantiable<O, Z> ivx;
		synchronized (_INIT_STORE)
		{
			// Pre-existing one exists?
			Reference<Instantiable<?, ?>> pref = _INIT_STORE.get(__init);
			Instantiable<?, ?> pin = null;
			
			// In the reference?
			if (pref != null)
				pin = pref.get();
			
			// Needs initialization"
			if (pin == null)
			{
				// Setup
				try
				{
					pin = __init.newInstance();
				}
				
				// Cannot create it due to lack of permission or lacks a
				// null constructor
				catch (InstantiationException|IllegalAccessException e)
				{
					throw new IllegalInstantiableException("CH01", e);
				}
				
				// Not one? then bad!
				if (!(pin instanceof Instantiable))
					throw new IllegalInstantiableException("CH02");
				
				// Store it
				_INIT_STORE.put((Class<Instantiable<?,?>>)((Object)__init),
					new WeakReference<Instantiable<?,?>>(
						(Instantiable<?,?>)((Object)pin)));
			}
			
			// Cast for usage
			ivx = (Instantiable<O, Z>)pin;
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * This takes an input reference and check to see if it is valid, and if
	 * it is not then a value is initialized and a new reference is created.
	 *
	 * The owner is assumed to be of type {@link Object} and is {@code null}.
	 *
	 * @param <Z> The type of value to reference.
	 * @param __ref The pre-existing reference which is checked to see if a
	 * value is contained.
	 * @param __out This is where the value is stored for returning by the
	 * calling method.
	 * @param __init This class is the initializer, it is instantiated for the
	 * creation of the given object.
	 * @return A reference wrapping the cached value, this should be stored
	 * in a local field for later usage and passed to this method.
	 * @throws IllegalInstantiableException If the instantiator is not valid.
	 * @throws NullPointerException If no output array was specified or the
	 * initializer was not specified.
	 * @since 2016/03/19
	 */
	public static <Z> Reference<Z> cacheStatic(Reference<Z> __ref, Z[] __out,
		Class<Instantiable<Object, Z>> __init)
		throws IllegalInstantiableException, NullPointerException
	{
		return Cache.<Object, Z>cache(null, __ref, __out, __init);
	}
	
	/**
	 * This creates a suitable array which is used for output by the cache
	 * manager.
	 *
	 * @param <Z> The type of object to store in the array.
	 * @return The array where the cached result is stored.
	 * @since 2016/03/19
	 */
	@SuppressWarnings({"unchecked"})
	public static <Z> Z[] output()
	{
		return (Z[])(new Object[1]);
	}
}

