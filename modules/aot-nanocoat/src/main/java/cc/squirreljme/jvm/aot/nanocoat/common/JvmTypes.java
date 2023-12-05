// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

import cc.squirreljme.c.CPrimitiveType;
import cc.squirreljme.c.CStructKind;
import cc.squirreljme.c.CStructTypeBuilder;
import cc.squirreljme.c.CType;
import cc.squirreljme.c.CTypeDefType;
import cc.squirreljme.c.std.CStdIntType;
import cc.squirreljme.c.std.CTypeProvider;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Types that are defined by NanoCoat.
 *
 * @since 2023/06/06
 */
public enum JvmTypes
	implements CTypeProvider
{
	/** Any/temporary storage. */
	ANY
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_any")
				.member(JvmTypes.BASIC_TYPE_ID, "type")
				.member(JvmTypes.ANY_DATA, "data")
				.build();
		}
	},
	
	/** Any data storage. */
	ANY_DATA
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.UNION,
				"sjme_anyData")
				.member(JvmTypes.JINT,
					"jint")
				.member(JvmTypes.JOBJECT,
					"jobject")
				.member(JvmTypes.TEMP_INDEX,
					"tempIndex")
				.build();
		}
	},
	
	/** Basic Type ID. */
	BASIC_TYPE_ID
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JINT.type(),
				"sjme_basicTypeId");
		}
	},
	
	/** Multiple basic type IDs. */
	BASIC_TYPE_IDS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/09
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
					"sjme_basicTypeIds")
				.member(JvmTypes.JINT, "count")
				.member(JvmTypes.BASIC_TYPE_ID.type().constType()
					.arrayType(0), "ids")
				.build();
		}
	},
	
	/** Dynamic Linkage. */
	DYNAMIC_LINKAGE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_dynamic_linkage")
				.member(JvmTypes.STATIC_LINKAGE_TYPE, "type")
				.member(JvmTypes.DYNAMIC_LINKAGE_DATA, "data")
				.build();
		}
	},
	
	/** Data for the dynamic linkage table. */
	DYNAMIC_LINKAGE_DATA
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.UNION,
				"sjme_dynamic_linkageData")
				.member(JvmTypes.DYNAMIC_LINKAGE_DATA_CLASS_OBJECT,
					"classObject")
				.member(JvmTypes.DYNAMIC_LINKAGE_DATA_FIELD_ACCESS,
					"fieldAccess")
				.member(JvmTypes.DYNAMIC_LINKAGE_DATA_INVOKE_SPECIAL,
					"invokeSpecial")
				.member(JvmTypes.DYNAMIC_LINKAGE_DATA_INVOKE_NORMAL,
					"invokeNormal")
				.member(JvmTypes.DYNAMIC_LINKAGE_DATA_STRING_OBJECT,
					"stringObject")
				.build();
		}
	},
	
	/** A class object reference. */
	DYNAMIC_LINKAGE_DATA_CLASS_OBJECT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_dynamic_linkage_data_classObject")
				.member(JvmTypes.JINT,"todo")
				.build();
		}
	},
	
	/** Field access. */
	DYNAMIC_LINKAGE_DATA_FIELD_ACCESS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_dynamic_linkage_data_fieldAccess")
				.member(JvmTypes.JINT,"todo")
				.build();
		}
	},
	
	/** Invoke normal linkage data. */
	DYNAMIC_LINKAGE_DATA_INVOKE_NORMAL
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_dynamic_linkage_data_invokeNormal")
				.member(JvmTypes.JINT,"todo")
				.build();
		}
	},
	
	/** Invoke special linkage data. */
	DYNAMIC_LINKAGE_DATA_INVOKE_SPECIAL
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_dynamic_linkage_data_invokeSpecial")
				.member(JvmTypes.JINT,"todo")
				.build();
		}
	},
	
	/** A string reference. */
	DYNAMIC_LINKAGE_DATA_STRING_OBJECT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_dynamic_linkage_data_stringObject")
				.member(JvmTypes.JINT,"todo")
				.build();
		}
	},
	
	/** Invocation type. */
	INVOKE_TYPE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/15
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JINT.type(),
				"sjme_invokeType");
		}
	},
	
	/** Boolean. */
	JBOOLEAN
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(CStdIntType.UINT8.type(),
				"jboolean");
		}
	},
	
	/** Byte. */
	JBYTE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/16
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(CStdIntType.INT8.type(),
				"jbyte");
		}
	},
	
	/** Character. */
	JCHAR
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(CStdIntType.UINT16.type(),
				"jchar");
		}
	},
	
	/** Class. */
	JCLASS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JOBJECT.type(),
				"jclass");
		}
	},
	
	/** Double. */
	JDOUBLE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"jdouble")
				.member(JvmTypes.JINT, "hi")
				.member(JvmTypes.JINT, "lo")
				.build();
		}
	},
	
	/** Field. */
	JFIELD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Float. */
	JFLOAT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"jfloat")
				.member(JvmTypes.JINT, "value")
				.build();
		}
	},
	
	/** Integer. */
	JINT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(CStdIntType.INT32.type(),
				"jint");
		}
	},
	
	/** Long. */
	JLONG
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"jlong")
				.member(JvmTypes.JINT, "hi")
				.member(JvmTypes.JINT, "lo")
				.build();
		}
	},
	
	/** Method. */
	JMETHOD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Object base. */
	JOBJECT_BASE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(
				CStructKind.STRUCT, "sjme_jobjectBase")
				.build();
		}
	},
	
	/** Object. */
	JOBJECT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JOBJECT_BASE.pointerType(),
				"jobject");
		}
	},
	
	/** Short. */
	JSHORT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(CStdIntType.INT16.type(),
				"jshort");
		}
	},
	
	/** String. */
	JSTRING
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JOBJECT.type(),
				"jstring");
		}
	},
	
	/** Throwable. */
	JTHROWABLE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JOBJECT.type(),
				"jthrowable");
		}
	},
	
	/** Unsigned Byte. */
	JUBYTE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/09
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(CStdIntType.UINT8.type(),
				"jubyte");
		}
	},
	
	/** Program counter address. */
	PC_ADDR
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JINT.type(),
				"sjme_pcAddr");
		}
	},
	
	/** Code information. */
	STATIC_CLASS_CODE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/09
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
					"sjme_static_classCode")
				.member(JvmTypes.STATIC_CLASS_CODE_LIMITS.type().constType()
					.pointerType(), "limits")
				.member(JvmTypes.JSHORT,
					"thrownVarIndex")
				.member(JvmTypes.STATIC_LINKAGE.type().constType()
					.pointerType(), "linkage")
				.member(JvmFunctions.METHOD_CODE.function(), "code")
				.build();
		}
	},
	
	/** Class code limits. */
	STATIC_CLASS_CODE_LIMITS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/09
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_classCodeLimits")
				.member(JvmTypes.JUBYTE.type().constType()
					.arrayType(JvmPrimitiveType.NUM_JAVA_TYPES),
					"maxVariables")
				.build();
		}
	},
	
	/** Field information. */
	STATIC_CLASS_FIELD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_classField")
				.member(CPrimitiveType.CONST_CHAR_STAR, "name")
				.member(JvmTypes.STATIC_FIELD_TYPE.type().constType()
					.pointerType(), "type")
				.member(JvmTypes.JINT.type().constType(), "flags")
				.member(JvmTypes.BASIC_TYPE_ID, "valueType")
				.member(JvmTypes.STATIC_CONST_VALUE,
					"value")
				.build();
		}
	},
	
	/** Multiple fields. */
	STATIC_CLASS_FIELDS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_classFields")
				.member(JvmTypes.JINT, "count")
				.member(JvmTypes.STATIC_CLASS_FIELD.type()
					.arrayType(0), "fields")
				.build();
		}
	},
	
	/** Class interface. */
	STATIC_CLASS_INTERFACE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_classInterface")
				.member(CPrimitiveType.CONST_CHAR_STAR, "interfaceName")
				.build();
		}
	},
	
	/** Class interfaces. */
	STATIC_CLASS_INTERFACES
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_classInterfaces")
				.member(JvmTypes.JINT, "count")
				.member(JvmTypes.STATIC_CLASS_INTERFACE.type()
					.arrayType(0), "interfaces")
				.build();
		}
	},
	
	/** Method information. */
	STATIC_CLASS_METHOD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_classMethod")
				.member(CPrimitiveType.CONST_CHAR_STAR, "name")
				.member(CPrimitiveType.CONST_CHAR_STAR, "type")
				.member(JvmTypes.JINT, "flags")
				.member(JvmTypes.BASIC_TYPE_IDS.type().constType()
						.pointerType(), "argTypes")
				.member(JvmTypes.BASIC_TYPE_ID,
					"rValType")
				.member(JvmTypes.STATIC_CLASS_CODE.type().constType()
					.pointerType(), "code")
				.build();
		}
	},
	
	/** Multiple class methods. */
	STATIC_CLASS_METHODS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_classMethods")
				.member(JvmTypes.JINT, "count")
				.member(JvmTypes.STATIC_CLASS_METHOD.type()
					.arrayType(0), "methods")
				.build();
		}
	},
	
	/** Class information. */
	STATIC_CLASS_INFO
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
					"sjme_static_classInfo")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"thisName")
				.member(JvmTypes.JINT,
					"thisNameHash")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"superName")
				.member(JvmTypes.STATIC_CLASS_INTERFACES.type()
					.constType().pointerType(), "interfaceNames")
				.member(JvmTypes.JINT,
					"flags")
				.member(JvmTypes.STATIC_CLASS_FIELDS.type()
					.constType().pointerType(), "fields")
				.member(JvmTypes.STATIC_CLASS_METHODS.type()
					.constType().pointerType(), "methods")
				.member(JvmTypes.STATIC_LINKAGES.type()
					.constType().pointerType(), "linkages")
				.build();
		}
	},
	
	/** Constant value union. */
	STATIC_CONST_VALUE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.UNION,
				"sjme_static_constValue")
				.member(JvmTypes.JINT, "jint")
				.member(JvmTypes.JLONG, "jlong")
				.member(JvmTypes.JFLOAT, "jfloat")
				.member(JvmTypes.JDOUBLE, "jdouble")
				.member(CPrimitiveType.CONST_CHAR_STAR, "jstring")
				.member(CPrimitiveType.CONST_CHAR_STAR, "jclass")
				.build();	
		}
	},
	
	/** The field type. */
	STATIC_FIELD_TYPE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/13
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
					"sjme_static_fieldType")
				.member(JvmTypes.JINT, "hashCode")
				.member(CPrimitiveType.CONST_CHAR_STAR, "descriptor")
				.member(JvmTypes.BASIC_TYPE_ID, "basicType")
				.build();
		}
	},
	
	/** Static libraries. */
	STATIC_LIBRARIES
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/28
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_libraries")
				.member(JvmTypes.JINT, "count")
				.member(JvmTypes.STATIC_LIBRARY.type()
					.constType().pointerType()
					.arrayType(0), "libraries")
				.build();
		}
	},
	
	/** Static library. */
	STATIC_LIBRARY
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
					"sjme_static_library")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"name")
				.member(JvmTypes.JINT,
					"nameHash")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"originalLibHash")
				.member(JvmTypes.STATIC_LIBRARY_CLASSES
					.type().constType().pointerType(), "classes")
				.member(JvmTypes.STATIC_LIBRARY_RESOURCES
					.type().constType().pointerType(), "resources")
				.build();
		}
	},
	
	/** Static library classes. */
	STATIC_LIBRARY_CLASSES
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
					"sjme_static_library_classes")
				.member(JvmTypes.JINT, "count")
				.member(JvmTypes.STATIC_CLASS_INFO.type().constType()
					.pointerType().pointerType(), "classes")
				.build();
		}
	},
	
	/** Static library resources. */
	STATIC_LIBRARY_RESOURCES
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
					"sjme_static_library_resources")
				.member(JvmTypes.JINT, "count")
				.member(JvmTypes.STATIC_RESOURCE.type().constType()
					.pointerType().pointerType(), "resources")
				.build();
		}
	},
	
	/** Static Linkage. */
	STATIC_LINKAGE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_linkage")
				.member(JvmTypes.STATIC_LINKAGE_TYPE, "type")
				.member(JvmTypes.STATIC_LINKAGE_DATA, "data")
				.build();
		}
	},
	
	/** Data for the static linkage table. */
	STATIC_LINKAGE_DATA
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.UNION,
				"sjme_static_linkageData")
				.member(JvmTypes.STATIC_LINKAGE_DATA_CLASS_OBJECT,
					"classObject")
				.member(JvmTypes.STATIC_LINKAGE_DATA_FIELD_ACCESS,
					"fieldAccess")
				.member(JvmTypes.STATIC_LINKAGE_DATA_INVOKE,
					"invoke")
				.member(JvmTypes.STATIC_LINKAGE_DATA_STRING_OBJECT,
					"stringObject")
				.build();
		}
	},
	
	/** A class object reference. */
	STATIC_LINKAGE_DATA_CLASS_OBJECT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_linkage_data_classObject")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"className")
				.build();
		}
	},
	
	/** Field access. */
	STATIC_LINKAGE_DATA_FIELD_ACCESS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_linkage_data_fieldAccess")
				.member(JvmTypes.JBOOLEAN,
					"isStatic")
				.member(JvmTypes.JBOOLEAN,
					"isStore")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"sourceMethodName")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"sourceMethodType")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"targetClass")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"targetFieldName")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"targetFieldType")
				.build();
		}
	},
	
	/** Invoke normal linkage data. */
	STATIC_LINKAGE_DATA_INVOKE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_linkage_data_invoke")
				.member(JvmTypes.INVOKE_TYPE,
					"invokeType")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"targetClass")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"targetMethodName")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"targetMethodType")
				.build();
		}
	},
	
	/** A string reference. */
	STATIC_LINKAGE_DATA_STRING_OBJECT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_linkage_data_stringObject")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"string")
				.build();
		}
	},
	
	/** Static linkage type identifier. */
	STATIC_LINKAGE_TYPE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JINT.type(),
				"sjme_staticLinkageType");
		}
	},
	
	/** Static linkages table. */
	STATIC_LINKAGES
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_linkages")
				.member(JvmTypes.JINT, "count")
				.member(JvmTypes.STATIC_LINKAGE.type()
					.arrayType(0), "linkages")
				.build();
		}
	},
	
	/** Static method type. */
	STATIC_METHOD_TYPE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/13
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_methodType")
				.member(JvmTypes.JINT, "hashCode")
				.member(CPrimitiveType.CONST_CHAR_STAR, "descriptor")
				.member(JvmTypes.STATIC_FIELD_TYPE.type().constType()
					.pointerType(), "returnType")
				.member(JvmTypes.JINT, "argCount")
				.member(JvmTypes.STATIC_FIELD_TYPE.type().constType()
					.pointerType().arrayType(0), "argTypes")
				.build();
		}
	},
	
	/** A NanoCoat resource. */
	STATIC_RESOURCE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(
				CStructKind.STRUCT, "sjme_static_resource")
				.member(CPrimitiveType.CHAR.pointerType(), "path")
				.member(JvmTypes.JINT, "pathHash")
				.member(JvmTypes.JINT, "size")
				.member(CStdIntType.UINT8.type().constType().pointerType(),
					"data")
				.build();
		}
	},
	
	/** Static ROM. */
	STATIC_ROM
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
					"sjme_static_rom")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"sourceSet")
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"clutterLevel")
				.member(JvmTypes.STATIC_LIBRARIES.type()
					.constType(), "libraries")
				.build();
		}
	},
	
	/** Temporary value index. */
	TEMP_INDEX
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/15
		 */
		@Override
		CType __build()
		{
			return CTypeDefType.of(JvmTypes.JINT.type(),
				"sjme_tempIndex");
		}
	},
	
	/** Stack frame. */
	VMFRAME
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(
				CStructKind.STRUCT, "sjme_nvm_frame")
				.member(JvmTypes.PC_ADDR, "pc")
				.member(JvmTypes.DYNAMIC_LINKAGE.type().pointerType(),
					"linkage")
				.member(JvmTypes.JTHROWABLE.type().pointerType(),
					"waitingThrown")
				.member(JvmTypes.ANY,
					"returnValue")
				.member(JvmTypes.ANY.type().pointerType().pointerType(),
					"tempStack")
				.member(JvmTypes.JOBJECT,
					"thisRef")
				.member(JvmTypes.JCLASS.pointerType(),
					"classObjectRef")
				.build();
		}
	},
	
	/** The NanoCoat state. */
	VMSTATE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(
				CStructKind.STRUCT, "sjme_nvm_state")
				.build();
		}
	},
	
	/** A NanoCoat thread. */
	VMTHREAD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(
				CStructKind.STRUCT, "sjme_nvm_thread")
				.member(JvmTypes.VMFRAME.pointerType(), "top")
				.build();
		}
	},
	
	/* End. */
	;
	
	/** Internal type cache. */
	private volatile Reference<CType> _type;
	
	/**
	 * Builds the type, this is done at run-time since this is an enum and
	 * some types may refer to each other accordingly using either consts
	 * or pointers.
	 * 
	 * @return The built type.
	 * @since 2023/06/06
	 */
	abstract CType __build();
	
	/**
	 * Returns the pointer type for this.
	 * 
	 * @return The pointer type for this type.
	 * @since 2023/07/16
	 */
	public final CType pointerType()
	{
		return this.type().pointerType();
	}
	
	/**
	 * Returns the type used.
	 * 
	 * @return The type used.
	 * @since 2023/06/06
	 */
	@Override
	public final CType type()
	{
		Reference<CType> ref = this._type;
		CType rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = this.__build();
			this._type = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the type used as the given class type.
	 * 
	 * @param <T> The class to cast to.
	 * @param __class The class to cast to.
	 * @return The resultant type as a class.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public final <T extends CType> T type(Class<T> __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		return __class.cast(this.type());
	}
}
