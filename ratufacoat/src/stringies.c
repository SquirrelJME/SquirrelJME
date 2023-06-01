/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <stdio.h>

#include "sjmerc.h"
#include "stringies.h"
#include "error.h"

/** Error string storage. */
typedef struct sjme_jvmErrorString
{
	sjme_jint code;
	const char* string;
} sjme_jvmErrorString;

/** Unknown error description. */
static const sjme_jvmErrorString sjme_jvmErrorUnknown =
{
	SJME_ERROR_UNKNOWN, 
	"Unknown Error"
};

/** Error strings that may be selected. */
static const sjme_jvmErrorString sjme_jvmErrorStrings[] =
{
	{SJME_ERROR_NOSUCHFILE,         "No such file"},
	{SJME_ERROR_INVALID_ARGUMENT,         "Invalid argument"},
	{SJME_ERROR_ENDOFFILE,          "End of file"},
	{SJME_ERROR_NO_MEMORY,          "No memory available"},
	{SJME_ERROR_NONATIVEROM,        "No Native ROM specified"},
	{SJME_ERROR_NOFILES,            "Files are not supported"},
	{SJME_ERROR_INVALIDROMMAGIC,    "Invalid ROM Magic Number"},
	{SJME_ERROR_INVALIDJARMAGIC,    "Invalid JAR Magic Number"},
	{SJME_ERROR_INVALIDBOOTRAMEND,  "Invalid BootRAM end"},
	{SJME_ERROR_INVALIDBOOTRAMSEED, "Invalid BootRAM seed"},
	{SJME_ERROR_CPUBREAKPOINT,      "Breakpoint hit"},
	{SJME_ERROR_NOJAVAWRITE,        "Java value write not supported"},
	{SJME_ERROR_READ_ERROR,         "Read error"},
	{SJME_ERROR_EARLYEOF,           "Unexpected end of file"},
	{SJME_ERROR_JVMNOTREADY,        "Virtual machine not ready"},
	{SJME_ERROR_JVMEXIT_SUV_OKAY,   "Normal JVM Exit, boot okay."},
	{SJME_ERROR_JVMEXIT_SUV_FAIL,   "Supervisor bootstrap failed"},
	{SJME_ERROR_THREADRETURN,       "Return from thread"},
	{SJME_ERROR_BADADDRESS,         "Bad address"},
	{SJME_ERROR_INVALIDOP,          "invalid operation"},
	{SJME_ERROR_VMMNEWFAIL,         "Initializing new JVM failed"},
	{SJME_ERROR_INVALIDSIZE,        "Invalid size"},
	{SJME_ERROR_ADDRRESFAIL,        "Address resolution failure"},
	{SJME_ERROR_INVALIDMEMTYPE, "Invalid memory type"},
	{SJME_ERROR_REGISTEROVERFLOW, "Register overflow"},
	{SJME_ERROR_VMMMAPFAIL, "Virtual memory mapping failed"},
	{SJME_ERROR_NULLARGS, "Null arguments"},
	{SJME_ERROR_NEGATIVE_SIZE, "Negative size"},
	{SJME_ERROR_INVALID_MEMHANDLE_KIND, "Invalid memory handle kind"},
	{SJME_ERROR_OUT_OF_BOUNDS, "Out of bounds"},
	{SJME_ERROR_COULD_NOT_SEED, "Could not seed the RNG"},
	{SJME_ERROR_DESTROY_FAIL, "Destruction failed"},
	{SJME_ERROR_INVALID_HANDLE, "Invalid memory handle"},
	{SJME_ERROR_INVALID_DATATYPE, "Invalid data type"},
	{SJME_ERROR_INVALID_BOOTRAM, "Invalid BootRAM"},
	{SJME_ERROR_UNKNOWN_LIBRARY_FORMAT, "Unknown library format"},
	{SJME_ERROR_DRIVER_NOT_FOUND, "Driver not found"},
	{SJME_ERROR_UNKNOWN_PACK_FORMAT, "Unknown pack format"},
	{SJME_ERROR_ZERO_MEMORY_ALLOCATION, "Zero memory allocated"},
	{SJME_ERROR_BAD_DRIVER_INIT, "Could not initialize driver"},
	{SJME_ERROR_INVALID_CLASS_VERSION, "Invalid class version"},
	{SJME_ERROR_UNKNOWN_FORMAT, "Unknown format"},
	{SJME_ERROR_INVALID_NUM_LIBRARIES, "Invalid number of libraries"},
	{SJME_ERROR_FAILED_TO_CLOSE_PACK, "Failed to close the pack file"},
	{SJME_ERROR_INVALID_FORMAT_STATE, "The format is not within a valid state"},
	{SJME_ERROR_INVALID_COUNTER_STATE, "Invalid counter state"},
	{SJME_ERROR_BAD_LOAD_LIBRARY, "Could not load the given library"},
	{SJME_ERROR_INVALID_PACK_FILE, "Invalid pack file"},
	{SJME_ERROR_CORRUPT_TOC, "The TOC has been corrupted"},
	{SJME_ERROR_CORRUPT_PACK_ENTRY, "An entry within the Pack is corrupt"},
	{SJME_ERROR_BAD_PACK_LIB_CLOSE, "Could not close the library via the pack"},
	{SJME_ERROR_INVALID_JAR_FILE, "The JAR has been corrupted"},
	{SJME_ERROR_NOT_IMPLEMENTED, "Function not implemented"},
	{SJME_ERROR_INVALID_STREAM_STATE, "Invalid stream state"},
	{SJME_ERROR_CALCULATE_ERROR, "Calculate error"},
	{SJME_ERROR_NOT_SUPPORTED, "Not supported"},
	{SJME_ERROR_ENGINE_NOT_FOUND, "Engine not found"},
	{SJME_ERROR_ENGINE_INIT_FAILURE, "Engine initialization failure"},
	{SJME_ERROR_INVALID_FUNCTIONAL, "Invalid functional method"},
	{SJME_ERROR_INVALID_UNLOCK_KEY, "Invalid unlock key"},
	{SJME_ERROR_NOT_LOCK_OWNER, "The lock owner is invalid"},
	{SJME_ERROR_BAD_PIPE_INIT, "Bad pipe initialization"},
	{SJME_ERROR_INVALID_LOCK, "Invalid lock."},
	
	{SJME_ERROR_NONE, NULL}
};

/**
 * Returns a pointer to the error string description
 * 
 * @param code The error code to look for. 
 * @return Pointer to the error string that is used, or NULL if not found.
 * @since 2020/08/09
 */
const sjme_jvmErrorString* sjme_locateJvmErrorString(sjme_jint code)
{
	const struct sjme_jvmErrorString* at;
	
	/* Locate the error. */
	for (at = sjme_jvmErrorStrings; at->code != SJME_ERROR_NONE; at++)
	{
		if (at->code == code)
			return at;
	}
	
	/* Not found at all. */
	return NULL;
}

void sjme_describeJvmError(sjme_error* error,
	sjme_jbyte* destMessage, sjme_jint* destLen)
{
	const sjme_jvmErrorString* stringy;
	int readLen;
	
	/* These conditions are not valid. */
	if (error == NULL || destMessage == NULL || destLen == NULL)
	{
		/* Set zero length, if that is even possible. */
		if (destLen != NULL)
			*destLen = 0;
			
		return;
	}

#if defined(SJME_HAS_TERMINAL_OUTPUT)
	/* Locate the string to seed with. */
	stringy = sjme_locateJvmErrorString(error->code);
	if (stringy == NULL)
		stringy = &sjme_jvmErrorUnknown;
	
	/* Build error string out. */
	readLen = snprintf((char*)destMessage, *destLen,
		"JVM Error: %s (%d) %d/0x%x",
		stringy->string,
		error->code,
		error->value,
		error->value);
	
	if (readLen >= 0)
		*destLen = readLen;
#else
	/* Just write it with zero. */
	if (*destLen > 0)
		*destMessage = 0;

	*destLen = 0;
#endif
}
