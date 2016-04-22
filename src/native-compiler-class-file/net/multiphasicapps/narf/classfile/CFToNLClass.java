// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.classfile.CFClass;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.narf.library.NLClass;

/**
 * This wraps a {@link CFClass} into a {@link CLClass} so that it may be used
 * by the native compiler.
 *
 * @since 2016/04/21
 */
public class CFToNLClass
	implements NLClass
{
	/** The class file to base off. */
	protected final CFClass classfile;
	
	/** The implemented interfaces. */
	private volatile Reference<Set<ClassNameSymbol>> _impls;
	
	/**
	 * Initializes the wrapped class.
	 *
	 * @param __cf The class file based class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	public CFToNLClass(CFClass __cf)
		throws NullPointerException
	{
		// Check
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		// Set
		classfile = __cf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/22
	 */
	@Override
	public Set<ClassNameSymbol> interfaceNames()
	{
		// Get reference
		Reference<Set<ClassNameSymbol>> ref = _impls;
		Set<ClassNameSymbol> rv;
		
		// Needs to be cached?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target set
			Set<ClassNameSymbol> in = new LinkedHashSet<>();
			
			// Fill in
			for (BinaryNameSymbol bns : classfile.interfaces())
				in.add(bns.asClassName());
			
			// Wrap
			_impls = new WeakReference<>((rv =
				MissingCollections.<ClassNameSymbol>unmodifiableSet(in)));
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/22
	 */
	@Override
	public ClassNameSymbol superName()
	{
		BinaryNameSymbol sn = classfile.superName();
		return (sn == null ? null : sn.asClassName());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/22
	 */
	@Override
	public ClassNameSymbol thisName()
	{
		return classfile.thisName().asClassName();
	}
}

