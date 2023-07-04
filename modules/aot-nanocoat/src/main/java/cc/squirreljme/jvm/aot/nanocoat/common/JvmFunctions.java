// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

import cc.squirreljme.c.CFunctionType;
import cc.squirreljme.c.CPrimitiveType;
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.c.std.CFunctionProvider;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Virtual machine functions for NanoCoat.
 *
 * @since 2023/06/24
 */
public enum JvmFunctions
	implements CFunctionProvider
{
	/** Method code. */
	METHOD_CODE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/25
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("methodCode",
				JvmTypes.JBOOLEAN.type(),
				CVariable.of(JvmTypes.VMSTATE.type().pointerType(),
					"currentState"),
				CVariable.of(JvmTypes.VMTHREAD.type().pointerType(),
					"currentThread"));
		}
	},
	
	/** Count down a reference. */
	NVM_COUNT_REFERENCE_DOWN
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_countReferenceDown",
				JvmTypes.JBOOLEAN.type(),
				CVariable.of(JvmTypes.VMSTATE.type().pointerType(),
					"currentState"),
				CVariable.of(JvmTypes.JOBJECT.type().pointerType(),
					"reference"));
		}
	},
	
	/** New instance. */
	NVM_NEW_INSTANCE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_newInstance",
				JvmTypes.JOBJECT.type().pointerType(),
				CVariable.of(JvmTypes.VMSTATE.type().pointerType(),
					"state"),
				CVariable.of(CPrimitiveType.CHAR_STAR.constType(),
					"type"));
		}
	},
	
	/** Invoke special method. */
	NVM_INVOKE_SPECIAL
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_invokeSpecial",
				JvmTypes.JBOOLEAN.type(),
				CVariable.of(JvmTypes.VMSTATE.type().pointerType(),
					"state"),
				CVariable.of(JvmTypes.VMTHREAD.type().pointerType(),
					"thread"),
				CVariable.of(JvmTypes.STATIC_LINKAGE.type(CStructType.class)
					.member("data").type(CStructType.class)
					.member("invokeSpecial").type.pointerType(),
					"linkage"));
		}
	},
	
	/** Copy reference in frame. */
	NVM_LOCAL_REFERENCE_PUSH
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_localReferencePush",
				JvmTypes.JBOOLEAN.type(),
				CVariable.of(JvmTypes.VMFRAME.type().pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JINT.type(), "index"));
		}
	},
	
	/** Return from method. */
	NVM_RETURN_FROM_METHOD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_returnFromMethod",
				JvmTypes.JBOOLEAN.type(),
				CVariable.of(JvmTypes.VMSTATE.type().pointerType(),
					"state"));
		}
	},
	
	/** Pop any from stack. */
	NVM_STACK_ANY_POP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackAnyPop",
				JvmTypes.JBOOLEAN.type(),
				CVariable.of(JvmTypes.VMFRAME.type().pointerType(),
					"frame"),
				CVariable.of(JvmTypes.ANY.type().pointerType(),
					"output"));
		}
	},
	
	/** Push any onto stack. */
	NVM_STACK_ANY_PUSH
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackAnyPush",
				JvmTypes.JBOOLEAN.type(),
				CVariable.of(JvmTypes.VMFRAME.type().pointerType(),
					"frame"),
				CVariable.of(JvmTypes.ANY.type().pointerType(),
					"input"));
		}
	},
	
	/** Pop from stack. */
	NVM_STACK_REFERENCE_POP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackReferencePop",
				JvmTypes.JOBJECT.type().pointerType(),
				CVariable.of(JvmTypes.VMFRAME.type().pointerType(),
					"frame"));
		}
	},
	
	/** Push to stack. */
	NVM_STACK_REFERENCE_PUSH
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackReferencePush",
				JvmTypes.JBOOLEAN.type(),
				CVariable.of(JvmTypes.VMFRAME.type().pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JOBJECT.type().pointerType(),
					"object"));
		}
	},
	
	/* End. */
	;
	
	/** Function cache. */
	private volatile Reference<CFunctionType> _function;
	
	/**
	 * Builds the given function.
	 * 
	 * @return The built function.
	 * @since 2023/06/24
	 */
	abstract CFunctionType __build();
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CFunctionType function()
	{
		Reference<CFunctionType> ref = this._function;
		CFunctionType rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = this.__build();
			this._function = new WeakReference<>(rv);
		}
		
		return rv;
	}
}
