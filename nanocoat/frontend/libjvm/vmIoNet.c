/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

jint JNICALL JVM_GetLastErrorString(char* buf, int len)
{
	sjme_todo("Impl?");
}

char* JNICALL JVM_NativePath(char* path)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Open(const char* fname, jint flags, jint mode)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Close(jint fd)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Read(jint fd, char* buf, jint nbytes)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Write(jint fd, char* buf, jint nbytes)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Available(jint fd, jlong* pbytes)
{
	sjme_todo("Impl?");
}

jlong JNICALL JVM_Lseek(jint fd, jlong offset, jint whence)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_SetLength(jint fd, jlong length)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Sync(jint fd)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_InitializeSocketLibrary(void)
{
	sjme_todo("Impl?");
}

struct sockaddr;

jint JNICALL JVM_Socket(jint domain, jint type, jint protocol)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_SocketClose(jint fd)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_SocketShutdown(jint fd, jint howto)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Recv(jint fd, char* buf, jint nBytes, jint flags)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Send(jint fd, char* buf, jint nBytes, jint flags)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Timeout(int fd, long timeout)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Listen(jint fd, jint count)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Connect(jint fd, struct sockaddr* him, jint len)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Bind(jint fd, struct sockaddr* him, jint len)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_Accept(jint fd, struct sockaddr* him, jint* len)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_RecvFrom(jint fd,
	char* buf,
	int nBytes,
	int flags,
	struct sockaddr* from,
	int* fromlen)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_SendTo(jint fd,
	char* buf,
	int len,
	int flags,
	struct sockaddr* to,
	int tolen)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_SocketAvailable(jint fd, jint* result)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_GetSockName(jint fd, struct sockaddr* him, int* len)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_GetSockOpt(jint fd,
	int level,
	int optname,
	char* optval,
	int* optlen)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_SetSockOpt(jint fd,
	int level,
	int optname,
	const char* optval,
	int optlen)
{
	sjme_todo("Impl?");
}

int JNICALL JVM_GetHostName(char* name, int namelen)
{
	sjme_todo("Impl?");
}

jstring JNICALL JVM_GetTemporaryDirectory(JNIEnv* env)
{
	sjme_todo("Impl?");
}
