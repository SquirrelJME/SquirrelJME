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

import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
import cc.squirreljme.runtime.cldc.asm.ResourceAccess;
import cc.squirreljme.runtime.cldc.io.ResourceInputStream;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public final class Class<T>
{
	/** This is the prefix that is used for assertion checks. */
	private static final String _ASSERTION_PREFIX =
		"cc.squirreljme.runtime.noassert.";
	
	/** The super class of this class. */
	private final Class<?> _superclass;
	
	/** The interface classes of this class. */
	private final Class<?>[] _interfaceclasses;
	
	/** The component type. */
	private final Class<?> _component;
	
	/** The binary name of this class. */
	final String _binaryname;
	
	/** Special class reference index. */
	private final int _specialindex;
	
	/** The number of dimensions. */
	private final int _dimensions;
	
	/** The JAR this class is in. */
	private final String _injar;
	
	/** Is this an interface. */
	private final boolean _isinterface;
	
	/** Has the assertion status been checked already? */
	private volatile boolean _checkedassert;
	
	/** Is this class being asserted? */
	private volatile boolean _useassert;
	
	/** The display name of the class. */
	private Reference<String> _name;
	
	/** String representation of class. */
	private Reference<String> _string;
	
	/**
	 * This constructor is internally called as needed.
	 *
	 * @param __csi Class special index.
	 * @param __bn The binary name of this class.
	 * @param __sc Super classes.
	 * @param __ic Interface classes.
	 * @param __ij The JAR this class is in.
	 * @param __int Is this an interface?
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	private Class(int __csi, String __bn, Class<?> __sc, Class<?>[] __ic,
		Class<?> __ct, String __ij, boolean __int)
		throws NullPointerException
	{
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		this._specialindex = __csi;
		this._binaryname = __bn;
		this._superclass = __sc;
		this._interfaceclasses = __ic;
		this._component = __ct;
		this._injar = __ij;
		this._isinterface = __int;
		
		// Count dimensions, used for comparison purposes
		int dims = 0;
		for (; __bn.charAt(dims) == '['; dims++)
			;
		this._dimensions = dims;
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
	
	/**
	 * Casts the object to this class, checking it.
	 *
	 * @param __o The object to cast.
	 * @return The casted object.
	 * @throws ClassCastException If the type is not matched.
	 * @since 2018/09/29
	 */
	@SuppressWarnings({"unchecked"})
	public T cast(Object __o)
		throws ClassCastException
	{
		// Null always casts OK
		if (__o == null)
			return null;
		
		// {@squirreljme.error ZZ26 The other class cannot be casted to this
		// class. (This class; The other class)}
		Class<?> other = __o.getClass();
		if (!this.isAssignableFrom(other))
			throw new ClassCastException("ZZ26 " + this.getName() + " " +
				other.getName());
		
		return (T)__o;
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
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2018/09/22
	 */
	public String getName()
	{
		Reference<String> ref = this._name;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			String bn = this._binaryname;
			
			// Slashes become dots
			this._name = new WeakReference<>((rv = bn.replace('/', '.')));
		}
		
		return rv;
	}
	
	/**
	 * Obtains a resource from the classpath which exists within a JAR file,
	 * inside of a directory, or in a prepacked resource. If a resource needs
	 * to be obtain from another class which exists in another JAR file then
	 * this method must be called from a class in that JAR.
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
	 * Relative paths are converted to absolute paths by appending the name
	 * to the binary name of the class package.
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
		
		// This is not within any JAR, so nothing will ever be found
		String injar = this._injar;
		if (injar == null)
			return null;
		
		// Absolute paths are not translated
		String want;
		if (__name.startsWith("/"))
			want = __name.substring(1);
		
		// Otherwise append to the binary name
		else
		{
			// Has a package
			String pkg = this._binaryname;
			int ld = pkg.lastIndexOf('/');
			if (ld >= 0)
				want = pkg.substring(0, ld + 1) + __name;
			
			// Is in default package
			else
				want = __name;
		}
		
		// Open the resource, perhaps
		return ResourceInputStream.open(injar, want);
	}
	
	/**
	 * Returns the class which is the superclass of this class.
	 *
	 * @return The superclass or {@code null} if there is none.
	 * @since 2017/03/29
	 */
	@SuppressWarnings({"unchecked"})
	public Class<? super T> getSuperclass()
	{
		return (Class<? super T>)((Object)this._superclass);
	}
	
	/**
	 * Returns {@code true} if this class represents an array type.
	 *
	 * @return {@code true} if this class represents an array type.
	 * @since 2016/06/16
	 */
	public boolean isArray()
	{
		// Guess what! Every array starts with with brackets so this is quite
		// easily something which can be determined from the classname
		return this._binaryname.startsWith("[");
	}
	
	/**
	 * Checks if the given class can be assigned to this one, the check is
	 * in the same order as {@link #instanceOf(Object)} that is
	 * {@code a.getClass().isAssignableFrom(b.getClass()) == (a instanceof b)}.
	 *
	 * @param __cl The other class type.
	 * @return If the otehr class can be assigned to this one.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/27
	 */
	public boolean isAssignableFrom(Class<?> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Go through target superclasses to find this class
		for (Class<?> r = __cl; r != null; r = r._superclass)
		{
			if (r == this)
				return true;
		
			// Go through interfaces for the class to find this class
			for (Class<?> i : r._interfaceclasses)
				if (i == this)
					return true;
		}
		
		// If this is an array and the other type is an array with the same
		// number of dimensions, then compare the base type so that say
		// Number[] is assignable from Integer[].
		int thisdims = this._dimensions,
			otherdims = __cl._dimensions;
		if (thisdims > 0 && thisdims == otherdims)
			if (this.__rootType().isAssignableFrom(__cl.__rootType()))
				return true;
		
		// Not assignable
		return false;
	}
	
	/**
	 * Is this class an interface?
	 *
	 * @return If this is an interface.
	 * @since 2018/11/03
	 */
	public boolean isInterface()
	{
		return this._isinterface;
	}
	
	/**
	 * Checks if the given class is an instance of this class.
	 *
	 * @param __o The object to check.
	 * @return If the given object is an instance of this class.
	 * @since 2018/09/27
	 */
	public boolean isInstance(Object __o)
	{
		// Null will never be an instance
		if (__o == null)
			return false;
		
		// This is in the same form
		return this.isAssignableFrom(__o.getClass());
	}
	
	public T newInstance()
		throws InstantiationException, IllegalAccessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/03
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Based on the binary name
			String binaryname = this._binaryname;
			switch (binaryname)
			{
					// Primitive types have the same binary name
				case "boolean":
				case "byte":
				case "short":
				case "char":
				case "int":
				case "long":
				case "float":
				case "double":
					rv = binaryname;
					break;
				
					// Otherwise build a string
				default:
					rv = (this._isinterface ? "interface " : "class ") +
						this.getName(); 
					break;
			}
			
			// Cache it
			this._string = new WeakReference<>(rv);
		}
		
		return rv;
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
	 * @param __n The name of the class to find.
	 * @return The class with the given name.
	 * @throws ClassNotFoundException If the given class was not found.
	 * @throws NullPointerException If no name was specified.
	 * @since 2016/03/01
	 */
	public static Class<?> forName(String __n)
		throws ClassNotFoundException
	{
		// No class specified
		if (__n == null)
			throw new NullPointerException();
		
		// The name will have to be converted to binary form since that is
		// what is internally used
		Class<?> rv = ObjectAccess.classByName(__n.replace('.', '/'));
		
		// {@squirreljme.error ZZ03 Could not find the specified class. (The
		// name of the class)}
		if (rv == null)
			throw new ClassNotFoundException(String.format("ZZ03 %s", __n));
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
	
	/**
	 * Returns the root type, the base of the component.
	 *
	 * @return The root type of this type.
	 * @since 2018/09/27
	 */
	private final Class<?> __rootType()
	{
		Class<?> rv = this;
		for (Class<?> r = this; r != null; r = r._component)
			rv = r;
		return rv;
	}
}

