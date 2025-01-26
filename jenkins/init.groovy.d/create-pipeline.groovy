import hudson.plugins.git.UserRemoteConfig
import hudson.plugins.git.BranchSpec
import hudson.plugins.git.GitSCM
import jenkins.model.*
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition

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
                        new UserRemoteConfig("https://github.com/Rapter1990/flightsearchapi.git", null, null, null)
                ],
                [new BranchSpec("*/development/issue-2/implement-jenkins-for-ci-cd")],
                false, Collections.emptyList(),
                null, null, Collections.emptyList()
        ),
        "Jenkinsfile"
)
definition.setLightweight(true)
pipelineJob.setDefinition(definition)
pipelineJob.save()

println("Pipeline job '${jobName}' başarıyla oluşturuldu!")