/* grails-cucumber configuration */

cucumber {
    tags = ["~@ignore"]

    features = ["test/functional"]
    glue = features
    formatter = "json:target/cucumber.json"
}
