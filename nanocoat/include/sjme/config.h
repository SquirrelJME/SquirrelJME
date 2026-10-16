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
 * The majority of this header is to simplify the system specific macros
 * and definitions and unify them so they are far easier to use.
 *
 * @file
 * @since 2023/07/27
 */

#ifndef SJME_C_CONFIG_H
#define SJME_C_CONFIG_H

/* Use a configure file? */
#if defined(SJME_CONFIG_USE_PATH)
	/* set() is used by CMake. */
	#define set(x)

	/* Include the configuration. */
	#include SJME_CONFIG_USE_PATH

	/* Revert use of set. */
	#undef set
#endif

#include <stddef.h>

/* Skip stdlib in certain cases? */
#if !defined(SJME_CONFIG_FORGET_STDLIB)
	#include <stdlib.h>
#endif

#include <setjmp.h>

/* Floating point header, determines if software floats should be used. */
#if defined(SJME_CONFIG_HAS_FLOAT_H)
	#include <float.h>
#endif

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CONFIG_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */
	
/*--------------------------------------------------------------------------*/

#if defined(__STDC__)
	/** Has C89 Support. */
	#define SJME_CONFIG_HAS_C89
	
	#if defined(__STDC_VERSION__)
		#if __STDC_VERSION__ >= 202311L
			/** Has C23. */
			#define SJME_CONFIG_HAS_C23
		#endif
		
		#if __STDC_VERSION__ >= 201710L
			/** Has C17. */
			#define SJME_CONFIG_HAS_C17
		#endif
		
		#if __STDC_VERSION__ >= 201112L
			/** Has C11 support. */
			#define SJME_CONFIG_HAS_C11
		#endif
		
		#if __STDC_VERSION__ >= 199901L
			/** Has C99 support. */
			#define SJME_CONFIG_HAS_C99
		#endif
		
		#if __STDC_VERSION__ >= 199409L
			/** Has C94 support. */
			#define SJME_CONFIG_HAS_C94
		#endif
	#endif
#elif defined(_MSC_VER)
	/** MSVC is assumed to support C89. */
	#define SJME_CONFIG_HAS_C89
#endif

#if defined(SJME_CONFIG_HAS_C11)
	/** Has this specific C version. */
	#define SJME_CONFIG_HAS_C 2011
#elif defined(SJME_CONFIG_HAS_C99)
	/** Has this specific C version. */
	#define SJME_CONFIG_HAS_C 1999
#elif defined(SJME_CONFIG_HAS_C94)
	/** Has this specific C version. */
	#define SJME_CONFIG_HAS_C 1994
#elif defined(SJME_CONFIG_HAS_C89)
	/** Has this specific C version. */
	#define SJME_CONFIG_HAS_C 1989
#endif
	
/** Visual Studio 6 */
#define SJME_CONFIG_MSVC_VERSION_6 1200

/** Visual Studio 2005 */
#define SJME_CONFIG_MSVC_VERSION_2005 1400

/** Visual Studio 2010 */
#define SJME_CONFIG_MSVC_VERSION_2010 1600

/** Visual Studio 2019 */
#define SJME_CONFIG_MSVC_VERSION_2019 1920

#if defined(__WATCOMC__)
	/** Watcom C Compiler. */
	#define SJME_CONFIG_HAS_WATCOM
	
	/** Compiler ID. */
	#define SJME_CONFIG_HAS_COMPILER "watcom"
#endif
	
#if !defined(SJME_CONFIG_HAS_COMPILER) && \
	defined(SDCC) || defined(__SDCC)
	/** SDCC. */
	#define SJME_CONFIG_HAS_SDCC
	
	/** Compiler ID. */
	#define SJME_CONFIG_HAS_COMPILER "sdcc"
#endif
	
#if !defined(SJME_CONFIG_HAS_COMPILER) && \
	defined(__clang__)
	/** CLang LLVM Compiler. */
	#define SJME_CONFIG_HAS_CLANG
	
	/** Compiler ID. */
	#define SJME_CONFIG_HAS_COMPILER "clang"
	
	/** Is the CLang version the specified version? */
	#define SJME_CONFIG_CLANG_VERSION_LEAST(major, minor) \
		(__clang_major__ > major ? 1 : \
		(__clang_major__ < major ? 0 : \
		(__clang_minor__ >= minor)))
#else
	/** Is the CLang version the specified version? */
	#define SJME_CONFIG_CLANG_VERSION_LEAST(major, minor) 0
#endif
	
/* Clang likes to lie about being MSVC. */
#if !defined(SJME_CONFIG_HAS_COMPILER) && \
	defined(_MSC_VER) && !defined(SJME_CONFIG_HAS_CLANG)
	/** Microsoft Visual C++ Compiler. */
	#define SJME_CONFIG_HAS_MSVC
	
	/** Compiler ID. */
	#define SJME_CONFIG_HAS_COMPILER "msvc"
	
	/** Is the MSVC version the specified version? */
	#define SJME_CONFIG_MSVC_VERSION_LEAST(against) \
		(_MSC_VER >= against)
#else
	/** Is the MSVC version the specified version? */
	#define SJME_CONFIG_MSVC_VERSION_LEAST(against) 0
#endif

/* Clang likes to lie about being GCC. */
#if !defined(SJME_CONFIG_HAS_COMPILER) && \
	defined(__GNUC__) && !defined(SJME_CONFIG_HAS_CLANG)
	/** GNU C Compiler. */
	#define SJME_CONFIG_HAS_GCC
	
	/** Compiler ID. */
	#define SJME_CONFIG_HAS_COMPILER "gcc"

	#if defined(__GNUC_MINOR__)
		/** Is the GCC version the specified version? */
		#define SJME_CONFIG_GCC_VERSION_LEAST(major, minor) \
			(__GNUC__ > major ? 1 : (__GNUC__ < major ? 0 : \
			(__GNUC_MINOR__ >= minor)))
	#else
		/** Is the GCC version the specified version? */
		#define SJME_CONFIG_GCC_VERSION_LEAST(major, minor) \
			(__GNUC__ > major ? 1 : (__GNUC__ < major ? 0 : \
			(0 >= minor)))
	#endif
#else
	/** Is the GCC version the specified version? */
	#define SJME_CONFIG_GCC_VERSION_LEAST(major, minor) 0
#endif
	
/* Fallback if there is no known compiler. */
#if !defined(SJME_CONFIG_HAS_COMPILER)
	/** Unknown compiler identifier. */
	#define SJME_CONFIG_HAS_COMPILER "unknown"
#endif
	
#if defined(SJME_CONFIG_HAS_CLANG) || defined(SJME_CONFIG_HAS_GCC)
	/** Has a GCC-like/clone compiler. */
	#define SJME_CONFIG_HAS_GCC_CLONE
#endif

#if !defined(SJME_CONFIG_RELEASE) && !defined(SJME_CONFIG_DEBUG)
	#if defined(SJME_CONFIG_HAS_SDCC)
		/** Force SDCC to be release. */
		#define SJME_CONFIG_RELEASE
	#elif (defined(DEBUG) || defined(_DEBUG)) || \
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
#if defined(SJME_CONFIG_IDENT_OS_SDCC) || defined(SJME_CONFIG_HAS_SDCC) || \
	defined(SJME_CONFIG_IDENT_OS_BIOS) || \
	defined(SJME_CONFIG_IDENT_OS_EFI) || \
	defined(SJME_CONFIG_IDENT_OS_IEEE1275OF)
	/** There is no operating system. */
	#define SJME_CONFIG_HAS_OS_BAREMETAL
#elif defined(SJME_CONFIG_IDENT_OS_WINE) || defined(__WINE__)
	/** Windows through Wine. */
	#define SJME_CONFIG_HAS_OS_WINDOWS_WINE
	
	/** Using Windows 32-bit (via Wine). */
	#define SJME_CONFIG_HAS_OS_WINDOWS_32
		
		/** Windows is available (via Wine). */
	#define SJME_CONFIG_HAS_OS_WINDOWS 32
#elif defined(__EMSCRIPTEN__) || defined(EMSCRIPTEN)
	/** Emscripten (WASM). */
	#define SJME_CONFIG_HAS_OS_EMSCRIPTEN
#elif defined(SJME_CONFIG_IDENT_OS_WIIU) || \
	(defined(GEKKO) && defined(HW_RVL) && defined(WIIU))
	/** Nintendo Wii U is available. */
	#define SJME_CONFIG_HAS_OS_NINTENDO_WIIU
#elif defined(SJME_CONFIG_IDENT_OS_WII) || \
	(defined(GEKKO) && defined(HW_RVL) && !defined(WIIU))
	/** Nintendo Wii is available. */
	#define SJME_CONFIG_HAS_OS_NINTENDO_WII
#elif defined(SJME_CONFIG_IDENT_OS_GAMECUBE) || \
	(defined(GEKKO) && defined(HW_DOL))
	/** Nintendo GameCube is available. */
	#define SJME_CONFIG_HAS_OS_NINTENDO_GAMECUBE
#elif defined(__3DS__) || defined(_3DS) || defined(SJME_CONFIG_IDENT_OS_3DS)
	/** Nintendo 3DS is available. */
	#define SJME_CONFIG_HAS_OS_NINTENDO_3DS
#elif defined(PSP) || defined(SJME_CONFIG_IDENT_OS_PSP)
	/** Sony PSP. */
	#define SJME_CONFIG_HAS_OS_SONY_PSP
#elif defined(PS2) || defined(_EE) || defined(_IOP) || defined(__PS2__) || \
	defined(SJME_CONFIG_IDENT_OS_PLAYSTATION2)
	/** Sony PlayStation 2. */
	#define SJME_CONFIG_HAS_OS_SONY_PS2
#elif defined(__ANDROID__) || defined(__ANDROID_API__) || \
	defined(SJME_CONFIG_IDENT_OS_ANDROID)
	/** Android is available. */
	#define SJME_CONFIG_HAS_OS_ANDROID
#elif defined(__linux__) || defined(linux) || defined(__linux)
	/** Linux is available. */
	#define SJME_CONFIG_HAS_OS_LINUX
#elif defined(__CYGWIN__)
	/** Cygwin is available. */
	#define SJME_CONFIG_HAS_OS_CYGWIN
#elif defined(_WIN16) || defined(__WIN16__) || defined(__WIN16)
	/** Using Windows 16-bit. */
	#define SJME_CONFIG_HAS_OS_WINDOWS_16

	/** Windows is available however. */
	#define SJME_CONFIG_HAS_OS_WINDOWS 16
#elif defined(_WIN32) || defined(__WIN32__) || \
	defined(__WIN32) || defined(_WINDOWS) || \
	defined(SJME_CONFIG_IDENT_OS_REACTOS)
	/** Using Windows 32-bit. */
	#define SJME_CONFIG_HAS_OS_WINDOWS_32
	
	/** Windows is available. */
	#define SJME_CONFIG_HAS_OS_WINDOWS 32
#elif (defined(__APPLE__) && defined(__MACH__)) || \
	defined(SJME_CONFIG_IDENT_OS_MACOSX)
	/** macOS 10+ is available. */
	#define SJME_CONFIG_HAS_OS_MACOS
#elif defined(macintosh)
	/** macOS Classic is available. */
	#define SJME_CONFIG_HAS_OS_MACOS_CLASSIC
#elif defined(__palmos__)
	/** PalmOS is available. */
	#define SJME_CONFIG_HAS_OS_PALMOS
#elif defined(__SYMBIAN32__)
	/** Is Symbian. */
	#define SJME_CONFIG_HAS_OS_SYMBIAN
#elif defined(__sun) || defined(__illumos__)
	#if defined(__illumos__)
		/** Is Illumos. */
		#define SJME_CONFIG_HAS_OS_ILLUMOS
	#endif
	
	/** Is Solaris. */
	#define SJME_CONFIG_HAS_OS_SOLARIS
#elif defined(NeXT)
	/** Is NeXTStep. */
	#define SJME_CONFIG_HAS_OS_NEXTSTEP
#elif defined(__FreeBSD__) || defined(__NetBSD__) || \
	defined(__OpenBSD__) || defined(__bsdi__) || \
	defined(__DragonFly__) || defined(__MidnightBSD__)
	/** BSD is available. */
	#define SJME_CONFIG_HAS_OS_BSD
#elif defined(__BEOS__) || defined(__HAIKU__)
	/** BeOS/Haiku is available. */
	#define SJME_CONFIG_HAS_OS_BEOS
#elif defined(MSDOS) || defined(__MSDOS__) || defined(_MSDOS) || \
	defined(__DOS__)
	/** Is Microsoft Dos. */
	#define SJME_CONFIG_HAS_OS_PC_DOS
#elif defined(SJME_CONFIG_IDENT_OS_SUNOS)
	/** Classic BSD based SunOS. */
	#define SJME_CONFIG_HAS_OS_SUNOS
#endif
	
/** POSIX 1990. */
#define SJME_CONFIG_POSIX_VERSION_1990 1

/** POSIX 1992. */
#define SJME_CONFIG_POSIX_VERSION_1992 2

/** POSIX 1993. */
#define SJME_CONFIG_POSIX_VERSION_1993 199309L

/** POSIX 1995. */
#define SJME_CONFIG_POSIX_VERSION_1995 199506L

/** POSIX 2001. */
#define SJME_CONFIG_POSIX_VERSION_2001 200112L

/** POSIX 2008. */
#define SJME_CONFIG_POSIX_VERSION_2008 200809L

#if defined(_POSIX_C_SOURCE)
	#if !defined(SJME_CONFIG_HAS_OS_WINDOWS_WINE)
		/** POSIX is available. */
		#define SJME_CONFIG_HAS_OS_POSIX
	#endif
#else
	/* These OSes have POSIX. */
	#if defined(SJME_CONFIG_HAS_OS_ANDROID) || \
		defined(SJME_CONFIG_HAS_OS_BSD) || \
		defined(SJME_CONFIG_HAS_OS_CYGWIN) || \
		defined(SJME_CONFIG_HAS_OS_LINUX) || \
		defined(SJME_CONFIG_HAS_OS_MACOS) || \
		defined(SJME_CONFIG_HAS_OS_NEXTSTEP) || \
		defined(SJME_CONFIG_HAS_OS_SOLARIS)
		/** POSIX is available. */
		#define SJME_CONFIG_HAS_OS_POSIX
	#endif
#endif
	
#if defined(SJME_CONFIG_HAS_OS_BSD) || defined(SJME_CONFIG_HAS_OS_MACOS) || \
	defined(SJME_CONFIG_HAS_OS_NEXTSTEP) || defined(SJME_CONFIG_HAS_OS_SUNOS)
	/** This is a BSD family operating system. */
	#define SJME_CONFIG_HAS_OS_BSD_FAMILY
#endif

#if defined(SJME_CONFIG_HAS_OS_POSIX)
	#if defined(_POSIX_C_SOURCE)
		/** POSIX version is at least the given version. */
		#define SJME_CONFIG_POSIX_VERSION_LEAST(posixVer) \
			(_POSIX_C_SOURCE > SJME_CONFIG_POSIX_VERSION_1990)
	#else
			/** POSIX version is at least the given version. */
		#define SJME_CONFIG_POSIX_VERSION_LEAST(posixVer) \
			(posixVer > SJME_CONFIG_POSIX_VERSION_1990)
	#endif
#else
	/** POSIX version is at least the given version. */
	#define SJME_CONFIG_POSIX_VERSION_LEAST(posixVer) 0
#endif

/** Windows 8. */
#define SJME_CONFIG_WINDOWS_VERSION_8 0x0600

/** Windows XP */
#define SJME_CONFIG_WINDOWS_VERSION_XP 0x0501

/** Windows Vista. */
#define SJME_CONFIG_WINDOWS_VERSION_VISTA 0x0600

/** Windows NT 4.0 */
#define SJME_CONFIG_WINDOWS_VERSION_NT_4 0x0400

#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_WINE)
	/* Include the Windows SDK versioning information, if available. */
	#if defined(SJME_CONFIG_HAS_SDKDDKVER_H)
		#include <sdkddkver.h>
	#endif

	/** Windows version is at least the given version. */
	#define SJME_CONFIG_WINDOWS_VERSION_LEAST(winVer) (WINVER >= winVer)

	#if SJME_CONFIG_WINDOWS_VERSION_LEAST(SJME_CONFIG_WINDOWS_VERSION_VISTA)
		/** Windows NT version is at least the given version. */
		#define SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(winVer) \
			SJME_CONFIG_WINDOWS_VERSION_LEAST(winVer)
	#elif defined(_WIN32_WINNT)
		/** Windows NT version is at least the given version. */
		#define SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(winVer) \
			(_WIN32_WINNT >= winVer)
	#else
		/** Windows NT version is at least the given version. */
		#define SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(winVer) 0
	#endif
#else
	/** Windows version is at least the given version. */
	#define SJME_CONFIG_WINDOWS_VERSION_LEAST(winVer) 0

	/** Windows NT version is at least the given version. */
	#define SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(winVer) 0
#endif
	
/** Possibly detect endianess. */
#if !defined(SJME_CONFIG_HAS_BIG_ENDIAN) && \
	!defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
	/** Defined by the system? */
	#if !defined(SJME_CONFIG_HAS_BIG_ENDIAN) && !defined(__LITTLE_ENDIAN__)
		#if defined(__BYTE_ORDER__) && \
			(__BYTE_ORDER__ == __ORDER_BIG_ENDIAN__)
			/** The system is big endian. */ 
			#define SJME_CONFIG_HAS_BIG_ENDIAN
		#elif defined(__BIG_ENDIAN__)
			/** The system is big endian. */
			#define SJME_CONFIG_HAS_BIG_ENDIAN
		#endif
	#endif

	/** Just set little endian if no endianess was defined */
	#if !defined(SJME_CONFIG_HAS_BIG_ENDIAN) && \
		!defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
			/** The system is little endian. */
		#define SJME_CONFIG_HAS_LITTLE_ENDIAN
	#endif

	/** If both are defined, just set big endian. */
	#if defined(SJME_CONFIG_HAS_BIG_ENDIAN) && \
		defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
		#undef SJME_CONFIG_HAS_LITTLE_ENDIAN
	#endif
#endif

#if defined(_ILP32) || defined(__ILP32__)
	/** Has known long-pointer 32-bit model. */
	#define SJME_CONFIG_HAS_ILP32
	
#elif defined(_LP64) || defined(__LP64__)
	/** Has known long-pointer 64-bit model. */
	#define SJME_CONFIG_HAS_LP64
	
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
#elif defined(__mips__) || defined(__mips) || defined(__MIPS__)
	#if defined(SJME_CONFIG_HAS_LP64)
		/** Has MIPS. */
		#define SJME_CONFIG_HAS_ARCH_MIPS 64
		
		/** Has MIPS 64-bit. */
		#define SJME_CONFIG_HAS_ARCH_MIPS_64
	#else
		/** Has MIPS. */
		#define SJME_CONFIG_HAS_ARCH_MIPS 32
		
		/** Has MIPS 32-bit. */
		#define SJME_CONFIG_HAS_ARCH_MIPS_32
	#endif
#elif defined(_M_I86) || defined(_M_IX86) || defined(__X86__) || \
	defined(_X86_) || defined(__I86__) || defined(__i386) || \
	defined(__i386__) || defined(__i486__) || defined(__i586__) || \
	defined(__i686__)
	/** Has Intel 32-bit (x86). */
	#define SJME_CONFIG_HAS_ARCH_IA32
#endif

/* Attempt detection of pointer sizes based on architecture? */
#if (defined(__SIZEOF_POINTER__) && __SIZEOF_POINTER__ == 4) || \
	defined(SJME_CONFIG_HAS_ILP32)
	/** Pointer size. */
	#define SJME_CONFIG_HAS_POINTER 32
#elif (defined(__SIZEOF_POINTER__) && __SIZEOF_POINTER__ == 8) || \
	defined(SJME_CONFIG_HAS_LP64)
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

	/** Bytes per pointer. */
	#define SJME_POINTER_BYTES 4
#endif

#if SJME_CONFIG_HAS_POINTER == 64
	/** Has 64-bit pointer. */
	#define SJME_CONFIG_HAS_POINTER64

	/** Bytes per pointer. */
	#define SJME_POINTER_BYTES 8
#endif

#if !defined(SJME_POINTER_BYTES)
	/** Bytes per pointer. */
	#define SJME_POINTER_BYTES (SJME_CONFIG_HAS_POINTER / 8)
#endif
	
#if !defined(SJME_CONFIG_HAS_STDINT)
	#if SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010) || \
		defined(SJME_CONFIG_HAS_GCC) || \
		defined(SJME_CONFIG_HAS_CLANG) || \
		defined(SJME_CONFIG_HAS_C99)
		#define SJME_CONFIG_HAS_STDINT
	#endif
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
#if defined(SJME_CONFIG_HAS_C99)
	/** Flexible array members. */
	#define sjme_flexibleArrayCount
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Flexible array count, MSVC requires 1 because C2233. */
	#define sjme_flexibleArrayCount 1
#elif defined(SJME_CONFIG_HAS_CLANG) || defined(SJME_CONFIG_HAS_GCC)
	/** Flexible array size count, GCC is fine with blank. */
	#define sjme_flexibleArrayCount
#elif defined(SJME_CONFIG_HAS_WATCOM)
	/** Flexible array size count. */
	#define sjme_flexibleArrayCount
#endif
	
/* SDCC does not have normal alloca. */
#if !defined(sjme_alloca) && defined(SJME_CONFIG_HAS_SDCC)
	/** Allocate on the heap. */
	#define sjme_alloca(size) ((void*)malloc((size)))

	/** Free heap allocated data. */
	#define sjme_alloca_free(ptr) ((void)free(ptr))
#endif

/* Visual C++ has a differently named alloca. */
#if !defined(sjme_alloca) && defined(SJME_CONFIG_HAS_MSVC)
	/** Allocate on the stack. */
	#define sjme_alloca(size) _alloca((size))

	/** Free stack allocated data. */
	#define sjme_alloca_free(ptr) ((void)ptr)
#endif

#if !defined(sjme_alloca)
	/** Allocate on the stack. */
	#define sjme_alloca(size) alloca((size))

	/** Free stack allocated data. */
	#define sjme_alloca_free(ptr) ((void)ptr)
#endif

#if defined(SJME_CONFIG_HAS_OS_MACOS) && \
	(defined(SJME_CONFIG_HAS_ARCH_IA32) || \
	defined(SJME_CONFIG_HAS_ARCH_POWERPC))
	/** Supports macOS Darwin kernel Atomic Access */
	#define SJME_CONFIG_HAS_ATOMIC_DARWIN
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	/** Supports Windows Atomic Access. */
	#define SJME_CONFIG_HAS_ATOMIC_WIN32
#elif defined(SJME_CONFIG_HAS_OS_SONY_PS2)
	/** Use volatile atomics. */
	#define SJME_CONFIG_HAS_ATOMIC_VOLATILE
#elif defined(SJME_CONFIG_HAS_C11) && !defined(__STDC_NO_ATOMICS__)
	/** Supports C11 atomics. */
	#define SJME_CONFIG_HAS_ATOMIC_C11
#elif defined(SJME_CONFIG_HAS_CLANG)
	/** CLang is based off GCC's newer __atomic family. */
	#define SJME_CONFIG_HAS_ATOMIC_GCC
#elif defined(SJME_CONFIG_HAS_GCC)
	/* GCC 4.7 introduces the __atomic family. */
	#if SJME_CONFIG_GCC_VERSION_LEAST(4, 7)
		/** GCC Atomics. */
		#define SJME_CONFIG_HAS_ATOMIC_GCC
	#else
		/** GCC Legacy Atomics. */
		#define SJME_CONFIG_HAS_ATOMIC_GCC_LEGACY
	#endif
#endif

#if !defined(SJME_CONFIG_HAS_ATOMIC)
	#if defined(SJME_CONFIG_HAS_ATOMIC_C11) || \
		defined(SJME_CONFIG_HAS_ATOMIC_DARWIN) || \
		defined(SJME_CONFIG_HAS_ATOMIC_GCC) || \
		defined(SJME_CONFIG_HAS_ATOMIC_GCC_LEGACY) || \
		defined(SJME_CONFIG_HAS_ATOMIC_WIN32) || \
		defined(SJME_CONFIG_HAS_ATOMIC_VOLATILE)
		/** Atomics are supported. */
		#define SJME_CONFIG_HAS_ATOMIC
	#else
		/** Use old atomic handling. */
		#define SJME_CONFIG_HAS_ATOMIC_VOLATILE
		
		/** Atomics are supported. */
		#define SJME_CONFIG_HAS_ATOMIC
	#endif
#endif

#if defined(SJME_CONFIG_HAS_OS_LINUX) || \
	defined(SJME_CONFIG_HAS_OS_BSD) || \
    defined(SJME_CONFIG_HAS_OS_BEOS)
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) \
		"lib" x ".so"
#elif defined(SJME_CONFIG_HAS_OS_CYGWIN)
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) \
		"lib" x ".dll"
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) \
		"" x ".dll"
#elif defined(SJME_CONFIG_HAS_OS_MACOS)
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) \
		"lib" x ".dylib"
#else
	/** Dynamic library path name as static define. */
	#define SJME_CONFIG_DYLIB_PATHNAME(x) NULL
#endif

/* DOS: Threading not supported. */
/* Nintendo 3DS: devkitPro has broken/unimplemented pthreads. */
/* Or if proper multithreaded volatiles are not supported. */
#if !defined(SJME_CONFIG_ONLY_THREAD_SINGLE)
	#if defined(SJME_CONFIG_HAS_OS_PC_DOS) || \
		defined(SJME_CONFIG_HAS_OS_NINTENDO_3DS) || \
		defined(SJME_CONFIG_HAS_ATOMIC_VOLATILE)
		/** Single threaded only (system does not support threads). */
		#define SJME_CONFIG_ONLY_THREAD_SINGLE
	#elif !defined(SJME_CONFIG_HAS_THREADS_PTHREAD) && \
		!defined(SJME_CONFIG_HAS_THREADS_WIN32)
		/** Single threaded only (no other implementations). */
		#define SJME_CONFIG_ONLY_THREAD_SINGLE
	#endif
#endif

#if !defined(SJME_CONFIG_HAS_NO_ERRNO_H) && \
	!defined(SJME_CONFIG_HAS_ERRNO_H)
	#if defined(SJME_CONFIG_HAS_OS_NINTENDO_3DS) || \
		defined(SJME_CONFIG_HAS_OS_NINTENDO_WIIU) || \
		defined(SJME_CONFIG_HAS_OS_NINTENDO_WII) || \
		defined(SJME_CONFIG_HAS_OS_BAREMETAL)
		/* Disable errno support. */
		#define SJME_CONFIG_HAS_NO_ERRNO_H 1
	#endif
#endif

#if !defined(SJME_CONFIG_HAS_ERRNO_H) && !defined(SJME_CONFIG_HAS_NO_ERRNO_H)
	/** errno.h was not checked, assumed to not be available? */
	#define SJME_CONFIG_HAS_NO_ERRNO_H
#endif

#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	/** Has no standard C I/O support. */
	#define SJME_CONFIG_HAS_NO_STDIO 1
#endif

#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	/** Has no abort() call. */
	#define SJME_CONFIG_HAS_NO_ABORT 1
#endif

#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	/** Has no exit() call. */
	#define SJME_CONFIG_HAS_NO_EXIT 1
#endif

#if defined(SJME_CONFIG_HAS_NO_VSNPRINTFA) && \
	defined(SJME_CONFIG_HAS_NO_VSNPRINTFV)
	/** No vsnprintf() */
	#define SJME_CONFIG_HAS_NO_VSNPRINTF 1
#endif

/** Disable all linting of any kind. */
#define sjme_noLint(what) (what) /* NOLINT */ \
	/* ReSharper disable once all */ \
	/* ReSharper disable once CppLocalVariableMightNotBeInitialized */

#if defined(SJME_CONFIG_HAS_ARCH_MIPS) || \
	defined(SJME_CONFIG_HAS_ARCH_POWERPC)
	/** Has no support for unaligned 16-bit access. */
	#define SJME_CONFIG_HAS_NO_UNALIGNED16
	
	/** Has no support for unaligned 32-bit access. */
	#define SJME_CONFIG_HAS_NO_UNALIGNED32
#endif

#if defined(SJME_CONFIG_HAS_MSVC)
	/* Qualifier used multiple times, as there are volatile typedefs. */
	#pragma warning(disable: 4114)

	/* "Deprecated" POSIX name. */
	#pragma warning(disable: 4996)
#endif
	
#if defined(SJME_CONFIG_HAS_SDCC)
	/* warning 85: in function sjme_nvm_byteCode_slowTableSwitch */
	/*   unreferenced function argument : 'id' */
	#pragma disable_warning 85
	
	/* warning 110: conditional flow changed by optimizer: so said EVELYN */
	/*   the modified DOG */
	#pragma disable_warning 110
	
	/* warning 115: Unknown pragma directive */
	#pragma disable_warning 115
	
	/* warning 126: unreachable code */
	#pragma disable_warning 126
	
	/* warning 127: non-pointer type cast to generic pointer */
	#pragma disable_warning 127
	
	/* warning 190: ISO C forbids an empty translation unit */
	#pragma disable_warning 190
	
	/* warning 283: function declarator with no prototype */
	#pragma disable_warning 283
#endif

/** Bitfield count for @link sjme_jboolean @endlink . */
#define sjme_booleanBit 2

/* Clang is completely broken with FLT_ROUNDS. */ 
#if defined(SJME_CONFIG_HAS_CLANG) || defined(SJME_CONFIG_HAS_MSVC)
	/** Assuming floating point rounds to nearest. */
	#define SJME_CONFIG_ASSUME_FLOAT_ROUND_NEAREST
#elif defined(SJME_CONFIG_HAS_FLOAT_H)
	#if defined(FLT_ROUNDS) && FLT_ROUNDS == 1
		/** Has floating point that rounds to nearest. */
		#define SJME_CONFIG_HAS_FLOAT_ROUND_NEAREST
	#endif
#endif
	
/* 32-bit floating point matches Java? */
#if defined(SJME_CONFIG_HAS_FLOAT_H) && \
	(defined(SJME_CONFIG_ASSUME_FLOAT_ROUND_NEAREST) || \
		defined(SJME_CONFIG_HAS_FLOAT_ROUND_NEAREST)) && \
	defined(FLT_EVAL_METHOD) && (FLT_EVAL_METHOD == 0) && \
	defined(FLT_RADIX) && (FLT_RADIX == 2)
	/* Compatible single floating point? */
	#if !defined(SJME_CONFIG_HAS_FLOAT_SOFT) && \
		defined(FLT_MANT_DIG) && (FLT_MANT_DIG == 24) && \
		((defined(FLT_HAS_SUBNORM) && (FLT_HAS_SUBNORM == 1)) || \
		(defined(__FLT_HAS_DENORM__) && (__FLT_HAS_DENORM__ == 1)))
		/** Hardware single floating point. */
		#define SJME_CONFIG_HAS_FLOAT_HARD
	#endif

	/* Compatible double floating point? */
	#if !defined(SJME_CONFIG_HAS_DOUBLE_SOFT) && \
		defined(DBL_MANT_DIG) && (DBL_MANT_DIG == 53) && \
		((defined(DBL_HAS_SUBNORM) && (DBL_HAS_SUBNORM == 1)) || \
		(defined(__DBL_HAS_DENORM__) && (__DBL_HAS_DENORM__ == 1)))
		/** Hardware double floating point. */
		#define SJME_CONFIG_HAS_DOUBLE_HARD
	#endif
#endif

#if defined(SJME_CONFIG_ASSUME_FLOAT_HARD)
	/** Assumed to have hardware floating point. */
	#define SJME_CONFIG_HAS_FLOAT_HARD
#endif

#if defined(SJME_CONFIG_ASSUME_DOUBLE_HARD)
	/** Assumed to have hardware double point. */
	#define SJME_CONFIG_HAS_DOUBLE_HARD
#endif

#if !defined(SJME_CONFIG_HAS_FLOAT_HARD)
	/** Has software single floating point. */
	#define SJME_CONFIG_HAS_FLOAT_SOFT
#endif

#if !defined(SJME_CONFIG_HAS_DOUBLE_HARD)
	/** Has software double floating point. */
	#define SJME_CONFIG_HAS_DOUBLE_SOFT
#endif

#if defined(SJME_CONFIG_HAS_C99) || \
	SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010)
	/** Constant-ish struct member set. */
	#define sjme_sm(dot, val) dot = val
#else
	/** Constant-ish struct member set. */
	#define sjme_sm(dot, val) val
#endif
	
/** Two structure values. */
#define sjme_sm2(dot, v1, v2) \
	sjme_sm(dot, {v1 SJME_TOKEN_COMMA v2})
	
/** Three structure values. */
#define sjme_sm3(dot, v1, v2, v3) \
	sjme_sm(dot, {v1 SJME_TOKEN_COMMA v2 SJME_TOKEN_COMMA v3})

/** Bitfield count for @link sjme_jboolean @endlink . */
#define sjme_booleanBit 2

/* Clang is completely broken with FLT_ROUNDS. */ 
#if defined(SJME_CONFIG_HAS_CLANG) || defined(SJME_CONFIG_HAS_MSVC)
	/** Assuming floating point rounds to nearest. */
	#define SJME_CONFIG_ASSUME_FLOAT_ROUND_NEAREST
#elif defined(SJME_CONFIG_HAS_FLOAT_H)
	#if defined(FLT_ROUNDS) && FLT_ROUNDS == 1
		/** Has floating point that rounds to nearest. */
		#define SJME_CONFIG_HAS_FLOAT_ROUND_NEAREST
	#endif
#endif
	
/* 32-bit floating point matches Java? */
#if defined(SJME_CONFIG_HAS_FLOAT_H) && \
	(defined(SJME_CONFIG_ASSUME_FLOAT_ROUND_NEAREST) || \
		defined(SJME_CONFIG_HAS_FLOAT_ROUND_NEAREST)) && \
	defined(FLT_EVAL_METHOD) && (FLT_EVAL_METHOD == 0) && \
	defined(FLT_RADIX) && (FLT_RADIX == 2)
	/* Compatible single floating point? */
	#if !defined(SJME_CONFIG_HAS_FLOAT_SOFT) && \
		defined(FLT_MANT_DIG) && (FLT_MANT_DIG == 24) && \
		((defined(FLT_HAS_SUBNORM) && (FLT_HAS_SUBNORM == 1)) || \
		(defined(__FLT_HAS_DENORM__) && (__FLT_HAS_DENORM__ == 1)))
		/** Hardware single floating point. */
		#define SJME_CONFIG_HAS_FLOAT_HARD
	#endif

	/* Compatible double floating point? */
	#if !defined(SJME_CONFIG_HAS_DOUBLE_SOFT) && \
		defined(DBL_MANT_DIG) && (DBL_MANT_DIG == 53) && \
		((defined(DBL_HAS_SUBNORM) && (DBL_HAS_SUBNORM == 1)) || \
		(defined(__DBL_HAS_DENORM__) && (__DBL_HAS_DENORM__ == 1)))
		/** Hardware double floating point. */
		#define SJME_CONFIG_HAS_DOUBLE_HARD
	#endif
#endif

#if !defined(SJME_CONFIG_HAS_FLOAT_HARD)
	/** Has software single floating point. */
	#define SJME_CONFIG_HAS_FLOAT_SOFT
#endif

#if !defined(SJME_CONFIG_HAS_DOUBLE_HARD)
	/** Has software double floating point. */
	#define SJME_CONFIG_HAS_DOUBLE_SOFT
#endif

#if defined(SJME_CONFIG_HAS_C99) || \
	SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2019)
	/** Binary float option (0x1.2p3 or 1.2e3). */
	#define SJME_FLOAT_BD(bin, dec) (bin)
#else
	/** Binary float option (0x1.2p3 or 1.2e3). */
	#define SJME_FLOAT_BD(bin, dec) (dec)
#endif

#if defined(SJME_CONFIG_HAS_AMIGA) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_16) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_CE) || \
	defined(SJME_CONFIG_HAS_OS_PALMOS) || \
	defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	/** Use a minimal amount of memory. */
	#define SJME_CONFIG_HAS_LOW_MEMORY
#endif

/* More verbosity? */
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	#if !defined(SJME_CONFIG_DEBUG_BYTECODES)
		/** Print out bytecodes. */
		#define SJME_CONFIG_DEBUG_BYTECODES
	#endif
	
	#if !defined(SJME_CONFIG_DEBUG_ENTRY)
		/** Entry and exit from methods. */
		#define SJME_CONFIG_DEBUG_ENTRY
	#endif

	#if !defined(SJME_CONFIG_DEBUG_FIELD)
		/** Print out field access operations. */
		#define SJME_CONFIG_DEBUG_FIELD
	#endif

	#if !defined(SJME_CONFIG_DEBUG_GC)
		/** Print out GC operations. */
		#define SJME_CONFIG_DEBUG_GC
	#endif

	#if !defined(SJME_CONFIG_DEBUG_MLE)
		/** Print out MLE operations. */
		#define SJME_CONFIG_DEBUG_MLE
	#endif
	
	#if !defined(SJME_CONFIG_DEBUG_TREAD)
		/** Print out tread operations. */
		#define SJME_CONFIG_DEBUG_TREAD
	#endif
#endif

/* Stable verbose printouts. */
#if defined(SJME_CONFIG_DEBUG_VERBOSE_STABLE)
	#if !defined(SJME_CONFIG_DEBUG_ALLOC)
		/** Allocation printouts. */
		#define SJME_CONFIG_DEBUG_ALLOC
	#endif

	#if !defined(SJME_CONFIG_DEBUG_CIRCLEBUF)
		/** Circle buffer printouts. */
		#define SJME_CONFIG_DEBUG_CIRCLEBUF
	#endif

	#if !defined(SJME_CONFIG_DEBUG_CLOSEABLE)
		/** Debug closeable objects. */
		#define SJME_CONFIG_DEBUG_CLOSEABLE
	#endif

	#if !defined(SJME_CONFIG_DEBUG_ZIP)
		/** Debug Zip files. */
		#define SJME_CONFIG_DEBUG_ZIP
	#endif
#endif

#if defined(SJME_CONFIG_HAS_GCC) || defined(SJME_CONFIG_HAS_CLANG)
	#if defined(__has_builtin)
		/** Is the specified GCC built-in available? */
		#define SJME_CONFIG_HAS_GCC_BUILTIN(x) __has_builtin(__builtin_##x)
	#else
		/** Is the specified GCC built-in available? */
		#define SJME_CONFIG_HAS_GCC_BUILTIN(x) 0
	#endif
#else
	/** Is the specified GCC built-in available? */
	#define SJME_CONFIG_HAS_GCC_BUILTIN(x) 0
#endif

#if defined(SJME_CONFIG_HAS_MSVC)
	/* Include the intrinsics header. */
	#include <intrin.h>
	
	/** Is the specified MSVC intrinsic available? */
	#define SJME_CONFIG_HAS_MSVC_INTRINSIC(x) defined(x)
#else
	/** Is the specified MSVC intrinsic available? */
	#define SJME_CONFIG_HAS_MSVC_INTRINSIC(x) 0
#endif

/** Include code that should work. */
#define SJME_CONFIG_CODE_SHOULD_WORK 1

#if defined(SJME_CONFIG_HAS_MSVC)
	#define sjme_asm(x) __asm {x}
#elif defined(SJME_CONFIG_HAS_GCC) || defined(SJME_CONFIG_HAS_CLANG)
	#define sjme_asm(x) __asm__ {x}
#else
	#define sjme_asm(x) sjme_execInlineAsm(#x)
#endif

/** Milliseconds as nanos. */
#define SJME_NANOS_MS(n) INT64_C(n##000000)
	
/** Microseconds as nanos. */
#define SJME_NANOS_US(n) INT64_C(n##000)
	
/** Nanoseconds as nanos (identity). */
#define SJME_NANOS_NS(n) INT64_C(n)

#pragma region(threadLocal)

#if !defined(SJME_CONFIG_HAS_NO_THREAD_LOCAL)
	#if defined(SJME_CONFIG_HAS_MSVC)
		/** Thread local storage. */
		#define sjme_threadLocal(type, name) \
			static sjme_align32 type __declspec(thread) name;
	#elif defined(SJME_CONFIG_HAS_GCC) || \
		defined(SJME_CONFIG_HAS_CLANG)
		/** Thread local storage. */
		#define sjme_threadLocal(type, name) \
			static sjme_align32 __thread type name
	#endif
#endif

#if !defined(sjme_threadLocal) && \
	!defined(SJME__CONFIG_CHECK__SJME_THREAD_LOCAL)
	/** Thread local storage. */
	#define sjme_threadLocal(type, name) \
		static sjme_align32 type name
#endif

#pragma endregion(threadLocal)
	
/* Weak does not work on Windows, select_any is an "equivalent". */
#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	/** Enable use of @link sjme_attrSelectAny @endlink. */
	#define SJME_CONFIG_HAS_ATTR_SELECT_ANY
#else
	/** Enable use of @link sjme_attrWeak @endlink. */
	#define SJME_CONFIG_HAS_ATTR_WEAK
#endif
	
/* Windows header needs to be included everywhere effectively. */
#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	#define WIN32_LEAN_AND_MEAN 1
	
	#include <windows.h>
#endif
	
/* Missing standard C functions, always include these. */
#include "sjme/stdGone.h"
	
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
