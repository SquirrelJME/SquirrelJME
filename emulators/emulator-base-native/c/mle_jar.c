/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

// The class to forward to
#define JARSHELF_CLASSNAME "cc/squirreljme/emulator/EmulatedJarPackageShelf"

#define JARSHELF_CLASSPATH_DESC "()[Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;"
#define JARSHELF_LIBRARIES_DESC "()[Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;"
#define JARSHELF_LIBRARYID_DESC "(Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;)I"
#define JARSHELF_LIBRARYPATH_DESC "(Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;)Ljava/lang/String;"
#define JARSHELF_OPENRESOURCE_DESC "(Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;Ljava/lang/String;)Ljava/io/InputStream;"
#define JARSHELF_PREFIXCODE_DESC "(Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;)I"
#define JARSHELF_RAWDATA_DESC "(Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;I[BII)I"
#define JARSHELF_RAWSIZE_DESC "(Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;)I"

JNIEXPORT jobject JNICALL Impl_mle_JarShelf_classPath(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticObject(env, JARSHELF_CLASSNAME,
		"classPath", JARSHELF_CLASSPATH_DESC);
}

JNIEXPORT jobject JNICALL Impl_mle_JarShelf_libraries(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticObject(env, JARSHELF_CLASSNAME,
		"libraries", JARSHELF_LIBRARIES_DESC);
}

JNIEXPORT jint JNICALL Impl_mle_JarShelf_libraryId(JNIEnv* env,
	jclass classy, jobject jar)
{
	return forwardCallStaticInteger(env, JARSHELF_CLASSNAME,
		"libraryId", JARSHELF_LIBRARYID_DESC,
		jar);
}

JNIEXPORT jobject JNICALL Impl_mle_JarShelf_libraryPath(JNIEnv* env,
	jclass classy, jobject jar)
{
	return forwardCallStaticObject(env, JARSHELF_CLASSNAME,
		"libraryPath", JARSHELF_LIBRARYPATH_DESC,
		jar);
}

JNIEXPORT jobject JNICALL Impl_mle_JarShelf_openResource(JNIEnv* env,
	jclass classy, jobject jar, jobject rcName)
{
	return forwardCallStaticObject(env, JARSHELF_CLASSNAME,
		"openResource", JARSHELF_OPENRESOURCE_DESC,
		jar, rcName);
}

JNIEXPORT jint JNICALL Impl_mle_JarShelf_prefixCode(JNIEnv* env,
	jclass classy, jobject jar)
{
	return forwardCallStaticInteger(env, JARSHELF_CLASSNAME,
		"prefixCode", JARSHELF_PREFIXCODE_DESC,
		jar);
}

JNIEXPORT jint JNICALL Impl_mle_JarShelf_rawData(JNIEnv* env,
	jclass classy, jobject jar, jint jarOff,
	jbyteArray buf, jint off, jint len)
{
	return forwardCallStaticInteger(env, JARSHELF_CLASSNAME,
		"rawData", JARSHELF_RAWDATA_DESC,
		jar, jarOff, buf, off, len);
}

JNIEXPORT jint JNICALL Impl_mle_JarShelf_rawSize(JNIEnv* env,
	jclass classy, jobject jar)
{
	return forwardCallStaticInteger(env, JARSHELF_CLASSNAME,
		"rawSize", JARSHELF_RAWSIZE_DESC,
		jar);
}

static const JNINativeMethod mleJarMethods[] =
{
	{"classPath", JARSHELF_CLASSPATH_DESC, (void*)Impl_mle_JarShelf_classPath},
	{"libraries", JARSHELF_LIBRARIES_DESC, (void*)Impl_mle_JarShelf_libraries},
	{"libraryId", JARSHELF_LIBRARYID_DESC, (void*)Impl_mle_JarShelf_libraryId},
	{"libraryPath", JARSHELF_LIBRARYPATH_DESC, (void*)Impl_mle_JarShelf_libraryPath},
	{"openResource", JARSHELF_OPENRESOURCE_DESC, (void*)Impl_mle_JarShelf_openResource},
	{"prefixCode", JARSHELF_PREFIXCODE_DESC, (void*)Impl_mle_JarShelf_prefixCode},
	{"rawData", JARSHELF_RAWDATA_DESC, (void*)Impl_mle_JarShelf_rawData},
	{"rawSize", JARSHELF_RAWSIZE_DESC, (void*)Impl_mle_JarShelf_rawSize},
};

jint JNICALL mleJarInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/JarPackageShelf"),
		mleJarMethods, sizeof(mleJarMethods) / sizeof(JNINativeMethod));
}
