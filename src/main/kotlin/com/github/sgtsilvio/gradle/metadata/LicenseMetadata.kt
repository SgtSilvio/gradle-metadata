package com.github.sgtsilvio.gradle.metadata

/**
 * @author Silvio Giebl
 */
interface LicenseMetadata {
    var shortName: String?
    var readableName: String?
    var url: String?

    fun apache2() {
        shortName = "Apache-2.0"
        readableName = "The Apache License, Version 2.0"
        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    }
}