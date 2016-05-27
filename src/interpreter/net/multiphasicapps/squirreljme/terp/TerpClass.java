// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIClassFlags;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodFlags;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This represents a class which is loaded by the interpreter.
 *
 * @since 2016/04/21
 */
public class TerpClass
{
	/** The static initializer method key. */
	public static final CIMethodID STATIC_ITerpTIALIZER =
		new CIMethodID(IdentifierSymbol.of("<clinit>"),
		MethodSymbol.of("()V"));
	
	/** No argument array. */
	private static final Object[] _NO_ARGS =
		new Object[0];
	
	/** The interpreter core. */
	protected final TerpCore core;
	
	/** The based class (if {@code null} is a virtual class). */
	protected final CIClass base;
	
	/** Is this class fully loaded? */
	protected final boolean loaded;
	
	/** The name of this class. */
	protected final ClassNameSymbol thisname;
	
	/** The super class of this class. */
	protected final TerpClass superclass;
	
	/** The interfaces this class implements. */
	protected final Set<TerpClass> interfaceclasses;
	
	/** The mapped methods for this class. */
	protected final Map<CIMethodID, TerpMethod> methods;
	
	/**
	 * Initializes an interpreted class.
	 *
	 * @param __core The core.
	 * @param __base The class to base off.
	 * @param __cns The name of the class.
	 * @param __tm The map to place a partially loaded class within.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	TerpClass(TerpCore __core, CIClass __base,
		ClassNameSymbol __cns,
		Map<ClassNameSymbol, Reference<TerpClass>> __tm)
		throws NullPointerException
	{
		// Check
		if (__core == null || __base == null || __tm == null || __cns == null)
			throw new NullPointerException("NARG");
		
		// Set
		core = __core;
		base = __base;
		
		// {@squirreljme.error AN0b The class which was read differs by name
		// with the class that is to be loaded. (The loaded class name; The
		// requested class)}
		thisname = __base.thisName();
		if (!__cns.equals(thisname))
			throw new TerpException(core, String.format("AN0b %s %s", thisname,
				__cns));
		
		// DEBUG
		System.err.printf("DEBUG -- Init class %s%n", thisname);
		
		// Place into the given map, it would be partially loaded at this time
		__tm.put(__cns, new WeakReference<>(this));
		
		// Obtain the superclass of this class
		ClassNameSymbol sn = __base.superName();
		superclass = (sn == null ? null : __core.initClass(sn));
		
		// Obtain class interfaces
		Set<TerpClass> in = new LinkedHashSet<>();
		for (ClassNameSymbol cns : __base.interfaceNames())
			in.add(__core.initClass(cns));
		interfaceclasses = UnmodifiableSet.<TerpClass>of(in);
		
		// Make sure the class does not eventually extend or implement itself
		// If it does then the class definition is circular
		for (TerpClass rover = superclass; rover != null;
			rover = rover.superclass)
		{
			// {@squirreljme.error AN0v The current class eventually extends
			// a final class. (This class; The final class)}
			if (rover.flags().isFinal())
				throw new TerpException(core, String.format("AN0v %s %s",
					thisname, rover));
			
			// {@squirreljme.error AN0c The current class eventually extends
			// itself. (The name of this class)}
			if (rover == this)
				throw new TerpException(core, String.format("AN0c %s",
					thisname));
			
			// {@squirreljme.error AN0d The current class eventually implements
			// itself. (The name of this name; The class implementing this)}
			for (TerpClass impl : rover.interfaceclasses)
				if (impl == this)
					throw new TerpException(core, String.format("AN0d %s %s",
						thisname, rover.thisname));
		}
		
		// Create methods for all of the current class methods
		Map<CIMethodID, TerpMethod> mm = new HashMap<>();
		for (Map.Entry<CIMethodID, CIMethod> e : base.methods().entrySet())
			mm.put(e.getKey(), new TerpMethod(this, e.getValue()));
		
		// Bind all superclass methods which are not static, are initializers,
		// or are constructors to the current method if they are not set (this
		// is for faster virtual handling)
		boolean selfabs = flags().isAbstract();
		for (TerpClass rover = superclass; rover != null;
			rover = rover.superclass)
			for (Map.Entry<CIMethodID, TerpMethod> e : rover.methods.
				entrySet())
			{
				// Get key and value
				CIMethodID k = e.getKey();
				TerpMethod v = e.getValue();
				
				// Get flags
				CIMethodFlags mf = v.flags();
				
				// Ignore static, initializers, and private methods
				if (mf.isStatic() || k.name().isConstructor() ||
					mf.isPrivate())
					continue;
				
				// If abstract, it must be implemented (it must be contained
				// in the map and not be abstract)
				TerpMethod iv = mm.get(k);
				if (!selfabs && mf.isAbstract())
				{
					// {@squirreljme.error AN0e The top-level class is not
					// abstract and it does not implement an abstract method.
					// (The method identifier)}
					if (iv == null || iv.flags().isAbstract())
						throw new TerpException(core, String.format("AN0e %s",
							k));
				}
				
				// Never replace methods in a sub-class with the superclass
				// methods.
				if (iv != null && iv != v)
				{
					// {@squirreljme.error AN0u A final method cannot be
					// replaced. (The method identifier; The super class; The
					// current class)}
					if (v.flags().isFinal())
						throw new TerpException(core, String.format(
							"AN0u %s %s %s", k, rover, this));
					
					// Do not add
					continue;
				}
				
				// Bind it
				mm.put(k, v);
			}
		
		// Lock in
		methods = UnmodifiableMap.<CIMethodID, TerpMethod>of(mm);
		
		// Check that interface methods are implemented
		if (!selfabs)
			for (TerpClass rover = this; rover != null; rover = rover.superclass)
				for (TerpClass interf : rover.interfaceclasses)
					for (Map.Entry<CIMethodID, TerpMethod> e : interf.methods.
						entrySet())
					{
						// Get target method
						TerpMethod v = e.getValue();
					
						// Only check abstract methods
						if (!v.flags().isAbstract())
							continue;
					
						// {@squirreljme.error AN0f The current class does not
						// implement an abstract interface method. (The
						// unimplemented method; The current class name;
						// The interface name)}
						CIMethodID k = e.getKey();
						if (!methods.containsKey(k))
							throw new TerpException(core,
								String.format("AN0f %s %s", k, base.thisName(),
									interf.base.thisName()));
						
					}
		
		// Obtain the static initializer
		TerpMethod sinit = methods.get(STATIC_ITerpTIALIZER);
		if (sinit != null && sinit.flags().isStatic())
		{
			// Get the current thread so that execution may be performed on it
			TerpThread tt = core.thread(Thread.currentThread());
			
			// Debug
			System.err.printf("DEBUG -- Static init %s.%n", base.thisName());
			
			// Invoke the method
			tt.invoke(sinit, _NO_ARGS);
		}
		
		// Class loaded
		loaded = true;
	}
	
	/**
	 * Returns the core of the interpreter.
	 *
	 * @return The interpreter core.
	 * @since 2016/04/27
	 */
	public TerpCore core()
	{
		return core;
	}
	
	/**
	 * Returns the flags for this class.
	 *
	 * @return The class flags.
	 * @since 2016/04/26
	 */
	public CIClassFlags flags()
	{
		return base.flags();
	}
	
	/**
	 * Is this an array?
	 *
	 * @return {@code true} if this is an array.
	 * @since 2016/04/27
	 */
	public boolean isArray()
	{
		return thisName().isArray();
	}
		
	/**
	 * Is this class fully loaded?
	 *
	 * @return {@code true} if it is loaded.
	 * @since 2016/04/22
	 */
	public boolean isLoaded()
	{
		return loaded;
	}
	
	/**
	 * Is this a primitive type?
	 *
	 * @return {@code true} if this is a primitive type.
	 * @since 2016/04/27
	 */
	public boolean isPrimitive()
	{
		return thisName().isPrimitive();
	}
	
	/**
	 * Returns the methods of this class.
	 *
	 * @return The mapping of methods for this class.
	 * @since 2016/04/26
	 */
	public Map<CIMethodID, TerpMethod> methods()
	{
		return methods;
	}
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The current class name.
	 * @since 2016/04/27
	 */
	public ClassNameSymbol thisName()
	{
		return base.thisName();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public String toString()
	{
		return thisName().toString();
	}
}

