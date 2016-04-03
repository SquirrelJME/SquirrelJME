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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This is the base class for all class related information.
 *
 * @since 2016/03/16
 */
public final class CFClass
{
	/** The class flags. */
	protected final CFClassFlags flags;
	
	/** The interfaces of a class. */
	protected final CFClassInterfaces interfaces;	
	
	/** Field. */
	protected final CFFields fields;
	
	/** Methods. */
	protected final CFMethods methods;
	
	/** The constant pool of the class. */
	protected final CFConstantPool constantpool;
	
	/** The version of this class. */
	protected final CFClassVersion version;
	
	/** The name of this class. */
	protected final BinaryNameSymbol thisname;
	
	/** The name of the super class. */
	protected final BinaryNameSymbol supername;
	
	/**
	 * Initializes the base class information.
	 *
	 * @param __cf The parser which loaded class file data.
	 * @throws CFFormatException If final checks for a class are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/16
	 */
	CFClass(CFClassParser __cf)
		throws CFFormatException, NullPointerException
	{
		// Check
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		// Set
		flags = Objects.<CFClassFlags>requireNonNull(__cf._flags);
		interfaces = new CFClassInterfaces(__cf._interfaces);
		fields = new CFFields(this, __cf._fields);
		methods = new CFMethods(this, __cf._methods);
		constantpool = Objects.<CFConstantPool>requireNonNull(
			__cf._constantpool);
		version = Objects.<CFClassVersion>requireNonNull(__cf._version);
		thisname = Objects.<BinaryNameSymbol>requireNonNull(__cf._thisname);
		supername = Objects.<BinaryNameSymbol>requireNonNull(__cf._supername);
		
		// {@squirreljme.error CF01 If this is the object class and it has a
		// super class, or this is not the object class and there is no super
		// class set. (This class name; The super class name)}
		if (thisname.equals("java/lang/Object") != (supername == null))
			throw new CFFormatException(String.format("CF01 %s %s", thisname,
				supername));
	}
	
	/**
	 * Returns the constant pool which this class uses.
	 *
	 * @return The constant pool of this class.
	 * @sincer 2016/04/03
	 */
	public final CFConstantPool constantPool()
	{
		return constantpool;
	}
	
	/**
	 * Returns the fields which are mapped to this class.
	 *
	 * @return The field mappings.
	 * @since 2016/03/20
	 */
	public final CFFields fields()
	{
		return fields;
	}
	
	/**
	 * Returns the flags which are specified by this class.
	 *
	 * @return The used class flags.
	 * @throws IllegalStateException If flags are not set.
	 * @since 2016/03/16
	 */
	public final CFClassFlags flags()
		throws IllegalStateException
	{
		return flags;
	}
	
	/**
	 * Returns the set of interfaces which may be implemented by the class.
	 *
	 * @return The class interface set.
	 * @since 2016/03/19
	 */
	public final CFClassInterfaces interfaces()
	{
		return interfaces;
	}
	
	/**
	 * Returns the methods which are mapped to this class.
	 *
	 * @return The method mappings.
	 * @since 2016/03/20
	 */
	public final CFMethods methods()
	{
		return methods;
	}
	
	/**
	 * Returns the super class name of this class.
	 *
	 * @return The super class this extends.
	 * @since 2016/03/19
	 */
	public final BinaryNameSymbol superName()
	{
		return supername;
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @since 2016/03/19
	 */
	public final BinaryNameSymbol thisName()
	{
		return thisname;
	}
	
	/**
	 * Returns the version of this class.
	 *
	 * @return The class version.
	 * @since 2016/04/03
	 */
	public final CFClassVersion version()
	{
		return version;
	}
}

