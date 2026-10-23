/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/task.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/classy.h"
#include "sjme/debug.h"
#include "sjme/util.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/classyUtil.h"
#include "sjme/nvm/cleanup.h"

/** The magic number for classes. */
#define SJME_NVM_CLASS_MAGIC INT32_C(0xCAFEBABE)

/** CLDC 1.1 max version (JSR 30). */
#define SJME_NVM_CLASS_CLDC_1_0_MAX INT32_C(3080191)

/** CLDC 1.1 max version. (JSR 139). */
#define SJME_NVM_CLASS_CLDC_1_1_MAX INT32_C(3342335)

/** CLDC 8 max version. */
#define SJME_NVM_CLASS_CLDC_1_8_MAX INT32_C(3407872)

static const sjme_nvm_class_parseAttributeHandler sjme_nvm_class_classAttr[] =
{
	{"RuntimeVisibleAnnotations",
		sjme_nvm_class_classAnnotations},
	
	{NULL, NULL},
};

static sjme_errorCode sjme_nvm_class_classFlagsParse(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_nvm_class_classFlags* outFlags)
{
	sjme_errorCode error;
	sjme_jshort rawFlags;
	
	if (inStream == NULL || outFlags == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in flags. */
	rawFlags = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &rawFlags)) || rawFlags < 0)
		return sjme_error_default(error);
	
	/* Translate to bitfield. */
	if (SJME_NVM_ACC_IS(rawFlags, PUBLIC))
		(*outFlags) |= SJME_NVM_ACC_PUBLIC;
	if (SJME_NVM_ACC_IS(rawFlags, FINAL))
		(*outFlags) |= SJME_NVM_ACC_FINAL;
	if (SJME_NVM_ACC_IS(rawFlags, SUPER))
		(*outFlags) |= SJME_NVM_ACC_SUPER;
	if (SJME_NVM_ACC_IS(rawFlags, INTERFACE))
		(*outFlags) |= SJME_NVM_ACC_INTERFACE;
	if (SJME_NVM_ACC_IS(rawFlags, ABSTRACT))
		(*outFlags) |= SJME_NVM_ACC_ABSTRACT;
	if (SJME_NVM_ACC_IS(rawFlags, SYNTHETIC))
		(*outFlags) |= SJME_NVM_ACC_SYNTHETIC;
	if (SJME_NVM_ACC_IS(rawFlags, ANNOTATION))
		(*outFlags) |= SJME_NVM_ACC_ANNOTATION;
	if (SJME_NVM_ACC_IS(rawFlags, ENUM))
		(*outFlags) |= SJME_NVM_ACC_ENUM;
	
	/* Cannot be abstract and final. */
	if (SJME_NVM_ACC_IS((*outFlags), ABSTRACT) &&
		SJME_NVM_ACC_IS((*outFlags), FINAL))
		return SJME_ERROR_INVALID_CLASS_FLAGS;
	
	/* Annotation must be an interface. */
	if (SJME_NVM_ACC_IS((*outFlags), ANNOTATION) &&
		!SJME_NVM_ACC_IS((*outFlags), INTERFACE))
		return SJME_ERROR_INVALID_CLASS_FLAGS;
	
	/* Interface must be abstract and not final, super, or enum */
	if (SJME_NVM_ACC_IS((*outFlags), INTERFACE))
		if (!SJME_NVM_ACC_IS((*outFlags), ABSTRACT) ||
			SJME_NVM_ACC_IS((*outFlags), FINAL) ||
			SJME_NVM_ACC_IS((*outFlags), SUPER) ||
			SJME_NVM_ACC_IS((*outFlags), ENUM))
		return SJME_ERROR_INVALID_CLASS_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_class_fieldAttrConstantValue(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_charSeq attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	sjme_errorCode error;
	sjme_nvm_class_fieldInfo fieldInfo;
	sjme_jshort index;
	sjme_nvm_class_poolEntry* item;
	
	fieldInfo = context;
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL || fieldInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read the constant value index. */
	index = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		attrStream, &index)) || index < 0)
		return sjme_error_default(error);
	
	/* Make sure it is valid. */
	if (index <= 0 || index >= inConstPool->pool->length)
		return SJME_ERROR_INVALID_CLASS_POOL_INDEX;
	
	/* Process based on the pool type used. */
	item = &inConstPool->pool->elements[index];
	if (item->type == SJME_NVM_CLASS_POOL_TYPE_INTEGER)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_INTEGER;
		fieldInfo->constVal.value.java.i = item->constInteger.value;
	}
	else if (item->type == SJME_NVM_CLASS_POOL_TYPE_FLOAT)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_FLOAT;
		fieldInfo->constVal.value.java.f = item->constFloat.value;
	}
	else if (item->type == SJME_NVM_CLASS_POOL_TYPE_LONG)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_LONG;
		fieldInfo->constVal.value.java.j = item->constLong.value;
	}
	else if (item->type == SJME_NVM_CLASS_POOL_TYPE_DOUBLE)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_DOUBLE;
		fieldInfo->constVal.value.java.d = item->constDouble.value;
	}
	else if (item->type == SJME_NVM_CLASS_POOL_TYPE_STRING)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_OBJECT;
		fieldInfo->constVal.value.string =
			sjme_weakUp(item->constString.value);
	}
	
	/* Invalid! */
	else
		return SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_nvm_class_parseAttributeHandler sjme_nvm_class_fieldAttr[] =
{
	{"ConstantValue", sjme_nvm_class_fieldAttrConstantValue},
	{NULL, NULL},
};

static sjme_errorCode sjme_nvm_class_fieldFlagsParse(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_nvm_class_fieldFlags* outFlags)
{
	sjme_errorCode error;
	sjme_jshort rawFlags;
	
	if (inStream == NULL || outFlags == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in flags. */
	rawFlags = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &rawFlags)) || rawFlags < 0)
		return sjme_error_default(error);
	
	/* Translate to bitfield. */
	memset(outFlags, 0, sizeof(*outFlags));
	if (SJME_NVM_ACC_IS(rawFlags, PUBLIC))
		(*outFlags) |= SJME_NVM_ACC_PUBLIC;
	if (SJME_NVM_ACC_IS(rawFlags, PRIVATE))
		(*outFlags) |= SJME_NVM_ACC_PRIVATE;
	if (SJME_NVM_ACC_IS(rawFlags, PROTECTED))
		(*outFlags) |= SJME_NVM_ACC_PROTECTED;
	if (SJME_NVM_ACC_IS(rawFlags, STATIC))
		(*outFlags) |= SJME_NVM_ACC_STATIC;
	if (SJME_NVM_ACC_IS(rawFlags, FINAL))
		(*outFlags) |= SJME_NVM_ACC_FINAL;
	if (SJME_NVM_ACC_IS(rawFlags, VOLATILE))
		(*outFlags) |= SJME_NVM_ACC_VOLATILE;
	if (SJME_NVM_ACC_IS(rawFlags, TRANSIENT))
		(*outFlags) |= SJME_NVM_ACC_TRANSIENT;
	if (SJME_NVM_ACC_IS(rawFlags, ENUM))
		(*outFlags) |= SJME_NVM_ACC_ENUM;
	if (SJME_NVM_ACC_IS(rawFlags, SYNTHETIC))
		(*outFlags) |= SJME_NVM_ACC_SYNTHETIC;
	
	/* Can only have a single access mode. */
	if (sjme_util_intBitCountU((*outFlags) & SJME_NVM_ACC_ACCESS_MASK) > 1)
		return SJME_ERROR_INVALID_FIELD_FLAGS;
	
	/* Cannot be both final and volatile. */
	if (SJME_NVM_ACC_IS((*outFlags), FINAL) &&
		SJME_NVM_ACC_IS((*outFlags), VOLATILE))
		return SJME_ERROR_INVALID_FIELD_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_parse(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_info* outClass)
{
#define MAX_RUNTIME_NAME 256
	sjme_errorCode error;
	sjme_jint magic, fullVersion, i, lastSlash;
	sjme_jshort major, minor, interfaceCount, fieldCount, methodCount;
	sjme_nvm_class_version actualVersion;
	sjme_nvm_class_poolInfo pool;
	sjme_nvm_class_info result;
	sjme_nvm_class_poolEntry* thisName;
	sjme_nvm_class_poolEntry* superName;
	sjme_nvm_class_poolEntry* interfaceName;
	sjme_list(sjme_nvm_stringPool_string)* interfaceNames;
	sjme_list(sjme_nvm_class_fieldInfo)* fields;
	sjme_list(sjme_nvm_class_methodInfo)* methods;
	sjme_nvm_class_fieldInfo field;
	sjme_nvm_class_methodInfo method;
	sjme_lpstr packageName;
	sjme_cchar runtimeNameRaw[MAX_RUNTIME_NAME];
	sjme_nvm_stringPool_string inPackage;
	sjme_nvm_stringPool_string runtimeName;
	sjme_nvm_class_methodInfo parsedMethod;
	sjme_nvm_class_fieldInfo parsedField;

	/* TODO FIXME: Optimize/shrink for SDCC. */
	
	if (allocPool == NULL || inStream == NULL || inStringPool == NULL ||
		outClass == NULL)
		return SJME_ERROR_NONE;
	
	/* Make sure we can actually allocate the resultant class. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_CLASS_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Read in magic number. */
	magic = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJI(
		inStream, &magic)))
		goto fail_readMagic;
	
	/* It must be valid! */
	if (magic != SJME_NVM_CLASS_MAGIC)
	{
		error = SJME_ERROR_INVALID_CLASS_MAGIC;
		goto fail_badMagic;
	}
		
	/* Read in version info. */	
	minor = INT16_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &minor)))
		goto fail_readMinor;
	
	major = INT16_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &major)))
		goto fail_readMajor;
	
	/* Compose and find matching version. */
	fullVersion = (((sjme_jint)major) << 16) |
		(((sjme_jint)minor) & 0xFFFF);
	if (fullVersion >= SJME_NVM_CLASS_CLDC_1_0 &&
		fullVersion <= SJME_NVM_CLASS_CLDC_1_0_MAX)
		actualVersion = SJME_NVM_CLASS_CLDC_1_0;
	else if (fullVersion >= SJME_NVM_CLASS_CLDC_1_1 &&
		fullVersion <= SJME_NVM_CLASS_CLDC_1_1_MAX)
		actualVersion = SJME_NVM_CLASS_CLDC_1_1;
	else if (fullVersion >= SJME_NVM_CLASS_CLDC_1_8 &&
		fullVersion <= SJME_NVM_CLASS_CLDC_1_8_MAX)
		actualVersion = SJME_NVM_CLASS_CLDC_1_8;
	
	/* Not valid. */
	else
	{
		error = SJME_ERROR_INVALID_CLASS_VERSION;
		goto fail_badVersion;
	}
	
	/* Set version. */
	result->version = actualVersion;
	
	/* Parse the constant pool. */
	pool = NULL;
	if (sjme_error_is(error = sjme_nvm_class_parseConstantPool(
		allocPool, inStream, inStringPool, &pool)) || pool == NULL)
		goto fail_parsePool;
	result->pool = sjme_weakUpR(sjme_nvm_class_poolInfo, pool);
	
	/* Read in flags. */
	if (sjme_error_is(error = sjme_nvm_class_classFlagsParse(
		inStream, &result->flags)))
		goto fail_readFlags;
	
	/* Read in this name. */
	thisName = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, result->pool,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		SJME_JNI_FALSE, &thisName)) || thisName == NULL)
		goto fail_readThisName;
	
	/* Reference it. */
	result->name = sjme_weakUpR(sjme_nvm_stringPool_string,
		SJME_P_C_N(thisName));
		
	/* Locate the last slash character in the binary name. */
	lastSlash = result->name->seq->length - 1;
	while (lastSlash > 0)
	{
		if ('/' == sjme_charSeq_charAtR(result->name->seq, lastSlash))
			break;
		
		lastSlash--;
	}

	/* Setup buffer for the package name. */
	packageName = sjme_alloca(sizeof(*packageName) * (lastSlash + 1));
	if (packageName == NULL)
	{
		error = SJME_ERROR_OUT_OF_MEMORY;
		goto fail_inPackage;
	}
	memset(packageName, 0, lastSlash + 1);

	/* Write out package name. */
	if (sjme_error_is(error = sjme_charSeq_dupToU(
		result->name->seq, 0,
		packageName, 0, lastSlash, lastSlash)))
		goto fail_inPackage;
	
	/* Locate string for package name. */
	inPackage = NULL;
	if (sjme_error_is(error = sjme_nvm_stringPool_locateUtf(
		inStringPool, &inPackage, packageName, 0, lastSlash)) ||
		inPackage == NULL)
		goto fail_inPackage;
	result->inPackage = sjme_weakUpR(sjme_nvm_stringPool_string, inPackage);

	/* Translate to the name as it would appear at runtime. */
	memset(runtimeNameRaw, 0, sizeof(runtimeNameRaw));
	if (sjme_error_is(error = sjme_charSeq_dupToU(result->name->seq,
		0, runtimeNameRaw, 0, MAX_RUNTIME_NAME - 1,
		-1)))
		goto fail_dupName;
	runtimeNameRaw[MAX_RUNTIME_NAME - 1] = 0;
	
	for (i = 0; i < MAX_RUNTIME_NAME; i++)
		if (runtimeNameRaw[i] == '/')
			runtimeNameRaw[i] = '.';
		else if (runtimeNameRaw[i] == '\0')
			break;

	/* Lookup the runtime string. */
	runtimeName = NULL;
	if (sjme_error_is(error = sjme_nvm_stringPool_locateUtf(
		inStringPool, &runtimeName, runtimeNameRaw, 0, -1)) ||
		runtimeName == NULL)
		goto fail_inRuntimeName;
	result->runtimeName = sjme_weakUpR(sjme_nvm_stringPool_string,
		runtimeName);
	
	/* Read in super name. */
	superName = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, result->pool,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		SJME_JNI_TRUE, &superName)))
		goto fail_readSuperName;
	
	/* Reference it, if valid. */
	if (superName != NULL)
		result->superName = sjme_weakUpR(sjme_nvm_stringPool_string,
			SJME_P_C_N(superName));
	
	/* How many interfaces are there? */
	interfaceCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &interfaceCount)) || interfaceCount < 0)
		goto fail_readInterfaceCount;
	
	/* Allocate interfaces count. */
	interfaceNames = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		interfaceCount, &interfaceNames, sjme_nvm_stringPool_string, 0)) ||
		interfaceNames == NULL)
		goto fail_allocInterfaceNames;
	result->interfaceNames = interfaceNames;
	
	/* Read in all interfaces. */
	for (i = 0; i < interfaceCount; i++)
	{
		/* Read in name. */
		interfaceName = NULL;
		if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
			inStream, result->pool,
			SJME_NVM_CLASS_POOL_TYPE_CLASS,
			SJME_JNI_FALSE, &interfaceName)) ||
			interfaceName == NULL)
			goto fail_readThisName;
		
		/* Reference it. */
		interfaceNames->elements[i] = sjme_weakUpR(sjme_nvm_stringPool_string,
			SJME_P_C_N(interfaceName));
	}
	
	/* Read in field count. */
	fieldCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &fieldCount)) || fieldCount < 0)
		goto fail_readFieldCount;
	
	/* Setup list to store fields in. */
	fields = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		fieldCount, &fields, sjme_nvm_class_fieldInfo, 0)) || fields == NULL)
		goto fail_allocFields;
	result->fields = fields;
	
	/* Load in and process each field. */
	for (i = 0; i < fieldCount; i++)
	{
		/* Parse each field. */
		parsedField = NULL;
		if (sjme_error_is(error = sjme_nvm_class_parseField(
			allocPool, inStream, result->pool, inStringPool,
			&parsedField)) || parsedField == NULL)
			goto fail_parseField;
		fields->elements[i] = sjme_weakUp(parsedField);
	}
	
	/* Determine the indexes of all fields. */
	for (i = 0; i < fieldCount; i++)
	{
		/* Determine the type index for its slot. */
		field = fields->elements[i];
		field->typedIndex = result->fieldCount[
			(SJME_NVM_ACC_IS(field->flags, STATIC) ?
			SJME_NVM_CLASS_MEMBER_STATIC : SJME_NVM_CLASS_MEMBER_INSTANCE)]
			[field->extendedType]++;
		
		/* Overflowed? */
		if (field->typedIndex < 0)
		{
			error = SJME_ERROR_CLASS_TOO_MANY_MEMBERS;
			goto fail_overflowFieldIndex;
		}
	}
	
	/* Read in method count. */
	methodCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &methodCount)) || methodCount < 0)
		goto fail_readMethodCount;
	
	/* Setup list to store methods in. */
	methods = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		methodCount, &methods, sjme_nvm_class_methodInfo, 0)) ||
		methods == NULL)
		goto fail_allocMethods;
	result->methods = methods;
	
	/* Load in and process each method. */
	for (i = 0; i < methodCount; i++)
	{
		/* Parse each method. */
		parsedMethod = NULL;
		if (sjme_error_is(error = sjme_nvm_class_parseMethod(
			allocPool, inStream, result->pool, inStringPool,
			&parsedMethod)) || parsedMethod == NULL)
			goto fail_parseMethod;
		methods->elements[i] = sjme_weakUp(parsedMethod);
		
		/* Link back. */
		sjme_atomic_s(sjme_nvm_class_info, &methods->elements[i]->inClass,
			result);
	}
	
	/* Determine the indexes of all methods. */
	for (i = 0; i < methodCount; i++)
	{
		/* Determine the type index for its slot. */
		method = methods->elements[i];
		method->typedIndex = result->methodCount[
			(SJME_NVM_ACC_IS(method->flags, STATIC) ? 
			SJME_NVM_CLASS_MEMBER_STATIC : SJME_NVM_CLASS_MEMBER_INSTANCE)]++;
		
		/* Overflowed? */
		if (method->typedIndex < 0)
		{
			error = SJME_ERROR_CLASS_TOO_MANY_MEMBERS;
			goto fail_overflowMethodIndex;
		}
	}
	
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_nvm_class_parseAttributes(
		allocPool, inStream, result->pool, inStringPool,
		sjme_nvm_class_classAttr, result)))
		goto fail_parseAttributes;
	
	/* Success! */
	*outClass = result;
	return SJME_ERROR_NONE;

fail_parseAttributes:
fail_overflowMethodIndex:
fail_refMethod:
fail_parseMethod:
fail_allocMethods:
fail_readMethodCount:
fail_overflowFieldIndex:
fail_refField:
fail_parseField:
fail_allocFields:
fail_readFieldCount:
fail_allocInterfaceNames:
fail_readInterfaceCount:
fail_refSuperName:
fail_readSuperName:
fail_refRuntimeName:
fail_inRuntimeName:
fail_dupName:
fail_refPackage:
fail_inPackage:
fail_refThisName:
fail_readThisName:
fail_readFlags:
fail_countPool:
fail_parsePool:
fail_badVersion:
fail_readMinor:
fail_readMajor:
fail_badMagic:
fail_readMagic:
fail_initResult:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_vmError(NULL, error);
#undef MAX_RUNTIME_NAME
}

sjme_errorCode sjme_nvm_class_parseAttributes(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull const sjme_nvm_class_parseAttributeHandler* handlers,
	sjme_attrInNotNull sjme_pointer context)
{
	sjme_errorCode error;
	sjme_jshort count;
	sjme_jint i, len;
	sjme_nvm_class_poolEntry* name;
	
	if (allocPool == NULL || inStream == NULL || inConstPool == NULL ||
		inStringPool == NULL || handlers == NULL || context == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in count. */
	count = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &count)) || count < 0)
		goto fail_readCount;
	
	/* Read individual attributes. */
	for (i = 0; i < count; i++)
	{
		/* Read in name. */
		name = NULL;
		if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
			inStream, inConstPool,
			SJME_NVM_CLASS_POOL_TYPE_UTF,
			SJME_JNI_FALSE, &name)) || name == NULL)
			goto fail_readName;
		
		/* Read in length. */
		len = -1;
		if (sjme_error_is(error = sjme_stream_inputReadValueJI(
			inStream, &len)) || len < 0)
			goto fail_readLen;
		
		/* Stage it for stack allocations. */
		if (sjme_error_is(error = sjme_nvm_class_parseAttribute(
			allocPool, inStream, inConstPool, inStringPool, handlers,
			context, name->utf.utf->seq,
			len)))
			goto fail_parseSingle;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_parseSingle:
fail_readLen:
fail_readName:
fail_readCount:
	return sjme_error_vmError(NULL, error);
}

sjme_errorCode sjme_nvm_class_parseConstantPool(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_poolInfo* outPool)
{
	sjme_errorCode error;
	sjme_jshort count;
	sjme_jint index;
	sjme_jbyte tag;
	sjme_list(sjme_nvm_class_poolEntry)* entries;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_class_poolEntry* target;
	sjme_nvm_stringPool_string utf;
	sjme_nvm_class_poolInfo result;
	
	if (allocPool == NULL || inStream == NULL || outPool == NULL ||
		inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure we can actually allocate this. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_POOL,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Read in pool count. */
	count = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &count)) || count < 0)
		goto fail_readCount;
	
	/* Invalid pool size? */
	if (count < 0 || count >= INT16_MAX)
	{
		error = sjme_error_vmError(NULL, SJME_ERROR_INVALID_CLASS_POOL_COUNT);
		goto fail_poolCount;
	}
	
	/* Count up by one, since zero is included! */
	count += 1;
	
	/* Allocate resultant entries, where they will all go. */
	entries = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		count, &entries, sjme_nvm_class_poolEntry, 0)) || entries == NULL)
		goto fail_entryList;
	result->pool = entries;
	
	/* Read in all entries. */
	/* This is a first pass since index items can refer to later entries. */
	for (index = 1; index < count - 1; index++)
	{
		/* Which entry is being written? */
		entry = &entries->elements[index];
		
		/* Read in tag. */
		tag = -1;
		if (sjme_error_is(error = sjme_stream_inputReadValueJB(
			inStream, &tag)) || tag < 0)
			goto fail_readTag;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
		/* Debug. */
		sjme_message("TAG: %d", tag);
#endif
		
		/* Set tag. */
		entry->type = tag;
		
		/* Which tag is this? */
		switch (tag)
		{
				/* Class reference. */
			case SJME_NVM_CLASS_POOL_TYPE_CLASS:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->classRef.descriptorIndex)))
					goto fail_readItem;
				break;
				
				/* Double value. */
			case SJME_NVM_CLASS_POOL_TYPE_DOUBLE:
				if (sjme_error_is(error = sjme_stream_inputReadValueJJ(
					inStream,
					(sjme_jlong*)&entry->constDouble.value)))
					goto fail_readItem;
				
				/* Skip wide index. */
				index++;
				break;
			
				/* Reference to a member. */
			case SJME_NVM_CLASS_POOL_TYPE_FIELD:
			case SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD:
			case SJME_NVM_CLASS_POOL_TYPE_METHOD:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->member.inClassIndex)))
					goto fail_readItem;
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->member.nameAndTypeIndex)))
					goto fail_readItem;
				break;
				
				/* Float value. */
			case SJME_NVM_CLASS_POOL_TYPE_FLOAT:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constFloat.value.bits)))
					goto fail_readItem;
				break;
				
				/* Integer value. */
			case SJME_NVM_CLASS_POOL_TYPE_INTEGER:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constInteger.value)))
					goto fail_readItem;
				break;
				
				/* Long value. */
			case SJME_NVM_CLASS_POOL_TYPE_LONG:
				if (sjme_error_is(error = sjme_stream_inputReadValueJJ(
					inStream,
					(sjme_jlong*)&entry->constLong.value)))
					goto fail_readItem;
				
				/* Skip wide index. */
				index++;
				break;
				
				/* Name and type information. */
			case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->nameAndType.nameIndex)))
					goto fail_readItem;
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->nameAndType.descriptorIndex)))
					goto fail_readItem;
				break;
				
				/* Constant string. */
			case SJME_NVM_CLASS_POOL_TYPE_STRING:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->constString.valueIndex)))
					goto fail_readItem;
				break;
			
				/* UTF String. */
			case SJME_NVM_CLASS_POOL_TYPE_UTF:
				utf = NULL;
				if (sjme_error_is(error = sjme_nvm_stringPool_locateStream(
					inStringPool, inStream, &utf)) || utf == NULL)
					goto fail_readItem;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
				/* Debug. */
				sjme_message("Read UTF: %s",
					sjme_charSeq_tempUtf(utf->seq));
#endif
				
				/* Store and count up entry as we are using it now. */
				entry->utf.utf = sjme_weakUp(utf);
				break;
			
			default:
				goto fail_initItem;
		}
	}
	
	/* Second stage item linking. */
	for (index = 1; index < count - 1; index++)
	{
		/* Which entry is being initialized? */
		entry = &entries->elements[index];
		
		/* Initialize accordingly. */
		switch (entry->type)
		{
				/* These are base elements that need no initialization. */
			case SJME_NVM_CLASS_POOL_TYPE_UTF:
			case SJME_NVM_CLASS_POOL_TYPE_INTEGER:
			case SJME_NVM_CLASS_POOL_TYPE_FLOAT:
				break;
				
				/* Skip wide element. */
			case SJME_NVM_CLASS_POOL_TYPE_LONG:
			case SJME_NVM_CLASS_POOL_TYPE_DOUBLE:
				index++;
				break;
			
				/* Class type. */
			case SJME_NVM_CLASS_POOL_TYPE_CLASS:
				if (entry->classRef.descriptorIndex <= 0 ||
					entry->classRef.descriptorIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be a UTF string. */
				target = &entries->elements[entry->classRef.descriptorIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Refer to it and count up, since we are using it. */
				entry->classRef.descriptor = sjme_weakUp(target->utf.utf);
				break;
				
				/* Member reference. */
			case SJME_NVM_CLASS_POOL_TYPE_FIELD:
			case SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD:
			case SJME_NVM_CLASS_POOL_TYPE_METHOD:
				if (entry->member.inClassIndex <= 0 ||
					entry->member.inClassIndex >= entries->length ||
					entry->member.nameAndTypeIndex <= 0 ||
					entry->member.nameAndTypeIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be a class. */
				target = &entries->elements[entry->member.inClassIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_CLASS)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set class. */
				sjme_atomic_sP(sjme_nvm_class_poolEntryClass, 1,
					&entry->member.inClass,
					(sjme_nvm_class_poolEntryClass*)target);
				
				/* Needs to be a name and type. */
				target = &entries->elements[entry->member.nameAndTypeIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set name and type. */
				sjme_atomic_sP(sjme_nvm_class_poolEntryNameAndType, 1,
					&entry->member.nameAndType,
					(sjme_nvm_class_poolEntryNameAndType*)target);
				break;
			
			case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
				if (entry->nameAndType.nameIndex <= 0 ||
					entry->nameAndType.nameIndex >= entries->length ||
					entry->nameAndType.descriptorIndex <= 0 ||
					entry->nameAndType.descriptorIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be UTF. */
				target = &entries->elements[entry->nameAndType.nameIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set name. */
				entry->nameAndType.name = sjme_weakUp(target->utf.utf);
				
				/* Needs to be UTF. */
				target = &entries->elements[
					entry->nameAndType.descriptorIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set descriptor. */
				entry->nameAndType.descriptor = sjme_weakUp(target->utf.utf);
				break;
				
				/* Constant string. */
			case SJME_NVM_CLASS_POOL_TYPE_STRING:
				if (entry->constString.valueIndex <= 0 ||
					entry->constString.valueIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be a UTF string. */
				target = &entries->elements[entry->constString.valueIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Refer to it and count up, since we are using it. */
				entry->constString.value = sjme_weakUp(target->utf.utf);
				break;
			
			default:
				goto fail_initItem;
		}
	}

	/* Third stage initialization, when everything is known. */
	for (index = 1; index < count - 1; index++)
	{
		/* Which entry is being initialized? */
		entry = &entries->elements[index];
		
		/* Initialize accordingly. */
		switch (entry->type)
		{
				/* Class reference. */
			case SJME_NVM_CLASS_POOL_TYPE_CLASS:
				entry->classRef.descriptorHash =
					sjme_charSeq_hashR(SJME_P_C_N(entry)->seq);
				break;
			
				/* Member reference. */
			case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
				/* Calculate the member ID hash. */
				entry->nameAndType.idHash = sjme_nvm_class_idHashMember(
					entry->nameAndType.name->seq,
					entry->nameAndType.descriptor->seq);
				break;

				/* Method reference. */
			case SJME_NVM_CLASS_POOL_TYPE_METHOD:
			case SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD:
				if (sjme_error_is(error = sjme_nvm_class_descriptorMethodSlots(
					SJME_P_M_T(entry)->seq,
					&entry->member.staticArgSlots,
					&entry->member.rvSlots)))
					goto fail_initItem;
				break;

				/* Not considered an error in the third stage. */
			default:
				break;
		}
	}
	
	/* Success! */
	*outPool = result;
	return SJME_ERROR_NONE;

fail_initItem:
fail_readItem:
fail_readTag:
fail_entryList:
	if (entries != NULL)
		sjme_alloc_free(entries);
fail_poolCount:
fail_readCount:
fail_initCommon:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_vmError(NULL, error);
}

sjme_errorCode sjme_nvm_class_parseField(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_fieldInfo* outField)
{
	sjme_errorCode error;
	sjme_nvm_class_fieldInfo result;
	sjme_nvm_class_poolEntry* name;
	sjme_nvm_class_poolEntry* type;
	
	if (allocPool == NULL || inStream == NULL || inConstPool == NULL ||
		outField == NULL || inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Ensure we can allocate the result first. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_FIELD_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Read in flags. */
	if (sjme_error_is(error = sjme_nvm_class_fieldFlagsParse(
		inStream, &result->flags)))
		goto fail_readFlags;
		
	/* Read in name. */
	name = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_NVM_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &name)) || name == NULL)
		goto fail_readName;
	
	/* Reference it. */
	result->name = sjme_weakUp(name->utf.utf);
		
	/* Read in type. */
	type = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_NVM_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &type)) || type == NULL)
		goto fail_readType;
	
	/* Reference it. */
	result->type = sjme_weakUp(type->utf.utf);
		
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_nvm_class_parseAttributes(
		allocPool, inStream, inConstPool, inStringPool,
		sjme_nvm_class_fieldAttr, result)))
		goto fail_parseAttributes;
	
	/* Determine type. */
	if (sjme_error_is(error = sjme_nvm_class_descriptorToType(
		result->type->seq,
		&result->javaType, &result->basicType, &result->extendedType)))
		goto fail_determineType;
	
	/* Initialize constant value to an invalid type. */
	result->constVal.type = SJME_NUM_JAVA_TYPE_IDS;

	/* Calculate the hash identifier. */
	result->idHash = sjme_nvm_class_idHashMember(result->name->seq,
		result->type->seq);
	
	/* Success! */
	*outField = result;
	return SJME_ERROR_NONE;
	
fail_determineType:
fail_parseAttributes:
fail_refType:
fail_readType:
fail_refName:
fail_readName:
fail_readFlags:
fail_initResult:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_vmError(NULL, error);
}

sjme_errorCode sjme_nvm_class_validBinaryName(
	sjme_attrInNotNull sjme_charSeq binaryName)
{
	sjme_jint i, n;
	sjme_jchar c;
	
	if (binaryName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	for (i = 0, n = binaryName->length; i < n; i++)
	{
		c = sjme_charSeq_charAtR(binaryName, i);

		/* These characters are invalid. */
		if (c == '.' || c == ';' || c == '[' || c == '<' ||
			c == '>' || c == ':')
			return SJME_ERROR_INVALID_BINARY_NAME;
	}
	
	/* Did not fail, so success! */
	return SJME_ERROR_NONE;
}
