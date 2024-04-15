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
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassLink;
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassObjectLinkage;
import cc.squirreljme.jvm.aot.nanocoat.linkage.FieldAccessLinkage;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;
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
		String.class,
		CPrimitiveType.CONST_CHAR_STAR.constType()),
	
	/** Method code. */
	CODE("code",
		CodeFingerprint.class,
		ByteCode.class,
		JvmTypes.STATIC_CLASS_CODE),
	
	/** Class interfaces. */
	CLASS_INTERFACES("ints",
		ClassInterfaces.class,
		ClassInterfaces.class,
		JvmTypes.STATIC_CLASS_INTERFACES),
	
	/** Class linkage tables. */
	CLASS_LINK("clln",
		ClassLink.class,
		ClassLink.class,
		JvmTypes.STATIC_LINKAGES),
	
	/** Class object linkages. */
	LINKAGE_CLASS("lncl",
		ClassObjectLinkage.class,
		ClassObjectLinkage.class,
		JvmTypes.STATIC_LINKAGE_DATA_CLASS_OBJECT),
	
	/** Field access linkages. */
	LINKAGE_FIELD_ACCESS("lnfa",
		FieldAccessLinkage.class,
		FieldAccessLinkage.class,
		JvmTypes.STATIC_LINKAGE_DATA_FIELD_ACCESS),
	
	/** Method invocation linkages. */
	LINKAGE_METHOD_INVOKE("lnmi",
		byte.class,
		byte.class,
		JvmTypes.STATIC_LINKAGE_DATA_INVOKE),
	
	/** String reference linkages. */
	LINKAGE_STRING("lnst",
		byte.class,
		byte.class,
		JvmTypes.STATIC_LINKAGE_DATA_STRING_OBJECT),
	
	/** Integer value linkages. */
	LINKAGE_INTEGER("lnvi",
		byte.class,
		byte.class,
		CPrimitiveType.VOID),
	
	/** Long value linkages. */
	LINKAGE_LONG("lnvj",
		byte.class,
		byte.class,
		CPrimitiveType.VOID),
	
	/** Float value linkages. */
	LINKAGE_FLOAT("lnvf",
		byte.class,
		byte.class,
		CPrimitiveType.VOID),
	
	/** Double value linkages. */
	LINKAGE_DOUBLE("lnvd",
		byte.class,
		byte.class,
		CPrimitiveType.VOID),
	
	/** Library resource. */
	RESOURCE("rsrc",
		byte.class,
		byte.class,
		JvmTypes.STATIC_RESOURCE),
	
	/** Field type information. */
	FIELD_TYPE("tyfi",
		FieldDescriptor.class,
		FieldDescriptor.class,
		JvmTypes.STATIC_FIELD_TYPE),
	
	/** Method type information. */
	METHOD_TYPE("tyme",
		MethodDescriptor.class,
		MethodDescriptor.class,
		JvmTypes.STATIC_METHOD_TYPE),
	
	/** Locals/stack variable limit information. */
	VARIABLE_LIMITS("valn",
		VariableLimits.class,
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
	public final Class<?> keyType;
	
	/** The value type. */
	public final Class<?> valueType;
	
	/** The C type to store for the table entry. */
	public final CType cType;
	
	/**
	 * Initializes the table type.
	 *
	 * @param __prefix The prefix used.
	 * @param __keyType The element type.
	 * @param __valueType The value type.
	 * @param __cType The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StaticTableType(String __prefix, Class<?> __keyType, Class<?> __valueType,
		CTypeProvider __cType)
		throws NullPointerException
	{
		this(__prefix, __keyType, __valueType, __cType.type());
	}
	
	/**
	 * Initializes the table type.
	 *
	 * @param __prefix The prefix used.
	 * @param __keyType The element type.
	 * @param __valueType The value type.
	 * @param __cType The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StaticTableType(String __prefix, Class<?> __keyType, Class<?> __valueType,
		CType __cType)
		throws NullPointerException
	{
		if (__prefix == null || __keyType == null || __valueType == null ||
			__cType == null)
			throw new NullPointerException("NARG");
		
		this.prefix = __prefix;
		this.keyType = __keyType;
		this.valueType = __valueType;
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
	final StaticTable<?, ?> __newTable(Reference<StaticTableManager> __group)
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
			
			case CLASS_LINK:
				throw Debugging.todo();
				
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
				return new FieldTypeStaticTable(__group);
			
			case METHOD_TYPE:
				return new MethodTypeStaticTable(__group);
			
			case VARIABLE_LIMITS:
				return new VariableLimitsStaticTable(__group);
			
			default:
				throw Debugging.todo(this);
		}
	}
}
