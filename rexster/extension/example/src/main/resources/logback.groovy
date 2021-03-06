//
// Built on Thu Jul 14 00:43:34 CEST 2011 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport


import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.TRACE

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36}.%msg%n"
//    pattern = "[%thread] %-5level %logger{36}.%msg%n"
  }
}

root(INFO, ["STDOUT"])

logger("net.justtrade.rest.mowa", INFO)
logger("net.justtrade.rest.handlers.graph.BasePathManager", DEBUG)
logger("net.justtrade.rest.handlers.graph.ManagementIndexHelper", DEBUG)
logger("net.justtrade.rest.handlers.rdf.RDF_Loader", DEBUG)


