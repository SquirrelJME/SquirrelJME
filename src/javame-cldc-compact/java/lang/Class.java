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
	private Class()
	{
		throw new Error("TODO");
	}
	
	public <U> Class<? extends U> asSubclass(Class<U> __a)
	{
		throw new Error("TODO");
	}
	
	public T cast(Object __a)
	{
		throw new Error("TODO");
	}
	
	public boolean desiredAssertionStatus()
	{
		throw new Error("TODO");
	}
	
	public String getName()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Obtains a resource 
	 *
	 * @param __name The name of the resource to find, if this starts with a
	 * forward slash {@code '/'} then it is treated as an absolute path.
	 * Otherwise a resource will be derived from the current class.
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
			throw new NullPointerException();
		
		// Get the real component type of this class
		Class<?> real = __guessComponentType();
		
		// If not absolute, make it absolute
		if (!__name.startsWith("/"))
		{
			// If this class represents a primitive type, then primitive types
			// are considered to be in the default package. In this case, a
			// slash is just prepended to it.
			if (real == boolean.class ||
				real == byte.class ||
				real == short.class ||
				real == char.class ||
				real == int.class ||
				real == long.class ||
				real == float.class ||
				real == double.class)
				__name = '/' + __name;
			
			// Otherwise the package this class is in must be derived.
			else
			{
				// Get name of the current class, which needs to be translated
				// in order to get the right resource
				String namethis = real.getName().replace('.', '/');
			
				// Get the last slash to chop it off
				int ls = namethis.lastIndexOf('/');
			
				// Does not have a slash to cut, assume default namespace
				if (ls < 0)
					__name = '/' + __name;
				
				// Otherwise clip it and add in
				else
					__name = '/' + namethis.substring(0, ls) + __name;
			}
		}
		
		// Sanity check
		if (!__name.startsWith("/"))
			throw new AssertionError("Requested a resource " +
				"which resulted in a non-absolute path '" + __name + "'.");
		
		throw new Error("TODO");
	}
	
	public boolean isArray()
	{
		throw new Error("TODO");
	}
	
	public boolean isAssignableFrom(Class<?> __a)
	{
		throw new Error("TODO");
	}
	
	public boolean isInterface()
	{
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
	 * Guesses the component type of this class if it is an array, otherwise
	 * this returns {@code this}.
	 *
	 * @return The true component type of this class or {@code this} if not
	 * an array.
	 * @since 2016/03/01
	 */
	private final Class<?> __guessComponentType()
	{
		// If not an array, return self
		if (!isArray())
			return this;
		
		// Otherwise get the name of this
		String name = getName();
		
		// Go through and decode the descriptor type
		int n = name.length();
		for (int i = 0; i < n; i++)
		{
			// Get character here
			char c = name.charAt(i);
			
			// Ignore array brackets
			if (c == '[')
				continue;
			
			// The class here depends on the character code used
			switch (c)
			{
					// Standard letters
				case 'Z': return boolean.class;
				case 'B': return byte.class;
				case 'C': return char.class;
				case 'D': return double.class;
				case 'F': return float.class;
				case 'I': return int.class;
				case 'J': return long.class;
				case 'S': return short.class;
				
					// A class name
				case 'L':
					// Make sure the last is a semicolon
					c = name.charAt(n - 1);
					
					// Not one?
					if (c == ';')
						throw new ClassFormatError("Class '" + name + "' " +
							"is a reference type, but it does not end in a " +
							"semi-colon.");
					
					// Get the given class in the bracket area
					String used = name.substring(i + 1, n - 1);
					
					// Before translation into dot form, the name cannot
					// contain internal VM characters such as '.', '[', or ';'.
					if (used.indexOf('.') >= 0 || used.indexOf('[') >= 0 ||
						used.indexOf(';') >= 0)
						throw new ClassFormatError("Class '" + name + "' " +
							"is not a valid class name as it contains " +
							"reserved characters.");
					
					// Otherwise find the class with the given name, which may
					// fail
					try
					{
						return Class.forName(used.replace('/', '.'));
					}
					
					// Does not exist?
					catch (ClassNotFoundException nsce)
					{
						throw new AssertionError("Could not find the class " +
							"'" + used + "' which belongs to the array of " +
							"class '" + name + "'.");
					}
				
					// Unknown, this is bad
				default:
					throw new ClassFormatError("Class '" + name + "' has an " +
						"illegal array type of '" + c + "'.");
			}
		}
		
		// Failure
		throw new AssertionError("Could not guess the component type of " +
			"class '" + name + "'.");
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
}


