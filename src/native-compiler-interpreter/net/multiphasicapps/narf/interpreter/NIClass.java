// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIClassFlags;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIMethodFlags;
import net.multiphasicapps.narf.classinterface.NCIMethodID;

/**
 * This represents a class which is loaded by the interpreter.
 *
 * @since 2016/04/21
 */
public class NIClass
{
	/** The static initializer method key. */
	public static final NCIMethodID STATIC_INITIALIZER =
		new NCIMethodID(new IdentifierSymbol("<clinit>"),
		new MethodSymbol("()V"));
	
	/** The interpreter core. */
	protected final NICore core;
	
	/** The based class (if {@code null} is a virtual class). */
	protected final NCIClass base;
	
	/** Is this class fully loaded? */
	protected final boolean loaded;
	
	/** The name of this class. */
	protected final ClassNameSymbol thisname;
	
	/** The super class of this class. */
	protected final NIClass superclass;
	
	/** The interfaces this class implements. */
	protected final Set<NIClass> interfaceclasses;
	
	/** The mapped methods for this class. */
	protected final Map<NCIMethodID, NIMethod> methods;
	
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
	NIClass(NICore __core, NCIClass __base,
		ClassNameSymbol __cns,
		Map<ClassNameSymbol, Reference<NIClass>> __tm)
		throws NullPointerException
	{
		// Check
		if (__core == null || __base == null || __tm == null || __cns == null)
			throw new NullPointerException("NARG");
		
		// Set
		core = __core;
		base = __base;
		
		// {@squirreljme.error NI0b The class which was read differs by name
		// with the class that is to be loaded. (The loaded class name; The
		// requested class)}
		thisname = __base.thisName();
		if (!__cns.equals(thisname))
			throw new NIException(core, NIException.Issue.CLASS_NAME_MISMATCH,
				String.format("NI0b %s %s", thisname, __cns));
		
		// DEBUG
		System.err.printf("DEBUG -- Init class %s%n", thisname);
		
		// Place into the given map, it would be partially loaded at this time
		__tm.put(__cns, new WeakReference<>(this));
		
		// Obtain the superclass of this class
		ClassNameSymbol sn = __base.superName();
		superclass = (sn == null ? null : __core.initClass(sn));
		
		// Obtain class interfaces
		Set<NIClass> in = new LinkedHashSet<>();
		for (ClassNameSymbol cns : __base.interfaceNames())
			in.add(__core.initClass(cns));
		interfaceclasses = MissingCollections.<NIClass>unmodifiableSet(in);
		
		// Make sure the class does not eventually extend or implement itself
		// If it does then the class definition is circular
		for (NIClass rover = superclass; rover != null;
			rover = rover.superclass)
		{
			// {@squirreljme.error NI0c The current class eventually extends
			// itself. (The name of this class)}
			if (rover == this)
				throw new NIException(core,
					NIException.Issue.CLASS_CIRCULARITY,
					String.format("NI0c %s", thisname));
			
			// {@squirreljme.error NI0d The current class eventually implements
			// itself. (The name of this name; The class implementing this)}
			for (NIClass impl : rover.interfaceclasses)
				if (impl == this)
					throw new NIException(core,
						NIException.Issue.CLASS_CIRCULARITY,
						String.format("NI0d %s %s", thisname, rover.thisname));
		}
		
		// Create methods for all of the current class methods
		Map<NCIMethodID, NIMethod> mm = new HashMap<>();
		for (Map.Entry<NCIMethodID, NCIMethod> e : base.methods().entrySet())
			mm.put(e.getKey(), new NIMethod(this, e.getValue()));
		
		// Bind all superclass methods which are not static, are initializers,
		// or are constructors to the current method if they are not set (this
		// is for faster virtual handling)
		boolean selfabs = flags().isAbstract();
		for (NIClass rover = superclass; rover != null;
			rover = rover.superclass)
			for (Map.Entry<NCIMethodID, NIMethod> e : rover.methods.
				entrySet())
			{
				// Get key and value
				NCIMethodID k = e.getKey();
				NIMethod v = e.getValue();
				
				// Get flags
				NCIMethodFlags mf = v.flags();
				
				// Ignore static, initializers, and private methods
				if (mf.isStatic() || k.name().isConstructor() ||
					mf.isPrivate())
					continue;
				
				// If abstract, it must be implemented (it must be contained
				// in the map and not be abstract)
				if (!selfabs && mf.isAbstract())
				{
					// Get the method
					NIMethod iv = mm.get(k);
					
					// {@squirreljme.error NI0e The top-level class is not
					// abstract and it does not implement an abstract method.
					// (The method identifier)}
					if (iv == null || iv.flags().isAbstract())
						throw new NIException(core,
							NIException.Issue.ABSTRACT_NOT_IMPLEMENTED,
							String.format("NI0e %s", k));
				}
				
				// Never replace methods in a sub-class with the superclass
				// methods.
				if (mm.containsKey(k))
					continue;
				
				// Bind it
				mm.put(k, v);
			}
		
		// Lock in
		methods = MissingCollections.<NCIMethodID, NIMethod>unmodifiableMap(
			mm);
		
		// Check that interface methods are implemented
		if (!selfabs)
			for (NIClass rover = this; rover != null; rover = rover.superclass)
				for (NIClass interf : rover.interfaceclasses)
					for (Map.Entry<NCIMethodID, NIMethod> e : interf.methods.
						entrySet())
					{
						// Get target method
						NIMethod v = e.getValue();
					
						// Only check abstract methods
						if (!v.flags().isAbstract())
							continue;
					
						// {@squirreljme.error NI0f The current class does not
						// implement an abstract interface method. (The
						// unimplemented method; The current class name;
						// The interface name)}
						NCIMethodID k = e.getKey();
						if (!methods.containsKey(k))
							throw new NIException(core,
								NIException.Issue.ABSTRACT_NOT_IMPLEMENTED,
								String.format("NI0f %s %s", k, base.thisName(),
									interf.base.thisName()));
						
					}
		
		// Obtain the static initializer
		NIMethod sinit = methods.get(STATIC_INITIALIZER);
		if (sinit != null && sinit.flags().isStatic())
		{
			throw new Error("TODO");
		}
		
		// Class loaded
		loaded = true;
	}
	
	/**
	 * Returns the flags for this class.
	 *
	 * @return The class flags.
	 * @since 2016/04/26
	 */
	public NCIClassFlags flags()
	{
		return base.flags();
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
	 * Returns the methods of this class.
	 *
	 * @return The mapping of methods for this class.
	 * @since 2016/04/26
	 */
	public Map<NCIMethodID, NIMethod> methods()
	{
		return methods;
	}
}

