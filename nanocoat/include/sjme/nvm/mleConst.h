/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * MLE constants.
 * 
 * @since 2025/02/23
 */

#ifndef SQUIRRELJME_MLECONST_H
#define SQUIRRELJME_MLECONST_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_MLECONST_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

typedef enum sjme_nvm_mle_builtInEncodingType
{
	/** Unspecified, use defined property or assume UTF-8. */
	SJME_NVM_MLE_ENCODING_UNSPECIFIED = 0,
	
	/** UTF-8. */
	SJME_NVM_MLE_ENCODING_UTF8 = 1,
	
	/** ASCII. */
	SJME_NVM_MLE_ENCODING_ASCII = 2,
	
	/** IBM037 (EBCDIC). */
	SJME_NVM_MLE_ENCODING_IBM037 = 3,
	
	/** ISO-8859-1. */
	SJME_NVM_MLE_ENCODING_ISO_8859_1 = 4,
	
	/** ISO-8859-15. */
	SJME_NVM_MLE_ENCODING_ISO_8859_15 = 5,
	
	/** Shift-JIS. */
	SJME_NVM_MLE_ENCODING_SHIFT_JIS = 6,
	
	/** IBM437. */
	SJME_NVM_MLE_ENCODING_IBM437 = 7,
	
	/** The number of built-in encodings. */
	SJME_NVM_MLE_ENCODING_NUM_BUILTIN = 8,
} sjme_nvm_mle_builtInEncodingType;
	
/**
 * The built-in locales which are supported.
 *
 * @since 2025/06/22
 */
typedef enum sjme_nvm_mle_builtInLocaleType
{
	/** Unspecified. */
	SJME_NVM_MLE_LOCALE_UNSPECIFIED = 0,

	/** English, US. */
	SJME_NVM_MLE_LOCALE_US_ENGLISH = 1,
} sjme_nvm_mle_builtInLocaleType;
	
/**
 * The type of line ending the system uses.
 *
 * @since 2025/03/02
 */
typedef enum sjme_nvm_mle_lineEndingType
{
	/** Unknown. */
	SJME_NVM_MLE_LINE_ENDING_UNSPECIFIED = 0,
	
	/** LF. */
	SJME_NVM_MLE_LINE_ENDING_LF = 1,
	
	/** CR. */
	SJME_NVM_MLE_LINE_ENDING_CR = 2,
	
	/** CRLF. */
	SJME_NVM_MLE_LINE_ENDING_CRLF = 3,
		
	/** Number of line ending types. */
	SJME_NVM_MLE_NUM_LINE_ENDINGS = 4,
} sjme_nvm_mle_lineEndingType;

/**
 * The memory profiles available.
 *
 * @since 2025/06/24
 */
typedef enum sjme_nvm_mle_memoryProfileType
{
	/** Minimal memory usage. */
	SJME_NVM_MLE_MEMORY_PROFILE_MINIMAL = -1,

	/** Normal memory usage. */
	SJME_NVM_MLE_MEMORY_PROFILE_NORMAL = 0,
} sjme_nvm_mle_memoryProfileType;
	
/**
 * Standard pipe descriptor IDs.
 *
 * @since 2025/02/23
 */
typedef enum sjme_nvm_mle_standardPipeType
{
	/** Standard input. */
	SJME_NVM_MLE_STD_PIPE_STDIN = 0,
	
	/** Standard output. */
	SJME_NVM_MLE_STD_PIPE_STDOUT = 1,
	
	/** Standard error. */
	SJME_NVM_MLE_STD_PIPE_STDERR = 2,
	
	/** The number of standard pipes. */
	SJME_NVM_MLE_NUM_STD_PIPES = 3,
} sjme_nvm_mle_standardPipeType;

/**
 * The thread model in use.
 *
 * @since 2025/06/29
 */
typedef enum sjme_nvm_mle_threadModel
{
	/** Single cooperatively threaded. */
	SJME_NVM_MLE_THREAD_SINGLE_COOP = 0,
	
	/** Single threaded, with preemption. */
	SJME_NVM_MLE_THREAD_SINGLE_PREEMPT = 1,
	
	/** Simultaneous Multi-threaded. */
	SJME_NVM_MLE_THREAD_MULTI = 2,
	
	/** The number of threading models. */
	SJME_NVM_MLE_THREAD_NUM_MODELS = 3,
} sjme_nvm_mle_threadModel;
	
/**
 * The virtual machine type.
 *
 * @since 2025/02/23
 */
typedef enum sjme_nvm_mle_vmType
{
	/** Not known. */
	SJME_NVM_MLE_VM_TYPE_UNKNOWN = 0,
	
	/** Running on Standard Java SE. */
	SJME_NVM_MLE_VM_TYPE_JAVA_SE = 1,
	
	/** Running on SpringCoat. */
	SJME_NVM_MLE_VM_TYPE_SPRINGCOAT = 2,
	
	/** Running on SummerCoat. */
	SJME_NVM_MLE_VM_TYPE_SUMMERCOAT = 3,
	
	/** Running on NanoCoat. */
	SJME_NVM_MLE_VM_TYPE_NANOCOAT = 4,
	
	/** The number of VM types. */
	SJME_NVM_MLE_NUM_VM_TYPES = 5,
} sjme_nvm_mle_vmType;
	
/**
 * The type of default directory used.
 * 
 * This is the same as @c cc.squirreljme.runtime.cldc.full.SystemPathProvider .
 * 
 * @since 2024/08/09
 */
typedef enum sjme_nvm_defaultDirectoryType
{
	/** Unknown. */
	SJME_NVM_DEFAULT_DIRECTORY_UNKNOWN = 0,
	
	/** The cache directory. */
	SJME_NVM_DEFAULT_DIRECTORY_CACHE = 1,
	
	/** The config directory. */
	SJME_NVM_DEFAULT_DIRECTORY_CONFIG = 2,
	
	/** The data directory. */
	SJME_NVM_DEFAULT_DIRECTORY_DATA = 3,
	
	/** The state directory. */
	SJME_NVM_DEFAULT_DIRECTORY_STATE = 4,
	
	/** The native library directory. */
	SJME_NVM_DEFAULT_DIRECTORY_NATIVES = 5,
	
	/** Executable directory. */
	SJME_NVM_DEFAULT_DIRECTORY_EXEC = 6,

	/** Temporary directory. */
	SJME_NVM_DEFAULT_DIRECTORY_TEMPORARY = 7,

	/** The libraries directory. */
	SJME_NVM_DEFAULT_DIRECTORY_LIBRARIES = 8,

	/** The non-volatile storage directory. */
	SJME_NVM_DEFAULT_DIRECTORY_BUCKET_DATA = 9,

	/** The extra bucket directory. */
	SJME_NVM_DEFAULT_DIRECTORY_BUCKET_EXTRA = 10,
	
	/** The number of default directory types. */
	SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPES = 11,
} sjme_nvm_defaultDirectoryType;

/**
 * Virtual machine description identifier.
 *
 * @since 2026/01/1
 */
typedef enum sjme_nvm_vmDescriptionType
{
	/** Unspecified. */
	SJME_NVM_VM_DESC_UNSPECIFIED = 0,
	
	/** The VM version. */
	SJME_NVM_VM_DESC_VM_VERSION = 1,
	
	/** The VM name. */
	SJME_NVM_VM_DESC_VM_NAME = 2,
	
	/** The VM Vendor. */
	SJME_NVM_VM_DESC_VM_VENDOR = 3,
	
	/** The VM E-mail. */
	SJME_NVM_VM_DESC_VM_EMAIL = 4,
	
	/** The VM URL. */
	SJME_NVM_VM_DESC_VM_URL = 5,
	
	/** The executable path of the VM. */
	SJME_NVM_VM_DESC_EXECUTABLE_PATH = 6,
	
	/** The operating system name. */
	SJME_NVM_VM_DESC_OS_NAME = 7,
	
	/** The operating system version. */
	SJME_NVM_VM_DESC_OS_VERSION = 8,
	
	/** The operating system architecture. */
	SJME_NVM_VM_DESC_OS_ARCH = 9,
	
	/**
	 * The current virtual machine security policy, this is used by
	 * @code AccessController @endcode.
	 */
	SJME_NVM_VM_DESC_VM_SECURITY_POLICY = 10,
	
	/** Single lines of legal text and copyrights used for ports. */
	SJME_NVM_VM_DESC_THIRD_PARTY_LEGAL_LINE = 11,
	
	/** Full document of legal text, with entire licenses. */
	SJME_NVM_VM_DESC_THIRD_PARTY_LEGAL_DOCUMENT = 12,
	
	/** The path separator used. */
	SJME_NVM_VM_DESC_PATH_SEPARATOR = 13,
	
	/** The virtual machine info. */
	SJME_NVM_VM_DESC_VM_INFO = 14,
	
	/** Unknown. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_UNKNOWN = 15,
	
	/** The cache directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_CACHE = 16,
	
	/** The config directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_CONFIG = 17,
	
	/** The data directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_DATA = 18,
	
	/** The state directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_STATE = 19,
	
	/** The native library directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_NATIVES = 20,
	
	/** Executable directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_EXEC = 21,
	
	/** Temporary directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_TEMPORARY = 22,
	
	/** The libraries directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_LIBRARIES = 23,
	
	/** The non-volatile storage directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_BUCKET_DATA = 24,
	
	/** The extra bucket directory. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_BUCKET_EXTRA = 25,
	
	/** The number of default directory types. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_NUM_TYPES = 26,
	
	/** Default directory reserved: 12. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_12 = 27,
	
	/** Default directory reserved: 13. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_13 = 28,
	
	/** Default directory reserved: 14. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_14 = 29,
	
	/** Default directory reserved: 15. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_15 = 30,
	
	/** Default directory reserved: 16. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_16 = 31,
	
	/** Default directory reserved: 17. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_17 = 32,
	
	/** Default directory reserved: 18. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_18 = 33,
	
	/** Default directory reserved: 19. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_19 = 34,
	
	/** Default directory reserved: 20. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_RESERVED_20 = 35,
	
	/** The number of reserved directories. */
	SJME_NVM_VM_DESC_DEFAULT_DIR_NUM_RESERVED = 36,
	
	/** The current number of properties. */
	SJME_NVM_VM_DESC_NUM_TYPES = 37,
} sjme_nvm_vmDescriptionType;

/**
 * Used to get a statistic from the VM.
 * 
 * @since 2026/05/17
 */
typedef enum sjme_nvm_vmStatisticType
{
	/** Unspecified. */
	SJME_NVM_VM_STAT_UNSPECIFIED = 0,
	
	/** The amount of free memory. */
	SJME_NVM_VM_STAT_MEM_FREE = 1,
	
	/** The maximum amount of memory. */
	SJME_NVM_VM_STAT_MEM_MAX = 2,
	
	/** The amount of used memory. */
	SJME_NVM_VM_STAT_MEM_USED = 3,
	
	/**
	 * The number of possible threads, if the virtual machine is
	 * cooperatively single threaded then this should always
	 * return 1.
	 */
	SJME_NVM_VM_STAT_CPU_THREAD_COUNT = 4,
	
	/**
	 * The root instance identifier of the root virtual machine. This is
	 * generally a process ID or other unique identifier.
	 * 
	 * All virtual machines launched, whether they have the same process and
	 * thread, a different thread, or a different thread will have the same
	 * root instance ID.
	 */
	SJME_NVM_VM_STAT_ROOT_INSTANCE_ID = 5,
	
	/** The number of statistics. */
	SJME_NVM_VM_STAT_NUM_STATISTICS = 6,
} sjme_nvm_vmStatisticType;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_MLECONST_H
}
#undef SJME_CXX_MLECONST_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_MLECONST_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MLECONST_H */
