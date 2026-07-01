# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Configures and builds natives via compiler.

# Final build step for any generator/compiler
macro(squirreljme_natives_build systemNormal archNormal configureRuleName
	buildRuleName buildPath method)
	# Where are all the packaged natives placed?
	set(packagePath
		"${buildPath}/bin/natives-${systemNormal}-${archNormal}.zip")

	# Make sure the build path exists
	file(MAKE_DIRECTORY "${buildPath}")

	# Setup rule to build
	add_custom_target(${buildRuleName}
		COMMAND "${CMAKE_COMMAND}"
			"--build" "${buildPath}"
			"--target" "libEmulatorBase"
		COMMAND "${CMAKE_COMMAND}"
			"--build" "${buildPath}"
			"--target" "bundleNatives"
		WORKING_DIRECTORY "${buildPath}"
		DEPENDS ${configureRuleName}
		BYPRODUCTS "${packagePath}"
		COMMENT "Building ${systemNormal}/${archNormal}..."
		COMMAND_EXPAND_LISTS)

	# Register the output path
	set_target_properties(${buildRuleName} PROPERTIES
		EXCLUDE_FROM_ALL YES
		ADDITIONAL_CLEAN_FILES "${packagePath}"
		SQUIRRELJME_OUTPUT_PATH "${packagePath}"
		SQUIRRELJME_OUTPUT_TYPE "natives")

	# Add to the order
	squirreljme_natives_append_rule(${buildRuleName}
		${systemNormal} ${archNormal} "${method}")
endmacro()

# Determine options to forward
squirreljme_env_forward(forwardedEnv)
message("Forwarded options: ${forwardedEnv}")

# Processes a compiler
macro(squirreljme_natives_compiler systemNormal archNormal compilerPath)
	# Determine the rule names
	unset(configureRuleName)
	unset(buildRuleName)
	squirreljme_compiler_rule_names(configureRuleName buildRuleName
		${systemNormal} ${archNormal} "compiler")

	# Determine the build path
	unset(buildPath)
	squirreljme_natives_build_path(buildPath ${systemNormal} ${archNormal})

	# Make sure it exists
	file(MAKE_DIRECTORY "${buildPath}")

	# Setup rule to configure
	if(squirreljme_bp_version_3_13)
		add_custom_target(${configureRuleName}
			COMMAND "${CMAKE_COMMAND}"
				"-DCC=${compilerPath}"
				"-DCMAKE_C_COMPILER=${compilerPath}"
				"${forwardedEnv}"
				"-B" "${buildPath}"
				"-S" "${CMAKE_SOURCE_DIR}/emulators/emulator-base-native"
			WORKING_DIRECTORY "${buildPath}"
			COMMENT "Configuring ${systemNormal}/${archNormal} (compiler)..."
			COMMAND_EXPAND_LISTS)
	else()
		add_custom_target(${configureRuleName}
			COMMAND "${CMAKE_COMMAND}"
				"-DCC=${compilerPath}"
				"-DCMAKE_C_COMPILER=${compilerPath}"
				"${forwardedEnv}"
				"${CMAKE_SOURCE_DIR}/emulators/emulator-base-native"
			WORKING_DIRECTORY "${buildPath}"
			COMMENT "Configuring ${systemNormal}/${archNormal} (compiler)..."
			COMMAND_EXPAND_LISTS)
	endif()

	# Do not build by default
	set_target_properties(${configureRuleName} PROPERTIES
		EXCLUDE_FROM_ALL YES)

	# The same build setup is used for any compiled target
	squirreljme_natives_build(${systemNormal} ${archNormal}
		${configureRuleName} ${buildRuleName} "${buildPath}" "compiler")
endmacro()

# Processes a generator
macro(squirreljme_natives_generator systemNormal archNormal
	generator toolset platform)
	# Truncate names
	squirreljme_truncate_name(truncGenerator "${generator}")
	squirreljme_truncate_name(truncToolset "${toolset}")
	squirreljme_truncate_name(truncPlatform "${platform}")

	# Determine a method
	set(method "${truncGenerator}-${truncToolset}-${truncPlatform}")
	set(method "generator.${method}")

	# Determine the rule names
	unset(configureRuleName)
	unset(buildRuleName)
	squirreljme_compiler_rule_names(configureRuleName buildRuleName
		${systemNormal} ${archNormal} "${method}")

	# Determine the build path
	unset(buildPath)
	squirreljme_natives_build_path(buildPath ${systemNormal} ${archNormal})

	# Make sure it exists
	file(MAKE_DIRECTORY "${buildPath}")

	# Determine generator arguments
	unset(generatorArgs)
	list(APPEND generatorArgs
		"-G" "${generator}")
	if(NOT "${toolset}" STREQUAL "none")
		list(APPEND generatorArgs
			"-T" "${toolset}")
	endif()
	if(NOT "${platform}" STREQUAL "none")
		list(APPEND generatorArgs
			"-A" "${platform}")
	endif()

	# Setup rule to configure
	if(squirreljme_bp_version_3_13)
		add_custom_target(${configureRuleName}
			COMMAND "${CMAKE_COMMAND}"
				"${generatorArgs}"
				"-B" "${buildPath}"
				"-S" "${CMAKE_SOURCE_DIR}/emulators/emulator-base-native"
			WORKING_DIRECTORY "${buildPath}"
			COMMENT "Configuring ${systemNormal}/${archNormal} (${method})..."
			COMMAND_EXPAND_LISTS)
	else()
		add_custom_target(${configureRuleName}
			COMMAND "${CMAKE_COMMAND}"
				"${generatorArgs}"
				"${CMAKE_SOURCE_DIR}/emulators/emulator-base-native"
			WORKING_DIRECTORY "${buildPath}"
			COMMENT "Configuring ${systemNormal}/${archNormal} (${method})..."
			COMMAND_EXPAND_LISTS)
	endif()

	# Do not build by default
	set_target_properties(${configureRuleName} PROPERTIES
		EXCLUDE_FROM_ALL YES)

	# The same build setup is used for any compiled target
	squirreljme_natives_build(${systemNormal} ${archNormal}
		${configureRuleName} ${buildRuleName} "${buildPath}" "${method}")
endmacro()

# Get list of compilers
get_property(compilerList GLOBAL PROPERTY
	SQUIRRELJME_KNOWN_COMPILERS)

# Add rules for each compiler
foreach(compilerMap IN ITEMS ${compilerList})
	# Split fields
	# "${systemNormal}!${archNormal}!${compilerPath}"
	squirreljme_unmap(systemNormal 0 "${compilerMap}")
	squirreljme_unmap(archNormal 1 "${compilerMap}")
	squirreljme_unmap(compilerPath 2 "${compilerMap}")

	# Process compiler
	squirreljme_natives_compiler(${systemNormal} ${archNormal}
		"${compilerPath}")
endforeach()

# Get list of generators
get_property(generatorList GLOBAL PROPERTY
	SQUIRRELJME_KNOWN_GENERATORS)

# Add rules for each generator
foreach(generatorMap IN ITEMS ${generatorList})
	# Split fields
	# "${systemNormal}!${archNormal}!${generator}!${toolset}!${platform}"
	squirreljme_unmap(systemNormal 0 "${generatorMap}")
	squirreljme_unmap(archNormal 1 "${generatorMap}")
	squirreljme_unmap(generator 2 "${generatorMap}")
	squirreljme_unmap(toolset 3 "${generatorMap}")
	squirreljme_unmap(platform 4 "${generatorMap}")

	# Process generator
	squirreljme_natives_generator(${systemNormal} ${archNormal}
		"${generator}" "${toolset}" "${platform}")
endforeach()
