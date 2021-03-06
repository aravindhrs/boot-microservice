package com.ofg.infrastructure.discovery

import com.ofg.infrastructure.discovery.watcher.presence.checker.NoInstancesRunningException
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

import static org.codehaus.groovy.runtime.StackTraceUtils.extractRootCause

class ApplicationContextStartupSpec extends Specification {    
        
    public static final String CONTEXT_FOR_ZOOKEEPER_WITHOUT_STUBS_PROFILE = 'context for zookeeper without stubs'
    
    def 'should fail to start application context if resource is missing'() {
        given:
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext()
            applicationContext.environment.setActiveProfiles(CONTEXT_FOR_ZOOKEEPER_WITHOUT_STUBS_PROFILE)
            applicationContext.register(ApplicationContextWithoutStubsConfiguration, ServiceResolverConfiguration)
        when:            
            applicationContext.refresh()
        then:    
            Throwable thrown = thrown(Throwable)
            extractRootCause(thrown).class == NoInstancesRunningException
        cleanup:
            applicationContext.close()
    }
         
}
