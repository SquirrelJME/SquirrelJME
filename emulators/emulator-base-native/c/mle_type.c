/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

#define TYPE_CLASSNAME "cc/squirreljme/emulator/EmulatedTypeShelf"

#define BINARYNAME_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Ljava/lang/String;"
#define CLASSTOTYPE_DESC "(Ljava/lang/Class;)Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"
#define COMPONENTROOT_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"
#define FINDTYPE_DESC "(Ljava/lang/String;)Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"
#define INJAR_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;"
#define INTERFACES_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)[Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"
#define ISARRAY_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Z"
#define TYPETOCLASS_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Ljava/lang/Class;"

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_binaryName(JNIEnv* env,
	jclass classy, jobject mleType)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"binaryName", BINARYNAME_DESC,
		mleType);
}

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_classToType(JNIEnv* env,
	jclass classy, jobject javaClass)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"classToType", CLASSTOTYPE_DESC,
		javaClass);
}

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_componentRoot(JNIEnv* env,
	jclass classy, jobject mleType)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"componentRoot", COMPONENTROOT_DESC,
		mleType);
}

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_findType(JNIEnv* env,
	jclass classy, jobject className)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"findType", FINDTYPE_DESC,
		className);
}

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_inJar(JNIEnv* env,
	jclass classy, jobject mleType)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"inJar", INJAR_DESC,
		mleType);
}

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_interfaces(JNIEnv* env,
	jclass classy, jobject mleType)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"interfaces", INTERFACES_DESC,
		mleType);
}

JNIEXPORT jboolean JNICALL Impl_mle_TypeShelf_isArray(JNIEnv* env,
	jclass classy, jobject mleType)
{
	return forwardCallStaticBoolean(env, TYPE_CLASSNAME,
		"isArray", ISARRAY_DESC,
		mleType);
}

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_typeToClass(JNIEnv* env,
	jclass classy, jobject mleType)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"typeToClass", TYPETOCLASS_DESC,
		mleType);
}

static const JNINativeMethod mleTypeMethods[] =
{
	{"binaryName", BINARYNAME_DESC, (void*)Impl_mle_TypeShelf_binaryName},
	{"classToType", CLASSTOTYPE_DESC, (void*)Impl_mle_TypeShelf_classToType},
	{"componentRoot", COMPONENTROOT_DESC, (void*)Impl_mle_TypeShelf_componentRoot},
	{"findType", FINDTYPE_DESC, (void*)Impl_mle_TypeShelf_findType},
	{"inJar", INJAR_DESC, (void*)Impl_mle_TypeShelf_inJar},
	{"interfaces", INTERFACES_DESC, (void*)Impl_mle_TypeShelf_interfaces},
	{"isArray", ISARRAY_DESC, (void*)Impl_mle_TypeShelf_isArray},
	{"typeToClass", TYPETOCLASS_DESC, (void*)Impl_mle_TypeShelf_typeToClass},
};

jint JNICALL mleTypeInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/TypeShelf"),
		mleTypeMethods, sizeof(mleTypeMethods) /
			sizeof(JNINativeMethod));
}
