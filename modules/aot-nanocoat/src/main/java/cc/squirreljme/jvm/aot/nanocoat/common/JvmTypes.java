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
			throw Debugging.todo();
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
			throw Debugging.todo();
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
			throw Debugging.todo();
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
			throw Debugging.todo();
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
			throw Debugging.todo();
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
			throw Debugging.todo();
		}
	},
	
	/** Field information. */
	STATIC_CLASS_FIELDS
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
	
	/** Method information. */
	STATIC_CLASS_METHODS
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
	
	/** Linkage. */
	STATIC_LINKAGE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
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
