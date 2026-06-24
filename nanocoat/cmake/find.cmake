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
	SJME_CONFIG_HAS_NO_THREADS)

# sys/socket.h available?
squirreljme_check_include_file("sys/socket.h"
	SJME_CONFIG_HAS_SYS_SOCKET_H
	SJME_CONFIG_HAS_NO_SYS_SOCKET_H)

# ctype.h available?
squirreljme_check_include_file("ctype.h"
	SJME_CONFIG_HAS_CTYPE_H
	SJME_CONFIG_HAS_NO_CTYPE_H)

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
check_symbol_exists("getenv" "stdlib.h" SJME_CONFIG_HAS_GETENV)
if(SJME_CONFIG_HAS_GETENV)
	add_compile_definitions(SJME_CONFIG_HAS_GETENV=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_NO_GETENV=1)
endif()

# strcasecmp()?
check_symbol_exists("strcasecmp" "strings.h" SJME_CONFIG_HAS_STRCASECMP)
if(SJME_CONFIG_HAS_STRCASECMP)
	add_compile_definitions(SJME_CONFIG_HAS_STRCASECMP=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_NO_STRCASECMP=1)
endif()

# stricmp()?
check_symbol_exists("stricmp" "string.h" SJME_CONFIG_HAS_STRICMP)
if(SJME_CONFIG_HAS_STRICMP)
	add_compile_definitions(SJME_CONFIG_HAS_STRICMP=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_NO_STRICMP=1)
endif()

# toupper()?
check_symbol_exists("toupper" "ctype.h" SJME_CONFIG_HAS_TOUPPER)
if(SJME_CONFIG_HAS_TOUPPER)
	add_compile_definitions(SJME_CONFIG_HAS_TOUPPER=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_NO_TOUPPER=1)
endif()

# tolower()?
check_symbol_exists("tolower" "ctype.h" SJME_CONFIG_HAS_TOLOWER)
if(SJME_CONFIG_HAS_TOLOWER)
	add_compile_definitions(SJME_CONFIG_HAS_TOLOWER=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_NO_TOLOWER=1)
endif()

# snprintf() available?
squirreljme_try_compile("fdatasync() in unistd.h"
	"tryFDataSync"
	SJME_CONFIG_HAS_FDATASYNC
	SJME_CONFIG_HAS_NO_FDATASYNC)

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
squirreljme_try_compile("sjme_threadLocal"
	"tryThreadLocal"
	SJME_CONFIG_HAS_THREAD_LOCAL
	SJME_CONFIG_HAS_NO_THREAD_LOCAL)
