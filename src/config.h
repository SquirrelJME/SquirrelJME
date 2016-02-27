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
 * Configuration file which is based on pre-processor macros.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGCONFIGH
#define SJME_hGCONFIGH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXCONFIGH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/** List of targets (not necessarily supported ones). */
#define SJME_TARGET_UNKNOWN		0
#define SJME_TARGET_RESERVED	1
#define SJME_TARGET_AMIGAOS4	2
#define SJME_TARGET_AMIGAOSC	3
#define SJME_TARGET_ARDUINO		4
#define SJME_TARGET_ARDUINOMEG	5
#define SJME_TARGET_ATARI		6
#define SJME_TARGET_BEOS		7
#define SJME_TARGET_BGLBNBLACK	8
#define SJME_TARGET_BREWMP		9
#define SJME_TARGET_BSD			10
#define SJME_TARGET_COMMDR128	11
#define SJME_TARGET_COMMDR64	12
#define SJME_TARGET_CSTANDARD	13
#define SJME_TARGET_DOS			14
#define SJME_TARGET_DREAMCAST	15
#define SJME_TARGET_DREAMCASTK	16
#define SJME_TARGET_GIMMIX		17
#define SJME_TARGET_GNUHURD		18
#define SJME_TARGET_HAIKU		19
#define SJME_TARGET_HPUX		20
#define SJME_TARGET_X86BIOS		21
#define SJME_TARGET_IEEE1275	22
#define SJME_TARGET_IMGTECCI20	23
#define SJME_TARGET_IMGTECCI40	24
#define SJME_TARGET_JAVAVM		25
#define SJME_TARGET_LINUX		26
#define SJME_TARGET_MACOSC		27
#define SJME_TARGET_MACOSX		28
#define SJME_TARGET_MINIX		29
#define SJME_TARGET_N3DS		30
#define SJME_TARGET_NDS			31
#define SJME_TARGET_NES			32
#define SJME_TARGET_NGB			33
#define SJME_TARGET_NGBA		34
#define SJME_TARGET_NGC			35
#define SJME_TARGET_NINTENDO64	36
#define SJME_TARGET_NWII		37
#define SJME_TARGET_NWIIU		38
#define SJME_TARGET_OS2			39
#define SJME_TARGET_PALMOS		40
#define SJME_TARGET_PALMOSCBLT	41
#define SJME_TARGET_PLAN9		42
#define SJME_TARGET_POSIX		43
#define SJME_TARGET_RBPI		44
#define SJME_TARGET_RBPI2		45
#define SJME_TARGET_RBPIZERO	46
#define SJME_TARGET_SEGACD		47
#define SJME_TARGET_SEGAGEN		48
#define SJME_TARGET_SEGAGGEAR	49
#define SJME_TARGET_SEGASAT		50
#define SJME_TARGET_SNES		51
#define SJME_TARGET_SOLARIS		52
#define SJME_TARGET_SONYPS		53
#define SJME_TARGET_SONYPS2		54
#define SJME_TARGET_SONYPS3		55
#define SJME_TARGET_SONYPS4		56
#define SJME_TARGET_TI83		57
#define SJME_TARGET_UNIX		58
#define SJME_TARGET_VOCORE		59
#define SJME_TARGET_WIN31		60
#define SJME_TARGET_WIN9X		61
#define SJME_TARGET_WINCE		62
#define SJME_TARGET_WINNT		63
#define SJME_TARGET_WRT54G		64
#define SJME_TARGET_UEFI		65
#define SJME_TARGET_XBOX		66
#define SJME_TARGET_XBOX360		67
#define SJME_TARGET_XBOXONE		68
#define SJME_TARGET_XEN			69
#define SJME_TARGET_Z8F642001	70

/** Endians. */
#define SJME_ENDIAN_UNKNOWN		0
#define SJME_ENDIAN_BIG			1
#define SJME_ENDIAN_LITTLE		2

/** CPUs. */
#define SJME_CPU_UNKOWN			0
#define SJME_CPU_6502			1
#define SJME_CPU_AARCH64		2
#define SJME_CPU_ALPHA			3
#define SJME_CPU_AMD64			4
#define SJME_CPU_IA16			5
#define SJME_CPU_IA32			6
#define SJME_CPU_IA64			7
#define SJME_CPU_LM32			8
#define SJME_CPU_M68K			9
#define SJME_CPU_MIPS64			10
#define SJME_CPU_MIPS			11
#define SJME_CPU_MIX			12
#define SJME_CPU_MMIX			13
#define SJME_CPU_MOXIE			14
#define SJME_CPU_OR32			15
#define SJME_CPU_POWERPC		16
#define SJME_CPU_S390X			17
#define SJME_CPU_SH4			18
#define SJME_CPU_SPARC			19
#define SJME_CPU_Z80			20
#define SJME_CPU_EZ8			21

/** Auto-detect target? */
#if !defined(SJME_TARGET) || \
	(SJME_TARGET == SJME_TARGET_UNKNOWN)
	
	/** Clear definition if defined. */
	#if defined(SJME_TARGET)
		#undef SJME_TARGET
	#endif

	/** DOS or MS-DOS. */
	#if defined(__MSDOS__) || defined(__DOS)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_DOS
		#endif
	#endif

	/** Linux. */
	#if defined(__linux__)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_LINUX
		#endif
	#endif

	/** Mac OS Classic */
	#if (defined(macintosh) || defined(Macintosh)) && !defined(__MACH__)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_MACOSC
		#endif
	#endif

	/** Mac OS X. */
	#if defined(__APPLE__) && defined(__MACH__)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_MACOSX
		#endif
	#endif

	/** PalmOS. */
	#if defined(__palmos__)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_PALMOS
		#endif
	#endif

	/** Windows (desktop). */
	#if defined(_WIN32) && !defined(_WIN32_WCE)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_WIN32
		#endif
	#endif

	/** Windows CE. */
	#if defined(_WIN32_WCE)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_WINCE
		#endif
	#endif
	
	/** Fallback: UNIX/POSIX. */
	#if defined(__unix__)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_POSIX
		#endif
	#endif
	
	/** Fallback: Standard C. */
	#if defined(__STDC__)
		#if !defined(SJME_TARGET)
			#define SJME_TARGET SJME_TARGET_CSTANDARD
		#endif
	#endif
#endif

/** Detect CPU? */
#if !defined(SJME_CPU) || \
	(SJME_CPU == SJME_CPU_UNKNOWN)
	
	/** Clear definition if defined. */
	#if defined(SJME_CPU)
		#undef SJME_CPU
	#endif
	
	/** PowerPC? */
	#if defined(_ARCH_PPC) || defined(__powerpc__) || defined(__powerpc)
		#if !defined(SJME_CPU)
			#define SJME_CPU SJME_CPU_POWERPC
		#endif
	#endif
	
	/** m68k? */
	#if defined(__m68k__) || defined(__mc68000__)
		#if !defined(SJME_CPU)
			#define SJME_CPU SJME_CPU_M68K
		#endif
	#endif
	
	/** Turbo C on DOS is always i286 */
	#if (SJME_TARGET == SJME_TARGET_DOS && defined(__TURBOC__))
		#if !defined(SJME_CPU)
			#define SJME_CPU SJME_CPU_IA16
		#endif
	#endif
#endif

/** Detect endianess? */
#if !defined(SJME_ENDIAN) || \
	(SJME_ENDIAN == SJME_ENDIAN_UNKNOWN)
	
	/** Clear definition if defined. */
	#if defined(SJME_ENDIAN)
		#undef SJME_ENDIAN
	#endif
	
	/** Big endian? */
	#if defined(BIG_ENDIAN) || defined(_BIG_ENDIAN_) || defined(__BIG_ENDIAN__)
		#if !defined(SJME_ENDIAN)
			#define SJME_ENDIAN SJME_ENDIAN_BIG
		#endif
	#endif
	
	/** Little endian? */
	#if defined(LITTLE_ENDIAN) || defined(_LITTLE_ENDIAN_) || \
		defined(__LITTLE_ENDIAN__)
		#if !defined(SJME_ENDIAN)
			#define SJME_ENDIAN SJME_ENDIAN_LITTLE
		#endif
	#endif
	
	/** Known big endian systems. */
	#if ((SJME_TARGET == SJME_TARGET_PALMOS) && \
			(SJME_CPU == SJME_CPU_M68K))
		#if !defined(SJME_ENDIAN)
			#define SJME_ENDIAN SJME_ENDIAN_BIG
		#endif
	#endif
	
	/** Known little endian systems. */
	#if ((SJME_TARGET == SJME_TARGET_DOS) && \
			(SJME_CPU == SJME_CPU_IA16)) || \
		((SJME_TARGET == SJME_TARGET_DOS) && \
			(SJME_CPU == SJME_CPU_IA32))
		#if !defined(SJME_ENDIAN)
			#define SJME_ENDIAN SJME_ENDIAN_LITTLE
		#endif
	#endif
#endif

/** No target configured? */
#if !defined(SJME_TARGET) || \
	(SJME_TARGET == SJME_TARGET_UNKNOWN)
	#error Must define SJME_TARGET.
#endif

/** No endianess configured? */
#if !defined(SJME_ENDIAN) || \
	(SJME_ENDIAN == SJME_ENDIAN_UNKNOWN)
	#error Must define SJME_ENDIAN.
#endif

/** No CPU configured? */
#if !defined(SJME_CPU) || \
	(SJME_CPU == SJME_CPU_UNKNOWN)
	#error Must define SJME_CPU.
#endif

/** Include system specific header. */
#if SJME_TARGET == SJME_TARGET_AMIGAOS4
	#include "sys_aos4.h"
#elif SJME_TARGET == SJME_TARGET_AMIGAOSC
	#include "sys_aosc.h"
#elif SJME_TARGET == SJME_TARGET_ARDUINO
	#include "sys_ardu.h"
#elif SJME_TARGET == SJME_TARGET_ARDUINOMEG
	#include "sys_ardm.h"
#elif SJME_TARGET == SJME_TARGET_ATARI
	#include "sys_atri.h"
#elif SJME_TARGET == SJME_TARGET_BEOS
	#include "sys_beos.h"
#elif SJME_TARGET == SJME_TARGET_BGLBNBLACK
	#include "sys_bbbl.h"
#elif SJME_TARGET == SJME_TARGET_BREWMP
	#include "sys_brwm.h"
#elif SJME_TARGET == SJME_TARGET_BSD
	#include "sys_xbsd.h"
#elif SJME_TARGET == SJME_TARGET_COMMDR128
	#include "sys_c128.h"
#elif SJME_TARGET == SJME_TARGET_COMMDR64
	#include "sys_co64.h"
#elif SJME_TARGET == SJME_TARGET_CSTANDARD
	#include "sys_stdc.h"
#elif SJME_TARGET == SJME_TARGET_DOS
	#include "sys_xdos.h"
#elif SJME_TARGET == SJME_TARGET_DREAMCAST
	#include "sys_drmc.h"
#elif SJME_TARGET == SJME_TARGET_DREAMCASTK
	#include "sys_dcko.h"
#elif SJME_TARGET == SJME_TARGET_GIMMIX
	#include "sys_gimx.h"
#elif SJME_TARGET == SJME_TARGET_GNUHURD
	#include "sys_hurd.h"
#elif SJME_TARGET == SJME_TARGET_HAIKU
	#include "sys_haik.h"
#elif SJME_TARGET == SJME_TARGET_HPUX
	#include "sys_hpux.h"
#elif SJME_TARGET == SJME_TARGET_X86BIOS
	#include "sys_x86b.h"
#elif SJME_TARGET == SJME_TARGET_IEEE1275
	#include "sys_ofwr.h"
#elif SJME_TARGET == SJME_TARGET_IMGTECCI20
	#include "sys_ci20.h"
#elif SJME_TARGET == SJME_TARGET_IMGTECCI40
	#include "sys_ci40.h"
#elif SJME_TARGET == SJME_TARGET_JAVAVM
	#include "sys_java.h"
#elif SJME_TARGET == SJME_TARGET_LINUX
	#include "sys_lnux.h"
#elif SJME_TARGET == SJME_TARGET_MACOSC
	#include "sys_macc.h"
#elif SJME_TARGET == SJME_TARGET_MACOSX
	#include "sys_macx.h"
#elif SJME_TARGET == SJME_TARGET_MINIX
	#include "sys_mnix.h"
#elif SJME_TARGET == SJME_TARGET_N3DS
	#include "sys_n3ds.h"
#elif SJME_TARGET == SJME_TARGET_NDS
	#include "sys_nids.h"
#elif SJME_TARGET == SJME_TARGET_NES
	#include "sys_nnes.h"
#elif SJME_TARGET == SJME_TARGET_NGB
	#include "sys_ngmb.h"
#elif SJME_TARGET == SJME_TARGET_NGBA
	#include "sys_ngba.h"
#elif SJME_TARGET == SJME_TARGET_NGC
	#include "sys_ngcu.h"
#elif SJME_TARGET == SJME_TARGET_NINTENDO64
	#include "sys_nn64.h"
#elif SJME_TARGET == SJME_TARGET_NWII
	#include "sys_nwii.h"
#elif SJME_TARGET == SJME_TARGET_NWIIU
	#include "sys_nwiu.h"
#elif SJME_TARGET == SJME_TARGET_OS2
	#include "sys_os2x.h"
#elif SJME_TARGET == SJME_TARGET_PALMOS
	#include "sys_palm.h"
#elif SJME_TARGET == SJME_TARGET_PALMOSCBLT
	#include "sys_plmc.h"
#elif SJME_TARGET == SJME_TARGET_PLAN9
	#include "sys_pln9.h"
#elif SJME_TARGET == SJME_TARGET_POSIX
	#include "sys_posx.h"
#elif SJME_TARGET == SJME_TARGET_RBPI
	#include "sys_rbpi.h"
#elif SJME_TARGET == SJME_TARGET_RBPI2
	#include "sys_rbp2.h"
#elif SJME_TARGET == SJME_TARGET_RBPIZERO
	#include "sys_rbpz.h"
#elif SJME_TARGET == SJME_TARGET_SEGACD
	#include "sys_sgcd.h"
#elif SJME_TARGET == SJME_TARGET_SEGAGEN
	#include "sys_sgge.h"
#elif SJME_TARGET == SJME_TARGET_SEGAGGEAR
	#include "sys_sggg.h"
#elif SJME_TARGET == SJME_TARGET_SEGASAT
	#include "sys_sgsa.h"
#elif SJME_TARGET == SJME_TARGET_SNES
	#include "sys_snes.h"
#elif SJME_TARGET == SJME_TARGET_SOLARIS
	#include "sys_sola.h"
#elif SJME_TARGET == SJME_TARGET_SONYPS
	#include "sys_sps1.h"
#elif SJME_TARGET == SJME_TARGET_SONYPS2
	#include "sys_sps2.h"
#elif SJME_TARGET == SJME_TARGET_SONYPS3
	#include "sys_sps3.h"
#elif SJME_TARGET == SJME_TARGET_SONYPS4
	#include "sys_sps4.h"
#elif SJME_TARGET == SJME_TARGET_TI83
	#include "sys_ti83.h"
#elif SJME_TARGET == SJME_TARGET_UNIX
	#include "sys_unix.h"
#elif SJME_TARGET == SJME_TARGET_VOCORE
	#include "sys_vocr.h"
#elif SJME_TARGET == SJME_TARGET_WIN31
	#include "sys_wi31.h"
#elif SJME_TARGET == SJME_TARGET_WIN9X
	#include "sys_wi9x.h"
#elif SJME_TARGET == SJME_TARGET_WINCE
	#include "sys_wice.h"
#elif SJME_TARGET == SJME_TARGET_WINNT
	#include "sys_wint.h"
#elif SJME_TARGET == SJME_TARGET_WRT54G
	#include "sys_wrt5.h"
#elif SJME_TARGET == SJME_TARGET_UEFI
	#include "sys_uefi.h"
#elif SJME_TARGET == SJME_TARGET_XBOX
	#include "sys_xbox.h"
#elif SJME_TARGET == SJME_TARGET_XBOX360
	#include "sys_xbx3.h"
#elif SJME_TARGET == SJME_TARGET_XBOXONE
	#include "sys_xbx1.h"
#elif SJME_TARGET == SJME_TARGET_XEN
	#include "sys_xxen.h"
#elif SJME_TARGET == SJME_TARGET_Z8F642001
	#include "sys_z8f6.h"
#else
	#error Do not know which target header to include.
#endif

/** Memory space keywords. */
#if !defined(sjme_addr_code)
	#error Must define sjme_addr_code address space.
#endif
#if !defined(sjme_addr_mem)
	#error Must define sjme_addr_mem address space.
#endif

/** Constant. */
#if !defined(sjme_const)
	#error Must define sjme_const keyword.
#endif

/** String specials. */
#if !defined(sjme_addr_codestr)
	#error Must define sjme_addr_codestr string modifier.
#endif
#if !defined(sjme_addr_memstr)
	#error Must define sjme_addr_memstr string modifier.
#endif

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXCONFIGH
}
#undef SJME_cXCONFIGH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXCONFIGH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGCONFIGH */

