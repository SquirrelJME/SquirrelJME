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

/* The current operating system. */
#if defined(__3DS__) || defined(_3DS)
	/** Nintendo 3DS is available. */
	#define SJME_CONFIG_HAS_NINTENDO_3DS
#elif defined(__linux__) || defined(linux) || defined(__linux)
	/** Linux is available. */
	#define SJME_CONFIG_HAS_LINUX
#elif defined(__CYGWIN__)
	/** Cygwin is available. */
	#define SJME_CONFIG_HAS_CYGWIN
#elif defined(_WIN16) || defined(__WIN16__) || defined(__WIN16)
	/** Using Windows 16-bit. */
	#define SJME_CONFIG_HAS_WINDOWS_16

	/** Windows is available however. */
	#define SJME_CONFIG_HAS_WINDOWS 16
#elif defined(_WIN32) || defined(__WIN32__) || \
	defined(__WIN32) || defined(_WINDOWS)
	/** Using Windows 32-bit. */
	#define SJME_CONFIG_HAS_WINDOWS_32
	
	/** Windows is available. */
	#define SJME_CONFIG_HAS_WINDOWS 32
#elif defined(__APPLE__) && defined(__MACH__)
	/** macOS 10+ is available. */
	#define SJME_CONFIG_HAS_MACOS
#elif defined(macintosh)
	/** macOS Classic is available. */
	#define SJME_CONFIG_HAS_MACOS_CLASSIC
#elif defined(__palmos__)
	/** PalmOS is available. */
	#define SJME_CONFIG_HAS_PALMOS
#elif defined(__FreeBSD__) || defined(__NetBSD__) || \
	defined(__OpenBSD__) || defined(__bsdi__) || \
	defined(__DragonFly__) || defined(__MidnightBSD__)
	/** BSD is available. */
	#define SJME_CONFIG_HAS_BSD
#elif defined(__BEOS__) || defined(__HAIKU__)
	/** BeOS/Haiku is available. */
	#define SJME_CONFIG_HAS_BEOS
#endif

/** Possibly detect endianess. */
#if !defined(SJME_CONFIG_HAS_BIG_ENDIAN) && \
	!defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
	/** Defined by the system? */
	#if !defined(SJME_CONFIG_HAS_BIG_ENDIAN)
		#if defined(__BYTE_ORDER__) && \
			(__BYTE_ORDER__ == __ORDER_BIG_ENDIAN__)
			#define SJME_CONFIG_HAS_BIG_ENDIAN
		#endif
	#endif

	/** Just set little endian if no endianess was defined */
	#if !defined(SJME_CONFIG_HAS_BIG_ENDIAN) && \
		!defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
		#define SJME_CONFIG_HAS_LITTLE_ENDIAN
	#endif

	/** If both are defined, just set big endian. */
	#if defined(SJME_CONFIG_HAS_BIG_ENDIAN) && \
		defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
		#undef SJME_CONFIG_HAS_LITTLE_ENDIAN
	#endif
#endif

#if defined(__amd64__) || defined(__amd64__) || defined(__x86_64__) || \
	defined(__x86_64) || defined(_M_X64) || defined(_M_AMD64)
	/** Has AMD64. */
	#define SJME_CONFIG_HAS_ARCH_AMD64
#elif defined(_M_ARM64) || defined(_M_ARM64EC) || defined(__aarch64__)
	/** Has ARM64. */
	#define SJME_CONFIG_HAS_ARCH_ARM64 
#elif defined(__ia64__) || defined(_IA64) || \
	defined(__IA64__) || defined(__ia64) || defined(_M_IA64) || \
	defined(__itanium__)
	/** Has Itanium. */
	#define SJME_CONFIG_HAS_ARCH_IA64
#elif defined(__powerpc64__) || defined(__ppc64__) || defined(__PPC64__) || \
	defined(_ARCH_PPC64)
	/** Has PowerPC. */
	#define SJME_CONFIG_HAS_ARCH_POWERPC 32
	
	/** Has PowerPC 64-bit. */
	#define SJME_CONFIG_HAS_ARCH_POWERPC_64
#elif defined(_M_PPC) || defined(__powerpc) || defined(__powerpc__) || \
	defined(__POWERPC__) || defined(__ppc__) || \
	defined(__PPC__) || defined(_ARCH_PPC)
	/** Has PowerPC. */
	#define SJME_CONFIG_HAS_ARCH_POWERPC 32
	
	/** Has PowerPC 32-bit. */
	#define SJME_CONFIG_HAS_ARCH_POWERPC_32
#elif defined(_M_I86) || defined(_M_IX86) || defined(__X86__) || \
	defined(_X86_) || defined(__I86__) || defined(__i386) || \
	defined(__i386__) || defined(__i486__) || defined(__i586__) || \
	defined(__i686__)
	/** Has Intel 32-bit (x86). */
	#define SJME_CONFIG_HAS_ARCH_IA32
#endif

/* Attempt detection of pointer sizes based on architecture? */
#if (defined(__SIZEOF_POINTER__) && __SIZEOF_POINTER__ == 4) || \
	defined(_ILP32) || defined(__ILP32__)
	/** Pointer size. */
	#define SJME_CONFIG_HAS_POINTER 32
#elif (defined(__SIZEOF_POINTER__) && __SIZEOF_POINTER__ == 8) || \
	defined(_LP64) || defined(_LP64)
	/** Pointer size. */
	#define SJME_CONFIG_HAS_POINTER 64
#else
	/* 64-bit seeming architecture, common 64-bit ones? */
	#if defined(SJME_CONFIG_HAS_ARCH_AMD64) || \
        defined(SJME_CONFIG_HAS_ARCH_ARM64) || \
		defined(SJME_CONFIG_HAS_ARCH_IA64) || \
		defined(SJME_CONFIG_HAS_ARCH_POWERPC_64) || \
        defined(_WIN64)
		/** Pointer size. */
		#define SJME_CONFIG_HAS_POINTER 64
	#else
		/** Pointer size. */
		#define SJME_CONFIG_HAS_POINTER 32
	#endif
#endif

#if SJME_CONFIG_HAS_POINTER == 32
	/** Has 32-bit pointer. */
	#define SJME_CONFIG_HAS_POINTER32
#endif

#if SJME_CONFIG_HAS_POINTER == 64
	/** Has 64-bit pointer. */
	#define SJME_CONFIG_HAS_POINTER64
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

	/** Return value must be checked. */
	#define sjme_attrCheckReturn _Must_inspect_result_
	
	/** Deprecated. */
	#define sjme_attrDeprecated __declspec(deprecated)
	
	/** Formatted string argument. */
	#define sjme_attrFormatArg _Printf_format_string_ 
	
	/** Input cannot be null. */
	#define sjme_attrInNotNull _In_
	
	/** Input can be null. */
	#define sjme_attrInNullable _In_opt_
	
	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull _In_opt_ _Out_opt_
	
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
		/** Flexible array count, MSVC requires 1 because C2233. */
		#define sjme_flexibleArrayCount 1
	#endif
	
	#if !defined(sjme_flexibleArrayCountUnion)
		/** Flexible array count for union, MSVC requires 1 because C2233. */
		#define sjme_flexibleArrayCountUnion 1
	#endif

	/** Allocate on the stack. */
	#define sjme_alloca(size) _alloca((size))
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
#elif defined(__WATCOMC__)
	/** Flexible array size count. */
	#define sjme_flexibleArrayCount

	/** Flexible array count but for unions. */
	#define sjme_flexibleArrayCountUnion 1
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

#if !defined(sjme_attrInNotNullBuf)
	/** Input to buffer. */
	#define sjme_attrInNotNullBuf(lenArg) sjme_attrInNotNull
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

#if !defined(sjme_flexibleArrayCountUnion)
	/** Flexible array count but for unions. */
	#define sjme_flexibleArrayCountUnion 0
#endif

#if !defined(sjme_attrUnused)
	/** Unused value. */
	#define sjme_attrUnused
#endif

#if !defined(sjme_attrUnusedEnum)
	/** Unused enumeration element. */
	#define sjme_attrUnusedEnum(x) x
#endif

#if !defined(sjme_attrArtificial)
	/** Artificial function. */
	#define sjme_attrArtificial
#endif

#if !defined(sjme_alloca)
	/** Allocate on the stack. */
	#define sjme_alloca(size) alloca((size))
#endif

#if !defined(sjme_inline)
	/** Inline function. */
	#define sjme_inline inline
#endif

#if defined(__GNUC__)
	/** GNU C Compiler. */
	#define SJME_CONFIG_HAS_GCC
#endif

#if defined(SJME_CONFIG_HAS_WINDOWS)
	/** Supports Windows Atomic Access. */
	#define SJME_CONFIG_HAS_ATOMIC_WIN32
#elif defined(__STDC_VERSION__) && __STDC_VERSION__ >= 201112L && \
	!defined(__STDC_NO_ATOMICS__)
	/** Supports C11 atomics. */
	#define SJME_CONFIG_HAS_ATOMIC_C11
#elif defined(SJME_CONFIG_HAS_GCC)
	/** GCC Atomics. */
	#define SJME_CONFIG_HAS_ATOMIC_GCC
#endif

#if !defined(SJME_CONFIG_HAS_ATOMIC)
	#if defined(SJME_CONFIG_HAS_ATOMIC_WIN32) || \
		defined(SJME_CONFIG_HAS_ATOMIC_C11) || \
		defined(SJME_CONFIG_HAS_ATOMIC_GCC)
		/** Atomics are supported. */
		#define SJME_CONFIG_HAS_ATOMIC
	#else
		/** Use old atomic handling. */
		#define SJME_CONFIG_HAS_ATOMIC_OLD

		/** Atomics are supported. */
		#define SJME_CONFIG_HAS_ATOMIC
	#endif
#endif

#if defined(SJME_CONFIG_HAS_LINUX) || \
	defined(SJME_CONFIG_HAS_BSD) || \
    defined(SJME_CONFIG_HAS_BEOS)
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) \
		"lib" x ".so"
#elif defined(SJME_CONFIG_HAS_CYGWIN)
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) \
		"lib" x ".dll"
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) \
		"" x ".dll"
#elif defined(SJME_CONFIG_HAS_MACOS)
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) \
		"lib" x ".dylib"
#else
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) NULL
#endif

/* Nintendo 3DS: devkitPro has broken/unimplemented pthreads. */
#if defined(SJME_CONFIG_HAS_NINTENDO_3DS)
	/** Use fallback threading regardless of the system. */
	#define SJME_CONFIG_HAS_THREADS_FALLBACK
#endif

#if defined(SJME_CONFIG_HAS_WINDOWS_16)
	#define SJME_CALL FAR PASCAL
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	#define SJME_CALL __stdcall 
#else
	/** SquirrelJME calling convention. */
	#define SJME_CALL
#endif

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
