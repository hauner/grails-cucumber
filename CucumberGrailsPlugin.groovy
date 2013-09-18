/*
 * Copyright 2011-2013 Martin Hauner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class CucumberGrailsPlugin {
    def version = "0.10.0-SNAPSHOT"
    def grailsVersion = "1.3.0 > *"
    def dependsOn = [:]
    def pluginExcludes = [
        "**/.gitignore",
        "grails-app/**",
        "src/java/**",
        "web-app/**",
        "lib/spock-*"
    ]

    def author = "Martin Hauner"
    def authorEmail = "martin.hauner@gmx.net"
    def license = "APACHE"
    def title = "Cucumber Plugin"
    def description = "Test your Grails apps with Cucumber"
    def documentation = "https://github.com/hauner/grails-cucumber/wiki"
    def issueManagement = [
        system: "github", url: "https://github.com/hauner/grails-cucumber/issues"
    ]
    def scm = [
        url: "https://github.com/hauner/grails-cucumber"
    ]
}
