/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/task.h"
#include "lib/scritchui/scritchuiConst.h"

static sjme_nvm_value* sjme_nvm_instance_fieldAccessorZZ(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field,
	sjme_attrInOutNotNull sjme_nvm_valueVolatile* volatileStore)
{
#define NUM_VOIDLESS 4
	sjme_threadLocal(sjme_nvm_value, voidless[NUM_VOIDLESS]);
	sjme_threadLocal(sjme_jint, voidlessNext);
	sjme_nvm_value* result;
	
	/* Fallback to read/write some kind of valid memory. */
	result = &voidless[(voidlessNext++) & (NUM_VOIDLESS - 1)];
	memset(result, 0, sizeof(*result));
	
	/* Return it. */
	return result;
#undef NUM_VOIDLESS
}

sjme_nvm_value* sjme_nvm_instance_fieldAccessorNI(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field,
	sjme_attrInOutNotNull sjme_nvm_valueVolatile* volatileStore)
{
	sjme_errorCode error;
	
	if (instance == NULL || field == NULL || volatileStore == NULL)
		goto fail_voidless;
	
	/* Can only read/write plain objects. */
	if (!sjme_nvm_isAR(instance, SJME_NVM_STRUCT_OBJECT_INSTANCE))
		goto fail_voidless;
	
#if defined(SJME_CONFIG_DEBUG_FIELD)
	/* Debug. */
	sjme_message("INSTANCE %p + %d",
		instance, field->pointerOffset);
#endif
	
	/* Value is based on the object itself, from the basis of */
	/* its allocation size. */
	return sjme_po((sjme_pointer)instance,
		field->pointerOffset);
	
fail_voidless:
	return sjme_nvm_instance_fieldAccessorZZ(NULL, NULL, NULL);
}
	
sjme_nvm_value* sjme_nvm_instance_fieldAccessorNS(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field,
	sjme_attrInOutNotNull sjme_nvm_valueVolatile* volatileStore)
{
	sjme_errorCode error;
	
	if (instance == NULL || field == NULL || volatileStore == NULL)
		goto fail_voidless;
	
	/* Cannot read/write to non-classes. */
	if (!sjme_nvm_isAR(instance, SJME_NVM_STRUCT_CLASS_INSTANCE))
		goto fail_voidless;

	/* Wrong class? */
	if (instance != (sjme_jobject)sjme_atomic_g(sjme_jclass,
		&field->member.inClass))
		goto fail_voidless;

#if defined(SJME_CONFIG_DEBUG_FIELD)
	/* Debug. */
	sjme_message("STATIC %p->%p + %d",
		instance, ((sjme_jclass)instance)->staticChunk,
		field->pointerOffset);
#endif

	/* Values is based on the static chunk. */
	return sjme_po(((sjme_jclass)instance)->staticChunk,
		field->pointerOffset);
	
fail_voidless:
	return sjme_nvm_instance_fieldAccessorZZ(NULL, NULL, NULL);
}

static sjme_nvm_value* sjme_nvm_instance_fieldAccessorVV(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field,
	sjme_attrInOutNotNull sjme_nvm_valueVolatile* volatileStore,
	sjme_attrInNotNull sjme_pointer rawP)
{
	sjme_errorCode error;
	
	if (instance == NULL || field == NULL || volatileStore == NULL)
		goto fail_voidless;
	
	/* Atomic read long. */
	if (field->javaType == SJME_JAVA_TYPE_ID_LONG ||
		field->javaType == SJME_JAVA_TYPE_ID_DOUBLE)
	{
#if !defined(SJME_CONFIG_HAS_NO_JLONG_MATH)
		volatileStore->value.v.j.full = sjme_atomic_g(sjme_jlongNative, rawP);
#else
#if defined(SJME_CONFIG_HAS_BIG_ENDIAN)
		volatileStore->value.v.j.part.hi =
			sjme_atomic_g(sjme_jint, rawP);
		volatileStore->value.v.j.part.lo =
			sjme_atomic_g(sjme_jint, sjme_po(rawP, 4));
#else
		volatileStore->value.v.j.part.hi =
			sjme_atomic_g(sjme_jint, sjme_po(rawP, 4));
		volatileStore->value.v.j.part.lo =
			sjme_atomic_g(sjme_jint, rawP);
#endif
#endif
	}
	
	/* Atomic read pointer. */
	else if (field->javaType == SJME_JAVA_TYPE_ID_OBJECT)
	{
		volatileStore->value.l.check = sjme_atomic_g(sjme_jint, 
			(sjme_pointer)sjme_po(rawP, 
				offsetof(sjme_nvm_valueObject, check)));
		volatileStore->value.l.p = sjme_atomic_g(sjme_pointer,
			(sjme_pointer)sjme_po(rawP, 
				offsetof(sjme_nvm_valueObject, p)));
	}
		
	/* Atomic read integer. */
	else
		volatileStore->value.v.i = sjme_atomic_g(sjme_jint, rawP);
		
	/* Always use the volatile storage. */
	return &volatileStore->value;
	
fail_voidless:
	return sjme_nvm_instance_fieldAccessorZZ(NULL, NULL, NULL);
}
static sjme_nvm_value* sjme_nvm_instance_fieldAccessorVC(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field,
	sjme_attrInOutNotNull sjme_nvm_valueVolatile* volatileStore,
	sjme_attrInNotNull sjme_pointer rawP)
{
	sjme_errorCode error;
	
	if (instance == NULL || field == NULL || volatileStore == NULL)
		return NULL;
	
	/* Atomic write long. */
	if (field->javaType == SJME_JAVA_TYPE_ID_LONG ||
		field->javaType == SJME_JAVA_TYPE_ID_DOUBLE)
	{
#if !defined(SJME_CONFIG_HAS_NO_JLONG_MATH)
		sjme_atomic_s(sjme_jlongNative, rawP,
			volatileStore->value.v.j.full);
#else
#if defined(SJME_CONFIG_HAS_BIG_ENDIAN)
		sjme_atomic_s(sjme_jint, rawP, 
			volatileStore->value.v.j.part.hi);
		sjme_atomic_s(sjme_jint, sjme_po(rawP, 4),
			volatileStore->value.v.j.part.lo);
#else
		sjme_atomic_s(sjme_jint, sjme_po(rawP, 4), 
			volatileStore->value.v.j.part.hi);
		sjme_atomic_s(sjme_jint, rawP,
			volatileStore->value.v.j.part.lo);
#endif
#endif
	}
	
	/* Atomic write pointer. */
	else if (field->javaType == SJME_JAVA_TYPE_ID_OBJECT)
	{
		sjme_atomic_s(sjme_jint, 
			(sjme_pointer)sjme_po(rawP, 
				offsetof(sjme_nvm_valueObject, check)),
			volatileStore->value.l.check);
		sjme_atomic_s(sjme_pointer,
			(sjme_pointer)sjme_po(rawP, 
				offsetof(sjme_nvm_valueObject, p)),
			volatileStore->value.l.p);
	}
		
	/* Atomic write integer. */
	else
		sjme_atomic_s(sjme_jint, rawP, volatileStore->value.v.i);
		
	/* Always null */
	return NULL;
}
	
sjme_nvm_value* sjme_nvm_instance_fieldAccessorVI(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field,
	sjme_attrInOutNotNull sjme_nvm_valueVolatile* volatileStore)
{
	sjme_errorCode error;
	sjme_pointer rawP;
	
	if (instance == NULL || field == NULL || volatileStore == NULL)
		goto fail_voidless;
	
	/* Raw pointer to the data. */
	rawP = sjme_po((sjme_pointer)instance,
		field->pointerOffset);
	
	/* Commit the value to memory. */
	if (volatileStore->commit)
	{
		/* Direct commit. */
		sjme_nvm_instance_fieldAccessorVC(instance, field,
			volatileStore, rawP);
		
		/* Always null. */
		return NULL;
	}
	
	/* Prime the value. */
	else
	{
		/* Clear the volatile storage. */
		memset(volatileStore, 0, sizeof(*volatileStore));
		
		/* Read the value. */
		return sjme_nvm_instance_fieldAccessorVV(instance, field,
			volatileStore, rawP);
	}
	
fail_voidless:
	return sjme_nvm_instance_fieldAccessorZZ(NULL, NULL, NULL);
}
	
sjme_nvm_value* sjme_nvm_instance_fieldAccessorVS(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field,
	sjme_attrInOutNotNull sjme_nvm_valueVolatile* volatileStore)
{
	sjme_errorCode error;
	sjme_pointer rawP;
	
	if (instance == NULL || field == NULL || volatileStore == NULL)
		goto fail_voidless;
	
	/* Cannot read/write to non-classes. */
	if (!sjme_nvm_isAR(instance, SJME_NVM_STRUCT_CLASS_INSTANCE))
		goto fail_voidless;

	/* Wrong class? */
	if (instance != (sjme_jobject)sjme_atomic_g(sjme_jclass,
		&field->member.inClass))
		goto fail_voidless;
	
	/* Raw pointer to the data. */
	rawP = sjme_po(((sjme_jclass)instance)->staticChunk,
		field->pointerOffset);
	
	/* Commit the value to memory. */
	if (volatileStore->commit)
	{
		/* Direct commit. */
		sjme_nvm_instance_fieldAccessorVC(instance, field,
			volatileStore, rawP);
		
		/* Always null. */
		return NULL;
	}
	
	/* Prime the value. */
	else
	{
		/* Clear the volatile storage. */
		memset(volatileStore, 0, sizeof(*volatileStore));
		
		/* Read the value. */
		return sjme_nvm_instance_fieldAccessorVV(instance, field,
			volatileStore, rawP);
	}
	
fail_voidless:
	return sjme_nvm_instance_fieldAccessorZZ(NULL, NULL, NULL);
}

sjme_errorCode sjme_nvm_instance_fieldAccessStack(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jfieldID fieldId,
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jvalueTyped* stackType,
	sjme_attrInValue sjme_jboolean isPut)
{
	sjme_errorCode error;
	sjme_nvm_value* direct;
	sjme_nvm_valueVolatile volatileStore;
	sjme_nvm_jfieldAccessFunc accessor;

	if (contextThread == NULL || fieldId == NULL ||
		stackType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (instance == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	/* Obtain accessor for this field. */
	if (fieldId->accessor != NULL)
		accessor = fieldId->accessor;
	
	/* Otherwise, default to a global accessor. */
	else if (SJME_NVM_ACC_IS(fieldId->flags, STATIC))
	{
		if (SJME_NVM_ACC_IS(fieldId->flags, VOLATILE))
			accessor = SJME_T_K(contextThread)->globals
				.accessor[SJME_NVM_FIELD_ACCESS_VOLATILE_STATIC];
		else
			accessor = SJME_T_K(contextThread)->globals
				.accessor[SJME_NVM_FIELD_ACCESS_NORMAL_STATIC];
	}
	else
	{
		if (SJME_NVM_ACC_IS(fieldId->flags, VOLATILE))
			accessor = SJME_T_K(contextThread)->globals
				.accessor[SJME_NVM_FIELD_ACCESS_VOLATILE_INSTANCE];
		else
			accessor = SJME_T_K(contextThread)->globals
				.accessor[SJME_NVM_FIELD_ACCESS_NORMAL_INSTANCE];
	}

	/* There must be an accessor. */
	if (accessor == NULL)
		return SJME_ERROR_FIELD_NOT_DIRECT;

	/* Direct access. */
	memset(&volatileStore, 0, sizeof(volatileStore));
	direct = accessor(instance, fieldId, &volatileStore);
	if (direct == NULL)
		return SJME_ERROR_FIELD_NOT_DIRECT;

	/* Handling an object. */
	if (fieldId->javaType == SJME_JAVA_TYPE_ID_OBJECT)
	{
		/* Did the object suddenly change in the field? */
		if (direct->l.check != (direct->l.p == NULL ? 0 :
			direct->l.p->identityHash))
			return sjme_error_vmError(contextThread,
				SJME_ERROR_OBJECT_MISMATCHED);
		
		/* Write value. */
		if (isPut)
		{
			/* Set the field value. */
			if (sjme_error_is(error = sjme_nvm_vmField_cisSet(
				direct, fieldId->info->basicType,
				commit, SJME_VLS_JVALUE_TYPED_P(stackType))))
				return sjme_error_vmError(contextThread, error);
		}

		/* Read in value. */
		else
		{
			stackType->v.l = direct->l.p;
		}
	}
	
	/* No promotion/demotion needed. */
	else if (fieldId->extendedType < SJME_NUM_JAVA_TYPE_IDS &&
		fieldId->extendedType != SJME_JAVA_TYPE_ID_OBJECT)
	{
		if (isPut)
			memmove(&direct->v, &stackType->v,
				sjme_nvm_typeMul[fieldId->extendedType]);
		else
			memmove(&stackType->v, &direct->v,
				sjme_nvm_typeMul[fieldId->extendedType]);
	}

	/* Translation is needed. */
	else
	{
		/* Determine where to move to/from. */
		switch (fieldId->basicType)
		{
				/* Force to boolean based value. */
			case SJME_BASIC_TYPE_ID_BOOLEAN:
				if (isPut)
					direct->v.b = (stackType->v.i ? 1 : 0);
				else
					stackType->v.i = (direct->v.b ? 1 : 0);
				break;

				/* Expand to byte. */
			case SJME_BASIC_TYPE_ID_BYTE:
			case SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE:
				if (isPut)
					direct->v.b = (sjme_jbyte)stackType->v.i;
				else
				{
					stackType->v.i = direct->v.b;
					if ((direct->v.b & INT8_C(0x80)) != 0)
						stackType->v.i |= INT32_C(0xFFFFFF00);
				}
				break;

				/* Expand to short. */
			case SJME_BASIC_TYPE_ID_SHORT:
				if (isPut)
					direct->v.s = (sjme_jshort)stackType->v.i;
				else
				{
					stackType->v.i = direct->v.s;
					if ((direct->v.s & INT16_C(0x8000)) != 0)
						stackType->v.i |= INT32_C(0xFFFF0000);
				}
				break;

				/* Limit to char. */
			case SJME_BASIC_TYPE_ID_CHARACTER:
				if (isPut)
					direct->v.c = (sjme_jchar)stackType->v.i;
				else
					stackType->v.i = ((sjme_jint)direct->v.c) &
						INT32_C(0xFFFF);
				break;

			default:
				return SJME_ERROR_INVALID_FIELD_TYPE;
		}
	}
	
	/* Commit value for volatile? */
	if (isPut && SJME_NVM_ACC_IS(fieldId->flags, VOLATILE))
	{
		volatileStore.commit = SJME_JNI_TRUE;
		accessor(instance, fieldId, &volatileStore);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
