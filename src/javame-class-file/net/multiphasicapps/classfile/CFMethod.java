// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Set;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents an interpreted method within a class.
 *
 * @since 2016/03/01
 */
public final class CFMethod
	extends CFMember<MethodSymbol, CFMethodFlags>
{
	/** Is this a constructor? */
	protected final boolean isconstructor;	
	
	/** Is this a class initialization method? */
	protected final boolean isclassinit;
	
	/** Code attribute data. */
	protected final byte[] codeattribute;
	
	/** Code attribute viewer. */
	private volatile Reference<CFCodeAttribute> _cviewer;
	
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __nat The name and type of the field.
	 * @param __fl The flags which the member uses.
	 * @param __ca The code attribute, the direct array is used and should
	 * not be modified.
	 * @param __ver The class version number.
	 * @throws NullPointerException On null arguments except for {@code __ca}.
	 * @since 2016/03/01
	 */
	CFMethod(CFMemberKey<MethodSymbol> __nat, CFMethodFlags __fl, byte[] __ca,
		CFClassVersion __ver)
		throws NullPointerException
	{
		super(MethodSymbol.class, __nat, CFMethodFlags.class, __fl);
		
		// Check
		if (__ver == null)
			throw new NullPointerException("NARG");
		
		// Is this a constructor?
		IdentifierSymbol name = name();
		isconstructor = name.equals("<init>");
		isclassinit = name.equals("<clinit>");
		
		// Set
		codeattribute = __ca;
		
		// If not an initializer of any kind
		if (!isconstructor && !isclassinit)
		{
			// {@squirreljme.error CF0w An abstract method cannot be {@code
			// private}, {@code static}, {@code final}, {@code synchronized},
			// {@code native}, or {@code strict}. (The method flags)}
			if (__fl.isAbstract())
				if (__fl.isPrivate() || __fl.isStatic() || __fl.isFinal() ||
					__fl.isSynchronized() || __fl.isNative() ||
					__fl.isStrict())
					throw new CFFormatException(String.format("CF0w %s",
						__fl));
			
			// Cannot contain < or >
			int n = name.length();
			for (int i = 0; i < n; i++)
			{
				char c = name.charAt(i);
				
				// {@squirreljme.error CF1c Methods cannot have greater than
				// or less than signs in their name. (The method name)}
				if (c == '<' || c == '>')
					throw new CFFormatException(String.format("CF1c %s",
						name));
			}
		}
		
		// Otherwise specific flags must be set
		else
		{
			if (isconstructor)
			{
				// {@squirreljme.error CF1d Instance initializers cannot be
				// {@code static}, {@code abstract}, {@code final}, 
				// {@code synchronized}, or {@code native}.
				// (Method flags)}
				if (__fl.isStatic() || __fl.isAbstract() || __fl.isFinal() ||
					__fl.isSynchronized() || __fl.isNative())
					throw new CFFormatException(String.format("CF1d", __fl));
			}
			
			// {@squirreljme.error CF1e Starting with CLDC 1.7, class
			// initializers must have their static flag set. (Method flags)}
			if (__ver.compareTo(CFClassVersion.CLDC_8) >= 0 &&
				isclassinit && !__fl.isStatic())
				throw new CFFormatException(String.format("CF1e", __fl));
		}
		
		// {@squirreljme.error CF0x Non-abstract and non-native methods must
		// have a code attribute.}
		if (codeattribute == null && !(__fl.isAbstract() || __fl.isNative()))
			throw new CFFormatException("CF0x");
	}
	
	/**
	 * Returns the code attribute that is associated with this method.
	 *
	 * @return The code attribute or {@code null} if
	 * there is no code attribute.
	 * @since 2016/04/03
	 */
	public CFCodeAttribute codeAttribute()
	{
		// Get attribute
		byte[] attr = codeattribute;
		if (attr == null)
			return null;
		
		// Get ref
		Reference<CFCodeAttribute> ref = _cviewer;
		CFCodeAttribute rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
			_cviewer = new WeakReference<>((rv = new CFCodeAttribute(this,
				attr)));
		
		// Return it
		return rv;
	}
	
	/**
	 * Is this method a class initializer?
	 *
	 * @return {@code true} if a class initializer.
	 * @since 2016/03/20
	 */
	public boolean isClassInitializer()
	{
		return isclassinit;
	}
	
	/**
	 * Is this method a constructor?
	 *
	 * @return {@code true} if a constructor.
	 * @since 2016/03/20
	 */
	public boolean isConstructor()
	{
		return isconstructor;
	}
}

