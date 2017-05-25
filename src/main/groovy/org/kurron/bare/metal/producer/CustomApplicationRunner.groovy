package org.kurron.bare.metal.producer

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

import java.util.concurrent.ThreadLocalRandom

/**
 * Handles command-line arguments.
 */
@Slf4j
class CustomApplicationRunner implements ApplicationRunner {

    /**
     * Handles AMQP communications.
     */
    @Autowired
    private MongoTemplate theTemplate

    @Autowired
    private ConfigurableApplicationContext theContext

    @Autowired
    private ApplicationProperties theConfiguration

    private static UUID generateModelID() {
        UUID.randomUUID()
    }

    private static Date generateTimeStamp() {
        Calendar.getInstance( TimeZone.getTimeZone('UTC') ).time
    }

    private static void randomize(byte[] buffer) {
        ThreadLocalRandom.current().nextBytes(buffer)
    }

    private static Model createModel( byte[] payload ) {
        new Model( primaryKey: generateModelID(), timestamp: generateTimeStamp(),  randomBytes: payload )
    }

    @Override
    void run(ApplicationArguments arguments) {

        def messageCount = Optional.ofNullable(arguments.getOptionValues('number-of-messages')).orElse(['100'])
        def messageSize = Optional.ofNullable(arguments.getOptionValues('payload-size')).orElse(['1024'])

        long start = System.currentTimeMillis()
        def criteria = new Criteria().where( '_id' ).exists( true )
        def stream = theTemplate.stream( new Query( criteria ), Model )
        long totalBytes = 0
        int totalDocuments = 0
        def tracker = { Model model -> totalDocuments++ ; totalBytes += model.randomBytes.size() }
        stream.forEachRemaining( tracker )
        long stop = System.currentTimeMillis()

        long duration = stop - start
        log.info('Read {} messages in {} milliseconds for a total payload of {} bytes', totalDocuments, duration, totalBytes )

        log.info 'Query complete'
        theContext.close()
    }
}
