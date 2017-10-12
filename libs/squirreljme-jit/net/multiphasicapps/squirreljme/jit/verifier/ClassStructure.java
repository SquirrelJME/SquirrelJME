// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.verifier;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.squirreljme.jit.cff.AccessibleFlags;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.cff.Method;
import net.multiphasicapps.squirreljme.jit.cff.MethodFlags;
import net.multiphasicapps.squirreljme.jit.cff.MethodName;
import net.multiphasicapps.squirreljme.jit.cff.MethodNameAndType;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

/**
 * This contains the structure information for a single class. The class
 * contains a structure of instance and static members. This information is
 * used to determine how large a class is and which methods should be
 * invoked when required.
 *
 * @since 2017/10/09
 */
public final class ClassStructure
{
	/** The methods which are available in this class. */
	private final Map<MethodNameAndType, Method> _methods;
	
	/** Read only view of methods. */
	private volatile Reference<Map<MethodNameAndType, Method>> _romethods;
	
	/**
	 * Initializes the individual class structure.
	 *
	 * @param __csr The owning class reference.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the class fails to verify.
	 * @since 2017/10/10
	 */
	ClassStructure(Reference<ClassStructures> __csr, FamilyTree __tree,
		ClassName __cn)
		throws NullPointerException, VerificationException
	{
		if (__csr == null || __tree == null || __cn == null)
			throw new NullPointerException("NARG");
		
		// Need to node to determine which methods are replacable or not
		ClassStructures structs = __csr.get();
		FamilyNode node = __tree.get(__cn);
		
		// Methods to exist within this class
		Map<MethodNameAndType, Method> methods = new SortedTreeMap<>();
		
		// Inherit all methods from super classes first
		ClassName supername = node.superName();
		if (supername != null)
		{
			boolean samepk = __cn.isInSamePackage(supername);
			
			// Initially add all methods which exist in the super class that
			// are not private, package private (from another package)
			// Static methods are always inherited, but if a static method is
			// final then it gets hidden
			ClassStructure superstruct = structs.get(supername);
			for (Map.Entry<MethodNameAndType, Method> e :
				superstruct.methods().entrySet())
			{
				MethodNameAndType nat = e.getKey();
				Method m = e.getValue();
				MethodFlags mflags = m.flags();
				MethodName name = m.name();
				
				// Never copy private methods or constructors
				if (mflags.isPrivate() || name.isAnyInitializer() ||
					(!samepk && mflags.isPackagePrivate()))
					continue;
				
				// Use those methods
				methods.put(nat, m);
			}
		}
		
		// Go through the classes for this method and see if they can override
		// or otherwise replace methods.
		// Final methods cannot be replaced
		// change of static cannot be performed (a static cannot override
		// an instance, and an instance cannot override a static)
		for (Method m : node.methods())
		{
			MethodFlags mflags = m.flags();
			MethodNameAndType nat = m.nameAndType();
			
			// A pre-existing method exists
			Method pre = methods.get(nat);
			if (pre != null)
			{
				MethodFlags pf = pre.flags();
				
				// {@squirreljme.error JI3i The specified method in the given
				// class overrides a method which cannot override the given
				// method. (The name of the current class; The name of the
				// method; The flags for the overriding method; The flags
				// for the method to be overridden}
				if (pf.isFinal() || (mflags.isStatic() != pf.isStatic()) ||
					__accessOrder(mflags) < __accessOrder(pf))
					throw new VerificationException(String.format(
						"JI3i %s %s %s %s", __cn, nat, mflags, pf));
			}
			
			// Override method
			methods.put(nat, m);
		}
		
		// Go through every interface which is implemented and copy all methods
		// which are available provided they are not already defined.
		// Support functionality for default methods that exist in Java 8 just
		// so it is there for if this kind of feature set becomes available
		// Note that interfaces do not need to be recursively handled because
		// all methods in the interface are given
		for (ClassName iname : node.interfaceNames())
		{
			ClassStructure istruct = structs.get(supername);
			for (Map.Entry<MethodNameAndType, Method> e :
				istruct.methods().entrySet())
			{
				MethodNameAndType nat = e.getKey();
				Method m = e.getValue();
				MethodFlags mflags = m.flags();
				MethodName name = m.name();
				
				// Only replace pre-existing methods if they are abstract and
				// the interface method is not abstract
				Method pre = methods.get(nat);
				if (pre != null)
				{
					MethodFlags pf = pre.flags();
					if (pf.isAbstract() && !mflags.isAbstract())
						methods.put(nat, m);
				}
				
				// Does not exist, use it regardless
				else
					methods.put(nat, m);
			}
		}
		
		// If the class is not abstract then go through all methods and make
		// sure that there are no abstract methods which exist in the class
		if (!node.flags().isAbstract())
			for (Method m : methods.values())
				if (m.flags().isAbstract())
					throw new VerificationException(String.format("JI3j %s %s",
						__cn, m.nameAndType()));
		
		// All methods are good now
		this._methods = methods;
		
		// Build the table of static and instance fields
		// Static fields are held differently from instance fields and count
		// twords static storage
		// Instance fields become part of the object
		throw new todo.TODO();
	}
	
	/**
	 * Returns the mapping of methods which exist in the structure.
	 *
	 * @return The mapping of all available methods.
	 * @since 2017/10/11
	 */
	public final Map<MethodNameAndType, Method> methods()
	{
		Reference<Map<MethodNameAndType, Method>> ref = this._romethods;
		Map<MethodNameAndType, Method> rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._romethods = new WeakReference<>(
				(rv = UnmodifiableMap.<MethodNameAndType, Method>of(
					this._methods)));
		
		return rv;
	}
	
	/**
	 * Returns the access order of the given flags.
	 *
	 * @param __f The flags to check.
	 * @return The access order.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/12
	 */
	private static final int __accessOrder(AccessibleFlags __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		if (__f.isPrivate())
			return 0;
		else if (__f.isPackagePrivate())
			return 1;
		else if (__f.isProtected())
			return 2;
		else
			return 3;
	}
}

