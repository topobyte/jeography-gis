package de.topobyte.gradle;

import org.gradle.jvm.application.tasks.CreateStartScripts
import org.gradle.api.file.DuplicatesStrategy

class ScriptsExtension {

    def createScript(project, mainClass, name) {
        project.tasks.create(name: name, type: CreateStartScripts) {
            outputDir = new File(project.buildDir, 'scripts')
            mainClassName = mainClass
            applicationName = name
            classpath = project.tasks.startScripts.classpath
            if (!project.hasProperty('scriptNames')) {
                project.ext.scriptNames = []
            }
            project.ext.scriptNames.add(name)
        }

        project.tasks[name].dependsOn(project.jar)

        project.distributions.main.contents.from(project.tasks[name]) {
             duplicatesStrategy = DuplicatesStrategy.EXCLUDE
             into 'bin'
        }
    }

}
