/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/internals.h"

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Accept(
	jint fileNo,
	struct sockaddr* him,
	jint* len)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Available(
	jint fileNo,
	jlong* numBytes)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Bind(
	jint fileNo,
	struct sockaddr* addr,
	jint len)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Close(
	jint fileNo)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Connect(
	jint fileNo,
	struct sockaddr* addr, 
	jint len)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

int SJME_JNI_LIB_FUNCTION JNICALL JVM_GetHostName(
	char* name, 
	int namelen)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetLastErrorString(
	char* buf, 
	int len)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetSockName(
	jint fileNo,
	struct sockaddr* addr,
	int* len)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetSockOpt(
	jint fileNo,
	int level,
	int optName,
	char* optVal,
	int* optLen)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jstring SJME_JNI_LIB_FUNCTION JNICALL JVM_GetTemporaryDirectory(
	JNIEnv* env)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_InitializeSocketLibrary(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Listen(
	jint fileNo, 
	jint count)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_Lseek(
	jint fileNo,
	jlong offset, 
	jint whence)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}

char* SJME_JNI_LIB_FUNCTION JNICALL JVM_NativePath(
	char* path)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Open(
	const char* fname,
	jint flags,
	jint mode)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Read(
	jint fileNo, 
	char* buf,
	jint len)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Recv(
	jint fileNo,
	char* buf, 
	jint len, 
	jint flags)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_RecvFrom(
	jint fileNo,
	char* buf,
	int len,
	int flags,
	struct sockaddr* fromAddr,
	int* fromAddrLen)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Send(
	jint fileNo,
	char* buf, 
	jint len,
	jint flags)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_SendTo(
	jint fileNo,
	char* buf,
	int len,
	int flags,
	struct sockaddr* toAddr,
	int toAddrLen)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_SetLength(jint fileNo, 
	jlong length)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_SetSockOpt(
	jint fileNo,
	int level,
	int optName,
	const char* optVal,
	int optLen)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Socket(
	jint domain, 
	jint type, 
	jint protocol)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_SocketAvailable(
	jint fileNo, 
	jint* result)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_SocketClose(
	jint fileNo)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_SocketShutdown(
	jint fileNo, 
	jint howto)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Sync(
	jint fileNo)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Timeout(
	int fd,
	long timeout)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_Write(
	jint fileNo, 
	char* buf, 
	jint len)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}
