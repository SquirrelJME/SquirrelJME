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
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.narf.classinterface.NCICodeAttribute;
import net.multiphasicapps.narf.classinterface.NCIException;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIMethodFlag;
import net.multiphasicapps.narf.classinterface.NCIMethodFlags;
import net.multiphasicapps.narf.classinterface.NCIMethodID;

/**
 * This represents a method which is contained within a class file.
 *
 * @since 2016/04/26
 */
public final class NCFMethod
	extends NCFMember<NCIMethodID, NCIMethodFlags>
	implements NCIMethod
{
	/** Raw code attribute. */
	protected final byte[] code;
	
	/** Cached code attribute data. */
	private volatile Reference<NCICodeAttribute> _ca;
	
	/**
	 * Initializes the class method.
	 *
	 * @param __oc The outer class.
	 * @param __id The identifier of the method.
	 * @param __fl The method flags.
	 * @param __ca The code attribute of this method.
	 * @throws NCIException If a native/abstract has code or there is no code
	 * and those flags are not set.
	 * @since 2016/04/26
	 */
	NCFMethod(NCFClass __oc, NCIMethodID __id, NCIMethodFlags __fl,
		byte[] __ca)
		throws NCIException
	{
		super(__oc, __id, __fl);
		
		// {@squirreljme.error AQ1x The specified method is either native or
		// abstract and has a code attribute or is not native or abstract and
		// does not have a code attribute. (The method ID; The flags)}
		NCIMethodFlags f = flags();
		boolean abs;
		if ((abs = (f.isNative() || f.isAbstract())) != (__ca == null))
			throw new NCIException((abs ? NCIException.Issue.ABSTRACT_CODE :
				NCIException.Issue.MISSING_CODE),
				String.format("AQ1x %s %s", __id, f));
		
		// Set
		code = __ca;
		
		// Get the identifier to check flags
		IdentifierSymbol name = __id.name();
		boolean in = name.isConstructor();
		boolean cl = name.isStaticInitializer();
		
		// {@squirreljme.error AQ20 Instance and static initializers have
		// additional requirements as to which flags may be set. (The method
		// ID; The flags)}
		if ((in && (f.contains(NCIMethodFlag.STATIC) ||
			f.contains(NCIMethodFlag.FINAL) ||
			f.contains(NCIMethodFlag.SYNCHRONIZED) ||
			f.contains(NCIMethodFlag.BRIDGE) ||
			f.contains(NCIMethodFlag.NATIVE) ||
			f.contains(NCIMethodFlag.ABSTRACT))) ||
			(cl && (!f.contains(NCIMethodFlag.STATIC))))
			throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
				String.format("AQ20 %s %s", __id, f));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/27
	 */
	@Override
	public NCICodeAttribute code()
	{
		// Obtain the attribute
		Reference<NCICodeAttribute> ref = _ca;
		NCICodeAttribute rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
			_ca = new WeakReference<>((rv = new NCICodeAttribute(this, code)));
		
		// Return it
		return rv;
	}
}

