# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: IEEE1275 Open Firmware Interface

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Only when actually using the IEEE1275 target
if(CMAKE_SYSTEM_NAME STREQUAL "ieee1275")
	# Setup executable
	add_library(SquirrelJMEIEEE1275Lib OBJECT
		boot.c
		devtree.c
		ieee1275.c
		io.c
		stdc.c
		)

	# This is written only in C!
	set_target_properties(SquirrelJMEIEEE1275Lib PROPERTIES LINKER_LANGUAGE C)

	# Position independent code always
	set_property(TARGET SquirrelJMEIEEE1275Lib
		PROPERTY POSITION_INDEPENDENT_CODE ${SQUIRRELJME_FPIC_MODE})

	# Includes for target
	target_include_directories(SquirrelJMEIEEE1275Lib PUBLIC
		"${PROJECT_BINARY_DIR}"
		"${PROJECT_SOURCE_DIR}/include")

	# Needed by platform specific targets
	if(SQUIRRELJME_BUNDLED_STDC)
		squirreljme_object_and_sources(SquirrelJMEIEEE1275LibTarget
			SquirrelJMEIEEE1275Lib
			SquirrelJMELibStdC)
	else()
		squirreljme_object_and_sources(SquirrelJMEIEEE1275LibTarget
			SquirrelJMEIEEE1275Lib)
	endif()
endif()

# Include directories for the build
if(CMAKE_SYSTEM_NAME STREQUAL "ieee1275")
	# PowerPC
	if (CMAKE_SYSTEM_PROCESSOR STREQUAL "powerpc")
		message("IEEE1275: PowerPC")
		add_subdirectory(powerpc)
	endif()

	# SPARC
	if (CMAKE_SYSTEM_PROCESSOR STREQUAL "sparc")
		message("IEEE1275: SPARC")
		add_subdirectory(sparc)
	endif()

	# x86
	if (CMAKE_SYSTEM_PROCESSOR STREQUAL "x86")
		message("IEEE1275: x86")
		add_subdirectory(x86)
	endif()
else()
	message("IEEE1275: Not in current build")

	# This is used by everything
	find_program(SQUIRRELJME_IEEE1275_POWERPC_GENISOIMAGE genisoimage)

	# Do these compilers exist?
	find_program(SQUIRRELJME_IEEE1275_POWERPC_COMPILER powerpc-elf-gcc)
	find_program(SQUIRRELJME_IEEE1275_SPARC_COMPILER sparc-elf-gcc)

	# Do these QEMUs exist?
	find_program(SQUIRRELJME_QEMU_POWERPC qemu-system-ppc)
	find_program(SQUIRRELJME_QEMU_POWERPC qemu-system-sparc)

	# PowerPC
	if(SQUIRRELJME_IEEE1275_POWERPC_COMPILER)
		# Otherwise recursive build for PowerPC
		message("Recursively including IEEE1275 PowerPC")
		execute_process(COMMAND "${CMAKE_COMMAND}"
				-G "Unix Makefiles"
				-S "${PROJECT_SOURCE_DIR}"
				-B "${PROJECT_BINARY_DIR}/ieee1275-powerpc"
				--toolchain "${CMAKE_CURRENT_SOURCE_DIR}/powerpc/ieee1275-powerpc.toolchain.cmake"
			COMMAND_ECHO STDERR
			COMMAND_ERROR_IS_FATAL ANY)

		# Then add custom target for output of that build
		add_custom_target(SquirrelJMEIEEE1275PowerPC
			COMMAND "${CMAKE_COMMAND}"
				--build "${PROJECT_BINARY_DIR}/ieee1275-powerpc"
				-j "${CMAKE_BUILD_PARALLEL_LEVEL}"
				-t "SquirrelJMEIEEE1275PowerPC"
			VERBATIM COMMAND_EXPAND_LISTS)

		# Create ISO Image
		if(SQUIRRELJME_IEEE1275_POWERPC_GENISOIMAGE)
			# Generate ISO Image
			add_custom_target(SquirrelJMEIEEE1275PowerPCIso
				COMMAND "${CMAKE_COMMAND}"
				--build "${PROJECT_BINARY_DIR}/ieee1275-powerpc"
				-j "${CMAKE_BUILD_PARALLEL_LEVEL}"
				-t "SquirrelJMEIEEE1275PowerPCIso"
				VERBATIM COMMAND_EXPAND_LISTS)

			# Capable of running in QEMU, requires ISO to be created first
			if(SQUIRRELJME_QEMU_POWERPC)
				add_custom_target(SquirrelJMEIEEE1275PowerPCQemu
					COMMAND "${CMAKE_COMMAND}"
					--build "${PROJECT_BINARY_DIR}/ieee1275-powerpc"
					-j "${CMAKE_BUILD_PARALLEL_LEVEL}"
					-t "SquirrelJMEIEEE1275PowerPCQemu"
					VERBATIM COMMAND_EXPAND_LISTS)

				# Do not run this when "all" is done
				set_target_properties(SquirrelJMEIEEE1275PowerPCQemu PROPERTIES
					EXCLUDE_FROM_DEFAULT_BUILD TRUE)
				set_target_properties(SquirrelJMEIEEE1275PowerPCQemu PROPERTIES
					EXCLUDE_FROM_ALL TRUE)
			endif()
		endif()
	else()
		message("No powerpc-elf-gcc found, not supporting IEEE1275 PowerPC.")
	endif()

	# SPARC
	if(SQUIRRELJME_IEEE1275_POWERPC_COMPILER)
		# Otherwise recursive build for Sparc
		message("Recursively including IEEE1275 Sparc")
		execute_process(COMMAND "${CMAKE_COMMAND}"
			-G "Unix Makefiles"
			-S "${PROJECT_SOURCE_DIR}"
			-B "${PROJECT_BINARY_DIR}/ieee1275-sparc"
			--toolchain "${CMAKE_CURRENT_SOURCE_DIR}/sparc/ieee1275-sparc.toolchain.cmake"
			COMMAND_ECHO STDERR
			COMMAND_ERROR_IS_FATAL ANY)

		# Then add custom target for output of that build
		add_custom_target(SquirrelJMEIEEE1275Sparc
			COMMAND "${CMAKE_COMMAND}"
			--build "${PROJECT_BINARY_DIR}/ieee1275-sparc"
			-j "${CMAKE_BUILD_PARALLEL_LEVEL}"
			-t "SquirrelJMEIEEE1275Sparc"
			VERBATIM COMMAND_EXPAND_LISTS)

		# Create ISO Image
		if(SQUIRRELJME_IEEE1275_POWERPC_GENISOIMAGE)
			# Generate ISO Image
			add_custom_target(SquirrelJMEIEEE1275SparcIso
				COMMAND "${CMAKE_COMMAND}"
				--build "${PROJECT_BINARY_DIR}/ieee1275-sparc"
				-j "${CMAKE_BUILD_PARALLEL_LEVEL}"
				-t "SquirrelJMEIEEE1275SparcIso"
				VERBATIM COMMAND_EXPAND_LISTS)

			# Capable of running in QEMU, requires ISO to be created first
			if(SQUIRRELJME_QEMU_POWERPC)
				add_custom_target(SquirrelJMEIEEE1275SparcQemu
					COMMAND "${CMAKE_COMMAND}"
					--build "${PROJECT_BINARY_DIR}/ieee1275-sparc"
					-j "${CMAKE_BUILD_PARALLEL_LEVEL}"
					-t "SquirrelJMEIEEE1275SparcQemu"
					VERBATIM COMMAND_EXPAND_LISTS)

				# Do not run this when "all" is done
				set_target_properties(SquirrelJMEIEEE1275SparcQemu PROPERTIES
					EXCLUDE_FROM_DEFAULT_BUILD TRUE)
				set_target_properties(SquirrelJMEIEEE1275SparcQemu PROPERTIES
					EXCLUDE_FROM_ALL TRUE)
			endif()
		endif()
	else()
		message("No sparc-elf-gcc found, not supporting IEEE1275 Sparc.")
	endif()
endif()
