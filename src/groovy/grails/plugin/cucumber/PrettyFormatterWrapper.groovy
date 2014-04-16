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
import gherkin.formatter.PrettyFormatter


class PrettyFormatterWrapper implements Formatter, Reporter {
    PrettyFormatterFactory factory
    PrettyFormatter pretty
    String uri

    PrettyFormatterWrapper (PrettyFormatterFactory factory) {
        this.factory = factory
    }

    /*
     * Formatter
     */

    void uri (String uri) {
        this.uri = uri
    }

    void feature (Feature feature) {
        pretty = factory.createFormatter (System.out)

        pretty.uri (uri)
        pretty.feature (feature)
    }

    void background (Background background) {
        pretty.background (background)
    }

    void scenario (Scenario scenario) {
        pretty.scenario (scenario)
    }

    void scenarioOutline (ScenarioOutline scenarioOutline) {
        pretty.scenarioOutline (scenarioOutline)
    }

    void examples (Examples examples) {
        pretty.examples (examples)
    }

    @Override
    void startOfScenarioLifeCycle (Scenario scenario) {
        pretty.startOfScenarioLifeCycle (scenario)
    }

    void step (Step step) {
        pretty.step (step)
    }

    @Override
    void endOfScenarioLifeCycle (Scenario scenario) {
        pretty.endOfScenarioLifeCycle (scenario)
    }

    void eof () {
        pretty.eof ()
    }

    void syntaxError (String state, String event, List<String> legalEvents, String uri, Integer line) {
        pretty.syntaxError (state, event, legalEvents, uri, line)
    }

    void done () {
        pretty.done ()
    }

    void close () {
        pretty.close ()
    }

    /*
    * Reporter
    */

    void before (Match match, Result result) {
        pretty.before (match, result)
    }

    void result (Result result) {
        pretty.result (result)
    }

    void after (Match match, Result result) {
        pretty.after (match, result)
    }

    void match (Match match) {
        pretty.match (match)
    }

    void embedding (String mimeType, byte[] data) {
        pretty.embedding (mimeType, data)
    }

    void write (String text) {
        pretty.write (text)
    }

}
