// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This is the base class for all class related information.
 *
 * @since 2016/03/16
 */
public class JVMClass
{
	/** Internal lock. */
	protected Object lock =
		new Object();	
	
	/** The interpreter engine which owns this class. */
	protected final JVMEngine engine;
	
	/** The current class flags. */
	private volatile JVMClassFlags _flags;
	
	/** The name of the current class. */
	private volatile ClassNameSymbol _this;
	
	/** The name of the super class. */
	private volatile ClassNameSymbol _super;
	
	/** Was the super-class name set? */
	private volatile boolean _superset;
	
	/** Class loader formatted binary name cache. */
	private volatile Reference<String> _clbname;
	
	/** Fields which exist in this class, lock on this. */
	@Deprecated
	protected final Map<JVMMemberKey<FieldSymbol>, JVMField> fields =
		new HashMap<>();
	
	/** Methods which exist in this class, lock on this. */
	@Deprecated
	protected final Map<JVMMemberKey<MethodSymbol>, JVMMethod> methods =
		new HashMap<>();
	
	/**
	 * Initializes the base class information.
	 *
	 * @param __eng The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/16
	 */
	protected JVMClass(JVMEngine __eng)
		throws NullPointerException
	{
		// Check
		if (__eng == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __eng;
	}
	
	/**
	 * Returns the engine which initialized this class.
	 *
	 * @return The owning engine
	 * @since 2016/03/16
	 */
	public final JVMEngine engine()
	{
		return engine;
	}
	
	/**
	 * Returns the name of this class as returned by {@link Class#getName()}.
	 *
	 * @return The name of this class.
	 * @since 2016/03/02
	 */
	public final String getClassLoaderName()
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<String> ref = _clbname;
			String rv;
			
			// Needs to be cached?
			if (ref == null || null == (rv = ref.get()))
				_clbname = new WeakReference<>((rv =
					getThisName().toString().replace('/', '.')));
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Locates a field in the class by the given identifier anme and field
	 * descriptor.
	 *
	 * @param __name The name of the field.
	 * @param __desc The field descriptor.
	 * @param __super Look in super classes for the field?
	 * @return The field matching the name and descriptor or {@code null} if
	 * none was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	public final JVMField getField(IdentifierSymbol __name, FieldSymbol __desc,
		boolean __super)
	{
		return this.<JVMField, FieldSymbol>__getMember(fields, __name, __desc,
			__super);
	}
	
	/**
	 * Returns the flags which are specified by this class.
	 *
	 * @return The used class flags.
	 * @throws IllegalStateException If flags are not set.
	 * @since 2016/03/16
	 */
	public final JVMClassFlags getFlags()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Not set?
			JVMClassFlags rv = _flags;
			if (rv == null)
				throw new IllegalStateException("IN0v");
			
			// Return them
			return rv; 
		}
	}
	
	/**
	 * Locates a method in the class by the given identifier name and method
	 * descriptor.
	 *
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @param __super Look in super classes for the method?
	 * @return The method matching the name and descriptor or {@code null}
	 * if none was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public final JVMMethod getMethod(IdentifierSymbol __name,
		MethodSymbol __desc, boolean __super)
		throws NullPointerException
	{
		return this.<JVMMethod, MethodSymbol>__getMember(methods, __name,
			__desc, __super);
	}
	
	/**
	 * Returns the super class name of this class.
	 *
	 * @return The super class this extends.
	 * @throws IllegalStateException If the super class name was not set.
	 * @since 2016/03/19
	 */
	public final ClassNameSymbol getSuperName()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// not set?
			if (!_superset)
				throw new IllegalStateException("IN0y");
			
			// Return it
			return _super;
		}
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @throws IllegalStateException On null arguments.
	 * @since 2016/03/19
	 */
	public final ClassNameSymbol getThisName()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Must be set
			ClassNameSymbol rv = _this;
			if (rv == null)
				throw new IllegalStateException("IN0x");
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Registers a field or a method with this class.
	 *
	 * @param __m The member to register to this class.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the member is owned by another
	 * class, or it is of an unknown type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	protected final JVMClass registerMember(JVMMember<?> __m)
		throws IllegalArgumentException, NullPointerException
	{	
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Wrong class?
		if (__m.outerClass() != this)
			throw new IllegalArgumentException("IN0t");
		
		// A field?
		if (__m instanceof JVMField)
			synchronized (fields)
			{
				fields.put(new JVMMemberKey<>(__m.name(),
					__m.type().asFieldSymbol()), (JVMField)__m);
			}
		
		// A method
		else if (__m instanceof JVMMethod)
			synchronized (methods)
			{
				methods.put(new JVMMemberKey<>(__m.name(),
					__m.type().asMethodSymbol()), (JVMMethod)__m);
			}
		
		// Unknown
		else
			throw new IllegalArgumentException("IN0u");
		
		// Self
		return this;
	}
	
	/**
	 * Sets the flags used by this class.
	 *
	 * @param __fl The class flags.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	public final JVMClass setFlags(JVMClassFlags __fl)
		throws NullPointerException
	{
		// Check
		if (__fl == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// Set
			_flags = __fl;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Sets the name of the super class, the current class name must be set
	 * before this is called.
	 *
	 * @param __n The name to set.
	 * @return {@code this}.
	 * @throws IllegalStateException Of the current class name is not set.
	 * @throws JVMClassFormatError If this class name is {@link Object} and
	 * a super class was attempted to be set.
	 * @since 2016/03/19
	 */
	public final JVMClass setSuperName(ClassNameSymbol __n)
		throws IllegalStateException, JVMClassFormatError
	{
		// Lock
		synchronized (lock)
		{
			// Get current class name
			ClassNameSymbol self = getThisName();
			
			// If it is object then super-class is not valid, or if this is
			// not object then it must be set
			boolean isobj = self.equals("java/lang/Object");
			if ((__n != null && isobj) || (__n == null && !isobj))
				throw new JVMClassFormatError(String.format("IN07 %s %s",
					self, __n));
			
			// Set
			_super = __n;
			_superset = true;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Sets the name of the current class.
	 *
	 * If the super class was already specified, then the super class may
	 * be adjusted accordingly when moving to or from {@link Object}.
	 *
	 * @param __n The name of the class.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	public final JVMClass setThisName(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// Set
			_this = __n;
			
			// Invalidate cache
			_clbname = null;
			
			// Get super class
			ClassNameSymbol sup = _super;
			
			// Not set?
			boolean tio = __n.equals("java/lang/Object");
			if (sup == null && !tio)
				_super = new ClassNameSymbol("java/lang/Object");
			
			// Is set?
			else if (sup != null && tio)
				_super = null;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Locates a member by the given name and type.
	 *
	 * @param <V> The type of member to find.
	 * @param <S> The symbol type it uses as a descriptor.
	 * @param __lookin The map to look inside for members.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @param __super If not found in this class, should superclasses be
	 * searched?
	 * @return The member with the given name and type, or {@code null} if
	 * not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016//03/17
	 */
	private <V extends JVMMember, S extends MemberTypeSymbol> V __getMember(
		Map<JVMMemberKey<S>, V> __lookin, IdentifierSymbol __name, S __type,
		boolean __super)
		throws NullPointerException
	{
		return this.<V, S>__getMember(__lookin, new JVMMemberKey<>(__name,
			__type), __super);
	}
	
	/**
	 * Locates a member by the given name and type.
	 *
	 * @param <V> The type of member to find.
	 * @param <S> The symbol type it uses as a descriptor.
	 * @param __lookin The map to look inside for members.
	 * @param __key The member key to use during the search
	 * @param __super If not found in this class, should superclasses be
	 * searched?
	 * @return The member with the given name and type, or {@code null} if
	 * not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016//03/17
	 */
	private <V extends JVMMember, S extends MemberTypeSymbol> V __getMember(
		Map<JVMMemberKey<S>, V> __lookin, JVMMemberKey<S> __key,
		boolean __super)
		throws NullPointerException
	{
		// Check
		if (__lookin == null || __key == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (__lookin)
		{
			// Go through the map, if it is found then use it
			V rv = __lookin.get(__key);
			if (rv != null)
				return rv;
			
			// Look in super class?
			if (__super)
				throw new Error("TODO");
			
			// Not found
			return null;
		}
	}
}

