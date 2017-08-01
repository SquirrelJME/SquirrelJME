/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: WinterCoat
//     Copyright (C) 2013-2016 Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * WinterCoat includes.
 *
 * @since 2016/10/19
 */

/** Header guard. */
#ifndef SJME_hGWINTERCOATH
#define SJME_hGWINTERCOATH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXWINTERCOATH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#include <pthread.h>
#include <stdnoreturn.h>

#include "jni.h"
#include "jvm.h"

/** Verbosity Modes. */
#define WC_VERBOSE_MODE_DEBUG 1
#define WC_VERBOSE_MODE_CLASS 2
#define WC_VERBOSE_MODE_GC 3
#define WC_VERBOSE_MODE_JNI 4
#define WC_VERBOSE_MODE_ERROR 5
#define WC_VERBOSE_MODE_TODO 6

/**
 * This is used to store static string information.
 *
 * @since 2016/10/19
 */
typedef struct WC_StaticString
{
	/** Characters of the string in modified UTF-8. */
	const char* utfchars;
	
	/** The length of the string in UTF-8 characters. */
	jint utflen;
	
	/** The string as an object. */
	jstring object;
} WC_StaticString;

/**
 * This acts as a single link in the system property table.
 *
 * @since 2016/10/19
 */
typedef struct WC_SystemPropertyLink WC_SystemPropertyLink;
struct WC_SystemPropertyLink
{
	/** The system property key. */
	WC_StaticString* key;
	
	/** The system property value. */
	WC_StaticString* value;
	
	/** The next link. */
	WC_SystemPropertyLink* next;
};

/**
 * The WinterCoat VM structure.
 *
 * @since 2016/10/19
 */
typedef union WC_JavaVM WC_JavaVM;
typedef union WC_JNIEnv WC_JNIEnv;
union WC_JavaVM
{
	/** Native Java interface. */
	struct JNIInvokeInterface_ native;
	
	/** WinterCoat interface. */
	struct
	{
		/** Lock on this virtual machine. */
		pthread_mutex_t* mutex;
		
		/** Linked list of system properties. */
		WC_SystemPropertyLink* syspropchain;
		
		/** The environment thread chain. */
		WC_JNIEnv* envchain;
	} furry;
};

/**
 * The WinterCoat Environment structure, used per thread.
 *
 * @since 2016/10/19
 */
union WC_JNIEnv
{
	/** Native interface. */
	struct JNINativeInterface_ native;
	
	/** WinterCoat interface */
	struct
	{
		/** Lock on this environment. */
		pthread_mutex_t* mutex;
		
		/** The owning virtual machine. */
		WC_JavaVM* vm;
		
		/** The thread for this environment. */
		pthread_t* thread;
		
		/** The next environment. */
		WC_JNIEnv* next;
	} furry;
};

/**
 * Allocates data which is cleared to zero of the given size, if allocation
 * fails then the VM fatally exits.
 *
 * @param plen The bytes to allocate.
 * @return The allocated data.
 * @since 2016/10/19
 */
void* WC_ForcedMalloc(jint plen);

/**
 * Returns a static string from the given input string.
 *
 * @param pstr The string to make static.
 * @param plen The characters in the string.
 * @return The string made static.
 * @since 2016/10/19
 */
WC_StaticString* WC_GetStaticString(const char* const pstr, jint plen);

/**
 * Checks for the specified condition and if it fails, an abort occurs.
 *
 * @param pfile The location of the TODO.
 * @param pline The line of the TODO.
 * @param pfunc The function of the TODO.
 * @param pcode The error code.
 * @param pcond The condition.
 * @since 2016/10/19
 */
#define WC_ASSERT(code, i) \
	WC_ASSERT_real(__FILE__, __LINE__, __func__, (code), (i))
void WC_ASSERT_real(const char* const pin, int pline, const char* const pfunc,
	const char* const pcode, int pcond);

/**
 * Prints the specified location and aborts.
 *
 * @param pfile The location of the abort.
 * @param pline The line of the abort.
 * @param pfunc The function of the abort.
 * @since 2016/10/19
 */
#define WC_TODO() WC_TODO_real(__FILE__, __LINE__, __func__)
_Noreturn void WC_TODO_real(const char* const pin, int pline, const char* const pfunc);

/**
 * Spits out a verbose message.
 *
 * @param pfile The location of the verbose message.
 * @param pline The line of the verbose message.
 * @param pfunc The function of the verbose message.
 * @param pmode The verbose message mode.
 * @param pmesg The message to print.
 * @since 2016/10/19
 */
#define WC_VERBOSE(mode, msg, ...) WC_VERBOSE_real(__FILE__, __LINE__, \
	__func__, (mode), (msg), __VA_ARGS__)
void WC_VERBOSE_real(const char* const pin, int pline,
	const char* const pfunc, int pmode,
	const char* const pmesg, ...);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXWINTERCOATH
}
#undef SJME_cXWINTERCOATH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXWINTERCOATH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGWINTERCOATH */

