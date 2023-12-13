/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Basic configuration header.
 * 
 * @since 2023/07/27
 */

#ifndef SQUIRRELJME_CONFIG_H
#define SQUIRRELJME_CONFIG_H

#include <stddef.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CONFIG_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if !defined(SJME_CONFIG_RELEASE) && !defined(SJME_CONFIG_DEBUG)
	#if (defined(DEBUG) || defined(_DEBUG)) || \
		(!defined(NDEBUG) && !defined(_NDEBUG))
		/** Debug build. */
		#define SJME_CONFIG_DEBUG
	#else
		/** Release build. */
		#define SJME_CONFIG_RELEASE
	#endif
#elif defined(SJME_CONFIG_RELEASE) && defined(SJME_CONFIG_DEBUG)
	#undef SJME_CONFIG_RELEASE
#endif

#if defined(SJME_CONFIG_ROM0)
	/** ROM 0 Address. */
	#define SJME_CONFIG_ROM0_ADDR &SJME_CONFIG_ROM0
#elif !defined(SJME_CONFIG_ROM0_ADDR)
	/** ROM 0 Address. */
	#define SJME_CONFIG_ROM0_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM1)
	/** ROM 1 Address. */
	#define SJME_CONFIG_ROM1_ADDR &SJME_CONFIG_ROM1
#elif !defined(SJME_CONFIG_ROM1_ADDR)
	/** ROM 1 Address. */
	#define SJME_CONFIG_ROM1_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM2)
	/** ROM 2 Address. */
	#define SJME_CONFIG_ROM2_ADDR &SJME_CONFIG_ROM2
#elif !defined(SJME_CONFIG_ROM2_ADDR)
	/** ROM 2 Address. */
	#define SJME_CONFIG_ROM2_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM3)
	/** ROM 3 Address. */
	#define SJME_CONFIG_ROM3_ADDR &SJME_CONFIG_ROM3
#elif !defined(SJME_CONFIG_ROM3_ADDR)
	/** ROM 3 Address. */
	#define SJME_CONFIG_ROM3_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM4)
	/** ROM 4 Address. */
	#define SJME_CONFIG_ROM4_ADDR &SJME_CONFIG_ROM4
#elif !defined(SJME_CONFIG_ROM4_ADDR)
	/** ROM 4 Address. */
	#define SJME_CONFIG_ROM4_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM5)
	/** ROM 5 Address. */
	#define SJME_CONFIG_ROM5_ADDR &SJME_CONFIG_ROM5
#elif !defined(SJME_CONFIG_ROM5_ADDR)
	/** ROM 5 Address. */
	#define SJME_CONFIG_ROM5_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM6)
	/** ROM 6 Address. */
	#define SJME_CONFIG_ROM6_ADDR &SJME_CONFIG_ROM6
#elif !defined(SJME_CONFIG_ROM6_ADDR)
	/** ROM 6 Address. */
	#define SJME_CONFIG_ROM6_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM7)
	/** ROM 7 Address. */
	#define SJME_CONFIG_ROM7_ADDR &SJME_CONFIG_ROM7
#elif !defined(SJME_CONFIG_ROM7_ADDR)
	/** ROM 7 Address. */
	#define SJME_CONFIG_ROM7_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM8)
	/** ROM 8 Address. */
	#define SJME_CONFIG_ROM8_ADDR &SJME_CONFIG_ROM8
#elif !defined(SJME_CONFIG_ROM8_ADDR)
	/** ROM 8 Address. */
	#define SJME_CONFIG_ROM8_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM9)
	/** ROM 9 Address. */
	#define SJME_CONFIG_ROM9_ADDR &SJME_CONFIG_ROM9
#elif !defined(SJME_CONFIG_ROM9_ADDR)
	/** ROM 9 Address. */
	#define SJME_CONFIG_ROM9_ADDR NULL
#endif

/* C99 supports flexible array members. */
#if defined(__STDC_VERSION__) && __STDC_VERSION__ >= 199901L
	/** Flexible array members. */
	#define sjme_flexibleArrayCount
#endif

/* Visual C++. */
#if defined(_MSC_VER)
	#include <sal.h>

	/** Allocator function. */
	#define sjme_attrAllocator __declspec(allocator)
	
	/** Return value must be checked. */
	#define sjme_attrCheckReturn _Must_inspect_result_
	
	/** Deprecated. */
	#define sjme_attrDeprecated __declspec(deprecated)
	
	/** Formatted string argument. */
	#define sjme_attrFormatArg _Printf_format_string_ 
	
	/** Input cannot be null. */
	#define sjme_attrInNotNull _In_opt_
	
	/** Input can be null. */
	#define sjme_attrInNullable _Nullable
	
	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull _InOut_
	
	/** Input value range. */
	#define sjme_attrInRange(lo, hi) _In_range_((lo), (hi))
	
	/** Method takes input. */
	#define sjme_attrInValue _In_
	
	/** Does not return. */
	#define sjme_attrReturnNever _Always_(_Raises_SEH_exception_)
	
	/** Returns nullable value. */
	#define sjme_attrReturnNullable _Outptr_result_maybenull_z_
	
	/** Method gives output. */
	#define sjme_attrOutNotNull _Out_
	
	/** Method output can be null. */
	#define sjme_attrOutNullable _Out_opt_

	/** Output to buffer. */
	#define sjme_attrOutNotNullBuf(lenArg) _Out_writes_(lenArg)
	
	/** Output value range. */
	#define sjme_attrOutRange(lo, hi) _Out_range_((lo), (hi))

	#if !defined(sjme_flexibleArrayCount)
		/** Flexible array count, MSVC assumes blank. */
		#define sjme_flexibleArrayCount
	#endif
#elif defined(__clang__) || defined(__GNUC__)
	/* Clang has special analyzer stuff, but also same as GCC otherwise. */
	#if defined(__clang__)
		#if __has_feature(attribute_analyzer_noreturn)
			/** Method does not return. */
			#define sjme_attrReturnNever __attribute__((analyzer_noreturn))
		#endif

		/** Returns nullable value. */
		#define sjme_attrReturnNullable _Nullable_result
	#endif
	
	#if !defined(sjme_attrReturnNever)
		#if defined(__builtin_unreachable)
			/** Function never returns. */
			#define sjme_attrReturnNever (__builtin_unreachable())
		#endif
	#endif

	/** Artificial function. */
	#define sjme_attrArtificial __attribute__((artificial))
	
	/** Check return value. */
	#define sjme_attrCheckReturn __attribute__((warn_unused_result))
	
	/** Deprecated. */
	#define sjme_attrDeprecated __attribute__((deprecated))
	
	/**
	 * Formatted string.
	 * 
	 * @param formatIndex The formatted string index.
	 * @param vaIndex The index of @c ... or @c va_list .
	 * @since 2023/08/05
	 */
	#define sjme_attrFormatOuter(formatIndex, vaIndex) \
		__attribute__((format(__printf__, formatIndex + 1, vaIndex + 1)))
	
	/** Indicates a callback. */
	#define sjme_attrCallback __attribute__((callback))
	
	/** Not used. */
	#define sjme_attrUnused __attribute__((unused))

	/** Not used enum constant. */
	#define sjme_attrUnusedEnum(x) x sjme_attrUnused
	
	#if !defined(sjme_attrReturnNever)
		/** Method does not return. */
		#define sjme_attrReturnNever __attribute__((noreturn))
	#endif

	#if !defined(sjme_flexibleArrayCount)
		/** Flexible array size count, GCC is fine with blank. */
		#define sjme_flexibleArrayCount
	#endif
#endif

#if !defined(sjme_attrCallback)
	/** Indicates a callback. */
	#define sjme_attrCallback
#endif

#if !defined(sjme_attrCheckReturn)
	/** Return value must be checked. */
	#define sjme_attrCheckReturn
#endif

#if !defined(sjme_attrDeprecated)
	/** Deprecated. */
	#define sjme_attrDeprecated
#endif

#if !defined(sjme_attrFormatArg)
	/** Formatted string argument. */
	#define sjme_attrFormatArg
#endif

#if !defined(sjme_attrFormatOuter)
	/**
	 * Formatted string.
	 * 
	 * @param formatIndex The formatted string index.
	 * The index of @c ... or @c va_list .
	 * @since 2023/08/05
	 */
	#define sjme_attrFormatOuter(formatIndex, vaIndex)
#endif

#if !defined(sjme_attrInValue)
	/** Method takes input. */
	#define sjme_attrInValue
#endif

#if !defined(sjme_attrInRange)
	/** Input value range. */
	#define sjme_attrInRange(lo, hi)
#endif

#if !defined(sjme_attrReturnNever)
	/** Method does not return. */
	#define sjme_attrReturnNever
#endif

#if !defined(sjme_attrReturnNullable)
	/** Returns a nullable value. */
	#define sjme_attrReturnNullable
#endif

#if !defined(sjme_attrInValue)
	/** Takes input value. */
	#define sjme_attrInValue
#endif

#if !defined(sjme_attrInNotNull)
	/** Cannot be null. */
	#define sjme_attrInNotNull sjme_attrInValue
#endif

#if !defined(sjme_attrInNullable)
	/** Nullable. */
	#define sjme_attrInNullable sjme_attrInValue
#endif

#if !defined(sjme_attrOutNotNull)
	/** Method gives output. */
	#define sjme_attrOutNotNull sjme_attrInNotNull
#endif

#if !defined(sjme_attrOutNullable)
	/** Method output can be null. */
	#define sjme_attrOutNullable sjme_attrInNullable
#endif

#if !defined(sjme_attrInOutNotNull)
	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull sjme_attrInNotNull sjme_attrOutNotNull 
#endif

#if !defined(sjme_attrOutNotNullBuf)
	/** Output to buffer. */
	#define sjme_attrOutNotNullBuf(lenArg) sjme_attrOutNotNull
#endif

#if !defined(sjme_attrOutRange)
	/** Output value range. */
	#define sjme_attrOutRange(lo, hi)
#endif

/** Positive value. */
#define sjme_attrInPositive sjme_attrInRange(0, INT32_MAX)

/** Non-zero positive value. */
#define sjme_attrInPositiveNonZero sjme_attrInRange(1, INT32_MAX)

/** Negative one to positive. */
#define sjme_attrInNegativeOnePositive sjme_attrInRange(-1, INT32_MAX)

/** Positive value. */
#define sjme_attrOutPositive sjme_attrOutRange(0, INT32_MAX)

/** Non-zero positive value. */
#define sjme_attrOutPositiveNonZero sjme_attrOutRange(1, INT32_MAX)

/** Negative one to positive. */
#define sjme_attrOutNegativeOnePositive sjme_attrOutRange(-1, INT32_MAX)

#if !defined(sjme_flexibleArrayCount)
	/** Flexible array count, zero by default. */
	#define sjme_flexibleArrayCount 0
#endif

#if !defined(sjme_attrUnused)
	/** Unused value. */
	#define sjme_attrUnused
#endif

#if !defined(sjme_attrAllocator)
	/** Allocator function. */
	#define sjme_attrAllocator
#endif

#if !defined(sjme_attrUnusedEnum)
	/** Unused enumeration element. */
	#define sjme_attrUnusedEnum(x) x
#endif

#if !defined(sjme_attrArtificial)
	/** Artificial function. */
	#define sjme_attrArtificial
#endif

/** Flexible array count but for unions. */
#define sjme_flexibleArrayCountUnion 0

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CONFIG_H
}
		#undef SJME_CXX_SQUIRRELJME_CONFIG_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CONFIG_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CONFIG_H */
