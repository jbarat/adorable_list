package uk.co.jbarat.data.logger;

import dagger.Module;
import dagger.Provides;
import uk.co.jbarat.domain.logger.Logger;

@Module
class LoggerModule {

    @Provides
    Logger logger(DebugLogger debugLogger) {
        return debugLogger;
    }
}
