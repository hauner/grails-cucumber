package world

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method


class Requests {

    def defaultSuccess = { response, data ->
        [response: response, data: data]
    }

    def defaultFailure = { response, data ->
        [response: response, data: data]
        assert false
    }

    def getJson (String targetUri, Closure success = null, Closure failure = null) {
        def http = new HTTPBuilder(binding.functionalBaseUrl)

        def result = http.request (Method.GET, ContentType.JSON) {
            uri.path = targetUri
//            headers.'X-Requested-With' = 'XMLHttpRequest'
//            headers.'Cookie' = cookies.join (';')
            response.success = success ?: defaultSuccess
            response.failure = failure ?: defaultFailure
        }
        result
    }

    def post (String targetUri, Map params, Closure success = null, Closure failure = null) {
        def http = new HTTPBuilder(binding.functionalBaseUrl)

        def result = http.request (Method.POST, ContentType.JSON) {
            uri.path = targetUri
//            headers.'X-Requested-With' = 'XMLHttpRequest'
//            headers.'Cookie' = cookies.join(';')
            requestContentType = ContentType.URLENC
            body = params
            response.success = success ?: defaultSuccess
            response.failure = failure ?: defaultFailure
        }
        result
    }
}
