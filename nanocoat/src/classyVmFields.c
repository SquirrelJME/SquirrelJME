/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/classyVmFields.h"
#include "sjme/util.h"
#include "sjme/nvm/instance.h"

sjme_errorCode sjme_nvm_vmField_cisGet(
	sjme_attrInNotNull sjme_nvm_value* srcValue,
	sjme_attrInRange(-SJME_NVM_VMFIELD_NUM_VAR, 0)
		sjme_nvm_vmField_var SJME_VLG_,
	...)
{
	if (srcValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (SJME_VLG_ < 0 || SJME_VLG_ >= SJME_NVM_VMFIELD_NUM_VAR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmField_cisGetS(
	sjme_attrInOutNotNull sjme_nvm_valueSet* srcSet,
	sjme_attrInPositive sjme_jint getIndex,
	sjme_attrInRange(-SJME_NVM_VMFIELD_NUM_VAR, 0)
		sjme_nvm_vmField_var SJME_VLG_,
	...)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmField_cisSet(
	sjme_attrInOutNotNull sjme_nvm_value* destValue,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VLS_,
	...)
{
	if (destValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (SJME_VLS_ < 0 || SJME_VLS_ >= SJME_NVM_VMFIELD_NUM_VAR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmField_cisSetS(
	sjme_attrInOutNotNull sjme_nvm_valueSet* destSet,
	sjme_attrInPositive sjme_jint setIndex,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VLS_,
	...)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmField_idByNameType(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_charSeq inName,
	sjme_attrInPositive sjme_charSeq inType,
	sjme_attrOutNotNull sjme_jfieldID* outID)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_list(sjme_jfieldID)* fields;
	sjme_jfieldID field;
	sjme_jclass pivot;
	sjme_jint wantHash;
	
	if (inClass == NULL || contextThread == NULL || inName == NULL ||
		inType == NULL || outID == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Needs to be initialized first. */
	if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
		inClass, contextThread)))
		return sjme_error_default(error);

	/* Calculate the hash to lookup. */
	wantHash = sjme_nvm_class_idHashMember(inName, inType);
	
	/* Look through all fields. */
	for (pivot = inClass; pivot != NULL; pivot = SJME_C_SU(pivot))
	{
		/* It is possible for there to be no fields in this scope. */
		fields = pivot->fields[instanceType].binds;
		if (fields == NULL)
			continue;
		
		/* Find matching field. */
		for (i = fields->length - 1; i >= 0; i--)
		{
			/* There must be a valid method here. */
			field = fields->elements[i];
			if (field == NULL)
				return sjme_error_vmError(contextThread,
					SJME_ERROR_NO_METHOD);
			
			/* Check against the hash, which is faster. */
			if (field->member.idHash != wantHash)
				continue;
			
			/* Is this the method. */
			if (sjme_charSeq_equalsR(SJME_M_N(field)->seq, inName) &&
				sjme_charSeq_equalsR(SJME_M_T(field)->seq, inType))
			{
				*outID = field;
				return SJME_ERROR_NONE;
			}
		}
	}

	/* Not found. */
	if (!required)
		return SJME_ERROR_NO_FIELD;
	return sjme_error_vmError(contextThread, SJME_ERROR_NO_FIELD);
}

sjme_errorCode sjme_nvm_vmField_idByNameTypeU(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_lpcstr inName,
	sjme_attrInPositive sjme_lpcstr inType,
	sjme_attrOutNotNull sjme_jfieldID* outID)
{
	sjme_errorCode error;
	sjme_charSeqStatic wrapName, wrapType;
	
	if (inName == NULL || inType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Wrap sequences. */
	memset(&wrapName, 0, sizeof(wrapName));
	memset(&wrapType, 0, sizeof(wrapType));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&wrapName,
		inName, 0, -1)))
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&wrapType,
		inType, 0, -1)))
		return sjme_error_default(error);

	/* Forward. */
	return sjme_nvm_vmField_idByNameType(inClass, contextThread,
		instanceType, required, &wrapName, &wrapType, outID);
}

sjme_errorCode sjme_nvm_vmField_sourceByIndex(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS)
		sjme_extendedTypeId extendedType,
	sjme_attrInPositive sjme_jint fieldId,
	sjme_attrOutNotNull sjme_nvm_class_fieldInfo* outInfo)
{
	sjme_list(sjme_nvm_class_fieldInfo)* fields;
	sjme_jint i, n, base;
	sjme_jclass atClass;
	sjme_jboolean wantStatic;
	sjme_nvm_class_fieldInfo field;
	
	if (inClass == NULL || outInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE ||
		extendedType < 0 || extendedType >= SJME_NUM_EXTENDED_JAVA_TYPE_IDS ||
		extendedType == SJME_BASIC_TYPE_ID_VOID)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (fieldId < 0 ||
		fieldId >= inClass->fields[instanceType].count[extendedType])
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Do we want static? */
	wantStatic = (instanceType == SJME_NVM_CLASS_MEMBER_STATIC);
	
	/* Start at the current class for the search. */
	atClass = inClass;
	
	/* If we are below the class index, drop to the super class. */
	while (fieldId < atClass->fields[instanceType].base[extendedType])
	{
		atClass = SJME_C_SU(atClass);
		
		/* This should not occur. */
		if (atClass == NULL)
			return sjme_error_vmError(NULL,
				SJME_ERROR_SUPER_CLASS_INVALID);
	}

	/* Find the associated field. */
	base = atClass->fields[instanceType].base[extendedType];
	fields = atClass->info->fields;
	for (i = 0, n = fields->length; i < n; i++)
	{
		/* Get the method here. */
		field = fields->elements[i];
		if (field == NULL)
			return sjme_error_vmError(NULL, SJME_ERROR_NO_FIELD);
		
		/* If the static flag, index, and type matches, this is the one! */
		if (SJME_NVM_ACC_IS(field->flags, STATIC) == wantStatic &&
			field->typedIndex == (fieldId - base) &&
			field->javaType == extendedType)
		{
			*outInfo = field;
			return SJME_ERROR_NONE;
		}
	}
	
	/* If this point is reached, the index is not valid. */
	return sjme_error_vmError(NULL, SJME_ERROR_NO_FIELD);
}

