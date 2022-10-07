/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

#define TYPE_CLASSNAME "cc/squirreljme/emulator/EmulatedTypeShelf"

#define CLASSTOTYPE_DESC "(Ljava/lang/Class;)Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"
#define FINDTYPE_DESC "(Ljava/lang/String;)Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"
#define INTERFACES_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)[Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"
#define TYPETOCLASS_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Ljava/lang/Class;"

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_classToType(JNIEnv* env,
	jclass classy, jobject javaClass)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"classToType", CLASSTOTYPE_DESC,
		javaClass);
}

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_findType(JNIEnv* env,
	jclass classy, jobject className)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"findType", FINDTYPE_DESC,
		className);
}

JNIEXPORT jobject JNICALL Impl_mle_TypeShelf_interfaces(JNIEnv* env,
	jclass classy, jobject mleType)
{
	return forwardCallStaticObject(env, TYPE_CLASSNAME,
		"interfaces", INTERFACES_DESC,
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
	{"classToType", CLASSTOTYPE_DESC, (void*)Impl_mle_TypeShelf_classToType},
	{"findType", FINDTYPE_DESC, (void*)Impl_mle_TypeShelf_findType},
	{"interfaces", INTERFACES_DESC, (void*)Impl_mle_TypeShelf_interfaces},
	{"typeToClass", TYPETOCLASS_DESC, (void*)Impl_mle_TypeShelf_typeToClass},
};

jint JNICALL mleTypeInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/TypeShelf"),
		mleTypeMethods, sizeof(mleTypeMethods) /
			sizeof(JNINativeMethod));
}
