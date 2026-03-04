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
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/task.h"

#define SJME_NVM_VMFIELD_DECOMPOSE_DECL(prefix) \
	sjme_errorCode error; \
	sjme_basicTypeId type; \
	prefix sjme_jvaluePrimitive* prim; \
	prefix sjme_jint* objC; \
	prefix sjme_jobject* objP; \
	va_list args

#define sjme_nvm_vmField_decomposeValue(value, inType) \
	do { \
		(type) = (inType); \
		(prim) = &(value)->v; \
		(objC) = &(value)->l.check; \
		(objP) = &(value)->l.p; \
	} while (0)

#define sjme_nvm_vmField_decomposeValueSet(value, index, prefix) \
	do { \
		/* Initialize everything to NULL. */ \
		(prim) = NULL; \
		(objC) = NULL; \
		(objP) = NULL; \
		/* This really depends on the type. */ \
		type = (value)->type; \
		switch (type) \
		{ \
			case SJME_BASIC_TYPE_ID_INTEGER: \
				(prim) = (prefix sjme_jvaluePrimitive*)( \
					&(value)->values.i[index]); \
				break; \
			case SJME_BASIC_TYPE_ID_LONG: \
				(prim) = (prefix sjme_jvaluePrimitive*)( \
					&(value)->values.j[index]); \
				break; \
			case SJME_BASIC_TYPE_ID_FLOAT: \
				(prim) = (prefix sjme_jvaluePrimitive*)( \
					&(value)->values.f[index]); \
				break; \
			case SJME_BASIC_TYPE_ID_DOUBLE: \
				(prim) = (prefix sjme_jvaluePrimitive*)( \
					&(value)->values.d[index]); \
				break; \
			case SJME_BASIC_TYPE_ID_BOOLEAN: \
			case SJME_BASIC_TYPE_ID_BYTE: \
				(prim) = (prefix sjme_jvaluePrimitive*)( \
					&(value)->values.b[index]); \
				break; \
			case SJME_BASIC_TYPE_ID_SHORT: \
				(prim) = (prefix sjme_jvaluePrimitive*)( \
					&(value)->values.s[index]); \
				break; \
			case SJME_BASIC_TYPE_ID_CHARACTER: \
				(prim) = (prefix sjme_jvaluePrimitive*)( \
					&(value)->values.c[index]); \
				break; \
			case SJME_BASIC_TYPE_ID_OBJECT: \
				(objC) = &((value)->values.l[index].check); \
				(objP) = &((value)->values.l[index].p); \
				break; \
			default: \
				return SJME_ERROR_INVALID_FIELD_TYPE; \
		} \
	} while (0)

#define sjme_nvm_vmField_operate(SJME_VLX_, extra) \
	do { \
		/* Read in arguments. */ \
		va_start(args, SJME_VLX_); \
		/* Forward. */ \
		if (sjme_error_is(error = SJME_TOKEN_PASTE3_PP( \
			sjme_nvm_vmField_operate, _, SJME_VLX_)(SJME_VLX_, type, prim, \
				objC, objP, (extra), args))) \
			return sjme_error_default(error); \
		/* Cleanup arguments. */ \
		va_end(args); \
	} while (0)

static sjme_errorCode sjme_nvm_vmField_operate_SJME_VLG_(
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VLG_,
	sjme_attrInValue sjme_basicTypeId type,
	sjme_attrInNullable const sjme_jvaluePrimitive* prim,
	sjme_attrInNullable const sjme_jint* objC,
	sjme_attrInNullable const sjme_jobject* objP,
	sjme_attrUnused sjme_pointer ignored,
	sjme_attrInValue va_list args)
{
	sjme_jobject checkObj;
	sjme_jvalueTyped* typedP;
	sjme_jvalue* valueP;
	
	if ((prim == NULL && objC == NULL && objP == NULL) ||
		((objC == NULL) != (objP == NULL)))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (SJME_VLG_ <= 0 || SJME_VLG_ >= SJME_NVM_VMFIELD_NUM_VAR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* If getting an object, make sure it did not change on us. */
	checkObj = NULL;
	if (type == SJME_JAVA_TYPE_ID_OBJECT)
	{
		/* Cannot get object values for this field? */
		if (objC == NULL || objP == NULL)
			return SJME_ERROR_INVALID_FIELD_TYPE;
			
		/* Did the object change on us? */
		checkObj = (*objP);
		if (checkObj != NULL && checkObj->identityHash != (*objC))
			return sjme_error_vmError(NULL, SJME_ERROR_OBJECT_GONE);
	}
	
	/* Read in typed value pointer? */
	valueP = NULL;
	typedP = NULL;
	if (SJME_VLG_ == SJME_NVM_VMFIELD_VAR_JVALUE_TYPED_P)
	{
		/* Read in. */
		typedP = va_arg(args, sjme_jvalueTyped*);
		
		/* Cannot be null. */
		if (typedP == NULL)
			return SJME_ERROR_NULL_ARGUMENTS;
		
		/* Value pointer is just part of the sub-structure. */
		valueP = &typedP->v;
	}
	
	/* Determine value to get. */
	switch (SJME_VLG_)
	{
		case SJME_NVM_VMFIELD_VAR_JVALUE_P:
		case SJME_NVM_VMFIELD_VAR_JVALUE_TYPED_P:
			/* Need to read in? */
			if (valueP == NULL)
			{
				/* Read in. */
				valueP = va_arg(args, sjme_jvalue*);
				
				/* Cannot be null. */
				if (valueP == NULL)
					return SJME_ERROR_NULL_ARGUMENTS;
			}
			
			/* Clear destination before receiving. */
			memset(valueP, 0, sizeof(*valueP));
			
			/* Read in, promote to a Java type. */
			if (typedP != NULL)
				typedP->t = sjme_nvm_typePromote[type];
			
			/* Depending on the input type, we need to promote. */
			switch (type)
			{
				case SJME_JAVA_TYPE_ID_INTEGER:
					valueP->i = prim->i;
					break;
				
				case SJME_JAVA_TYPE_ID_OBJECT:
					valueP->l = checkObj;
					break;
				
				default:
					sjme_todo("Impl %d %d?", SJME_VLG_, type);
					return sjme_error_notImplemented(0);
			}
			break;
		
		case SJME_NVM_VMFIELD_VAR_JOBJECT_P:
			/* Must be an object. */
			if (type != SJME_JAVA_TYPE_ID_OBJECT)
				return SJME_ERROR_INVALID_FIELD_TYPE;
			
			/* Copy the object out. */
			*va_arg(args, sjme_jobject*) = checkObj;
			break;
			
			/* Non-pointer types are not valid. */
		case SJME_NVM_VMFIELD_VAR_JVALUE:
		case SJME_NVM_VMFIELD_VAR_JOBJECT:
		case SJME_NVM_VMFIELD_VAR_JVALUE_TYPED:
			return SJME_ERROR_INVALID_ARGUMENT;
		
		default:
			sjme_todo("Impl %d?", SJME_VLG_);
			return sjme_error_notImplemented(0);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_vmField_operate_SJME_VLS_(
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VLS_,
	sjme_attrInValue sjme_basicTypeId type,
	sjme_attrInNullable sjme_jvaluePrimitive* prim,
	sjme_attrInNullable sjme_jint* objC,
	sjme_attrInNullable sjme_jobject* objP,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInValue va_list args)
{
	sjme_errorCode error;
	sjme_jvalueTyped set;
	sjme_jobject oldObject;
	sjme_jobject* setObjectP;
	sjme_jvalueTyped* typedP;
	sjme_jvalue* valueP;
	
	if ((prim == NULL && objC == NULL && objP == NULL) ||
		((objC == NULL) != (objP == NULL)))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (SJME_VLS_ <= 0 || SJME_VLS_ >= SJME_NVM_VMFIELD_NUM_VAR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Determine value to set. */
	memset(&set, 0, sizeof(set));
	switch (SJME_VLS_)
	{
		case SJME_NVM_VMFIELD_VAR_JVALUE:
			/* Copy over. */
			set.t = sjme_nvm_typePromote[type];
			set.v = va_arg(args, sjme_jvalue);
			break;
			
		case SJME_NVM_VMFIELD_VAR_JVALUE_P:
			/* Cannot be null. */
			valueP = va_arg(args, sjme_jvalue*);
			if (valueP == NULL)
				return SJME_ERROR_NULL_ARGUMENTS;
			
			/* Copy over. */
			set.t = sjme_nvm_typePromote[type];
			set.v = *valueP;
			break;
		
		case SJME_NVM_VMFIELD_VAR_JVALUE_TYPED:
			/* Copy over. */
			set = va_arg(args, sjme_jvalueTyped);
			break;
			
		case SJME_NVM_VMFIELD_VAR_JVALUE_TYPED_P:
			/* Cannot be null. */
			typedP = va_arg(args, sjme_jvalueTyped*);
			if (typedP == NULL)
				return SJME_ERROR_NULL_ARGUMENTS;
			
			/* Copy over. */
			set = *typedP;
			break;
		
		case SJME_NVM_VMFIELD_VAR_JOBJECT:
			set.t = SJME_JAVA_TYPE_ID_OBJECT;
			set.v.l = va_arg(args, sjme_jobject);
			break;
			
		case SJME_NVM_VMFIELD_VAR_JOBJECT_P:
			/* Cannot be null. */
			setObjectP = va_arg(args, sjme_jobject*);
			if (setObjectP == NULL)
				return SJME_ERROR_NULL_ARGUMENTS;
			
			/* Read out. */
			set.t = SJME_JAVA_TYPE_ID_OBJECT;
			set.v.l = *setObjectP;
			break;
		
		default:
			sjme_todo("Impl %d?", SJME_VLS_);
			return sjme_error_notImplemented(0);
	}
	
	/* Set object value? */
	if (set.t == SJME_JAVA_TYPE_ID_OBJECT)
	{
		/* Cannot set object values for this field? */
		if (objC == NULL || objP == NULL)
			return SJME_ERROR_INVALID_FIELD_TYPE;
		
		/* GC old object? */
		oldObject = (*objP);
		if (oldObject != NULL)
		{
			/* Value was wrongly GCed, or other memory corruption? */
			if (!sjme_nvm_isAR(oldObject,
				SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE) ||
				(*objC) != oldObject->identityHash)
				return sjme_error_vmError(NULL, SJME_ERROR_OBJECT_GONE);
			
			/* Clear old information here. */
			(*objC) = 0;
			(*objP) = NULL;
			
			/* Direct GC? */
			if (commit == NULL)
			{
				if (sjme_error_is(error = sjme_alloc_weakUnRef(oldObject)))
					return sjme_error_default(error);
			}
			
			/* Otherwise commit it. */
			else
			{
				if (sjme_error_is(error = sjme_nvm_task_frameCommitPush(NULL,
					commit, oldObject)))
					return sjme_error_default(error);
			}
		}
		
		/* Set new object. */
		if (set.v.l != NULL)
			(*objC) = set.v.l->identityHash;
		(*objP) = sjme_weakUpR(sjme_jobject, set.v.l);
	}
	
	/* Non-object. */
	else
	{
		/* Cannot set primitives for this field? */
		if (prim == NULL)
			return SJME_ERROR_INVALID_FIELD_TYPE;
		
		switch (type)
		{
			case SJME_BASIC_TYPE_ID_CHARACTER:
				prim->c = (sjme_jchar)(set.v.i & 0xFFFF);
				break;
				
			case SJME_JAVA_TYPE_ID_INTEGER:
				prim->i = set.v.i;
				break;
			
			default:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
		}
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_vmField_cisGet(
	sjme_attrInNotNull const sjme_nvm_value* srcValue,
	sjme_attrInValue sjme_basicTypeId srcType,
	sjme_attrInRange(-SJME_NVM_VMFIELD_NUM_VAR, 0)
		sjme_nvm_vmField_var SJME_VLG_,
	...)
{
	SJME_NVM_VMFIELD_DECOMPOSE_DECL(const);
	
	if (srcValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (srcType < 0 || srcType >= SJME_NUM_BASIC_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	SJME_VLG_ = -SJME_VLG_;
	if (SJME_VLG_ <= 0 || SJME_VLG_ >= SJME_NVM_VMFIELD_NUM_VAR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Decompose. */
	sjme_nvm_vmField_decomposeValue(srcValue, srcType);
	
	/* Operate. */
	sjme_nvm_vmField_operate(SJME_VLG_, NULL);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_vmField_cisGetS(
	sjme_attrInNotNull const sjme_nvm_valueSet* srcSet,
	sjme_attrInPositive sjme_jint getIndex,
	sjme_attrInRange(-SJME_NVM_VMFIELD_NUM_VAR, 0)
		sjme_nvm_vmField_var SJME_VLG_,
	...)
{
	SJME_NVM_VMFIELD_DECOMPOSE_DECL(const);
	
	if (srcSet == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_VLG_ = -SJME_VLG_;
	if (SJME_VLG_ <= 0 || SJME_VLG_ >= SJME_NVM_VMFIELD_NUM_VAR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (getIndex < 0 || getIndex >= srcSet->length)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Decompose. */
	sjme_nvm_vmField_decomposeValueSet(srcSet, getIndex, const);
	
	/* Operate. */
	sjme_nvm_vmField_operate(SJME_VLG_, NULL);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_vmField_cisSet(
	sjme_attrInOutNotNull sjme_nvm_value* destValue,
	sjme_attrInValue sjme_basicTypeId destType,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VLS_,
	...)
{
	SJME_NVM_VMFIELD_DECOMPOSE_DECL();
	
	if (destValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (destType < 0 || destType >= SJME_NUM_BASIC_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (SJME_VLS_ <= 0 || SJME_VLS_ >= SJME_NVM_VMFIELD_NUM_VAR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Decompose. */
	sjme_nvm_vmField_decomposeValue(destValue, destType);
	
	/* Operate. */
	sjme_nvm_vmField_operate(SJME_VLS_, commit);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_vmField_cisSetS(
	sjme_attrInOutNotNull sjme_nvm_valueSet* destSet,
	sjme_attrInPositive sjme_jint setIndex,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VLS_,
	...)
{
	SJME_NVM_VMFIELD_DECOMPOSE_DECL();
	
	if (destSet == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (SJME_VLS_ <= 0 || SJME_VLS_ >= SJME_NVM_VMFIELD_NUM_VAR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (setIndex < 0 || setIndex >= destSet->length)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Decompose. */
	sjme_nvm_vmField_decomposeValueSet(destSet, setIndex, );
	
	/* Operate. */
	sjme_nvm_vmField_operate(SJME_VLS_, commit);
	
	/* Success! */
	return SJME_ERROR_NONE;
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

sjme_errorCode sjme_nvm_vmField_sizeValueSet(
	sjme_attrOutNotNull sjme_jint* outSize,
	sjme_attrInValue sjme_basicTypeId type,
	sjme_attrInPositive sjme_jint length)
{
	sjme_jint result;
	
	if (outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length < 0 || type < 0 || type >= SJME_NUM_EXTENDED_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Calculate the size of all values, make sure there is no overflow. */
	result = (sjme_nvm_typeMul[type] * length);
	if (length != 0)
		if (result <= 0 || (result / sjme_nvm_typeMul[type]) != length)
			return SJME_ERROR_TOO_LARGE;
	
	/* Offset up the base size. */
	result += offsetof(sjme_nvm_valueSet, values.l[0]);
	if (result <= 0)
		return SJME_ERROR_TOO_LARGE;
	
	/* Return the resultant size. */
	*outSize = result;
	return SJME_ERROR_NONE;
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

