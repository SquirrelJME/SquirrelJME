// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

import cc.squirreljme.c.CExpressionBuilder;
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
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.FieldDescriptor;

/**
 * Types that are defined by NanoCoat.
 *
 * @since 2023/06/06
 */
public enum JvmTypes
	implements CTypeProvider
{
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
			return CTypeDefType.of(JvmTypes.JINT.type().arrayType(2),
				"jlong");
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
			return CTypeDefType.of(JvmTypes.JINT.type(), "jfloat");
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
			return CTypeDefType.of(JvmTypes.JINT.type().arrayType(2),
				"jfloat");
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
			throw Debugging.todo();
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
			throw Debugging.todo();
		}
	},
	
	/** String. */
	JSTRING
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
				.member(JvmTypes.VMFRAME.type().pointerType(), "top")
				.build();
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
				.member(JvmTypes.JINT.type(), "groupIndex")
				.member(JvmTypes.STATIC_LINKAGE.type().pointerType(),
					"linkage")
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
				.member(JvmTypes.JINT.type(), "size")
				.member(CStdIntType.UINT8.type().constType().pointerType(),
					"data")
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
				.member(CPrimitiveType.CONST_CHAR_STAR,
					"superName")
				.member(JvmTypes.JINT.type(),
					"flags")
				.member(JvmTypes.STATIC_CLASS_FIELDS.type()
					.constType().pointerType(), "fields")
				.member(JvmTypes.STATIC_CLASS_METHODS.type()
					.constType().pointerType(), "methods")
				.build();
		}
	},
	
	/** Constant value union. */
	STATIC_CLASS_CVALUE
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
				.member(JvmTypes.JINT.type(), "jint")
				.member(JvmTypes.JLONG.type(), "jlong")
				.member(JvmTypes.JFLOAT.type(), "jfloat")
				.member(JvmTypes.JDOUBLE.type(), "jdouble")
				.member(CPrimitiveType.CONST_CHAR_STAR, "string")
				.member(CPrimitiveType.CONST_CHAR_STAR, "jclass")
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
				.member(CPrimitiveType.CONST_CHAR_STAR, "type")
				.member(JvmTypes.JINT.type().constType(), "flags")
				.member(JvmTypes.JINT.type(), "valueType")
				.member(JvmTypes.STATIC_CLASS_CVALUE.type(),
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
				.member(JvmTypes.JINT.type(), "count")
				.member(JvmTypes.STATIC_CLASS_FIELD.type()
					.arrayType(0), "fields")
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
				.member(JvmTypes.JINT.type().constType(), "flags")
				.member(JvmTypes.JINT.type(), "rValSlots")
				.member(JvmTypes.JINT.type(), "argSlots")
				.member(JvmFunctions.METHOD_CODE.function(), "code")
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
				.member(JvmTypes.JINT.type(), "count")
				.member(JvmTypes.STATIC_CLASS_METHOD.type()
					.arrayType(0), "methods")
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
				.member(JvmTypes.STATIC_LINKAGE_DATA.type(), "data")
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
				.member(JvmTypes.STATIC_LINKAGE_DATA_INVOKESPECIAL.type(),
					"invokeSpecial")
				.build();
		}
	},
	
	/** Invoke special linkage data. */
	STATIC_LINKAGE_DATA_INVOKESPECIAL
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/25
		 */
		@Override
		CType __build()
		{
			return CStructTypeBuilder.builder(CStructKind.STRUCT,
				"sjme_static_linkageData_invokeSpecial")
				.member(JvmTypes.JINT.type(),"todo")
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
