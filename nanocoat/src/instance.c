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

sjme_jint sjme_nvm_fieldValueSize(
	sjme_attrInValue sjme_extendedTypeId extendedType,
	sjme_attrInPositiveNonZero sjme_jint n)
{
	sjme_jint baseSize;

	if (extendedType < 0 || extendedType >= SJME_NUM_EXTENDED_JAVA_TYPE_IDS ||
		n < 0)
		return -1;
	
	if (extendedType == SJME_JAVA_TYPE_ID_OBJECT)
		baseSize = sizeof(sjme_nvm_valueObject);
	else
		baseSize = sjme_nvm_typeMul[extendedType];
	
	/* Base size is the offset of where values start */
	return (baseSize * n) +
		offsetof(sjme_nvm_valueSet, values) +
		offsetof(sjme_nvm_valueSetRaw, l);
}

sjme_errorCode sjme_nvm_instance_defaultInit(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNullable sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_lpcstr inDesc,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_jmethodID defaultCon;
	sjme_nvm_frame subFrame;
	sjme_jint callC;
	sjme_jvalueTyped* callV;

	if (contextThread == NULL || inObject == NULL || inDesc == NULL ||
		(argC > 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Locate the default constructor. */
	defaultCon = NULL;
	if (sjme_error_is(sjme_nvm_vmMethod_idByNameTypeU(
		sjme_atomic_g(sjme_jclass, &inObject->isClass),
		contextThread, SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_TRUE, "<init>", inDesc,
		&defaultCon)) || defaultCon == NULL)
		return SJME_ERROR_MLE_CALL;

	/* Allocate space for arguments to pass. */
	callC = argC + 1;
	callV = sjme_alloca(sizeof(*callV) * (callC + 1));
	if (callV == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(callV, 0, sizeof(*callV) * (callC + 1));

	/* Copy over arguments. */
	if (argC > 0 && argV != NULL)
		memmove(&callV[1], &argV[0], sizeof(argV[0]) * argC);

	/* Set base instance argument. */
	callV[0].t = SJME_JAVA_TYPE_ID_OBJECT;
	callV[0].v.l = inObject;
	
	/* Call the default constructor. */
	subFrame = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadEnter(contextThread,
		&subFrame, defaultCon, SJME_NVM_CALL_VIRTUAL,
		callC, callV)) || subFrame == NULL)
		return sjme_error_vmError(contextThread,
			sjme_error_defaultOr(error, SJME_ERROR_MLE_CALL));

	/* Give the resultant frame that was created. */
	if (outFrame != NULL)
		*outFrame = subFrame;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_initFields(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_pointer chunk,
	sjme_attrInNotNull sjme_list(sjme_jfieldID)* fields,
	sjme_attrInNotNull sjme_nvm_jclass_fields* placements)
{
	sjme_errorCode error;
	sjme_jint i, n;
	sjme_jfieldID field;
	sjme_nvm_jfieldAccessFunc accessor;
	sjme_nvm_value* direct;
	sjme_nvm_valueVolatile volatileStorage;
	sjme_nvm_class_fieldConstVal* constVal;
	sjme_jstring constString;

	if (contextThread == NULL || instance == NULL || chunk == NULL ||
		fields == NULL || placements == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Always use the default accessor to write initial field values, */
	/* however always initialize as volatile. */
	accessor = SJME_T_K(contextThread)->
		globals.accessor[SJME_NVM_FIELD_ACCESS_VOLATILE_INSTANCE];

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
		direct = accessor(instance, field, &volatileStorage);
		if (direct == NULL)
			return SJME_ERROR_FIELD_NOT_DIRECT;

		/* If a string, it needs to be initialized as a string object. */
		if (constVal->value.string != NULL &&
			field->info->javaType == SJME_JAVA_TYPE_ID_OBJECT)
		{
			/* Initialize. */
			constString = NULL;
			if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfP(
				contextThread, SJME_AS_JSTRINGP(&constString),
				constVal->value.string)) || constString == NULL)
				return sjme_error_vmError(contextThread,
					sjme_error_defaultOr(error,
						SJME_ERROR_STATIC_STRING_INIT));
			
			/* Set field. */
			if (sjme_error_is(error = sjme_nvm_vmField_cisSet(
				direct, field->info->basicType,
				NULL, SJME_VLS_JOBJECT(constString))))
				return sjme_error_vmError(contextThread, error);
		}

		/* Copy value directly if primitive. */
		else if (field->info->javaType != SJME_JAVA_TYPE_ID_OBJECT)
			memmove(direct, &constVal->value.java,
				sjme_nvm_typeMul[field->info->javaType]);
		
		/* Commit. */
		volatileStorage.commit = SJME_JNI_TRUE;
		accessor(instance, field, &volatileStorage);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_initFieldsChunk(
	sjme_attrInNotNull sjme_pointer chunk,
	sjme_attrInNotNull sjme_nvm_jclass_fields* placements)
{
	sjme_extendedTypeId type;
	sjme_nvm_valueSet* into;
	
	if (chunk == NULL || placements == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Placements are calculated for each type. */
	for (type = 0; type < SJME_NUM_EXTENDED_JAVA_TYPE_IDS; type++)
	{
		/* If there are no fields, ignore. */
		if (placements->count[type] == 0)
			continue;
		
		/* Not valid. */
		if (placements->offset[type] < 0)
			return SJME_ERROR_ILLEGAL_STATE;
		
		/* Determine the base offset to write at. */
		into = sjme_po(chunk, placements->offset[type]);

		/* Set details for the partition. */
		into->type = type;
		into->length = placements->count[type];
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_objectArrayNewR(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jarray* outObject,
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInPositive sjme_jint arrayLength
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_jclass arrayClass;
	sjme_jarray result;
	sjme_jint allocSize;
	sjme_cchar buf[SJME_NVM_CLASS_NAME_LIMIT];
	sjme_jint setSize;

	/* TODO FIXME: Optimize/shrink for SDCC. */
	
	if (contextThread == NULL || outObject == NULL || componentType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (arrayLength < 0)
		return sjme_error_vmError(contextThread,
			SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* Cannot be void. */
	if (componentType->arrayTypeId == SJME_JAVA_TYPE_ID_VOID)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Determine value set size. */
	setSize = INT32_C(-1);
	if (sjme_error_is(error = sjme_nvm_vmField_sizeValueSet(
		&setSize, componentType->arrayTypeId, arrayLength)))
		return sjme_error_vmError(contextThread, error);

	/* Determine the base allocation size. */
	allocSize = sizeof(*result) + 
		offsetof(sjme_jarrayBase, e) + setSize;
	if (allocSize <= 0)
		return sjme_error_vmError(contextThread, SJME_ERROR_TOO_LARGE);

	/* Determine array type class name. */
	memset(buf, 0, sizeof(buf));
	snprintf(buf, SJME_NVM_CLASS_NAME_LIMIT - 1,
		"[%s", sjme_charSeq_tempUtf(componentType->fieldName));

	/* Locate array type class. */
	arrayClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadFU(
		SJME_F_CL(contextThread),
		&arrayClass, contextThread, buf, SJME_JNI_TRUE)) ||
		arrayClass == NULL)
		return sjme_error_vmError(contextThread, error);
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNewR(contextThread,
		allocSize, SJME_NVM_STRUCT_ARRAY_INSTANCE,
		SJME_AS_JOBJECTP(&result), arrayClass SJME_DEBUG_ONLY_COMMA
		SJME_DEBUG_FILE_LINE_COPY)) || result == NULL)
		return sjme_error_vmError(contextThread, error);
	
	/* Setup array. */
	result->e.type = componentType->arrayTypeId;
	result->e.length = arrayLength;

	/* Success! */
	*outObject = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_objectArrayNewTR(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jarray* outObject,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_basicTypeId componentType,
	sjme_attrInPositive sjme_jint arrayLength
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
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
			commonId = SJME_NVM_COMMON_PRIMITIVE_BOOLEAN;
			break;

		case SJME_BASIC_TYPE_ID_BYTE:
			commonId = SJME_NVM_COMMON_PRIMITIVE_BYTE;
			break;

		case SJME_BASIC_TYPE_ID_SHORT:
			commonId = SJME_NVM_COMMON_PRIMITIVE_SHORT;
			break;

		case SJME_BASIC_TYPE_ID_CHARACTER:
			commonId = SJME_NVM_COMMON_PRIMITIVE_CHARACTER;
			break;

		case SJME_BASIC_TYPE_ID_INTEGER:
			commonId = SJME_NVM_COMMON_PRIMITIVE_INTEGER;
			break;

		case SJME_BASIC_TYPE_ID_LONG:
			commonId = SJME_NVM_COMMON_PRIMITIVE_LONG;
			break;

		case SJME_BASIC_TYPE_ID_FLOAT:
			commonId = SJME_NVM_COMMON_PRIMITIVE_FLOAT;
			break;

		case SJME_BASIC_TYPE_ID_DOUBLE:
			commonId = SJME_NVM_COMMON_PRIMITIVE_DOUBLE;
			break;

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Load the component class. */
	componentClass = NULL;
	if (sjme_error_is(error = sjme_nvm_task_commonClass(
		contextThread, commonId, &componentClass, SJME_JNI_TRUE)) ||
		componentClass == NULL)
		return sjme_error_vmError(contextThread, error);

	/* Forward initialize. */
	return sjme_nvm_instance_objectArrayNewR(contextThread, outObject,
		componentClass, arrayLength SJME_DEBUG_ONLY_COMMA
		SJME_DEBUG_FILE_LINE_COPY);
}

sjme_errorCode sjme_nvm_instance_objectNewR(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNegativeOnePositive sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_jclass inClass
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
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
			SJME_NVM_COMMON_CLASS))
			return SJME_ERROR_INVALID_ARGUMENT;
		
		/* Remap @c String . */
		else if (inClass == sjme_nvm_task_commonClassR(contextThread,
			SJME_NVM_COMMON_STRING))
		{
			inType = SJME_NVM_STRUCT_STRING_INSTANCE;
			allocSize = sizeof(sjme_jstringBase);
		}

		/* Remap Thread */
		else if (inClass == sjme_nvm_task_commonClassR(contextThread,
			SJME_NVM_COMMON_THREAD))
		{
			inType = SJME_NVM_STRUCT_THREAD_INSTANCE;
			allocSize = sizeof(sjme_nvm_threadBase);
		}

		/* Remap Thread subclass. */
		/* This becomes the actual allocation size of the subclass as this */
		/* can get a number of fields added to it. */
		/* Thus, these become actual threads, magically. */
		else if (SJME_NVM_ACC_IS(inClass->special, SPECIAL_THREAD_SUBCLASS))
		{
			inType = SJME_NVM_STRUCT_THREAD_INSTANCE;
			allocSize =
				inClass->fields[SJME_NVM_CLASS_MEMBER_INSTANCE].allocSize;
		}
		
		/* Remap @c Reference based classes, however specifically */
		/* limit to a small selection of reference based classes. */
		/* This is so that any aliases are treated the same regardless. */
		else if (inClass == sjme_nvm_task_commonClassR(contextThread,
				SJME_NVM_COMMON_REFERENCE_PHANTOM) ||
			inClass == sjme_nvm_task_commonClassR(contextThread,
				SJME_NVM_COMMON_REFERENCE_SOFT) ||
			inClass == sjme_nvm_task_commonClassR(contextThread,
				SJME_NVM_COMMON_REFERENCE_WEAK))
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
	if (sjme_error_is(error = sjme_nvm_allocR(
		sjme_atomic_g(sjme_nvm, &contextThread->inState),
		allocSize, inType,
		SJME_AS_NVM_COMMONP(&result) SJME_DEBUG_ONLY_COMMA
		SJME_DEBUG_FILE_LINE_COPY)) || result == NULL)
		return sjme_error_vmError(contextThread, error);
	
	/* Setup object. */
	sjme_atomic_s(sjme_jclass, &result->isClass, sjme_weakUp(inClass));
	result->identityHash = sjme_nvm_instance_calcIdentityHash(
		SJME_T_K(contextThread), result);

	/* Remember the task as it may need to be recovered later. */
	sjme_atomic_s(sjme_nvm_task, &result->phantomTask,
		SJME_T_K(contextThread));
	
	/* Success! */
	*outObject = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_objectNewBracketR(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrInNegativeOnePositive sjme_jint subType,
	sjme_attrOutNotNull sjme_jobject* outObject
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_nvm_task_commonClassId commonId;
	sjme_jint allocSize;
	sjme_jobject result;
	
	if (contextThread == NULL || outObject == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Only certain brackets have a sub-type */
	if (inType != SJME_NVM_STRUCT_BRACKET_SCRITCH_UI && subType != -1)
		return SJME_ERROR_INVALID_ARGUMENT;
	else if (inType == SJME_NVM_STRUCT_BRACKET_SCRITCH_UI &&
		(subType <= SJME_SCRITCHUI_TYPE_RESERVED ||
			subType >= SJME_SCRITCHUI_NUM_UI_TYPES))
		return SJME_ERROR_INVALID_ARGUMENT;

	/* For ScritchUI */
#define SJME_NVM_CC_SCRITCHUI_ALT(caps, alt) \
		case alt: \
			commonId = SJME_TOKEN_PASTE_PP(SJME_NVM_COMMON_SCRITCH_UI_TYPE_, \
				caps); \
			break
#define SJME_NVM_CC_SCRITCHUI(caps) \
	SJME_NVM_CC_SCRITCHUI_ALT(caps, \
		SJME_TOKEN_PASTE_PP(SJME_SCRITCHUI_TYPE_, caps))

	/* Determine size and type. */
	switch (inType)
	{
		case SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE:
			commonId = SJME_NVM_COMMON_JAR_PACKAGE;
			allocSize = sizeof(sjme_jbracketJarPackageBase);
			break;

		case SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE:
			commonId = SJME_NVM_COMMON_PIPE;
			allocSize = sizeof(sjme_jbracketPipeBase);
			break;

			/* ScritchUI types, note that pseudo types such as base, */
			/* container, and otherwise are not here as they do not truly */
			/* exist. */
		case SJME_NVM_STRUCT_BRACKET_SCRITCH_UI:
			allocSize = sizeof(sjme_jbracketScritchUiBase);
			switch (subType)
			{
				SJME_NVM_CC_SCRITCHUI(LIST);
				SJME_NVM_CC_SCRITCHUI(MENU_BAR);
				SJME_NVM_CC_SCRITCHUI(MENU);
				SJME_NVM_CC_SCRITCHUI(MENU_ITEM);
				SJME_NVM_CC_SCRITCHUI(PANEL);
				SJME_NVM_CC_SCRITCHUI(PENCIL);
				SJME_NVM_CC_SCRITCHUI(SCREEN);
				SJME_NVM_CC_SCRITCHUI(SCROLL_PANEL);
				SJME_NVM_CC_SCRITCHUI_ALT(STATE,
					SJME_SCRITCHUI_TYPE_ROOT_STATE);
				SJME_NVM_CC_SCRITCHUI(WINDOW);

				/* TODO: not yet implemented. */
#if defined(SJME_CONFIG_TODO)
				SJME_NVM_CC_SCRITCHUI(BUTTON);
				SJME_NVM_CC_SCRITCHUI(COMBOBOX);
				SJME_NVM_CC_SCRITCHUI(PROGRESSBAR);
				SJME_NVM_CC_SCRITCHUI(SCROLLBAR);
				SJME_NVM_CC_SCRITCHUI(SLIDER);
				SJME_NVM_CC_SCRITCHUI(TEXTBOX);
#endif

				/* Pseudo types that are not real. */
#if 0 && SCRITCHUI_THESE_ARE_PSEUDO_TYPES
				SJME_NVM_CC_SCRITCHUI(BASE);
				SJME_NVM_CC_SCRITCHUI(CHOICE);
				SJME_NVM_CC_SCRITCHUI(COMPONENT);
				SJME_NVM_CC_SCRITCHUI(CONTAINER);
				SJME_NVM_CC_SCRITCHUI(LABEL);
				SJME_NVM_CC_SCRITCHUI(MENU_HAS_CHILDREN);
				SJME_NVM_CC_SCRITCHUI(MENU_HAS_LABEL);
				SJME_NVM_CC_SCRITCHUI(MENU_HAS_PARENT);
				SJME_NVM_CC_SCRITCHUI(MENU_KIND);
				SJME_NVM_CC_SCRITCHUI(PAINTABLE);
				SJME_NVM_CC_SCRITCHUI(TRIGGER);
				SJME_NVM_CC_SCRITCHUI(VIEW);
#endif

				default:
					sjme_todo("Impl?");
					return sjme_error_notImplemented(0);
			}
			break;
		
		case SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE:
			commonId = SJME_NVM_COMMON_TRACE_POINT;
			allocSize = sizeof(sjme_jbracketTraceBase);
			break;
			
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* No longer needed. */
#undef SJME_NVM_CC_SCRITCHUI

	/* Allocate. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNewR(contextThread,
		allocSize, inType, &result, sjme_nvm_task_commonClassR(contextThread,
			commonId) SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
			result == NULL)
		return sjme_error_default(error);

	/* Set subtype? */
	if (inType == SJME_NVM_STRUCT_BRACKET_SCRITCH_UI)
		((sjme_jbracketScritchUi)result)->type = subType;

	/* Success! */
	*outObject = result;
	return SJME_ERROR_NONE;
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
