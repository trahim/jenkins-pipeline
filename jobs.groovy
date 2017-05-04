def getJobs() {
    def pipelineFile = readProperties file: "pipelines.txt"
    pipelines = pipelineFile['pipelines']
    pipelines.each {
        createJob($it)
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