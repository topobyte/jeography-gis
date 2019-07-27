package de.topobyte.gradle;

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.InvalidUserDataException

class ScriptsPlugin implements Plugin<Project> {

    void apply(Project project) {
        def extension = project.extensions.create("scripts", ScriptsExtension)

        project.task('postInstallScript') {
            doLast {
                if (extension.installationPath == null) {
                    throw new InvalidUserDataException("You need to specify the installationPath")
                }
                createPostInstallScript(project, extension)
            }
        }
    }

    def createPostInstallScript(project, extension) {
        def dir = new File("$project.buildDir/setup")
        def file = new File("$dir/post-install.sh")
        project.mkdir dir
        project.delete file
        file.append("#!/bin/bash\n")
        file.append("\n")
        file.append("mkdir -p ~/bin\n")
        file.append("\n")
        project.scriptNames.each {
            file.append("ln -fsr ~/share/$extension.installationPath/bin/$it ~/bin/$it\n")
        }
        project.exec { executable 'chmod' args '+x', file  }
    }

}
