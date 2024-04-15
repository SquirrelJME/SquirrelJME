// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Reference type command set.
 *
 * @since 2021/03/13
 */
public enum JDWPCommandSetReferenceType
	implements JDWPCommand
{
	/** Non-generic signature of a given type. */
	SIGNATURE(1),
	
	/** The class loader used on a class. */
	CLASS_LOADER(2),
	
	/** Modifiers for class. */
	MODIFIERS(3),
	
	/** Fields without generic. */
	FIELDS(4),
	
	/** Methods without generic. */
	METHODS(5),
	
	/** Static field values. */
	STATIC_FIELD_VALUE(6),
	
	/** Source file. */
	SOURCE_FILE(7),
	
	/** Interfaces. */
	INTERFACES(10),
	
	/** Class object of type. */
	CLASS_OBJECT(11),
	
	/** Signature with generic (class). */
	SIGNATURE_WITH_GENERIC(13),
	
	/** Fields with generic types. */
	FIELDS_WITH_GENERIC(14),
	
	/** Methods with generic types. */
	METHODS_WITH_GENERIC(15),
	
	/** Class file version. */
	CLASS_FILE_VERSION(17),
	
	/** Read the raw constant pool of a class. */
	CONSTANT_POOL(18),
	
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	JDWPCommandSetReferenceType(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
