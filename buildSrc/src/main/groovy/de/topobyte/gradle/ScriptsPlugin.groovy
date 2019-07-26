package de.topobyte.gradle;

import org.gradle.api.Project
import org.gradle.api.Plugin

class ScriptsPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.extensions.create("scripts", ScriptsExtension)

        project.task('postInstallScript') {
            doLast {
                createPostInstallScript(project)
            }
        }
    }

    def createPostInstallScript(project) {
        def dir = new File("$project.buildDir/setup")
        def file = new File("$dir/post-install.sh")
        project.mkdir dir
        project.delete file
        file.append("#!/bin/bash\n")
        file.append("\n")
        project.scriptNames.each {
            // TODO: remove hardcoded project name
            file.append("ln -fsr ~/share/topobyte/jeography-gis/bin/$it ~/bin/$it\n")
        }
        project.exec { executable 'chmod' args '+x', file  }
    }

}
