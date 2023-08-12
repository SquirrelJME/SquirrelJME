// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.c.CPrimitiveType;
import cc.squirreljme.c.CType;
import cc.squirreljme.c.std.CTypeProvider;
import cc.squirreljme.jvm.aot.nanocoat.ClassInterfaces;
import cc.squirreljme.jvm.aot.nanocoat.CodeFingerprint;
import cc.squirreljme.jvm.aot.nanocoat.VariableLimits;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassObjectLinkage;
import cc.squirreljme.jvm.aot.nanocoat.linkage.FieldAccessLinkage;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents the type of static table this is.
 *
 * @since 2023/08/11
 */
public enum StaticTableType
{
	/** Strings. */
	STRING("char",
		String.class,
		CPrimitiveType.CONST_CHAR_STAR.constType()),
	
	/** Method code. */
	CODE("code",
		CodeFingerprint.class,
		JvmTypes.STATIC_CLASS_CODE),
	
	/** Class interfaces. */
	CLASS_INTERFACES("ints",
		ClassInterfaces.class,
		JvmTypes.STATIC_CLASS_INTERFACES),
	
	/** Class object linkages. */
	LINKAGE_CLASS("lncl",
		ClassObjectLinkage.class,
		JvmTypes.STATIC_LINKAGE_DATA_CLASS_OBJECT),
	
	/** Field access linkages. */
	LINKAGE_FIELD_ACCESS("lnfa",
		FieldAccessLinkage.class,
		JvmTypes.STATIC_LINKAGE_DATA_FIELD_ACCESS),
	
	/** Method invocation linkages. */
	LINKAGE_METHOD_INVOKE("lnmi",
		byte.class,
		JvmTypes.STATIC_LINKAGE_DATA_INVOKE_NORMAL),
	
	/** String reference linkages. */
	LINKAGE_STRING("lnst",
		byte.class,
		JvmTypes.STATIC_LINKAGE_DATA_STRING_OBJECT),
	
	/** Integer value linkages. */
	LINKAGE_INTEGER("lnvi",
		byte.class,
		CPrimitiveType.VOID),
	
	/** Long value linkages. */
	LINKAGE_LONG("lnvj",
		byte.class,
		CPrimitiveType.VOID),
	
	/** Float value linkages. */
	LINKAGE_FLOAT("lnvf",
		byte.class,
		CPrimitiveType.VOID),
	
	/** Double value linkages. */
	LINKAGE_DOUBLE("lnvd",
		byte.class,
		CPrimitiveType.VOID),
	
	/** Library resource. */
	RESOURCE("rsrc",
		byte.class,
		JvmTypes.STATIC_RESOURCE),
	
	/** Field type information. */
	FIELD_TYPE("tyme",
		byte.class,
		CPrimitiveType.VOID),
	
	/** Method type information. */
	METHOD_TYPE("tyme",
		byte.class,
		CPrimitiveType.VOID),
	
	/** Locals/stack variable limit information. */
	LOCALS_STACK_LIMITS("valn",
		VariableLimits.class,
		JvmTypes.STATIC_CLASS_CODE_LIMITS),
	
	/* End. */
	;
	
	/** The static table types available. */
	public static final List<StaticTableType> TYPES =
		UnmodifiableList.of(Arrays.asList(StaticTableType.values()));
	
	/** The number of types available. */
	public static final int NUM_TYPES =
		StaticTableType.TYPES.size();
	
	/** The table prefix. */
	public final String prefix;
	
	/** The type of elements the table stores. */
	public final Class<?> elementType;
	
	/** The C type to store for the table entry. */
	public final CType cType;
	
	/**
	 * Initializes the table type.
	 *
	 * @param __prefix The prefix used.
	 * @param __elementType The element type.
	 * @param __cType The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StaticTableType(String __prefix, Class<?> __elementType,
		CTypeProvider __cType)
		throws NullPointerException
	{
		this(__prefix, __elementType, __cType.type());
	}
	
	/**
	 * Initializes the table type.
	 *
	 * @param __prefix The prefix used.
	 * @param __elementType The element type.
	 * @param __cType The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StaticTableType(String __prefix, Class<?> __elementType, CType __cType)
		throws NullPointerException
	{
		if (__prefix == null || __elementType == null || __cType == null)
			throw new NullPointerException("NARG");
		
		this.prefix = __prefix;
		this.elementType = __elementType;
		this.cType = __cType;
	}
	
	/**
	 * Initializes a new table.
	 *
	 * @param __group The group this is under.
	 * @return The created table.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	final StaticTable<?> __newTable(Reference<StaticTableManager> __group)
		throws NullPointerException
	{
		if (__group == null)
			throw new NullPointerException("NARG");
		
		switch (this)
		{
			case STRING:
				return new StringStaticTable(__group);
				
			case CODE:
				return new CodeStaticTable(__group);
				
			case CLASS_INTERFACES:
				return new ClassInterfacesStaticTable(__group);
			
			case LINKAGE_CLASS:
				throw Debugging.todo();
				
			case LINKAGE_FIELD_ACCESS:
				throw Debugging.todo();
			
			case LINKAGE_METHOD_INVOKE:
				throw Debugging.todo();
				
			case LINKAGE_STRING:
				throw Debugging.todo();
			
			case LINKAGE_INTEGER:
				throw Debugging.todo();
			
			case LINKAGE_LONG:
				throw Debugging.todo();
			
			case LINKAGE_FLOAT:
				throw Debugging.todo();
			
			case LINKAGE_DOUBLE:
				throw Debugging.todo();
			
			case RESOURCE:
				throw Debugging.todo();
			
			case FIELD_TYPE:
				throw Debugging.todo();
			
			case METHOD_TYPE:
				throw Debugging.todo();
			
			case LOCALS_STACK_LIMITS:
				throw Debugging.todo();
			
			default:
				throw Debugging.todo(this);
		}
	}
}
