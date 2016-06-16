// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.InputStream;

public final class Class<T>
{
	/** If this is an array then this will be the component type. */
	private final Class<?> _componenttype =
		__getComponentType();
	
	/**
	 * This method may or may not be called internally by the virtual machine
	 * when it initializes a new class object for a given class type.
	 *
	 * @since 2016/04/12
	 */
	private Class()
	{
	}
	
	/**
	 * This checks whether the specified input class extends this class or
	 * implements an interface and then returns this class object which is
	 * "cast" to the specified type. Note that this does not change the
	 * returned value.
	 *
	 * @param <U> The sub-class to cast this class object to.
	 * @param __cl A class which is checked to see if it extends or implements
	 * this class.
	 * @return {@code this} except cast to the specified sub-class
	 * @throws ClassCastException If the specified class is not a sub-class of
	 * this class type.
	 * @throws NullPointerException On null arguments.
	 * @sicne 2016/06/13
	 */
	@SuppressWarnings({"unchecked"})
	public <U> Class<? extends U> asSubclass(Class<U> __cl)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	public T cast(Object __o)
	{
		// Null always casts OK
		if (__o == null)
			return null;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns whether or not assertions should be enabled in the specified
	 * class, this is used internally by the virtual machine to determine if
	 * assertions should fail or not.
	 *
	 * In SquirrelJME, this always returns {@code true}.
	 *
	 * @return In SquirrelJME, always returns {@code true}.
	 * @since 2016/06/13
	 */
	public boolean desiredAssertionStatus()
	{
		return true;
	}
	
	public String getName()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Obtains a resource from the classpath which exists within a JAR file,
	 * inside of a directory, or in a prepacked resource. If a resource needs
	 * to be obtain from another class which exists in another JAR file then
	 * this method must be called from a class in that JAR.
	 *
	 * Relative names are based on the method which called this method.
	 * Instances of {@code /./} will be translated to {@code /} and
	 * {@code /foo/../} will be translated to {@code /}.
	 *
	 * In the Java ME environment, one should not rely on getting resources
	 * which are executable class files (files ending in .class). These class
	 * files may be deleted during native compilation. This however should not
	 * be relied upon.
	 *
	 * Using this method on the classes for primitive types ({@code int.class})
	 * and using a relative name will always result in the path being treated
	 * as absolute.
	 *
	 * @param __name The name of the resource to find, if this starts with a
	 * forward slash {@code '/'} then it is treated as an absolute path.
	 * Otherwise a resource will be derived from the calling class.
	 * @return A stream to the given resource or {@code null} if one was not
	 * found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public InputStream getResourceAsStream(String __name)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Get the real component type of this class
		if (true)
			throw new Error("TODO");
		Class<?> real = null;/*Magic.componentType(this);*/
		
		// If not absolute, make it absolute
		if (!__name.startsWith("/"))
		{
			// If this class represents a primitive type, then primitive types
			// are considered to be in the default package. In this case, a
			// slash is just prepended to it.
			// This is from observed behavior from OpenJDK on the desktop,
			// which is the reference implementation.
			if (real == boolean.class ||
				real == byte.class ||
				real == short.class ||
				real == char.class ||
				real == int.class ||
				real == long.class ||
				real == float.class ||
				real == double.class)
				__name = '/' + __name;
			
			// Otherwise base the resource based on the class this code is
			// being called from (the method directly above this one).
			// So if a called was made from the package "foo.bar" and a
			// resource called "orange/lime.lemon" was requested, the full path
			// will be "/foo/bar/orange/lime.lemon".
			// 
			else
				throw new Error("TODO");
		}
		
		// {@squirreljme.error ZZ0l A request for a resource was made which
		// does not start with a leading slash. (The name of the requested
		//resource)}
		if (!__name.startsWith("/"))
			throw new AssertionError(String.format("ZZ0l %s", __name));
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if this class represents an array type.
	 *
	 * @return {@code true} if this class represents an array type.
	 * @since 2016/06/16
	 */
	public boolean isArray()
	{
		return this._componenttype != null;
	}
	
	public boolean isAssignableFrom(Class<?> __a)
	{
		throw new Error("TODO");
	}
	
	public boolean isInterface()
	{
		throw new Error("TODO");
	}
	
	public boolean isInstance(Object __o)
	{
		// Null will never be an instance
		if (__o == null)
			return false;
		
		throw new Error("TODO");
	}
	
	public T newInstance()
		throws InstantiationException, IllegalAccessException
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Locates the class with the given name and returns it, otherwise an
	 * exception is thrown.
	 *
	 * The expected form of the class is a name as is mostly used in the
	 * Java language ({@code some.package.Foo}) and not one that is internal
	 * to the virtual machine except in the case of an array. Inner classes do
	 * not follow dot notation, an inner class is usually separated by a dollar
	 * sign '$'. For example {@code Map.Entry} is {@code java.util.Map$Entry}.
	 *
	 * If an array is requested then it must only be of a primitive type using
	 * a Java internal type descriptor.
	 *
	 * @param __a The name of the class to find.
	 * @return The class with the given name.
	 * @throws ClassNotFoundException If the given class was not found.
	 * @throws NullPointerException If no name was specified.
	 * @since 2016/03/01
	 */
	public static Class<?> forName(String __a)
		throws ClassNotFoundException
	{
		// No class specified
		if (__a == null)
			throw new NullPointerException();
		
		throw new Error("TODO");
	}
	
	/**
	 * The component type field is initialized by the virtual machine.
	 *
	 * @return The component type, or {@code null} if not an array.
	 * @since 2016/06/16
	 */
	private final Class<?> __getComponentType()
	{
		return this._componenttype;
	}
}


