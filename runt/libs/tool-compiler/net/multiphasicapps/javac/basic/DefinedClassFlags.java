// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import net.multiphasicapps.classfile.AccessibleFlags;
import net.multiphasicapps.classfile.ClassFlag;
import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.collections.EnumSet;
import net.multiphasicapps.collections.UnmodifiableSet;

/**
 * This class represents a set of defined class flags.
 *
 * @since 2018/03/10
 */
public final class DefinedClassFlags
	extends AbstractSet<DefinedClassFlag>
	implements AccessibleFlags
{
	/** Flags which have been defined. */
	private final Set<DefinedClassFlag> _flags;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Defines the set of flags to use for a given class.
	 *
	 * @param __f The flags to use for the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/10
	 */
	public DefinedClassFlags(DefinedClassFlag... __f)
		throws NullPointerException
	{
		this(Arrays.<DefinedClassFlag>asList(__f));
	}
	
	/**
	 * Defines the set of flags to use for a given class.
	 *
	 * @param __f The flags to use for the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/10
	 */
	public DefinedClassFlags(Iterable<DefinedClassFlag> __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Setup flags
		Set<DefinedClassFlag> flags = new EnumSet<>(DefinedClassFlag.class);
		for (DefinedClassFlag f : __f)
			flags.add(f);
		this._flags = (flags = UnmodifiableSet.<DefinedClassFlag>of(flags));
		
		// {@squirreljme.error AQ1z A class cannot have multiple modifiers
		// associated with it. (The modifiers)}
		int modcount = 0;
		if (this.isPublic())
			modcount++;
		if (this.isProtected())
			modcount++;
		if (this.isPrivate())
			modcount++;
		if (modcount > 1)
			throw new BasicStructureException(String.format("AQ1z %s", flags));
		
		// Is this an inner class?
		if (this.isInnerClass())
		{
			// {@squirreljme.error AQ1y A non-static inner class cannot be
			// an annotation, enumeration, or an interface. (The class flags)}
			if (!this.isStatic() && (this.isInterface() || this.isEnum() ||
				this.isAnnotation()))
				throw new BasicStructureException(String.format("AQ1y %s",
					flags));
		}
		
		// Not one
		else
		{
			// {@squirreljme.error AQ20 An outer-class cannot be static.}
			if (this.isStatic())
				throw new BasicStructureException(String.format("AQ20 %s",
					flags));
			
			// {@squirreljme.error AQ1v An outer-class cannot be protected or
			// private. (The class flags)}
			if (this.isProtected() || this.isPrivate())
				throw new BasicStructureException(String.format("AQ1v %s",
					flags));
		}
		
		// {@squirreljme.error AQ1w A class cannot be both final and abstract.
		// (The class flags)}
		if (this.isFinal() && this.isAbstract())
			throw new BasicStructureException(String.format("AQ1w %s", flags));
			
		// {@squirreljme.error AQ1wx An enumeration cannot declared final
		// or declared abstract, these are implicitly set by the compiler and
		// must not be in source code. (The class flags)}
		if ((this.isAbstract() || this.isFinal()) && this.isEnum())
			throw new BasicStructureException(String.format("AQ1x %s", flags));
		
		// {@squirreljme.error AQ21 An annotation must also be an interface.
		// (The class flags)}
		if (this.isAnnotation() && !this.isInterface())
			throw new BasicStructureException(String.format("AQ21 %s", flags));
	}
	
	/**
	 * Is this class abstract?
	 *
	 * @return If the class is abstract.
	 * @since 2018/03/21
	 */
	public final boolean isAbstract()
	{
		return this._flags.contains(DefinedClassFlag.ABSTRACT);
	}

	/**
	 * Is this class an annotation?
	 *
	 * @return If this class is an annotation.
	 * @since 2018/03/21
	 */
	public final boolean isAnnotation()
	{
		return this._flags.contains(DefinedClassFlag.ANNOTATION);
	}

	/**
	 * Is this class an enumeration?
	 *
	 * @return If this class is an enumeration.
	 * @since 2018/03/21
	 */
	public final boolean isEnum()
	{
		return this._flags.contains(DefinedClassFlag.ENUM);
	}

	/**
	 * Is this class final?
	 *
	 * @return If this class is final.
	 * @since 2018/03/21
	 */
	public final boolean isFinal()
	{
		return this._flags.contains(DefinedClassFlag.FINAL);
	}

	/**
	 * Is this class an inner class?
	 *
	 * @return If this is an inner class.
	 * @since 2018/03/21
	 */
	public final boolean isInnerClass()
	{
		return this._flags.contains(DefinedClassFlag.INNER_CLASS);
	}

	/**
	 * Is this class an interface?
	 *
	 * @return If the class is an interface.
	 * @since 2018/03/21
	 */
	public final boolean isInterface()
	{
		return this._flags.contains(DefinedClassFlag.INTERFACE);
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final boolean isPackagePrivate()
	{
		return !(this.isPublic() || this.isProtected() || this.isPrivate());
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final boolean isPrivate()
	{
		return this._flags.contains(DefinedClassFlag.PRIVATE);
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final boolean isProtected()
	{
		return this._flags.contains(DefinedClassFlag.PROTECTED);
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final boolean isPublic()
	{
		return this._flags.contains(DefinedClassFlag.PUBLIC);
	}

	/**
	 * Is the class static?
	 *
	 * @return If the class is static.
	 * @since 2018/03/21
	 */
	public final boolean isStatic()
	{
		return this._flags.contains(DefinedClassFlag.STATIC);
	}

	/**
	 * Does the entire class use struct floating point?
	 *
	 * @return If strict floating point is used.
	 * @since 2018/03/21
	 */
	public final boolean isStrictFloatingPoint()
	{
		return this._flags.contains(DefinedClassFlag.STRICTFP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final Iterator<DefinedClassFlag> iterator()
	{
		return this._flags.iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final int size()
	{
		return this._flags.size();
	}
	
	/**
	 * Returns the class flags which would be used if this were compiled.
	 *
	 * @return The flags to use for compilation.
	 * @since 2018/03/10
	 */
	public final ClassFlags toClassFlags()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this._flags.toString()));
		
		return rv;
	}
}

