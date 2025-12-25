/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_IDENT_OS_WINE)
	#define SJME_CONFIG_FORGET_STDLIB
#endif

#include "sjme/config.h"

#if (SJME_CONFIG_NAL_TCP_UDP == SJME_CONFIG_NAL_IMPLEMENT_WIN32)
	#include <winsock2.h>
#endif

#include "sjme/intern/nal.h"
#include "sjme/path.h"
#include "sjme/util.h"

#if defined(SJME_CONFIG_NAL_HAS_ANY_WIN32)
	#define WIN32_LEAN_AND_MEAN 1

	#include <windows.h>
	#include <commctrl.h>
	#include <shlwapi.h>
	#include <shlobj.h>

	#undef WIN32_LEAN_AND_MEAN
#endif

#if defined(SJME_CONFIG_FORGET_STDLIB)
	#include <stdlib.h>
#endif

#pragma region(execPath)
#if (SJME_CONFIG_NAL_EXEC_PATH == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

sjme_errorCode sjme_nal_default_execPath(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	sjme_lpstr temp;
	sjme_jint tempLen, procLen;

	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Setup buffer that is slightly larger, to detect small buffers. */
	tempLen = outLen + 16;
	temp = sjme_alloca(sizeof(*temp) * tempLen);
	if (temp == NULL)
		return sjme_error_outOfMemory(NULL, temp);

	/* Clear. */
	memset(temp, 0, sizeof(*temp) * tempLen);

	/* Unfortunately the only way to tell if a path is too long is by */
	/* requesting more than what the user requested. */
	procLen = GetModuleFileNameA(NULL, temp, tempLen);
	if (procLen > 0 && procLen < outLen)
		strncpy(out, temp, sjme_min(procLen, outLen));

	/* Cleanup. */
	sjme_alloca_free(temp);

	/* If the path is too long, then fail. */
	if (procLen < 0 || procLen >= outLen)
		return SJME_ERROR_PATH_TOO_LONG;
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(execPath)

#pragma region(nanotime)
#if (SJME_CONFIG_NAL_NANOTIME == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

sjme_errorCode sjme_nal_default_nanoTime(
	sjme_attrOutNotNull sjme_jlong* result)
{
	LARGE_INTEGER freq;
	LARGE_INTEGER ticks;
	
	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get frequency of the clock. */
	memset(&freq, 0, sizeof(freq));
	if (!QueryPerformanceFrequency(&freq))
		return SJME_ERROR_NATIVE_SYSTEM_CLOCK_FAILURE;
	
	/* Get actual counter. */
	memset(&ticks, 0, sizeof(ticks));
	if (!QueryPerformanceCounter(&ticks))
		return SJME_ERROR_NATIVE_SYSTEM_CLOCK_FAILURE;
	
	/* Calculate time. */
	/* Freq: A pointer to a variable that receives the current */
	/* performance-counter frequency, in counts per second. */
	result->full = (ticks.QuadPart / (freq.QuadPart * UINT64_C(1000000000)) /
		UINT64_C(1000000000));
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(nanotime)

#pragma region(pathStyle)
#if (SJME_CONFIG_NAL_PATH_STYLE == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

sjme_errorCode sjme_nal_default_pathStyle(
	sjme_attrOutNotNull const sjme_path_style** outStyle)
{
	if (outStyle == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_HAS_OS_WINDOWS_16)
	*outStyle = &sjme_path_styles[SJME_PATH_STYLE_DOS];
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS_CE)
	*outStyle = &sjme_path_styles[SJME_PATH_STYLE_VFAT];
#else
	*outStyle = &sjme_path_styles[SJME_PATH_STYLE_WINDOWS];
#endif
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(pathStyle)

#pragma region(tcpUdp)
#if (SJME_CONFIG_NAL_TCP_UDP == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

typedef struct sjme_stream_biNetSocketData
{
	/** The socket descriptor. */
	SOCKET sfd;

	/** The listening file descriptor. */
	SOCKET lfd;

	/** The remote file descriptor. */
	SOCKET rfd;
} sjme_stream_biNetSocketData;

static sjme_errorCode sjme_stream_inputNetAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
	if (stream == NULL || inImplState == NULL || outAvail == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputNetClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputNetInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_biNetSocketData* socketData;

	socketData = (sjme_stream_biNetSocketData*)data;
	if (stream == NULL || inImplState == NULL || socketData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just copy the FDs over. */
	inImplState->handle.i = socketData->sfd;
	inImplState->handleTwo.i = socketData->rfd;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputNetRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_stream_inputFunctions sjme_stream_inputNetFunctions =
{
	sjme_sm(.available, sjme_stream_inputNetAvailable),
	sjme_sm(.close, sjme_stream_inputNetClose),
	sjme_sm(.init, sjme_stream_inputNetInit),
	sjme_sm(.read, sjme_stream_inputNetRead),
};

static sjme_errorCode sjme_stream_outputNetClose(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_outputNetFlush(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_outputNetInit(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_biNetSocketData* socketData;

	socketData = (sjme_stream_biNetSocketData*)data;
	if (stream == NULL || inImplState == NULL || socketData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Just copy the FDs over. */
	inImplState->handle.i = socketData->sfd;
	inImplState->handleTwo.i = socketData->rfd;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_outputNetWrite(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_buffer buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (stream == NULL || inImplState == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_stream_outputFunctions sjme_stream_outputNetFunctions =
{
	sjme_sm(.close, sjme_stream_outputNetClose),
	sjme_sm(.flush, sjme_stream_outputNetFlush),
	sjme_sm(.init, sjme_stream_outputNetInit),
	sjme_sm(.write, sjme_stream_outputNetWrite),
};

sjme_errorCode sjme_nal_default_tcpUdp(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNullable sjme_stream_input* netIn,
	sjme_attrOutNullable sjme_stream_output* netOut,
	sjme_attrInValue sjme_jboolean isUdp,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port)
{
	sjme_errorCode error;
	sjme_stream_input rawIn;
	sjme_stream_output rawOut;
	sjme_stream_biNetSocketData data;
	
	if (allocPool == NULL || (netIn == NULL && netOut == NULL) ||
		(!listening && address == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (port < 1 || port > 65535)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Initialize blank stream state. */
	memset(&data, 0, sizeof(data));
	error = SJME_ERROR_NONE;
	rawIn = NULL;
	rawOut = NULL;

	if (SJME_JNI_TRUE)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Open output stream. */
	if (sjme_error_is(error = sjme_stream_outputOpen(allocPool,
		&rawOut, &sjme_stream_outputNetFunctions,
		&data, NULL)) || rawOut == NULL)
		goto fail_openOut;

	/* Open input stream. */
	if (sjme_error_is(error = sjme_stream_inputOpen(allocPool,
		&rawIn, &sjme_stream_inputNetFunctions,
		&data, NULL)) || rawIn == NULL)
		goto fail_openIn;

	/* Do we not care about the input stream? Close it! */
	if (rawIn != NULL && netIn == NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(rawIn))))
			goto fail_closeIn;
		rawIn = NULL;
	}

	/* Ditto for the output stream if we do not care for it. */
	if (rawOut != NULL && netOut == NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(rawOut))))
			goto fail_closeOut;
		rawOut = NULL;
	}

	/* Return resultant raw streams */
	if (netIn != NULL)
		*netIn = rawIn;
	if (netOut != NULL)
		*netOut = rawOut;

	/* Success! */
	return SJME_ERROR_NONE;

fail_closeIn:
fail_closeOut:
fail_openIn:
	if (rawIn != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(rawIn));
fail_openOut:
	if (rawOut != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(rawOut));

	/* Is a standard error set? */
	if (sjme_error_is(error))
		return sjme_error_default(error);

	/* Use errno, if possible. */
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

#endif
#pragma endregion(tcpUdp)

#pragma region(threadSleep)
#if (SJME_CONFIG_NAL_THREAD_SLEEP == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

sjme_errorCode sjme_nal_default_threadSleep(
	sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos)
{
	LARGE_INTEGER baseTime;
	
	/* Yield instead. */
	if (millis <= 0 && nanos <= 0)
		return sjme_nal_default_threadYield();
		
	/* Sleep for the given number of milliseconds. */
	if (millis > 0)
		Sleep(millis);

	/* Burn the CPU to consume the nanoseconds. */
	QueryPerformanceCounter(&baseTime);
	while (nanos > 0)
		nanos = 0; /* TODO */

	/* Success! */
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(threadSleep)

#pragma region(threadYield)
#if (SJME_CONFIG_NAL_THREAD_YIELD == SJME_CONFIG_NAL_IMPLEMENT_WIN32)
	
sjme_errorCode sjme_nal_default_threadYield(void)
{
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_NT_4)
	if (!SwitchToThread())
		SetLastError(0);
#else
	Sleep(0);
#endif

	/* Success! */
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(threadYield)

#pragma region(userHome)
#if (SJME_CONFIG_NAL_USER_HOME == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

#if !defined(CSIDL_PROFILE)
	#define CSIDL_PROFILE 0x0028
#endif

/** @code sjme_nal_win32_SHGetFolderPathA @endcode . */
typedef HRESULT (WINAPI *sjme_nal_win32_SHGetFolderPathA)(HWND,
	INT, HANDLE, DWORD, LPSTR);

sjme_errorCode sjme_nal_default_userHome(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	OSVERSIONINFOEX info;
	sjme_lpcstr env;
	sjme_jint envLen;
	HMODULE shellDll;
	DLLGETVERSIONPROC dllVerProc;
	DLLVERSIONINFO dllVer;
	CHAR winPath[MAX_PATH];
	sjme_nal_win32_SHGetFolderPathA shProc;

	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Get the Windows version information. */
	memset(&info, 0, sizeof(info));
	info.dwOSVersionInfoSize = sizeof(info);
	if (!GetVersionEx((LPOSVERSIONINFO)&info))
		return SJME_ERROR_NATIVE_ERROR;

	/* Clear. */
	env = NULL;

	/* Windows NT, this is just %USERPROFILE%. */
	if (info.dwPlatformId == VER_PLATFORM_WIN32_NT)
		env = getenv("USERPROFILE");

	/* Windows 9x/ME. */
	else if (info.dwPlatformId == VER_PLATFORM_WIN32_WINDOWS)
	{
		/* Find the version procedure from the shell library. */
		shellDll = GetModuleHandleA("shell32.dll");
		if (shellDll == NULL)
			shellDll = LoadLibrary("shell32.dll");
		dllVerProc = NULL;
		if (shellDll != NULL)
			dllVerProc = (DLLGETVERSIONPROC)GetProcAddress(shellDll,
				"DllGetVersion");

		/* If it is available, then grab it. */
		memset(&dllVer, 0, sizeof(dllVer));
		dllVer.cbSize = sizeof(dllVer);
		if (dllVerProc != NULL)
			if (!dllVerProc(&dllVer))
				memset(&dllVer, 0, sizeof(dllVer));

		/* If shell32.dll is 5.0+ (WinME) then use CSIDL_PROFILE. */
		if (dllVer.dwMajorVersion >= 5)
		{
			/* Locate the procedure for getting the shell path. */
			shProc = (sjme_nal_win32_SHGetFolderPathA)GetProcAddress(shellDll,
				"SHGetFolderPathA");
			if (shProc != NULL)
			{
				memset(winPath, 0, sizeof(winPath));
				if (shProc(NULL, CSIDL_PROFILE,
					NULL, 0/*SHGFP_TYPE_CURRENT*/, winPath))
					env = winPath;
			}
		}

		/* Otherwise, check the registry (or fallback to it). */
		if (env == NULL)
		{
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
		}
	}

	/* Windows 3.1, just place somewhere in the C: drive. */
	else if (info.dwPlatformId == VER_PLATFORM_WIN32s)
		env = "C:\\SQUIRREL.JME";

	/* If not set, then set to somewhere on the C: drive. */
	if (env == NULL)
		env = "C:\\ProgramData\\SquirrelJME";

	/* Too long of a path? */
	envLen = strlen(env);
	if (envLen > outLen || envLen > SJME_MAX_PATH)
		return SJME_ERROR_PATH_TOO_LONG;

	/* Give the resultant path. */
	strncpy(out, env, sjme_min(envLen, outLen));
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(userHome)

#pragma region(userName)
#if (SJME_CONFIG_NAL_USER_NAME == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

sjme_errorCode sjme_nal_default_userName(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
#define BUF_SIZE 128
	OSVERSIONINFOEX info;
	HKEY defKey, useKey;
	sjme_lpcstr env;
	CHAR classicName[BUF_SIZE];
	DWORD bufSize;

	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Get the Windows version information. */
	memset(&info, 0, sizeof(info));
	info.dwOSVersionInfoSize = sizeof(info);
	if (!GetVersionEx((LPOSVERSIONINFO)&info))
		return SJME_ERROR_NO_USER_LOGIN;

	/* Clear. */
	env = NULL;

	/* Windows NT, this is just %USERNAME%. */
	if (info.dwPlatformId == VER_PLATFORM_WIN32_NT)
		env = getenv("USERNAME");

	/* Windows 9x/ME. */
	else if (info.dwPlatformId == VER_PLATFORM_WIN32_WINDOWS)
	{
		/* Open keys for the default and current user. */
		defKey = NULL;
		useKey = NULL;
		if (!RegOpenKeyExA(HKEY_USERS, ".DEFAULT",
			0, KEY_READ, &defKey))
			defKey = NULL;
		if (!RegOpenKeyExA(HKEY_CURRENT_USER, NULL,
			0, KEY_READ, &useKey))
			useKey = NULL;

		/* Are these both actually valid and different? */
		/* Note that we cannot get the path that the registry is at (oddly) */
		/* so all we know is that user profiles are being used. */
		if (defKey != NULL && useKey != NULL && defKey != useKey)
		{
			/* Get the user's name. */
			bufSize = BUF_SIZE - 1;
			memset(classicName, 0, sizeof(classicName));
			if (GetUserNameA(classicName, &bufSize))
			{
				classicName[BUF_SIZE - 1] = '\0';
				env = classicName;
			}
		}

		/* The default key was relatively opened, so it has to be closed. */
		if (defKey != NULL)
			RegCloseKey(defKey);

		/* If the current user is the default user, then there is no login. */
		if (defKey == useKey)
			return SJME_ERROR_NO_USER_LOGIN;
	}

	/* Windows 3.1 does not support logins. */
	else if (info.dwPlatformId == VER_PLATFORM_WIN32s)
		return SJME_ERROR_NO_USER_LOGIN;

	/* Could not determine the user name. */
	if (env == NULL)
		return SJME_ERROR_NO_USER_LOGIN;

	/* Give the resultant path. */
	strncpy(out, env, sjme_min(strlen(env), outLen));
	return SJME_ERROR_NONE;
#undef BUF_SIZE
}

#endif
#pragma endregion(userName)
