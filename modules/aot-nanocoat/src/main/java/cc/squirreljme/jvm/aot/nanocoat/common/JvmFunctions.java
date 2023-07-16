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
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMSTATE.pointerType(),
					"currentState"),
				CVariable.of(JvmTypes.VMTHREAD.pointerType(),
					"currentThread"));
		}
	},
	
	/** Load from array. */
	NVM_ARRAY_LOAD_INTO_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_arrayLoadIntoTemp",
				JvmTypes.TEMP_INDEX,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.PRIMITIVE_TYPE,
					"primitiveType"),
				CVariable.of(JvmTypes.JOBJECT.pointerType(),
					"reference"),
				CVariable.of(JvmTypes.JINT,
					"index"));
		}
	},
	
	/** Store into array. */
	NVM_ARRAY_STORE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_arrayStore",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.PRIMITIVE_TYPE,
					"primitiveType"),
				CVariable.of(JvmTypes.JOBJECT.pointerType(),
					"reference"),
				CVariable.of(JvmTypes.JINT,
					"index"));
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
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMSTATE.pointerType(),
					"state"),
				CVariable.of(JvmTypes.JOBJECT.pointerType(),
					"reference"));
		}
	},
	
	/** Get value from field and place into temp. */
	NVM_FIELD_GET_TO_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_fieldGetToTemp",
				JvmTypes.TEMP_INDEX.type(),
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JOBJECT.pointerType(),
					"instance"),
				JvmFunctions.__linkage("fieldAccess",
					"linkage"));
		}
	},
	
	/** Put value into field. */
	NVM_FIELD_PUT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_fieldPut",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JOBJECT.pointerType(),
					"instance"),
				JvmFunctions.__linkage("fieldAccess",
					"linkage"),
				CVariable.of(JvmTypes.ANY.type().pointerType(),
					"value"));
		}
	},
	
	/** Invoke normal method. */
	NVM_INVOKE_NORMAL
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_invokeNormal",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMSTATE.pointerType(),
					"state"),
				CVariable.of(JvmTypes.VMTHREAD.pointerType(),
					"thread"),
				JvmFunctions.__linkage("invokeNormal",
					"linkage"));
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
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMSTATE.pointerType(),
					"state"),
				CVariable.of(JvmTypes.VMTHREAD.pointerType(),
					"thread"),
				JvmFunctions.__linkage("invokeSpecial",
					"linkage"));
		}
	},
	
	/** Pop integer from the stack to local variable. */
	NVM_LOCAL_POP_INTEGER
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_localPopInteger",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JINT, "index"));
		}
	},
	
	/** Pop reference from the stack to local variable. */
	NVM_LOCAL_POP_REFERENCE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_localPopReference",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JINT, "index"));
		}
	},
	
	/** Push integer value. */
	NVM_LOCAL_PUSH_INTEGER
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_localPushInteger",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JINT, "index"));
		}
	},
	
	/** Push local variable reference to the stack. */
	NVM_LOCAL_PUSH_REFERENCE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/24
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_localPushReference",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JINT, "index"));
		}
	},
	
	/** Lookup a string and store into temporary index. */
	NVM_LOOKUP_STRING_INTO_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_lookupStringIntoTemp",
				JvmTypes.TEMP_INDEX.type(),
				CVariable.of(JvmTypes.VMTHREAD.pointerType(),
					"thread"),
				CVariable.of(CPrimitiveType.CHAR_STAR, "string"));
		}
	},
	
	/** New array into temporary variable. */
	NVM_NEW_ARRAY_INTO_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/05
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_newArrayIntoTemp",
				JvmTypes.TEMP_INDEX.type(),
				CVariable.of(JvmTypes.VMTHREAD.pointerType(),
					"thread"),
				CVariable.of(CPrimitiveType.CHAR_STAR.constType(),
					"componentType"),
				CVariable.of(JvmTypes.JINT,
					"length"));
		}
	},
	
	/** New instance into temporary. */
	NVM_NEW_INSTANCE_INTO_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_newInstanceIntoTemp",
				JvmTypes.TEMP_INDEX.type(),
				CVariable.of(JvmTypes.VMTHREAD.pointerType(),
					"thread"),
				CVariable.of(CPrimitiveType.CHAR_STAR.constType(),
					"type"));
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
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMSTATE.pointerType(),
					"state"));
		}
	},
	
	/** Pop any from stack. */
	NVM_STACK_POP_ANY
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPopAny",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.ANY.type().pointerType(),
					"output"));
		}
	},
	
	/** Pop a reference on the stack to a temporary variable. */
	NVM_STACK_POP_ANY_TO_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/15
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPopAnyToTemp",
				JvmTypes.TEMP_INDEX.type(),
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"));
		}
	},
	
	/** Pop integer from stack. */
	NVM_STACK_POP_INTEGER
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPopInteger",
				JvmTypes.JINT,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"));
		}
	},
	
	/** Pop reference from stack. */
	NVM_STACK_POP_REFERENCE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPopReference",
				JvmTypes.JOBJECT.pointerType(),
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"));
		}
	},
	
	/** Pop reference from stack, then throw it. */
	NVM_STACK_POP_REFERENCE_THEN_THROW
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of(
				"sjme_nvm_stackPopReferenceThenThrow",
				JvmTypes.JBOOLEAN.pointerType(),
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"));
		}
	},
	
	/** Pop a reference on the stack to a temporary variable. */
	NVM_STACK_POP_REFERENCE_TO_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/15
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPopReferenceToTemp",
				JvmTypes.TEMP_INDEX.type(),
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"));
		}
	},
	
	/** Push any onto stack. */
	NVM_STACK_PUSH_ANY
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPushAny",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.ANY.type().pointerType(),
					"input"));
		}
	},
	
	/** Push any to stack from temporary. */
	NVM_STACK_PUSH_ANY_FROM_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/16
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPushAnyFromTemp",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.TEMP_INDEX,
					"input"));
		}
	},
	
	/** Push integer to stack. */
	NVM_STACK_PUSH_INTEGER
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/04
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPushInteger",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JINT,
					"value"));
		}
	},
	
	/** Push to stack. */
	NVM_STACK_PUSH_REFERENCE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_stackPushReference",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.JOBJECT.pointerType(),
					"object"));
		}
	},
	
	/** Push reference to stack from temporary index. */
	NVM_STACK_PUSH_REFERENCE_FROM_TEMP
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/015
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of(
				"sjme_nvm_stackPushReferenceFromTemp",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"),
				CVariable.of(JvmTypes.TEMP_INDEX.type(),
					"tempIndex"));
		}
	},
	
	/** Discard temporary values. */
	NVM_TEMP_DISCARD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/03
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_tempDiscard",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"));
		}
	},
	
	/** Execute throw from method. */
	NVM_THROW_EXECUTE
	{
	 /**
		 * {@inheritDoc}
		 * @since 2023/07/15
		 */
		@Override
		CFunctionType __build()
		{
			return CFunctionType.of("sjme_nvm_throwExecute",
				JvmTypes.JBOOLEAN,
				CVariable.of(JvmTypes.VMFRAME.pointerType(),
					"frame"));
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
	
	/**
	 * Calculates the variable for the given linkage.
	 * 
	 * @param __type The type to access.
	 * @param __name The name of the variable.
	 * @return The variable for the given linkage.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	static final CVariable __linkage(String __type, String __name)
		throws NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		return CVariable.of(JvmTypes.STATIC_LINKAGE.type(CStructType.class)
			.member("data").type(CStructType.class)
			.member(__type).type.pointerType(),
			__name);
	}
}
