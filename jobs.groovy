def getJobs() {
    def pipelineFile = readProperties file: "pipelines.txt"
    pipelines = pipelineFile['pipelines']
    for (int i=0; i<pipelines.size(); i++) {
        def name = pipelines[i]
        createJob(name)
        build job: name
    }
}

def createJob(name){
    step([
            $class: "ExecuteDslScripts",
            lookupStrategy: "SEED_JOB",
            scriptText: """
                pipelineJob("${name}") {
                    definition {
                        cpsScm {
                            scm {
                                git {
                                    remote {
                                        url("${gitUrl}")
                                        branch("${gitBranch}")
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