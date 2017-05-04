def getJobs(String environment) {
    def pipelineFile = readProperties file: "pipelines.txt"
    pipelines = pipelineFile['pipelines'].tokenize(',')
    for (int i=0; i<pipelines.size(); i++) {
        def name = pipelines[i]
        createJob(name, environment)
        build job: name
    }
}

def createJob(String name, environment){
    step([
            $class: "ExecuteDslScripts",
            lookupStrategy: "SEED_JOB",
            scriptText: """
                folder("$environment)")
                pipelineJob("${name}") {
                    definition {
                        cpsScm {
                            scm {
                                git {
                                    remote {
                                        url("${gitUrl}")
                                        branch("master")
                                    }
                                }
                            }
                            scriptPath("${name}")
                        }
                    }
                }
            """
    ])
}
return this