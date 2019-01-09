githubOrg = System.getenv("TENANT_GITHUB_ORG")
credId = System.getenv("TENANT_GITHUB_CREDS_ID")
apiUri = System.getenv("TENANT_GITHUB_API_URL")
orgPipelineRepo = System.getenv("TENANT_GITHUB_CONFIG_URL")
libraryRepo = System.getenv("TENANT_GITHUB_LIBRARY_URL")

organizationFolder githubOrg, {
  configure{
    it / 'properties' << 'org.boozallen.plugins.jte.config.TemplateConfigFolderProperty' {
        tier {
            baseDir('./resources/sdp-org')
            scm (class: 'hudson.plugins.git.GitSCM'){
                configVersion 2
                userRemoteConfigs{
                  'hudson.plugins.git.UserRemoteConfig' {
                    url orgPipelineRepo
                    credentialsId credId
                  }
                }
                branches{
                  'hudson.plugins.git.BranchSpec'{
                    name "*/master"
                  }
                }
      		}
            librarySources{
                'org.boozallen.plugins.jte.config.TemplateLibrarySource'{
                    scm (class: 'hudson.plugins.git.GitSCM'){
                        configVersion 2
                        userRemoteConfigs{
                          'hudson.plugins.git.UserRemoteConfig' {
                            url libraryRepo
                            credentialsId credId
                          }
                        }
                        branches{
                          'hudson.plugins.git.BranchSpec'{
                            name "*/master"
                          }
                        }
      				      }
                }
            }
        }
    }
    it / 'navigators' << 'org.jenkinsci.plugins.github__branch__source.GitHubSCMNavigator'{
      repoOwner githubOrg
      credentialsId credId
      traits {
        'jenkins.scm.impl.trait.WildcardSCMSourceFilterTrait'{
          includes "sdp-example-proj"
        }
        'org.jenkinsci.plugins.github__branch__source.BranchDiscoveryTrait'{
          strategyId 1
        }
        'org.jenkinsci.plugins.github__branch__source.OriginPullRequestDiscoveryTrait'{
          strategyId 1
        }
      }
    }
    it / 'projectFactories' << 'org.boozallen.plugins.jte.job.TemplateMultiBranchProjectFactory' {
    }
  }
}
