import jenkins.model.*
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import hudson.plugins.git.GitSCM

def instance = Jenkins.getInstance()

def jobName = "flightsearchapi"
def job = instance.getItem(jobName)

if (job != null) {
    job.delete()
}

def pipelineJob = instance.createProject(org.jenkinsci.plugins.workflow.job.WorkflowJob, jobName)
def definition = new CpsScmFlowDefinition(
        new GitSCM(
                [
                        userRemoteConfigs: [[url: "https://github.com/Rapter1990/flightsearchapi.git"]],
                        branches: [[name: "*/main"]]
                ]
        ),
        "jenkins/Jenkinsfile"
)
definition.setLightweight(true)
pipelineJob.setDefinition(definition)
pipelineJob.save()

println("Pipeline job '${jobName}' başarıyla oluşturuldu!")
