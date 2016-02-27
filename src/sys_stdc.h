/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * Standard C Header.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGSYS_STDCH
#define SJME_hGSYS_STDCH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXSYS_STDCH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/** Configuration Header. */
#include "config.h"

/** Standard C systems only. */
#if SJME_TARGET == SJME_TARGET_CSTANDARD || defined(SJME_STANDARD_C)

/****************************************************************************/

/** Define standard C just in case. */
#ifndef SJME_STANDARD_C
	#define SJME_STANDARD_C
#endif

/** Include standard library stuff. */
#include "stdlib.h"


/** If C99 or better, use stdint. */
#if (defined(__STDC_VERSION__) && __STDC_VERSION__ >= 199901L) || \
	(defined(_MSC_VER) && _MSC_VER >= 1600) || \
	(defined(_POSIX_C_SOURCE) && _POSIX_C_SOURCE >= 200400L)
	/** Include stdint. */
	#include "stdint.h"
	
	/** Use limits. */
	#include "limits.h"
	
	/** Standard argument parsing. */
	#include "stdarg.h"

/** Unknown, do some guessing. */
#else
	/** This header defines some limitations. */
	#include "limits.h"
	
	/** Char type. */
	#if defined(SCHAR_MAX)
		#if SCHAR_MAX == 127L
			#if !defined(SJME_TYPE_INT8)
				typedef signed char int8_t;
				typedef unsigned char uint8_t;
				#define SJME_TYPE_INT8
			#endif
		#elif SCHAR_MAX == 32767L
			#if !defined(SJME_TYPE_INT16)
				typedef signed char int16_t;
				typedef unsigned char uint16_t;
				#define SJME_TYPE_INT16
			#endif
		#elif SCHAR_MAX == 2147483647L
			#if !defined(SJME_TYPE_INT32)
				typedef signed char int32_t;
				typedef unsigned char uint32_t;
				#define SJME_TYPE_INT32
			#endif
		#endif
	#endif
	
	/** Short type. */
	#if defined(SHRT_MAX)
		#if SHRT_MAX == 127L
			#if !defined(SJME_TYPE_INT8)
				typedef signed short int8_t;
				typedef unsigned short uint8_t;
				#define SJME_TYPE_INT8
			#endif
		#elif SHRT_MAX == 32767L
			#if !defined(SJME_TYPE_INT16)
				typedef signed short int16_t;
				typedef unsigned short uint16_t;
				#define SJME_TYPE_INT16
			#endif
		#elif SHRT_MAX == 2147483647L
			#if !defined(SJME_TYPE_INT32)
				typedef signed short int32_t;
				typedef unsigned short uint32_t;
				#define SJME_TYPE_INT32
			#endif
		#endif
	#endif
	
	/** Int type. */
	#if defined(INT_MAX)
		#if INT_MAX == 127L
			#if !defined(SJME_TYPE_INT8)
				typedef signed int int8_t;
				typedef unsigned int uint8_t;
				#define SJME_TYPE_INT8
			#endif
		#elif INT_MAX == 32767L
			#if !defined(SJME_TYPE_INT16)
				typedef signed int int16_t;
				typedef unsigned int uint16_t;
				#define SJME_TYPE_INT16
			#endif
		#elif INT_MAX == 2147483647L
			#if !defined(SJME_TYPE_INT32)
				typedef signed int int32_t;
				typedef unsigned int uint32_t;
				#define SJME_TYPE_INT32
			#endif
		#endif
	#endif
	
	/** Long type. */
	#if defined(LONG_MAX)
		#if LONG_MAX == 127L
			#if !defined(SJME_TYPE_INT8)
				typedef signed long int8_t;
				typedef unsigned long uint8_t;
				#define SJME_TYPE_INT8
				
				#if !defined(INT8_C)
					#define INT8_C(x) x##L
				#endif
				#if !defined(UINT8_C)
					#define UINT8_C(x) x##UL
				#endif
			#endif
		#elif LONG_MAX == 32767L
			#if !defined(SJME_TYPE_INT16)
				typedef signed long int16_t;
				typedef unsigned long uint16_t;
				#define SJME_TYPE_INT16
				
				#if !defined(INT16_C)
					#define INT16_C(x) x##L
				#endif
				#if !defined(UINT16_C)
					#define UINT16_C(x) x##UL
				#endif
			#endif
		#elif LONG_MAX == 2147483647L
			#if !defined(SJME_TYPE_INT32)
				typedef signed long int32_t;
				typedef unsigned long uint32_t;
				#define SJME_TYPE_INT32
				
				#if !defined(INT32_C)
					#define INT32_C(x) x##L
				#endif
				#if !defined(UINT32_C)
					#define UINT32_C(x) x##UL
				#endif
			#endif
		#endif
	#endif
	
	/** Long long type. */
	#if defined(LLONG_MAX)
		#if LLONG_MAX == 127LL
			#if !defined(SJME_TYPE_INT8)
				typedef signed long long int8_t;
				typedef unsigned long long uint8_t;
				#define SJME_TYPE_INT8
				
				#if !defined(INT8_C)
					#define INT8_C(x) x##LL
				#endif
				#if !defined(UINT8_C)
					#define UINT8_C(x) x##ULL
				#endif
			#endif
		#elif LLONG_MAX == 32767LL
			#if !defined(SJME_TYPE_INT16)
				typedef signed long long int16_t;
				typedef unsigned long long uint16_t;
				#define SJME_TYPE_INT16
				
				#if !defined(INT16_C)
					#define INT16_C(x) x##LL
				#endif
				#if !defined(UINT16_C)
					#define UINT16_C(x) x##ULL
				#endif
			#endif
		#elif LLONG_MAX == 2147483647LL
			#if !defined(SJME_TYPE_INT32)
				typedef signed long long int32_t;
				typedef unsigned long long uint32_t;
				#define SJME_TYPE_INT32
				
				#if !defined(INT32_C)
					#define INT32_C(x) x##LL
				#endif
				#if !defined(UINT32_C)
					#define UINT32_C(x) x##ULL
				#endif
			#endif
		#elif LLONG_MAX == 18446744073709551615LL
			#if !defined(SJME_TYPE_INT64)
				typedef signed long long int64_t;
				typedef unsigned long long uint64_t;
				#define SJME_TYPE_INT64
				
				#if !defined(INT64_C)
					#define INT64_C(x) x#LL
				#endif
				#if !defined(UINT64_C)
					#define UINT64_C(x) x#ULL
				#endif
			#endif
		#endif
	#endif
	
	/** Did not detect type. */
	#if !defined(SJME_TYPE_INT8)
		#error Could not detect 8-bit integer type.
	#endif
	#if !defined(SJME_TYPE_INT16)
		#error Could not detect 16-bit integer type.
	#endif
	#if !defined(SJME_TYPE_INT32)
		#error Could not detect 32-bit integer type.
	#endif
	
	/** Constant sizes. */
	#if !defined(INT8_C)
		#define INT8_C(x) x
	#endif
	#if !defined(UINT8_C)
		#define UINT8_C(x) x##U
	#endif
	#if !defined(INT16_C)
		#define INT16_C(x) x
	#endif
	#if !defined(UINT16_C)
		#define UINT16_C(x) x##U
	#endif
	#if !defined(INT32_C)
		#define INT32_C(x) x
	#endif
	#if !defined(UINT32_C)
		#define UINT32_C(x) x##U
	#endif
	
	/** 8-bit limits. */
	#if !defined(INT8_MAX)
		#define INT8_MAX  INT8_C(127)
	#endif
	#if !defined(INT8_MIN)
		#define INT8_MIN  ((int8_t)(-INT8_MAX - INT8_C(1)))
	#endif
	#if !defined(UINT8_MAX)
		#define UINT8_MAX UINT8_C(255)
	#endif
	
	/** 16-bit limits. */
	#if !defined(INT16_MAX)
		#define INT16_MAX  INT16_C(32767)
	#endif
	#if !defined(INT16_MIN)
		#define INT16_MIN  ((int16_t)(-INT16_MAX - INT16_C(1)))
	#endif
	#if !defined(UINT16_MAX)
		#define UINT16_MAX INT16_C(65535)
	#endif
	
	/** 32-bit limits. */
	#if !defined(INT32_MAX)
		#define INT32_MAX  INT32_C(2147483647)
	#endif
	#if !defined(INT32_MIN)
		#define INT32_MIN  ((int32_t)(-INT32_MAX - INT32_C(1)))
	#endif
	#if !defined(UINT32_MAX)
		#define UINT32_MAX UINT32_C(4294967295)
	#endif
	
	/** 64-bit limits. */
	#if defined(SJME_TYPE_INT64)
		#if !defined(INT64_MAX)
			#define INT64_MAX  INT64_C(9223372036854775807)
		#endif
		#if !defined(INT64_MIN)
			#define INT64_MIN  ((int64_t)(-INT64_MAX - INT64_C(1)))
		#endif
		#if !defined(UINT64_MAX)
			#define UINT64_MAX UINT64_C(18446744073709551615)
		#endif
	#endif
	
	/** Standard argument parsing. */
	#include "stdarg.h"
#endif

/** Where code is placed. */
#ifndef sjme_addr_code
	#define sjme_addr_code
#endif

/** Where normal memory is used. */
#ifndef sjme_addr_mem
	#define sjme_addr_mem
#endif

/** Strings in the code area. */
#ifndef sjme_addr_codestr
	#define sjme_addr_codestr(v) v
#endif

/** Strings in the memory area. */
#ifndef sjme_addr_memstr
	#define sjme_addr_memstr(v) v
#endif

/** Do not modify variables. */
#ifndef sjme_const
	#define sjme_const const
#endif

/****************************************************************************/

/** End. */
#endif

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXSYS_STDCH
}
#undef SJME_cXSYS_STDCH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXSYS_STDCH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGSYS_STDCH */

