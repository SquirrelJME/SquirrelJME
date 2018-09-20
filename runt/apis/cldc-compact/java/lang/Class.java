// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.InputStream;

public final class Class<T>
{
	/** This is the prefix that is used for assertion checks. */
	private static final String _ASSERTION_PREFIX =
		"cc.squirreljme.runtime.noassert.";
	
	/** Special class reference index. */
	private final int _specialindex;
	
	/** Has the assertion status been checked already? */
	private volatile boolean _checkedassert;
	
	/** Is this class being asserted? */
	private volatile boolean _useassert;
	
	/**
	 * This constructor is internally called as needed.
	 *
	 * @param __csi Class special index.
	 * @since 2016/04/12
	 */
	private Class(int __csi)
	{
		this._specialindex = __csi;
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
	 * @since 2016/06/13
	 */
	@SuppressWarnings({"unchecked"})
	public <U> Class<? extends U> asSubclass(Class<U> __cl)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	public T cast(Object __o)
	{
		// Null always casts OK
		if (__o == null)
			return null;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns whether or not assertions should be enabled in the specified
	 * class, this is used internally by the virtual machine to determine if
	 * assertions should fail or not.
	 *
	 * In SquirrelJME, this defaults to returning {@code true}. To disable
	 * assertions for a class or an entire package then the following system
	 * property may be specified to disable them:
	 * {@code cc.squirreljme.noassert.(package)(.class)=true}.
	 *
	 * @return In SquirrelJME this returns by default {@code true}, otherwise
	 * this may return {@code false} if they are disabled for a class.
	 * @since 2016/06/13
	 */
	public boolean desiredAssertionStatus()
	{
		// If assertions have been checked, they do not have to be rechecked
		if (this._checkedassert)
			return this._useassert;
		
		// Otherwise check it
		return __checkAssertionStatus();
	}
	
	public String getName()
	{
		throw new todo.TODO();
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
		
		/*
		// If this is an array then perform the resource lookup on the
		// component type.
		Class<?> comp = this._componenttype;
		if (comp != null)
			return comp.getResourceAsStream(__name);
		
		// A fully resolved full path?
		String res;
		if (__name.startsWith("/"))
			res = __name.substring(1);
		
		// Relative name
		else
		{
			// Need to build a new name due to . and .. components
			StringBuilder sb = new StringBuilder();
			
			if (true)
				throw new todo.TODO();
			
			// Use it
			res = sb.toString();
		}
		
		// Locate the resource in the JAR that this class resides in
		VM vmi = VM.INSTANCE;
		String found = vmi.jar.findResource(this, res);
		
		// Not found?
		if (found == null)
			return null;
		
		// Open it
		return vmi.jar.openResource(found);
		*/
		throw new todo.TODO();
	}
	
	/**
	 * Returns the class which is the superclass of this class.
	 *
	 * @return The superclass or {@code null} if there is none.
	 * @since 2017/03/29
	 */
	public Class<? super T> getSuperclass()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns {@code true} if this class represents an array type.
	 *
	 * @return {@code true} if this class represents an array type.
	 * @since 2016/06/16
	 */
	public boolean isArray()
	{
		throw new todo.TODO();
	}
	
	public boolean isAssignableFrom(Class<?> __a)
	{
		throw new todo.TODO();
	}
	
	public boolean isInterface()
	{
		throw new todo.TODO();
	}
	
	public boolean isInstance(Object __o)
	{
		// Null will never be an instance
		if (__o == null)
			return false;
		
		throw new todo.TODO();
	}
	
	public T newInstance()
		throws InstantiationException, IllegalAccessException
	{
		throw new todo.TODO();
	}
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
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
		
		// {@squirreljme.error ZZ03 Could not find the specified class. (The
		// name of the class)}
		Class<?> rv = null;
		if (true)
			throw new todo.TODO();
		if (rv == null)
			throw new ClassNotFoundException(String.format("ZZ03 %s", __a));
		return rv;
	}
	
	/**
	 * This checks whether assertions should be **disabled** for this class (or
	 * for the entire package).
	 *
	 * @return The assertions status to use.
	 * @since 2016/10/09
	 */
	private final boolean __checkAssertionStatus()
	{
		// Default to true
		boolean rv = true;
		
		// Determine class name
		String cn = this.getName();
		String prop = Class._ASSERTION_PREFIX + cn;
		
		// Disabled for this class?
		if (Boolean.getBoolean(prop))
			rv = false;
		
		// Disabled for this package?
		else
		{
			// Find last dot, if there is none then this is just the default
			// package so never bother checking the package
			int ld = cn.lastIndexOf('.');
			if (ld > 0 && Boolean.getBoolean(prop.substring(0,
				prop.length() - (cn.length() - ld))))
				rv = false;
		}
		
		// Set as marked
		this._checkedassert = true;
		this._useassert = rv;
		return rv;
	}
}

