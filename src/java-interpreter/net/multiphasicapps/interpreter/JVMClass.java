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
	implements JVMComponentType
{
	/** Internal lock. */
	Object lock =
		new Object();	
	
	/** The interpreter engine which owns this class. */
	protected final JVMEngine engine;
	
	/** The component type of this class. */
	protected final JVMComponentType componenttype;
	
	/** Class interfaces. */
	protected final JVMClassInterfaces interfaces =
		new JVMClassInterfaces(this);
	
	/** Class fields. */
	protected final JVMFields fields =
		new JVMFields(this);
	
	/** Class methods. */
	protected final JVMMethods methods =
		new JVMMethods(this);
	
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
		componenttype = null;
	}
	
	/**
	 * This initializes a class which is an array of the given component type.
	 *
	 * @param __eng The owning engine.
	 * @param __ct The component type of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	protected JVMClass(JVMEngine __eng, JVMComponentType __ct)
		throws NullPointerException
	{
		// Check
		if (__eng == null || __ct == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __eng;
		componenttype = null;
		
		// Flags are fixed
		_flags = new JVMClassFlags(JVMClassFlag.PUBLIC.mask |
			JVMClassFlag.FINAL.mask |
			JVMClassFlag.SYNTHETIC.mask);
		
		// The this name of the class is the component type as a field with
		// an array marker atatched
		_this = new ClassNameSymbol("[" + __ct.getThisName().asField());
		
		// The super class is always object
		_super = ClassNameSymbol.BINARY_OBJECT;
		_superset = true;
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
	 * @throws IllegalStateException If the name of the class was not set.
	 * @since 2016/03/02
	 */
	public final String getClassLoaderName()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<String> ref = _clbname;
			String rv;
			
			// Needs to be cached?
			if (ref == null || null == (rv = ref.get()))
			{
				// If an array, then the field syntax is directly used
				if (isArray())
					rv = getThisName().toString();
				
				// Otherwise use the binary form instead but with characters
				// replaced
				else
					rv = getThisName().toString().replace('/', '.');
				
				// Cache it
				_clbname = new WeakReference<>(rv);
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns the fields which are mapped to this class.
	 *
	 * @return The field mappings.
	 * @since 2016/03/20
	 */
	public final JVMFields getFields()
	{
		return fields;
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
	 * Returns the set of interfaces which may be implemented by the class.
	 *
	 * @return The class interface set.
	 * @since 2016/03/19
	 */
	public final JVMClassInterfaces getInterfaces()
	{
		return interfaces;
	}
	
	/**
	 * Returns the methods which are mapped to this class.
	 *
	 * @return The method mappings.
	 * @since 2016/03/20
	 */
	public final JVMMethods getMethods()
	{
		return methods;
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
	 * Is this class an array?
	 *
	 * @return {@code true} if this class is an array.
	 * @since 2016/03/31
	 */
	public boolean isArray()
	{
		return componenttype != null;
	}
	
	/**
	 * Sets the flags used by this class.
	 *
	 * @param __fl The class flags.
	 * @return {@code this}.
	 * @throws IllegalStateException If this is an array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	public final JVMClass setFlags(JVMClassFlags __fl)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__fl == null)
			throw new NullPointerException("NARG");
		if (isArray())
			throw new IllegalStateException("IN2n");
		
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
	 * The set class name is removed from the list of implemented interfaces.
	 *
	 * @param __n The name to set.
	 * @return {@code this}.
	 * @throws IllegalStateException Of the current class name is not set, or
	 * this is an array.
	 * @throws JVMClassFormatError If this class name is {@link Object} and
	 * a super class was attempted to be set.
	 * @since 2016/03/19
	 */
	public final JVMClass setSuperName(ClassNameSymbol __n)
		throws IllegalStateException, JVMClassFormatError
	{
		// Cannot be an array
		if (__n != null && __n.isArray())
			throw new JVMClassFormatError(String.format("IN0z %s", __n));
		if (isArray())
			throw new IllegalStateException("IN2o");
		
		// Lock
		synchronized (lock)
		{
			// Get current class name
			ClassNameSymbol self = getThisName();
			
			// Cannot be this
			if (__n != null && self.equals(__n))
				throw new JVMClassFormatError(String.format("IN12 %s", __n));
			
			// If it is object then super-class is not valid, or if this is
			// not object then it must be set
			boolean isobj = self.equals("java/lang/Object");
			if ((__n != null && isobj) || (__n == null && !isobj))
				throw new JVMClassFormatError(String.format("IN07 %s %s",
					self, __n));
			
			// Set
			_super = __n;
			_superset = true;
			
			// Remove from interfaces
			getInterfaces().remove(__n);
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
	 * The implemented interface list is cleared of the input class.
	 *
	 * @param __n The name of the class.
	 * @return {@code this}.
	 * @throws IllegalStateException If this is an array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	public final JVMClass setThisName(ClassNameSymbol __n)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		if (isArray())
			throw new IllegalStateException("IN2p");
		
		// Lock
		synchronized (lock)
		{
			// Set
			_this = __n;
			
			// Invalidate cache
			_clbname = null;
			
			// Get super class
			ClassNameSymbol sup = _super;
			
			// If not set or this is the super class, then correct to Object
			boolean tio = __n.equals("java/lang/Object");
			if ((sup == null && !tio) || (sup != null && sup.equals(__n)))
				_super = ClassNameSymbol.BINARY_OBJECT;
			
			// Otherwise clear if not already set
			else if (sup != null && tio)
				_super = null;
			
			// Remove interface
			getInterfaces().remove(__n);
		}
		
		// Self
		return this;
	}
}

