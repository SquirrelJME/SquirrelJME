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
#include "cc_squirreljme_jvm_mle_TerminalShelf.h"

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

JNIEXPORT jint JNICALL Impl_mle_TerminalShelf_writeIABIII(
	JNIEnv* env, jclass classy, jint fd, jbyteArray buf, jint off, jint len)
{
	jbyte* jbuf;
	FILE* pipe = getPipeForFd(fd);

	if (pipe == NULL)
		return -1;
	
	int okay = 0;
	jbuf = (jbyte*)env->GetByteArrayElements(buf, NULL);
	for (int i = 0; i < len; i++)
		if (fputc(jbuf[off++], pipe) >= 0)
			okay++;
		else
			break;
	
	env->ReleaseByteArrayElements(buf, jbuf, 0);
	
	return okay;
}

static const JNINativeMethod mleTerminalMethods[] =
{
	{"flush", "(I)I", (void*)Impl_mle_TerminalShelf_flush},
	{"write", "(II)I", (void*)Impl_mle_TerminalShelf_writeII},
	{"write", "(I[BII)I", (void*)Impl_mle_TerminalShelf_writeIABIII},
};

jint JNICALL mleTerminalInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/TerminalShelf"),
		mleTerminalMethods, sizeof(mleTerminalMethods) /
			sizeof(JNINativeMethod));
}
