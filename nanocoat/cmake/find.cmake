# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Is pkg-config available?
if(NOT DEFINED PKG_CONFIG_FOUND)
	find_package(PkgConfig)
endif()

# float.h available?
squirreljme_check_include_file("float.h"
	SJME_CONFIG_HAS_FLOAT_H
	SJME_CONFIG_HAS_NO_FLOAT_H)

# dlfcn.h available?
squirreljme_check_include_file("dlfcn.h"
	SJME_CONFIG_HAS_DLFCN_H
	SJME_CONFIG_HAS_NO_DLFCN_H)

# stdarg.h available?
squirreljme_check_include_file("stdarg.h"
	SJME_CONFIG_HAS_STDARG_H
	SJME_CONFIG_HAS_NO_STDARG_H)

# inttypes.h available?
squirreljme_check_include_file("inttypes.h"
	SJME_CONFIG_HAS_INTTYPES_H
	SJME_CONFIG_HAS_NO_INTTYPES_H)

# varargs.h available?
squirreljme_check_include_file("varargs.h"
	SJME_CONFIG_HAS_VARARGS_H
	SJME_CONFIG_HAS_NO_VARARGS_H)

# threads.h available?
squirreljme_check_include_file("threads.h"
	SJME_CONFIG_HAS_THREADS_H
	SJME_CONFIG_HAS_NO_THREADS_H)

# wchar.h available?
squirreljme_check_include_file("wchar.h"
	SJME_CONFIG_HAS_WCHAR_H
	SJME_CONFIG_HAS_NO_WCHAR_H)

# sys/socket.h available?
squirreljme_check_include_file("sys/socket.h"
	SJME_CONFIG_HAS_SYS_SOCKET_H
	SJME_CONFIG_HAS_NO_SYS_SOCKET_H)

# ctype.h available?
squirreljme_check_include_file("ctype.h"
	SJME_CONFIG_HAS_CTYPE_H
	SJME_CONFIG_HAS_NO_CTYPE_H)

# wctype.h available?
squirreljme_check_include_file("wctype.h"
	SJME_CONFIG_HAS_WCTYPE_H
	SJME_CONFIG_HAS_NO_WCTYPE_H)

# netinet/in.h available?
squirreljme_check_include_file("netinet/in.h"
	SJME_CONFIG_HAS_NETINET_IN_H
	SJME_CONFIG_HAS_NO_NETINET_IN_H)

# sys/ioctl.h available?
squirreljme_check_include_file("sys/ioctl.h"
	SJME_CONFIG_HAS_SYS_IOCTL_H
	SJME_CONFIG_HAS_NO_SYS_IOCTL_H)

# stropts.h available?
squirreljme_check_include_file("stropts.h"
	SJME_CONFIG_HAS_STROPTS_H
	SJME_CONFIG_HAS_NO_STROPTS_H)

# errno.h available?
squirreljme_check_include_file("errno.h"
	SJME_CONFIG_HAS_ERRNO_H
	SJME_CONFIG_HAS_NO_ERRNO_H)

# poll.h available?
squirreljme_check_include_file("poll.h"
	SJME_CONFIG_HAS_POLL_H
	SJME_CONFIG_HAS_NO_POLL_H)

# stdint.h available?
squirreljme_check_include_file("stdint.h"
	SJME_CONFIG_HAS_STDINT_H
	SJME_CONFIG_HAS_NO_STDINT_H)

# Is the SDK version header information available?
squirreljme_check_include_file("sdkddkver.h"
	SJME_CONFIG_HAS_SDKDDKVER_H
	SJME_CONFIG_HAS_NO_SDKDDKVER_H)

# getenv()?
squirreljme_check_symbol_exists("getenv" "stdlib.h"
	SJME_CONFIG_HAS_GETENV
	SJME_CONFIG_HAS_NO_GETENV)

# strcasecmp()?
squirreljme_check_symbol_exists("strcasecmp" "strings.h"
	SJME_CONFIG_HAS_STRCASECMP
	SJME_CONFIG_HAS_NO_STRCASECMP)

# stricmp()?
squirreljme_check_symbol_exists("stricmp" "string.h"
	SJME_CONFIG_HAS_STRICMP
	SJME_CONFIG_HAS_NO_STRICMP)

# wcscasecmp()?
squirreljme_check_symbol_exists("wcscasecmp" "wchar.h"
	SJME_CONFIG_HAS_WCSCASECMP
	SJME_CONFIG_HAS_NO_WCSCASECMP)

# wcsicmp()?
squirreljme_check_symbol_exists("wcsicmp" "wchar.h"
	SJME_CONFIG_HAS_WCSICMP
	SJME_CONFIG_HAS_NO_WCSICMP)

# strnlen()?
squirreljme_check_symbol_exists("strnlen" "string.h"
	SJME_CONFIG_HAS_STRNLEN
	SJME_CONFIG_HAS_NO_STRNLEN)

# C case conversion?
squirreljme_check_symbol_exists("toupper" "ctype.h"
	SJME_CONFIG_HAS_TOUPPER
	SJME_CONFIG_HAS_NO_TOUPPER)
squirreljme_check_symbol_exists("tolower" "ctype.h"
	SJME_CONFIG_HAS_TOLOWER
	SJME_CONFIG_HAS_NO_TOLOWER)
squirreljme_check_symbol_exists("towupper" "wctype.h"
	SJME_CONFIG_HAS_TOWUPPER
	SJME_CONFIG_HAS_NO_TOWUPPER)
squirreljme_check_symbol_exists("towlower" "wctype.h"
	SJME_CONFIG_HAS_TOWLOWER
	SJME_CONFIG_HAS_NO_TOWLOWER)

# fdatasync() available?
# If the system is unknown, the assume this POSIX function does not exist
if(SQUIRRELJME_IS_UNKNOWN)
	squirreljme_try_compile_no("fdatasync()"
		SJME_CONFIG_HAS_NO_FDATASYNC)
else()
	squirreljme_try_compile("fdatasync() in unistd.h"
		"tryFDataSync"
		SJME_CONFIG_HAS_FDATASYNC
		SJME_CONFIG_HAS_NO_FDATASYNC)
endif()

# snprintf() available?
squirreljme_try_compile("snprintf()"
	"trySNPrintF"
	SJME_CONFIG_HAS_SNPRINTF
	SJME_CONFIG_HAS_NO_SNPRINTF)

# vsnprintf() available?
squirreljme_try_compile("vsnprintf() in stdarg.h"
	"tryVSNPrintFA"
	SJME_CONFIG_HAS_VSNPRINTFA
	SJME_CONFIG_HAS_NO_VSNPRINTFA)
squirreljme_try_compile("vsnprintf() in varargs.h"
	"tryVSNPrintFV"
	SJME_CONFIG_HAS_VSNPRINTFV
	SJME_CONFIG_HAS_NO_VSNPRINTFV)

# Can use thread local?
# If the system is unknown, we cannot rely on this to properly exist
if(SQUIRRELJME_IS_UNKNOWN)
	squirreljme_try_compile_no("sjme_threadLocal"
		SJME_CONFIG_HAS_NO_THREAD_LOCAL)
else()
	squirreljme_try_compile("sjme_threadLocal"
		"tryThreadLocal"
		SJME_CONFIG_HAS_THREAD_LOCAL
		SJME_CONFIG_HAS_NO_THREAD_LOCAL)
endif()
