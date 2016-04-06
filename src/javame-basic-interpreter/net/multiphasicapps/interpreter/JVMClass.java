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
import java.util.Map;
import net.multiphasicapps.classfile.CFClass;
import net.multiphasicapps.classfile.CFClassFlag;
import net.multiphasicapps.classfile.CFClassFlags;
import net.multiphasicapps.descriptors.ClassNameSymbol;

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
	
	/** The owning engine. */
	protected final JVMEngine engine;
	
	/** The class path which spawned this. */
	protected final JVMClassPath owningpath;
	
	/** The name of this class. */
	protected final ClassNameSymbol thisname;
	
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
		
		throw new Error("TODO");
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
		throw new Error("TODO");
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
}

