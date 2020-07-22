/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>

#include "squirreljme.h"

static FILE* getPipeForFd(jint fd)
{
	if (fd == 1)
		return stdout;
	else if (fd == 2)
		return stderr;
	return NULL;
}

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_flush(JNIEnv* env,
	jclass classy, jint fd)
{
	FILE* pipe = getPipeForFd(fd);

	if (pipe == NULL)
		return -1;

	if (fflush(pipe) == 0)
		return 1;
	return -1;
}

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_writeII(JNIEnv* env,
	jclass classy, jint fd, jint code)
{
	FILE* pipe = getPipeForFd(fd);

	if (pipe == NULL)
		return -1;

	if (fputc(code, pipe) >= 0)
		return 1;
	return -1;
}

static const JNINativeMethod mleTerminalMethods[] =
{
	{"flush", "(I)I", (void*)Impl_mle_TerminalShelf_flush},
	{"write", "(II)I", (void*)Impl_mle_TerminalShelf_writeII},
};

jint JNICALL mleTerminalInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/TerminalShelf"),
		mleTerminalMethods, sizeof(mleTerminalMethods) /
			sizeof(JNINativeMethod));
}
