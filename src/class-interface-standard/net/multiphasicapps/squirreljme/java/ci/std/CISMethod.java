// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci.std;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.ci.CICodeAttribute;
import net.multiphasicapps.squirreljme.java.ci.CIException;
import net.multiphasicapps.squirreljme.java.ci.CIMethod;
import net.multiphasicapps.squirreljme.java.ci.CIMethodFlag;
import net.multiphasicapps.squirreljme.java.ci.CIMethodFlags;
import net.multiphasicapps.squirreljme.java.ci.CIMethodID;
import net.multiphasicapps.squirreljme.java.ci.CINativeCode;

/**
 * This represents a method which is contained within a class file.
 *
 * @since 2016/04/26
 */
public final class CISMethod
	extends CISMember<CIMethodID, CIMethodFlags>
	implements CIMethod
{
	/** Raw code attribute. */
	protected final byte[] code;
	
	/** Cached code attribute data. */
	private volatile Reference<CICodeAttribute> _ca;
	
	/**
	 * Initializes the class method.
	 *
	 * @param __oc The outer class.
	 * @param __id The identifier of the method.
	 * @param __fl The method flags.
	 * @param __ca The code attribute of this method.
	 * @throws CIException If a native/abstract has code or there is no code
	 * and those flags are not set.
	 * @since 2016/04/26
	 */
	CISMethod(CISClass __oc, CIMethodID __id, CIMethodFlags __fl,
		byte[] __ca)
		throws CIException
	{
		super(__oc, __id, __fl);
		
		// {@squirreljme.error AQ1x The specified method is either native or
		// abstract and has a code attribute or is not native or abstract and
		// does not have a code attribute. (The method ID; The flags; If the
		// method is abstract or not)}
		CIMethodFlags f = flags();
		boolean abs;
		if ((abs = (f.isNative() || f.isAbstract())) != (__ca == null))
			throw new CIException(String.format("AQ1x %s %s %d", __id, f,
				abs));
		
		// Set
		code = __ca;
		
		// Get the identifier to check flags
		IdentifierSymbol name = __id.name();
		boolean in = name.isConstructor();
		boolean cl = name.isStaticInitializer();
		
		// {@squirreljme.error AQ20 Instance and static initializers have
		// additional requirements as to which flags may be set. (The method
		// ID; The flags)}
		if ((in && (f.contains(CIMethodFlag.STATIC) ||
			f.contains(CIMethodFlag.FINAL) ||
			f.contains(CIMethodFlag.SYNCHRONIZED) ||
			f.contains(CIMethodFlag.BRIDGE) ||
			f.contains(CIMethodFlag.NATIVE) ||
			f.contains(CIMethodFlag.ABSTRACT))) ||
			(cl && (!f.contains(CIMethodFlag.STATIC))))
			throw new CIException(String.format("AQ20 %s %s", __id, f));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/27
	 */
	@Override
	public CICodeAttribute code()
	{
		// Obtain the attribute
		Reference<CICodeAttribute> ref = _ca;
		CICodeAttribute rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
			_ca = new WeakReference<>((rv = new CICodeAttribute(this, code)));
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public CINativeCode nativeCode()
	{
		// Class files do not have native code
		return null;
	}
}

