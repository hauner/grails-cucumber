#require 'java'

#puts $CLASSPATH

module Grails

  class Formatter
    def initialize(step_mother, io, options)
      @step_mother, @io, @options = step_mother, io, options
    end

    def before_feature(feature)
      puts "GRAILS FORMATTER before_feature:(#{feature.title} (#{feature.file}))"

      $grails_formatter.before_feature(feature)

      #$GRAILS_EVENT_PUBLISHER.testCaseStart("#{feature.title}")
    end

    def feature_name(keyword, name)
      #puts "FORMATTER feature_name:(#{keyword}: #{name})"
    end

    def before_feature_element(feature_element)
      #puts "FORMATTER before_feature_element:(#{feature_element.title})"
      #$GRAILS_EVENT_PUBLISHER.testStart("#{feature_element.title}")
      #@scenario = feature_element.title
    end

    def scenario_name(keyword, name, file_colon_line, source_indent)
      #puts "FORMATTER scenario_name:(keyword:#{keyword} name:#{name})"
    end

    def before_steps(steps)
      #puts "FORMATTER before_steps:()"
    end

    def after_steps(steps)
      #if steps.failed?
        #puts "FORMATTER after_steps:(failed)"
       # $GRAILS_EVENT_PUBLISHER.testFailure(@scenario)
      #end
      #puts "FORMATTER after_steps:()"
    end

    def after_feature_element(feature_element)
      #puts "FORMATTER after_feature_element:(#{feature_element.title})"
      #$GRAILS_EVENT_PUBLISHER.testEnd("#{feature_element.title}")
    end

    def after_feature(feature)
      #puts "FORMATTER after_feature:(#{feature.title} (#{feature.file}))"
      #$GRAILS_EVENT_PUBLISHER.testCaseEnd("#{feature.title}")
    end


    def step_name(keyword, step_match, status, source_indent, background)
      #puts "FORMATTER step_name:(#{keyword})"
    end
  end
end


#before_features
#  before_feature
#    before_tags
#    after_tags
#    feature_name
#    before_feature_element
#      before_tags
#      after_tags
#      scenario_name
#      before_steps

#        before_step
#          before_step_result
#            step_name
#          after_step_result
#        after_step

#        before_step
#          before_step_result
#            step_name
#          after_step_result
#        after_step

#        before_step
#          before_step_result
#            step_name
#          after_step_result
#        after_step
#        before_step
#          before_step_result
#            step_name
#          after_step_result
#        after_step

#      after_steps
#   after_feature_element
#  after_feature
#after_features
