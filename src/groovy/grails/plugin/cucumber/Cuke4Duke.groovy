/*
 * Copyright 2011 Martin Hauner
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

package grails.plugin.cucumber


class Cuke4Duke {
    final File cukepath

    Cuke4Duke (File cukepath) {
        this.cukepath = cukepath
    }

    Reader reader () {
        InputStream stream = cukepath.newInputStream ()

        if (stream == null) {
            throw new FileNotFoundException (name ())
        }

        new InputStreamReader (stream)
    }

    String name () {
        cukepath.absolutePath
    }

            
    /*
    
    void run () {
        try {
            Reader reader = new File ("$homedir/bin/cuke4duke").newReader ();

            def ruby = new ScriptingContainer (LocalContextScope.THREADSAFE)
            //ruby.put("x", eventPublisher)
            //ruby.setLoadPaths ([
            //        "target/classes"
            //])

            ruby.setArgv ([
            "--require",
            "$basedir/target/classes",
            "--require",
            "$basedir/test/cucumber/support",
            "--require",
            "$basedir/src/test/groovy",
                //"-h",
                //"--format",
                //"Grails::TestFormatter",
                //"--format",
                //"junit",
                //"--out",
                //"$basedir/target/test-reports",
                "$basedir/test/cucumber"
            ] as String[])

            //java.util.Map env = new java.util.HashMap (ruby.getEnvironment ())
            //env.put ("GEM_PATH", homedir)
            //ruby.setEnvironment (env)

            def result
            //ruby.put('$GROOVY', 12345);
            //result = ruby.runScriptlet('puts $GROOVY.to_s(2)')
            //ruby.put('$GRAILS_EVENT_PUBLISHER', eventPublisher)

            //result = ruby.runScriptlet ("puts \"ENV: #{ENV.to_hash}\"")
            result = ruby.runScriptlet ("ENV['GEM_PATH'] = \"$homedir\"")
//            result = ruby.runScriptlet ("require 'java'")
//            result = ruby.runScriptlet ("\$CLASSPATH << \"$basedir/target/classes\"")
            result = ruby.runScriptlet ('puts "JRUBY CLASSPATH: <<#{$CLASSPATH}>>"')
            //result = ruby.runScriptlet ("puts \"ENV: #{ENV.to_hash}\"")
            result = ruby.runScriptlet (reader, "cuke4duke")
            result = ruby.runScriptlet ('puts "JRUBY CLASSPATH: <<#{$CLASSPATH}>>"')

        } catch (EvalFailedException e) {
            println "jruby says: ${e.getMessage ()}"
        } finally {
            println "jruby finally"
        }
    }
    */
}

