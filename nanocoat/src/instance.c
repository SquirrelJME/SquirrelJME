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

sjme_jint sjme_nvm_fieldValueSize(
	sjme_attrInValue sjme_extendedTypeId extendedType,
	sjme_attrInPositiveNonZero sjme_jint n)
{
	sjme_jint baseSize;

	if (extendedType < 0 || extendedType >= SJME_NUM_EXTENDED_JAVA_TYPE_IDS ||
		n < 0)
		return -1;
	
	if (extendedType == SJME_JAVA_TYPE_ID_OBJECT)
		baseSize = sizeof(sjme_nvm_fieldObject);
	else
		baseSize = sjme_nvm_typeMul[extendedType];
	
	/* Base size is the offset of where values start */
	return (baseSize * n) +
		offsetof(sjme_nvm_fieldValues, values) +
		offsetof(sjme_nvm_rawFieldValues, l);
}

sjme_jint sjme_nvm_instance_calcIdentityHash(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrInValue void* pointer)
{
	sjme_jint base;
	
	/* Use random base PRNG from task. */
	base = 0;
	if (inTask != NULL)
		base = sjme_random_nextIntR(&inTask->idHash);
	
	/* Then based on the pointer. */
#if defined(SJME_CONFIG_HAS_POINTER64)
	return (sjme_jint)(base + (((sjme_intPointer)pointer) ^
		((((sjme_intPointer)pointer)) >> 31)));
#else
	return (sjme_jint)(base + (sjme_jint)((sjme_intPointer)pointer));
#endif
}

sjme_errorCode sjme_nvm_instance_countBalanceR(
	sjme_attrInNullable sjme_jobject oldV,
	sjme_attrInNullable sjme_jobject newV
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
#if defined(SJME_CONFIG_DEBUG)
	sjme_alloc_weak weak;
#endif

	/* If these are the same, do nothing. */
	if (oldV == newV)
	{
		/* Must be a valid object. */
		if (oldV != NULL)
			if (!sjme_nvm_isAR(oldV,
				SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
				return SJME_ERROR_INVALID_OBJECT;
		
#if defined(SJME_CONFIG_DEBUG)
		if (oldV != NULL)
		{
			/* Recover the weak reference to get the count. */
			if (sjme_error_is(error = sjme_alloc_weakRefGet(oldV, &weak)))
				return sjme_error_default(error);
				
			/* Debug. */
			sjme_messageR(SJME_DEBUG_FILE_LINE_COPY, SJME_JNI_FALSE,
				"GC LV~0: %p (%s) %d == %d",
				(void*)oldV,
				(oldV->isClass != NULL ?
					sjme_charSeq_tempUtf(oldV->isClass->binaryName) :
					"?"),
				sjme_atomic_sjme_jint_get(&weak->count),
				sjme_atomic_sjme_jint_get(&weak->count));
		}
#endif

		/* Nothing to be done! */
		return SJME_ERROR_NONE;
	}

	/* Count down old value? */
	if (oldV != NULL)
		if (sjme_error_is(error = sjme_nvm_instance_countDownR(oldV
			SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)))
			return sjme_error_default(error);

	/* Count up new value? */
	if (newV != NULL)
		if (sjme_error_is(error = sjme_nvm_instance_countUpR(newV
			SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)))
			return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_countDownR(
	sjme_attrInNotNull sjme_jobject object
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_alloc_weak weak;
	sjme_errorCode error;
	
	if (object == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;

	/* Must be a valid object type. */
	if (!sjme_nvm_isAR(object, SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
		return SJME_ERROR_INVALID_OBJECT;
	
	/* This must be a valid weak as well! */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefGet(object, &weak)))
		return sjme_error_default(error);

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageR(SJME_DEBUG_FILE_LINE_COPY, SJME_JNI_FALSE,
		"GC DN-1: %p (%s) %d -> %d",
		(void*)object,
		(object->isClass != NULL ?
			sjme_charSeq_tempUtf(object->isClass->binaryName) : "?"),
		sjme_atomic_sjme_jint_get(&weak->count) + 1,
		sjme_atomic_sjme_jint_get(&weak->count));
#endif

	/* Reduce the count on this. */
	if (sjme_error_is(error = sjme_alloc_weakUnRef(object)) ||
		weak == NULL)
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_countUpR(
	sjme_attrInNotNull sjme_jobject object
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_alloc_weak weak;
	sjme_errorCode error;
	
	if (object == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;

	/* Must be a valid object type. */
	if (!sjme_nvm_isAR(object, SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
		return SJME_ERROR_INVALID_OBJECT;

	/* This must be a valid weak as well! */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRef(object, &weak)) ||
		weak == NULL)
		return sjme_error_default(error);

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_messageR(SJME_DEBUG_FILE_LINE_COPY, SJME_JNI_FALSE,
		"GC UP+1: %p (%s) %d -> %d",
		(void*)object,
		(object->isClass != NULL ?
			sjme_charSeq_tempUtf(object->isClass->binaryName) : "?"),
		sjme_atomic_sjme_jint_get(&weak->count) - 1,
		sjme_atomic_sjme_jint_get(&weak->count));
#endif

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_nvm_rawFieldValue* sjme_nvm_instance_fieldAccessor(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field)
{
#define NUM_VOIDLESS 4
	sjme_threadLocal(sjme_nvm_rawFieldValue, voidless[NUM_VOIDLESS]);
	sjme_threadLocal(sjme_jint, voidlessNext);

	/* If neither are valid, treat this as a bad memory read/write. */
	if (instance == NULL || field == NULL)
		goto fail_voidless;

	/* Static field? */
	if (field->flags.member.isStatic)
	{
		/* Cannot read/write to non-classes. */
		if (!sjme_nvm_isAR(instance, SJME_NVM_STRUCT_CLASS_INSTANCE))
			goto fail_voidless;

		/* Wrong class? */
		if (instance != (sjme_jobject)field->member.inClass)
			goto fail_voidless;

		/* Values is based on the static chunk. */
		sjme_message("STATIC %p->%p + %d",
			(void*)instance, ((sjme_jclass)instance)->staticChunk,
			field->pointerOffset);
		return SJME_POINTER_OFFSET(((sjme_jclass)instance)->staticChunk,
			field->pointerOffset);
	}

	/* Instance field? */
	else
	{
		/* Can only read/write plain objects. */
		if (!sjme_nvm_isAR(instance, SJME_NVM_STRUCT_OBJECT_INSTANCE))
			goto fail_voidless;
		
		/* Value is based on the object itself, from the basis of */
		/* its allocation size. */
		sjme_message("INSTANCE %p + %d",
			(void*)instance, field->pointerOffset);
		return SJME_POINTER_OFFSET((sjme_pointer)instance,
			field->pointerOffset);
	}
	
	/* Fallback to read/write some kind of valid memory. */
fail_voidless:
	memset(&voidless, 0, sizeof(voidless));
	return &voidless[(voidlessNext++) & (NUM_VOIDLESS - 1)];
#undef NUM_VOIDLESS
}

sjme_errorCode sjme_nvm_instance_initFields(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_pointer chunk,
	sjme_attrInNotNull sjme_list_sjme_jfieldID* fields,
	sjme_attrInNotNull sjme_nvm_jclass_fields* placements)
{
	sjme_errorCode error;
	sjme_jint i, n;
	sjme_jfieldID field;
	sjme_nvm_jfieldAccessFunc accessor;
	sjme_nvm_rawFieldValue* direct;
	sjme_nvm_class_fieldConstVal* constVal;

	if (contextThread == NULL || instance == NULL || chunk == NULL ||
		fields == NULL || placements == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Always use the default accessor to write initial field values. */
	accessor = SJME_T_K(contextThread)->globals.accessor;

	/* Since we have bound all static fields, we can just use that. */
	for (i = 0, n = fields->length; i < n; i++)
	{
		/* Operate on this field. */
		field = fields->elements[i];

		/* If there is no actual constant value to set, skip. */
		constVal = &field->info->constVal;
		if (constVal->type >= SJME_NUM_JAVA_TYPE_IDS)
			continue;
		
		/* Get direct pointer to the data. */
		direct = accessor(instance, field);
		if (direct == NULL)
			return SJME_ERROR_FIELD_NOT_DIRECT;

		/* If a string, it needs to be initialized as a string object. */
		if (constVal->value.string != NULL &&
			field->info->javaType == SJME_JAVA_TYPE_ID_OBJECT)
		{
			/* Initialize. */
			direct->l.p = NULL;
			if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfP(
				contextThread, SJME_AS_JSTRINGP(&direct->l),
				constVal->value.string)) || direct->l.p == NULL)
				return sjme_error_vmError(contextThread,
					sjme_error_defaultOr(error,
						SJME_ERROR_STATIC_STRING_INIT));

				/* Count up as this exists in a field. */
				if (sjme_error_is(error = sjme_nvm_instance_countUp(
					direct->l.p)))
					return sjme_error_vmError(contextThread, error);

			/* Set check value. */
			direct->l.check = direct->l.p->identityHash;
		}

		/* Copy value directly is primitive. */
		else if (field->info->javaType != SJME_JAVA_TYPE_ID_OBJECT)
			memmove(direct, &constVal->value.java,
				sjme_nvm_typeMul[field->info->javaType]);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_initFieldsChunk(
	sjme_attrInNotNull sjme_pointer chunk,
	sjme_attrInNotNull sjme_nvm_jclass_fields* placements)
{
	sjme_extendedTypeId type;
	sjme_nvm_fieldValues* into;
	
	if (chunk == NULL || placements == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Placements are calculated for each type. */
	for (type = 0; type < SJME_NUM_EXTENDED_JAVA_TYPE_IDS; type++)
	{
		/* If there are no fields, ignore. */
		if (placements->count[type] == 0)
			continue;
		
		/* Determine the base offset to write at. */
		into = SJME_POINTER_OFFSET(chunk, placements->offset[type]);

		/* Set details for the partition. */
		into->type = type;
		into->length = placements->count[type];
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_fieldAccessStack(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jfieldID fieldId,
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jvalueTyped* stackType,
	sjme_attrInValue sjme_jboolean isPut)
{
	sjme_errorCode error;
	sjme_nvm_rawFieldValue* direct;
	sjme_nvm_jfieldAccessFunc accessor;

	if (contextThread == NULL || fieldId == NULL || stackType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (instance == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	/* Obtain accessor for this field. */
	if (fieldId->accessor != NULL)
		accessor = fieldId->accessor;
	else
		accessor = SJME_T_K(contextThread)->globals.accessor;

	/* There must be an accessor. */
	if (accessor == NULL)
		return SJME_ERROR_FIELD_NOT_DIRECT;

	/* Direct access. */
	direct = accessor(instance, fieldId);
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
		
		if (isPut)
		{
			/* Balance garbage count. */
			if (sjme_error_is(error = sjme_nvm_instance_countBalance(
				direct->l.p, stackType->v.l)))
				return sjme_error_default(error);
			
			/* Put in the new value. */
			direct->l.p = stackType->v.l;

			/* Set new check value. */
			if (stackType->v.l == NULL)
				direct->l.check = 0;
			else
				direct->l.check = stackType->v.l->identityHash;
		}
		else
			stackType->v.l = direct->l.p;
	}
	
	/* No promotion/demotion needed. */
	else if (fieldId->extendedType < SJME_NUM_JAVA_TYPE_IDS)
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

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_monitorEnter(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance)
{
	sjme_errorCode error;
	
	if (contextThread == NULL || instance == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Grab the lock on the object. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&instance->common.lock)))
		return sjme_error_vmError(contextThread, error);

	/* Count up the monitor since we do have the lock. */
	sjme_atomic_sjme_jint_getAdd(&instance->monitorCount, 1);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_monitorExit(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance)
{
	sjme_errorCode error;
	
	if (contextThread == NULL || instance == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* There are no monitor locks on this instance? */
	if (sjme_atomic_sjme_jint_get(&instance->monitorCount) < 0)
		return SJME_ERROR_NOT_LOCK_OWNER;
	
	/* Release the lock on the object. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&instance->common.lock, NULL)))
		return sjme_error_vmError(contextThread, error);

	/* We did a successful release, so count the locks down. */
	sjme_atomic_sjme_jint_getAdd(&instance->monitorCount, -1);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_objectArrayNew(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jarray* outObject,
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInPositive sjme_jint arrayLength)
{
	sjme_errorCode error;
	sjme_jclass arrayClass;
	sjme_jarray result;
	sjme_jint allocSize;
	sjme_cchar buf[SJME_NVM_CLASS_NAME_LIMIT];
	
	if (contextThread == NULL || outObject == NULL || componentType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (arrayLength < 0)
		return sjme_error_vmError(contextThread,
			SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* Cannot be void. */
	if (componentType->arrayTypeId == SJME_JAVA_TYPE_ID_VOID)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Determine the allocation size. */
	allocSize = sizeof(*result);
	if (componentType->arrayTypeId == SJME_BASIC_TYPE_ID_BOOLEAN)
		allocSize += (arrayLength / 8) + 1;
	else
		allocSize += (sjme_nvm_typeMul[componentType->arrayTypeId] *
			arrayLength);

	/* Determine array type class name. */
	memset(buf, 0, sizeof(buf));
	snprintf(buf, SJME_NVM_CLASS_NAME_LIMIT - 1,
		"[%s", sjme_charSeq_tempUtf(componentType->binaryName));

	/* Locate array type class. */
	arrayClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadFU(
		SJME_F_CL(contextThread),
		&arrayClass, contextThread, buf, SJME_JNI_TRUE)) ||
		arrayClass == NULL)
		return sjme_error_vmError(contextThread, error);
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(contextThread,
		allocSize, SJME_NVM_STRUCT_ARRAY_INSTANCE,
		SJME_AS_JOBJECTP(&result), arrayClass)) || result == NULL)
		return sjme_error_vmError(contextThread, error);
	
	/* Setup array. */
	result->type = componentType->arrayTypeId;
	result->length = arrayLength;

	/* Success! */
	*outObject = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_objectArrayNewT(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jarray* outObject,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_basicTypeId componentType,
	sjme_attrInPositive sjme_jint arrayLength)
{
	sjme_errorCode error;
	sjme_jclass componentClass;
	sjme_nvm_task_commonClassId commonId;
	
	if (contextThread == NULL || outObject == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (componentType < 0 || componentType >= SJME_NUM_BASIC_TYPE_IDS ||
		componentType == SJME_JAVA_TYPE_ID_OBJECT ||
		componentType == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE ||
		componentType == SJME_JAVA_TYPE_ID_SHORT_OR_CHAR ||
		componentType == SJME_BASIC_TYPE_ID_VOID)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (arrayLength < 0)
		return sjme_error_vmError(contextThread,
			SJME_ERROR_NEGATIVE_ARRAY_SIZE);
	
	/* Determine the common ID to use. */
	switch (componentType)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_BOOLEAN;
			break;

		case SJME_BASIC_TYPE_ID_BYTE:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_BYTE;
			break;

		case SJME_BASIC_TYPE_ID_SHORT:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_SHORT;
			break;

		case SJME_BASIC_TYPE_ID_CHARACTER:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_CHARACTER;
			break;

		case SJME_BASIC_TYPE_ID_INTEGER:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_INTEGER;
			break;

		case SJME_BASIC_TYPE_ID_LONG:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_LONG;
			break;

		case SJME_BASIC_TYPE_ID_FLOAT:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_FLOAT;
			break;

		case SJME_BASIC_TYPE_ID_DOUBLE:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_DOUBLE;
			break;

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Load the component class. */
	componentClass = NULL;
	if (sjme_error_is(error = sjme_nvm_task_commonClass(
		contextThread, commonId, &componentClass, SJME_JNI_TRUE)) || componentClass == NULL)
		return sjme_error_vmError(contextThread, error);

	/* Forward initialize. */
	return sjme_nvm_instance_objectArrayNew(contextThread, outObject,
		componentClass, arrayLength);
}

sjme_errorCode sjme_nvm_instance_objectNew(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNegativeOnePositive sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_jclass inClass)
{
	sjme_errorCode error;
	sjme_jobject result;
	
	if (contextThread == NULL || outObject == NULL || inClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType < 0 || inType >= SJME_NVM_NUM_STRUCT || allocSize < -1 ||
		allocSize == 0 ||
		(inType != SJME_NVM_STRUCT_OBJECT_INSTANCE && allocSize <= 0))
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Make sure the class is initialized. */
	if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(inClass,
		contextThread)))
		return sjme_error_vmError(contextThread, error);
	
	/* Need to calculate/determine object size? */
	if (inType == SJME_NVM_STRUCT_OBJECT_INSTANCE && allocSize < 0)
	{
		/* Cannot be these types as they are allocated implicitly by */
		/* the virtual machine. */
		if (inClass == sjme_nvm_task_commonClassR(contextThread,
			SJME_NVM_TASK_COMMON_CLASS_CLASS))
			return SJME_ERROR_INVALID_ARGUMENT;
		
		/* Remap @c String . */
		else if (inClass == sjme_nvm_task_commonClassR(contextThread,
			SJME_NVM_TASK_COMMON_CLASS_STRING))
		{
			inType = SJME_NVM_STRUCT_STRING_INSTANCE;
			allocSize = sizeof(sjme_jstringBase);
		}
		
		/* Remap @c Reference based classes, however specifically */
		/* limit to a small selection of reference based classes. */
		/* This is so that any aliases are treated the same regardless. */
		else if (inClass == sjme_nvm_task_commonClassR(contextThread,
				SJME_NVM_TASK_COMMON_CLASS_REFERENCE_PHANTOM) ||
			inClass == sjme_nvm_task_commonClassR(contextThread,
				SJME_NVM_TASK_COMMON_CLASS_REFERENCE_SOFT) ||
			inClass == sjme_nvm_task_commonClassR(contextThread,
				SJME_NVM_TASK_COMMON_CLASS_REFERENCE_WEAK))
		{
			inType = SJME_NVM_STRUCT_WEAK_INSTANCE;
			allocSize = sizeof(sjme_jweakBase);
		}

		/* Otherwise, object storage is pre-calculated. */
		else
			allocSize =
				inClass->fields[SJME_NVM_CLASS_MEMBER_INSTANCE].allocSize;
	}
	
	/* Setup object. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(contextThread->inState,
		allocSize, inType,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		return sjme_error_vmError(contextThread, error);
	
	/* Setup object. */
	result->isClass = inClass;
	result->identityHash = sjme_nvm_instance_calcIdentityHash(
		SJME_T_K(contextThread), result);
	
	/* Success! */
	*outObject = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_objectNewBracket(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject)
{
	sjme_nvm_task_commonClassId commonId;
	sjme_jint allocSize;
	
	if (contextThread == NULL || outObject == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Determine size and type. */
	switch (inType)
	{
		case SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE:
			commonId = SJME_NVM_TASK_COMMON_CLASS_JAR_PACKAGE;
			allocSize = sizeof(sjme_jbracketJarPackageBase);
			break;

		case SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE:
			commonId = SJME_NVM_TASK_COMMON_CLASS_PIPE;
			allocSize = sizeof(sjme_jbracketPipeBase);
			break;
		
		case SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE:
			commonId = SJME_NVM_TASK_COMMON_CLASS_TRACE_POINT;
			allocSize = sizeof(sjme_jbracketTraceBase);
			break;
			
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Allocate. */
	return sjme_nvm_instance_objectNew(contextThread, allocSize,
		inType, outObject, sjme_nvm_task_commonClassR(contextThread,
			commonId));
}

sjme_errorCode sjme_nvm_instance_objectNewN(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_charSeq inClass)
{
	sjme_errorCode error;
	sjme_jclass classy;
	
	if (contextThread == NULL || outObject == NULL || inClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType < 0 || inType >= SJME_NVM_NUM_STRUCT)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Lookup the class first. */
	classy = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(contextThread), &classy, contextThread,
		inClass, SJME_JNI_FALSE)) || classy == NULL)
		return sjme_error_vmError(contextThread, error);

	/* Forward call. */
	return sjme_nvm_instance_objectNew(contextThread, allocSize, inType,
		outObject, classy);
}

sjme_errorCode sjme_nvm_instance_objectNewNU(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_lpcstr inClass)
{
	sjme_errorCode error;
	sjme_charSeqStatic tempSeq;

	if (contextThread == NULL || outObject == NULL || inClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup wrapped sequence. */
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&tempSeq,
		inClass, 0, -1)))
		return sjme_error_default(error);

	/* Forward. */
	return sjme_nvm_instance_objectNewN(contextThread, allocSize, inType,
		outObject, &tempSeq);
}
