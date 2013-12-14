package grails.plugin.cucumber

import gherkin.formatter.Formatter
import gherkin.formatter.Reporter
import gherkin.formatter.model.Background
import gherkin.formatter.model.Examples
import gherkin.formatter.model.Feature
import gherkin.formatter.model.Match
import gherkin.formatter.model.Result
import gherkin.formatter.model.Scenario
import gherkin.formatter.model.ScenarioOutline
import gherkin.formatter.model.Step

/**
 * User: gcurrey
 * Date: 23/07/13
 * Time: 9:35 AM
 */
class DelegatingFormatter implements Formatter, Reporter{

    def delegates = []

    public void addFormatter(def formatter){
        delegates.add(formatter)
    }

    @Override
    void uri(String s) {
        delegates.each{ Formatter delegate ->
            delegate.uri(s)
        }
    }

    @Override
    void feature(Feature feature) {
        delegates.each{ Formatter delegate ->
            delegate.feature(feature)
        }
    }

    @Override
    void background(Background background) {
        delegates.each{ Formatter delegate ->
            delegate.background(background)
        }
    }

    @Override
    void scenario(Scenario scenario) {
        delegates.each{ Formatter delegate ->
            delegate.scenario(scenario)
        }
    }

    @Override
    void scenarioOutline(ScenarioOutline scenarioOutline) {
        delegates.each{ Formatter delegate ->
            delegate.scenarioOutline(scenarioOutline)
        }
    }

    @Override
    void examples(Examples examples) {
        delegates.each{ Formatter delegate ->
            delegate.examples(examples)
        }
    }

    @Override
    void step(Step step) {
        delegates.each{ Formatter delegate ->
            delegate.step(step)
        }
    }

    @Override
    void eof() {
        delegates.each{ Formatter delegate ->
            delegate.eof()
        }
    }

    @Override
    void syntaxError(String s, String s1, List<String> strings, String s2, Integer integer) {
        delegates.each{ Formatter delegate ->
            delegate.syntaxError(s,s1,strings,s2,integer)
        }
    }

    @Override
    void done() {
        delegates.each{ Formatter delegate ->
            delegate.done()
        }
    }

    @Override
    void close() {
        delegates.each{ Formatter delegate ->
            delegate.close()
        }
    }

    @Override
    void before(Match match, Result result) {
        delegates.each{ Reporter delegate ->
            delegate.before(match,result)
        }
    }

    @Override
    void result(Result result) {
        delegates.each{ Reporter delegate ->
            delegate.result(result)
        }
    }

    @Override
    void after(Match match, Result result) {
        delegates.each{ Reporter delegate ->
            delegate.after(match,result)
        }
    }

    @Override
    void match(Match match) {
        delegates.each{ Reporter delegate ->
            delegate.match(match)
        }
    }

    @Override
    void embedding(String s, byte[] bytes) {
        delegates.each{ Reporter delegate ->
            delegate.embedding(s,bytes)
        }
    }

    @Override
    void write(String s) {
        delegates.each{ Reporter delegate ->
            delegate.write(s)
        }
    }
}
