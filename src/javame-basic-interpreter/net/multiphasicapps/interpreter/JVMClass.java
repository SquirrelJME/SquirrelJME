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

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.CFClass;
import net.multiphasicapps.classfile.CFClassFlag;
import net.multiphasicapps.classfile.CFClassFlags;
import net.multiphasicapps.classfile.CFClassParser;
import net.multiphasicapps.classfile.CFFormatException;
import net.multiphasicapps.classfile.CFMemberKey;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a class which is loaded by the virtual machine.
 *
 * @since 2016/04/02
 */
public final class JVMClass
	implements JVMComponentType
{
	/** All arrays use the given flags. */
	public static final CFClassFlags ARRAY_FLAGS =
		new CFClassFlags(CFClassFlag.PUBLIC.mask |
			CFClassFlag.FINAL.mask |
			CFClassFlag.SYNTHETIC.mask);
	
	/** The name of the Class class. */
	public static final ClassNameSymbol CLASS_NAME =
		new ClassNameSymbol("java/lang/Class");
	
	/** The name of the cloneable interface. */
	public static final ClassNameSymbol CLONEABLE_NAME =
		new ClassNameSymbol("java/lang/Cloneable");
	
	/** This is the key which is associated with the static class init. */
	public static CFMemberKey<MethodSymbol> CLINIT_KEY =
		new CFMemberKey<>(new IdentifierSymbol("<clinit>"),
			new MethodSymbol("()V"));
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The owning engine. */
	protected final JVMEngine engine;
	
	/** The class path which spawned this. */
	protected final JVMClassPath owningpath;
	
	/** The name of this class. */
	protected final ClassNameSymbol thisname;
	
	/** The based class file. */
	private volatile CFClass _classfile;
	
	/** Methods contained in this class. */
	private volatile JVMMethods _methods;
	
	/** The initialized {@link Class} object. */
	private volatile Reference<JVMObject> _classobject;
	
	/** The super class of this class. */
	private volatile JVMClass _superclass;
	
	/** Interfaces this implements. */
	private volatile Set<JVMClass> _interfaceclasses;
	
	/** No super-class? */
	private volatile boolean _nosuperclass;
	
	/**
	 * Initalizes the lazily loaded class.
	 *
	 * @param __cp The owning class path.
	 * @param __bn The name of this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/06
	 */
	JVMClass(JVMClassPath __cp, ClassNameSymbol __bn)
		throws NullPointerException
	{
		// Check
		if (__cp == null || __bn == null)
			throw new NullPointerException("NARG");
		
		// Set
		owningpath = __cp;
		engine = owningpath.engine();
		thisname = __bn;
	}
	
	/**
	 * Returns the compiler representation of the class that this is based on.
	 *
	 * @return The class file, or {@code null} if an array.
	 * @since 2016/04/04
	 */
	public final CFClass base()
	{
		// No base for arrays
		if (thisname.isArray())
			return null;
		
		// Lock
		synchronized (lock)
		{
			// Already loaded?
			CFClass rv = _classfile;
			
			// Needs loading?
			String res;
			if (rv == null)
				try (InputStream is = owningpath.getResourceAsStream((res =
					thisname.asClassLoaderName().resourceName())))
				{
					// {@squirreljme.error IN04 The current class has no
					// definition. (The class which does not exist; The
					// resource which was attempted to be obtained)}
					if (is == null)
						throw new JVMNoClassDefFoundError(
							String.format("IN04 %s %s", thisName(), res));
					
					// Parse the class
					else
						_classfile = rv = new CFClassParser().parse(is);
				}
				
				// {@squirreljme.error IN05 Cannot get class representation
				// because the class file is malformed. (The failing class)}
				catch (CFFormatException|IOException e)
				{
					throw new JVMClassFormatError(String.format("IN05 %s",
						thisName()), e);
				}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns the initialized class object which is used by the internal
	 * virtual machine to represent classes of this type.
	 *
	 * @param __th The thread which may be used if this class required
	 * initialization, if {@code null} then the default thread is used.
	 * @return The initializes class object.
	 * @since 2016/04/07
	 */
	public final JVMObject classObject(JVMThread __th)
	{
		// If no thread was specified, use the default thread
		if (__th == null)
			__th = engine.threads().defaultThread();
		
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<JVMObject> ref = _classobject;
			JVMObject rv;
			
			// Class needs to be initialized in the VM?
			if (ref == null || null == (rv = ref.get()))
			{
				// If this is the object for the Class class then it is
				// special and preinitialized before-hand
				if (thisName().equals(CLASS_NAME))
					rv = engine().objects().classObject();
				
				// Otherwise, it is a normal object instance which does not
				// require a work around.
				else
				{
					// Obtain and initialize any superclasses and interfaces
					JVMClass scl = superClass();
					if (scl != null)
						scl.classObject(__th);
				
					// Also initialize interfaces
					for (JVMClass ji : interfaceClasses())
						ji.classObject(__th);
					
					// Get the class for the class type
					JVMClass classtype = engine().classes().
						loadClass(CLASS_NAME);
				
					// Spawn an object of that class
					rv = engine().objects().spawnObject(__th, classtype);
				}
				
				// Cache it
				_classobject = new WeakReference<>(rv);
				
				// Debug
				System.err.printf("DEBUG -- Initialize class %s%n", this);
				
				// Call the static class constructor, if one exists
				JVMMethod clinit = methods().get(CLINIT_KEY);
				if (clinit != null)
				{
					// {@squirreljme.error IN0f The static class initializer
					// for the given class is not static. (The name of this
					// class)}
					if (!clinit.flags().isStatic())
						throw new JVMClassFormatError(String.format("IN0f %s",
							this));
					
					// Interpret to initialize it
					clinit.interpret(true, __th);
				}
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns the owning engine.
	 *
	 * @return The engine owner.
	 * @since 2016/04/05
	 */
	public JVMEngine engine()
	{
		return engine;
	}
	
	/**
	 * Returns the list of fields that this class contains.
	 *
	 * @return The fields contained in this class.
	 * @since 2016/04/04
	 */
	public JVMFields fields()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the flags which are associated with the current class.
	 *
	 * @return The class flags.
	 * @since 2016/04/02
	 */
	public CFClassFlags flags()
	{
		// If an array, the flags are fixed to a specific variety.
		if (thisname.isArray())
			return ARRAY_FLAGS;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the interfaces that this class implements.
	 *
	 * @return The class interfaces.
	 * @since 2016/03/07
	 */
	public Set<JVMClass> interfaceClasses()
	{
		// Lock
		synchronized (lock)
		{
			// Get interface list
			Set<JVMClass> rv = _interfaceclasses;
			
			// Needs loading?
			if (rv == null)
			{
				// Get the class path
				JVMClassPath cpath = engine().classes();
				
				// Setup
				Set<JVMClass> is = new LinkedHashSet<>();
				
				// Arrays are just cloneable
				if (isArray())
					is.add(cpath.loadClass(CLONEABLE_NAME));
				
				// Otherwise add all of them
				else
					for (BinaryNameSymbol bn : base().interfaces())
						is.add(cpath.loadClass(bn.asClassName()));
				
				// Lock in
				rv = MissingCollections.<JVMClass>unmodifiableSet(is);
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Is this an array?
	 *
	 * @return Returns {@code true} if this is an array.
	 * @since 2016/04/05
	 */
	public boolean isArray()
	{
		return thisname.isArray();
	}
	
	/**
	 * Returns the list of methods that this class contains.
	 *
	 * @return The methods contained in this class.
	 * @since 2016/04/04
	 */
	public JVMMethods methods()
	{
		// Lock
		synchronized (lock)
		{
			// Already loaded?
			JVMMethods rv = _methods;
			
			// Needs loading?
			if (rv == null)
				_methods = (rv = (thisname.isArray() ?
					new JVMMethods(this, true) : new JVMMethods(this)));
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns the super class of this class.
	 *
	 * @return The super class or {@code null} if there is none.
	 * @since 2016/04/07
	 */
	public JVMClass superClass()
	{
		// Lock
		synchronized (lock)
		{
			// No super class?
			if (_nosuperclass)
				return null;
			
			// Get it
			JVMClass rv = _superclass;
			
			// Needs obtaining?
			if (rv == null)
			{
				// If this is Object then there will be no super class
				ClassNameSymbol sn = superName();
				if (sn == null)
				{
					_nosuperclass = true;
					return null;
				}
				
				// There is one
				else
					_superclass = (rv = engine().classes().loadClass(sn));
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns the name of the super class.
	 *
	 * @return The super class name or {@code null} if there is none.
	 * @since 2016/04/07
	 */
	public ClassNameSymbol superName()
	{
		// Arrays always just have Object as their superclass
		if (isArray())
			return ClassNameSymbol.BINARY_OBJECT;
		
		// Otherwise derive from the base
		BinaryNameSymbol bn = base().superName();
		if (bn == null)
			return null;
		return bn.asClassName();
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @since 2016/04/04
	 */
	public ClassNameSymbol thisName()
	{
		return thisname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/06
	 */
	@Override
	public String toString()
	{
		return thisname.toString();
	}
}

