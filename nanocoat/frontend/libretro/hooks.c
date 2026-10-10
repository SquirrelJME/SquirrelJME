/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <libretro.h>

#include "sjme/native.h"
#include "frontend/libretro/shared.h"
#include "sjme/path.h"
#include "sjme/intern/nal.h"

sjme_errorCode sjme_libretro_vfsClose(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState)
{
	struct retro_vfs_interface* iface;
	struct retro_vfs_file_handle* handle;
	
	if (inSeekable == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover VFS handle. */
	handle = inSeekable->implState.handle;
	iface = inImplState->handleTwo;

	/* Close it. */
	if (iface->close(handle) != 0)
		return SJME_ERROR_IO_EXCEPTION;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_libretro_vfsInit(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	if (inSeekable == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Store both the handle and the VFS interface as there could be some */
	/* weird global state where memory goes missing. */
	inImplState->handle = data;
	inImplState->handleTwo = sjme_libretro_globals.vfs.iface;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_libretro_vfsRead(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	struct retro_vfs_file_handle* handle;
	struct retro_vfs_interface* iface;
	sjme_jint newPos;
	
	if (inSeekable == NULL || inImplState == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover VFS handle. */
	handle = inSeekable->implState.handle;
	iface = inImplState->handleTwo;

	/* Seek to the position. */
	/* Note that we just check for a seek error here as before */
	/* RetroArch #18073 there is a bug with memory mapped files */
	newPos = (sjme_jint)iface->seek(handle, base,
		RETRO_VFS_SEEK_POSITION_START);
	if (newPos < 0)
		return SJME_ERROR_IO_EXCEPTION;

	/* Perform the read. */
	if (iface->read(handle, outBuf, length) != length)
		return SJME_ERROR_IO_EXCEPTION;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_libretro_vfsSize(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	struct retro_vfs_file_handle* handle;
	struct retro_vfs_interface* iface;
	sjme_jint result, base, limit, mul, cap, attempt;
	sjme_jubyte ignored;
	
	if (inSeekable == NULL || inImplState == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover VFS handle. */
	handle = inSeekable->implState.handle;
	iface = inImplState->handleTwo;

	/* Get size from the handle. */
	/* If zero/negative  then the size of the file is unknown, */
	/* need to do manual seek. */
	/* This is the case when RETRO_VFS_FILE_ACCESS_HINT_FREQUENT_ACCESS is */
	/* set and valid for the platform. */
	result = (sjme_jint)iface->size(handle);
	if (result <= 0)
	{
		/* Seek to the end. */
		result = (sjme_jint)iface->seek(handle, 0,
			RETRO_VFS_SEEK_POSITION_END);
		
		/* RetroArch before #18073 has a bug where only set works properly. */
		/* So we need to actually binary search the file size with seek set */
		/* which sounds really silly, but it works! */
		/* Note that the cap is the highest known good value. */
		if (result <= 0)
			for (base = 0, mul = 1, limit = 1, cap = INT32_MAX; cap > 0;)
			{				
				/* Try the limit position. */
				iface->seek(handle, limit,
					RETRO_VFS_SEEK_POSITION_START);
				attempt = (sjme_jint)iface->read(handle, &ignored, 1);
				
				/* If it was valid, we need to bump the limit up. */
				if (attempt == 1)
				{
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
					/* Debug. */
					sjme_message("vfsSize(%d, %d (*%d), %d) -> OK",
						base, limit, mul, cap);
#endif
					
					/* The new base becomes the limit because we know that */
					/* base is valid. */
					base = limit;
					mul *= 2;
					limit += mul;

					/* Try again, the last run is okay. */
					continue;
				}

				/* Set the cap to the limit if lower, since we know */
				/* it is smaller than this size. */
				if (limit < cap)
					cap = limit;

				/* If the cap did not change, then we know the file size. */
				else
				{
					result = cap;
					break;
				}

				/* We failed, so set the limit back to the base, we need */
				/* to scan up from zero again. */
				limit = base;
				mul = 1;
			}

		/* Invalid? */
		if (result <= 0)
			return SJME_ERROR_IO_EXCEPTION;
	}

	/* Success! */
	*outSize = result;
	return SJME_ERROR_NONE;
}

static const sjme_seekable_functions sjme_libretro_vfsFunctions =
{
	sjme_sm(.close, sjme_libretro_vfsClose),
	sjme_sm(.init, sjme_libretro_vfsInit),
	sjme_sm(.read, sjme_libretro_vfsRead),
	sjme_sm(.size, sjme_libretro_vfsSize),
};

static sjme_errorCode sjme_libretro_currentTimeMillis(
	sjme_attrOutNotNull sjme_jlong* result)
{
	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* If in TAS mode, do not use the real-time clock. */
	if (sjme_libretro_globals.tasMode)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Otherwise, use the OS implementation. */
	if (sjme_nal_default.currentTimeMillis != NULL)
		return sjme_nal_default.currentTimeMillis(result);
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_libretro_execPath(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* This is always not implemented. */
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_libretro_fileOpen(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInValue sjme_nal_openMode openMode)
{
	struct retro_vfs_interface* vfs;
	struct retro_vfs_file_handle* handle;
	sjme_jint mode;
		
	if (allocPool == NULL || inPath == NULL || outSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (openMode < SJME_NAL_OPEN_READ || openMode > SJME_NAL_OPEN_WRITE_TRUNCATE)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* VFS is not supported? */
	vfs = sjme_libretro_globals.vfs.iface;
	if (vfs == NULL)
		return SJME_ERROR_NATIVE_ERROR;

	/* Which mode? */
	if (openMode == SJME_NAL_OPEN_WRITE_TRUNCATE)
		mode = RETRO_VFS_FILE_ACCESS_READ |
			RETRO_VFS_FILE_ACCESS_WRITE;
	else if (openMode == SJME_NAL_OPEN_WRITE_EXIST)
		mode = RETRO_VFS_FILE_ACCESS_READ |
			RETRO_VFS_FILE_ACCESS_WRITE |
			RETRO_VFS_FILE_ACCESS_UPDATE_EXISTING;
	else
		mode = RETRO_VFS_FILE_ACCESS_READ;

	/* Attempt opening. */
	handle = vfs->open(inPath, mode,
		RETRO_VFS_FILE_ACCESS_HINT_FREQUENT_ACCESS);
	if (handle == NULL)
		return SJME_ERROR_FILE_NOT_FOUND;

	/* Setup seekable to access it. */
	return sjme_seekable_open(allocPool, outSeekable,
		&sjme_libretro_vfsFunctions,
		handle, NULL);
}

static sjme_errorCode sjme_libretro_pathStyle(
	sjme_attrOutNotNull const sjme_path_style** outStyle)
{
	if (outStyle == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Use the embedded VFAT variant. */
	*outStyle = &sjme_path_styles[SJME_PATH_STYLE_VFAT_EMBEDDED];
	return SJME_ERROR_NONE;
}


static sjme_errorCode sjme_libretro_nanoTime(
	sjme_attrOutNotNull sjme_jlong* result)
{
	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* If in TAS mode, do not use the system monotonic clock. */
	if (sjme_libretro_globals.tasMode)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Otherwise, use the OS implementation. */
	if (sjme_nal_default.nanoTime != NULL)
		return sjme_nal_default.nanoTime(result);
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_libretro_threadSleep(
	sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos)
{
	/* Not sleeping for any duration? */
	if (millis < 0 || nanos < 0 || (millis == 0 && nanos == 0))
		return SJME_ERROR_NONE;

	/* Never yield in TAS mode. */
	if (sjme_libretro_globals.tasMode)
		return SJME_ERROR_NONE;

	/* Otherwise, use the OS implementation. */
	if (sjme_nal_default.threadSleep != NULL)
		return sjme_nal_default.threadSleep(millis, nanos);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_libretro_threadYield(void)
{
	/* Never yield in TAS mode. */
	if (sjme_libretro_globals.tasMode)
		return SJME_ERROR_NONE;

	/* Otherwise, use the OS implementation. */
	if (sjme_nal_default.threadYield != NULL)
		return sjme_nal_default.threadYield();
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_libretro_userHome(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* The home directory is always just /home, this should be somewhere */
	/* on the VFS. */
	strncat(out, "/home", outLen);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_libretro_userName(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* The username here is always libretro */
	strncat(out, "libretro", outLen);
	return SJME_ERROR_NONE;
}

const sjme_nal sjme_libretro_nal =
{
	sjme_sm(.currentTimeMillis, sjme_libretro_currentTimeMillis),
	sjme_sm(.execPath, sjme_libretro_execPath),
	sjme_sm(.fileOpen, sjme_libretro_fileOpen),
	sjme_sm(.getEnv, NULL),
	sjme_sm(.lineEnding, sjme_nal_default_lineEnding),
	sjme_sm(.nanoTime, sjme_libretro_nanoTime),
	sjme_sm(.pathStyle, sjme_libretro_pathStyle),
	sjme_sm(.tcpUdp, NULL),
	sjme_sm(.threadSleep, sjme_libretro_threadSleep),
	sjme_sm(.threadYield, sjme_libretro_threadYield),
	sjme_sm(.stdIo, ){
		{
			sjme_sm(.close, NULL),
			sjme_sm(.in, NULL),
			sjme_sm(.out, NULL),
			sjme_sm(.flush, NULL),
		},
		{
			sjme_sm(.close, NULL),
			sjme_sm(.in, NULL),
			sjme_sm(.out, NULL),
			sjme_sm(.flush, NULL),
		},
		{
			sjme_sm(.close, NULL),
			sjme_sm(.in, NULL),
			sjme_sm(.out, NULL),
			sjme_sm(.flush, NULL),
		},
	},
	sjme_sm(.userHome, sjme_libretro_userHome),
	sjme_sm(.userName, sjme_libretro_userName),
};
