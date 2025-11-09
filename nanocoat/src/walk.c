/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/boot.h"
#include "sjme/nvm/walk.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/walkCustom.h"
#include "sjme/nvm/classy.h"

#pragma region(supportMacros)

#define SJME_WALK_SELECT(cType, inStructType) \
	{ \
		sjme_sm(.typeName, SJME_TOKEN_STRING_PP(cType)), \
		sjme_sm(.typeId, inStructType), \
		sjme_sm(.size, sizeof(cType)), \
		sjme_sm(.steps, SJME_TOKEN_PASTE4_PP(sjme_nvm_walk_decl_, \
			cType, _, inStructType)), \
	}

#define SJME_WALK_SELECT_END() \
	{ \
		sjme_sm(.typeName, NULL), \
		sjme_sm(.typeId, 0), \
		sjme_sm(.size, -1), \
		sjme_sm(.steps, NULL), \
	}

/** Begin walk structure. */
#define SJME_WALK_BEGIN(structType) \
	const sjme_nvm_walk_step SJME_TOKEN_PASTE4_PP(sjme_nvm_walk_decl_, \
		SJME_WALK_CURRENT, _, structType)[] = \
	{ \

/** Walk step, full definition. */
#define SJME_WS_FULL(inMemberName, inIsPointer, \
	inJavaType, inNvmId, inMemberSize, customWalkFunc) \
	{ \
		sjme_sm(.offset, offsetof(SJME_WALK_CURRENT, inMemberName)), \
		sjme_sm(.memberName, #inMemberName), \
		sjme_sm(.isPointer, inIsPointer), \
		sjme_sm(.size, sizeof(((SJME_WALK_CURRENT*)(0))->inMemberName)), \
		sjme_sm(.memberSize, inMemberSize), \
		sjme_sm(.javaType, inJavaType), \
		sjme_sm(.typeId, inNvmId), \
		sjme_sm(.customStep, customWalkFunc) \
	}

/** Walk step a primitive value (pointer). */
#define SJME_WS_JAVA_P(memberName, javaType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, javaType, SJME_NVM_WALK_PSEUDO_PRIMITIVE, -1, NULL)

/** Walk step a primitive value (value). */
#define SJME_WS_JAVA_V(memberName, javaType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, javaType, SJME_NVM_WALK_PSEUDO_PRIMITIVE, -1, NULL)

/** Walk step another structure type (pointer). */
#define SJME_WS_NORM_P(memberName, structType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, structType, -1, NULL)

/** Walk with custom logic (value). */
#define SJME_WS_CUSTOM_V(memberName, structType, customFunc) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, structType, -1, customFunc)

/** Walk with custom logic (pointer). */
#define SJME_WS_CUSTOM_P(memberName, structType, customFunc) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, structType, -1, customFunc)

/** Walk step another structure type (value). */
#define SJME_WS_NORM_V(memberName, structType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, structType, -1, NULL)

/** Walk step an array value (value). */
#define SJME_WS_ARRAY(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_FIXED_ARRAY, \
		sizeof(*((SJME_WALK_CURRENT*)(0))->memberName), NULL), \
	subDef

/** Walk step a list value (pointer). */
#define SJME_WS_LIST_P(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_LIST, -1, NULL), \
	subDef

/** Walk step a phantom (atomic) pointer. */
#define SJME_WS_ATOMIC(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_ATOMIC, -1, NULL), \
	subDef

/** Walk step a phantom atomic pointer. */
#define SJME_WS_PHANTOM(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_PHANTOM, -1, NULL), \
	subDef

/** Walk step a non-cyclic atomic pointer. */
#define SJME_WS_NON_CYCLIC(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_NON_CYCLIC, -1, NULL), \
	subDef

/** Walk step a list value (value). */
#define SJME_WS_LIST_V(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_LIST, -1, NULL), \
	subDef

/** End walk structure. */
#define SJME_WALK_END() \
		{ \
			sjme_sm(.offset, -1), \
			sjme_sm(.memberName, NULL), \
			sjme_sm(.isPointer, SJME_JNI_FALSE), \
			sjme_sm(.size, -1), \
			sjme_sm(.memberSize, -1), \
			sjme_sm(.javaType, 0), \
			sjme_sm(.typeId, 0), \
			sjme_sm(.customStep, NULL) \
		} \
	}

#pragma endregion(supportMacros)
#pragma region(customWalkHandlers)

static sjme_errorCode sjme_nvm_walk_customFieldConstVal(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_nvm_walk_state step;
	sjme_nvm_class_fieldConstVal* constVal;
	sjme_jint desireType;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Initialize sub-step, virtually same as self. */
	memmove(&step, at, sizeof(step));
	step.parent = at;

	/* Which type ID to set? */
	constVal = at->valueP.value;
	switch (constVal->type)
	{
			/* Objects are strings. */
		case SJME_JAVA_TYPE_ID_OBJECT:
			desireType = SJME_NVM_WALK_PSEUDO_FIELD_CONST_VAL_STRING;
			break;

			/* Anything else is treated as a number. */
		default:
			desireType = SJME_NVM_WALK_PSEUDO_FIELD_CONST_VAL_NUMBER;
			break;
	}
	
	/* Normal post-custom logic after step setup. */
	step.typeId.i = desireType;
	return step.normalCustom(root, at, &step, function);
}

static sjme_errorCode sjme_nvm_walk_customPoolEntries(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_nvm_walk_state step;
	sjme_nvm_class_poolEntry* poolEntry;
	sjme_jint desireType;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Initialize sub-step, virtually same as self. */
	memmove(&step, at, sizeof(step));
	step.parent = at;

	/* Which type ID to set? */
	poolEntry = at->valueP.value;
	switch (poolEntry->type)
	{
		case SJME_NVM_CLASS_POOL_TYPE_NULL:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_NULL;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_UTF:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_UTF;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_INTEGER:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_INTEGER;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_FLOAT:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_FLOAT;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_LONG:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_LONG;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_DOUBLE:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_DOUBLE;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_CLASS:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_CLASS;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_STRING:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_STRING;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_FIELD:
		case SJME_NVM_CLASS_POOL_TYPE_METHOD:
		case SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_MEMBER;
			break;
			
		case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
			desireType = SJME_NVM_WALK_PSEUDO_POOL_TYPE_NAME_AND_TYPE;
			break;
			
			/* Invalid. */
		default:
			return SJME_ERROR_WALK_UNKNOWN_TYPE;
	}
	
	/* Normal post-custom logic after step setup. */
	step.typeId.i = desireType;
	return step.normalCustom(root, at, &step, function);
}

#pragma endregion(customWalkHandlers)

/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */

#pragma region(walkPseudo)

#define SJME_WALK_CURRENT sjme_closeableBase
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_CLOSEABLE)
	SJME_WS_ATOMIC(isClosed,
		SJME_WS_JAVA_V(isClosed, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_JAVA_V(refCounting, SJME_BASIC_TYPE_ID_BOOLEAN),
	SJME_WS_NORM_P(closeHandler,
		SJME_NVM_WALK_PSEUDO_CLOSE_HANDLER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_frontEnd
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_FRONT_END)
	SJME_WS_NORM_P(wrapper, SJME_NVM_WALK_PSEUDO_FRONT_END_WRAPPER),
	SJME_WS_NORM_P(data, SJME_NVM_WALK_PSEUDO_FRONT_END_DATA),
	SJME_WS_NORM_V(bindLock, SJME_NVM_WALK_PSEUDO_SPIN_LOCK),
	SJME_WS_NORM_V(bindType, SJME_NVM_WALK_PSEUDO_FRONT_END_BIND_TYPE),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_jmemberIDBase
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_MEMBER_ID)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_PHANTOM(inClass,
		SJME_WS_NORM_P(inClass, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_JAVA_V(idHash, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_P(name, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_NORM_P(type, SJME_NVM_STRUCT_STRING_POOL_STRING),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_bootParam
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_BOOT_PARAM)
	SJME_WS_NORM_P(bootSuite, SJME_NVM_STRUCT_ROM_SUITE),
	SJME_WS_NORM_P(librarySuite, SJME_NVM_STRUCT_ROM_SUITE),
	SJME_WS_LIST_P(mainClassPathById,
		SJME_WS_JAVA_V(mainClassPathById, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_LIST_P(mainClassPathByName,
		SJME_WS_NORM_P(mainClassPathByName, SJME_NVM_WALK_PSEUDO_LPSTR)),
	SJME_WS_NORM_P(mainClass, SJME_NVM_WALK_PSEUDO_LPSTR),
	SJME_WS_LIST_P(mainArgs,
		SJME_WS_NORM_V(mainArgs, SJME_NVM_WALK_PSEUDO_LPSTR)),
	SJME_WS_LIST_P(sysProps,
		SJME_WS_NORM_V(sysProps, SJME_NVM_WALK_PSEUDO_LPSTR)),
	SJME_WS_NORM_V(belay, SJME_NVM_WALK_PSEUDO_BOOT_BELAY_TYPE),
	SJME_WS_JAVA_V(launcherFallback, SJME_BASIC_TYPE_ID_BOOLEAN),
	SJME_WS_NORM_V(clutterLevel, SJME_NVM_WALK_PSEUDO_CLUTTER_LEVEL),
	SJME_WS_JAVA_V(noOptimize, SJME_BASIC_TYPE_ID_BOOLEAN),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_fieldConstVal
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_FIELD_CONST_VAL)
	SJME_WS_CUSTOM_V(type, SJME_NVM_WALK_PSEUDO_UNION,
		sjme_nvm_walk_customFieldConstVal),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntry
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_ENTRY)
	SJME_WS_CUSTOM_V(type, SJME_NVM_WALK_PSEUDO_UNION,
		sjme_nvm_walk_customPoolEntries),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntry
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_TYPE_NULL)
	SJME_WS_JAVA_V(type, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntryClass
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_TYPE_CLASS)
	SJME_WS_JAVA_V(type, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(descriptorIndex, SJME_BASIC_TYPE_ID_SHORT),
	SJME_WS_NORM_P(descriptor, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_JAVA_V(descriptorHash, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntryInteger
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_TYPE_INTEGER)
	SJME_WS_JAVA_V(type, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(value, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntryLong
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_TYPE_LONG)
	SJME_WS_JAVA_V(type, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(value, SJME_JAVA_TYPE_ID_LONG),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntryMember
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_TYPE_MEMBER)
	SJME_WS_JAVA_V(type, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(inClassIndex, SJME_BASIC_TYPE_ID_SHORT),
	SJME_WS_PHANTOM(inClass,
		SJME_WS_NORM_P(inClass, SJME_NVM_WALK_PSEUDO_POOL_TYPE_CLASS)),
	SJME_WS_JAVA_V(nameAndTypeIndex, SJME_BASIC_TYPE_ID_SHORT),
	SJME_WS_PHANTOM(nameAndType,
		SJME_WS_NORM_P(nameAndType,
			SJME_NVM_WALK_PSEUDO_POOL_TYPE_NAME_AND_TYPE)),
	SJME_WS_JAVA_V(staticArgSlots, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(rvSlots, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntryNameAndType
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_TYPE_NAME_AND_TYPE)
	SJME_WS_JAVA_V(type, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(nameIndex, SJME_BASIC_TYPE_ID_SHORT),
	SJME_WS_NORM_P(name, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_JAVA_V(descriptorIndex, SJME_BASIC_TYPE_ID_SHORT),
	SJME_WS_NORM_P(descriptor, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_JAVA_V(idHash, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntryString
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_TYPE_STRING)
	SJME_WS_JAVA_V(type, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(valueIndex, SJME_BASIC_TYPE_ID_SHORT),
	SJME_WS_NORM_P(value, SJME_NVM_STRUCT_STRING_POOL_STRING),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolEntryUtf
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_POOL_TYPE_UTF)
	SJME_WS_JAVA_V(type, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_P(utf, SJME_NVM_STRUCT_STRING_POOL_STRING),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_commonBase
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_COMMON)
	SJME_WS_NORM_V(closeable, SJME_NVM_WALK_PSEUDO_CLOSEABLE),
	SJME_WS_NORM_V(type, SJME_NVM_WALK_PSEUDO_NVM_STRUCT_TYPE),
	SJME_WS_JAVA_V(magic, SJME_BASIC_TYPE_ID_INTEGER),
	SJME_WS_NORM_V(frontEnd, SJME_NVM_WALK_PSEUDO_FRONT_END),
	SJME_WS_NORM_V(lock, SJME_NVM_WALK_PSEUDO_SPIN_LOCK),
	SJME_WS_NORM_P(postClose,
		SJME_NVM_WALK_PSEUDO_CLOSE_HANDLER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_task_globals
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_TASK_GLOBALS)
	SJME_WS_NORM_V(lock, SJME_NVM_WALK_PSEUDO_SPIN_LOCK),
	SJME_WS_ARRAY(stdPipes,
		SJME_WS_NORM_P(stdPipes, SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE)),
	SJME_WS_NORM_P(mainClassName, SJME_NVM_STRUCT_STRING_INSTANCE),
	SJME_WS_LIST_P(mainArgs,
		SJME_WS_NORM_P(mainArgs, SJME_NVM_STRUCT_STRING_INSTANCE)),
	SJME_WS_ARRAY(commonClasses,
		SJME_WS_NORM_P(commonClasses, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_NORM_P(accessor, SJME_NVM_WALK_PSEUDO_FIELD_ACCESSOR_FUNC),
	SJME_WS_LIST_P(jarBrackets,
		SJME_WS_NORM_P(jarBrackets,
			SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE)),
	SJME_WS_ATOMIC(mainThread,
		SJME_WS_NORM_P(mainThread, SJME_NVM_STRUCT_THREAD_INSTANCE)),
	SJME_WS_JAVA_V(noOptimize, SJME_BASIC_TYPE_ID_BOOLEAN),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_task_taskNewConfig
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_INIT_TASK_CONFIG)
	SJME_WS_NORM_V(stdOut, SJME_NVM_WALK_PSEUDO_TASK_PIPE_REDIRECT_TYPE),
	SJME_WS_NORM_V(stdErr, SJME_NVM_WALK_PSEUDO_TASK_PIPE_REDIRECT_TYPE),
	SJME_WS_LIST_P(classPath,
		SJME_WS_NORM_P(classPath, SJME_NVM_STRUCT_ROM_LIBRARY)),
	SJME_WS_NORM_P(mainClass, SJME_NVM_WALK_PSEUDO_LPSTR),
	SJME_WS_LIST_P(mainArgs,
		SJME_WS_NORM_P(mainArgs, SJME_NVM_WALK_PSEUDO_LPSTR)),
	SJME_WS_LIST_P(sysProps,
		SJME_WS_NORM_P(sysProps, SJME_NVM_WALK_PSEUDO_LPSTR)),
	SJME_WS_NORM_P(classLoader, SJME_NVM_STRUCT_VM_CLASS_LOADER),
	SJME_WS_NORM_V(belay, SJME_NVM_WALK_PSEUDO_BOOT_BELAY_TYPE),
	SJME_WS_JAVA_V(noOptimize, SJME_BASIC_TYPE_ID_BOOLEAN),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_threadSchedule
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE)
	SJME_WS_NORM_V(model, SJME_NVM_WALK_PSEUDO_MLE_THREAD_MODEL),
	SJME_WS_NORM_V(lock, SJME_NVM_WALK_PSEUDO_SPIN_LOCK),
	SJME_WS_ARRAY(mode,
		SJME_WS_NORM_V(mode, SJME_NVM_WALK_PSEUDO_THREAD_SUB_SCHEDULE)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_threadSubSchedule
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_THREAD_SUB_SCHEDULE)
	SJME_WS_JAVA_V(count, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_LIST_P(order,
		SJME_WS_NORM_P(order, SJME_NVM_STRUCT_THREAD_INSTANCE)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_frame_threadStacks
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_THREAD_STACKS)
	SJME_WS_NORM_P(storage, SJME_NVM_WALK_PSEUDO_POINTER),
	SJME_WS_JAVA_V(storageLen, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(storageTop, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_random
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_RANDOM)
	SJME_WS_JAVA_V(seed, SJME_JAVA_TYPE_ID_LONG),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_thread_rwLock
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_RW_LOCK)
	SJME_WS_NORM_P(read, SJME_NVM_WALK_PSEUDO_SPIN_LOCK),
	SJME_WS_ATOMIC(writeCount,
		SJME_WS_JAVA_V(writeCount, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_NORM_V(write, SJME_NVM_WALK_PSEUDO_SPIN_LOCK),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#pragma endregion(walkPseudo)
#pragma region(walkNvmStruct)

#define SJME_WALK_CURRENT sjme_jarrayBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_ARRAY_INSTANCE)
	SJME_WS_NORM_V(object, SJME_NVM_STRUCT_OBJECT_INSTANCE),
	SJME_WS_NORM_V(type, SJME_NVM_WALK_PSEUDO_BASIC_TYPE_ID),
	SJME_WS_JAVA_V(length, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_CUSTOM_V(e, SJME_NVM_WALK_PSEUDO_RAW_ARRAY_VALUES, NULL),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_jclassBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_CLASS_INSTANCE)
	SJME_WS_NORM_V(object, SJME_NVM_STRUCT_OBJECT_INSTANCE),
	SJME_WS_NORM_P(binaryName, SJME_NVM_WALK_PSEUDO_CHAR_SEQ),
	SJME_WS_JAVA_V(binaryHash, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_ATOMIC(error,
		SJME_WS_JAVA_V(error, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_ATOMIC(isLoaded,
		SJME_WS_JAVA_V(isLoaded, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_ATOMIC(isInitialized,
		SJME_WS_JAVA_V(isInitialized, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_NORM_P(info, SJME_NVM_STRUCT_CLASS_INFO),
	SJME_WS_ATOMIC(superClass,
		SJME_WS_NORM_P(superClass, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_LIST_P(interfaceClasses,
		SJME_WS_NORM_P(interfaceClasses, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_ARRAY(fields,
		SJME_WS_NORM_V(fields, SJME_NVM_WALK_PSEUDO_CLASS_FIELDS)),
	SJME_WS_ARRAY(methods,
		SJME_WS_NORM_V(methods, SJME_NVM_WALK_PSEUDO_CLASS_METHODS)),
	SJME_WS_LIST_P(interfaceBinds,
		SJME_WS_NORM_P(interfaceBinds, SJME_NVM_STRUCT_INTERFACE_ID)),
	SJME_WS_NORM_P(isClasses, SJME_NVM_STRUCT_IS_CLASSES),
	SJME_WS_NORM_V(typeId, SJME_NVM_WALK_PSEUDO_JAVA_TYPE_ID),
	SJME_WS_NORM_V(arrayTypeId, SJME_NVM_WALK_PSEUDO_BASIC_TYPE_ID),
	SJME_WS_ATOMIC(componentType,
		SJME_WS_NORM_P(componentType, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_PHANTOM(phantomArrayType,
		SJME_WS_NORM_P(phantomArrayType, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_NORM_P(staticChunk, SJME_NVM_WALK_PSEUDO_UNSPECIFIED_BINARY),
	SJME_WS_ATOMIC(numDimensions,
		SJME_WS_JAVA_V(numDimensions, SJME_JAVA_TYPE_ID_INTEGER)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT


#define SJME_WALK_CURRENT sjme_jfieldIDBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_FIELD_ID)
	SJME_WS_NORM_V(member, SJME_NVM_WALK_PSEUDO_MEMBER_ID),
	SJME_WS_NORM_V(javaType, SJME_NVM_WALK_PSEUDO_JAVA_TYPE_ID),
	SJME_WS_NORM_V(javaType, SJME_NVM_WALK_PSEUDO_BASIC_TYPE_ID),
	SJME_WS_NORM_V(extendedType, SJME_NVM_WALK_PSEUDO_EXTENDED_TYPE_ID),
	SJME_WS_PHANTOM(objectType,
		SJME_WS_NORM_P(objectType, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_NORM_V(flags, SJME_NVM_WALK_PSEUDO_FIELD_FLAGS),
	SJME_WS_NORM_P(info, SJME_NVM_STRUCT_FIELD_INFO),
	SJME_WS_NORM_P(accessor, SJME_NVM_WALK_PSEUDO_FIELD_ACCESSOR_FUNC),
	SJME_WS_JAVA_V(pointerOffset, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_jmethodIDBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_METHOD_ID)
	SJME_WS_NORM_V(member, SJME_NVM_WALK_PSEUDO_MEMBER_ID),
	SJME_WS_NORM_V(flags, SJME_NVM_WALK_PSEUDO_METHOD_FLAGS),
	SJME_WS_ARRAY(info,
		SJME_WS_NORM_V(flags, SJME_NVM_STRUCT_METHOD_INFO)),
	SJME_WS_NORM_V(bits, SJME_NVM_WALK_PSEUDO_METHOD_INFO_BITS),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_jobjectBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_OBJECT_INSTANCE)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_JAVA_V(identityHash, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NON_CYCLIC(isClass,
		SJME_WS_NORM_P(isClass, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_ATOMIC(monitorCount,
		SJME_WS_JAVA_V(monitorCount, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_ATOMIC(special,
		SJME_WS_JAVA_V(special, SJME_JAVA_TYPE_ID_INTEGER)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_jstringBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_STRING_INSTANCE)
	SJME_WS_NORM_V(object, SJME_NVM_STRUCT_OBJECT_INSTANCE),
	SJME_WS_PHANTOM(poolString,
		SJME_WS_NORM_P(poolString, SJME_NVM_STRUCT_STRING_POOL_STRING)),
	SJME_WS_PHANTOM(seq,
		SJME_WS_NORM_P(seq, SJME_NVM_WALK_PSEUDO_CHAR_SEQ)),
	SJME_WS_JAVA_V(intern.hashCode, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(intern.length, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_infoBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_CLASS_INFO)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_P(fileName, SJME_NVM_WALK_PSEUDO_LPSTR),
	SJME_WS_JAVA_V(fileNameHash, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_P(pool, SJME_NVM_STRUCT_POOL),
	SJME_WS_NORM_V(version, SJME_NVM_WALK_PSEUDO_CLASS_VERSION),
	SJME_WS_NORM_V(flags, SJME_NVM_WALK_PSEUDO_CLASS_FLAGS),
	SJME_WS_NORM_P(inPackage, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_NORM_P(name, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_NORM_P(inPackage, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_NORM_P(superName, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_NORM_P(runtimeName, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_LIST_P(interfaceNames,
		SJME_WS_NORM_P(interfaceNames, SJME_NVM_STRUCT_STRING_POOL_STRING)),
	SJME_WS_LIST_P(fields,
		SJME_WS_NORM_P(fields, SJME_NVM_STRUCT_FIELD_INFO)),
	SJME_WS_ARRAY(fieldCount,
		SJME_WS_ARRAY(fieldCount,
			SJME_WS_JAVA_V(fieldCount, SJME_BASIC_TYPE_ID_SHORT))),
	SJME_WS_ARRAY(methodCount,
		SJME_WS_JAVA_V(methodCount, SJME_BASIC_TYPE_ID_SHORT)),
	SJME_WS_LIST_P(methods,
		SJME_WS_NORM_P(methods, SJME_NVM_STRUCT_METHOD_INFO)),
	SJME_WS_JAVA_P(isArray, SJME_BASIC_TYPE_ID_BOOLEAN),
	SJME_WS_PHANTOM(library,
		SJME_WS_NORM_P(library, SJME_NVM_STRUCT_ROM_LIBRARY)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_codeInfoBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_CODE_INFO)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_PHANTOM(inMethod,
		SJME_WS_NORM_P(inMethod, SJME_NVM_STRUCT_METHOD_INFO)),
	SJME_WS_ARRAY(perType,
		SJME_WS_NORM_V(perType, SJME_NVM_WALK_PSEUDO_CODE_PER_TYPE)),
	SJME_WS_LIST_P(exceptions,
		SJME_WS_NORM_P(exceptions, SJME_NVM_WALK_PSEUDO_EXCEPTION_HANDLER)),
	SJME_WS_JAVA_V(rawCodeLen, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_P(rawCode, SJME_NVM_WALK_PSEUDO_BYTE_CODE),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_isClassesBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_IS_CLASSES)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_LIST_P(classes,
		SJME_WS_PHANTOM(classes,
			SJME_WS_NORM_P(classes, SJME_NVM_STRUCT_CLASS_INSTANCE))),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_fieldInfoBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_FIELD_INFO)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_V(flags, SJME_NVM_WALK_PSEUDO_FIELD_FLAGS),
	SJME_WS_NORM_P(name, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_NORM_P(type, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_JAVA_V(idHash, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_V(constVal, SJME_NVM_WALK_PSEUDO_FIELD_CONST_VAL),
	SJME_WS_NORM_V(javaType, SJME_NVM_WALK_PSEUDO_JAVA_TYPE_ID),
	SJME_WS_NORM_V(basicType, SJME_NVM_WALK_PSEUDO_BASIC_TYPE_ID),
	SJME_WS_NORM_V(extendedType, SJME_NVM_WALK_PSEUDO_EXTENDED_TYPE_ID),
	SJME_WS_JAVA_V(typedIndex, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_methodInfoBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_METHOD_INFO)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_V(flags, SJME_NVM_WALK_PSEUDO_METHOD_FLAGS),
	SJME_WS_JAVA_V(idHash, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_P(name, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_NORM_P(type, SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WS_JAVA_V(argC, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_P(argT, SJME_NVM_WALK_PSEUDO_JAVA_TYPE_ID),
	SJME_WS_NORM_V(argR, SJME_NVM_WALK_PSEUDO_JAVA_TYPE_ID),
	SJME_WS_NORM_P(code, SJME_NVM_STRUCT_CODE_INFO),
	SJME_WS_JAVA_V(typedIndex, SJME_BASIC_TYPE_ID_SHORT),
	SJME_WS_PHANTOM(inClass,
		SJME_WS_NORM_P(inClass, SJME_NVM_STRUCT_CLASS_INFO)),
	SJME_WS_NORM_V(bits, SJME_NVM_WALK_PSEUDO_METHOD_INFO_BITS),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_class_poolInfoBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_POOL)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_LIST_P(pool,
		SJME_WS_NORM_V(pool, SJME_NVM_WALK_PSEUDO_POOL_ENTRY)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_rom_libraryBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_ROM_LIBRARY)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_P(functions, SJME_NVM_WALK_PSEUDO_LIBRARY_FUNCTIONS),
	SJME_WS_NORM_P(allocPool, SJME_NVM_WALK_PSEUDO_ALLOC_POOL),
	SJME_WS_NORM_P(handle, SJME_NVM_WALK_PSEUDO_POINTER),
	SJME_WS_NORM_P(prefix, SJME_NVM_WALK_PSEUDO_LPSTR),
	SJME_WS_JAVA_V(id, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_P(name, SJME_NVM_WALK_PSEUDO_LPSTR),
	SJME_WS_JAVA_V(nameHash, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(rawSize, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(checkedRawAccess, SJME_BASIC_TYPE_ID_BOOLEAN),
	SJME_WS_JAVA_V(validRawAccess, SJME_BASIC_TYPE_ID_BOOLEAN),
	SJME_WS_NORM_V(rwLock, SJME_NVM_WALK_PSEUDO_RW_LOCK),
	SJME_WS_LIST_P(classInfos,
		SJME_WS_NORM_P(classInfos, SJME_NVM_STRUCT_CLASS_INFO)),
	SJME_WS_NORM_P(stringPool, SJME_NVM_STRUCT_STRING_POOL),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_rom_suiteBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_ROM_SUITE)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_P(functions, SJME_NVM_WALK_PSEUDO_SUITE_FUNCTIONS),
	SJME_WS_NORM_P(allocPool, SJME_NVM_WALK_PSEUDO_ALLOC_POOL),
	SJME_WS_NORM_P(handle, SJME_NVM_WALK_PSEUDO_POINTER),
	SJME_WS_LIST_P(libraries,
		SJME_WS_NORM_P(libraries, SJME_NVM_STRUCT_ROM_LIBRARY)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_stateBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_STATE)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_P(allocPool, SJME_NVM_WALK_PSEUDO_ALLOC_POOL),
	SJME_WS_NORM_P(bootParamCopy, SJME_NVM_WALK_PSEUDO_BOOT_PARAM),
	SJME_WS_NORM_P(hooks, SJME_NVM_WALK_PSEUDO_STATE_HOOKS),
	SJME_WS_NORM_P(nal, SJME_NVM_WALK_PSEUDO_NAL),
	SJME_WS_NORM_P(suite, SJME_NVM_STRUCT_ROM_SUITE),
	SJME_WS_LIST_P(tasks,
		SJME_WS_NORM_P(tasks, SJME_NVM_STRUCT_TASK)),
	SJME_WS_ATOMIC(numRunningTasks,
		SJME_WS_JAVA_V(numRunningTasks, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_ATOMIC(nextTaskId,
		SJME_WS_JAVA_V(nextTaskId, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_ATOMIC(nextThreadId,
		SJME_WS_JAVA_V(nextThreadId, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_NORM_V(threadModel, SJME_NVM_WALK_PSEUDO_MLE_THREAD_MODEL),
	SJME_WS_NORM_P(schedule, SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE),
	SJME_WS_ATOMIC(terminating,
		SJME_WS_JAVA_V(terminating, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_NORM_P(initTaskConfig, SJME_NVM_WALK_PSEUDO_INIT_TASK_CONFIG),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_stringPoolBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_STRING_POOL)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_P(allocPool, SJME_NVM_WALK_PSEUDO_ALLOC_POOL),
	SJME_WS_LIST_P(strings,
		SJME_WS_NORM_P(strings, SJME_NVM_STRUCT_STRING_POOL_STRING)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_stringPool_stringBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_STRING_POOL_STRING)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_P(seq, SJME_NVM_WALK_PSEUDO_CHAR_SEQ),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_taskBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_TASK)
	SJME_WS_NORM_V(object, SJME_NVM_STRUCT_OBJECT_INSTANCE),
	SJME_WS_JAVA_V(id, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_PHANTOM(inState,
		SJME_WS_NORM_P(inState, SJME_NVM_STRUCT_STATE)),
	SJME_WS_JAVA_V(exitCode, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_V(status, SJME_NVM_WALK_PSEUDO_TASK_STATUS_TYPE),
	SJME_WS_ATOMIC(terminate,
		SJME_WS_JAVA_V(terminate, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_ARRAY(numThreads,
		SJME_WS_ATOMIC(numThreads,
			SJME_WS_JAVA_V(numThreads, SJME_JAVA_TYPE_ID_INTEGER))),
	SJME_WS_LIST_P(threads,
		SJME_WS_NORM_P(threads, SJME_NVM_STRUCT_THREAD_INSTANCE)),
	SJME_WS_NORM_P(classLoader, SJME_NVM_STRUCT_VM_CLASS_LOADER),
	SJME_WS_NORM_P(strings, SJME_NVM_STRUCT_TASK_STRINGS),
	SJME_WS_NORM_V(globals, SJME_NVM_WALK_PSEUDO_TASK_GLOBALS),
	SJME_WS_ATOMIC(nextFrameId,
		SJME_WS_JAVA_V(nextFrameId, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_NORM_P(initConfig,
		SJME_NVM_WALK_PSEUDO_INIT_TASK_CONFIG),
	SJME_WS_NORM_V(idHash,
		SJME_NVM_WALK_PSEUDO_RANDOM),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_taskStringsBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_TASK_STRINGS)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_LIST_P(interns,
		SJME_WS_NORM_P(interns, SJME_NVM_STRUCT_STRING_INSTANCE)),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_threadBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_THREAD_INSTANCE)
	SJME_WS_NORM_V(object, SJME_NVM_STRUCT_OBJECT_INSTANCE),
	SJME_WS_PHANTOM(inState,
		SJME_WS_NORM_P(inState, SJME_NVM_STRUCT_STATE)),
	SJME_WS_PHANTOM(inTask,
		SJME_WS_NORM_P(inTask, SJME_NVM_STRUCT_TASK)),
	SJME_WS_ATOMIC(start,
		SJME_WS_NORM_V(start, SJME_NVM_WALK_PSEUDO_THREAD_START_TYPE)),
	SJME_WS_ATOMIC(status,
		SJME_WS_NORM_V(status, SJME_NVM_WALK_PSEUDO_THREAD_STATUS_TYPE)),
	SJME_WS_NORM_V(frontEnd, SJME_NVM_WALK_PSEUDO_FRONT_END),
	SJME_WS_ATOMIC(nativeThread,
		SJME_WS_NORM_P(nativeThread, SJME_NVM_STRUCT_THREAD_INSTANCE)),
	SJME_WS_JAVA_V(threadId, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_JAVA_V(isMain, SJME_BASIC_TYPE_ID_BOOLEAN),
	SJME_WS_JAVA_V(numFrames, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_LIST_P(frames,
		SJME_WS_NORM_P(frames, SJME_NVM_STRUCT_FRAME)),
	SJME_WS_NORM_P(stack, SJME_NVM_WALK_PSEUDO_THREAD_STACKS),
	SJME_WS_ATOMIC(scheduleMode,
		SJME_WS_NORM_V(scheduleMode,
			SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE_MODE)),
	SJME_WS_ATOMIC(tossed,
		SJME_WS_NORM_P(tossed, SJME_NVM_STRUCT_OBJECT_INSTANCE)),
	SJME_WS_ATOMIC(tossedLevel,
		SJME_WS_JAVA_V(tossedLevel, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_ATOMIC(interrupted,
		SJME_WS_JAVA_V(interrupted, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_JAVA_V(flags, SJME_JAVA_TYPE_ID_INTEGER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_vmClass_loaderBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_VM_CLASS_LOADER)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_P(inState, SJME_NVM_STRUCT_STATE),
	SJME_WS_NORM_V(rwLock, SJME_NVM_WALK_PSEUDO_RW_LOCK),
	SJME_WS_LIST_P(classPath,
		SJME_WS_NORM_P(classPath, SJME_NVM_STRUCT_ROM_LIBRARY)),
	SJME_WS_LIST_P(classes,
		SJME_WS_NORM_P(classes, SJME_NVM_STRUCT_CLASS_INSTANCE)),
	SJME_WS_NORM_P(nullStrings, SJME_NVM_STRUCT_STRING_POOL),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#pragma endregion(walkNvmStruct)

/** Pseudo only structureless types. */
static const sjme_nvm_walk_pseudoType sjme_nvm_walk_pseudoOnly[] =
{
	SJME_NVM_WALK_PSEUDO_ALLOC_POOL,
	SJME_NVM_WALK_PSEUDO_ATOMIC,
	SJME_NVM_WALK_PSEUDO_BASIC_TYPE_ID,
	SJME_NVM_WALK_PSEUDO_FRONT_END_BIND_TYPE,
	SJME_NVM_WALK_PSEUDO_BOOT_BELAY_TYPE,
	SJME_NVM_WALK_PSEUDO_CHAR_SEQ,
	SJME_NVM_WALK_PSEUDO_CLASS_FLAGS,
	SJME_NVM_WALK_PSEUDO_CLASS_VERSION,
	SJME_NVM_WALK_PSEUDO_CLOSE_HANDLER,
	SJME_NVM_WALK_PSEUDO_CLUTTER_LEVEL,
	SJME_NVM_WALK_PSEUDO_EXTENDED_TYPE_ID,
	SJME_NVM_WALK_PSEUDO_FIELD_ACCESSOR_FUNC,
	SJME_NVM_WALK_PSEUDO_FIELD_FLAGS,
	SJME_NVM_WALK_PSEUDO_FIXED_ARRAY,
	SJME_NVM_WALK_PSEUDO_FRONT_END_BIND_TYPE,
	SJME_NVM_WALK_PSEUDO_FRONT_END_DATA,
	SJME_NVM_WALK_PSEUDO_FRONT_END_WRAPPER,
	SJME_NVM_WALK_PSEUDO_JAVA_TYPE_ID,
	SJME_NVM_WALK_PSEUDO_LIBRARY_FUNCTIONS,
	SJME_NVM_WALK_PSEUDO_LIST,
	SJME_NVM_WALK_PSEUDO_LPSTR,
	SJME_NVM_WALK_PSEUDO_METHOD_FLAGS,
	SJME_NVM_WALK_PSEUDO_METHOD_INFO_BITS,
	SJME_NVM_WALK_PSEUDO_MLE_THREAD_MODEL,
	SJME_NVM_WALK_PSEUDO_NAL,
	SJME_NVM_WALK_PSEUDO_NON_CYCLIC,
	SJME_NVM_WALK_PSEUDO_NVM_STRUCT_TYPE,
	SJME_NVM_WALK_PSEUDO_PHANTOM,
	SJME_NVM_WALK_PSEUDO_POINTER,
	SJME_NVM_WALK_PSEUDO_PRIMITIVE,
	SJME_NVM_WALK_PSEUDO_BYTE_CODE,
	SJME_NVM_WALK_PSEUDO_RAW_ARRAY_VALUES,
	SJME_NVM_WALK_PSEUDO_SPIN_LOCK,
	SJME_NVM_WALK_PSEUDO_STATE_HOOKS,
	SJME_NVM_WALK_PSEUDO_SUITE_FUNCTIONS,
	SJME_NVM_WALK_PSEUDO_TASK_PIPE_REDIRECT_TYPE,
	SJME_NVM_WALK_PSEUDO_TASK_STATUS_TYPE,
	SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE_MODE,
	SJME_NVM_WALK_PSEUDO_THREAD_START_TYPE,
	SJME_NVM_WALK_PSEUDO_UNION,
	SJME_NVM_WALK_PSEUDO_UNSPECIFIED_BINARY,

	/* End. */
	0,
};

const sjme_nvm_walk_stepSelect sjme_nvm_walk_select[] =
{
	/* Pseudo Structures. */
	SJME_WALK_SELECT(sjme_closeableBase, SJME_NVM_WALK_PSEUDO_CLOSEABLE),
	SJME_WALK_SELECT(sjme_frontEnd, SJME_NVM_WALK_PSEUDO_FRONT_END),
	SJME_WALK_SELECT(sjme_jmemberIDBase,
		SJME_NVM_WALK_PSEUDO_MEMBER_ID),
	SJME_WALK_SELECT(sjme_nvm_bootParam, SJME_NVM_WALK_PSEUDO_BOOT_PARAM),
	SJME_WALK_SELECT(sjme_nvm_class_fieldConstVal,
		SJME_NVM_WALK_PSEUDO_FIELD_CONST_VAL),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntry,
		SJME_NVM_WALK_PSEUDO_POOL_ENTRY),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntry,
		SJME_NVM_WALK_PSEUDO_POOL_TYPE_NULL),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntryClass,
		SJME_NVM_WALK_PSEUDO_POOL_TYPE_CLASS),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntryInteger,
		SJME_NVM_WALK_PSEUDO_POOL_TYPE_INTEGER),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntryLong,
		SJME_NVM_WALK_PSEUDO_POOL_TYPE_LONG),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntryMember,
		SJME_NVM_WALK_PSEUDO_POOL_TYPE_MEMBER),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntryNameAndType,
		SJME_NVM_WALK_PSEUDO_POOL_TYPE_NAME_AND_TYPE),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntryString,
		SJME_NVM_WALK_PSEUDO_POOL_TYPE_STRING),
	SJME_WALK_SELECT(sjme_nvm_class_poolEntryUtf,
		SJME_NVM_WALK_PSEUDO_POOL_TYPE_UTF),
	SJME_WALK_SELECT(sjme_nvm_commonBase, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WALK_SELECT(sjme_nvm_task_globals,
		SJME_NVM_WALK_PSEUDO_TASK_GLOBALS),
	SJME_WALK_SELECT(sjme_nvm_task_taskNewConfig,
		SJME_NVM_WALK_PSEUDO_INIT_TASK_CONFIG),
	SJME_WALK_SELECT(sjme_nvm_threadSchedule,
		SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE),
	SJME_WALK_SELECT(sjme_frame_threadStacks,
		SJME_NVM_WALK_PSEUDO_THREAD_STACKS),
	SJME_WALK_SELECT(sjme_nvm_threadSubSchedule,
		SJME_NVM_WALK_PSEUDO_THREAD_SUB_SCHEDULE),
	SJME_WALK_SELECT(sjme_random, SJME_NVM_WALK_PSEUDO_RANDOM),
	SJME_WALK_SELECT(sjme_thread_rwLock, SJME_NVM_WALK_PSEUDO_RW_LOCK),

	/* NVM Structures. */
	SJME_WALK_SELECT(sjme_jarrayBase, SJME_NVM_STRUCT_ARRAY_INSTANCE),
	SJME_WALK_SELECT(sjme_jclassBase, SJME_NVM_STRUCT_CLASS_INSTANCE),
	SJME_WALK_SELECT(sjme_jfieldIDBase, SJME_NVM_STRUCT_FIELD_ID),
	SJME_WALK_SELECT(sjme_jmethodIDBase,
		SJME_NVM_STRUCT_METHOD_ID),
	SJME_WALK_SELECT(sjme_jobjectBase, SJME_NVM_STRUCT_OBJECT_INSTANCE),
	SJME_WALK_SELECT(sjme_jstringBase, SJME_NVM_STRUCT_STRING_INSTANCE),
	SJME_WALK_SELECT(sjme_nvm_class_codeInfoBase,
		SJME_NVM_STRUCT_CODE_INFO),
	SJME_WALK_SELECT(sjme_nvm_class_infoBase,
		SJME_NVM_STRUCT_CLASS_INFO),
	SJME_WALK_SELECT(sjme_nvm_class_poolInfoBase,
		SJME_NVM_STRUCT_POOL),
	SJME_WALK_SELECT(sjme_nvm_class_fieldInfoBase,
		SJME_NVM_STRUCT_FIELD_INFO),
	SJME_WALK_SELECT(sjme_nvm_class_methodInfoBase,
		SJME_NVM_STRUCT_METHOD_INFO),
	SJME_WALK_SELECT(sjme_nvm_isClassesBase, SJME_NVM_STRUCT_IS_CLASSES),
	SJME_WALK_SELECT(sjme_nvm_rom_libraryBase, SJME_NVM_STRUCT_ROM_LIBRARY),
	SJME_WALK_SELECT(sjme_nvm_rom_suiteBase, SJME_NVM_STRUCT_ROM_SUITE),
	SJME_WALK_SELECT(sjme_nvm_stateBase, SJME_NVM_STRUCT_STATE),
	SJME_WALK_SELECT(sjme_nvm_stringPoolBase, SJME_NVM_STRUCT_STRING_POOL),
	SJME_WALK_SELECT(sjme_nvm_stringPool_stringBase,
		SJME_NVM_STRUCT_STRING_POOL_STRING),
	SJME_WALK_SELECT(sjme_nvm_taskBase, SJME_NVM_STRUCT_TASK),
	SJME_WALK_SELECT(sjme_nvm_taskStringsBase,
		SJME_NVM_STRUCT_TASK_STRINGS),
	SJME_WALK_SELECT(sjme_nvm_threadBase,
		SJME_NVM_STRUCT_THREAD_INSTANCE),
	SJME_WALK_SELECT(sjme_nvm_vmClass_loaderBase,
		SJME_NVM_STRUCT_VM_CLASS_LOADER),

	/* No more walk structures defines. */
	SJME_WALK_SELECT_END()
};

/* ------------------------------------------------------------------------ */
/* clang-format on */ /* @formatter:on */

static sjme_errorCode sjme_nvm_walk_doItem(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	const sjme_nvm_walk_stepSelect* select;
	const sjme_nvm_walk_step* inStep;
	sjme_jint oldTypeId;
	sjme_javaTypeId oldJavaType;
	sjme_jboolean skipDefault;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Always clear string data. */
	at->lpstr = NULL;

	/* Walk on this item. */
	if (at->breadth == SJME_NVM_WALK_BREADTH_LEVEL)
	{
		/* Store old type IDs. */
		oldTypeId = at->typeId.i;
		oldJavaType = at->javaType;

		/* Aliased type? */
		inStep = (at->variantStep != NULL ? at->variantStep : at->inStep);
		if (inStep != NULL)
			switch (oldTypeId)
			{
					/* Enum type, can be differently sized by the compiler. */
				case SJME_NVM_WALK_PSEUDO_MLE_THREAD_MODEL:
				case SJME_NVM_WALK_PSEUDO_NVM_STRUCT_TYPE:
				case SJME_NVM_WALK_PSEUDO_FRONT_END_BIND_TYPE:
				case SJME_NVM_WALK_PSEUDO_TASK_STATUS_TYPE:
				case SJME_NVM_WALK_PSEUDO_FIXED_ARRAY:
					at->typeId.i = SJME_NVM_WALK_PSEUDO_PRIMITIVE;
					if (inStep->size == 1)
						at->javaType = SJME_BASIC_TYPE_ID_BYTE;
					else if (inStep->size == 2)
						at->javaType = SJME_BASIC_TYPE_ID_SHORT;
					else if (inStep->size == 4)
						at->javaType = SJME_BASIC_TYPE_ID_INTEGER;
					else if (inStep->size == 8)
						at->javaType = SJME_BASIC_TYPE_ID_LONG;

					/* Some other weird size? */
					else
						at->typeId.i = oldTypeId;
					break;

					/* String data? */
				case SJME_NVM_WALK_PSEUDO_LPSTR:
					if (at->valueP.value != NULL)
						at->lpstr = at->valueP.value;

					/* Character sequence? */
				case SJME_NVM_WALK_PSEUDO_CHAR_SEQ:
					if (at->valueP.value != NULL)
						at->lpstr = sjme_charSeq_tempUtf(at->valueP.value);
					break;

					/* Not aliased. */
				default:
					break;
			}
		
		/* Does this have custom step logic/initialization? */
		skipDefault = SJME_JNI_FALSE;
		if (inStep != NULL && inStep->customStep != NULL)
			if (sjme_error_is(error = inStep->customStep(root, parent,
				at, function)))
			{
				if (error != SJME_ERROR_WALK_SKIP_CUSTOM_DEFAULT)
					return sjme_error_default(error);
				skipDefault = SJME_JNI_TRUE;
			}
		
		/* Execute item handler. */
		if (!skipDefault)
			if (sjme_error_is(error = function(root, parent, at)))
				return sjme_error_default(error);

		/* Restore old types. */
		at->typeId.i = oldTypeId;
		at->javaType = oldJavaType;
	}

	/* Dive into this specific item, if enabled. */
	else if (at->breadth == SJME_NVM_WALK_BREADTH_DIVE && !at->noDive)
	{
		/* Diving into a normal structure? */
		if (at->typeId.i != SJME_NVM_WALK_PSEUDO_LIST &&
			at->typeId.i != SJME_NVM_WALK_PSEUDO_FIXED_ARRAY &&
			at->typeId.i != SJME_NVM_WALK_PSEUDO_PHANTOM &&
			at->typeId.i != SJME_NVM_WALK_PSEUDO_ATOMIC)
		{
			/* We can only dive into known types. */
			if (sjme_error_is(error = sjme_nvm_select(at->typeId.i, &select)))
				return sjme_error_default(error);
		}
		
		/* If this is a pointer, we need to dereference it. */
		if (at->inStep->isPointer)
		{
			/* Dereference. */
			at->valueP.value = *at->valueP.pointer;

			/* Set new structure base. */
			at->base.walkLayer = at->valueP.walkLayer; 
			at->baseStruct.walkLayer = at->base.walkLayer; 
		}

		/* Does this have custom step logic/initialization? */
		skipDefault = SJME_JNI_FALSE;
		if (at->inStep->customStep != NULL)
			if (sjme_error_is(error = at->inStep->customStep(root, parent,
				at, function)))
			{
				if (error != SJME_ERROR_WALK_SKIP_CUSTOM_DEFAULT)
					return sjme_error_default(error);
				skipDefault = SJME_JNI_TRUE;
			}
		
		/* Go back to the root item walking for this. */
		if (!skipDefault)
			if (sjme_error_is(error = sjme_nvm_walk(root, parent,
				at, function)))
				return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walk_doArray(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_nvm_walk_state subStep;
	const sjme_nvm_walk_step* variantStep;
	const sjme_nvm_walk_step* currentStep;
	sjme_jint i, n;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* We do not know enough to walk through this array. */
	currentStep = at->inStep;
	if (currentStep == NULL)
		return SJME_ERROR_NONE;
	
	/* Determine the fixed array length. */
	n = currentStep->size / currentStep->memberSize;
	
	/* Go through the entire list. */
	variantStep = at->variantStep;
	for (i = 0; i < n; i++)
	{
		/* Setup base step information. */
		memmove(&subStep, at, sizeof(subStep));
		subStep.uniqueId = ++at->nextUniqueId;
		subStep.depth = at->depth + 1;
		subStep.parent = at;
		
		/* The value is directly where the array element is */
		subStep.valueP.walkLayer = at->baseStruct.walkLayer +
			(i * currentStep->memberSize);
		
		/* Set item specific data. */
		subStep.index = i;
		subStep.inStep = variantStep;
		subStep.typeId.i = variantStep->typeId.i;
		subStep.javaType = variantStep->javaType;
		subStep.isPointer = variantStep->isPointer;

		/* Walk on this item. */
		if (sjme_error_is(error = sjme_nvm_walk_doItem(root, at,
			&subStep, function)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walk_doAtomic(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Set the type to be atomic. */
	at->isAtomic = SJME_JNI_TRUE;
	
	/* Directly walk on this! */
	if (sjme_error_is(error = sjme_nvm_walk_doItem(root, parent, at, function)))
		return sjme_error_default(error);

	/* Set to no longer be atomic before leaving. */
	at->isAtomic = SJME_JNI_FALSE;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walk_doList(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_nvm_walk_state subStep;
	sjme_list_void* voidList;
	const sjme_nvm_walk_step* variantStep;
	sjme_jint i, n;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Recover the associated void list. */
	voidList = at->valueP.value;

	/* Skip null pointers. */
	if (voidList == NULL)
		return SJME_ERROR_NONE;

	/* Go through the entire list. */
	variantStep = at->variantStep;
	for (i = 0, n = voidList->length; i < n; i++)
	{
		/* Setup base step information. */
		memmove(&subStep, at, sizeof(subStep));
		subStep.uniqueId = ++at->nextUniqueId;
		subStep.depth = at->depth + 1;
		subStep.parent = at;

		/* The structure base is the pointer of the list. */
		subStep.baseStruct.walkLayer = (sjme_intPointer)voidList;

		/* If the list itself is a pointer, then the base structure is also */
		/* the list because it is another allocated structure. */
		if (at->inStep->isPointer)
			subStep.base.walkLayer = subStep.baseStruct.walkLayer;
		
		/* Where is the actual pointer directly to this item? */
		subStep.valueP.walkLayer = (sjme_intPointer)voidList +
			voidList->elementOffset + (voidList->elementSize * i);
		
		/* Set item specific data. */
		subStep.index = i;
		subStep.inStep = variantStep;
		subStep.typeId.i = variantStep->typeId.i;
		subStep.javaType = variantStep->javaType;
		subStep.isPointer = variantStep->isPointer;

		/* Walk on this item. */
		if (sjme_error_is(error = sjme_nvm_walk_doItem(root, at,
			&subStep, function)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walk_doPhantom(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Set the type to be phantom. */
	at->isPhantom = SJME_JNI_TRUE;
	
	/* Directly walk on this! */
	if (sjme_error_is(error = sjme_nvm_walk_doItem(root, parent, at, function)))
		return sjme_error_default(error);

	/* Set to no longer be phantom before leaving. */
	at->isPhantom = SJME_JNI_FALSE;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walk_doStruct(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_jint atIndex, stepAdd;
	const sjme_nvm_walk_stepSelect* inSelect;
	const sjme_nvm_walk_step* currentStep;
	sjme_nvm_walk_state subStep;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* If there is no selection data, then we cannot walk this structure. */
	inSelect = at->inSelect;
	if (at->inSelect == NULL)
		return SJME_ERROR_NONE;

	/* Go through all steps. */
	for (atIndex = 0, currentStep = inSelect->steps;
		currentStep->memberName != NULL; currentStep += stepAdd, atIndex++)
	{
		/* Setup base step information. */
		memmove(&subStep, at, sizeof(subStep));
		subStep.uniqueId = ++at->nextUniqueId;
		subStep.depth = at->depth + 1;
		subStep.parent = at;
		
		/* Where is the actual pointer directly to this item? */
		subStep.valueP.walkLayer = at->baseStruct.walkLayer +
			currentStep->offset;

		/* Determine the structure that is being walked. */
		if (sjme_error_is(error = sjme_nvm_select(currentStep->typeId.i,
			&subStep.inSelect)))
			return sjme_error_default(error);
		
		/* Set item specific data. */
		subStep.index = atIndex;
		subStep.typeId.i = currentStep->typeId.i;
		subStep.javaType = currentStep->javaType;
		subStep.isPointer = currentStep->isPointer;
		subStep.inStep = currentStep;
		
		/* Is this a variant? That is an array or list. */
		if (subStep.typeId.i == SJME_NVM_WALK_PSEUDO_FIXED_ARRAY ||
			subStep.typeId.i == SJME_NVM_WALK_PSEUDO_LIST ||
			subStep.typeId.i == SJME_NVM_WALK_PSEUDO_PHANTOM ||
			subStep.typeId.i == SJME_NVM_WALK_PSEUDO_ATOMIC)
		{
			stepAdd = 2;
			subStep.variantStep = (currentStep + 1);
		}

		/* Not a variant. */
		else
		{
			stepAdd = 1;
			subStep.variantStep = NULL;
		}
		
		/* Walk on this item. */
		if (sjme_error_is(error = sjme_nvm_walk_doItem(root, at,
			&subStep, function)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walk_normalCustom(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;

	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* A select is required for the new type. */
	if (sjme_error_is(error = sjme_nvm_select(at->typeId.i,
		&at->inSelect)))
		return sjme_error_default(error);

	/* Start at the first step. */
	at->inStep = &at->inSelect->steps[0];
	at->variantStep = NULL;

	/* If level, call function handler. */
	if (at->breadth == SJME_NVM_WALK_BREADTH_LEVEL)
		return function(root, parent, at);

	/* Otherwise at the dive level, we are going in! */
	return sjme_nvm_walk/*at->stepItem*/(root, parent, at, function);
}

sjme_errorCode sjme_nvm_select(
	sjme_attrInValue sjme_jint typeId,
	sjme_attrOutNotNull const sjme_nvm_walk_stepSelect** outSelect)
{
	const sjme_nvm_walk_pseudoType* pseudo;
	const sjme_nvm_walk_stepSelect* select;

	if (outSelect == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Ignore purely pseudo items. */
	for (pseudo = &sjme_nvm_walk_pseudoOnly[0]; *pseudo != 0; pseudo++)
		if (typeId == *pseudo)
		{
			*outSelect = NULL;
			return SJME_ERROR_NONE;
		}

	/* Locate the walk stepper. */
	for (select = &sjme_nvm_walk_select[0]; select->typeName != NULL; select++)
		if (typeId == select->typeId)
		{
			*outSelect = select;
			return SJME_ERROR_NONE;
		}

	/* Not implemented. */
	sjme_todo("Impl? %d", typeId);
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_walk(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_nvm_walk_state subStep;
	sjme_nvm_walk_breadthType breadth;
	sjme_jboolean skipElements;
	sjme_nvm_walk_stepOuterFunc outer;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Skip walking null pointers. */
	if (at->base.walkLayer == 0 || at->baseStruct.walkLayer == 0)
		return SJME_ERROR_NONE;

	/* Try to get a selection for the current item where applicable. */
	if (sjme_error_is(error = sjme_nvm_select(at->typeId.i, &at->inSelect)))
		return sjme_error_default(error);
	
	/* Start a walk into the structure. */
	skipElements = SJME_JNI_FALSE;
	at->index = -1;
	if (sjme_error_is(error = function(root, parent, at)))
	{
		/* Skip element walk. */
		if (error == SJME_ERROR_WALK_SKIP_ELEMENTS)
			skipElements = SJME_JNI_TRUE;
		else
			return sjme_error_default(error);
	}
	
	/* Stepping over individual members? Walk in two breadths... */
	for (breadth = 0; breadth < SJME_NVM_WALK_NUM_BREADTH &&
		!skipElements; breadth++)
	{
		/* Determine the function used for outer stepping. */
		if (at->typeId.i == SJME_NVM_WALK_PSEUDO_FIXED_ARRAY)
			outer = sjme_nvm_walk_doArray;
		else if (at->typeId.i == SJME_NVM_WALK_PSEUDO_LIST)
			outer = sjme_nvm_walk_doList;
		else if (at->typeId.i == SJME_NVM_WALK_PSEUDO_PHANTOM)
			outer = sjme_nvm_walk_doPhantom;
		else if (at->typeId.i == SJME_NVM_WALK_PSEUDO_ATOMIC)
			outer = sjme_nvm_walk_doAtomic;
		else
			outer = sjme_nvm_walk_doStruct;

		/* Setup basic step. */
		memmove(&subStep, at, sizeof(subStep));
		subStep.root = at->root;
		subStep.uniqueId = ++at->nextUniqueId;
		subStep.breadth = breadth;
		subStep.depth = at->depth + 1;
		subStep.parent = at;

		/* The base structure is the value pointer if it is set. */
		/* Otherwise it just becomes the base of the object. */
		if (subStep.valueP.walkLayer != 0)
			subStep.baseStruct.walkLayer = subStep.valueP.walkLayer;
		else
			subStep.baseStruct.walkLayer = subStep.base.walkLayer;

		/* Perform stepping. */
		if (sjme_error_is(error = outer(root, at, &subStep,
			function)))
			return sjme_error_default(error);
	}
	
	/* End walk of structure. */
	/* If elements were skipped, then set a low index to indicate that. */
	at->index = (skipElements ? INT32_MIN : INT32_MAX);
	if (sjme_error_is(error = function(root, parent, at)))
	{
		/* Ignore this. */
		if (error != SJME_ERROR_WALK_SKIP_ELEMENTS)
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_walk_start(
	sjme_attrInNotNull sjme_pointer startAt,
	sjme_attrInValue sjme_jint typeId,
	sjme_attrInNotNull const sjme_nvm_walk_functions* functions,
	sjme_attrInNullable sjme_pointer anyData)
{
	sjme_errorCode error;
	const sjme_nvm_walk_stepSelect* select;
	sjme_nvm_walk_state rootState;
	sjme_nvm_walk_stageType stage;
	sjme_nvm_walk_stepHandlerFunc function;
	
	if (startAt == NULL || functions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Not implemented? */
	if (functions->step == NULL)
		return sjme_error_notImplemented(0);

	/* Find the selector for this. */
	if (sjme_error_is(error = sjme_nvm_select(typeId, &select)))
		return sjme_error_default(error);

	/* Not a structured type? */
	if (select == NULL)
		return SJME_ERROR_WALK_UNKNOWN_TYPE;

	/* Perform two-stage walk. */
	for (stage = 0; stage < SJME_NVM_WALK_NUM_STAGES; stage++)
	{
		/* Which function is being called? */
		function = (stage == SJME_NVM_WALK_STAGE_PRE ?
			functions->pre : functions->step);
		if (function == NULL)
			continue;
		
		/* Initialize root state. */
		memset(&rootState, 0, sizeof(rootState));
		rootState.root = &rootState;
		rootState.base.walkLayer = (sjme_intPointer)startAt;
		rootState.baseStruct.walkLayer = (sjme_intPointer)startAt;
		rootState.valueP.walkLayer = (sjme_intPointer)startAt;
		rootState.typeId.i = typeId;
		rootState.javaType = SJME_NUM_JAVA_TYPE_IDS;
		rootState.isPointer = SJME_JNI_TRUE;
		rootState.inSelect = select;
		rootState.functions = functions;
		rootState.index = -1;
		rootState.depth = 0;
		rootState.stage = stage;
		rootState.data = anyData;
		rootState.uniqueId = ++rootState.nextUniqueId;
		rootState.stepItem = sjme_nvm_walk_doItem;
		rootState.normalCustom = sjme_nvm_walk_normalCustom;
		
		/* Perform the recursive walk. */
		if (sjme_error_is(error = sjme_nvm_walk(&rootState, NULL,
			&rootState, function)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
