# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Threading and atomics support

# Used to determine if certain symbols exist
include(CheckSymbolExists)

# These platforms do not support any kind of threading just by their nature
# Previously a comment existed that specified that unknown platforms
# Do not use any form of threading if building a libretro core, as most of
# the toolchains used are either out of date and/or broken.
if("${SQUIRRELJME_SYSTEM}" STREQUAL "dos" OR
	SQUIRRELJME_IS_LIBRETRO OR
	SJME_CONFIG_ONLY_THREAD_SINGLE)
	# Why?
	if(SJME_CONFIG_ONLY_THREAD_SINGLE)
		message(STATUS "A configuration flag disabled threading!")
	elseif(SQUIRRELJME_IS_LIBRETRO)
		message(STATUS "libretro cores explicitly have threading disabled!")
	else()
		message(STATUS "Threads not supported on ${SQUIRRELJME_SYSTEM}!")
	endif()

	# Only a single thread is possible
	set(SJME_CONFIG_ONLY_THREAD_SINGLE TRUE)

# On Windows do not use pthreads at all as it has its own threading system
elseif("${SQUIRRELJME_SYSTEM}" STREQUAL "windows" OR
	("${SQUIRRELJME_SYSTEM}" STREQUAL "wine" AND
	SJME_CONFIG_EO_LIBWINE_THREADS))
	# Notice
	message(STATUS "Forcing Win32 Threads!")

	# Force this
	add_compile_definitions(SJME_CONFIG_HAS_THREADS=1)
	add_compile_definitions(SJME_CONFIG_HAS_THREADS_WIN32=1)

	# Threading is valid
	set(SJME_CONFIG_ONLY_THREAD_SINGLE FALSE)

	# No library is needed for Win32
	unset(SQUIRRELJME_LIBTHREADS)

# Otherwise, try to detect the threading mechanism
else()
	# Debug
	message(STATUS "Checking threading support through CMake...")

	# Locate system threads
	find_package(Threads)

	# Were specific pthreads found?
	if(Threads_FOUND AND CMAKE_USE_PTHREADS_INIT)
		# Debug
		message(STATUS "Checking of PThreads is valid through CMake...")

		# Does pthread actually exist, and is linkable in both forms?
		# Static first
		try_compile(tryPThreadStatic
			"${CMAKE_CURRENT_BINARY_DIR}"
			SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryPThread.c"
			CMAKE_FLAGS
				"-DCMAKE_TRY_COMPILE_TARGET_TYPE:STRING=STATIC_LIBRARY"
			LINK_LIBRARIES "${CMAKE_THREAD_LIBS_INIT}"
			OUTPUT_VARIABLE outputPThreadStatic)

		# Then executable, as -lpthread may be needed
		try_compile(tryPThreadExe
			"${CMAKE_CURRENT_BINARY_DIR}"
			SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryPThread.c"
			CMAKE_FLAGS
				"-DCMAKE_TRY_COMPILE_TARGET_TYPE:STRING=EXECUTABLE"
			LINK_LIBRARIES "${CMAKE_THREAD_LIBS_INIT}"
			OUTPUT_VARIABLE outputPThreadExe)

		# Debugging
		message(STATUS "PThread Valid (Static)? ${tryPThreadStatic}")
		message(STATUS "PThread Valid (Executable)? ${tryPThreadExe}")
		message(STATUS "PThread (Static): ${outputPThreadStatic}")
		message(STATUS "PThread (Executable): ${outputPThreadExe}")

		# Valid?
		if(tryPThreadExe AND tryPThreadStatic)
			message(STATUS "PThread: Valid!")

			# Include linkage of pthreads? Note that this may be blank if
			# the system provides pthreads
			set(SQUIRRELJME_LIBTHREADS "${CMAKE_THREAD_LIBS_INIT}")
			if("${CMAKE_THREAD_LIBS_INIT}" STREQUAL "")
				message(STATUS "PThread: Appears to be built-in!")
			endif()

			# Using pthreads for threading support
			add_compile_definitions(
				SJME_CONFIG_HAS_THREADS=1)
			add_compile_definitions(
				SJME_CONFIG_HAS_THREADS_PTHREAD=1)

			# Threading is valid
			set(SJME_CONFIG_ONLY_THREAD_SINGLE FALSE)

			# OS specific flavor of pthread?
			if ("${SQUIRRELJME_SYSTEM}" STREQUAL "linux")
				add_compile_definitions(
					SJME_CONFIG_HAS_THREADS_PTHREAD_LINUX=1)
			elseif ("${SQUIRRELJME_SYSTEM}" STREQUAL "macosx")
				add_compile_definitions(
					SJME_CONFIG_HAS_THREADS_PTHREAD_MACOS=1)
				add_compile_definitions(
					SJME_CONFIG_HAS_THREADS_PTHREAD_BSD=1)
			elseif ("${SQUIRRELJME_SYSTEM}" STREQUAL "bsd" OR
					"${SQUIRRELJME_SYSTEM}" STREQUAL "freebsd" OR
					"${SQUIRRELJME_SYSTEM}" STREQUAL "netbsd" OR
					"${SQUIRRELJME_SYSTEM}" STREQUAL "openbsd")
				add_compile_definitions(
					SJME_CONFIG_HAS_THREADS_PTHREAD_BSD=1)
			endif()

			# Older versions of glibc do not have pthread_kill() so determine
			# if a fallback can be used specifically for that
			# Use pthread
			set(CMAKE_REQUIRED_INCLUDES "${CMAKE_THREAD_INCLUDE}")
			set(CMAKE_REQUIRED_LIBRARIES "${CMAKE_THREAD_LIBS_INIT}")

			# Is there pthread_kill()?
			squirreljme_check_symbol_exists("pthread_kill" "signal.h"
				SJME_CONFIG_HAS_PTHREAD_KILL
				SJME_CONFIG_HAS_NO_PTHREAD_KILL)

			# Clear
			unset(CMAKE_REQUIRED_INCLUDES)
			unset(CMAKE_REQUIRED_LIBRARIES)
		else()
			# Debug
			message(STATUS "PThread: Not available or toolchain is broken.")

			# Force single threading
			set(SJME_CONFIG_ONLY_THREAD_SINGLE TRUE)
		endif()

	# CMake has no Threads package or it has a threading library that we do
	# not know about yet
	else()
		# Do not know what to do here other than disable threading support
		set(SJME_CONFIG_ONLY_THREAD_SINGLE TRUE)
	endif()
endif()

# No threading is supported at all
if(SJME_CONFIG_ONLY_THREAD_SINGLE)
	# Debug
	message(STATUS "No threading support was detected and/or enabled.")

	# Only a single thread is possible
	add_compile_definitions(SJME_CONFIG_ONLY_THREAD_SINGLE=1)

	# No library exists at all
	unset(SQUIRRELJME_LIBTHREADS)
else()
	message(STATUS "Threading support was enabled and detected!")
endif()
