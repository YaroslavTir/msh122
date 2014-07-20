package com.fls.metro.app.server

import com.fls.metro.api.servlet.JerseyServlet
import com.sun.jersey.spi.spring.container.servlet.SpringServlet
import groovy.util.logging.Slf4j
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.server.session.HashSessionManager
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.WebApplicationContext

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.servlet.DispatcherType

import static java.net.InetSocketAddress.createUnresolved

/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 16:47
 */
@Slf4j
class Jetty {
    Integer port
    String host
    List<RestServletConfiguration> restServlets
    List<FilterConfiguration> filters
    Boolean addStatic = Boolean.FALSE
    String staticResourcePath

    private Server server

    @Autowired
    private ApplicationContext applicationContext

    @PostConstruct
    def run() throws Exception {
        server = new Server(createUnresolved(host, port))
        server.handler = createHandler()
        server.start()
        log.info("Server started on ${host}:${port}")
    }

    @PreDestroy
    def stop() throws Exception {
        server.stop()
    }

    def createHandler() {
        def handlerList = new ContextHandlerCollection()
        if (addStatic) {
            def ctxHandler = new ContextHandler('/')
            ctxHandler.handler = new ResourceHandler(
                    resourceBase: staticResourcePath,
                    directoriesListed: true,
                    welcomeFiles: ['index.html'])
            handlerList.addHandler(ctxHandler)
        }
        ServletContextHandler restHandler = new ServletContextHandler()
        restHandler.addEventListener(new ContextLoaderListener(applicationContext as WebApplicationContext))
        filters.each {
            def filterHolder = new FilterHolder(it.filterClass)
            filterHolder.name = it.filterName
            filterHolder.initParameters = it.initParameters
            restHandler.addFilter(filterHolder, it.urlPattern, EnumSet.allOf(DispatcherType))
        }
        restServlets.each {
            def holder = new ServletHolder(new JerseyServlet(applicationContext))
            holder.setInitParameter(SpringServlet.CONTEXT_CONFIG_LOCATION, it.configLocation)
            it.params.each {
                holder.setInitParameter(it.key, it.value)
            }
            restHandler.addServlet(holder, it.contextPath)
        }
        restHandler.sessionHandler = new SessionHandler(new HashSessionManager())
        handlerList.addHandler(restHandler)
        return handlerList
    }
}
