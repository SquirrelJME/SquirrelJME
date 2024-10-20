/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Zip Support.
 * 
 * @since 2023/12/31
 */

#ifndef SQUIRRELJME_ZIP_H
#define SQUIRRELJME_ZIP_H

#include "sjme/stream.h"
#include "sjme/seekable.h"
#include "sjme/path.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ZIP_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Opaque Zip structure.
 *
 * @since 2023/12/31
 */
typedef struct sjme_zipBase* sjme_zip;

/**
 * Zip access information.
 *
 * @since 2023/12/31
 */
typedef struct sjme_zipBase
{
	/** The closeable for this Zip. */
	sjme_closeableBase closeable;
	
	/** The pool this was allocated within. */
	sjme_alloc_pool* inPool;
	
	/** The central directory position. */
	sjme_jint centralDirPos;
	
	/** The logical start position of the central directory. */
	sjme_jint logicalCentralDirPos;
	
	/** The end central directory record position. */
	sjme_jint endCentralDirPos;
	
	/** The start position of the Zip in the seekable. */
	sjme_jint archiveStartPos;
	
	/** The seekable where the Zip is. */
	sjme_seekable seekable;
	
	/** The lock for accessing the Zip. */
	sjme_thread_spinLock lock;
} sjme_zipBase;

/**
 * Zip entry.
 *
 * @since 2023/12/31
 */
typedef struct sjme_zip_entry
{
	/** The Zip this is in. */
	sjme_zip zip;
	
	/** The version this was made by. */
	sjme_jchar versionMadeBy;
	
	/** The version needed to extract. */
	sjme_jchar versionNeeded;
	
	/** General purpose bits. */
	sjme_jchar generalBits;
	
	/** Compression method. */
	sjme_jchar method;
	
	/** Last modification time. */
	sjme_jchar lastModTime;
	
	/** Last modification date. */
	sjme_jchar lastModDate;
	
	/** Uncompressed CRC. */
	sjme_jint uncompressedCrc;
	
	/** Compressed size. */
	sjme_jint compressedSize;
	
	/** Uncompressed size. */
	sjme_jint uncompressedSize;
	
	/** Disk number. */
	sjme_jchar diskNum;
	
	/** Internal attributes. */
	sjme_jchar internalAttrib;
	
	/** External attributes. */
	sjme_jint externalAttrib;
	
	/** Relative offset to the local file header. */
	sjme_jint offset;
	
	/** The file name. */
	sjme_cchar name[SJME_MAX_FILE_NAME];
} sjme_zip_entry;

/**
 * Opens a stream to read the given Zip entry.
 *
 * @param inEntry The entry to read from.
 * @param outStream The resultant stream for reading the data.
 * @return On any error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_zip_entryRead(
	sjme_attrInNotNull const sjme_zip_entry* inEntry,
	sjme_attrOutNotNull sjme_stream_input* outStream);

/**
 * Locates an entry within a Zip file.
 *
 * @param inZip The Zip to look within.
 * @param outEntry The resultant entry.
 * @param entryName The entry to look for.
 * @return On any error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_zip_locateEntry(
	sjme_attrInNotNull sjme_zip inZip,
	sjme_attrOutNotNull sjme_zip_entry* outEntry,
	sjme_attrInNotNull sjme_lpcstr entryName);

/**
 * Opens a Zip file at the given location.
 *
 * @param inPool The pool for structure allocation.
 * @param outZip The resultant opened Zip file.
 * @param rawData Raw data to the Zip in memory somewhere.
 * @param rawSize The length of the Zip data.
 * @return Any resultant error code, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_zip_openMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_zip* outZip,
	sjme_attrInNotNull void* rawData,
	sjme_attrInPositive sjme_jint rawSize);

/**
 * Opens a Zip from the given seekable.
 * 
 * @param inPool The pool to allocate from.
 * @param outZip The resultant opened Zip.
 * @param inSeekable The seekable to use for accessing data.
 * @return Any resultant error, if any.
 * @since 2024/08/12
 */
sjme_errorCode sjme_zip_openSeekable(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_zip* outZip,
	sjme_attrInNotNull sjme_seekable inSeekable); 

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ZIP_H
}
		#undef SJME_CXX_SQUIRRELJME_ZIP_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ZIP_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ZIP_H */
