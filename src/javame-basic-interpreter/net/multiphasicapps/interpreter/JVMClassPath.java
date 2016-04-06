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
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.CFClass;
import net.multiphasicapps.classfile.CFClassParser;
import net.multiphasicapps.classfile.CFFormatException;
import net.multiphasicapps.classfile.CFMethod;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This contains the class path which manages a set of classes which are
 * available in an engine.
 *
 * @see JVMClassPathElement
 * @since 2016/04/06
 */
public final class JVMClassPath
{
	/** The owning engine. */
	protected final JVMEngine engine;
	
	/** The maximum number of dimensions an array may have. */
	public static final int MAX_ARRAY_DIMENSIONS =
		255;
	
	/** Active loaded classes (lock on this). */
	protected final Map<ClassNameSymbol, Reference<JVMClass>> classes =
		new HashMap<>();
	
	/** The class reference queue. */
	protected final ReferenceQueue<JVMClass> classqueue =
		new ReferenceQueue<JVMClass>();
	
	/** Classpaths for the interpreter (how it finds classes). */
	protected final Set<JVMClassPathElement> classpaths =
		new LinkedHashSet<>();
	
	/**
	 * Initializes the class path.
	 *
	 * @param __eng The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/06
	 */
	JVMClassPath(JVMEngine __eng)
		throws NullPointerException
	{
		// Check
		if (__eng == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __eng;
	}
	
	/**
	 * Adds a class path to the engine.
	 *
	 * @param __icp The class path to add.
	 * @return {@code this}.
	 * @throws IllegalArgumentExcepton If another engine owns this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/05
	 */
	public JVMClassPath addClassPath(JVMClassPathElement __icp)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__icp == null)
			throw new NullPointerException("NARG");
		
		// Lock on the class paths
		synchronized (classpaths)
		{
			// Add it
			classpaths.add(__icp);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the engine which owns this.
	 *
	 * @return The owning engine.
	 * @since 2016/04/06
	 */
	public JVMEngine engine()
	{
		return engine;
	}
	
	/**
	 * Attempts to load a class with the given binary name, the input class
	 * may also be an array for which one will be generated.
	 *
	 * @param __bn The name of the class to load.
	 * @return The loaded class or {@code null} if no such class exists.
	 * @throws IllegalArgumentException If the given name is not a valid class
	 * name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public JVMClass loadClass(ClassNameSymbol __bn)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Lock on the class map
		synchronized (classes)
		{
			// See if an existing reference exists
			Reference<JVMClass> ref = classes.get(__bn);
			JVMClass rv;
			
			// Reference is still valid?
			if (ref == null || null == (rv = ref.get()))
				classes.put(__bn, new WeakReference<>((rv =
					new JVMClass(this, __bn)), classqueue));
			
			// Return the read class
			return rv;
		}
	}
}

