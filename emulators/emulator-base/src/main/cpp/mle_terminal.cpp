/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>

#include "squirreljme.h"

// The class to forward to
#define TERMINAL_CLASSNAME "cc/squirreljme/emulator/EmulatedTerminalShelf"

#define TERMINAL_AVAILABLE_DESC "(I)I"
#define TERMINAL_CLOSE_DESC "(I)I"
#define TERMINAL_FLUSH_DESC "(I)I"
#define TERMINAL_READIABIII_DESC "(I[BII)I"
#define TERMINAL_WRITEIII_DESC "(II)I"
#define TERMINAL_WRITEIABIII_DESC "(I[BII)I"

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_available(JNIEnv* env,
	jclass classy, jint fd)
{
	return forwardCallStaticInteger(env, TERMINAL_CLASSNAME,
		"available", TERMINAL_CLOSE_DESC,
		fd);
}

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_close(JNIEnv* env,
	jclass classy, jint fd)
{
	return forwardCallStaticInteger(env, TERMINAL_CLASSNAME,
		"close", TERMINAL_CLOSE_DESC,
		fd);
}

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_flush(JNIEnv* env,
	jclass classy, jint fd)
{
	return forwardCallStaticInteger(env, TERMINAL_CLASSNAME,
		"flush", TERMINAL_FLUSH_DESC,
		fd);
}

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_readIABIII(
	JNIEnv* env, jclass classy, jint fd, jbyteArray buf, jint off, jint len)
{
	return forwardCallStaticInteger(env, TERMINAL_CLASSNAME,
		"read", TERMINAL_READIABIII_DESC,
		fd, buf, off, len);
}

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_writeII(JNIEnv* env,
	jclass classy, jint fd, jint code)
{
	return forwardCallStaticInteger(env, TERMINAL_CLASSNAME,
		"write", TERMINAL_WRITEIII_DESC,
		fd, code);
}

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_writeIABIII(
	JNIEnv* env, jclass classy, jint fd, jbyteArray buf, jint off, jint len)
{
	return forwardCallStaticInteger(env, TERMINAL_CLASSNAME,
		"write", TERMINAL_WRITEIABIII_DESC,
		fd, buf, off, len);
}

static const JNINativeMethod mleTerminalMethods[] =
{
	{"available", TERMINAL_AVAILABLE_DESC, (void*)Impl_mle_TerminalShelf_available},
	{"close", TERMINAL_CLOSE_DESC, (void*)Impl_mle_TerminalShelf_close},
	{"flush", TERMINAL_FLUSH_DESC, (void*)Impl_mle_TerminalShelf_flush},
	{"read", TERMINAL_READIABIII_DESC, (void*)Impl_mle_TerminalShelf_readIABIII},
	{"write", TERMINAL_WRITEIII_DESC, (void*)Impl_mle_TerminalShelf_writeII},
	{"write", TERMINAL_WRITEIABIII_DESC, (void*)Impl_mle_TerminalShelf_writeIABIII},
};

jint JNICALL mleTerminalInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/TerminalShelf"),
		mleTerminalMethods, sizeof(mleTerminalMethods) /
			sizeof(JNINativeMethod));
}
