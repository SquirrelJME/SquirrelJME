/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/BucketShelf"
#define FORWARD_CLASS_NAME Bucket
#define FORWARD_NATIVE_CLASS "cc/squirreljme/emulator/EmulatedBucketShelf"

#define FORWARD_DESC_delete \
	DESC_METHOD(DESC_BOOLEAN, DESC_BUCKET DESC_STRING)
#define FORWARD_DESC_lastModifiedTime \
	DESC_METHOD(DESC_LONG, DESC_BUCKET DESC_STRING)
#define FORWARD_DESC_exists \
	DESC_METHOD(DESC_BOOLEAN, DESC_BUCKET DESC_STRING)
#define FORWARD_DESC_bucket \
	DESC_METHOD(DESC_BUCKET, DESC_INTEGER)
#define FORWARD_DESC_exists \
	DESC_METHOD(DESC_BOOLEAN, DESC_BUCKET DESC_STRING)
#define FORWARD_DESC_list \
	DESC_METHOD(DESC_ARRAY(DESC_STRING), DESC_BUCKET)
#define FORWARD_DESC_list_filtered \
	DESC_METHOD(DESC_ARRAY(DESC_STRING), DESC_BUCKET DESC_BOOLEAN \
		DESC_STRING DESC_STRING DESC_STRING)
#define FORWARD_DESC_length \
	DESC_METHOD(DESC_LONG, DESC_BUCKET DESC_STRING)
#define FORWARD_DESC_path \
	DESC_METHOD(DESC_STRING, DESC_BUCKET)
#define FORWARD_DESC_read \
	DESC_METHOD(DESC_INT, DESC_BUCKET DESC_STRING \
	DESC_INT DESC_ARRAY(DESC_BYTE) DESC_INT DESC_INT)
#define FORWARD_DESC_write \
	DESC_METHOD(DESC_VOID, DESC_BUCKET DESC_STRING \
	DESC_INT DESC_ARRAY(DESC_BYTE) DESC_INT DESC_INT DESC_INT)

FORWARD_IMPL(Bucket, delete,
	jboolean, Boolean,
	FORWARD_IMPL_args(jobject bucket, jstring fileName),
	FORWARD_IMPL_pass(bucket, fileName))
FORWARD_IMPL(Bucket, lastModifiedTime,
	jlong, Long,
	FORWARD_IMPL_args(jobject bucket, jstring fileName),
	FORWARD_IMPL_pass(bucket, fileName))
FORWARD_IMPL(Bucket, exists,
	jboolean, Boolean,
	FORWARD_IMPL_args(jobject bucket, jstring fileName),
	FORWARD_IMPL_pass(bucket, fileName))
FORWARD_IMPL(Bucket, bucket,
	jobject, Object,
	FORWARD_IMPL_args(jint type),
	FORWARD_IMPL_pass(type))
FORWARD_IMPL(Bucket, list,
	jarray, Object,
	FORWARD_IMPL_args(jobject bucket),
	FORWARD_IMPL_pass(bucket))
FORWARD_IMPL_ALT(Bucket, list, filtered,
	jarray, Object,
	FORWARD_IMPL_args(jobject bucket, jboolean not,
		jstring prefix, jstring contains, jstring suffix),
	FORWARD_IMPL_pass(bucket, not, prefix, contains, suffix))
FORWARD_IMPL(Bucket, length,
	jlong, Long,
	FORWARD_IMPL_args(jobject bucket, jstring fileName),
	FORWARD_IMPL_pass(bucket, fileName))
FORWARD_IMPL(Bucket, path,
	jobject, Object,
	FORWARD_IMPL_args(jobject bucket),
	FORWARD_IMPL_pass(bucket))
FORWARD_IMPL(Bucket, read,
	jint, Integer,
	FORWARD_IMPL_args(jobject bucket, jstring fileName,
		jint fileOff, jarray buf, jint off, jint len),
	FORWARD_IMPL_pass(bucket, fileName, fileOff, buf, off, len))
FORWARD_IMPL_VOID(Bucket, write,
	FORWARD_IMPL_args(jobject bucket, jstring fileName,
		jint fileOff, jarray buf, jint off, jint len, jint mode),
	FORWARD_IMPL_pass(bucket, fileName, fileOff, buf, off, len, mode))

static const JNINativeMethod mleBucketMethods[] =
{
	FORWARD_list(Bucket, delete),
	FORWARD_list(Bucket, lastModifiedTime),
	FORWARD_list(Bucket, exists),
	FORWARD_list(Bucket, bucket),
	FORWARD_list(Bucket, list),
	FORWARD_listAlt(Bucket, list, filtered),
	FORWARD_list(Bucket, length),
	FORWARD_list(Bucket, path),
	FORWARD_list(Bucket, read),
	FORWARD_list(Bucket, write),
};

jint JNICALL mleBucketInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, FORWARD_CLASS),
		mleBucketMethods, sizeof(mleBucketMethods) /
			sizeof(JNINativeMethod));
}
