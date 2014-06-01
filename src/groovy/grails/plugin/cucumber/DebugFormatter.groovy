/*
 * Copyright 2011-2012, 2014 Martin Hauner
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

import gherkin.formatter.Formatter
import gherkin.formatter.Reporter
import gherkin.formatter.model.Feature
import gherkin.formatter.model.Background
import gherkin.formatter.model.Scenario
import gherkin.formatter.model.ScenarioOutline
import gherkin.formatter.model.Examples
import gherkin.formatter.model.Step
import gherkin.formatter.model.Match
import gherkin.formatter.model.Result


class DebugFormatter implements Formatter, Reporter {
    def sysout
    def pretty

    DebugFormatter (sysout, pretty) {
        this.sysout = sysout
        this.pretty = pretty
    }

    void finish () {
        sysout << "D(finish)\n"
    }

    CucumberTestTypeResult getResult () {
        sysout << "D(getResult)\n"

        new CucumberTestTypeResult (1, 0, 0)
    }

    /*
     * Formatter
     */

    void uri (String uri) {
        sysout << "\nF(uri): ($uri)\n"

        pretty.uri (uri)
    }

    void feature (Feature feature) {
        sysout << "F(feature): (${feature.getName ()})\n"

        pretty.feature (feature)
    }

    void background (Background background) {
        sysout << "F(background): (${background.getName ()})\n"
    }

    void scenario (Scenario scenario) {
        sysout << "F(scenario): (${scenario.getName ()})\n"

        pretty.scenario (scenario)
    }

    void scenarioOutline (ScenarioOutline scenarioOutline) {
        sysout << "F(scenarioOutline): (${scenarioOutline.getName ()})\n"
    }

    void examples (Examples examples) {
        sysout << "F(examples): (${examples.getName ()})\n"
    }

    @Override
    void startOfScenarioLifeCycle (Scenario scenario) {
        sysout << "F(startOfScenarioLifeCycle): (${scenario.name})\n"
    }

    void step (Step step) {
        sysout << "F(step): (${step.getName ()})\n"
    }

    @Override
    void endOfScenarioLifeCycle (Scenario scenario) {
        sysout << "F(endOfScenarioLifeCycle): (${scenario.name})\n"
    }

    void eof () {
        sysout << "F(eof)\n"
    }

    void syntaxError (String state, String event, List<String> legalEvents, String uri, Integer line) {
        sysout << "F(syntaxError)\n"
    }

    void done () {
        sysout << "F(done)\n"

        pretty.done ()
    }

    void close () {
        sysout << "F(close)\n"
        
        pretty.close ()
    }

    /*
     * Reporter
     */

    void before (Match match, Result result) {
        sysout << "R(before)"
    }

    void result (Result result) {
        sysout << "R(result):\n"
    }

    void after (Match match, Result result) {
        sysout << "R(after)"
    }

    void match (Match match) {
        sysout << "R(match)\n"

        pretty.match (match)
    }

    void embedding (String mimeType, byte[] data) {
        sysout << "R(embedding):\n"

        pretty.embedding (mimeType, data)
    }

    void write (String s) {
        syout << "R(write)\n"
        
        pretty.write (s)
    }

}
