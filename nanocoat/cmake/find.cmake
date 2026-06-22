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
check_include_file("float.h" SJME_CONFIG_HAS_FLOAT_H)
if(NOT SJME_CONFIG_HAS_FLOAT_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_FLOAT_H=1)
endif()

# dlfcn.h available?
check_include_file("dlfcn.h" SJME_CONFIG_HAS_DLFCN_H)
if(NOT SJME_CONFIG_HAS_DLFCN_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_DLFCN_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_DLFCN_H=1)
endif()

# stdarg.h available?
check_include_file("stdarg.h" SJME_CONFIG_HAS_STDARG_H)
if(NOT SJME_CONFIG_HAS_STDARG_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_STDARG_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_STDARG_H=1)
endif()

# inttypes.h available?
check_include_file("inttypes.h" SJME_CONFIG_HAS_INTTYPES_H)
if(NOT SJME_CONFIG_HAS_INTTYPES_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_INTTYPES_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_INTTYPES_H=1)
endif()

# varargs.h available?
check_include_file("varargs.h" SJME_CONFIG_HAS_VARARGS_H)
if(NOT SJME_CONFIG_HAS_VARARGS_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_VARARGS_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_VARARGS_H=1)
endif()

# threads.h available?
check_include_file("threads.h" SJME_CONFIG_HAS_THREADS_H)
if(NOT SJME_CONFIG_HAS_THREADS_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_C11_THREADS=1)
endif()

# sys/socket.h available?
check_include_file("sys/socket.h" SJME_CONFIG_HAS_SYS_SOCKET_H)
if(NOT SJME_CONFIG_HAS_SYS_SOCKET_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_SYS_SOCKET_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_SYS_SOCKET_H=1)
endif()

# ctype.h available?
check_include_file("ctype.h" SJME_CONFIG_HAS_CTYPE_H)
if(NOT SJME_CONFIG_HAS_CTYPE_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_CTYPE_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_CTYPE_H=1)
endif()

# netinet/in.h available?
check_include_file("netinet/in.h" SJME_CONFIG_HAS_NETINET_IN_H)
if(NOT SJME_CONFIG_HAS_NETINET_IN_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_NETINET_IN_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_NETINET_IN_H=1)
endif()

# sys/ioctl.h available?
check_include_file("sys/ioctl.h" SJME_CONFIG_HAS_SYS_IOCTL_H)
if(NOT SJME_CONFIG_HAS_SYS_IOCTL_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_SYS_IOCTL_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_SYS_IOCTL_H=1)
endif()

# stropts.h available?
check_include_file("stropts.h" SJME_CONFIG_HAS_STROPTS_H)
if(NOT SJME_CONFIG_HAS_STROPTS_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_STROPTS_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_STROPTS_H=1)
endif()

# errno.h available?
check_include_file("errno.h" SJME_CONFIG_HAS_ERRNO_H)
if(NOT SJME_CONFIG_HAS_ERRNO_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_ERRNO_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_ERRNO_H=1)
endif()

# poll.h available?
check_include_file("poll.h" SJME_CONFIG_HAS_POLL_H)
if(NOT SJME_CONFIG_HAS_POLL_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_POLL_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_POLL_H=1)
endif()

# stdint.h available?
check_include_file("stdint.h" SJME_CONFIG_HAS_STDINT_H)
if(NOT SJME_CONFIG_HAS_STDINT_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_STDINT_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_STDINT_H=1)
endif()

# Is the SDK version header information available?
check_include_file("sdkddkver.h" WIN32_SDKDDKVER_INCLUDE)
if(WIN32_SDKDDKVER_INCLUDE)
	add_compile_definitions(SJME_CONFIG_HAS_SDKDDKVER_H=1)
endif()

# getenv()?
check_symbol_exists("getenv" "stdlib.h" SJME_CONFIG_HAS_GETENV)
if(NOT SJME_CONFIG_HAS_GETENV)
	add_compile_definitions(SJME_CONFIG_HAS_NO_GETENV=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_GETENV=1)
endif()

# strcasecmp()?
check_symbol_exists("strcasecmp" "strings.h" SJME_CONFIG_HAS_STRCASECMP)
if(NOT SJME_CONFIG_HAS_STRCASECMP)
	add_compile_definitions(SJME_CONFIG_HAS_NO_STRCASECMP=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_STRCASECMP=1)
endif()

# stricmp()?
check_symbol_exists("stricmp" "string.h" SJME_CONFIG_HAS_STRICMP)
if(NOT SJME_CONFIG_HAS_STRICMP)
	add_compile_definitions(SJME_CONFIG_HAS_NO_STRICMP=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_STRICMP=1)
endif()

# toupper()?
check_symbol_exists("toupper" "ctype.h" SJME_CONFIG_HAS_TOUPPER)
if(NOT SJME_CONFIG_HAS_TOUPPER)
	add_compile_definitions(SJME_CONFIG_HAS_NO_TOUPPER=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_TOUPPER=1)
endif()

# tolower()?
check_symbol_exists("tolower" "ctype.h" SJME_CONFIG_HAS_TOLOWER)
if(NOT SJME_CONFIG_HAS_TOLOWER)
	add_compile_definitions(SJME_CONFIG_HAS_NO_TOLOWER=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_TOLOWER=1)
endif()

# snprintf() available?
squirreljme_try_compile("fdatasync()"
	SJME_CONFIG_HAS_FDATASYNC
	"tryFDataSync"
	SJME_CONFIG_HAS_NO_FDATASYNC)
if(NOT SJME_CONFIG_HAS_NO_FDATASYNC)
	add_compile_definitions(SJME_CONFIG_HAS_NO_FDATASYNC=1)
elseif(SJME_CONFIG_HAS_FDATASYNC)
	add_compile_definitions(SJME_CONFIG_HAS_FDATASYNC=1)
endif()

# snprintf() available?
squirreljme_try_compile("snprintf()"
	SQUIRRELJME_SNPRINTF_TRY_VALID
	"trySNPrintF"
	SJME_CONFIG_HAS_NO_SNPRINTF)

# vsnprintf() available?
squirreljme_try_compile("vsnprintf() with stdarg.h"
	SQUIRRELJME_VSNPRINTFA_TRY_VALID
	"tryVSNPrintFA"
	SJME_CONFIG_HAS_NO_VSNPRINTFA)
squirreljme_try_compile("vsnprintf() with varargs.h"
	SQUIRRELJME_VSNPRINTFV_TRY_VALID
	"tryVSNPrintFV"
	SJME_CONFIG_HAS_NO_VSNPRINTFV)

# Can use thread local?
squirreljme_try_compile("sjme_threadLocal"
	SQUIRRELJME_C11_THREADS_TRY_THREAD_LOCAL
	"tryThreadLocal"
	SJME_CONFIG_HAS_NO_THREAD_LOCAL)
