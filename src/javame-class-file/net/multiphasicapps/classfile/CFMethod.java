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
	
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __nat The name and type of the field.
	 * @param __fl The flags which the member uses.
	 * @param __ca The code attribute, the direct array is used and should
	 * not be modified.
	 * @throws NullPointerException On null arguments except for {@code __ca}.
	 * @since 2016/03/01
	 */
	CFMethod(CFMemberKey<MethodSymbol> __nat, CFMethodFlags __fl, byte[] __ca)
		throws NullPointerException
	{
		super(MethodSymbol.class, __nat, CFMethodFlags.class, __fl);
		
		// Is this a constructor?
		isconstructor = name().equals("<init>");
		isclassinit = name().equals("<clinit>");
		
		// Set
		codeattribute = __ca;
		
		// Class initializer flags are ignored for the most part
		if (!isClassInitializer())
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
		}
		
		// {@squirreljme.error CF0x Non-abstract and non-native methods must
		// have a code attribute.}
		if (codeattribute == null && !(__fl.isAbstract() || __fl.isNative()))
			throw new CFFormatException("CF0x");
	}
	
	/**
	 * Returns the code attribute data as an input stream.
	 *
	 * @return The code attribute data as an input stream, or {@code null} if
	 * there is no code attribute.
	 * @since 2016/04/03
	 */
	public InputStream codeAttribute()
	{
		if (codeattribute != null)
			return new ByteArrayInputStream(codeattribute);
		return null;
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

