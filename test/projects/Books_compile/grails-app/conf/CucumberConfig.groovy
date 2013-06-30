cucumber {
    tags = ["~@ignore"]

    // steps, hooks etc that will be compiled
    sources = ["test/cucumber"]

    // load compiled steps from classpath
    glue = ["classpath:steps", "classpath:hooks"]
}
